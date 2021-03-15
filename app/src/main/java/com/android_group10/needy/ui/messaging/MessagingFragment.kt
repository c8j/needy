package com.android_group10.needy.ui.messaging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.android_group10.needy.R
import com.android_group10.needy.databinding.FragmentMessagingBinding
import com.android_group10.needy.messaging.MessagingFragmentViewModel
import com.android_group10.needy.ui.messaging.conversations.ConversationsFragment
import com.android_group10.needy.ui.messaging.requests.RequestsFragment
import com.google.android.material.color.MaterialColors
import com.google.android.material.tabs.TabLayoutMediator


class MessagingFragment : Fragment() {

    private var _binding: FragmentMessagingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by navGraphViewModels<MessagingFragmentViewModel>(R.id.main_graph)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMessagingBinding.inflate(inflater, container, false)
        binding.viewPager.adapter = MessagingPagerAdapter(
            listOf(
                RequestsFragment(this::changeTab),
                ConversationsFragment()
            ), this
        )

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.messaging_tab_label_requests)
                    tab.setIcon(R.drawable.ic_messaging_requests)
                    tab.orCreateBadge.apply {
                        isVisible = false
                        backgroundColor = MaterialColors.getColor(binding.root, R.attr.colorAccent)
                        maxCharacterCount = 2
                        viewModel.requestsCounter.observe(viewLifecycleOwner) { counter ->
                            if (counter == 0) {
                                isVisible = false
                            } else {
                                number = counter
                                isVisible = true
                            }
                        }
                    }
                }
                1 -> {
                    tab.text = getString(R.string.messaging_tab_label_messages)
                    tab.setIcon(R.drawable.ic_messaging_chat)
                    tab.orCreateBadge.apply {
                        isVisible = false
                        backgroundColor = MaterialColors.getColor(binding.root, R.attr.colorAccent)
                        maxCharacterCount = 2
                        viewModel.unreadConversationsCounter.observe(viewLifecycleOwner) { counter ->
                            if (counter == 0) {
                                isVisible = false
                            } else {
                                number = counter
                                isVisible = true
                            }
                        }
                    }
                }
            }
        }.attach()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changeTab(tabIndex: Int) {
        binding.viewPager.apply {
            if (!isDetached && currentItem == 0) {
                currentItem = tabIndex
            }
        }
    }
}