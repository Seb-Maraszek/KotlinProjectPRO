package com.example.kotlinprojectpro.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kotlinprojectpro.FirebaseCommunicator
import com.example.kotlinprojectpro.Login
import com.example.kotlinprojectpro.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home_horizontal.*
import kotlinx.android.synthetic.main.fragment_settings_horizontal.*
import kotlinx.android.synthetic.main.fragment_settings_horizontal.textView4
import kotlinx.android.synthetic.main.fragment_settings_page.*

class SettingsHorizontal : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_settings_horizontal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textField = textView4
        textField.text = FirebaseCommunicator.getCurrentlyLoggedInUser()
        buttonLogoutHorizontal.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
            activity?.finish()
        }
    }
}