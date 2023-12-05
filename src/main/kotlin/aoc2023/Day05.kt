package aoc2023

typealias Transform = Map<Pair<String, String>, List<Day05.TransformEntry>>

class Day05 {
    fun part1(input: String): Long {
        val almanac = parseAlmanac(input)
        var key = "seed"
        val items = almanac.seeds.toLongArray()

        while (key in almanac.keys) {
            val keyPair = key to almanac.keys[key]!!
            items@ for (i in items.indices) {
                val transforms = almanac.transforms.getOrDefault(keyPair, listOf())
                for (transform in transforms) {
                    if (transform.source.contains(items[i])) {
                        items[i] = transform.destination.first + (items[i] - transform.source.first)
                        continue@items
                    }
                }
            }
            key = almanac.keys.getOrDefault(key, "")
        }

        return items.min()
    }

    fun part2(input: String): Long {
        return 0L
    }

    private fun parseAlmanac(almanac: String): Almanac {
        val lines = almanac.split('\n')
        val seeds = lines.first().split(' ').drop(1).filter { it.isNotEmpty() }.map { it.toLong() }

        val titleRegex = "^([a-z]+)-to-([a-z]+) map:$".toRegex()
        val numsRegex = "^(\\d+) (\\d+) (\\d+)$".toRegex()
        val transforms = mutableMapOf<Pair<String, String>, MutableList<TransformEntry>>()
        var key: Pair<String, String>? = null

        for (line in lines.drop(2).filter { it.isNotEmpty() }) {
            val numsMatch = numsRegex.find(line)
            if (numsMatch != null) {
                val (dest, source, count) = numsMatch.destructured
                val entry = TransformEntry(
                    source = LongRange(source.toLong(), source.toLong() + count.toLong() - 1),
                    destination = LongRange(dest.toLong(), dest.toLong() + count.toLong() - 1),
                )
                transforms[key]!!.add(entry)
                continue
            }

            val titleMatch = titleRegex.find(line)
            if (titleMatch != null) {
                val (source, dest) = titleMatch.destructured
                key = source to dest
                transforms[key] = mutableListOf()
            }
        }

        return Almanac(seeds, transforms, transforms.keys.associateBy({it.first}, {it.second}))
    }

    data class Almanac(val seeds: List<Long>, val transforms: Transform, val keys: Map<String, String>) {}
    data class TransformEntry(val source: LongRange, val destination: LongRange) {}
}

fun main() {
    val day05 = Day05()
    val input = readInputAsString("day05.txt")

    println("05, part 1: ${day05.part1(input)}")
    println("05, part 2: ${day05.part2(input)}")
}
