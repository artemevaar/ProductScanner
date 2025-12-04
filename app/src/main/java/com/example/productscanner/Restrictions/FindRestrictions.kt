package com.example.productscanner.Restrictions

import java.util.Locale

fun findRestrictions(text: String?, restrictions: List<String>): List<String> {
    if (text.isNullOrBlank()) return emptyList()
    val normalizedText = text.lowercase(Locale.ROOT)

    return restrictions.mapNotNull { category ->
        Restrictions.compiledPatterns[category]?.takeIf { it.containsMatchIn(normalizedText) }
            ?.let { category }
    }
}
