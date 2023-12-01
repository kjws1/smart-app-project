package com.example.a12_pa

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.a12_pa.databinding.ActivityAuthBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (MyApplication.checkAuth()) {
            changeVisibility("login")
        } else {
            changeVisibility("logout")
        }
        // when pressed signin
        binding.goSignInBtn.setOnClickListener {
            changeVisibility("signin")
        }

        // when pressed sign up button
        binding.signBtn.setOnClickListener {
            val email = binding.authEmailEditView.text.toString()
            val password = binding.authPasswordEditView.text.toString()
            val task = MyApplication.auth.createUserWithEmailAndPassword(email, password)
            task.addOnCompleteListener { result ->
                binding.authEmailEditView.text.clear()
                binding.authPasswordEditView.text.clear()
                // check if the user credentials are valid
                if (result.isSuccessful) {
                    val taskEmail = MyApplication.auth.currentUser?.sendEmailVerification()
                    taskEmail?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                baseContext,
                                "Please check your inbox and verify",
                                Toast.LENGTH_LONG
                            ).show()
                            changeVisibility("logout")
                        } else {
                            Toast.makeText(
                                baseContext,
                                "Failed to send verification email",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(baseContext, "Logging in failed", Toast.LENGTH_LONG).show()
                    changeVisibility("logout")
                }
            }
        }
        binding.loginBtn.setOnClickListener {
            //이메일, 비밀번호 로그인.......................
            val email = binding.authEmailEditView.text.toString()
            val password = binding.authPasswordEditView.text.toString()

            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                    if (task.isSuccessful) {
                        // check if email verification was successful
                        if (MyApplication.checkAuth()) {
                            MyApplication.email = email
                            changeVisibility("login")
                        } else {
                            Toast.makeText(
                                baseContext,
                                "전송된 메일로 이메일 인증이 되지 않았습니다.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    } else {
                        Toast.makeText(baseContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        binding.logoutBtn.setOnClickListener {
            //로그아웃...........
            MyApplication.auth.signOut()
            MyApplication.email = null
            changeVisibility("logout")
        }

        // Google Auth
        val requestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )
        {
            //구글 로그인 결과 처리...........................
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
                val credential: AuthCredential =
                    GoogleAuthProvider.getCredential(account.idToken, null)
                MyApplication.auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            MyApplication.email = account.email
                            changeVisibility("login")
                        } else {
                            changeVisibility("logout")
                        }
                    }
            } catch (e: ApiException) {
                e.printStackTrace()
                changeVisibility("logout")
            }
        }

        binding.googleLoginBtn.setOnClickListener {
            //구글 로그인....................
            val gso: GoogleSignInOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            val signInClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso)
            val signInIntent = signInClient.signInIntent
            requestLauncher.launch(signInIntent)
        }
    } // oncreate

    private fun changeVisibility(mode: String) {
        if (mode === "login") {
            binding.run {
                authMainTextView.text = "${MyApplication.email} 님 반갑습니다."
                logoutBtn.visibility = View.VISIBLE
                goSignInBtn.visibility = View.GONE
                googleLoginBtn.visibility = View.GONE
                authEmailEditView.visibility = View.GONE
                authPasswordEditView.visibility = View.GONE
                signBtn.visibility = View.GONE
                loginBtn.visibility = View.GONE
            }

        } else if (mode === "logout") {
            binding.run {
                authMainTextView.text = "로그인 하거나 회원가입 해주세요."
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.VISIBLE
                googleLoginBtn.visibility = View.VISIBLE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.GONE
                loginBtn.visibility = View.VISIBLE
            }
        } else if (mode === "signin") { // sign up
            binding.run {
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.GONE
                googleLoginBtn.visibility = View.GONE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.VISIBLE
                loginBtn.visibility = View.GONE
            }
        }
    }
}