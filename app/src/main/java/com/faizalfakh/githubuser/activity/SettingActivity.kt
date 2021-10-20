package com.faizalfakh.githubuser.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.faizalfakh.githubuser.R
import com.faizalfakh.githubuser.broadcast.AlarmReceiver
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity(){

    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var sharedPreferences: SharedPreferences

    companion object{
        const val PREFS_NAME = "setting_pref"
        private const val REMINDER ="reminder"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        alarmReceiver = AlarmReceiver()
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        change_language.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        swRemainder.apply{
            isChecked = sharedPreferences.getBoolean(REMINDER, false)
            setOnCheckedChangeListener{ _, isChecked ->
                if(isChecked){
                    alarmReceiver.setDailyReminder(applicationContext, AlarmReceiver.TYPE_REMINDER, "Let's find your friend on Github")
                }else{
                    alarmReceiver.cancelDailyReminder(applicationContext)
                }
                sharedPreferences.edit().putBoolean(REMINDER, isChecked).apply()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}