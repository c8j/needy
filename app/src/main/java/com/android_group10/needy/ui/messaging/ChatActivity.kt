package com.android_group10.needy.ui.messaging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_group10.needy.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding

    private val viewModel by viewModels<ChatActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        viewModel.retrieveMessages()
        binding.recyclerView.apply {
            adapter = ChatMessageAdapter(viewModel.messageList)
            layoutManager = LinearLayoutManager(this@ChatActivity)
        }
        setContentView(binding.root)

        setSupportActionBar(binding.actionBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //Dummy title
        supportActionBar?.title = "John Doe"
    }
}