package com.android_group10.needy.ui.messaging.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_group10.needy.databinding.FragmentMessagingRequestsBinding
import com.android_group10.needy.messaging.MessagingFragmentViewModel

class RequestsFragment : Fragment() {

    private var _binding: FragmentMessagingRequestsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MessagingFragmentViewModel>({ requireParentFragment() })

    private var shouldInitRecyclerView = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessagingRequestsBinding.inflate(inflater, container, false)
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

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            val listAdapter = RequestsFragmentAdapter()
            val requestsLiveData = viewModel.getRequests()
            requestsLiveData.observe(viewLifecycleOwner) { requestQueryItemList ->
                if (requestQueryItemList != null) {
                    listAdapter.submitList(requestQueryItemList)
                }
            }
            adapter = listAdapter
        }
        shouldInitRecyclerView = false
    }
}