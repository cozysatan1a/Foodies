package com.cozysatan1a.foodies.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cozysatan1a.foodies.databinding.AboutBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AboutBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "AboutBottomDialog"
    }

    private var _binding: AboutBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AboutBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val manager = context?.packageManager
            val info = manager?.getPackageInfo(
                requireContext().packageName, 0
            )

            val versionName = info?.versionName
            val versionNumber = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info?.longVersionCode
            } else {
                info?.versionCode
            }

            val appVersionTxt = "$versionName ($versionNumber)"
            appVersion.text = appVersionTxt

            repoLink.setOnClickListener {
                val url: Uri = Uri.parse("https://github.com/cozysatan1a/Foodies")
                val intent = Intent(Intent.ACTION_VIEW, url)
                if (intent.resolveActivity(requireContext().packageManager) != null) {
                    startActivity(intent)
                }
            }

            linkedInLink.setOnClickListener {

            }

            githubLink.setOnClickListener {
                val recipeUrl: Uri = Uri.parse("https://github.com/cozysatan1a")
                val intent = Intent(Intent.ACTION_VIEW, recipeUrl)
                if (intent.resolveActivity(requireContext().packageManager) != null) {
                    startActivity(intent)
                }
            }

            mailLink.setOnClickListener {

            }
        }
    }

    fun newInstance(): AboutBottomSheetFragment {
        return AboutBottomSheetFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}