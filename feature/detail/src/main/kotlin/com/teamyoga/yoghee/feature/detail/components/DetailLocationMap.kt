package com.teamyoga.yoghee.feature.detail.components

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.teamyoga.yoghee.core.domain.model.ClassCenter
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.MIND_ORANGE
import com.teamyoga.yoghee.core.ui.theme.WHITE

private const val MAP_ZOOM_LEVEL = 17 // 건물 수준 — 레퍼런스 이미지와 유사한 축척

/**
 * 위치 섹션 — Kakao Maps 로 센터 좌표를 중앙에 두고, Compose 오버레이로 핀/라벨을 그린다.
 * - 지도는 모든 제스처를 끈 상태 (요구사항: 정적 이미지처럼 동작).
 * - lat/lng 둘 다 0.0 이면 유효 좌표 아님으로 간주, 섹션 전체 숨김.
 */
@Composable
fun DetailLocationMap(
    title: String,
    center: ClassCenter?,
    modifier: Modifier = Modifier,
) {
    if (center == null) return
    if (center.latitude == 0.0 && center.longitude == 0.0) return

    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(
            thickness = 0.5.dp,
            color = GRAY,
            modifier = Modifier.padding(top = 32.dp, bottom = 32.dp, start = 16.dp, end = 16.dp),
        )
        RoundBorderTitle(text = title, modifier = Modifier.padding(start = 16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .aspectRatio(343f / 245f)
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center,
        ) {
            KakaoStaticMap(
                latitude = center.latitude,
                longitude = center.longitude,
                modifier = Modifier.fillMaxSize(),
            )
            CenterPinLabel(label = center.name)
        }
        YogheeText(
            text = center.description,
            fontSize = 12.sp,
            lineHeight = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 16.dp, start = 24.dp, end = 9.dp)
        )
    }
}

@Composable
private fun KakaoStaticMap(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    // MapView 는 수동으로 라이프사이클을 start/pause/resume/finish 해줘야 메모리 누수가 없다.
    val mapView = remember(latitude, longitude) {
        MapView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            start(
                object : MapLifeCycleCallback() {
                    override fun onMapDestroy() = Unit
                    override fun onMapError(error: Exception?) = Unit
                },
                object : KakaoMapReadyCallback() {
                    override fun onMapReady(kakaoMap: KakaoMap) {
                        kakaoMap.moveCamera(
                            CameraUpdateFactory.newCenterPosition(
                                LatLng.from(latitude, longitude),
                                MAP_ZOOM_LEVEL,
                            ),
                        )
                    }

                    override fun getPosition(): LatLng = LatLng.from(latitude, longitude)
                    override fun getZoomLevel(): Int = MAP_ZOOM_LEVEL
                },
            )
        }
    }

    DisposableEffect(mapView) {
        mapView.resume()
        onDispose {
            mapView.pause()
            mapView.finish()
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier,
    )
}

@Composable
private fun CenterPinLabel(
    label: String,
    modifier: Modifier = Modifier,
) {
    val bubble = remember { SpeechBubbleShape() }
    Text(
        text = label,
        color = WHITE,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = modifier
            .background(color = MIND_ORANGE, shape = bubble)
            // 아래 tailHeight 만큼 여유를 둬야 글자가 뾰족이 영역을 침범하지 않는다.
            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp + SpeechBubbleShape.TAIL_HEIGHT),
    )
}

/**
 * 라운드 사각형 몸체 + 하단 중앙 뾰족이로 이뤄진 말풍선 Shape.
 * - 크기(가로/세로)에 따라 자동으로 늘어나기 때문에 .9.png 대신 사용하기 좋다.
 * - 뾰족이 꼭짓점이 Shape 의 bottom-center 가 되므로 핀 포인터가 지도 상 "정확한 위치"를 가리킨다.
 */
private class SpeechBubbleShape(
    private val cornerRadius: Dp = 16.dp,
    private val tailWidth: Dp = 10.dp,
    private val tailHeight: Dp = TAIL_HEIGHT,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline = with(density) {
        val cr = cornerRadius.toPx()
        val tw = tailWidth.toPx()
        val th = tailHeight.toPx()
        val bodyBottom = size.height - th
        val cx = size.width / 2f
        val path = Path().apply {
            moveTo(cr, 0f)
            lineTo(size.width - cr, 0f)
            arcTo(Rect(size.width - 2 * cr, 0f, size.width, 2 * cr), -90f, 90f, false)
            lineTo(size.width, bodyBottom - cr)
            arcTo(Rect(size.width - 2 * cr, bodyBottom - 2 * cr, size.width, bodyBottom), 0f, 90f, false)
            lineTo(cx + tw / 2f, bodyBottom)
            lineTo(cx, size.height)
            lineTo(cx - tw / 2f, bodyBottom)
            lineTo(cr, bodyBottom)
            arcTo(Rect(0f, bodyBottom - 2 * cr, 2 * cr, bodyBottom), 90f, 90f, false)
            lineTo(0f, cr)
            arcTo(Rect(0f, 0f, 2 * cr, 2 * cr), 180f, 90f, false)
            close()
        }
        Outline.Generic(path)
    }

    companion object {
        val TAIL_HEIGHT = 6.dp
    }
}
