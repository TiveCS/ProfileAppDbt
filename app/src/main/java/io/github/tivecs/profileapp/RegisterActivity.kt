package io.github.tivecs.profileapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var editAbout: EditText
    private lateinit var editName: EditText

    private lateinit var btnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editEmail = findViewById(R.id.register_form_email)
        editAbout = findViewById(R.id.register_form_about)
        editPassword = findViewById(R.id.register_form_password)
        editName = findViewById(R.id.register_form_name)
        btnSubmit = findViewById(R.id.register_button_submit)

        btnSubmit.setOnClickListener(this)
    }

    private fun isValidInput(): Boolean {
        val emailIsNotEmpty = editEmail.text.isNotEmpty()
        val passwordIsNotEmpty = editPassword.text.isNotEmpty()
        val aboutIsNotEmpty = editAbout.text.isNotEmpty()
        val nameIsNotEmpty = editName.text.isNotEmpty()

        if (!emailIsNotEmpty) {
            editEmail.error = "Please fill your email"
        }

        if (!nameIsNotEmpty) {
            editName.error = "Please fill your name"
        }

        if (!passwordIsNotEmpty) {
            editPassword.error = "Please fill your password"
        }

        if (!aboutIsNotEmpty) {
            editAbout.error = "Please explain about yourself"
        }

        return emailIsNotEmpty && passwordIsNotEmpty && aboutIsNotEmpty
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.register_button_submit -> {
                if (!isValidInput()) return

                val accountsStorage = this.getSharedPreferences("accounts", Context.MODE_PRIVATE)
                val type = object : TypeToken<List<Map<String, Any>>>() {}.type
                val accountsStorageEdit = accountsStorage.edit()

                val profiles: List<Map<String, Any>> =
                    Gson().fromJson(accountsStorage.getString("profiles", "[]"), type)
                val mutableProfiles = profiles.toMutableList()

                val newProfile = mapOf<String, Any>(
                    "email" to editEmail.text,
                    "password" to editPassword.text,
                    "name" to editName.text,
                    "about" to editAbout.text
                )

                mutableProfiles.add(newProfile)

                val profilesJson = Gson().toJson(mutableProfiles.toList())

                accountsStorageEdit.putString("profiles", profilesJson)
                accountsStorageEdit.apply()

                finish()
            }
        }
    }
}