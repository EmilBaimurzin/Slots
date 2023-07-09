package com.bossbon.bossnza.game.game.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bossbon.bossnza.R
import com.bossbon.bossnza.databinding.DialogWheelBinding
import com.bossbon.bossnza.game.game.FragmentGameDirections
import com.bossbon.bossnza.game.library.ViewBindingDialog
import com.bossbon.bossnza.game.library.soundClickListener

class DialogWheel : ViewBindingDialog<DialogWheelBinding>(DialogWheelBinding::inflate) {
    private val args: DialogWheelArgs by navArgs()
    private var winnings = 0L
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.Dialog_No_Border)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        winnings = args.winnings
        dialog!!.setCancelable(false)

        binding.closeButton.soundClickListener {
            findNavController().popBackStack()
        }
        binding.spinButton.soundClickListener {
            findNavController().popBackStack()
            findNavController().navigate(
                FragmentGameDirections.actionFragmentGameToWheelFragment(
                winnings))
        }
    }
}