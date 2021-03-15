package com.android_group10.needy.ui.messaging.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_group10.needy.R
import com.android_group10.needy.databinding.FragmentChatBinding
import com.android_group10.needy.messaging.MessagingFragmentViewModel
import com.android_group10.needy.messaging.util.FirestoreUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel by navGraphViewModels<MessagingFragmentViewModel>(R.id.main_graph)
    private val args by navArgs<ChatFragmentArgs>()

    private var shouldInitRecyclerView = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        //Get passed args
        viewModel.conversationUID = args.conversationUID

        if (shouldInitRecyclerView) {
            initRecyclerView()
        }

        binding.apply {
            iBtnSend.isEnabled = false

            etMessage.addTextChangedListener {
                iBtnSend.isEnabled = !it.isNullOrEmpty()
            }

            iBtnSend.setOnClickListener {
                val messageText = etMessage.text.toString()
                if (messageText != "") {
                    iBtnSend.isSelected = true
                    viewModel.sendMessage(etMessage.text.toString()) { message ->
                        if (message != null) {
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        } else {
                            etMessage.setText("")
                        }
                        iBtnSend.isSelected = false
                    }
                }
            }
        }

        //Check if conversation got concluded while chatting
        viewModel.getConversation().observe(viewLifecycleOwner) {
            if (it.concluded) {
                MaterialAlertDialogBuilder(
                    requireContext(),
                    R.style.ThemeOverlay_Needy_MaterialAlertDialog
                )
                    .setMessage(R.string.messaging_dialog_concluded)
                    .setPositiveButton(R.string.messaging_dialog_ok) { _, _ ->
                        //Do nothing
                    }
                    .show()
                findNavController().navigate(ChatFragmentDirections.actionChatToMessaging())
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        shouldInitRecyclerView = true
        val inputManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            requireActivity().currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
        _binding = null
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
                if (bottom < oldBottom) {
                    this.post(kotlinx.coroutines.Runnable { this@apply.smoothScrollToPosition(0) })
                }
            }

            layoutManager = LinearLayoutManager(requireContext())
            (layoutManager as LinearLayoutManager).reverseLayout = true
            (layoutManager as LinearLayoutManager).stackFromEnd = true
            val listAdapter = ChatMessageAdapter()
            viewModel.retrieveMessages().observe(viewLifecycleOwner) { chatMessageQueryList ->
                if (chatMessageQueryList.isNotEmpty()) {
                    listAdapter.submitList(
                        chatMessageQueryList,
                        kotlinx.coroutines.Runnable { this.smoothScrollToPosition(0) })
                    FirestoreUtil.firebaseAuthInstance.currentUser?.let {
                        if (it.uid != chatMessageQueryList[0].item.senderUid){
                            viewModel.markMessageAsRead()
                        }
                    }
                }
            }
            adapter = listAdapter
        }
        shouldInitRecyclerView = false
    }
}