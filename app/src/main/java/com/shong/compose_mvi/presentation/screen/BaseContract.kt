package com.shong.compose_mvi.presentation.screen

// ViewModel ➡️ UI (2가지 방식)
// UI가 어떻게 보아야 하는지에 대한 지속적인 데이터
interface UiState

// UI ➡️ ViewModel (단방향)
interface UiEvent

// ViewModel ➡️ UI (2가지 방식)
// 단발성 이벤트 전파
interface UiEffect