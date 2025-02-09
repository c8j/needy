package com.android_group10.needy.ui.messaging.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_group10.needy.R
import com.android_group10.needy.databinding.FragmentMessagingRequestsBinding
import com.android_group10.needy.messaging.MessagingFragmentViewModel

class RequestsFragment(private val tabCallback: (selectTab: Int) -> Unit) : Fragment() {

    private var _binding: FragmentMessagingRequestsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by navGraphViewModels<MessagingFragmentViewModel>(R.id.main_graph)

    private var shouldInitRecyclerView = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessagingRequestsBinding.inflate(inflater, container, false)
        binding.lpiRequests.setVisibilityAfterHide(View.GONE)
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
        binding.lpiRequests.apply {
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
            val listAdapter = RequestsFragmentAdapter(this@RequestsFragment::progressBarVisibility, tabCallback)
            val requestsLiveData = viewModel.requests
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