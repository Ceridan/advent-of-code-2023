package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day13Test {
    private lateinit var day13: Day13

    @BeforeEach
    fun setUp() {
        day13 = Day13()
    }

    @Test
    fun `part1 example`() {
        assertEquals(405L, day13.part1(INPUT))
    }

    @Test
    fun `part2 example`() {
        assertEquals(400L, day13.part2(INPUT))
    }

    private companion object {
        val INPUT = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.

            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
        """.trimIndent()
    }
}
