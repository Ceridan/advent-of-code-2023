package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day23Test {
    private lateinit var day23: Day23

    @BeforeEach
    fun setUp() {
        day23 = Day23()
    }

    @Test
    fun `part1 example - toy`() {
        val input = """
            #.##
            #..#
            #..#
            #.##
        """.trimIndent()

        assertEquals(5, day23.part1(input))
    }

    @Test
    fun `part1 example - full`() {
        assertEquals(94, day23.part1(INPUT))
    }

    @Test
    fun `part2 example`() {
        assertEquals(154, day23.part2(INPUT))
    }

    private companion object {
        val INPUT = """
            #.#####################
            #.......#########...###
            #######.#########.#.###
            ###.....#.>.>.###.#.###
            ###v#####.#v#.###.#.###
            ###.>...#.#.#.....#...#
            ###v###.#.#.#########.#
            ###...#.#.#.......#...#
            #####.#.#.#######.#.###
            #.....#.#.#.......#...#
            #.#####.#.#.#########v#
            #.#...#...#...###...>.#
            #.#.#v#######v###.###v#
            #...#.>.#...>.>.#.###.#
            #####v#.#.###v#.#.###.#
            #.....#...#...#.#.#...#
            #.#########.###.#.#.###
            #...###...#...#...#.###
            ###.###.#.###v#####v###
            #...#...#.#.>.>.#.>.###
            #.###.###.#.###.#.#v###
            #.....###...###...#...#
            #####################.#
        """.trimIndent()
    }
}
