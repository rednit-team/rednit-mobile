package com.github.rednit.swipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.github.rednit.api.TinderConnection
import com.github.rednit.databinding.DialogNewMatchBinding
import com.github.rednit.util.ImageUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatchDialogFragment(private val matchId: String) :
    DialogFragment() {

    private lateinit var binding: DialogNewMatchBinding
    private val imageUtil = ImageUtil(this)
    private val connection = TinderConnection.connection

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogNewMatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            val match = withContext(Dispatchers.IO) { connection.getMatch(matchId) }
            imageUtil.bitmap(match.matchedUser.photos[0].url).into(binding.imageMatched)
            val self = withContext(Dispatchers.IO) { connection.getSelfUser() }
            imageUtil.bitmap(self.photos[0].url).into(binding.imageSelf)
        }

        binding.buttonKeepSwiping.setOnClickListener {
            dismiss()
        }

        binding.buttonOpenChat.setOnClickListener {
            Toast.makeText(context, "Not implemented yet!", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        binding.buttonDeleteMatch.setOnClickListener {
            Toast.makeText(context, "Not implemented yet!", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
}
