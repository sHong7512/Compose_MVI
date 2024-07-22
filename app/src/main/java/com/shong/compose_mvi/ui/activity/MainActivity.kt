package com.shong.compose_mvi.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.shong.compose_mvi.ui.composable.time.TimePreview
import com.shong.compose_mvi.ui.composable.time.TimeView
import com.shong.compose_mvi.ui.composable.counter.CounterPreview
import com.shong.compose_mvi.ui.composable.counter.CounterView
import com.shong.compose_mvi.ui.composable.log.LogPreview
import com.shong.compose_mvi.ui.composable.log.LogView
import com.shong.compose_mvi.ui.theme.Compose_MVITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Content(contents = listOf({ CounterView() }, { TimeView() }, { LogView() }))

        }
    }
}

@Composable
private fun Content(contents: List<@Composable () -> Unit>) {
    Compose_MVITheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
        ) { basePadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(basePadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(basePadding),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    contents[0].invoke()
                    contents[1].invoke()
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    contents[2].invoke()
                }
            }
        }
    }
}

@Preview
@Composable
fun MainPreview() {
    Content(
        contents = listOf({ CounterPreview() }, { TimePreview() }, { LogPreview() })
    )
}