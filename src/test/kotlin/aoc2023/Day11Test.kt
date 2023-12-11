package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day11Test {
    private lateinit var day11: Day11

    @BeforeEach
    fun setUp() {
        day11 = Day11()
    }

    @Test
    fun `part1 example`() {
        val input = """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
        """.trimIndent()

        assertEquals(374, day11.part1(input))
    }

    @Test
    fun `part2 example`() {
        assertEquals(0, day11.part2(""))
    }
}
