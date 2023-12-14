package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day14Test {
    private lateinit var day14: Day14

    @BeforeEach
    fun setUp() {
        day14 = Day14()
    }

    @Test
    fun `part1 example`() {
        assertEquals(136, day14.part1(INPUT))
    }

    @Test
    fun `part2 example`() {
        assertEquals(64, day14.part2(INPUT, cycles = 1000000000))
    }

    private companion object {
        val INPUT = """
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....
        """.trimIndent()
    }
}
