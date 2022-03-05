package com.mradzinski.probablelamp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mradzinski.probablelamp.common.clearOnDestroyView
import com.mradzinski.probablelamp.common.extension.*
import com.mradzinski.probablelamp.common.utils.RecyclerViewEndlessScrollListener
import com.mradzinski.probablelamp.databinding.HomeFragmentBinding
import kotlinx.coroutines.flow.collect

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels { HomeViewModelFactory() }
    private var binding: HomeFragmentBinding by clearOnDestroyView()
    private lateinit var adapter: CharactersAdapter

    /* ********************************************
     *              End of variables              *
     ******************************************** */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        bindState()
    }

    private fun bindViews() = with(binding) {
        adapter = CharactersAdapter { character, _ ->
            Toast.makeText(requireContext(), character.name, Toast.LENGTH_SHORT).show()
        }

        val layoutManager = LinearLayoutManager(requireContext())

        val listener = RecyclerViewEndlessScrollListener(
            layoutManager = layoutManager,
            initialPage = 1,
            onLoadMore = { _, nextPage, _, _ ->
                viewModel.getCharacters(nextPage)
            })

        recyclerView.apply {
            setLayoutManager(layoutManager)
            addOnScrollListener(listener)
            adapter = this@HomeFragment.adapter
        }

        errorView.retry.setOnClickListener { viewModel.retry() }

    }

    private fun bindState() = launchAndRepeatWithViewLifecycle(Lifecycle.State.STARTED) {
        viewModel.state.collect { state ->
            if (state is HomeViewState.Success) {
                adapter.submitList(state.characters)
            }

            updateVisibilityState(state)
        }
    }

    private fun updateVisibilityState(state: HomeViewState) {
        val isLoading = (state is HomeViewState.Loading)
        val isError = state is HomeViewState.Failure

        with(binding) {
            recyclerView.showIfElseHide(!isError)
            toolbarView.progressBar.showIfElseHide(isLoading)
            errorView.root.showIfElseHide(isError)
        }
    }
}