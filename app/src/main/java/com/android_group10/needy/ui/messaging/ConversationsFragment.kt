package com.android_group10.needy.ui.messaging

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.android_group10.needy.R
import com.android_group10.needy.databinding.FragmentMessagingConversationsBinding
import com.google.android.material.tabs.TabLayoutMediator


class ConversationsFragment : Fragment() {

    private var _binding: FragmentMessagingConversationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ConversationsFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMessagingConversationsBinding.inflate(inflater, container, false)
        viewModel.init()
        binding.viewPager.adapter = ConversationsPagerAdapter(viewModel.pagerFragments, this)

        //TODO: implement badges for incoming requests/unread messages
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.conversations_tab_label_requests)
                1 -> tab.text = getString(R.string.conversations_tab_label_messages)
            }
        }.attach()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}