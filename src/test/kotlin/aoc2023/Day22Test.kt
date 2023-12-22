package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day22Test {
    private lateinit var day22: Day22

    @BeforeEach
    fun setUp() {
        day22 = Day22()
    }

    @Test
    fun `part1 example`() {
        assertEquals(5, day22.part1(INPUT))
    }

    @Test
    fun `part2 example`() {
        assertEquals(7, day22.part2(INPUT))
    }

    private companion object {
        val INPUT = listOf(
            "1,0,1~1,2,1",
            "0,0,2~2,0,2",
            "0,2,3~2,2,3",
            "0,0,4~0,2,4",
            "2,0,5~2,2,5",
            "0,1,6~2,1,6",
            "1,1,8~1,1,9",
        )
    }
}
