package com.app.hyo.presentation.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.hyo.domain.manger.LocalUserManger
import com.app.hyo.domain.manger.UserRepository
import com.app.hyo.presentation.dictionary.AlphabetSign
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val userRepository: UserRepository, // <-- ADD
    private val localUserManger: LocalUserManger // <-- ADD
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private val allQuestions = ('A'..'Z').map { AlphabetSign(it, "body_$it.jpg") }
    private lateinit var quizQuestions: List<AlphabetSign>

    init {
        startNewQuiz()
    }

    private fun startNewQuiz() {
        quizQuestions = allQuestions.shuffled().take(10) // Take 10 random questions for the quiz
        _uiState.update { it.copy(
            score = 0,
            quizFinished = false,
            questionNumber = 0,
            totalQuestions = quizQuestions.size
        ) }
        loadNextQuestion()
    }

    private fun loadNextQuestion() {
        val currentQuestionIndex = _uiState.value.questionNumber
        if (currentQuestionIndex < quizQuestions.size) {
            val question = quizQuestions[currentQuestionIndex]
            val options = generateOptions(question.letter)
            _uiState.update {
                it.copy(
                    currentQuestion = question,
                    options = options,
                    isAnswered = false,
                    isCorrect = null,
                    questionNumber = currentQuestionIndex + 1
                )
            }
        } else {
            // Quiz finished
            viewModelScope.launch {
                saveCurrentQuizResult()
                _uiState.update { it.copy(quizFinished = true) }
            }
        }
    }

    private fun saveCurrentQuizResult() {
        viewModelScope.launch {
            val userEmail = localUserManger.readUserEmail().first() // Get current user's email
            if (userEmail != null) {
                val result = com.app.hyo.domain.model.QuizResult(
                    score = _uiState.value.score,
                    totalQuestions = _uiState.value.totalQuestions
                )
                userRepository.addQuizResultToUser(userEmail, result)
            }
        }
    }

    private fun generateOptions(correctAnswer: Char): List<Char> {
        val options = mutableSetOf(correctAnswer)
        val alphabet = 'A'..'Z'
        while (options.size < 4) {
            options.add(alphabet.random())
        }
        return options.toList().shuffled()
    }

    private val _soundEvent = Channel<QuizSoundEvent>()
    val soundEvent = _soundEvent.receiveAsFlow()


    fun onEvent(event: QuizEvent) {
        when (event) {
            is QuizEvent.SelectAnswer -> {
                if (!_uiState.value.isAnswered) {
                    val isCorrect = event.answer == _uiState.value.currentQuestion?.letter
                    viewModelScope.launch { // Launch a coroutine to send the event
                        if (isCorrect) {
                            _soundEvent.send(QuizSoundEvent.PlayCorrectSound)
                        } else {
                            _soundEvent.send(QuizSoundEvent.PlayWrongSound)
                        }
                    }
                    _uiState.update {
                        it.copy(
                            isAnswered = true,
                            isCorrect = isCorrect,
                            score = if (isCorrect) it.score + 10 else it.score
                        )
                    }
                }
            }
            is QuizEvent.NextQuestion -> {
                loadNextQuestion()
            }
            is QuizEvent.RestartQuiz -> {
                startNewQuiz()
            }
        }
    }


    sealed class QuizSoundEvent {
        object PlayCorrectSound : QuizSoundEvent()
        object PlayWrongSound : QuizSoundEvent()
    }
}