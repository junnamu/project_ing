package com.example.comp_nav_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.comp_nav_test.ui.theme.Comp_nav_testTheme
import kotlinx.coroutines.delay

// 사용할 data를 class로 선언
data class Answer(val text: String, var isSelected: Boolean = false)
data class Percentages(
    val iPercentage: Float,
    val ePercentage: Float,
    val nPercentage: Float,
    val sPercentage: Float,
    val tPercentage: Float,
    val fPercentage: Float,
    val pPercentage: Float,
    val jPercentage: Float
)

// 데이터 및 계산한 함수를 호출할 때 여러 화면 전환시 데이터전달 목적으로 뷰모델 사용
class ScoreViewModel : ViewModel() {
    private val answerScoreMap = mutableMapOf<Int, Int>() // 답변 인덱스를 키로 사용하여 점수 저장
    private val selectedAnswerIndexMap = mutableMapOf<Int, Int>()
    private val selectedAnswerCountMap = mutableMapOf<Int, Int>()
    private val selectedAnswerTextMap = mutableMapOf<Int, String>()

    //점수 저장하는 함수
    fun saveAnswerScore(answerIndex: Int, score: Int) {
        answerScoreMap[answerIndex] = score
    }
    //점수 불러오는 함수
    fun getAnswerScore(answerIndex: Int): Int {
        return answerScoreMap[answerIndex] ?: 0
    }
    //저장된 점수 초기화
    fun clearAnswerScore(answerIndex: Int) {
        answerScoreMap.remove(answerIndex)
    }
    //사용자가 선택한 답변 인덱스를 저장
    fun saveSelectedAnswerIndex(answerIndex: Int, selectedIndex: Int) {
        selectedAnswerIndexMap[answerIndex] = selectedIndex
    }
    //사용자가 선택한 답변 인덱스를 불러오는 함수
    fun getSelectedAnswerIndex(answerIndex: Int): Int? {
        return selectedAnswerIndexMap[answerIndex]
    }
    //사용자가 입력한 답변 텍스트를 저장
    fun saveSelectedAnswerText(answerIndex: Int, answerText: String) {
        selectedAnswerTextMap[answerIndex] = answerText
    }


    //모든 답변에 대한 점수 리스트를 반환
    fun getAnswerScores(): List<Int> {
        val answerScores = mutableListOf<Int>()
        for (index in 0 until MAX_QUESTION_INDEX) {
            answerScores.add(getAnswerScore(index))
        }
        return answerScores
    }
    //선택된 답변의 인덱스를 기준으로 선택된 횟수 증가
    fun incrementSelectedAnswerCount(choiceIndex: Int) {
        val currentCount = selectedAnswerCountMap.getOrDefault(choiceIndex, 0)
        selectedAnswerCountMap[choiceIndex] = currentCount + 1
    }
    //최대 질문수 설정,  0부터 20까지의 질문 인덱스 값을 얻으려고 21로 설정
    companion object {
        const val MAX_QUESTION_INDEX = 21
    }

