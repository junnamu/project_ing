package com.example.comp_nav_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.comp_nav_test.ui.theme.Comp_nav_testTheme
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.coroutines.delay


class ScoreViewModel : ViewModel() {
    private val answerScoreMap = mutableMapOf<Int, Int>() // 답변 인덱스를 키로 사용하여 점수 저장
    private val selectedAnswerIndexMap = mutableMapOf<Int, Int>()
    private val selectedAnswerCountMap = mutableMapOf<Int, Int>()
    private val selectedAnswerTextMap = mutableMapOf<Int, String>()

    fun saveAnswerScore(answerIndex: Int, score: Int) {
        answerScoreMap[answerIndex] = score
    }

    fun getAnswerScore(answerIndex: Int): Int {
        return answerScoreMap[answerIndex] ?: 0
    }

    fun clearAnswerScore(answerIndex: Int) {
        answerScoreMap.remove(answerIndex)
    }

    fun saveSelectedAnswerIndex(answerIndex: Int, selectedIndex: Int) {
        selectedAnswerIndexMap[answerIndex] = selectedIndex
    }

    fun getSelectedAnswerIndex(answerIndex: Int): Int? {
        return selectedAnswerIndexMap[answerIndex]
    }

    fun saveSelectedAnswerText(answerIndex: Int, answerText: String) {
        selectedAnswerTextMap[answerIndex] = answerText
    }

    fun getSelectedAnswerText(answerIndex: Int): String? {
        return selectedAnswerTextMap[answerIndex]
    }

    // New function to get all answer scores
    fun getAnswerScores(): List<Int> {
        val answerScores = mutableListOf<Int>()
        for (index in 0 until MAX_QUESTION_INDEX) {
            answerScores.add(getAnswerScore(index))
        }
        return answerScores
    }

    // New function to get the count of selected answers for each choice
    fun getSelectedAnswerCounts(choiceIndex: Int): Int {
        return selectedAnswerCountMap[choiceIndex] ?: 0
    }

    fun incrementSelectedAnswerCount(choiceIndex: Int) {
        val currentCount = selectedAnswerCountMap.getOrDefault(choiceIndex, 0)
        selectedAnswerCountMap[choiceIndex] = currentCount + 1
    }

    companion object {
        const val MAX_QUESTION_INDEX = 6
    }
}

data class Answer(val text: String, var isSelected: Boolean = false)

