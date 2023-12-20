package aoc2023

class Day20 {
    fun part1(input: String): Long {
        val modules = parseInput(input)
        for (i in 1..1000) {
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
        val modules = parseInput(input)
        val rxDepsL1 = modules.values.filter { "rx" in it.modules }.map { it.name }
        val rxDepsL2 =
            modules.values.filter { rxDepsL1.toSet().intersect(it.modules.toSet()).isNotEmpty() }.map { it.name }
        val l2Cycles = rxDepsL2.associateWith { 0L }.toMutableMap()
        var counter = 1L
        while (true) {
            val queue = ArrayDeque(listOf("button"))
            while (queue.isNotEmpty()) {
                val module = modules[queue.removeFirst()]!!
                val (pulse, outputs) = module.processOutput()
                for (output in outputs) {
                    if (output == "rx" && pulse == 0) return counter
                    if (output in l2Cycles && pulse == 0) {
                        l2Cycles[output] = counter
                        if (l2Cycles.values.all { it > 0 }) {
                            return l2Cycles.values.reduce { acc, value -> lcm(acc, value) }
                        }
                    }
                    modules[output]!!.processInput(module.name, pulse)
                    queue.add(output)
                }
            }
            counter++
        }
    }

    private fun parseInput(input: String): Map<String, MachineModule> {
        val lines = input.split('\n').filter { it.isNotEmpty() }
        val inputs = mutableMapOf<String, MutableList<String>>()
        val outputs = mutableMapOf<String, List<String>>()
        for (line in lines) {
            val (module, outputString) = line.split(" -> ")
            val moduleName = if (module == "broadcast") "broadcast" else module.drop(1)
            val outs = outputString.split(',').filter { it.isNotEmpty() }.map { it.trim() }
            outputs[moduleName] = outs
            for (out in outs) {
                if (out !in inputs) {
                    inputs[out] = mutableListOf()
                }
                inputs[out]!!.add(moduleName)
            }
        }

        val modules = mutableMapOf<String, MachineModule>("button" to ButtonModule("button", listOf("broadcaster")))
        for (line in lines) {
            val module = line.takeWhile { it != ' ' }
            val moduleType = module[0]
            val moduleName = if (module == "broadcast") "broadcast" else module.drop(1)
            when (moduleType) {
                '%' -> modules[moduleName] = FlipFlopModule(moduleName, outputs[moduleName]!!)
                '&' -> modules[moduleName] = ConjunctionModule(moduleName, outputs[moduleName]!!, inputs[moduleName]!!)
                else -> modules["broadcaster"] = BroadcastModule("broadcaster", outputs[moduleName]!!)
            }
        }

        for (moduleName in inputs.keys) {
            if (moduleName !in modules) {
                modules[moduleName] = OutputModule(moduleName, listOf())
            }
        }

        return modules
    }

    abstract class MachineModule(open val name: String, open val modules: List<String>) {
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

    data class ConjunctionModule(
        override val name: String,
        override val modules: List<String>,
        private val inputs: List<String>
    ) :
        MachineModule(name, modules) {
        private val inputMap = inputs.associateWith { 0 }.toMutableMap()

        override fun processInput(moduleName: String, pulse: Int) {
            inputMap[moduleName] = pulse
        }

        override fun processOutput(): Pair<Int, List<String>> {
            return if (inputMap.values.all { it == 1 }) {
                lowCounter += modules.size
                0 to modules
            } else {
                highCounter += modules.size
                1 to modules
            }
        }

        override fun printState(): String = "$name -> $inputMap"
    }

    data class OutputModule(override val name: String, override val modules: List<String>) :
        MachineModule(name, modules) {
        override fun processInput(moduleName: String, pulse: Int) {}
        override fun processOutput(): Pair<Int, List<String>> = 0 to listOf()

        override fun printState(): String = "$name -> $modules"
    }
}

fun main() {
    val day20 = Day20()
    val input = readInputAsString("day20.txt")

    println("20, part 1: ${day20.part1(input)}")
    println("20, part 2: ${day20.part2(input)}")
}
