package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day12Test {
    private lateinit var day12: Day12

    @BeforeEach
    fun setUp() {
        day12 = Day12()
    }

    @Test
    fun `part1 example`() {
        val input = """
            #.#.### 1,1,3
            .#...#....###. 1,1,3
            .#.###.#.###### 1,3,1,6
            ####.#...#... 4,1,1
            #....######..#####. 1,6,5
            .###.##....# 3,2,1
        """.trimIndent()

        assertEquals(6L, day12.part1(input))
    }

    @Test
    fun `part1 example with wildcards`() {
        val input = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
        """.trimIndent()

        assertEquals(21L, day12.part1(input))
    }

    @Test
    fun `part2 example`() {
        val input = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
        """.trimIndent()

        assertEquals(525152L, day12.part2(input))
    }
}
