package days

class Day8 : Day {
    private val mapExtractRegex = Regex("(\\w+) = \\((\\w+), (\\w+)\\)")

    override fun solvePart1(): String {
        val input = super.readFile("day_8.txt")

        val (leftRight, map) = parseInput(input)

        val leftRightSequence = generateSequence { leftRight }.flatten()

        val startingNode = "AAA"

        return walkMap(map, leftRightSequence, startingNode) { node -> node == "ZZZ" }.toString()
    }

    override fun solvePart2(): String {
        val input = super.readFile("day_8.txt")

        val (leftRight, map) = parseInput(input)

        val nodes = map.keys.filter { it.endsWith('A') }

        return lcm(nodes.map {
            walkMap(
                map,
                generateSequence { leftRight }.flatten(),
                it
            ) { node -> node.endsWith('Z') }
        }).toString()
    }

    private fun parseInput(input: String): Pair<List<Char>, Map<String, Pair<String, String>>> {
        val leftRight = input.substringBefore("\n").toList()

        val rawMap = input.substringAfter("\n\n").lines()

        val map = rawMap.associate(::extractPairFromString).toMap()
        return Pair(leftRight, map)
    }

    private fun extractPairFromString(formattedString: String): Pair<String, Pair<String, String>> {
        val matchResult = mapExtractRegex.find(formattedString)
        val (key, left, right) = matchResult!!.destructured
        return Pair(key, Pair(left, right))
    }

    private fun walkMap(
        map: Map<String, Pair<String, String>>,
        leftRightSequence: Sequence<Char>,
        startingNode: String,
        check: (String) -> Boolean
    ): Long {
        var result = 0L
        var currentNode = startingNode

        for ((index, side) in leftRightSequence.withIndex()) {
            if (check(currentNode)) {
                result = index.toLong()
                break
            }
            when (side) {
                'L' -> currentNode = map[currentNode]!!.first
                'R' -> currentNode = map[currentNode]!!.second
            }
        }
        return result
    }

    private fun gcd(a: Long, b: Long): Long {
        return if (b == 0L) a else gcd(b, a % b)
    }

    private fun lcm(a: Long, b: Long): Long {
        return if (a == 0L || b == 0L) 0 else (a * b) / gcd(a, b)
    }

    private fun lcm(list: List<Long>): Long {
        return list.reduce { acc, element -> lcm(acc, element) }
    }
}