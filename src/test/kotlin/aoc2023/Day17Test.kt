package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day17Test {
    private lateinit var day17: Day17

    @BeforeEach
    fun setUp() {
        day17 = Day17()
    }

    @Test
    fun `part1 example`() {
        assertEquals(102, day17.part1(INPUT))
    }

    @Test
    fun `part2 example`() {
        assertEquals(0, day17.part2(INPUT))
    }

    private companion object {
        val INPUT = """
            2413432311323
            3215453535623
            3255245654254
            3446585845452
            4546657867536
            1438598798454
            4457876987766
            3637877979653
            4654967986887
            4564679986453
            1224686865563
            2546548887735
            4322674655533
        """.trimIndent()
    }
}