    fun calculateMBTI(answerScores: List<Int>): Pair<Percentages, String> {
        val answerChoiceCounts = mutableMapOf<Int, Int>()

        var iCount = 0
        var eCount = 0
        var nCount = 0
        var sCount = 0
        var tCount = 0
        var fCount = 0
        var pCount = 0
        var jCount = 0

        for ((index) in answerScores.withIndex()) {
            val selectedAnswerIndex = getSelectedAnswerIndex(index)
            if (selectedAnswerIndex != null) {
                val choiceCount = answerChoiceCounts.getOrDefault(selectedAnswerIndex, 0)
                answerChoiceCounts[selectedAnswerIndex] = choiceCount + 1

                when (index) {
                    in 1..5 -> {
                        if (selectedAnswerIndex == 0) {
                            iCount++
                        } else if (selectedAnswerIndex == 1) {
                            eCount++
                        }
                    }

                    in 6..10 -> {
                        if (selectedAnswerIndex == 0) {
                            nCount++
                        } else if (selectedAnswerIndex == 1) {
                            sCount++
                        }
                    }

                    in 11..15 -> {
                        if (selectedAnswerIndex == 0) {
                            tCount++
                        } else if (selectedAnswerIndex == 1) {
                            fCount++
                        }
                    }

                    in 15..19 -> {
                        if (selectedAnswerIndex == 0) {
                            pCount++
                        } else if (selectedAnswerIndex == 1) {
                            jCount++
                        }
                    }
                }
            }
        }

        val ieCount = iCount + eCount
        val nsCount = nCount + sCount
        val tfCount = tCount + fCount
        val pjCount = pCount + jCount

        val iPercentage = (iCount.toFloat() / ieCount) * 100
        val ePercentage = (eCount.toFloat() / ieCount) * 100
        val nPercentage = (nCount.toFloat() / nsCount) * 100
        val sPercentage = (sCount.toFloat() / nsCount) * 100
        val tPercentage = (tCount.toFloat() / tfCount) * 100
        val fPercentage = (fCount.toFloat() / tfCount) * 100
        val pPercentage = (pCount.toFloat() / pjCount) * 100
        val jPercentage = (jCount.toFloat() / pjCount) * 100
        //
        val percentages = Percentages(
            iPercentage, ePercentage, nPercentage, sPercentage,
            tPercentage, fPercentage, pPercentage, jPercentage
        )
        // MBTI 조합
        val iOrE = if (iCount > eCount) "I" else "E"
        val nOrS = if (nCount > sCount) "N" else "S"
        val tOrF = if (tCount > fCount) "T" else "F"
        val pOrJ = if (pCount > jCount) "P" else "J"
        // 최종 MBTI 출력
        val finalMBTI = "$iOrE$nOrS$tOrF$pOrJ"

        return Pair(percentages, finalMBTI)
    }


}
// 선택된 답변마다 점수 계산하는 함수
private fun calculateScore(selectedAnswer: Answer, answerOptions: List<Answer>): Int {
    return when (selectedAnswer) {
        answerOptions[0] -> 20
        answerOptions[1] -> 20
        else -> 0
    }
}

class MainActivity : ComponentActivity() {
    //뷰모델 사용을 위해 선언
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

//네비게이션 컴포즈 / 뷰모델도 각각 데이터 전달
@Composable
fun AppNavigation(scoreViewModel: ScoreViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = "title"
    ) {
        composable("title") { Title(navController) }
        composable("q1") { Q1(navController, scoreViewModel) }
        composable("q2") { Q2(navController, scoreViewModel) }
        composable("q3") { Q3(navController, scoreViewModel) }
        composable("q4") { Q4(navController, scoreViewModel) }
        composable("q5") { Q5(navController, scoreViewModel) }
        composable("q6") { Q6(navController, scoreViewModel) }
        composable("q7") { Q7(navController, scoreViewModel) }
        composable("q8") { Q8(navController, scoreViewModel) }
        composable("q9") { Q9(navController, scoreViewModel) }
        composable("q10") { Q10(navController, scoreViewModel) }
        composable("q11") { Q11(navController, scoreViewModel) }
        composable("q12") { Q12(navController, scoreViewModel) }
        composable("q13") { Q13(navController, scoreViewModel) }
        composable("q14") { Q14(navController, scoreViewModel) }
        composable("q15") { Q15(navController, scoreViewModel) }
        composable("q16") { Q16(navController, scoreViewModel) }
        composable("q17") { Q17(navController, scoreViewModel) }
        composable("q18") { Q18(navController, scoreViewModel) }
        composable("q19") { Q19(navController, scoreViewModel) }
        composable("q20") { Q20(navController, scoreViewModel) }
        composable("results") { backStackEntry ->
            ResultsPage(navController, scoreViewModel = scoreViewModel)
        }
        composable("spResult") { SPResultScreen(navController, scoreViewModel) }
        composable("sjResult") { SJResultScreen(navController, scoreViewModel) }
        composable("nfReuslt") { NFResultScreen(navController, scoreViewModel) }
        composable("ntResult") { NTResultScreen(navController, scoreViewModel) }
    }
}

//메인 타이틀 화면
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Title(navController: NavController) {
    //애니메이션 반복
    val isVisible = remember { mutableStateOf(true) }
    LaunchedEffect(isVisible) {
        while (true) {
            delay(500)
            isVisible.value = !isVisible.value
        }
    }
    //메인화면에 사용될 폰트 변수 선언
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
        //Click 텍스트가 커지면서 나타났다가 작아지면서 사라지는 애니메이션
        Box {
            Column {
                AnimatedVisibility(
                    visible = isVisible.value, enter = fadeIn() + scaleIn(), exit = scaleOut()
                ) {
                    Text(
                        text = "Click",
                        fontSize = 38.sp,
                        modifier = Modifier
                            .alpha(0.95f)
                            .animateContentSize(),
                        fontWeight = FontWeight.Bold,
                        fontFamily = customFont
                    )
                }

            }

//        Box {
            Spacer(modifier = Modifier.size(64.dp))
        }
    }
}

