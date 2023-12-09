package days

class Day7 : Day {
    override fun solvePart1(): String {
        val input = super.readFile("day_7.txt")
        val cardsToBid = input.lines().map { it.split(" ") }.map { it[0].toList() to it[1].toInt() }

        val sorted = cardsToBid.map { Triple(Cards(it.first.map(::convertCharToIntKinda)), it.second, convertToPoints(it.first))}.sortedWith(
            compareBy<Triple<Cards, Int, Int>> {it.third}.thenBy { it.first })

        return sorted.foldIndexed(0) {index, acc, triple -> acc + (index + 1) * triple.second }.toString()
    }

    private fun convertToPoints(cards: List<Char>): Int {
        val counts = cards.groupingBy { it }.eachCount()

        if (counts.getOrDefault('J', 0 ) == 5) return 7


        return when (counts.entries.filter {it.key != 'J'}.maxOfOrNull {it.value} ?: 1) {
            1 -> 1
            2 -> if (counts.size == 4) 2 else 3
            3 -> if (counts.size == 3) 4 else 5
            4 -> 6
            5 -> 7
            else -> throw IllegalArgumentException("Should not be possible")
        } + counts.getOrDefault('J', 0)
    }

    private fun convertCharToIntKinda(char: Char): Int {
        return when (char) {
            'T' -> 10
            'J' -> 0
            'Q' -> 12
            'K' -> 13
            'A' -> 14
            else -> if (char.isDigit()) char.digitToInt() else throw IllegalArgumentException("Cannot convert to Int")
        }
    }

    private operator fun <T : Comparable<T>> List<T>.compareTo(other: List<T>): Int {
        return this.zip(other)
            .map { (a, b) -> a.compareTo(b) }
            .firstOrNull { it != 0 } ?: 0
    }
    override fun solvePart2(): String {
        TODO("Not yet implemented")
    }

    private data class Cards(val cards: List<Int>): Comparable<Cards> {
        override fun compareTo(other: Cards): Int {
            return this.cards.zip(other.cards)
                .map { (a, b) -> a.compareTo(b) }
                .firstOrNull { it != 0 } ?: 0
        }

    }
}