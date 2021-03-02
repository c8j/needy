package com.android_group10.needy.ui.messaging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_group10.needy.databinding.FragmentMessagingRequestsBinding
import com.android_group10.needy.messaging.ConversationsFragmentViewModel
import com.google.firebase.firestore.ListenerRegistration

class RequestsFragment : Fragment() {

    private var _binding: FragmentMessagingRequestsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ConversationsFragmentViewModel>({requireParentFragment()})

    private var shouldInitRecyclerView = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessagingRequestsBinding.inflate(inflater, container, false)
        if (shouldInitRecyclerView){
            initRecyclerView()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView(){
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    (layoutManager as LinearLayoutManager).orientation
                )
            )

            val listAdapter = RequestsFragmentAdapter()
            val requestsLiveData = viewModel.getRequests()
            requestsLiveData.observe(viewLifecycleOwner){
                if (it != null){
                    listAdapter.submitList(it)
                }
            }
            adapter = listAdapter
        }
        shouldInitRecyclerView = false
    }
}