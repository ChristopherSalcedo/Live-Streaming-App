package com.example.caption

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
private lateinit var auth: FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        setup();
    }
    private fun setup(){
        title = "Auth";
        val email = findViewById<EditText>(R.id.txtemail)
        val password = findViewById<EditText>(R.id.txtPassword)
        val sendButton = findViewById<Button>(R.id.button)
        val regButton = findViewById<Button>(R.id.button2)
        sendButton.setOnClickListener {
            if (email.text.isNotEmpty() && password.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showHome(it.result?.user?.email ?:"", ProviderType.BASIC)
                    } else {
                        error()
                    }

                }
            }
        }

        regButton.setOnClickListener {
            val homeIntent = Intent(this,Register::class.java)
            startActivity(homeIntent)

        }

    }
    private fun showHome(email:String,provider:ProviderType){
        val homeIntent = Intent(this,Home::class.java).apply {
            putExtra("email",email)
            putExtra("provider",provider.name)
        }
        startActivity(homeIntent)
    }

    private fun error() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error")
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog = builder.create()
        dialog.show()
    }
}