package com.teamyoga.yoghee.feature.registerClass

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeHeader
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.LAND_BROWN
import com.teamyoga.yoghee.core.ui.theme.SAND_BEIGE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.core.ui.util.noRippleClickable
import com.teamyoga.yoghee.feature.registerClass.components.ClassIntroductionModule

private const val TOTAL_STEPS = 7

@Composable
fun OneDayClassRegisterScreen(
    typeIndex: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentStep by remember { mutableIntStateOf(1) }

    val goPrevious: () -> Unit = {
        if (currentStep == 1) onBack() else currentStep--
    }
    val goNext: () -> Unit = {
        if (currentStep < TOTAL_STEPS) currentStep++
    }

    BackHandler(onBack = goPrevious)    // back key 버튼 이벤트를 가로채는 API

    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(SAND_BEIGE),
    ) {
        Box(modifier = Modifier.weight(1f)) {
            when (currentStep) {
                1 -> Step1Content(onBack = goPrevious)
                else -> StepPlaceholderContent(step = currentStep, onBack = goPrevious)
            }
        }
        RegisterBottomBar(
            currentStep = currentStep,
            totalSteps = TOTAL_STEPS,
            onPrevious = goPrevious,
            onNext = goNext,
        )
    }
}

@Composable
private fun Step1Content(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        YogheeHeader(
            title = stringResource(R.string.one_day_class_register_step1_title),
            onBack = onBack,
            subTitle = stringResource(R.string.inquire),
            onSubTitleClick = {},
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ClassIntroductionModule()
        }
    }
}

@Composable
private fun StepPlaceholderContent(
    step: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        YogheeHeader(
            title = "Step $step",
            onBack = onBack,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            YogheeText(
                text = "Step $step (준비 중)",
                color = BLACK,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun RegisterBottomBar(
    currentStep: Int,
    totalSteps: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SAND_BEIGE)
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        ProgressBar(currentStep = currentStep, totalSteps = totalSteps)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            YogheeText(
                text = stringResource(R.string.previous_page),
                color = GRAY,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(116.dp).noRippleClickable(onPrevious),
            )

            Box(
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f)
                    .paint(
                        painter = painterResource(R.drawable.btn_continue_class_register),
                        contentScale = ContentScale.FillBounds,
                    )
                    .noRippleClickable(onNext),
                contentAlignment = Alignment.Center,
            ) {
                YogheeText(
                    text = stringResource(R.string.continue_page),
                    color = BLACK,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ProgressBar(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(currentStep.toFloat() / totalSteps.toFloat())
                .height(2.dp)
                .background(LAND_BROWN),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(GRAY),
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "OneDayClassRegisterScreen")
@Composable
private fun OneDayClassRegisterScreenPreview() {
    YogheeTheme {
        OneDayClassRegisterScreen(typeIndex = 0, onBack = {})
    }
}

@Preview(showBackground = true, name = "Step1Content")
@Composable
private fun Step1ContentPreview() {
    YogheeTheme {
        Box(modifier = Modifier.background(SAND_BEIGE)) {
            Step1Content(onBack = {})
        }
    }
}

@Preview(showBackground = true, name = "StepPlaceholderContent")
@Composable
private fun StepPlaceholderContentPreview() {
    YogheeTheme {
        Box(modifier = Modifier.background(SAND_BEIGE)) {
            StepPlaceholderContent(step = 3, onBack = {})
        }
    }
}