//질문화면 내용 함수로 각 질문 화면으로 중복되는 코드를 분리했다
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

    Box(
        modifier = Modifier
            .fillMaxSize() // 전체 화면을 채우도록 설정
            .padding(16.dp), contentAlignment = Alignment.Center // 수직(세로) 중앙 정렬
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "질문 $answerIndex",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
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
                            scoreViewModel.saveSelectedAnswerIndex(
                                answerIndex, index
                            ) // 선택한 답변의 인덱스 저장
                            scoreViewModel.incrementSelectedAnswerCount(index) // 해당 선택지 카운트 증가
                        }, verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = answer == selectedAnswer.value, onClick = {
                        selectedAnswer.value = answer
                    })
                    Text(
                        text = answer.text, modifier = Modifier.padding(start = 8.dp)
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
                            // 현재 질문 인덱스가 1보다 크다면
                            if (answerIndex > 1) {
                                //이전 질문에서 점수를 가져오고
                                val previousScore = scoreViewModel.getAnswerScore(answerIndex - 1)
                                // 이전 점수가 0이 아니라면 (이전에 답변이 있었다면)
                                if (previousScore != 0) {
                                    // 이전 점수를 현재 질문의 점수로 저장
                                    scoreViewModel.saveAnswerScore(answerIndex, previousScore)
                                } else {
                                    // 이전 점수가 0이면 (이전에 답변이 없었다면)
                                    // 현재 질문의 점수를 초기화
                                    scoreViewModel.clearAnswerScore(answerIndex)
                                }
                            }

                            val score = calculateScore(answer, answerOptions)
                            scoreViewModel.saveAnswerScore(answerIndex, score)
                            scoreViewModel.saveSelectedAnswerText(
                                answerIndex, answer.text
                            )
                            onNextClicked()
                        }
                    }, enabled = selectedAnswer.value != null
                ) {
                    Text(text = "다음")
                }
            }
        }
    }
}

