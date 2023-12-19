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
            val (name, rawConditions) = workflowRegex.find(line)!!.destructured
            val conditions = rawConditions.split(',').map {
                val condParts = it.split(':')
                if (condParts.size == 1) {
                    TerminalCondition(condParts[0])
                } else {
                    val param = condParts[0].take(1)[0]
                    val op = condParts[0].drop(1).take(1)[0]
                    val value = condParts[0].drop(2).toInt()
                    val result = condParts[1]
                    WorkflowCondition(param, op, value, result)
                }
            }
            workflows[name] = Workflow(name, conditions)
        }
        return workflows to parts
    }

    data class Workflow(val name: String, private val conditions: List<Condition>) {
        private val fnConditions = mutableListOf<(XmasPart) -> String>()
        private val compactedConditions = mutableListOf<Condition>()

        init {
            compactedConditions.addAll(compactConditions(conditions))
            fnConditions.addAll(compactedConditions.map { parseRawCondition(it) })
        }

        fun checkPart(part: XmasPart): String {
            for (fnCond in fnConditions) {
                val result = fnCond(part)
                if (result != "C") return result
            }

            throw IllegalStateException("No conditions are applicable. Workflow: $name, XmasPart: $part")
        }

        private fun compactConditions(conditions: List<Condition>): List<Condition> {
            val terminalCondition = conditions.first { it is TerminalCondition }
            val compactedConditions = conditions.asReversed().dropWhile { it.result == terminalCondition.result }.toMutableList()
            compactedConditions.reverse()
            compactedConditions.add(terminalCondition)
            return compactedConditions
        }

        private fun parseRawCondition(condition: Condition): (XmasPart) -> String {
            if (condition is TerminalCondition) {
                return { _: XmasPart -> condition.result }
            }

            val workflowCondition = condition as WorkflowCondition

            return when (val paramOpPair = "${workflowCondition.param}${workflowCondition.op}") {
                "x<" -> { xmas: XmasPart -> if (xmas.x < workflowCondition.value) workflowCondition.result else "C" }
                "x>" -> { xmas: XmasPart -> if (xmas.x > workflowCondition.value) workflowCondition.result else "C" }
                "m<" -> { xmas: XmasPart -> if (xmas.m < workflowCondition.value) workflowCondition.result else "C" }
                "m>" -> { xmas: XmasPart -> if (xmas.m > workflowCondition.value) workflowCondition.result else "C" }
                "a<" -> { xmas: XmasPart -> if (xmas.a < workflowCondition.value) workflowCondition.result else "C" }
                "a>" -> { xmas: XmasPart -> if (xmas.a > workflowCondition.value) workflowCondition.result else "C" }
                "s<" -> { xmas: XmasPart -> if (xmas.s < workflowCondition.value) workflowCondition.result else "C" }
                "s>" -> { xmas: XmasPart -> if (xmas.s > workflowCondition.value) workflowCondition.result else "C" }
                else -> throw IllegalArgumentException("Unknown operation: $paramOpPair")
            }
        }
    }

    interface Condition {
        val result: String
    }

    data class TerminalCondition(override val result: String) : Condition
    data class WorkflowCondition(
        val param: Char,
        val op: Char,
        val value: Int,
        override val result: String,
    ) : Condition

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
