package aoc2023

import org.jgrapht.alg.clustering.GirvanNewmanClustering
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DefaultUndirectedGraph
import java.lang.IllegalStateException

class Day25 {
    fun part1(input: List<String>): Int {
        val parsedGraph = parseInput(input)
        val vertices = parsedGraph.map { (k, v) -> v + k }.flatten().toSet()
        val edges = parsedGraph.map { (k, v) -> v.map { k to it } }.flatten().toSet()

        val graph = DefaultUndirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)
        vertices.forEach { graph.addVertex(it) }
        edges.forEach { graph.addEdge(it.first, it.second) }

        // https://en.wikipedia.org/wiki/Girvan%E2%80%93Newman_algorithm
        val clustering = GirvanNewmanClustering(graph, 2).clustering

        if (clustering.numberClusters != 2) throw IllegalStateException("Can't build exactly 2 clusters.")

        return clustering.clusters[0].size * clustering.clusters[1].size
    }

    private fun parseInput(lines: List<String>): Map<String, Set<String>> {
        val graph = mutableMapOf<String, MutableSet<String>>()
        lines.forEach { line ->
            val (src, rest) = line.split(':')
            if (src !in graph) {
                graph[src] = mutableSetOf()
            }

            val destinations = rest.split(' ').filter { it.isNotEmpty() }
            destinations.forEach { dest ->
                graph[src]!!.add(dest)
            }
        }
        return graph
    }

}

fun main() {
    val day25 = Day25()
    val input = readInputAsStringList("day25.txt")

    println("25, part 1: ${day25.part1(input)}")
    println("25, part 2: -")
}
