package days

class Day1 : Day {
    private val digitWordRegex = Regex("(?=(zero|one|two|three|four|five|six|seven|eight|nine|\\d))")

    override fun solvePart1(): String {
        val input = super.readFile("day_1.txt")
        return input.lines().sumOf(::combineFirstAndLastDigit).toString()
    }

    private fun combineFirstAndLastDigit(line: String): Int {
        return line.findFirstDigit() * 10 + line.findLastDigit()
    }

    private fun String.findFirstDigit(): Int {
        return this.first{it.isDigit()}.digitToInt()
    }

    private fun String.findLastDigit(): Int {
        return this.last{it.isDigit()}.digitToInt()
    }

    override fun solvePart2(): String {
        val input = super.readFile("day_1.txt")

        return input.lines()
            .map { line ->
                digitWordRegex.findAll(line)
                    .mapNotNull { it.groups[1]?.value }
                    .map(::convertToDigit)
                    .joinToString()
            }.sumOf(::combineFirstAndLastDigit).toString()
    }

    private fun convertToDigit(word: String): Char {
        return when (word) {
            "zero" -> '0'
            "one" -> '1'
            "two" -> '2'
            "three" -> '3'
            "four" -> '4'
            "five" -> '5'
            "six" -> '6'
            "seven" -> '7'
            "eight" -> '8'
            "nine" -> '9'
            else -> if (word.first()
                    .isDigit()
            ) word.first() else throw IllegalArgumentException("Not a valid word or digit.")
        }
    }

}