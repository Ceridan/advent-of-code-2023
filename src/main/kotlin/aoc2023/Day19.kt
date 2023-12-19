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

        for (rule in workflows[currentWorkflow]!!.compactRules()) {
            if (accepted.values.any { it.last == 0 }) return combinations

            if (rule.result != "R") {
                currAccepted.toMutableMap().let { newAccepted ->
                    newAccepted[rule.param] =
                        calculateNewRange(newAccepted, rule)
                    combinations += if (rule.result == "A") {
                        newAccepted.values.map { it.last - it.first + 1 }
                            .fold(1L) { acc, num -> acc * num }
                    } else {
                        dfs(workflows, rule.result, newAccepted)
                    }
                }
            }
            currAccepted[rule.param] = calculateNewRange(currAccepted, rule.inverse())
        }
        return combinations
    }

    private fun calculateNewRange(
        accepted: Map<Char, IntRange>,
        rule: Rule,
    ): IntRange = when (val paramOpPair = "${rule.param}${rule.op}") {
        "x<" -> accepted['x']!!.rangeIntersect(IntRange(1, rule.value - 1))
        "x>" -> accepted['x']!!.rangeIntersect(IntRange(rule.value + 1, 4000))
        "m<" -> accepted['m']!!.rangeIntersect(IntRange(1, rule.value - 1))
        "m>" -> accepted['m']!!.rangeIntersect(IntRange(rule.value + 1, 4000))
        "a<" -> accepted['a']!!.rangeIntersect(IntRange(1, rule.value - 1))
        "a>" -> accepted['a']!!.rangeIntersect(IntRange(rule.value + 1, 4000))
        "s<" -> accepted['s']!!.rangeIntersect(IntRange(1, rule.value - 1))
        "s>" -> accepted['s']!!.rangeIntersect(IntRange(rule.value + 1, 4000))
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
            val (name, rawRules) = workflowRegex.find(line)!!.destructured
            val rules = rawRules.split(',').map {
                val condRules = it.split(':')
                if (condRules.size == 1) {
                    Rule('x', '>', 0, condRules[0])
                } else {
                    val param = condRules[0].take(1)[0]
                    val op = condRules[0].drop(1).take(1)[0]
                    val value = condRules[0].drop(2).toInt()
                    val result = condRules[1]
                    Rule(param, op, value, result)
                }
            }
            workflows[name] = Workflow(name, rules)
        }
        return workflows to parts
    }

    private fun IntRange.rangeIntersect(other: IntRange): IntRange {
        if (this.first > other.last || this.last < other.first) {
            return IntRange(1, 0)
        }
        return IntRange(max(this.first, other.first), min(this.last, other.last))
    }

    data class Workflow(val name: String, private val rules: List<Rule>) {
        private val fnRules = mutableListOf<(XmasPart) -> String>()

        init {
            fnRules.addAll(rules.map { convertToFn(it) })
        }

        fun checkPart(part: XmasPart): String {
            for (fnCond in fnRules) {
                val result = fnCond(part)
                if (result != "C") return result
            }

            throw IllegalStateException("No conditions are applicable. Workflow: $name, XmasPart: $part")
        }

        fun compactRules(): List<Rule> {
            val terminalCondition = rules.last()
            val compactedConditions =
                rules.asReversed().dropWhile { it.result == terminalCondition.result }
                    .toMutableList()
            compactedConditions.reverse()
            compactedConditions.add(terminalCondition)
            return compactedConditions
        }

        private fun convertToFn(rule: Rule): (XmasPart) -> String {
            return when (val paramOpPair = "${rule.param}${rule.op}") {
                "x<" -> { xmas: XmasPart -> if (xmas.x < rule.value) rule.result else "C" }
                "x>" -> { xmas: XmasPart -> if (xmas.x > rule.value) rule.result else "C" }
                "m<" -> { xmas: XmasPart -> if (xmas.m < rule.value) rule.result else "C" }
                "m>" -> { xmas: XmasPart -> if (xmas.m > rule.value) rule.result else "C" }
                "a<" -> { xmas: XmasPart -> if (xmas.a < rule.value) rule.result else "C" }
                "a>" -> { xmas: XmasPart -> if (xmas.a > rule.value) rule.result else "C" }
                "s<" -> { xmas: XmasPart -> if (xmas.s < rule.value) rule.result else "C" }
                "s>" -> { xmas: XmasPart -> if (xmas.s > rule.value) rule.result else "C" }
                else -> throw IllegalArgumentException("Unknown operation: $paramOpPair")
            }
        }
    }

    data class Rule(val param: Char, val op: Char, val value: Int, val result: String) {
        fun inverse(): Rule {
            val invertedOp = if (op == '>') '<' else '>'
            val invertedValue = if (op == '>') value + 1 else value - 1
            return Rule(param, invertedOp, invertedValue, result)
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
