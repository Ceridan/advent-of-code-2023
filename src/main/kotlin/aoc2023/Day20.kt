package aoc2023

class Day20 {
    fun part1(input: String): Long {
        return 0
    }

    fun part2(input: String): Long {
        return 0
    }

    abstract class MachineModule(open val name: String) {
        abstract fun processInput(moduleName: String, pulse: Int)
        abstract fun processOutput(): Pair<Int, List<String>>
        abstract fun connectModule(moduleName: String)
        abstract fun printState(): String
    }

    data class ButtonModule(override val name: String) : MachineModule(name) {
        private val modules = mutableListOf<String>()
        override fun processInput(moduleName: String, pulse: Int) {}

        override fun processOutput(): Pair<Int, List<String>> = 0 to modules

        override fun connectModule(moduleName: String) {
            modules.add(moduleName)
        }

        override fun printState(): String = "$name -> $modules"
    }

    data class BroadcastModule(override val name: String) : MachineModule(name) {
        private val modules = mutableListOf<String>()
        private var pulse = 0
        override fun processInput(moduleName: String, pulse: Int) {
            this.pulse = pulse
        }

        override fun processOutput(): Pair<Int, List<String>> = pulse to modules

        override fun connectModule(moduleName: String) {
            modules.add(moduleName)
        }

        override fun printState(): String = "$name -> $pulse -> $modules"
    }

    data class FlipFlopModule(override val name: String) : MachineModule(name) {
        private val modules = mutableListOf<String>()
        private var pulse = 1
        private var isOn = false
        override fun processInput(moduleName: String, pulse: Int) {
            if (pulse == 0) {
                isOn = !isOn
                this.pulse = 0
            }
        }

        override fun processOutput(): Pair<Int, List<String>> {
            val receivers = if (pulse == 0) modules else listOf()
            val pulse = if (isOn) 1 else 0
            return pulse to receivers
        }

        override fun connectModule(moduleName: String) {
            modules.add(moduleName)
        }

        override fun printState(): String = "$name -> { $isOn, $pulse} -> $modules"
    }

    data class ConjunctionModule(override val name: String) : MachineModule(name) {
        private val modules = mutableMapOf<String, Int>()

        override fun processInput(moduleName: String, pulse: Int) {
            modules[moduleName] = pulse
        }

        override fun processOutput(): Pair<Int, List<String>> {
            val pulse = if (modules.values.all { it == 1 }) 0 else 1
            return pulse to modules.keys.toList()
        }

        override fun connectModule(moduleName: String) {
            modules[moduleName] = 0
        }

        override fun printState(): String = "$name -> $modules"
    }
}

fun main() {
    val day20 = Day20()
    val input = readInputAsString("day20.txt")

    println("20, part 1: ${day20.part1(input)}")
    println("20, part 2: ${day20.part2(input)}")
}
