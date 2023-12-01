package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day01Test {
    private lateinit var day01: Day01

    @BeforeEach
    fun setUp() {
        day01 = Day01()
    }

    @Test
    fun `part1 example`() {
        val input = listOf(
            "1abc2",
            "pqr3stu8vwx",
            "a1b2c3d4e5f",
            "treb7uchet",
        )

        assertEquals(142, day01.part1(input))
    }

    @Test
    fun `part2 example`() {
        val input = listOf(
            "two1nine",
            "eightwothree",
            "abcone2threexyz",
            "xtwone3four",
            "4nineeightseven2",
            "zoneight234",
            "7pqrstsixteen",
        )

        assertEquals(281, day01.part2(input))
    }
}
