package com.android_group10.needy.ui.messaging.chat

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_group10.needy.databinding.ActivityChatBinding
import com.android_group10.needy.messaging.ChatActivityViewModel
import com.android_group10.needy.messaging.util.ChatConstants
import kotlinx.coroutines.Runnable

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding

    private val viewModel by viewModels<ChatActivityViewModel>()

    private var shouldInitRecyclerView = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.actionBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel.conversationUID = intent.getStringExtra(ChatConstants.CONVERSATION_UID) ?: ""
        supportActionBar?.title =
            intent.getStringExtra(ChatConstants.PARTNER_FULL_NAME) ?: "John Doe"
        viewModel.partnerUID = intent.getStringExtra(ChatConstants.PARTNER_UID) ?: ""

        if (shouldInitRecyclerView) {
            initRecyclerView()
        }

        binding.apply {
            iBtnSend.setOnClickListener {
                val messageText = etMessage.text.toString()
                if (messageText != "") {
                    viewModel.sendMessage(etMessage.text.toString()) { message ->
                        if (message != null) {
                            Toast.makeText(this@ChatActivity, message, Toast.LENGTH_SHORT).show()
                        } else {
                            etMessage.setText("")
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        shouldInitRecyclerView = true
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
                if (bottom < oldBottom) {
                    this.post(Runnable { this@apply.scrollToPosition(0) })
                }
            }

            layoutManager = LinearLayoutManager(this@ChatActivity)
            (layoutManager as LinearLayoutManager).reverseLayout = true
            (layoutManager as LinearLayoutManager).stackFromEnd = true
            val listAdapter = ChatMessageAdapter()
            if (viewModel.conversationUID != "") {
                viewModel.retrieveMessages().observe(this@ChatActivity) { chatMessageQueryList ->
                    if (chatMessageQueryList != null) {
                        listAdapter.submitList(
                            chatMessageQueryList,
                            Runnable { this.scrollToPosition(0) })
                    }
                }
            }
            adapter = listAdapter
        }
        shouldInitRecyclerView = false
    }
}