package com.bossbon.bossnza.game.main.components

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bossbon.bossnza.databinding.FragmentSettingsBinding
import com.bossbon.bossnza.game.other.ViewBindingFragment
import com.bossbon.bossnza.game.library.soundClickListener

class FragmentSettings :
    ViewBindingFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {
    private val sharedPreferences: SharedPreferences by lazy {
        requireActivity().getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonsText()
        binding.resetButton.soundClickListener {
            Toast.makeText(requireContext(), "Your Balance was reset to 5000", Toast.LENGTH_SHORT)
                .show()
            sharedPreferences.edit().putLong("BALANCE", 5000).apply()
        }
        binding.soundButton.soundClickListener {
            val soundValue = sharedPreferences.getBoolean("SOUND", true)
            sharedPreferences.edit().putBoolean("SOUND", !soundValue).apply()
            setButtonsText()
        }

        binding.menuButton.soundClickListener {
            findNavController().popBackStack()
        }

        binding.exitGameButton.soundClickListener {
            requireActivity().finish()
        }
    }

    private fun setButtonsText() {
        if (sharedPreferences.getBoolean("SOUND", true)) {
            binding.soundButton.text = "Volume: On"
        } else {
            binding.soundButton.text = "Volume: Off"
        }
    }
}