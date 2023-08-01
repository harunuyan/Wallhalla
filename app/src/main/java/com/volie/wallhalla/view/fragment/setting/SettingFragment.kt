package com.volie.wallhalla.view.fragment.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.volie.wallhalla.R
import com.volie.wallhalla.databinding.BottomSheetLayoutChooseThemeBinding
import com.volie.wallhalla.databinding.FragmentSettingBinding
import com.volie.wallhalla.util.Constant.GITHUB_GIST_URL
import com.volie.wallhalla.util.Constant.GITHUB_REPO_URL
import com.volie.wallhalla.util.Constant.GOOGLE_PLAY_URL
import com.volie.wallhalla.view.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private var _mBinding: FragmentSettingBinding? = null
    private val mBinding get() = _mBinding!!

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

        getThemePreference()

        with(mBinding) {
            llTheme.setOnClickListener {
                showThemeBottomSheet()
            }

            flRateApp.setOnClickListener {
                rateApp()
            }
            llRecommend.setOnClickListener {
                shareApp()
            }

            llAppVersion.setOnClickListener {
                showGithubRepository()
            }

            flPrivacyPolicy.setOnClickListener {
                showPrivacyPolicy()
            }

            llSendFeedback.setOnClickListener {
                openEmailComposer()
            }

        }
    }

    private fun getThemePreference() {
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
    }

    private fun showThemeBottomSheet() {
        with(mBinding) {
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
    }

    private fun openEmailComposer() {
        val recipient = "harunuyan6@gmail.com"
        val subject = getString(R.string.app_feedback)
        val message =
            "${getString(R.string.hello)}\n${getString(R.string.feedback_message)}\n\n"

        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$recipient")
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }

        if (emailIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(emailIntent)
        } else {
            Toast.makeText(
                requireContext(),
                "E-mail application not found!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showPrivacyPolicy() {
        val myGithubProfile = GITHUB_GIST_URL
        val action =
            SettingFragmentDirections.actionSettingFragmentToPhotographerFragment(
                myGithubProfile
            )
        findNavController().navigate(action)
    }

    private fun shareApp() {
        val url = GOOGLE_PLAY_URL
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            getString(R.string.download_wallhalla)
        )
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            "${getString(R.string.download_wallhalla)}\n$url"
        )
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun rateApp() {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(GOOGLE_PLAY_URL)
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showGithubRepository() {
        val myGithubProfile = GITHUB_REPO_URL
        val action =
            SettingFragmentDirections.actionSettingFragmentToPhotographerFragment(
                myGithubProfile
            )
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}