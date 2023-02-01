package com.cabify.common

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

fun Double.toEuro(): String = NumberFormat.getCurrencyInstance(Locale.ITALY).apply {
    currency = Currency.getInstance("EUR")
}.format(this)
