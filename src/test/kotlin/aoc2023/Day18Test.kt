package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day18Test {
    private lateinit var day18: Day18

    @BeforeEach
    fun setUp() {
        day18 = Day18()
    }

    @Test
    fun `part1 example`() {
        assertEquals(62, day18.part1(INPUT))
    }

    @Test
    fun `part2 example`() {
        assertEquals(952408144115L, day18.part2(INPUT))
    }

    private companion object {
        val INPUT = listOf(
            "R 6 (#70c710)",
            "D 5 (#0dc571)",
            "L 2 (#5713f0)",
            "D 2 (#d2c081)",
            "R 2 (#59c680)",
            "D 2 (#411b91)",
            "L 5 (#8ceee2)",
            "U 2 (#caa173)",
            "L 1 (#1b58a2)",
            "U 2 (#caa171)",
            "R 2 (#7807d2)",
            "U 3 (#a77fa3)",
            "L 2 (#015232)",
            "U 2 (#7a21e3)",
        )
    }
}
