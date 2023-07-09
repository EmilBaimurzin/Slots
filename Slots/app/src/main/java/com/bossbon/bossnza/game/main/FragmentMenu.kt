package com.bossbon.bossnza.game.main

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bossbon.bossnza.R
import com.bossbon.bossnza.databinding.FragmentMenuBinding
import com.bossbon.bossnza.game.other.ViewBindingFragment
import com.bossbon.bossnza.game.library.soundClickListener

class FragmentMenu : ViewBindingFragment<FragmentMenuBinding>(FragmentMenuBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            playButton.soundClickListener {
                findNavController().navigate(FragmentMenuDirections.actionFragmentMenuToFragmentGame())
            }

            settingsButton.soundClickListener {
                findNavController().navigate(R.id.action_fragmentMenu_to_fragmentSettings)
            }

            infoButton.soundClickListener {
                findNavController().navigate(R.id.action_fragmentMenu_to_fragmentRules)
            }
        }
    }
}