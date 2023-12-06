package aoc2023

class Day06 {
    fun part1(input: List<String>): Long = parseRaces(input)
        .map { race -> (1L..race.time).map { t -> (race.time - t) * t }.count { it > race.distance } }
        .fold(1L) { acc, r -> acc * r }

    fun part2(input: List<String>): Long {
        val (time, distance) = parseRace(input)

        var first = 1L
        while ((time - first) * first <= distance) {
            first += 1
        }

        var last = time - 1L
        while ((time - last) * last <= distance) {
            last -= 1
        }

        return last - first + 1L
    }

    private fun parseRaces(input: List<String>): List<Race> {
        val parser: (String) -> List<Long> =
            { s -> s.split(' ').drop(1).filter { it.isNotEmpty() }.map { it.toLong() } }
        val times = parser(input[0])
        val distances = parser(input[1])
        return times.zip(distances).map { (t, d) -> Race(time = t, distance = d) }
    }

    private fun parseRace(input: List<String>): Race {
        val (time) = "^Time: (.+)$".toRegex().find(input[0])!!.destructured
        val (distance) = "^Distance: (.+)$".toRegex().find(input[1])!!.destructured
        return Race(
            time = time.replace(" ", "").toLong(),
            distance = distance.replace(" ", "").toLong(),
        )
    }

    data class Race(val time: Long, val distance: Long)
}

fun main() {
    val day06 = Day06()
    val input = readInputAsStringList("day06.txt")

    println("06, part 1: ${day06.part1(input)}")
    println("06, part 2: ${day06.part2(input)}")
}
