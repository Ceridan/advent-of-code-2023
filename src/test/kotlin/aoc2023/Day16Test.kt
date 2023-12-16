package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day16Test {
    private lateinit var day16: Day16

    @BeforeEach
    fun setUp() {
        day16 = Day16()
    }

    @Test
    fun `part1 example`() {
        assertEquals(46, day16.part1(INPUT))
    }

    @Test
    fun `part2 example`() {
        assertEquals(0, day16.part2(INPUT))
    }

    private companion object {
        val INPUT = """
            .|...\....
            |.-.\.....
            .....|-...
            ........|.
            ..........
            .........\
            ..../.\\..
            .-.-/..|..
            .|....-|.\
            ..//.|....
        """.trimIndent()
    }
}
