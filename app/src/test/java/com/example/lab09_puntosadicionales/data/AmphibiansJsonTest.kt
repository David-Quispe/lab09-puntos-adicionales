package com.example.lab09_puntosadicionales.data

import org.junit.Assert.assertEquals
import org.junit.Test

class AmphibiansJsonTest {
    @Test
    fun parseReadsAllFieldsFromApiResponse() {
        val response = """[
            {
                "name": "Roraima Bush Toad",
                "type": "Toad",
                "description": "A mountain species.",
                "img_src": "https://example.org/toad.png"
            },
            {
                "name": "Tiger Salamander",
                "type": "Salamander",
                "description": "Lives underground.",
                "img_src": "https://example.org/salamander.png"
            }
        ]"""

        val amphibians = AmphibiansJson.parse(response)

        assertEquals(2, amphibians.size)
        assertEquals("Roraima Bush Toad", amphibians[0].name)
        assertEquals("Toad", amphibians[0].type)
        assertEquals("A mountain species.", amphibians[0].description)
        assertEquals("https://example.org/toad.png", amphibians[0].imageUrl)
        assertEquals("Tiger Salamander", amphibians[1].name)
    }
}
