package com.example.classroomapp

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.classroomapp.databinding.FragmentPomoBinding

const val WORK_TIME_MINUTES = 25
const val BREAK_TIME_MINUTES = 5
const val LONG_BREAK_TIME_MINUTES = 30
const val POMODORO_CYCLES = 4

class PomoFragment : Fragment() {

    lateinit var binding: FragmentPomoBinding
    
    var timer: CountDownTimer? = null
    var isWorking = false
    var isPaused = false
    var minutesLeft = 0
    var secondsLeft = 0
    var currentCycle = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPomoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStart.setOnClickListener {
            startOrResumeTimer()
        }

        binding.btnPause.setOnClickListener {
            pauseTimer()
        }

        binding.btnReset.setOnClickListener {
            resetTimer()
        }

        // Starting the initial timer
//        startTimer(WORK_TIME_MINUTES, 0)
//        binding.txt.text = "Work Time !!"
//        isWorking = true
    }

    private fun startOrResumeTimer() {
        if (isPaused) {
            isPaused = false
            startTimer(minutesLeft, secondsLeft)
        } else {
            if (currentCycle % (POMODORO_CYCLES + 1) == 0) { // Long break
                startTimer(LONG_BREAK_TIME_MINUTES, 0)
                binding.txt.text = "Long Break!!"
            } else {
                startTimer(if (!isWorking) WORK_TIME_MINUTES else BREAK_TIME_MINUTES, 0)
                binding.txt.text = if (isWorking) "Work Time " else "Break !!"
            }
        }
        updateButtonText()
    }

    private fun startTimer(minutes: Int, seconds: Int) {
        if (timer != null) {
            timer?.cancel()
        }

        isWorking = !isWorking
        minutesLeft = minutes
        secondsLeft = seconds

        timer = object : CountDownTimer(((minutes * 60 + seconds) * 1000).toLong(), 10) { // Change here to 10 milliseconds
            override fun onTick(millisUntilFinished: Long) {
                minutesLeft = (millisUntilFinished / 1000 / 60).toInt()
                secondsLeft = ((millisUntilFinished / 1000) % 60).toInt()

                updateTimerUI()
            }

            override fun onFinish() {
                currentCycle++
                startOrResumeTimer()
            }
        }.start()
    }

    private fun pauseTimer() {
        timer?.cancel()
        isPaused = true
        updateButtonText()
    }

    private fun updateButtonText() {
        if (isPaused) {
            binding.btnPause.text = "Continue"
            isPaused=false
        } else {
            binding.btnPause.text = "Pause"
            startTimer(minutesLeft, secondsLeft)
        }
    }

    private fun updateTimerUI() {
        binding.min.text = minutesLeft.toString()
        binding.sec.text = if (secondsLeft < 10) "0$secondsLeft" else secondsLeft.toString()
    }

    private fun resetTimer() {
        timer?.cancel()
        isPaused = false
        isWorking = false
        currentCycle = 1
        minutesLeft = WORK_TIME_MINUTES
        secondsLeft = 0
        updateTimerUI()
        updateButtonText()
        startOrResumeTimer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
    }
}