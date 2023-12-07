package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day07Test {
    private lateinit var day07: Day07

    @BeforeEach
    fun setUp() {
        day07 = Day07()
    }

    @Test
    fun `part1 example`() {
        val input = listOf(
            "32T3K 765",
            "T55J5 684",
            "KK677 28",
            "KTJJT 220",
            "QQQJA 483",
        )

        assertEquals(5905, day07.part1(input))
    }

    @Test
    fun `part2 example`() {
        val input = listOf(
            "32T3K 765",
            "T55J5 684",
            "KK677 28",
            "KTJJT 220",
            "QQQJA 483",
        )

        assertEquals(0, day07.part2(listOf()))
    }
}
