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
        assertEquals(16L, day21.part1(INPUT, 6L))
    }

    @Test
    fun `part2 example - 6`() {
        assertEquals(16L, day21.part2(INPUT, 6L))
    }

    @Test
    fun `part2 example - 10`() {
        assertEquals(50L, day21.part2(INPUT, 10L))
    }

    @Test
    fun `part2 example - 50`() {
        assertEquals(1594L, day21.part2(INPUT, 50L))
    }

    @Test
    fun `part2 example - 100`() {
        assertEquals(6536L, day21.part2(INPUT, 100L))
    }

    @Test
    fun `part2 example - 500`() {
        assertEquals(167004L, day21.part2(INPUT, 500L))
    }

    @Test
    fun `part2 example - 1000`() {
        assertEquals(668697L, day21.part2(INPUT, 1000L))
    }

    @Test
    fun `part2 example - 5000`() {
        assertEquals(16733044L, day21.part2(INPUT, 5000L))
    }

    private companion object {
        val INPUT = """
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
    }
}
