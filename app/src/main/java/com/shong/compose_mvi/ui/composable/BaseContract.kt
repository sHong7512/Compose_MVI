package com.shong.compose_mvi.ui.composable

// 현 상태의 view
// 화면의 UI를 구성하기 위해 필요한 데이터를 모아놓는 data class이다.
// 홈 화면에서 Loading 상태, 네트워크 에러상태, 데이터 호출에 실패한 상태, 데이터를 성공적으로 호출한 상태로 이루어져 있을 수도 있다.
interface UiState

// 사용자의 액션
interface UiEvent

// Side Effect
// 단발성 이벤트를 전파하기 위한 side Effect의 모음이다.
// 실제로는 API 호출, 네비게이션 등등이 여기에 정의될 수 있다.
interface UiEffect