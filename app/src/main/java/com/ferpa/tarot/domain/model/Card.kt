package com.ferpa.tarot.domain.model

import android.widget.ImageView
import android.widget.TextView

data class Card(
    val number: Int,
    val name: String,
    val resourceId: Int,
    var isSelected: Boolean = false,
    val description: String = "Esta carta significa cambio inesperado o drástico, ingenuidad y euforia, nuevas aventuras, nuevas oportunidades, comienzos y asunción de riesgos. Además, la carta podría significar tanto optimismo, inventiva, liberación, rebeldía… como insensatez y toma de riesgos innecesarios. En general, la aparición de esta carta es muy positiva."
)

/**
 * Binds the properties of a [Card] to views in the layout.
 *
 * @param imageView The [ImageView] to display the image of the card.
 * @param titleTextView The [TextView] to display the title of the card.
 * @param descriptionTextView The [TextView] to display the description of the card. Defaults to `null`.
 */
fun Card?.bind(imageView: ImageView, titleTextView: TextView, descriptionTextView: TextView? = null) {
    if (this == null) return
    imageView.setImageResource(this.resourceId)
    titleTextView.text = name
    if (descriptionTextView != null) descriptionTextView.text = description
}