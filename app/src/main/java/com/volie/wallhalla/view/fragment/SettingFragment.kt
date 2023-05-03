package com.volie.wallhalla.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.volie.wallhalla.R
import com.volie.wallhalla.databinding.BottomSheetLayoutChooseThemeBinding
import com.volie.wallhalla.databinding.FragmentSettingBinding
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

        with(mBinding) {
            llTheme.setOnClickListener {
                val bottomSheetDialog =
                    BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
                val bottomSheetView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.bottom_sheet_layout_choose_theme, root, false)
                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.show()

                val mBindingBottomSheet = BottomSheetLayoutChooseThemeBinding.bind(bottomSheetView)

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}