class MainActivity : ComponentActivity() {
    private val scoreViewModel: ScoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Comp_nav_testTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(scoreViewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(scoreViewModel: ScoreViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = "q1"
    ) {
        composable("title") { Title(navController) }
        composable("q1") { Q1(navController, scoreViewModel) }
        composable("q2") { Q2(navController, scoreViewModel) }
        composable("q3") { Q3(navController, scoreViewModel) }
        composable("q4") { Q4(navController, scoreViewModel) }
        composable("q5") { Q5(navController, scoreViewModel) }
        composable("results") {
            ResultsPage(scoreViewModel = scoreViewModel)
        }
    }
}

@Composable
fun Title(navController: NavController) {
    val isVisible = remember { mutableStateOf(true) }

    LaunchedEffect(isVisible) {
        while (true) {
            delay(600)
            isVisible.value = !isVisible.value
        }
    }

    val customFont = FontFamily(
        Font(R.font.cafe24_regular, FontWeight.Normal), Font(R.font.cafe24_bold, FontWeight.Bold)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                isVisible.value = !isVisible.value
                navController.navigate("q1")
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "MBTI 유형 검사",
            fontSize = 50.sp,
            fontFamily = customFont,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(1.dp))
        Text(
            text = "&",
            fontSize = 50.sp,
            fontFamily = customFont,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(1.dp))
        Text(
            text = "유형별 재테크 추천",
            fontSize = 50.sp,
            fontFamily = customFont,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        AnimatedVisibility(
            visible = isVisible.value, enter = fadeIn() + expandIn(), exit = shrinkOut()
        ) {
            Text(
                text = "Click",
                fontSize = 35.sp,
                color = Color.DarkGray,
                modifier = Modifier
                    .alpha(0.5f)
                    .animateContentSize(),
                fontWeight = FontWeight.Bold,
                fontFamily = customFont
            )
        }
    }
}

private fun calculateScore(selectedAnswer: Answer, answerOptions: List<Answer>): Int {
    return when (selectedAnswer) {
        answerOptions[0] -> 20
        answerOptions[1] -> 20
        else -> 0
    }
}

@Composable
fun QuestionPageContent(
    answerIndex: Int,
    question: String,
    answerOptions: List<Answer>,
    scoreViewModel: ScoreViewModel,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
) {
    val selectedAnswerIndex = remember { mutableStateOf(-1) }
    val selectedAnswer = remember { mutableStateOf<Answer?>(null) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = question,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        answerOptions.forEachIndexed { index, answer ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        selectedAnswerIndex.value = index // 선택한 답변의 인덱스를 저장
                        selectedAnswer.value = answer
                        scoreViewModel.saveSelectedAnswerIndex(answerIndex, index) // 선택한 답변의 인덱스 저장
                        scoreViewModel.incrementSelectedAnswerCount(index) // 해당 선택지 카운트 증가
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = answer == selectedAnswer.value,
                    onClick = {
                        selectedAnswer.value = answer
                    })
                Text(
                    text = answer.text,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onPreviousClicked) {
                Text(text = "이전")
            }

            Button(
                onClick = {
                    selectedAnswer.value?.let { answer ->
                        // Save or clear previous answer score depending on navigation direction
                        if (answerIndex > 1) {
                            val previousScore = scoreViewModel.getAnswerScore(answerIndex - 1)
                            if (previousScore != 0) {
                                // Use previous score if available
                                scoreViewModel.saveAnswerScore(answerIndex, previousScore)
                            } else {
                                // Clear previous score if not available
                                scoreViewModel.clearAnswerScore(answerIndex)
                            }
                        }

                        val score = calculateScore(answer, answerOptions)
                        scoreViewModel.saveAnswerScore(answerIndex, score)
                        scoreViewModel.saveSelectedAnswerText(answerIndex, answer.text) // Save selected answer text
                        onNextClicked()
                    }
                },
                enabled = selectedAnswer.value != null
            ) {
                Text(text = "다음")
            }
        }

        // Display selected answer and its score
        selectedAnswer.value?.let { selected ->
            val score = scoreViewModel.getAnswerScore(answerIndex)
            Text(
                text = "Selected Answer: ${selected.text}\nScore: $score",
                modifier = Modifier.padding(top = 16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}


@Composable
fun Q1(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 1

    val answerOptions = remember {
        listOf(
            Answer("Blue"),
            Answer("Red")
        )
    }

    val questionText = "What is your favorite color?"

    QuestionPageContent(
        answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        onNextClicked = {
            navController.navigate("q2")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        }
    )
}

@Composable
fun Q2(navController: NavController, scoreViewModel: ScoreViewModel) { // scoreViewModel 전달 추가
    val answerIndex = 2

    val answerOptions = remember {
        listOf(
            Answer("Option A"),
            Answer("Option B")
        )
    }

    val questionText = "Choose an option"

    QuestionPageContent(
        answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가

        onNextClicked = {
            navController.navigate("q3")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        }
    )
}

@Composable
fun Q3(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 3

    val answerOptions = remember {
        listOf(
            Answer("Option X"),
            Answer("Option Y")
        )
    }

    val questionText = "Another question"

    QuestionPageContent(
        answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
        onNextClicked = {
            navController.navigate("q4")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        }
    )
}

@Composable
fun Q4(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 4

    val answerOptions = remember {
        listOf(
            Answer("Option P"),
            Answer("Option Q")
        )
    }

    val questionText = "Yet another question"

    QuestionPageContent(
        answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
        onNextClicked = {
            navController.navigate("q5")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        }
    )
}

@Composable
fun Q5(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 5

    val answerOptions = remember {
        listOf(
            Answer("Choice X"),
            Answer("Choice Y")
        )
    }

    val questionText = "Last question"

    QuestionPageContent(
        answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
        onNextClicked = {
            navController.navigate("results")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        }
    )
}


@Composable
fun ResultsPage(scoreViewModel: ScoreViewModel) {
    val answerScores = scoreViewModel.getAnswerScores()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Text(
            text = "Results",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val answerChoiceCounts = mutableMapOf<Int, Int>()

        for ((index, score) in answerScores.withIndex()) {
            val selectedAnswerIndex = scoreViewModel.getSelectedAnswerIndex(index)
            if (selectedAnswerIndex != null) {
                val choiceCount = answerChoiceCounts.getOrDefault(selectedAnswerIndex, 0)
                answerChoiceCounts[selectedAnswerIndex] = choiceCount + 1
            }
        }
        HorizontalBarChartWithText(scoreViewModel)

        for ((choiceIndex, count) in answerChoiceCounts) {
            Text(
                text = "Choice $choiceIndex: Total Score = ${count * 20}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
fun HorizontalBarChartWithText(scoreViewModel: ScoreViewModel) {
    val iCount = scoreViewModel.getSelectedAnswerCounts(0)
    val eCount = scoreViewModel.getSelectedAnswerCounts(1)
    val totalCount = iCount + eCount

    val iPercentage = (iCount.toFloat() / totalCount) * 100
    val ePercentage = (eCount.toFloat() / totalCount) * 100

    val barEntries = listOf(
        BarEntry(0f, floatArrayOf(iPercentage, ePercentage))
    )

    val dataSet = BarDataSet(barEntries, "")
    dataSet.colors = listOf(android.graphics.Color.BLUE, android.graphics.Color.RED)

    val data = BarData(dataSet)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier.size(550.dp, 100.dp),

            factory = { context ->
                val barChart = HorizontalBarChart(context)
                barChart.data = data
                barChart.setDrawValueAboveBar(true)
                barChart.description.isEnabled = false
                barChart.axisRight.isEnabled = false
                barChart.legend.isEnabled = false

                barChart.xAxis.setDrawGridLines(false)
                barChart.xAxis.setDrawAxisLine(false)
                barChart.xAxis.setDrawLabels(false)
                barChart.xAxis.valueFormatter = PercentFormatter()

                barChart.axisLeft.setDrawGridLines(false)
                barChart.axisLeft.setDrawAxisLine(false)
                barChart.axisLeft.setDrawLabels(false)
                barChart.axisLeft.axisMinimum = 0f
                barChart.axisLeft.axisMaximum = 100f

                barChart.axisRight.isEnabled = false

                barChart.animateY(800)

                barChart.invalidate()

                barChart
            }
        )

        if (iPercentage < 100f && ePercentage < 100f) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "I ${"%.1f".format(iPercentage)}%",
                    color = Color.White,
                    modifier = Modifier.padding(start = 23.dp, end = 0.dp)
                )
                Text(text = "E ${"%.1f".format(ePercentage)}%",
                    color = Color.White,
                    modifier = Modifier.padding(start = 0.dp, end = 48.dp)
                )
            }
        } else if (ePercentage < 100f) {
            Text(text = "I ${"%.1f".format(iPercentage)}%",
                color = Color.White,
                modifier = Modifier.padding(start = 0.dp, end = 15.dp)
                )
        } else if (iPercentage < 100f) {
            Text(text = "E ${"%.1f".format(ePercentage)}%",
                color = Color.White,
                modifier = Modifier.padding(start = 0.dp, end = 15.dp)
                )
        }
    }
}
