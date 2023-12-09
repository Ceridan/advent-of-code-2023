package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day09Test {
    private lateinit var day09: Day09

    @BeforeEach
    fun setUp() {
        day09 = Day09()
    }

    @Test
    fun `part1 example`() {
        val input = listOf(
            "0 3 6 9 12 15",
            "1 3 6 10 15 21",
            "10 13 16 21 30 45",
        )

        assertEquals(114, day09.part1(input))
    }

    @Test
    fun `part2 example`() {
        val input = listOf(
            "0 3 6 9 12 15",
            "1 3 6 10 15 21",
            "10 13 16 21 30 45",
        )

        assertEquals(2, day09.part2(input))
    }
}
