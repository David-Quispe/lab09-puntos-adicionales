package com.example.lab09_puntosadicionales.ui

import com.example.lab09_puntosadicionales.data.Amphibian
import org.junit.Assert.assertEquals
import org.junit.Test

class AmphibianFiltersTest {
    private val species = listOf(
        Amphibian("Pacific Chorus Frog", "Frog", "Pacific coast", "https://example.org/1.png"),
        Amphibian("Roraima Bush Toad", "Toad", "Mount Roraima", "https://example.org/2.png"),
        Amphibian("Blue Jeans Frog", "Frog", "Tropical forest", "https://example.org/3.png"),
    )

    @Test
    fun combinesTypeAndCaseInsensitiveSearch() {
        val result = filterAmphibians(species, "  forest ", "Frog")

        assertEquals(listOf("Blue Jeans Frog"), result.map { it.name })
    }

    @Test
    fun returnsAllWhenNoFilterIsSelected() {
        assertEquals(species, filterAmphibians(species, "", ""))
    }
}
