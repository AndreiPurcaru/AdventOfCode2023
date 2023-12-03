package days

class Day2 : Day {
    private val numberRegex = Regex("(\\d+)")
    private val numberColorRegex = Regex("(\\d+)(?=\\s(blue|red|green))")
    private val maxCubesPerColor = mapOf(
        "red" to 12,
        "green" to 13,
        "blue" to 14
    )

    override fun solvePart1(): String {
        val input = super.readFile("day_2.txt")
        return input.lines().sumOf(::checkGamePossible).toString()
    }

    /**
     * Checks if a game is possible based on the provided line.
     *
     * @param line the line containing game information
     * @return the game ID if the game is possible, otherwise 0
     */
    private fun checkGamePossible(line: String): Int {
        val gameId = numberRegex.find(line)?.value!!.toInt()
        if (line.split(";").none(::isRoundImpossible)) {
            return gameId
        }
        return 0
    }

    private fun isRoundImpossible(round: String): Boolean {
        return numberColorRegex.findAll(round).any(::checkCubesOverLimit)
    }

    private fun checkCubesOverLimit(match: MatchResult): Boolean {
        val numberOfCubes = match.value.toInt()
        val colorOfCubes = match.groups[2]?.value
        return numberOfCubes > maxCubesPerColor[colorOfCubes]!!
    }

    override fun solvePart2(): String {
        val input = super.readFile("day_2.txt")
        return input.lines()
            .map(::findGameMax)
            .sumOf { it.findPowerSet() }.toString()
    }

    private fun findGameMax(game: String): RedGreenBlue {
        return game.split(";").fold(RedGreenBlue.new()) { acc, round -> acc.findMax(findRoundMax(round)) }
    }

    private fun findRoundMax(round: String): RedGreenBlue {
        return numberColorRegex.findAll(round)
            .fold(RedGreenBlue.new()) { acc, matchResult -> acc.findMax(checkMax(matchResult)) }
    }

    private fun checkMax(match: MatchResult): RedGreenBlue {
        val numberOfCubes = match.value.toInt()
        val colorOfCubes = match.groups[2]?.value!!
        return RedGreenBlue.new().findMax(colorOfCubes, numberOfCubes)
    }

    data class RedGreenBlue(val red: Int, val green: Int, val blue: Int) {
        companion object {
            fun new(): RedGreenBlue {
                return RedGreenBlue(0, 0, 0)
            }
        }

        fun findMax(color: String, newValue: Int): RedGreenBlue {
            return when (color) {
                "red" -> RedGreenBlue(maxOf(this.red, newValue), this.green, this.blue)
                "green" -> RedGreenBlue(this.red, maxOf(this.green, newValue), this.blue)
                "blue" -> RedGreenBlue(this.red, this.green, maxOf(this.blue, newValue))
                else -> throw IllegalArgumentException("Unknown color provided")
            }
        }

        fun findMax(other: RedGreenBlue): RedGreenBlue {
            return RedGreenBlue(
                maxOf(this.red, other.red),
                maxOf(this.green, other.green),
                maxOf(this.blue, other.blue)
            )
        }

        fun findPowerSet(): Int {
            return this.red * this.green * this.blue
        }
    }
}