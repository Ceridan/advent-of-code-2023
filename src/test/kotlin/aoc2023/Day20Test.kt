package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day20Test {
    private lateinit var day20: Day20

    @BeforeEach
    fun setUp() {
        day20 = Day20()
    }

    @Test
    fun `part1 example - 1 step cycle`() {
        val input = """
            broadcaster -> a, b, c
            %a -> b
            %b -> c
            %c -> inv
            &inv -> a
        """.trimIndent()

        assertEquals(32000000L, day20.part1(input))
    }

    @Test
    fun `part1 example - 4 step cycle`() {
        val input = """
            broadcaster -> a
            %a -> inv, con
            &inv -> b
            %b -> con
            &con -> output
        """.trimIndent()

        assertEquals(11687500L, day20.part1(input))
    }
}
