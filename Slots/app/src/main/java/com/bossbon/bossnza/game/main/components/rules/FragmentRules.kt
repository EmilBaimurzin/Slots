package com.bossbon.bossnza.game.main.components.rules

import android.os.Bundle
import android.view.View
import com.bossbon.bossnza.game.other.ViewBindingFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.bossbon.bossnza.databinding.FragmentRulesBinding

class FragmentRules : ViewBindingFragment<FragmentRulesBinding>(FragmentRulesBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rulesVP.adapter =
            RulesAdapter(childFragmentManager, lifecycle, arrayListOf(Page1(), Page2()))
        TabLayoutMediator(binding.dotsTabLayout, binding.rulesVP) { _, _ -> }.attach()
    }
}