package aoc2023

import kotlin.IllegalStateException

class Day19 {
    fun part1(input: String): Int {
        val (workflows, parts) = parseInput(input)
        var totalSum = 0
        for (part in parts) {
            var result = "in"
            while (result !in setOf("A", "R")) {
                result = workflows[result]!!.checkPart(part)
            }
            if (result == "A") {
                totalSum += part.sum()
            }
        }
        return totalSum
    }

    fun part2(input: String): Long {
        return 0L
    }

    private fun parseInput(input: String): Pair<Map<String, Workflow>, List<XmasPart>> {
        val workflows = mutableMapOf<String, Workflow>()
        val parts = mutableListOf<XmasPart>()
        val lines = input.split('\n').filter { it.isNotEmpty() }
        val partRegex = "^\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)}$".toRegex()
        val workflowRegex = "^([a-z]+)\\{(.+)}$".toRegex()
        for (line in lines) {
            val partMatch = partRegex.find(line)
            if (partMatch != null) {
                val (x, m, a, s) = partMatch.destructured
                parts.add(XmasPart(x = x.toInt(), m = m.toInt(), a = a.toInt(), s = s.toInt()))
                continue
            }
            val (name, conditions) = workflowRegex.find(line)!!.destructured
            workflows[name] = Workflow(name, conditions.split(','))
        }
        return workflows to parts
    }

    data class Workflow(val name: String, private val rawConditions: List<String>) {
        private val conditions = mutableListOf<(XmasPart) -> String>()

        init {
            conditions.addAll(rawConditions.map { parseCondition(it) })
        }

        fun checkPart(part: XmasPart): String {
            for (cond in conditions) {
                val result = cond(part)
                if (result != "C") return result
            }

            throw IllegalStateException("No conditions are applicable. Workflow: $name, XmasPart: $part")
        }

        private fun parseCondition(rawCondition: String): (XmasPart) -> String {
            val condParts = rawCondition.split(':')
            if (condParts.size == 1) {
                return { _: XmasPart -> condParts[0] }
            }

            val op = condParts[0].take(2)
            val value = condParts[0].drop(2).toInt()
            val result = condParts[1]

            return when (op) {
                "x<" -> { xmas: XmasPart -> if (xmas.x < value) result else "C" }
                "x>" -> { xmas: XmasPart -> if (xmas.x > value) result else "C" }
                "m<" -> { xmas: XmasPart -> if (xmas.m < value) result else "C" }
                "m>" -> { xmas: XmasPart -> if (xmas.m > value) result else "C" }
                "a<" -> { xmas: XmasPart -> if (xmas.a < value) result else "C" }
                "a>" -> { xmas: XmasPart -> if (xmas.a > value) result else "C" }
                "s<" -> { xmas: XmasPart -> if (xmas.s < value) result else "C" }
                "s>" -> { xmas: XmasPart -> if (xmas.s > value) result else "C" }
                else -> throw IllegalArgumentException("Unknown operation: $op")
            }
        }
    }

    data class XmasPart(val x: Int, val m: Int, val a: Int, val s: Int) {
        fun sum(): Int = x + m + a + s
    }
}

fun main() {
    val day19 = Day19()
    val input = readInputAsString("day19.txt")

    println("19, part 1: ${day19.part1(input)}")
    println("19, part 2: ${day19.part2(input)}")
}
