package com.volie.wallhalla.view.fragment.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.volie.wallhalla.R
import com.volie.wallhalla.auth.AuthViewModel
import com.volie.wallhalla.data.model.Quality
import com.volie.wallhalla.data.model.Theme
import com.volie.wallhalla.databinding.BottomSheetLayoutChooseQualityBinding
import com.volie.wallhalla.databinding.BottomSheetLayoutChooseThemeBinding
import com.volie.wallhalla.databinding.FragmentSettingBinding
import com.volie.wallhalla.util.Constant.GITHUB_GIST_URL
import com.volie.wallhalla.util.Constant.GITHUB_REPO_URL
import com.volie.wallhalla.util.Constant.GOOGLE_PLAY_URL
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private var _mBinding: FragmentSettingBinding? = null
    private val mBinding get() = _mBinding!!
    private val mViewModel: SettingViewModel by viewModels()
    private val mAuthViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentSettingBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showUserInfo()
        getThemeFlow()
        getQualityFlow()

        with(mBinding) {
            ivLogout.setOnClickListener {
                mAuthViewModel.onLogOut()
                val action = SettingFragmentDirections.actionSettingFragmentToLoginFragment()
                findNavController().navigate(action)
            }
            llTheme.setOnClickListener {
                showThemeBottomSheet()
            }

            llQuality.setOnClickListener {
                showQualityBottomSheet()
            }

            tvRateApp.setOnClickListener {
                rateApp()
            }
            llRecommend.setOnClickListener {
                shareApp()
            }

            llAppVersion.setOnClickListener {
                showGithubRepository()
            }

            tvPrivacyPolicy.setOnClickListener {
                showPrivacyPolicy()
            }

            llSendFeedback.setOnClickListener {
                openEmailComposer()
            }
        }
    }

    private fun showUserInfo() {
        mAuthViewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                mBinding.tvDisplayName.text = it.fullName
                mBinding.tvEmail.visibility = View.VISIBLE
                mBinding.tvEmail.text = it.email
            } else {
                mBinding.tvEmail.visibility = View.GONE
                mBinding.ivLogout.setImageResource(R.drawable.ic_login)
            }
            if (it?.profilePic != null) {
                Glide.with(requireContext()).load(it.profilePic).into(mBinding.ivUserProfile)
            }
        }
    }

    private fun showQualityBottomSheet() {
        with(mBinding) {
            val bottomSheetDialog =
                BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
            val bottomSheetView = LayoutInflater.from(requireContext())
                .inflate(R.layout.bottom_sheet_layout_choose_quality, root, false)
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()

            val mBindingBottomSheet = BottomSheetLayoutChooseQualityBinding.bind(bottomSheetView)

            with(mBindingBottomSheet) {
                with(bottomSheetDialog) {
                    tvOriginal.setOnClickListener {
                        mViewModel.saveQuality(Quality.ORIGINAL)
                        tvQuality.text = getString(R.string.original)
                        dismiss()
                    }
                    tvLarge2x.setOnClickListener {
                        mViewModel.saveQuality(Quality.LARGE_2X)
                        tvQuality.text = getString(R.string.large2x)
                        dismiss()
                    }
                    tvLarge.setOnClickListener {
                        mViewModel.saveQuality(Quality.LARGE)
                        tvQuality.text = getString(R.string.large)
                        dismiss()
                    }
                    tvMedium.setOnClickListener {
                        mViewModel.saveQuality(Quality.MEDIUM)
                        tvQuality.text = getString(R.string.medium)
                        dismiss()
                    }
                    tvSmall.setOnClickListener {
                        mViewModel.saveQuality(Quality.SMALL)
                        tvQuality.text = getString(R.string.small)
                        dismiss()
                    }
                    tvPortrait.setOnClickListener {
                        mViewModel.saveQuality(Quality.PORTRAIT)
                        tvQuality.text = getString(R.string.portrait)
                        dismiss()
                    }
                    tvLandscape.setOnClickListener {
                        mViewModel.saveQuality(Quality.LANDSCAPE)
                        tvQuality.text = getString(R.string.landscape)
                        dismiss()
                    }
                    tvTiny.setOnClickListener {
                        mViewModel.saveQuality(Quality.TINY)
                        tvQuality.text = getString(R.string.tiny)
                        dismiss()
                    }
                }
            }
        }
    }

    private fun getThemeFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.selectedTheme.collect { savedTheme ->
                with(mBinding.tvTheme) {
                    text = when (savedTheme) {
                        Theme.DARK -> getString(R.string.dark_theme)
                        Theme.LIGHT -> getString(R.string.light_theme)
                        Theme.SYSTEM -> getString(R.string.system_default)
                    }
                }
            }
        }
    }

    private fun getQualityFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.selectedQuality.collect { savedQuality ->
                with(mBinding.tvQuality) {
                    text = when (savedQuality) {
                        Quality.ORIGINAL -> getString(R.string.original)
                        Quality.LARGE_2X -> getString(R.string.large2x)
                        Quality.LARGE -> getString(R.string.large)
                        Quality.MEDIUM -> getString(R.string.medium)
                        Quality.SMALL -> getString(R.string.small)
                        Quality.PORTRAIT -> getString(R.string.portrait)
                        Quality.LANDSCAPE -> getString(R.string.landscape)
                        Quality.TINY -> getString(R.string.tiny)
                    }
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
                    tvLightTheme.setOnClickListener {
                        mViewModel.saveTheme(Theme.LIGHT)
                        tvTheme.text = getString(R.string.light_theme)
                        dismiss()
                    }

                    tvDarkTheme.setOnClickListener {
                        mViewModel.saveTheme(Theme.DARK)
                        tvTheme.text = getString(R.string.dark_theme)
                        dismiss()
                    }

                    tvSystemDefaultTheme.setOnClickListener {
                        mViewModel.saveTheme(Theme.SYSTEM)
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
        val message = "${getString(R.string.hello)}\n${getString(R.string.feedback_message)}\n\n"

        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$recipient")
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }

        if (emailIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(emailIntent)
        } else {
            Toast.makeText(
                requireContext(), "E-mail application not found!", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showPrivacyPolicy() {
        val myGithubProfile = GITHUB_GIST_URL
        val action = SettingFragmentDirections.actionSettingFragmentToPhotographerFragment(
            myGithubProfile
        )
        findNavController().navigate(action)
    }

    private fun shareApp() {
        val url = GOOGLE_PLAY_URL
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_SUBJECT, getString(R.string.download_wallhalla)
        )
        shareIntent.putExtra(
            Intent.EXTRA_TEXT, "${getString(R.string.download_wallhalla)}\n$url"
        )
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun rateApp() {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_URL)
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showGithubRepository() {
        val myGithubProfile = GITHUB_REPO_URL
        val action = SettingFragmentDirections.actionSettingFragmentToPhotographerFragment(
            myGithubProfile
        )
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}