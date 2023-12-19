package aoc2023

import kotlin.IllegalStateException
import kotlin.math.min
import kotlin.math.max

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
        val (workflows, _) = parseInput(input)
        return dfs(workflows, "in")
    }

    private fun dfs(
        workflows: Map<String, Workflow>,
        currentWorkflow: String,
        accepted: MutableMap<Char, IntRange> = mutableMapOf(
            'x' to 1..4000,
            'm' to 1..4000,
            'a' to 1..4000,
            's' to 1..4000,
        ),
    ): Long {
        var combinations = 0L
        val currAccepted = accepted.toMutableMap()

        for (condition in workflows[currentWorkflow]!!.compactedConditions) {
            if (accepted.values.any { it.last == 0 }) return combinations

            if (condition is TerminalCondition) {
                if (condition.result == "A") {
                    return combinations + currAccepted.values.map { it.last - it.first + 1 }
                        .fold(1L) { acc, num -> acc * num }
                }
                if (condition.result == "R") {
                    return combinations
                }
                return combinations + dfs(workflows, condition.result, currAccepted)
            }

            val workflowCondition = condition as WorkflowCondition
            if (workflowCondition.result != "R") {
                currAccepted.toMutableMap().let { newAccepted ->
                    newAccepted[workflowCondition.param] =
                        calculateNewRange(newAccepted, workflowCondition)
                    combinations += if (workflowCondition.result == "A") {
                        newAccepted.values.map { it.last - it.first + 1 }
                            .fold(1L) { acc, num -> acc * num }
                    } else {
                        dfs(workflows, workflowCondition.result, newAccepted)
                    }
                }
            }

            val reverseWorkflowCondition = WorkflowCondition(
                workflowCondition.param,
                op = if (workflowCondition.op == '>') '<' else '>',
                value = if (workflowCondition.op == '>') workflowCondition.value + 1 else workflowCondition.value - 1,
                result = workflowCondition.result
            )
            currAccepted[workflowCondition.param] =
                calculateNewRange(currAccepted, reverseWorkflowCondition)
        }
        return combinations
    }

    private fun calculateNewRange(
        accepted: Map<Char, IntRange>,
        condition: WorkflowCondition,
    ): IntRange = when (val paramOpPair = "${condition.param}${condition.op}") {
        "x<" -> accepted['x']!!.rangeIntersect(IntRange(1, condition.value - 1))
        "x>" -> accepted['x']!!.rangeIntersect(IntRange(condition.value + 1, 4000))
        "m<" -> accepted['m']!!.rangeIntersect(IntRange(1, condition.value - 1))
        "m>" -> accepted['m']!!.rangeIntersect(IntRange(condition.value + 1, 4000))
        "a<" -> accepted['a']!!.rangeIntersect(IntRange(1, condition.value - 1))
        "a>" -> accepted['a']!!.rangeIntersect(IntRange(condition.value + 1, 4000))
        "s<" -> accepted['s']!!.rangeIntersect(IntRange(1, condition.value - 1))
        "s>" -> accepted['s']!!.rangeIntersect(IntRange(condition.value + 1, 4000))
        else -> throw IllegalArgumentException("Unknown operation: $paramOpPair")
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

    private fun IntRange.rangeIntersect(other: IntRange): IntRange {
        if (this.first > other.last || this.last < other.first) {
            return IntRange(1, 0)
        }
        return IntRange(max(this.first, other.first), min(this.last, other.last))
    }

    data class Workflow(val name: String, private val conditions: List<Condition>) {
        val compactedConditions = mutableListOf<Condition>()
        private val fnConditions = mutableListOf<(XmasPart) -> String>()

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
            val compactedConditions =
                conditions.asReversed().dropWhile { it.result == terminalCondition.result }
                    .toMutableList()
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
