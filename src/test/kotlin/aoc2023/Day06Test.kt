package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day06Test {
    private lateinit var day06: Day06

    @BeforeEach
    fun setUp() {
        day06 = Day06()
    }

    @Test
    fun `part1 example`() {
        val input = listOf(
            "Time:      7  15   30",
            "Distance:  9  40  200",
        )

        assertEquals(288, day06.part1(input))
    }

    @Test
    fun `part2 example`() {
        val input = listOf(
            "Time:      7  15   30",
            "Distance:  9  40  200",
        )

        assertEquals(71503L, day06.part2(input))
    }
}
