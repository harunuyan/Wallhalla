package com.volie.wallhalla.view.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.volie.wallhalla.R
import com.volie.wallhalla.databinding.BottomSheetLayoutChooseThemeBinding
import com.volie.wallhalla.databinding.FragmentSettingBinding
import com.volie.wallhalla.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private var _mBinding: FragmentSettingBinding? = null
    private val mBinding get() = _mBinding!!
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentSettingBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getSavedTheme = (requireActivity() as MainActivity).getSavedTheme()

        with(mBinding.tvTheme) {
            when (getSavedTheme) {
                AppCompatDelegate.MODE_NIGHT_NO -> {
                    text = getString(R.string.light_theme)
                }

                AppCompatDelegate.MODE_NIGHT_YES -> {
                    text = getString(R.string.dark_theme)
                }

                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                    text = getString(R.string.system_default)
                }
            }
        }

        with(mBinding) {
            llTheme.setOnClickListener {
                val bottomSheetDialog =
                    BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
                val bottomSheetView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.bottom_sheet_layout_choose_theme, root, false)
                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.show()

                val mBindingBottomSheet = BottomSheetLayoutChooseThemeBinding.bind(bottomSheetView)

                with(mBindingBottomSheet) {
                    with(bottomSheetDialog) {
                        flLightTheme.setOnClickListener {
                            (requireActivity() as MainActivity).saveTheme(AppCompatDelegate.MODE_NIGHT_NO)
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            tvTheme.text = getString(R.string.light_theme)
                            dismiss()
                        }

                        flDarkTheme.setOnClickListener {
                            (requireActivity() as MainActivity).saveTheme(AppCompatDelegate.MODE_NIGHT_YES)
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            tvTheme.text = getString(R.string.dark_theme)
                            dismiss()
                        }

                        flSystemDefaultTheme.setOnClickListener {
                            (requireActivity() as MainActivity).saveTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                            tvTheme.text = getString(R.string.system_default)
                            dismiss()
                        }
                    }
                }
            }

            llAppVersion.setOnLongClickListener {
                val myGithubProfile = "https://github.com/harunuyan/Wallhalla"
                val action =
                    SettingFragmentDirections.actionSettingFragmentToPhotographerFragment(
                        myGithubProfile
                    )
                findNavController().navigate(action)
                return@setOnLongClickListener true
            }

            flPrivacyPolicy.setOnLongClickListener {
                val myGithubProfile =
                    "https://gist.github.com/harunuyan/d55c56eafcf97d4e4234d5590e1ee2aa"
                val action =
                    SettingFragmentDirections.actionSettingFragmentToPhotographerFragment(
                        myGithubProfile
                    )
                findNavController().navigate(action)
                return@setOnLongClickListener true
            }

            llSendFeedback.setOnClickListener {
                val recipient = "harunuyan6@gmail.com"
                val subject = "App Feedback 'Wallhalla'"
                val message = "Hello,\nI want to give feedback about the app.\n\n"

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, message)
                    val chooserIntent = Intent.createChooser(this, "Send Email")
                    startActivity(chooserIntent)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}