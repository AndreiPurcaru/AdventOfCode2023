package days

class Day6 : Day {
    override fun solvePart1(): String {
        val input = super.readFile("day_6.txt")
        val lines = input.lines()
        val time = lines[0].extractAndConvertNumbers("Time:")
        val distance = lines[1].extractAndConvertNumbers("Distance:")

        return time.zip(distance).map(TimeDistance::from).map(::findAllowedWays)
            .reduce { acc, element -> acc * element }.toString()
    }

    private fun String.extractAndConvertNumbers(lineStart: String): List<Long> {
        return this.substringAfter(lineStart).split(Regex("\\s+")).filter(String::isNotBlank).map(String::toLong)
    }

    private fun findAllowedWays(timeDistance: TimeDistance): Int {
        return (1..timeDistance.time).count { spentTime -> spentTime * (timeDistance.time - spentTime) > timeDistance.distance }
    }

    override fun solvePart2(): String {
        val input = super.readFile("day_6.txt")
        val lines = input.lines()
        val time = lines[0].extractAndConvertNumbers("Time:").joinToString(separator = "").toLong()
        val distance = lines[1].extractAndConvertNumbers("Distance:").joinToString(separator = "").toLong()

        return findAllowedWays(TimeDistance(time, distance)).toString()
    }

    private data class TimeDistance(val time: Long, val distance: Long) {
        companion object {
            fun from(pair: Pair<Long, Long>): TimeDistance {
                return TimeDistance(pair.first, pair.second)
            }
        }
    }
}