package days

interface Day {
    fun solve(): Pair<String, String> {
        return Pair(solvePart1(), solvePart2())
    }
    fun solvePart1(): String
    fun solvePart2(): String
    fun readFile(fileName: String): String {
        return ClassLoader.getSystemResource("days/$fileName").readText()
    }
}