package com.ferpa.tarot.presentation.result_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ferpa.tarot.R
import com.ferpa.tarot.databinding.FragmentResultBinding
import com.ferpa.tarot.domain.model.Card
import com.ferpa.tarot.presentation.game_fragment.GameViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment : Fragment(R.layout.fragment_result) {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var resultAdapter: ResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setResultRecyclerView()
        subscribeResult()

    }

    private fun subscribeResult() {
        viewModel.selectedCards.observe(viewLifecycleOwner, Observer { selectedCards ->
            setResultAdapter(selectedCards)
        })
    }

    /**
     * Set initial configuration for Tarot Menu Recycler View
     */
    private fun setResultRecyclerView() {
        binding.rvResult.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
    }

    /**
     * Set Result Adapter
     */
    private fun setResultAdapter(result: List<Card>) {
        resultAdapter = ResultAdapter()
        resultAdapter.submitList(result)
        binding.rvResult.adapter = resultAdapter
    }
}