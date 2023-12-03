package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day03Test {
    private lateinit var day03: Day03

    @BeforeEach
    fun setUp() {
        day03 = Day03()
    }

    @Test
    fun `part1 example`() {
        val input = listOf(
            "467..114..",
            "...*......",
            "..35..633.",
            "......#...",
            "617*......",
            ".....+.58.",
            "..592.....",
            "......755.",
            "...$.*....",
            ".664.598..",
        )

        assertEquals(4361, day03.part1(input))
    }

    @Test
    fun `part2 example`() {
        val input = listOf(
            "467..114..",
            "...*......",
            "..35..633.",
            "......#...",
            "617*......",
            ".....+.58.",
            "..592.....",
            "......755.",
            "...$.*....",
            ".664.598..",
        )

        assertEquals(467835L, day03.part2(input))
    }
}
