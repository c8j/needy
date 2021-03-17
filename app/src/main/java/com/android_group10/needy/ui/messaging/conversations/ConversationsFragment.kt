package com.android_group10.needy.ui.messaging.conversations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_group10.needy.R
import com.android_group10.needy.databinding.FragmentMessagingConversationsBinding
import com.android_group10.needy.messaging.MessagingFragmentViewModel
import com.android_group10.needy.messaging.util.FirestoreUtil

class ConversationsFragment : Fragment() {

    private var _binding: FragmentMessagingConversationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by navGraphViewModels<MessagingFragmentViewModel>(R.id.main_graph)

    private var shouldInitRecyclerView = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMessagingConversationsBinding.inflate(inflater, container, false)
        binding.lpiConversations.setVisibilityAfterHide(View.GONE)
        if (shouldInitRecyclerView) {
            initRecyclerView()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        shouldInitRecyclerView = true
        _binding = null
    }

    private fun progressBarVisibility(isVisible: Boolean){
        binding.lpiConversations.apply {
            if (isVisible){
                show()
            } else {
                hide()
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            val listAdapter =
                ConversationsFragmentAdapter(this@ConversationsFragment::progressBarVisibility, resources.getString(R.string.messaging_conversation_pronoun))
            viewModel.conversations.observe(viewLifecycleOwner) { conversationQueryItemList ->
                if (conversationQueryItemList != null) {
                    listAdapter.submitList(conversationQueryItemList)
                }
            }
            adapter = listAdapter
        }
        shouldInitRecyclerView = false
    }
}