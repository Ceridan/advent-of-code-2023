package aoc2023

class Day06 {
    fun part1(input: List<String>): Int = parseRaces(input)
            .map { race -> (1..race.time).map { t -> (race.time - t) * t }.count { it > race.distance } }
            .fold(1) {acc, r -> acc * r }

    fun part2(input: List<String>): Long {
        return 0L
    }


    private fun parseRaces(input: List<String>): List<Race> {
        val parser: (String) -> List<Int> = { s -> s.split(' ').drop(1).filter { it.isNotEmpty() }.map { it.toInt() } }
        val times = parser(input[0])
        val distances = parser(input[1])
        return times.zip(distances).map { (t, d) -> Race(time = t, distance = d) }
    }

    data class Race(val time: Int, val distance: Int)
}

fun main() {
    val day06 = Day06()
    val input = readInputAsStringList("day06.txt")

    println("06, part 1: ${day06.part1(input)}")
    println("06, part 2: ${day06.part2(input)}")
}
