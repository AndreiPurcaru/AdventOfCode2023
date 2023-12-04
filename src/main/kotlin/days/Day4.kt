package days

import kotlin.math.pow

class Day4 : Day {
    private val whitespaceRegex = Regex("\\s+")
    override fun solvePart1(): String {
        val input = super.readFile("day_4.txt")

        return input.lines().sumOf{ splitAndCreateCards(it).calculateWinningScore()}.toString()
    }

    private fun splitAndCreateCards(line: String): ScratchCard {
        val split = line.split(Regex("[:|]"))

        val winningNumbers = convertToInt(split[1]).toSet()
        val currentNumbers = convertToInt(split[2])

        return ScratchCard(winningNumbers, currentNumbers)
    }

    private fun convertToInt(numbersLine: String): List<Int> {
        return numbersLine.trim().split(whitespaceRegex).map(String::toInt)
    }

    override fun solvePart2(): String {
        val input = super.readFile("day_4.txt")

        val cardsToAmount = input.lines()
            .map {CardAmount(splitAndCreateCards(it), 1)}

        for (cardIndex in cardsToAmount.indices) {
            val cardToAmount = cardsToAmount[cardIndex]
            val amountOfWinningNumbers = cardToAmount.card.calculateNumberOfWinningTickets()
            for (copyIndex in cardIndex + 1 ..< cardIndex + amountOfWinningNumbers + 1) {
                cardsToAmount[copyIndex].amount += cardToAmount.amount
            }
        }

        return cardsToAmount.sumOf { it.amount }.toString()
    }

    private data class ScratchCard(val winningNumbers: Set<Int>, val currentNumbers: List<Int>) {
        fun calculateWinningScore(): Int {
            val winningPower = calculateNumberOfWinningTickets() - 1
            if (winningPower < 0) {
                return 0
            }
            return 2.0.pow(winningPower).toInt()
        }

        fun calculateNumberOfWinningTickets(): Int {
            return currentNumbers.count { winningNumbers.contains(it) }
        }
    }

    private class CardAmount(val card: ScratchCard, var amount: Int)
}