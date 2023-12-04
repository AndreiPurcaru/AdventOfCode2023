package days

class Day3 : Day {
    override fun solvePart1(): String {
        val input = super.readFile("day_3.txt")
        val matrix = input.lines().map { it.toCharArray() }.toTypedArray()

        val partNumberList: MutableList<Int> = mutableListOf()
        var currentNumber = 0
        var isCurrentPartNumber = false

        for (rowIndex in matrix.indices) {
            if (isCurrentPartNumber) {
                partNumberList.add(currentNumber)
            }
            currentNumber = 0
            isCurrentPartNumber = false
            for (columnIndex in matrix[rowIndex].indices) {
                val currentElement = matrix[rowIndex][columnIndex]
                if (!currentElement.isDigit()) {
                    if (isCurrentPartNumber) {
                        partNumberList.add(currentNumber)
                    }
                    currentNumber = 0
                    isCurrentPartNumber = false
                    continue
                }
                currentNumber = currentNumber * 10 + currentElement.digitToInt()
                isCurrentPartNumber = isCurrentPartNumber || checkSurroundingsForSymbol(matrix, rowIndex, columnIndex)
            }
        }

        return partNumberList.sum().toString()
    }

    private fun checkSurroundingsForSymbol(matrix: Array<CharArray>, rowIndex: Int, columnIndex: Int): Boolean {
        return Directions.entries.any {
            checkForSymbolAt(matrix, rowIndex + it.rowOffset, columnIndex + it.columnOffset)
        }
    }

    private fun checkForSymbolAt(matrix: Array<CharArray>, rowIndex: Int, columnIndex: Int): Boolean {
        if (rowIndex < 0 || rowIndex >= matrix.size || columnIndex < 0 || columnIndex >= matrix[rowIndex].size) {
            return false
        }
        val currentElement = matrix[rowIndex][columnIndex]
        return !currentElement.isLetterOrDigit() && currentElement != '.'
    }

    override fun solvePart2(): String {
        val input = super.readFile("day_3.txt")
        val matrix = input.lines().map { it.toCharArray() }.toTypedArray()

        val gearCoordToPartNumbers: MutableMap<Pair<Int, Int>, MutableList<Int>> = mutableMapOf()
        var currentNumber = 0
        var currentGear: Pair<Int, Int>? = null

        for (rowIndex in matrix.indices) {
            if (currentGear != null && currentNumber != 0) {
                gearCoordToPartNumbers.getOrPut(currentGear) { mutableListOf() }.add(currentNumber)
            }
            currentNumber = 0
            currentGear = null
            for (columnIndex in matrix[rowIndex].indices) {
                val currentElement = matrix[rowIndex][columnIndex]
                if (!currentElement.isDigit()) {
                    if (currentGear != null && currentNumber != 0) {
                        gearCoordToPartNumbers.getOrPut(currentGear) { mutableListOf() }.add(currentNumber)
                    }
                    currentNumber = 0
                    currentGear = null
                    continue
                }
                currentNumber = currentNumber * 10 + currentElement.digitToInt()

                if (currentGear == null) {
                    currentGear = checkSurroundingsForGear(matrix, rowIndex, columnIndex)
                }
            }
        }

        return gearCoordToPartNumbers.values.filter { it.size == 2 }
            .sumOf { it.reduce { acc, element -> acc * element } }.toString()
    }

    private fun checkSurroundingsForGear(matrix: Array<CharArray>, rowIndex: Int, columnIndex: Int): Pair<Int, Int>? {
        Directions.entries.forEach {
            val maybeGear = checkForGearAt(matrix, rowIndex + it.rowOffset, columnIndex + it.columnOffset)
            if (maybeGear != null) {
                return maybeGear
            }
        }
        return null
    }

    private fun checkForGearAt(matrix: Array<CharArray>, rowIndex: Int, columnIndex: Int): Pair<Int, Int>? {
        if (rowIndex < 0 || rowIndex >= matrix.size || columnIndex < 0 || columnIndex >= matrix[rowIndex].size) {
            return null
        }
        val currentElement = matrix[rowIndex][columnIndex]
        if (currentElement == '*') {
            return Pair(rowIndex, columnIndex)
        }
        return null
    }


    private enum class Directions(val rowOffset: Int, val columnOffset: Int) {
        UP(0, -1),
        UP_RIGHT(1, -1),
        RIGHT(1, 0),
        DOWN_RIGHT(1, 1),
        DOWN(0, 1),
        DOWN_LEFT(-1, 1),
        LEFT(-1, 0),
        UP_LEFT(-1, -1);
    }
}