package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day10Test {
    private lateinit var day10: Day10

    @BeforeEach
    fun setUp() {
        day10 = Day10()
    }

    @Test
    fun `part1 example 1`() {
        val input = """
            .....
            .S-7.
            .|.|.
            .L-J.
            .....
        """.trimIndent()

        assertEquals(4, day10.part1(input))
    }

    @Test
    fun `part1 example 2`() {
        val input = """
            ..F7.
            .FJ|.
            SJ.L7
            |F--J
            LJ...
        """.trimIndent()

        assertEquals(8, day10.part1(input))
    }

    @Test
    fun `part2 example`() {
        assertEquals(0, day10.part2(""))
    }
}
