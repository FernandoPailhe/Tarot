package com.ferpa.tarot.domain.model

import com.ferpa.tarot.R

data class Deck(
    val cards: List<Card> = listOf(
        Card(0, "El Loco", R.drawable.c0),
        Card(1, "El Mago", R.drawable.c1),
        Card(2, "La Sacerdotisa", R.drawable.c2),
        Card(3, "La Emperatriz", R.drawable.c3),
        Card(4, "El Emperador", R.drawable.c4),
        Card(5, "El Sumo Sacerdote", R.drawable.c5),
        Card(6, "Los Enamorados", R.drawable.c6),
        Card(7, "El Carro", R.drawable.c7),
        Card(8, "La Justicia", R.drawable.c8),
        Card(9, "El Hermitaño", R.drawable.c9),
        Card(10, "Rueda de la Fortuna", R.drawable.c10),
        Card(11, "La Fuerza", R.drawable.c11),
        Card(12, "El Colgado", R.drawable.c12),
        Card(13, "La Muerte", R.drawable.c13),
        Card(14, "La Templanza", R.drawable.c14),
        Card(15, "El Diablo", R.drawable.c15),
        Card(16, "La Torre", R.drawable.c16),
        Card(17, "La Estrella", R.drawable.c17),
        Card(18, "La luna", R.drawable.c18),
        Card(19, "El Sol", R.drawable.c19),
        Card(20, "El Juicio", R.drawable.c20),
        Card(21, "El Mundo", R.drawable.c21),
    )
)
