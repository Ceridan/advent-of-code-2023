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
        val almanac = parseAlmanac(input)
        var key = "seed"
        var items = almanac.seeds.windowed(2, 2).map { LongRange(it[0], it[0] + it[1]) }.toSet()

        while (key in almanac.keys) {
            val keyPair = key to almanac.keys[key]!!
            val newItems = mutableSetOf<LongRange>()

            val queue = ArrayDeque(items)
            queue@ while (queue.isNotEmpty()) {
                val range = queue.removeFirst()
                val transforms = almanac.transforms.getOrDefault(keyPair, listOf())
                for (transform in transforms) {
                    val (left, intersection, right) = range.splitIntersect(transform.source)
                    if (intersection != null) {
                        val dest = LongRange(
                            transform.destination.first + (intersection.first - transform.source.first),
                            transform.destination.first + (intersection.last - transform.source.first)
                        )
                        newItems.add(dest)
                        if (left != null) queue.addFirst(left)
                        if (right != null) queue.addFirst(right)
                        continue@queue
                    }
                }
                newItems.add(range)
            }
            items = newItems
            key = almanac.keys.getOrDefault(key, "")
        }

        return items.minOf { it.first }
    }

    private fun LongRange.splitIntersect(other: LongRange): Triple<LongRange?, LongRange?, LongRange?> {
        var left: LongRange? = null
        var intersect: LongRange? = null
        var right: LongRange? = null

        if (first < other.first) {
            left = LongRange(first, last.coerceAtMost(other.first - 1L))
        }

        if (last > other.last) {
            right = LongRange(first.coerceAtLeast(other.last + 1L), last)
        }

        if ((other.first in first..last) || (first in other.first..other.last)) {
            intersect = LongRange(first.coerceAtLeast(other.first), last.coerceAtMost(other.last))
        }

        return Triple(left, intersect, right)
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

        return Almanac(seeds, transforms, transforms.keys.associateBy({ it.first }, { it.second }))
    }

    data class Almanac(val seeds: List<Long>, val transforms: Transform, val keys: Map<String, String>)
    data class TransformEntry(val source: LongRange, val destination: LongRange)
}

fun main() {
    val day05 = Day05()
    val input = readInputAsString("day05.txt")

    println("05, part 1: ${day05.part1(input)}")
    println("05, part 2: ${day05.part2(input)}")
}
