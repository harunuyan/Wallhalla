package com.volie.wallhalla.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.volie.wallhalla.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private var _mBinding: FragmentSignupBinding? = null
    private val mBinding get() = _mBinding!!
    private val mViewModel: AuthViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentSignupBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.tvSkipLogin.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null) {
                mViewModel.onLogOut()
            }
            mViewModel.signInGuest()
            val action = SignupFragmentDirections.actionSignupFragmentToHomeFragment()
            findNavController().navigate(action)
        }

        mBinding.tvLogin.setOnClickListener {
            val action = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        mBinding.signUpButton.setOnClickListener {
            val fullName = mBinding.etFullName.text.toString().trim()
            val email = mBinding.etEmail.text.toString().trim()
            val password = mBinding.etPassword.text.toString()

            if (fullName.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please fill in the fields", Toast.LENGTH_SHORT)
                    .show()
            } else if (password.length < 6) {
                mBinding.tvPasswordHint.visibility = View.VISIBLE

            } else {
                mBinding.tvPasswordHint.visibility = View.GONE
                mViewModel.onSignup(fullName, email, password)

                val action = SignupFragmentDirections.actionSignupFragmentToHomeFragment()
                findNavController().navigate(action)
            }
        }
        observeLiveData()
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