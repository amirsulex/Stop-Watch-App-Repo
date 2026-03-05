package com.example.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var timeText: TextView
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button
    private lateinit var resetButton: Button

    private var startTime: Long = 0
    private var timeInMilliseconds: Long = 0
    private var timeSwapBuff: Long = 0
    private var updatedTime: Long = 0

    private val handler = Handler(Looper.getMainLooper())

    private val updateTimer = object : Runnable {
        override fun run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime
            updatedTime = timeSwapBuff + timeInMilliseconds

            val seconds = (updatedTime / 1000).toInt()
            val minutes = seconds / 60
            val hours = minutes / 60
            val secs = seconds % 60

            timeText.text = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours % 24, minutes % 60, secs)

            handler.postDelayed(this, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timeText = findViewById(R.id.timeText)
        startButton = findViewById(R.id.startButton)
        pauseButton = findViewById(R.id.pauseButton)
        resetButton = findViewById(R.id.resetButton)

        startButton.setOnClickListener {
            startTime = SystemClock.uptimeMillis()
            handler.postDelayed(updateTimer, 0)
        }

        pauseButton.setOnClickListener {
            timeSwapBuff += timeInMilliseconds
            handler.removeCallbacks(updateTimer)
        }

        resetButton.setOnClickListener {
            startTime = 0
            timeSwapBuff = 0
            timeInMilliseconds = 0
            updatedTime = 0
            timeText.text = getString(R.string.initial_time)
            handler.removeCallbacks(updateTimer)
        }
    }
}
