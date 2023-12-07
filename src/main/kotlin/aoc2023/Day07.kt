package aoc2023

class Day07 {
    fun part1(input: List<String>): Long {
        val sortedCards = input.map { parseCameCard(it, isJokerRule = false) }.sorted()
        return IntRange(1, sortedCards.size).zip(sortedCards).sumOf { (rank, card) -> rank * card.bid * 1L }
    }

    fun part2(input: List<String>): Long {
        val sortedCards = input.map { parseCameCard(it, isJokerRule = true) }.sorted()
        return IntRange(1, sortedCards.size).zip(sortedCards).sumOf { (rank, card) -> rank * card.bid * 1L }
    }

    private fun parseCameCard(camelCard: String, isJokerRule: Boolean): CamelCard {
        val (card, bid) = camelCard.split(' ')
        return CamelCard(card, bid.toInt(), isJokerRule)
    }

    enum class CombinationType(val priority: Int) {
        HIGH_CARD(1),
        ONE_PAIR(2),
        TWO_PAIR(3),
        THREE_OF_A_KIND(4),
        FULL_HOUSE(5),
        FOUR_OF_A_KIND(6),
        FIVE_OF_A_KIND(7),
    }

    data class CamelCard(val card: String, val bid: Int, val isJokerRule: Boolean = false) : Comparable<CamelCard> {
        private val numbers = convertToNumbers(card, isJokerRule)
        private val type = if (isJokerRule) getTypeWithJoker(card) else getType(card)

        override fun compareTo(other: CamelCard): Int {
            if (type.priority < other.type.priority) return -1
            if (type.priority > other.type.priority) return 1

            for ((thisNum, otherNum) in numbers.zip(other.numbers)) {
                if (thisNum < otherNum) return -1
                if (thisNum > otherNum) return 1
            }

            return 0
        }

        private companion object {
            fun convertToNumbers(card: String, isJokerRule: Boolean): List<Int> = card.toCharArray()
                .map { num ->
                    when (num) {
                        'T' -> 10
                        'J' -> if (isJokerRule) 1 else 11
                        'Q' -> 12
                        'K' -> 13
                        'A' -> 14
                        else -> num.digitToInt()
                    }
                }

            fun getType(card: String): CombinationType {
                val counts = card.groupingBy { it }.eachCount().values
                return calculateType(counts)
            }

            fun getTypeWithJoker(card: String): CombinationType {
                val counts = card.groupingBy { it }.eachCount()
                val jokerCounts = counts.getOrDefault('J', 0)

                if (jokerCounts == 0) return getType(card)
                if (jokerCounts == 5) return CombinationType.FIVE_OF_A_KIND

                val otherCounts = counts.filterKeys { it != 'J' }.values
                val type = calculateType(otherCounts)

                return if (type == CombinationType.FOUR_OF_A_KIND) CombinationType.FIVE_OF_A_KIND
                else if (type == CombinationType.THREE_OF_A_KIND && jokerCounts == 2) CombinationType.FIVE_OF_A_KIND
                else if (type == CombinationType.THREE_OF_A_KIND) CombinationType.FOUR_OF_A_KIND
                else if (type == CombinationType.TWO_PAIR) CombinationType.FULL_HOUSE
                else if (type == CombinationType.ONE_PAIR && jokerCounts == 3) CombinationType.FIVE_OF_A_KIND
                else if (type == CombinationType.ONE_PAIR && jokerCounts == 2) CombinationType.FOUR_OF_A_KIND
                else if (type == CombinationType.ONE_PAIR) CombinationType.THREE_OF_A_KIND
                else if (jokerCounts == 4) CombinationType.FIVE_OF_A_KIND
                else if (jokerCounts == 3) CombinationType.FOUR_OF_A_KIND
                else if (jokerCounts == 2) CombinationType.THREE_OF_A_KIND
                else CombinationType.ONE_PAIR
            }

            private fun calculateType(counts: Collection<Int>): CombinationType =
                if (counts.contains(5)) CombinationType.FIVE_OF_A_KIND
                else if (counts.contains(4)) CombinationType.FOUR_OF_A_KIND
                else if (counts.contains(2) && counts.contains(3)) CombinationType.FULL_HOUSE
                else if (counts.contains(3)) CombinationType.THREE_OF_A_KIND
                else if (counts.count { it == 2 } == 2) CombinationType.TWO_PAIR
                else if (counts.contains(2)) CombinationType.ONE_PAIR
                else CombinationType.HIGH_CARD
        }
    }
}

fun main() {
    val day07 = Day07()
    val input = readInputAsStringList("day07.txt")

    println("07, part 1: ${day07.part1(input)}")
    println("07, part 2: ${day07.part2(input)}")
}
