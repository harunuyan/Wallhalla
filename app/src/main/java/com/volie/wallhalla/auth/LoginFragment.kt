package com.volie.wallhalla.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.volie.wallhalla.R
import com.volie.wallhalla.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _mBinding: FragmentLoginBinding? = null
    private val mBinding get() = _mBinding!!
    private val mViewModel: AuthViewModel by viewModels()
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.signInButton.setOnClickListener {
            onLogin()
        }

        Log.i("Current user : ", FirebaseAuth.getInstance().currentUser?.email.toString())

        Glide.with(requireContext())
            .load("https://media1.giphy.com/media/v1.Y2lkPTc5MGI3NjExMGZoNzBybGd5Zm4wZ2EyeHpuYjY4dzRtNzJtY3JvNzNmbWVpMHEyayZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9cw/IzuCdpzRfdc0zuthDv/giphy.gif")
            .into(mBinding.ivTitle)

        mBinding.tvSignUp.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            findNavController().navigate(action)
        }

        mBinding.tvSkipLogin.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null) {
                mViewModel.onLogOut()
            }
            mViewModel.signInGuest()
            val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
            findNavController().navigate(action)
        }

        mBinding.signInGoogle.setOnClickListener {
            signInWithGoogle()
        }

        googleSignInLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        val account = task.getResult(ApiException::class.java)
                        mViewModel.handleGoogleSignInResult(account)
                    } catch (e: ApiException) {
                        Toast.makeText(requireContext(), "Google login failed.", Toast.LENGTH_SHORT)
                            .show()
                    }
                    if (task.isSuccessful) {
                        mBinding.tvSkipLogin.visibility = View.GONE
                        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        findNavController().navigate(action)
                    }
                }
            }

        observeLiveData()
    }

    private fun onLogin() {
        val email = mBinding.etEmail.text.toString().trim()
        val password = mBinding.etPassword.text.toString()

        mViewModel.onLogin(email = email, password = password) {
            val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        val signInIntent = mGoogleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun observeLiveData() {
        mViewModel.inProgress.observe(viewLifecycleOwner) {
            if (it) {
                mBinding.progressBar.visibility = View.VISIBLE
            } else {
                mBinding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}