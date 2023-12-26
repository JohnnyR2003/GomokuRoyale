package com.isel.GomokuRoyale.about.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.isel.GomokuRoyale.R
import com.isel.GomokuRoyale.TAG
import com.isel.GomokuRoyale.about.model.Author

//import isel.pdm.jokes.R


class AboutActivity : ComponentActivity() {

    companion object {
        fun navigateTo(origin: ComponentActivity) {
            val intent = Intent(origin, AboutActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AboutView(
                onBackRequest = { finish() },
                onSendEmailRequested =  ::openSendEmail,
                onUrlRequested = ::openURL,
                listOfAuthor = listOfAuthor
            )
        }
    }

    private fun openURL(url: Uri) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
        catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Failed to open URL", e)
            Toast
                .makeText(
                    this,
                    R.string.activity_info_no_suitable_app,
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }

    private fun openSendEmail(email:String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, "About the Gomoku App")
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e(ContentValues.TAG, "Failed to send email", e)
            Toast
                .makeText(
                    this,
                    R.string.activity_info_no_suitable_app,
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "AboutActivity.onStart() called")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "AboutActivity.onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "AboutActivity.onDestroy() called")
    }
}

private val listOfAuthor: List<Author> = listOf(
    Author(
        "João Ramos",
        "A49424@alunos.isel.pt",
        "https://github.com/JohnnyR2003"
    ),
    Author(
        "Danilo Vieira",
        "A49988@alunos.isel.pt",
        "https://github.com/"
    ),
    Author(
        "Ricardo Cristino",
        "A@alunos.isel.pt",
        "https://github.com/")
)