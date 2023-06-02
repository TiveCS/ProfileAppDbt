package io.github.tivecs.profileapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var textFormError: TextView
    private lateinit var btnLogin: Button
    private lateinit var btnTextToSignUp: TextView

    private lateinit var accountsStorage: SharedPreferences

    private fun generateAccounts() {
        val accounts = listOf<Map<String, Any>>(
            mapOf(
                "email" to "john@gmail.com",
                "password" to "password123",
                "name" to "John Doe",
                "about" to "ini profil john"
            ), mapOf(
                "email" to "budi@gmail.com",
                "password" to "secret123",
                "name" to "Budi Budiman",
                "about" to "ini profil budi"
            )
        )

        val accountsStorageEdit = accountsStorage.edit()
        accountsStorageEdit.putString("profiles", Gson().toJson(accounts))
        accountsStorageEdit.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editEmail = findViewById(R.id.login_form_email)
        editPassword = findViewById(R.id.login_form_password)
        textFormError = findViewById(R.id.login_form_error)
        btnLogin = findViewById(R.id.login_button_submit)
        btnTextToSignUp = findViewById(R.id.login_button_to_signup)

        btnLogin.setOnClickListener(this)
        btnTextToSignUp.setOnClickListener(this)

        accountsStorage = this.getSharedPreferences("accounts", Context.MODE_PRIVATE)
        generateAccounts()
    }

    private fun getProfile(email: String, password: String): Map<String, Any>? {
        val profilesJson = accountsStorage.getString("profiles", "[]")
        val type = object : TypeToken<List<Map<String, Any>>>() {}.type
        val profiles: List<Map<String, Any>> = Gson().fromJson(profilesJson, type)

        val profile: Map<String, Any>? = profiles.find { profile ->
            profile["email"] == email && profile["password"] == password
        }

        return profile
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login_button_submit -> {
                val inputEmail = editEmail.text.toString()
                val inputPassword = editPassword.text.toString()

                if (inputEmail.isEmpty()) {
                    editEmail.error = "Email tidak boleh kosong"
                    return
                }

                if (inputPassword.isEmpty()) {
                    editPassword.error = "Password tidak boleh kosong"
                    return
                }

                val profile = getProfile(inputEmail, inputPassword)
                if (profile == null) {
                    textFormError.setText(R.string.login_error_invalid_credentials)
                    return
                }

                val moveToProfile = Intent(this@MainActivity, ProfileActivity::class.java)
                moveToProfile.putExtra(ProfileActivity.EMAIL, profile["email"] as String)
                moveToProfile.putExtra(ProfileActivity.NAME, profile["name"] as String)
                moveToProfile.putExtra(ProfileActivity.ABOUT, profile["about"] as String)

                startActivity(moveToProfile)
            }

            R.id.login_button_to_signup -> {
                val moveToSignUp = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(moveToSignUp)
            }
        }
    }
}