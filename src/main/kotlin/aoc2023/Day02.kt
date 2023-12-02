package aoc2023

class Day02 {
    fun part1(input: List<String>, redCap: Int, greenCap: Int, blueCap: Int): Int = input
        .map { line -> CubeGame.fromString(line) }
        .filter { game -> game.red.all { it <= redCap } && game.green.all { it <= greenCap } && game.blue.all { it <= blueCap }}
        .sumOf { game -> game.id }

    fun part2(input: List<String>): Int = input
        .map { line -> CubeGame.fromString(line) }.sumOf { game -> game.red.max() * game.green.max() * game.blue.max() }

    class CubeGame(val id: Int, val red: IntArray, val green: IntArray, val blue: IntArray, ) {
        companion object {
            fun fromString(game: String): CubeGame {
                val gameRegex = "^Game (\\d+): (.*)$".toRegex()
                val redRegex = "(\\d+) red".toRegex()
                val greenRegex = "(\\d+) green".toRegex()
                val blueRegex = "(\\d+) blue".toRegex()

                val (gameId, gameResultString) = gameRegex.find(game)!!.destructured
                val gameResults = gameResultString.split(';')

                val red = IntArray(gameResults.size)
                val green = IntArray(gameResults.size)
                val blue = IntArray(gameResults.size)
                for ((i, result) in gameResults.withIndex()) {
                    red[i] = redRegex.find(result)?.groupValues?.get(1)?.toInt() ?: 0
                    green[i] = greenRegex.find(result)?.groupValues?.get(1)?.toInt() ?: 0
                    blue[i] = blueRegex.find(result)?.groupValues?.get(1)?.toInt() ?: 0
                }

                return CubeGame(gameId.toInt(), red = red, green = green, blue = blue)
            }
        }
    }
}

fun main() {
    val day02 = Day02()
    val input = readInputAsStringList("day02.txt")

    println("02, part 1: ${day02.part1(input, redCap = 12, greenCap = 13, blueCap = 14)}")
    println("02, part 2: ${day02.part2(input)}")
}
