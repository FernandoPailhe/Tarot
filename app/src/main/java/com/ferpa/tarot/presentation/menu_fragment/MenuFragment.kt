package com.ferpa.tarot.presentation.menu_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ferpa.tarot.R
import com.ferpa.tarot.databinding.FragmentMenuBinding
import com.ferpa.tarot.domain.model.TarotMenuItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : Fragment(R.layout.fragment_menu) {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var tarotMenuAdapter: TarotMenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTarotMenuInterface()

    }

    /**
     * Set Tarot Menu Interface with default menu
     */
    private fun setTarotMenuInterface() {
        setUpTarotMenuRecyclerView()
        setTarotMenuAdapter(
            listOf(
                TarotMenuItem(
                    title = getString(R.string.menu_title_1),
                    description = getString(R.string.menu_description_1),
                    numberOfCards = 1
                ),
                TarotMenuItem(
                    title = getString(R.string.menu_title_2),
                    description = getString(R.string.menu_description_2),
                    numberOfCards = 3
                ),
                TarotMenuItem(
                    title = getString(R.string.menu_title_3),
                    description = getString(R.string.menu_description_3),
                    numberOfCards = 5
                )
            )
        )
    }

    /**
     * Set initial configuration for Tarot Menu Recycler View
     */
    private fun setUpTarotMenuRecyclerView() {
        binding.rvMenuList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
    }

    /**
     * Set Tarot Menu Adapter
     */
    private fun setTarotMenuAdapter(menu: List<TarotMenuItem>) {
        tarotMenuAdapter = TarotMenuAdapter(
            onItemClicked = { tarotMenuItem ->
                if (tarotMenuItem.numberOfCards == 5 ){
                    val action = MenuFragmentDirections.actionMenuFragmentToGameFragment()
                    this.findNavController().navigate(action)
                }
            }
        )
        tarotMenuAdapter.submitList(menu)
        binding.rvMenuList.adapter = tarotMenuAdapter
    }
}