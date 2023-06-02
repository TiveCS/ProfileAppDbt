package io.github.tivecs.profileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ProfileActivity : AppCompatActivity() {

    companion object {
        const val EMAIL = "john@example.com"
        const val NAME = "John Doe"
        const val ABOUT = "About john"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val email = intent.getStringExtra(EMAIL)
        val name = intent.getStringExtra(NAME)
        val about = intent.getStringExtra(ABOUT)

        val profileName = findViewById<TextView>(R.id.profile_name)
        val profileEmail = findViewById<TextView>(R.id.profile_email)
        val profileAbout = findViewById<TextView>(R.id.profile_about)

        profileName.text = name
        profileEmail.text = email
        profileAbout.text = about
    }
}