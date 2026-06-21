package com.teamyoga.yoghee.feature.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FloatingBottomNavigation(
    isLoggedIn: Boolean,
    onGoSearch: () -> Unit,
    onGoProfile: () -> Unit,
    onGoLogin: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            TextButton(onClick = onGoSearch) { Text("검색") }
            TextButton(onClick = onGoProfile) { Text("내정보") }
            // 로그인 상태에 따라 버튼이 토글된다.
            // - 비로그인: 로그인 화면으로 이동
            // - 로그인 중: 토큰 삭제 → authState 전환 → 화면 자동 리프레시
            if (isLoggedIn) {
                TextButton(onClick = onLogout) { Text("로그아웃") }
            } else {
                TextButton(onClick = onGoLogin) { Text("로그인") }
            }
        }
    }
}
