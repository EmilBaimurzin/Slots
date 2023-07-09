package com.bossbon.bossnza.game.other

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bossbon.bossnza.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment =
            this.supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.first()

//        if (fragment is SecondFragment) {
//            fragment.activityResult(requestCode, resultCode, data)
//        }
    }

    override fun onBackPressed() {
        val fragment =
            this.supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.first()

//        if (fragment is SecondFragment) {
//            if (!fragment.onBackPressed()) {
//
//                super.onBackPressed()
//            }
//        } else {
//            super.onBackPressed()
//        }
    }
}