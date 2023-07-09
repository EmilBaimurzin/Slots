package com.bossbon.bossnza.game.game

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bossbon.bossnza.game.other.ViewBindingFragment
import com.bossbon.bossnza.R
import com.bossbon.bossnza.databinding.FragmentGameBinding
import com.bossbon.bossnza.game.game.recyclerView.Adapter
import com.bossbon.bossnza.game.library.*

class FragmentGame :
    ViewBindingFragment<FragmentGameBinding>(FragmentGameBinding::inflate), GameInterface {
    private val viewModel: SlotGameViewModel by viewModels {
        SlotViewModelFactory(true)
    }
    private val sp: SharedPreferences by lazy {
        requireActivity().getSharedPreferences("SHARED_PREFS", MODE_PRIVATE)
    }
    private lateinit var playerRoulette: MediaPlayer
    private lateinit var adapter1: Adapter
    private lateinit var adapter2: Adapter
    private lateinit var adapter3: Adapter
    private val adapters: List<Adapter> by lazy {
        listOf(adapter1, adapter2, adapter3)
    }
    private val slots: List<RecyclerView> by lazy {
        listOf(binding.slot1, binding.slot2, binding.slot3)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter1 = initAdapter(binding.slot1, requireContext())
        adapter2 = initAdapter(binding.slot2, requireContext())
        adapter3 = initAdapter(binding.slot3, requireContext())
        addListeners()
        addOnScrollListeners()
        setValues()
        viewModel.currentList.observe(viewLifecycleOwner) {
            adapter1.items = it.s1
            adapter2.items = it.s2
            adapter3.items = it.s3
            adapters.forEach { adapter -> adapter.notifyDataSetChanged() }
            slots.forEach { slots -> slots.scrollToPosition(0) }
        }
        viewModel.spinState.observe(viewLifecycleOwner) {
            binding.apply {
                lessButton.isEnabled = !it
                moreButton.isEnabled = !it
                spinButton.isEnabled = !it
                betEditText.isEnabled = !it
            }
        }
    }

    private fun addListeners() {
        binding.apply {
            bidListeners(viewModel.currentBid,
                sp,
                requireContext(),
                betEditText,
                lessButton,
                moreButton,
                spinButton,
                ::spin
            ) { viewModel.currentBid = it }
        }
    }

    private fun addOnScrollListeners() {
        scrollListener(binding.slot1, viewModel.spinState.value!!, {
            smoothScroll(binding.slot1, getValueOfIndex(0))
        }, {
            viewModel.position1 = it
        })

        scrollListener(binding.slot2, viewModel.spinState.value!!, {
            smoothScroll(binding.slot2, getValueOfIndex(1))
        }, {
            viewModel.position2 = it
        })

        binding.slot3.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lm = binding.slot3.layoutManager as LinearLayoutManager
                val lastPosition = lm.findLastVisibleItemPosition()
                viewModel.position3 = lastPosition
                if (lastPosition == 49 && viewModel.spinState.value!!) {
                    spinEnding()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (viewModel.spinState.value!!) {
                    smoothScroll(recyclerView, 49)
                }
            }
        })
    }

    private fun spinEnding() {
        viewModel.changeSpinningValue(false)
        safe { playerRoulette.stop() }
        checkWin()
        viewModel.changeItems()
    }

    private fun checkWin() {
        when (viewModel.checkWin().max()) {
            1 -> {
                if (getVolumeState(sp)) MediaPlayer.create(requireContext(), R.raw.sound_lose)
                    .start()
            }
            2 -> {
                val winnings = viewModel.currentBid * 2
                MediaPlayer.create(requireContext(), R.raw.sound_win).start()
                increaseBalance(sp, winnings)
                sp.edit().putLong("LAST_WIN", winnings).apply()
                setValues()
                when (1 random 9) {
                    1 -> findNavController().navigate(FragmentGameDirections.actionFragmentGameToDialogWheel(
                        winnings))
                    2 -> {
                        findNavController().navigate(FragmentGameDirections.actionFragmentGameToDialogLottery(
                            winnings))
                    }
                    else -> {}
                }
            }
            3 -> {
                val winnings = viewModel.currentBid * 5
                if (getVolumeState(sp)) MediaPlayer.create(requireContext(), R.raw.sound_win)
                    .start()
                increaseBalance(sp, winnings)
                sp.edit().putLong("LAST_WIN", winnings).apply()
                setValues()
                when (1 random 9) {
                    1 -> findNavController().navigate(FragmentGameDirections.actionFragmentGameToDialogWheel(
                        winnings))
                    2 -> {
                        findNavController().navigate(FragmentGameDirections.actionFragmentGameToDialogLottery(
                            winnings))
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setValues() {
        binding.balanceTextView.text =  balance(sp).toString()
        binding.winTextView.text = sp.getLong("LAST_WIN", 0L).toString()
    }

    private fun spin() {
        setValues()
        if (getVolumeState(sp)) {
            playerRoulette = MediaPlayer.create(requireContext(), R.raw.sound_wheel)
            playerRoulette.start()
        }
        viewModel.changeSpinningValue(true)
        slots.forEachIndexed { index, recyclerView ->
            smoothScroll(recyclerView, getValueOfIndex(index))
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.spinState.value!!) {
            slots.forEachIndexed { index, slot ->
                when (index) {
                    0 -> {
                        slot.scrollToPosition(viewModel.position1)
                        smoothScroll(slot, getValueOfIndex(index))
                    }
                    1 -> {
                        slot.scrollToPosition(viewModel.position2)
                        smoothScroll(slot, getValueOfIndex(index))
                    }
                    else -> {
                        slot.scrollToPosition(viewModel.position3)
                        smoothScroll(slot, getValueOfIndex(index))
                    }
                }
            }
            safe {
                if (getVolumeState(sp)) {
                    playerRoulette = MediaPlayer.create(requireContext(), R.raw.sound_wheel)
                    playerRoulette.seekTo(viewModel.audioLength)
                    playerRoulette.start()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.apply {
            slots.forEach {
                it.stopScroll()
            }
        }
        safe {
            viewModel.audioLength = playerRoulette.currentPosition
            playerRoulette.pause()
        }
    }
}