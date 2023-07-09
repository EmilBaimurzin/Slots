package com.bossbon.bossnza.game.lottery

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bossbon.bossnza.R
import com.bossbon.bossnza.databinding.FragmentLotteryBinding
import com.bossbon.bossnza.game.library.getAmountOfSameValuesInList
import com.bossbon.bossnza.game.library.getVolumeState
import com.bossbon.bossnza.game.library.increaseBalance
import com.bossbon.bossnza.game.library.shortToast
import com.bossbon.bossnza.game.other.ViewBindingFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentLottery :
    ViewBindingFragment<FragmentLotteryBinding>(FragmentLotteryBinding::inflate) {
    private val viewModel: LotteryViewModel by viewModels()
    private val args: FragmentLotteryArgs by navArgs()
    private var reward = 0L
    private val sharedPreferences: SharedPreferences by lazy {
        requireActivity().getSharedPreferences("SHARED_PREFS", MODE_PRIVATE)
    }
    private lateinit var lotteryAdapter: LotteryAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reward = args.reward
        initAdapter()

        viewModel.items.observe(viewLifecycleOwner) { items ->
            lotteryAdapter.items = items
            lotteryAdapter.notifyDataSetChanged()

            val openedCards = items.filter { it.isOpened }
            val openedCardsValues = openedCards.map { it.itemValue }
            val sameCardsAmount2x = getAmountOfSameValuesInList(openedCardsValues, 2)
            val sameCardsAmount3x = getAmountOfSameValuesInList(openedCardsValues, 3)
            val sameCardsAmount5x = getAmountOfSameValuesInList(openedCardsValues, 5)
            val maxSames = listOf(sameCardsAmount2x, sameCardsAmount3x, sameCardsAmount5x)

            lifecycleScope.launch {
                val navigateBack = {
                    findNavController().popBackStack(
                        R.id.fragmentMenu,
                        false,
                        saveState = false
                    )
                    findNavController().navigate(R.id.action_fragmentMenu_to_fragmentGame)
                }
                if (openedCards.size == 5 && maxSames.max() < 3) {
                    delay(500)
                    if (sharedPreferences.getBoolean("SOUND", true)) {
                        MediaPlayer.create(context, R.raw.sound_lose).start()
                    }
                    navigateBack.invoke()
                }

                if (openedCards.size == 5 && maxSames.max() == 3) {
                    delay(500)
                    if (sharedPreferences.getBoolean("SOUND", true)) {
                        MediaPlayer.create(context, R.raw.sound_win).start()
                    }
                    val increase: (multiplier: Double) -> Unit = {
                        increaseBalance(sharedPreferences, (reward * it).toLong())
                    }
                    when {
                        maxSames[0] == 3 -> {
                            increase.invoke(2.0)
                            navigateBack.invoke()
                            sharedPreferences.edit().putLong("LAST_WIN", (reward * 2).toLong()).apply()
                            shortToast(requireContext(), "You won ${reward * 2}")
                        }
                        maxSames[1] == 3 -> {
                            increase.invoke(2.5)
                            navigateBack.invoke()
                            sharedPreferences.edit().putLong("LAST_WIN", (reward * 2.5).toLong()).apply()
                            shortToast(requireContext(), "You won ${reward * 2.5}")
                        }
                        maxSames[2] == 3 -> {
                            increase.invoke(3.0)
                            sharedPreferences.edit().putLong("LAST_WIN", (reward * 3).toLong()).apply()
                            shortToast(requireContext(), "You won ${reward * 3}")
                            navigateBack.invoke()
                        }
                    }
                }
            }
        }
    }

    private fun initAdapter() {
        with(binding.lotteryRecyclerView) {
            lotteryAdapter = LotteryAdapter(
                soundValue = getVolumeState(sharedPreferences),
                itemClickListener = { position, item ->
                    if (getClickState()) {
                        lifecycleScope.launch {
                            viewModel.changeClickState(false)
                            delay(500)
                            viewModel.openItem(position)
                            viewModel.changeClickState(true)
                        }
                    }
                })
            adapter = lotteryAdapter
            val lm = GridLayoutManager(context, 3)
            lm.orientation = GridLayoutManager.VERTICAL
            layoutManager = lm
            setHasFixedSize(true)
        }
    }

    private fun getClickState(): Boolean = viewModel.clickState.value!!
}