//각 질문 답변 화면 Q1~Q20까지의 함수
@Composable
fun Q1(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 1

    val answerOptions = remember {
        listOf(
            Answer("와~ 드디어 나만의 시간이다. 편하고 행복하다"), Answer("외롭다 바깥과는 달리 깜깜하고 너무 조용해")
        )
    }

    val questionText = "아무도 없는 집에 들어가면 어떤 느낌을 받는지?"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        //
        onNextClicked = {
            navController.navigate("q2")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}

@Composable
fun Q2(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 2

    val answerOptions = remember {
        listOf(
            Answer("아뇨"), Answer("네")
        )
    }

    val questionText = "학창시절 인기가 많았었나요?"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,

        onNextClicked = {
            navController.navigate("q3")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}

@Composable
fun Q3(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 3

    val answerOptions = remember {
        listOf(
            Answer("와~ 갑자기 혼자만의 시간이 생겼네. 뭐하지? 신나~~"), Answer("아 이럴수가 그럼.. 누구를 만날까? 연락해봐야지")
        )
    }

    val questionText = "친구랑 만나기로 했는데 갑자기 약속이 취소되었다"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        onNextClicked = {
            navController.navigate("q4")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}

@Composable
fun Q4(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 4

    val answerOptions = remember {
        listOf(
            Answer("조용히 사람들을 관찰한다"), Answer("주변 사람에게 말을 걸어 대화를 시작한다")
        )
    }

    val questionText = "파티에 가서 당신의 행동에 가까운 것은?"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        onNextClicked = {
            navController.navigate("q5")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}

@Composable
fun Q5(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 5

    val answerOptions = remember {
        listOf(
            Answer("혼자할 수 있는 취미 생활을 즐긴다"), Answer("일단 취미 동호회에 가입해서 사람들과 어울린다")
        )
    }

    val questionText = "취미 생활을 새로 시작한 당신은 어떤 스타일?"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        onNextClicked = {
            navController.navigate("q6")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}

@Composable
fun Q6(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 6

    val answerOptions = remember {
        listOf(
            Answer("가사가 너무 중요해~ 가사까지 마음에 들어야 최애곡!"), Answer("노래는 멜로디지~ 흥얼흥얼 가사가 뭐가 중요해 음악은 Feel!")
        )
    }

    val questionText = "노래 들을 때 뭐가 중요해?"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        onNextClicked = {
            navController.navigate("q7")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}

@Composable
fun Q7(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 7

    val answerOptions = remember {
        listOf(
            Answer("아뇨.."), Answer("네!")
        )
    }

    val questionText = "당신은 운동을 잘하나요?"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        onNextClicked = {
            navController.navigate("q8")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}

@Composable
fun Q8(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 8

    val answerOptions = remember {
        listOf(
            Answer("백설공주, 애플 , 뉴턴, 아침사과, 금사과 "), Answer("빨갛다, 과일, 맛있다")
        )
    }

    val questionText = "사과 하면 뭐가 더 생각나는지?"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        onNextClicked = {
            navController.navigate("q9")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}

//

@Composable
fun Q9(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 9

    val answerOptions = remember {
        listOf(
            Answer("책을 읽거나, 영화를 보거나, 음악을 듣는다"), Answer("집안일을 하거나, 요리를 하거나, TV를 본다")
        )
    }

    val questionText = "집에서 간만에 여유시간이 주어졌다면 당신은"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        onNextClicked = {
            navController.navigate("q10")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}

@Composable
fun Q10(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 10

    val answerOptions = remember {
        listOf(
            Answer("비행기가 추락하면 어쩌지. 비상구 자리에 앉을까?"), Answer("기내식 뭐 나오지? 비행기에선 영화나 볼까?")
        )
    }

    val questionText = "비행기 타기 전 무슨 생각을 해?"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        onNextClicked = {
            navController.navigate("q11")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}


@Composable
fun Q11(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 11

    val answerOptions = remember {
        listOf(
            Answer("둘이 된다. 왜냐면 슬픈 사람이 두명이 되기 때문이지."), Answer("반이 된다. 슬픔은 공유해야지~")
        )
    }

    val questionText = "친구가 얘기한다. 슬픔을 나누면?"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        onNextClicked = {
            navController.navigate("q12")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}

@Composable
fun Q12(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 12

    val answerOptions = remember {
        listOf(
            Answer("펌했어? 커트? 염색? 사진찍어 보내봐"), Answer("왜 우울해? 무슨일 있었어?")
        )
    }

    val questionText = "친구야 나 우울해서 미용실 갔다왔어."

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        onNextClicked = {
            navController.navigate("q13")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}

@Composable
fun Q13(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 13

    val answerOptions = remember {
        listOf(
            Answer("보험은 들어놨어? 누구 과실이야?"), Answer("어디 많이 다쳤어? 너 괜찮냐?")
        )
    }

    val questionText = "친구야 나 교통사고 났어.."

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        onNextClicked = {
            navController.navigate("q14")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}

@Composable
fun Q14(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 14

    val answerOptions = remember {
        listOf(
            Answer("네 어느정도는 했죠"), Answer("아뇨 수학은 별로")
        )
    }

    val questionText = "수학을 잘하는 편인가요? 과거에 잘하는 편이었는지"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        onNextClicked = {
            navController.navigate("q15")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}

@Composable
fun Q15(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 15

    val answerOptions = remember {
        listOf(
            Answer("나 혼자 다녀올게. 너 할거 하고있어"), Answer("같이 가자! 혼자가면 심심하잖아")
        )
    }

    val questionText = "맞다 친구야 차에 뭐 놓고왔나봐"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        onNextClicked = {
            navController.navigate("q16")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}

@Composable
fun Q16(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 16

    val answerOptions = remember {
        listOf(
            Answer("읽지 않은 알람이.. 매우 많다."), Answer("읽지 않은 알람은 거의 없다")
        )
    }

    val questionText = "당신의 보통 카카오톡 상태는?"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        onNextClicked = {
            navController.navigate("q17")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}

@Composable
fun Q17(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 17

    val answerOptions = remember {
        listOf(
            Answer("음식은 손맛이지. 감으로 하면 요리 완성!"), Answer("레시피랑 계량대로 잘 만드는게 중요해")
        )
    }

    val questionText = "요리할 때 당신은 어떤 스타일?"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        onNextClicked = {
            navController.navigate("q18")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}

@Composable
fun Q18(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 18

    val answerOptions = remember {
        listOf(
            Answer("오~ 좋지. 어디로 갈까?"), Answer("응? 갑자기? 나 오늘 계획 다 세워놨는데..")
        )
    }

    val questionText = "야 친구야 뭐하냐~ 심심한데 나와!"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        onNextClicked = {
            navController.navigate("q19")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}

@Composable
fun Q19(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 19

    val answerOptions = remember {
        listOf(
            Answer("아뇨 더러운 것 같아요"), Answer("네 깨끗한 편이죠")
        )
    }

    val questionText = "당신의 방은 보통 깨끗한편인가요?"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        onNextClicked = {
            navController.navigate("q20")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}


@Composable
fun Q20(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 20

    val answerOptions = remember {
        listOf(
            Answer("여행 계획을 세우지 않고, 그냥 떠나고 싶습니다"), Answer("여행지와 일정을 미리 계획하고, 예약을 합니다")
        )
    }

    val questionText = "휴가를 간다면?"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        onNextClicked = {
            navController.navigate("results")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}
//각 유형 차트마다 차트를 컴포즈로 그려주기 위한 ProportionBar 함수
@Composable
fun ProportionBar(
    data: List<Number>,
    colors: List<Color>,
    strokeWidth: Float,
    cornerRadius: CornerRadius = CornerRadius(strokeWidth),
    modifier: Modifier,
) {
    val sumOfData = data.map { it.toFloat() }.sum()
    Canvas(
        modifier = modifier
    ) {
        //canvas size
        val lineStart = size.width * 0.05f
        val lineEnd = size.width * 0.95f
        //차트 길이
        val lineLength = (lineEnd - lineStart)
        val lineHeightOffset = (size.height - strokeWidth) * 0.5f
        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    Rect(
                        offset = Offset(lineStart, lineHeightOffset),
                        size = Size(lineLength, strokeWidth)
                    ), cornerRadius
                )
            )
        }
        val dataAndColor = data.zip(colors)
        clipPath(
            path
        ) {
            var dataStart = lineStart
            dataAndColor.forEach { (number, color) ->
                val dataEnd = dataStart + ((number.toFloat() / sumOfData) * lineLength)
                drawRect(
                    color = color,
                    topLeft = Offset(dataStart, lineHeightOffset),
                    size = Size(dataEnd - dataStart, strokeWidth)
                )
                dataStart = dataEnd
            }
        }

    }
}
//SP 재테크추천 화면
@Composable
fun SPResultScreen(navController: NavController, scoreViewModel: ScoreViewModel) {
    val (percentages, finalMBTI) = scoreViewModel.calculateMBTI(scoreViewModel.getAnswerScores())
    val srText = buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)) {
            append(finalMBTI)
        }
        append(" 는 도전적이에요. \n위기 대처 능력도 우수해서, 주가 흐름을 빠르게 파악하여 투자하는 단기 투자에 강합니다.")
        append("\n뛰어난 순발력으로 거래량과 시황을 확인하여 수익을 내는 것이죠.")
        append("\n그러나 그만큼 리스크가 큰 위험한 투자에 빠지기 쉽습니다.")
        append("\n이익보다 손실을 따지는 통제력을 기르고, 적립식 투자와 같은 안정적인 투자와 병행하는 것을 추천합니다.")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = srText,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(bottom = 30.dp)
        )
        
        Button(
            onClick = {
                navController.navigate("title")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 110.dp)
                .padding(top = 16.dp)
        ) {
            Text(
                text = "메인 페이지로\n돌아가기",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.White,
            )
        }
    }
}
//SJ 재테크추천 화면
@Composable
fun SJResultScreen(navController: NavController, scoreViewModel: ScoreViewModel) {

    val (percentages, finalMBTI) = scoreViewModel.calculateMBTI(scoreViewModel.getAnswerScores())
    val sjText = buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)) {
            append(finalMBTI)
        }
        append(" 는 신중한 편이에요. \n보수적인 이들에겐 안정적인 재테크를 추천합니다.")
        append("\n안전한 목돈 마련 방법인 예·적금, 손실 위험은 줄이고 수익률을 높이는 장기 투자 및 대형 우량주 투자,")
        append("\n주기적으로 배당 수익이 발생하는 리츠 투자 등의 상품이 있습니다.")
        append("\n하지만 원금 손실에 대한 두려움으로 수익성을 놓칠 수 있어요.")
        append("\n꼼꼼한 사전 공부로 안정성이 높은 상품들을 추려 재테크 포트폴리오를 계획해보세요.")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = sjText,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(bottom = 30.dp)
        )
        Button(
            onClick = {
                navController.navigate("title")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 110.dp)
                .padding(top = 16.dp)
        ) {
            Text(
                text = "메인 페이지로\n 돌아가기",
                fontSize = (16.sp),
                textAlign = TextAlign.Center,
                color = Color.White,

                )
        }

    }
}
//NT 재테크추천 화면
@Composable
fun NTResultScreen(navController: NavController, scoreViewModel: ScoreViewModel) {
    val (percentages, finalMBTI) = scoreViewModel.calculateMBTI(scoreViewModel.getAnswerScores())
    val ntText = buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)) {
            append(finalMBTI)
        }
        append(" 는 논리적이에요. \n배움을 좋아하고, 통찰력이 뛰어나 어떤 재테크도 시작하기에 무리가 없습니다.")
        append("\n주식 매매 시에는 시황뿐만 아니라 전망, 기술력, 성장 가능성 등 다양한 정보를 확인하고 좋은 투자처도 잘 찾아냅니다.")
        append("\n다만 자신을 과신할 위험이 있어요. 꾸준한 공부와 사례 연구로 자신만의 투자 방법을 구축해야 합니다.")
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = ntText,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(bottom = 30.dp)
        )
        Button(
            onClick = {
                navController.navigate("title")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 110.dp)
                .padding(top = 16.dp)
        ) {
            Text(
                text = "메인 페이지로\n 돌아가기",
                fontSize = (16.sp),
                textAlign = TextAlign.Center,
                color = Color.White,

                )
        }

    }
}
//NF 재테크추천 화면
@Composable
fun NFResultScreen(navController: NavController, scoreViewModel: ScoreViewModel) {
    val (percentages, finalMBTI) = scoreViewModel.calculateMBTI(scoreViewModel.getAnswerScores())
    val nfText = buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)) {
            append(finalMBTI)
        }
        append(" 는 가치 중심적이에요. \n돈보다는 정신적 성장과 이상향에 집중하는 편입니다.")
        append("\n이들은 먼저 자신의 관심사에 따라 재테크를 시작해보는 것을 추천할게요.")
        append("\n예를 들어, 음악 저작권 재테크 플랫폼인 ‘뮤직 카우’를 이용하면, 좋아하는 가수나 음악에 투자할 수 있습니다.")
        append("\n또 좋아하는 작가가 있다면, 작품 NFT 투자를 시작할 수 있어요.")
        append("\n다만 관심이 적은 만큼, 원금을 손실하면 금방 포기하기 쉽습니다.")
        append("\n관리의 필요성이 적고, 꾸준한 수익이 생기는 예금 및 적금 상품도 함께 추천합니다.")
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = nfText,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(bottom = 30.dp)
        )
        Button(
            onClick = {
                navController.navigate("title")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 110.dp)
                .padding(top = 16.dp)
        ) {
            Text(
                text = "메인 페이지로\n 돌아가기",
                fontSize = (16.sp),
                textAlign = TextAlign.Center,
                color = Color.White,

                )
        }

    }
}

//각각 유형별 설명 map으로 저장
val mbtiDescriptions = mapOf(
    "ISTJ" to """
ISTJ - 청렴결백한 논리주의자: 질서와 안정을 중요시하며 책임감이 강하고 현실적입니다. 계획적이고 조직적인 성향이 있습니다.
    """.trimIndent(), "ISFJ" to """
ISFJ - 용감한 수호자: 따뜻하고 현실적이며 자신을 희생하는 경향이 있습니다. 다른 사람의 필요를 챙기는데 능숙하며 세심한 관리자입니다.
    """.trimIndent(), "INFJ" to """
INFJ - 선의의 옹호자: 이해심이 깊고 비전을 가지며 창의적입니다. 타인의 성장을 도모하고 공동체에 기여하는 데 관심이 많습니다.
    """.trimIndent(), "INTJ" to """
INTJ - 전략가: 분석적이고 논리적인 사고를 지닌 전략적 사고자입니다. 계획을 세우고 목표를 달성하기 위해 노력합니다.
    """.trimIndent(), "ISTP" to """
ISTP - 만능재주꾼: 문제 해결 능력이 뛰어나며 현실적인 도구를 다루는 데 능숙합니다. 논리적 사고와 실용성을 중요시합니다.
    """.trimIndent(), "ISFP" to """
ISFP - 호기심 많은 예술가: 예술적인 감각이 뛰어나며 센스가 있습니다. 감정 표현에 능숙하고 자연과 조화롭게 연결되기를 원합니다.
    """.trimIndent(), "INFP" to """
INFP - 열정적인 중재자: 이상적이고 창의적인 성향을 가졌으며 타인의 감정과 가치를 중요시합니다. 개인적인 성장을 추구합니다.
    """.trimIndent(), "INTP" to """
INTP - 논리적인 사색가: 
분석과 비판적 사고를 중요시하며 복잡한 문제를 해결하는 데 능숙합니다. 지식을 추구하고 탐구합니다.
    """.trimIndent(), "ESTP" to """
ESTP - 모험심 많은 사업가: 자신감이 넘치며 리더십을 펼치는 데 능숙합니다. 현실적이고 활동적인 성향을 가지며 도전을 즐깁니다.
    """.trimIndent(), "ESFP" to """
ESFP - 자유로운 영혼의 연예인: 사교적이고 재능 있는 멀티태스커입니다. 즉흥적으로 행동하며 새로운 경험을 즐깁니다.
    """.trimIndent(), "ENTP" to """
ENTP - 논쟁을 즐기는 변론가: 독창적이고 논리적인 사고를 가지며 문제 해결에 열정을 갖습니다. 새로운 아이디어를 창출하고 토론을 즐깁니다.
    """.trimIndent(), "ENTP" to """
ENTP - 논쟁을 즐기는 변론가: 독창적이고 논리적인 사고를 가지며 문제 해결에 열정을 갖습니다. 새로운 아이디어를 창출하고 토론을 즐깁니다.
    """.trimIndent(), "ESFJ" to """
ESFJ - 사교적인 친선도모자: 사회적인 상호작용을 중요시하며 다른 사람을 돕는 데 열정적입니다. 조화롭고 협조적인 성향을 가집니다.
    """.trimIndent(), "ESFJ" to """
ESFJ - 사교적인 친선도모자: 사회적인 상호작용을 중요시하며 다른 사람을 돕는 데 열정적입니다. 조화롭고 협조적인 성향을 가집니다.
    """.trimIndent(), "ENFJ" to """
ENFJ - 정열적인 사회운동가: 친절하고 동정심이 많으며 타인의 성장을 도모하려는 노력을 기울입니다. 리더십과 커뮤니케이션 능력이 뛰어납니다.
    """.trimIndent(), "ENTJ" to """
ENTJ - 대담한 통솔자: 목표 지향적이며 리더십을 펼치는 데 능숙합니다.
 조직을 효율적으로 운영하고 목표를 달성하기 위해 노력합니다.
    """.trimIndent()
)

//각각 유형별 설명 불러오는 함수
fun getMBTIDescription(mbtiType: String): String {
    return mbtiDescriptions[mbtiType] ?: "Unknown MBTI type"
}

//결과 화면
@Composable
fun ResultsPage(navController: NavController, scoreViewModel: ScoreViewModel) {
    val (percentages, finalMBTI) = scoreViewModel.calculateMBTI(scoreViewModel.getAnswerScores())
    val iPercentage = percentages.iPercentage
    val ePercentage = percentages.ePercentage
    val nPercentage = percentages.nPercentage
    val sPercentage = percentages.sPercentage
    val tPercentage = percentages.tPercentage
    val fPercentage = percentages.fPercentage
    val pPercentage = percentages.pPercentage
    val jPercentage = percentages.jPercentage

    LazyColumn(
        Modifier.fillMaxSize(), contentPadding = PaddingValues(10.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "결과",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    //최종MBTI 변수로 설정된 finalMBTI만 폰트사이즈 크게
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontSize = 24.sp)) {
                            append("당신의 MBTI는 ")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontSize = 36.sp, fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(finalMBTI)
                        }
                        withStyle(style = SpanStyle(fontSize = 24.sp)) {
                            append(" 입니다")
                        }
                    }, modifier = Modifier.padding(top = 16.dp), textAlign = TextAlign.Center
                )

                Text(
                    text = (getMBTIDescription(finalMBTI)),
                    modifier = Modifier.padding(top = 16.dp),
                    textAlign = TextAlign.Center
                )




                Spacer(modifier = Modifier.height(20.dp))


