package com.example.caption

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*

private lateinit var auth: FirebaseAuth

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate()
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        setup()
    }
    private fun setup(){
        title = "Auth"
        val email = findViewById<EditText>(R.id.txtemail2)
        val password = findViewById<EditText>(R.id.txtPassword2)
        val password1 = findViewById<EditText>(R.id.txtPassword3)
        val sendButton = findViewById<Button>(R.id.button)
        sendButton.setOnClickListener {
            if (email.text.isNotEmpty() && password.text.isNotEmpty() && password.text.toString() == password1.text.toString()){
                /*TODO firebase */
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.text.toString(),password.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            var userac:String ? = it.result?.user?.email
                            auth.currentUser?.sendEmailVerification()
                                ?.addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        showHome(userac , ProviderType.BASIC)
                                    } else {
                                        error("No se ha completado la verificacion por correo")
                                    }
                                }
                        }else {
                            error("Error al crear su usuario")
                        }
                    }
            } else{
                error("Debe coincidir ambos campos de las contrasenas y no tener campos vacios")
            }
        }

    }
    private fun showHome(email:String?,provider:ProviderType){
        val homeIntent = Intent(this,Home::class.java).apply {
            putExtra("email",email)
            putExtra("provider",provider.name)
        }
        startActivity(homeIntent)
    }
    private fun setLocate(languageToLoad:String?){
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang",languageToLoad)
        editor.apply()
    }
    private fun loadLocate(){
        val sharedpref = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedpref.getString("My_Lang","")
        setLocate(language)
    }
    private fun error(msg:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(msg)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}