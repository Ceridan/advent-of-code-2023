package aoc2023

class Day20 {
    fun part1(input: String): Long {
        val modules = parseInput(input)
        for (i in 1..1) {
            val queue = ArrayDeque(listOf("button"))
            while (queue.isNotEmpty()) {
                val module = modules[queue.removeFirst()]!!
                val (pulse, outputs) = module.processOutput()
                for (output in outputs) {
                    modules[output]!!.processInput(module.name, pulse)
                    queue.add(output)
                }
            }
        }
        return modules.values.sumOf { it.lowCounter } * modules.values.sumOf { it.highCounter }
    }

    fun part2(input: String): Long {
        return 0
    }

    private fun parseInput(input: String): Map<String, MachineModule> {
        val lines = input.split('\n').filter { it.isNotEmpty() }
        val modules: MutableMap<String, MachineModule> =
            mutableMapOf("button" to ButtonModule("button", listOf("broadcaster")))
        for (line in lines) {
            val (module, outputString) = line.split(" -> ")
            val moduleName = module.drop(1)
            val outputs = outputString.split(',').filter { it.isNotEmpty() }.map { it.trim() }
            when (module[0]) {
                '%' -> modules[moduleName] = FlipFlopModule(moduleName, outputs)
                '&' -> modules[moduleName] = ConjunctionModule(moduleName, outputs)
                else -> modules["broadcaster"] = BroadcastModule("broadcaster", outputs)
            }
        }
        return modules
    }

    abstract class MachineModule(open val name: String, protected open val modules: List<String>) {
        var lowCounter = 0L
        var highCounter = 0L
        abstract fun processInput(moduleName: String, pulse: Int)
        abstract fun processOutput(): Pair<Int, List<String>>
        abstract fun printState(): String
    }

    data class ButtonModule(override val name: String, override val modules: List<String>) :
        MachineModule(name, modules) {
        override fun processInput(moduleName: String, pulse: Int) {}
        override fun processOutput(): Pair<Int, List<String>> {
            lowCounter += modules.size
            return 0 to modules
        }

        override fun printState(): String = "$name -> $modules"
    }

    data class BroadcastModule(override val name: String, override val modules: List<String>) :
        MachineModule(name, modules) {
        private var pulse = 0

        override fun processInput(moduleName: String, pulse: Int) {
            this.pulse = pulse
        }

        override fun processOutput(): Pair<Int, List<String>> {
            if (pulse == 0) {
                lowCounter += modules.size
            } else {
                highCounter += modules.size
            }
            return pulse to modules
        }

        override fun printState(): String = "$name -> $pulse -> $modules"
    }

    data class FlipFlopModule(override val name: String, override val modules: List<String>) :
        MachineModule(name, modules) {
        private val pulses = ArrayDeque<Int>()
        private var isOn = false
        override fun processInput(moduleName: String, pulse: Int) {
            pulses.add(pulse)
        }

        override fun processOutput(): Pair<Int, List<String>> {
            val pulse = pulses.removeFirst()
            if (pulse == 1) {
                return pulse to listOf()
            }

            isOn = !isOn
            return if (isOn) {
                highCounter += modules.size
                1 to modules
            } else {
                lowCounter += modules.size
                0 to modules
            }
        }

        override fun printState(): String = "$name -> { $isOn } -> $modules"
    }

    data class ConjunctionModule(override val name: String, override val modules: List<String>) :
        MachineModule(name, modules) {
        private val modulesMap = modules.associateBy({ it }, { 0 }).toMutableMap()

        override fun processInput(moduleName: String, pulse: Int) {
            modulesMap[moduleName] = pulse
        }

        override fun processOutput(): Pair<Int, List<String>> {
            return if (modulesMap.values.all { it == 1 }) {
                lowCounter += modules.size
                0 to modules
            } else {
                highCounter += modules.size
                1 to modules
            }
        }

        override fun printState(): String = "$name -> $modulesMap"
    }
}

fun main() {
    val day20 = Day20()
    val input = readInputAsString("day20.txt")

    println("20, part 1: ${day20.part1(input)}")
    println("20, part 2: ${day20.part2(input)}")
}
