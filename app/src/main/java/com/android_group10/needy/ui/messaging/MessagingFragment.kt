package com.android_group10.needy.ui.messaging

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.android_group10.needy.R
import com.android_group10.needy.databinding.FragmentMessagingBinding
import com.android_group10.needy.messaging.MessagingFragmentViewModel
import com.google.android.material.tabs.TabLayoutMediator


class MessagingFragment : Fragment() {

    private var _binding: FragmentMessagingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MessagingFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMessagingBinding.inflate(inflater, container, false)
        binding.viewPager.adapter = MessagingPagerAdapter(viewModel.pagerFragments, this)

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