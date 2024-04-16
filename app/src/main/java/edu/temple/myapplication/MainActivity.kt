package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import java.security.KeyStore.TrustedCertificateEntry

class MainActivity : AppCompatActivity() {

    lateinit var timerBinder: TimerService.TimerBinder
    var isConnected = false

    val handler = Handler(Looper.getMainLooper()){
        true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val serviceConnection = object : ServiceConnection{
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                timerBinder = service as TimerService.TimerBinder
                timerBinder.isRunning
                isConnected = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                isConnected = false
            }

        }

        findViewById<Button>(R.id.startButton).setOnClickListener {
            if (isConnected) timerBinder.start(100, handler)
        }

        findViewById<Button>(R.id.pauseButton).setOnClickListener {
            if (isConnected) timerBinder.pause()
        }

        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if (isConnected) timerBinder.stop()
        }
        bindService(
            Intent(this,TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_start_button->{if (isConnected) timerBinder.start(100, handler)}
            R.id.action_pause_button->{if (isConnected) timerBinder.pause()}
            R.id.action_stop_button->{if (isConnected) timerBinder.stop()}
        }


        return super.onOptionsItemSelected(item)
    }
}