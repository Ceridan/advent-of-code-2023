package aoc2023

class Day20 {
    fun part1(input: String): Long {
        val modules = parseInput(input)
        var low = 0L
        var high = 0L
        repeat(1000) {
            val queue = ArrayDeque(listOf("button"))
            while (queue.isNotEmpty()) {
                val module = modules[queue.removeFirst()]!!
                module.processOutput()?.let { pulse ->
                    for (output in module.outputs) {
                        if (pulse == 0) {
                            low++
                        } else {
                            high++
                        }

                        modules[output]!!.processInput(module.name, pulse)
                        queue.add(output)
                    }
                }
            }
        }
        return low * high
    }

    fun part2(input: String): Long {
        val modules = parseInput(input)
        val rxInputsL1 = modules["rx"]!!.inputs
        val rxInputsL2 = rxInputsL1.map { modules[it]!!.inputs }.flatten()
        val rxInputsL2Cycles = rxInputsL2.associateWith { 0L }.toMutableMap()
        var pressCounter = 1L
        while (true) {
            val queue = ArrayDeque(listOf("button"))
            while (queue.isNotEmpty()) {
                val module = modules[queue.removeFirst()]!!
                module.processOutput()?.let { pulse ->
                    for (output in module.outputs) {
                        if (output == "rx" && pulse == 0) return pressCounter
                        if (output in rxInputsL2Cycles && pulse == 0) {
                            rxInputsL2Cycles[output] = pressCounter
                            if (rxInputsL2Cycles.values.all { it > 0L }) {
                                return rxInputsL2Cycles.values.reduce { acc, value -> lcm(acc, value) }
                            }
                        }
                        modules[output]!!.processInput(module.name, pulse)
                        queue.add(output)
                    }
                }
            }
            pressCounter++
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

        val modules = mutableMapOf<String, MachineModule>("button" to ButtonModule())
        for (line in lines) {
            val module = line.takeWhile { it != ' ' }
            val moduleType = module[0]
            val moduleName = if (module == "broadcast") "broadcast" else module.drop(1)
            when (moduleType) {
                '%' -> modules[moduleName] =
                    FlipFlopModule(moduleName, inputs = inputs[moduleName]!!, outputs = outputs[moduleName]!!)

                '&' -> modules[moduleName] =
                    ConjunctionModule(moduleName, inputs = inputs[moduleName]!!, outputs = outputs[moduleName]!!)

                else -> modules["broadcaster"] = BroadcastModule(outputs[moduleName]!!)
            }
        }

        inputs.filter { it.key !in modules }.forEach { (name, inputs) ->
            modules[name] = OutputModule(name, inputs = inputs)
        }

        return modules
    }

    abstract class MachineModule(
        val name: String,
        val inputs: List<String> = listOf(),
        val outputs: List<String> = listOf()
    ) {
        open fun processInput(moduleName: String, pulse: Int) {}
        open fun processOutput(): Int? = null
    }

    class ButtonModule : MachineModule("button", outputs = listOf("broadcaster")) {
        override fun processOutput(): Int = 0
    }

    class BroadcastModule(outputs: List<String>) :
        MachineModule("broadcaster", inputs = listOf("button"), outputs = outputs) {
        private var pulse = 0
        override fun processInput(moduleName: String, pulse: Int) {
            this.pulse = pulse
        }

        override fun processOutput(): Int = pulse
    }

    class FlipFlopModule(name: String, inputs: List<String>, outputs: List<String>) :
        MachineModule(name, inputs = inputs, outputs = outputs) {
        private val pulses = ArrayDeque<Int>()
        private var isOn = 0
        override fun processInput(moduleName: String, pulse: Int) {
            pulses.add(pulse)
        }

        override fun processOutput(): Int? {
            val pulse = pulses.removeFirst()
            if (pulse == 1) return null
            isOn = (isOn + 1) % 2
            return isOn
        }
    }

    class ConjunctionModule(name: String, inputs: List<String>, outputs: List<String>) :
        MachineModule(name, inputs = inputs, outputs = outputs) {
        private val inputSignals = inputs.associateWith { 0 }.toMutableMap()
        override fun processInput(moduleName: String, pulse: Int) {
            inputSignals[moduleName] = pulse
        }

        override fun processOutput(): Int = if (inputSignals.values.all { it == 1 }) 0 else 1
    }

    class OutputModule(name: String, inputs: List<String>) : MachineModule(name, inputs = inputs)
}

fun main() {
    val day20 = Day20()
    val input = readInputAsString("day20.txt")

    println("20, part 1: ${day20.part1(input)}")
    println("20, part 2: ${day20.part2(input)}")
}
