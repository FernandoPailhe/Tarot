package com.ferpa.tarot.presentation.game_fragment

import android.content.ClipData
import android.content.ClipDescription
import android.os.Bundle
import android.view.*
import android.view.View.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ferpa.tarot.R
import com.ferpa.tarot.common.Extensions.getScreenHeight
import com.ferpa.tarot.common.Extensions.getScreenWidth
import com.ferpa.tarot.databinding.FragmentGameBinding
import com.ferpa.tarot.domain.model.Card
import com.ferpa.tarot.domain.model.bind
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameFragment : Fragment(R.layout.fragment_game) {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDeckInterface()

        manageLowDpScreens()

        subscribeSelectedCards()

    }

    /**
     * Check if screen Height is lower than 1920 dp and change layout params if it is necessary.
     */
    private fun manageLowDpScreens() {
        val screenHeight = binding.root.context.getScreenHeight()
        if (screenHeight < 1920) {
            binding.tarotTable.layoutParams = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    /**
     * Creates a drag listener and subscribes to the selected cards. When a change is detected in
     * the selected cards, the function calls setDragAndDropInterface and bindSelectedCards.
     */
    private fun subscribeSelectedCards() {
        val dragListener = createDragListener()
        viewModel.selectedCards.observe(viewLifecycleOwner, Observer { selectedCards ->
            setDragAndDropInterface(selectedCards.size, dragListener)
            if (selectedCards.isNotEmpty()) bindSelectedCards(selectedCards)
        })
    }

    /**
     * Returns an OnDragListener.
     * This listener contains a when statement to handle different drag events.
     */
    private fun createDragListener() = OnDragListener { view, event ->
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> true
            DragEvent.ACTION_DRAG_EXITED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                val item = event.clipData.getItemAt(0)
                val dragData = item.text.toString().toInt()
                viewModel.selectCard(dragData)
                view.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                view.invalidate()
                true
            }
            else -> false
        }
    }

    /**
     * This function sets up the drag-and-drop interface for the cards based on the given card space
     * @param cardSpace: an integer representing the current card space (0-5)
     * @param dragListener: a listener that listens for drag events on the card space views
     */
    private fun setDragAndDropInterface(cardSpace: Int, dragListener: OnDragListener) {
        when (cardSpace) {
            0 -> setOnCardSpaceDragListener(
                binding.firstCard.layoutCard,
                binding.firstCard.textDragHere,
                dragListener
            )
            1 -> {
                binding.apply {
                    firstCard.apply {
                        layoutCard.setOnDragListener(null)
                    }
                    secondCard.apply {
                        setOnCardSpaceDragListener(layoutCard, textDragHere, dragListener)
                    }
                }
            }
            2 -> {
                binding.apply {
                    secondCard.apply {
                        layoutCard.setOnDragListener(null)
                    }
                    thirdCard.apply {
                        setOnCardSpaceDragListener(layoutCard, textDragHere, dragListener)
                    }
                }
            }
            3 -> {
                binding.apply {
                    thirdCard.apply {
                        layoutCard.setOnDragListener(null)
                    }
                    fourthCard.apply {
                        setOnCardSpaceDragListener(layoutCard, textDragHere, dragListener)
                    }
                }
            }
            4 -> {
                binding.apply {
                    fourthCard.apply {
                        layoutCard.setOnDragListener(null)
                    }
                    fifthCard.apply {
                        setOnCardSpaceDragListener(layoutCard, textDragHere, dragListener)
                    }
                }
            }
            5 -> {
                binding.apply {
                    fifthCard.apply {
                        layoutCard.setOnDragListener(null)
                    }
                    enableResultButton()
                }
            }
        }
    }

    /**
     * Binds the selected cards to the appropriate views in the layout
     */
    private fun bindSelectedCards(selectedCards: List<Card>) {
        // Use the forEachIndexed method to iterate over the selected card
        selectedCards.forEachIndexed { index, card ->
            // Use a switch statement to handle different card indices
            when (index) {
                0 -> card.bind(binding.firstCard.imageSelectedCard, binding.firstCard.textCardTitle)
                1 -> card.bind(
                    binding.secondCard.imageSelectedCard,
                    binding.secondCard.textCardTitle
                )
                2 -> card.bind(binding.thirdCard.imageSelectedCard, binding.thirdCard.textCardTitle)
                3 -> card.bind(
                    binding.fourthCard.imageSelectedCard,
                    binding.fourthCard.textCardTitle
                )
                4 -> card.bind(binding.fifthCard.imageSelectedCard, binding.fifthCard.textCardTitle)
            }
        }
    }


    /**
     * Sets the OnDragListener for the layout view and sets its background to the
     * R.drawable.gradient_border resource.
     * Additionally, the visibility of the text view is set to VISIBLE.
     */
    private fun setOnCardSpaceDragListener(layout: View, text: View, dragListener: OnDragListener) {
        layout.setOnDragListener(dragListener)
        text.visibility = VISIBLE
        layout.setBackgroundResource(R.drawable.gradient_border)
    }


    /**
     * Disables and hides the linearLayoutDeck and textPickCard views, and makes the buttonResult
     * view visible. It also sets a click listener on the buttonResult view, which navigates to the
     * result fragment when clicked and call viewModel.setAlreadyBeenSeen() function.
     */
    private fun enableResultButton() {
        binding.apply {
            linearLayoutDeck.visibility = INVISIBLE
            linearLayoutDeck.isEnabled = false
            textPickCard.visibility = INVISIBLE
            buttonResult.visibility = VISIBLE
            buttonResult.setOnClickListener {
                val action = GameFragmentDirections.actionGameFragmentToResultFragment()
                this@GameFragment.findNavController().navigate(action)
                viewModel.setAlreadyBeenSeen()
            }
        }
    }

    /**
     *  Resets the game if viewModel.alreadyBeenSeen is set to true
     */
    private fun setDeckInterface() {
        if (viewModel.alreadyBeenSenn.value == true) viewModel.resetGame()
        if (viewModel.deck.value != null) {
            setDeckView(viewModel.deck.value!!)
        }
    }

    /**
     * Takes a list of cards as input and adds each card as a view to the linearLayoutDeck.
     * It sets the width of each card based on whether it is the last card or not, and sets a
     * long click listener on each view to enable drag and drop functionality.
     */
    private fun setDeckView(cards: List<Card>) {
        val linearLayout = binding.linearLayoutDeck
        val layoutInflater = LayoutInflater.from(binding.root.context)
        val cardWidth = (binding.root.context.getScreenWidth() / 12.5).toInt()
        cards.forEachIndexed { index, card ->
            val isLastCard = index == cards.size - 1
            val view = layoutInflater.inflate(R.layout.card, linearLayout, false)
            val params = view.layoutParams
            if (!isLastCard) {
                params.width = cardWidth / 2
                view.setPadding(0, 0, -cardWidth / 2, 0)
            } else {
                params.width = cardWidth
            }
            subscribeDeckCard(view, index)
            view.setOnLongClickListener {
                val dragShadowView = binding.imageDragShadow
                val clipText = index.toString()
                val item = ClipData.Item(clipText)
                val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
                val data = ClipData(clipText, mimeTypes, item)
                val dragShadowBuilder = DragShadowBuilder(dragShadowView)
                it.startDragAndDrop(data, dragShadowBuilder, it, 0)

                true
            }
            linearLayout.addView(view)
        }
    }

    /**
     * Subscribes to the deck property of the viewModel and updates the view accordingly.
     * If the size of the deck is greater than or equal to index, the function sets the alpha and
     * enables/disables the view based on whether the card at the given index is selected or not.
     */
    private fun subscribeDeckCard(view: View, index: Int) {
        viewModel.deck.observe(viewLifecycleOwner, Observer {
            if (it.size >= index) {
                val card = it[index]
                if (card.isSelected) {
                    view.alpha = 0.5f
                    view.isEnabled = false
                } else {
                    view.alpha = 1f
                    view.isEnabled = true
                }
            }
        })
    }

}