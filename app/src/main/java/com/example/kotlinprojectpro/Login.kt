package com.example.kotlinprojectpro

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import java.security.MessageDigest


class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var callbackManager:CallbackManager?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        callbackManager = CallbackManager.Factory.create()
        loginUser.setOnClickListener {
            val email = findViewById<EditText>(R.id.emailEditText).text.toString()
            val password = findViewById<EditText>(R.id.editTextPassword).text.toString()
            if (email != "" || password != "") {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            startActivity(Intent(this, MainActivity::class.java))
                            Toast.makeText(
                                baseContext, "Authentication goooood.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            } else {
                Toast.makeText(
                    baseContext, "Authentication failed.",
                    Toast.LENGTH_SHORT)
            }
        }
        registerUser.setOnClickListener{
            val email = findViewById<EditText>(R.id.emailEditText).text.toString()
            val password = findViewById<EditText>(R.id.editTextPassword).text.toString()
            if (email != "" || password != "") {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            startActivity(Intent(this, MainActivity::class.java))
                            Toast.makeText(
                                baseContext, "Authentication goooood.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    baseContext, "Authentication failed.",
                    Toast.LENGTH_SHORT)
            }

        }

        loginUserFB.setReadPermissions("email")
        loginUserFB.setOnClickListener {
            signIn()
        }
        if(auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun signIn() {
        loginUserFB.registerCallback(callbackManager,object:FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result!!.accessToken)
            }

            override fun onCancel() {
                TODO("Not yet implemented")
            }

            override fun onError(error: FacebookException?) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {

        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)
        auth.signInWithCredential(credential).addOnFailureListener { e ->
            Toast.makeText(this,e.message, Toast.LENGTH_LONG).show()
        }
            .addOnSuccessListener { result ->
                val email = result.user.email
                Toast.makeText(this, "email logged: "+ email, Toast.LENGTH_LONG).show()
                val user = auth.currentUser
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(
                    baseContext, "Authentication goooood.",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
    }

    private fun findKey() {
        try {
            val info = packageManager.getPackageInfo("com.example.kotlinprojectpro", PackageManager.GET_SIGNATURES)
            for (siganture in info.signatures)
            {
                val md = MessageDigest.getInstance("SHA")
                md.update(siganture.toByteArray())
                Log.e("KEYHASH", Base64.encodeToString(md.digest(), Base64.DEFAULT))

            }
        }
        catch (e:PackageManager.NameNotFoundException)
        {

        }
    }

    public override fun onStart() {
        super.onStart()

        if(auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}