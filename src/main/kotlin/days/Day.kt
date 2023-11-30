package days

interface Day {
    fun solve() {
        solvePart1()
        solvePart2()
    }
    fun solvePart1()
    fun solvePart2()
    fun readFile(fileName: String): String {
        return ClassLoader.getSystemResource("days/$fileName").readText()
    }
}