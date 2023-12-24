package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day24Test {
    private lateinit var day24: Day24

    @BeforeEach
    fun setUp() {
        day24 = Day24()
    }

    @Test
    fun `part1 example`() {
        val input = listOf(
            "19, 13, 30 @ -2,  1, -2",
            "18, 19, 22 @ -1, -1, -2",
            "20, 25, 34 @ -2, -2, -4",
            "12, 31, 28 @ -1, -2, -1",
            "20, 19, 15 @  1, -5, -3",
        )

        assertEquals(2, day24.part1(input, area = 7L..27L))
    }

    @Test
    fun `part2 example`() {
        val input = listOf(
            "19, 13, 30 @ -2,  1, -2",
            "18, 19, 22 @ -1, -1, -2",
            "20, 25, 34 @ -2, -2, -4",
            "12, 31, 28 @ -1, -2, -1",
            "20, 19, 15 @  1, -5, -3",
        )

        assertEquals(47L, day24.part2(input))
    }
}
