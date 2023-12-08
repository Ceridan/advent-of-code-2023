package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day08Test {
    private lateinit var day08: Day08

    @BeforeEach
    fun setUp() {
        day08 = Day08()
    }

    @Test
    fun `part1 example 1`() {
        val input = listOf(
            "RL",
            "",
            "AAA = (BBB, CCC)",
            "BBB = (DDD, EEE)",
            "CCC = (ZZZ, GGG)",
            "DDD = (DDD, DDD)",
            "EEE = (EEE, EEE)",
            "GGG = (GGG, GGG)",
            "ZZZ = (ZZZ, ZZZ)",
        )

        assertEquals(2, day08.part1(input))
    }

    @Test
    fun `part1 example 2`() {
        val input = listOf(
            "LLR",
            "",
            "AAA = (BBB, BBB)",
            "BBB = (AAA, ZZZ)",
            "ZZZ = (ZZZ, ZZZ)",
        )

        assertEquals(6, day08.part1(input))
    }

    @Test
    fun `part2 example`() {
        val input = listOf(
            "LR",
            "",
            "11A = (11B, XXX)",
            "11B = (XXX, 11Z)",
            "11Z = (11B, XXX)",
            "22A = (22B, XXX)",
            "22B = (22C, 22C)",
            "22C = (22Z, 22Z)",
            "22Z = (22B, 22B)",
            "XXX = (XXX, XXX)",
        )
        assertEquals(6L, day08.part2(input))
    }
}
