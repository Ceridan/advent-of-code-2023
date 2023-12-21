package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day21Test {
    private lateinit var day21: Day21

    @BeforeEach
    fun setUp() {
        day21 = Day21()
    }

    @Test
    fun `part1 example`() {
        val input = """
            ...........
            .....###.#.
            .###.##..#.
            ..#.#...#..
            ....#.#....
            .##..S####.
            .##..#...#.
            .......##..
            .##.#.####.
            .##..##.##.
            ...........
        """.trimIndent()

        assertEquals(16, day21.part1(input, 6))
    }
}
