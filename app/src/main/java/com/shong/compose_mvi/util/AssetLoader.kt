package com.shong.compose_mvi.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipInputStream

// main/assets 데이터 사용 (더미데이터 및 테스트 데이터)
object AssetLoader {
    inline fun <reified T> getJson(context: Context): T {
        val jsonString = try {
            context.assets.open("${T::class.simpleName}.json").bufferedReader()
                .use { it.readText() }
        } catch (e: IOException) {
            logE("$e")
            ""
        }

        val gson = Gson()
        val listProductType = object : TypeToken<T>() {}.type
        return gson.fromJson(jsonString, listProductType)
    }

    fun unzipAsset(context: Context, zipFileName: String): String {
        // 압축 파일 이름에서 확장자(.zip)를 제거하여 폴더 이름으로 사용
        val destDirName = zipFileName.removeSuffix(".zip")
        val destDir = File(context.cacheDir, destDirName)

        // 만약 이전에 압축 해제한 폴더가 있다면 삭제하여 항상 새로운 파일 사용을 보장
        if (destDir.exists()) {
            destDir.deleteRecursively()
        }
        destDir.mkdirs()

        try {
            // assets에서 zip 파일을 열고 ZipInputStream으로 감싸기
            context.assets.open(zipFileName).use { inputStream ->
                ZipInputStream(inputStream).use { zipInputStream ->
                    var zipEntry = zipInputStream.nextEntry

                    // zip 파일 내의 모든 entry(파일/폴더)를 순회
                    while (zipEntry != null) {
                        val newFile = File(destDir, zipEntry.name)

                        // Path Traversal 공격 방지
                        if (!newFile.canonicalPath.startsWith(destDir.canonicalPath + File.separator)) {
                            throw SecurityException("Zip Path Traversal Vulnerability")
                        }

                        if (zipEntry.isDirectory) {
                            // 디렉터리인 경우
                            if (!newFile.exists()) {
                                newFile.mkdirs()
                            }
                        } else {
                            // 파일인 경우
                            // 상위 디렉터리가 없을 경우 생성
                            val parentDir = newFile.parentFile
                            if (parentDir != null && !parentDir.exists()) {
                                parentDir.mkdirs()
                            }

                            // 파일 스트림을 통해 데이터 복사
                            FileOutputStream(newFile).use { fos ->
                                zipInputStream.copyTo(fos)
                            }
                        }
                        zipInputStream.closeEntry()
                        zipEntry = zipInputStream.nextEntry
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return destDir.absolutePath
    }

    fun copyAssetFolder(context: Context, assetDir: String): String {
        val destDir = File(context.cacheDir, assetDir)
        if (!destDir.exists()) {
            destDir.mkdirs()
        }

        copyAssets(context, assetDir, destDir.absolutePath)
        return destDir.absolutePath
    }

    private fun copyAssets(context: Context, assetPath: String, destPath: String) {
        try {
            val files = context.assets.list(assetPath) ?: return
            files.forEach { filename ->
                val currentAssetPath = if (assetPath.isEmpty()) filename else "$assetPath/$filename"
                val subFiles = context.assets.list(currentAssetPath)

                if (subFiles.isNullOrEmpty()) { // 파일인 경우
                    val outFile = File(destPath, filename)
                    context.assets.open(currentAssetPath).use { inputStream ->
                        FileOutputStream(outFile).use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                } else { // 디렉토리인 경우
                    val subDir = File(destPath, filename)
                    if (!subDir.exists()) {
                        subDir.mkdirs()
                    }
                    copyAssets(context, currentAssetPath, subDir.absolutePath)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}