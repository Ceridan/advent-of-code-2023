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
        assertEquals(374L, day11.part1(INPUT))
    }

    @Test
    fun `part2 example 10 times expansion`() {
        assertEquals(1030L, day11.part2(INPUT, 10))
    }

    @Test
    fun `part2 example 100 times expansion`() {
        assertEquals(8410L, day11.part2(INPUT, 100))
    }

    private companion object {
        val INPUT = """
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
    }
}