// IE차트
                Box(
                    modifier = Modifier.size(500.dp, 60.dp), contentAlignment = Alignment.Center
                ) {
                    ProportionBar(
                        data = listOf(iPercentage, ePercentage),
                        colors = listOf(Color.Blue, Color.Red),
                        strokeWidth = with(LocalDensity.current) { 40.dp.toPx() },
                        modifier = Modifier.fillMaxSize()
                    )

                    if (iPercentage < 100f && ePercentage < 100f) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "I ${"%.1f".format(iPercentage)}%",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(start = 30.dp, end = 0.dp)
                            )
                            Text(
                                text = "E ${"%.1f".format(ePercentage)}%",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(start = 0.dp, end = 25.dp)
                            )
                        }
                    } else if (ePercentage < 100f) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                        ) {
                            Text(
                                text = "I ${"%.1f".format(iPercentage)}%",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }

                    } else if (iPercentage < 100f) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                        ) {
                            Text(
                                text = "E ${"%.1f".format(ePercentage)}%",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

// NS차트
                Box(
                    modifier = Modifier.size(500.dp, 60.dp), contentAlignment = Alignment.Center
                ) {
                    ProportionBar(
                        data = listOf(nPercentage, sPercentage),
                        colors = listOf(Color.Blue, Color.Red),
                        strokeWidth = with(LocalDensity.current) { 40.dp.toPx() },
                        modifier = Modifier.fillMaxSize()
                    )

                    if (nPercentage < 100f && sPercentage < 100f) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "N ${"%.1f".format(nPercentage)}%",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(start = 30.dp, end = 0.dp)
                            )
                            Text(
                                text = "S ${"%.1f".format(sPercentage)}%",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(start = 0.dp, end = 25.dp)
                            )
                        }
                    } else if (sPercentage < 100f) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                        ) {
                            Text(
                                text = "N ${"%.1f".format(nPercentage)}%",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }

                    } else if (nPercentage < 100f) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                        ) {
                            Text(
                                text = "S ${"%.1f".format(sPercentage)}%",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
// TF차트
                Box(
                    modifier = Modifier.size(500.dp, 60.dp), contentAlignment = Alignment.Center
                ) {
                    ProportionBar(
                        data = listOf(tPercentage, fPercentage),
                        colors = listOf(Color.Blue, Color.Red),
                        strokeWidth = with(LocalDensity.current) { 40.dp.toPx() },
                        modifier = Modifier.fillMaxSize()
                    )

                    if (tPercentage < 100f && fPercentage < 100f) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "T ${"%.1f".format(tPercentage)}%",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(start = 30.dp, end = 0.dp)
                            )
                            Text(
                                text = "F ${"%.1f".format(fPercentage)}%",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(start = 0.dp, end = 25.dp)
                            )
                        }
                    } else if (fPercentage < 100f) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                        ) {
                            Text(
                                text = "T ${"%.1f".format(tPercentage)}%",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }

                    } else if (tPercentage < 100f) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                        ) {
                            Text(
                                text = "F ${"%.1f".format(fPercentage)}%",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

// PJ차트
                Box(
                    modifier = Modifier.size(500.dp, 60.dp), contentAlignment = Alignment.Center
                ) {
                    ProportionBar(
                        data = listOf(pPercentage, jPercentage),
                        colors = listOf(Color.Blue, Color.Red),
                        strokeWidth = with(LocalDensity.current) { 40.dp.toPx() },
                        modifier = Modifier.fillMaxSize()
                    )

                    if (pPercentage < 100f && jPercentage < 100f) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "P ${"%.1f".format(pPercentage)}%",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(start = 30.dp, end = 0.dp)
                            )
                            Text(
                                text = "J ${"%.1f".format(jPercentage)}%",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(start = 0.dp, end = 25.dp)
                            )
                        }
                    } else if (jPercentage < 100f) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                        ) {
                            Text(
                                text = "P ${"%.1f".format(pPercentage)}%",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }

                    } else if (pPercentage < 100f) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                        ) {
                            Text(
                                text = "J ${"%.1f".format(jPercentage)}%",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
        //메인 돌아가기 버튼
        item {
            Button(
                onClick = {
                    navController.navigate("title")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 110.dp)
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "메인 페이지로\n 돌아가기",
                    fontSize = (16.sp),
                    textAlign = TextAlign.Center,
                    color = Color.White,

                    )
            }
        }
        //재테크 추천 버튼 : 각 mbti 유형마다 연결 / 조건은 특정 문자열이 포함될 경우 총 4개의 페이지로 각각 중복해서 연결
        item {
            Button(
                onClick = {
                    when {
                        finalMBTI.contains("STP") || finalMBTI.contains("SFP") -> navController.navigate(
                            "spResult"
                        )

                        finalMBTI.contains("STJ") || finalMBTI.contains("SFJ") -> navController.navigate(
                            "sjResult"
                        )

                        finalMBTI.contains("SF") -> navController.navigate("sjResult")
                        finalMBTI.contains("NT") -> navController.navigate("ntResult")
                        finalMBTI.contains("NF") -> navController.navigate("nfResult")
                        else -> navController.navigate("title") // 기본 메인 페이지로 이동
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 105.dp)
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "$finalMBTI\n\uD83D\uDCB0 재테크 추천",
                    fontSize = (16.sp),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }
}



