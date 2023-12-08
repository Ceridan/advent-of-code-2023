package aoc2023

class Day08 {
    fun part1(input: List<String>): Int {
        val navigation = parseInput(input)
        var node = "AAA"
        while (node != "ZZZ") {
            node = navigation.nextNode(node)
        }
        return navigation.steps
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    private fun parseInput(input: List<String>): Navigation {
        val directions = input[0].toCharArray()
        val network = mutableMapOf<String, Pair<String, String>>()
        val nodeRegex = "^([A-Z]{3}) = \\(([A-Z]{3}), ([A-Z]{3})\\)".toRegex()
        for (i in 2..<input.size) {
            val (node, nodeL, nodeR) = nodeRegex.find(input[i])!!.destructured
            network[node] = nodeL to nodeR
        }
        return Navigation(directions, network)
    }

    class Navigation(private val directions: CharArray, private val network: Map<String, Pair<String, String>>) {
        var steps = 0
        private var directionIndex = 0

        fun nextNode(node: String): String {
            val nextNode = if (directions[directionIndex] == 'L') network[node]!!.first else network[node]!!.second

            directionIndex = (directionIndex + 1) % directions.size
            steps += 1
            return nextNode
        }
    }
}

fun main() {
    val day08 = Day08()
    val input = readInputAsStringList("day08.txt")

    println("08, part 1: ${day08.part1(input)}")
    println("08, part 2: ${day08.part2(input)}")
}
