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
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.comp_nav_test.ui.theme.Comp_nav_testTheme
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


    // New function to get all answer scores
    fun getAnswerScores(): List<Int> {
        val answerScores = mutableListOf<Int>()
        for (index in 0 until MAX_QUESTION_INDEX) {
            answerScores.add(getAnswerScore(index))
        }
        return answerScores
    }


    fun incrementSelectedAnswerCount(choiceIndex: Int) {
        val currentCount = selectedAnswerCountMap.getOrDefault(choiceIndex, 0)
        selectedAnswerCountMap[choiceIndex] = currentCount + 1
    }

    companion object {
        const val MAX_QUESTION_INDEX = 21
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
                        scoreViewModel.saveSelectedAnswerText(
                            answerIndex, answer.text
                        ) // Save selected answer text
                        onNextClicked()
                    }
                }, enabled = selectedAnswer.value != null
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
            Answer("와~ 드디어 나만의 시간이다. 편하고 행복하다"),
            Answer("외롭다 바깥과는 달리 깜깜하고 너무 조용해")
        )
    }

    val questionText = "아무도 없는 집에 들어가면 어떤 느낌을 받는지?"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel,
        onNextClicked = {
            navController.navigate("q2")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}

@Composable
fun Q2(navController: NavController, scoreViewModel: ScoreViewModel) { // scoreViewModel 전달 추가
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
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가

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
            Answer("와~ 갑자기 혼자만의 시간이 생겼네. 뭐하지? 신나~~"),
            Answer("아 이럴수가 그럼.. 누구를 만날까? 연락해봐야지")
        )
    }

    val questionText = "친구랑 만나기로 했는데 갑자기 약속이 취소되었다"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
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
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
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
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
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
            Answer("가사가 너무 중요해~ 가사까지 마음에 들어야 최애곡!"),
            Answer("노래는 멜로디지~ 흥얼흥얼 가사가 뭐가 중요해 음악은 Feel!")
        )
    }

    val questionText = "노래 들을 때 뭐가 중요해?"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
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
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
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
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
        onNextClicked = {
            navController.navigate("q9")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}

@Composable
fun Q9(navController: NavController, scoreViewModel: ScoreViewModel) {
    val answerIndex = 9

    val answerOptions = remember {
        listOf(
            Answer("책을 읽거나, 영화를 보거나, 음악을 듣는다"),
            Answer("집안일을 하거나, 요리를 하거나, TV를 본다")
        )
    }

    val questionText = "집에서 간만에 여유시간이 주어졌다면 당신은"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
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
            Answer("비행기가 추락하면 어쩌지. 비상구 자리에 앉을까?"),
            Answer("기내식 뭐 나오지? 비행기에선 영화나 볼까?")
        )
    }

    val questionText = "비행기 타기 전 무슨 생각을 해?"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
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
            Answer("둘이 된다. 왜냐면 슬픈 사람이 두명이 되기 때문이지."),
            Answer("반이 된다. 슬픔은 공유해야지~")
        )
    }

    val questionText = "친구가 얘기한다. 슬픔을 나누면?"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
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
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
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
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
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
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
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
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
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
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
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
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
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
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
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
            Answer("아뇨 더러운 것 같아요"),
            Answer("네 깨끗한 편이죠")
        )
    }

    val questionText = "당신의 방은 보통 깨끗한편인가요?"

    QuestionPageContent(answerIndex = answerIndex,
        question = questionText,
        answerOptions = answerOptions,
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
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
        scoreViewModel = scoreViewModel, // scoreViewModel 전달 추가
        onNextClicked = {
            navController.navigate("results")
        },
        onPreviousClicked = {
            scoreViewModel.clearAnswerScore(answerIndex)
            navController.popBackStack()
        })
}


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


@Composable
fun ResultsPage(scoreViewModel: ScoreViewModel) {
    val answerScores = scoreViewModel.getAnswerScores()
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
        val selectedAnswerIndex = scoreViewModel.getSelectedAnswerIndex(index)
        if (selectedAnswerIndex != null) {
            val choiceCount = answerChoiceCounts.getOrDefault(selectedAnswerIndex, 0)
            answerChoiceCounts[selectedAnswerIndex] = choiceCount + 1

            when (index) {
                in 1..5 -> { // 사고/감정 범위
                    if (selectedAnswerIndex == 0) {
                        iCount++
                    } else if (selectedAnswerIndex == 1) {
                        eCount++
                    }
                }

                in 6..10 -> { // 감각/직관 범위
                    if (selectedAnswerIndex == 0) {
                        nCount++
                    } else if (selectedAnswerIndex == 1) {
                        sCount++
                    }
                }
                in 11..15 -> { // 사고/감정 범위
                    if (selectedAnswerIndex == 0) {
                        tCount++
                    } else if (selectedAnswerIndex == 1) {
                        fCount++
                    }
                }
                in 15..19 -> { // 판단/인식 범위
                    if (selectedAnswerIndex == 0) {
                        pCount++
                    } else if (selectedAnswerIndex == 1) {
                        jCount++
                    }
                }
            }
        }
    }

    // Percentage 계산
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

    val iOrE = if (iCount > eCount) "I" else "E"
    val nOrS = if (nCount > sCount) "N" else "S"
    val tOrF = if (tCount > fCount) "T" else "F"
    val pOrJ = if (pCount > jCount) "P" else "J"

    // 최종 MBTI 출력
    val finalMBTI = "$iOrE$nOrS$tOrF$pOrJ"




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



        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "당신의 MBTI는 $finalMBTI 입니다",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
// IE그래프
            Box(
                modifier = Modifier
                    .size(500.dp, 100.dp)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
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

// NS그래프
            Box(
                modifier = Modifier
                    .size(500.dp, 100.dp)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
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
// TF그래프
            Box(
                modifier = Modifier
                    .size(500.dp, 100.dp)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
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

// PJ그래프
            Box(
                modifier = Modifier
                    .size(500.dp, 100.dp)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
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
}



