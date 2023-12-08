package aoc2023

class Day08 {
    fun part1(input: List<String>): Int {
        val (directions, network) = parseInput(input)
        var node = "AAA"
        var steps = 0
        var directionIndex = 0
        while (node != "ZZZ") {
            node = if (directions[directionIndex] == 'L') network[node]!!.first else network[node]!!.second
            directionIndex = (directionIndex + 1) % directions.size
            steps += 1
        }
        return steps
    }

    fun part2(input: List<String>): Int {
        val (directions, network) = parseInput(input)
        val current = network.keys.filter { it.endsWith('A') }.toTypedArray()
        val zNodes = network.keys.filter { it.endsWith('Z') }
        var steps = 0
        var zCount = 0
        var directionIndex = 0
        while (zCount < zNodes.size) {
            zCount = 0
            for (i in current.indices) {
                current[i] = if (directions[directionIndex] == 'L') network[current[i]]!!.first else network[current[i]]!!.second
                if (current[i] in zNodes) zCount += 1
            }
            directionIndex = (directionIndex + 1) % directions.size
            steps += 1
        }
        return steps
    }

    private fun parseInput(input: List<String>): Pair<CharArray, Map<String, Pair<String, String>>> {
        val directions = input[0].toCharArray()
        val network = mutableMapOf<String, Pair<String, String>>()
        val nodeRegex = "^([A-Z0-9]{3}) = \\(([A-Z0-9]{3}), ([A-Z0-9]{3})\\)".toRegex()
        for (i in 2..<input.size) {
            val (node, nodeL, nodeR) = nodeRegex.find(input[i])!!.destructured
            network[node] = nodeL to nodeR
        }
        return directions to network
    }
}

fun main() {
    val day08 = Day08()
    val input = readInputAsStringList("day08.txt")

    println("08, part 1: ${day08.part1(input)}")
    println("08, part 2: ${day08.part2(input)}")
}
