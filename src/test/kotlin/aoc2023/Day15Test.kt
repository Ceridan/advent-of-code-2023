package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day15Test {
    private lateinit var day15: Day15

    @BeforeEach
    fun setUp() {
        day15 = Day15()
    }

    @Test
    fun `part1 example HASH`() {
        assertEquals(52, day15.part1("HASH"))
    }

    @Test
    fun `part1 example`() {
        assertEquals(1320, day15.part1("rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"))
    }

    @Test
    fun `part2 example`() {
        assertEquals(145, day15.part2("rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"))
    }
}
