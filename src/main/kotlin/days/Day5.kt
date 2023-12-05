package days

import kotlinx.coroutines.*
import kotlinx.coroutines.awaitAll
class Day5 : Day {
    private val endsWithMap = Regex(".*map:\\n")
    override fun solvePart1(): String {
        val input = super.readFile("day_5.txt")
        val (seeds, mappers) = interpretInput(input)

        return seeds.minOf { seed -> mappers.fold(seed) { acc, mapper -> mapper.convert(acc) } }.toString()
    }

    private fun interpretInput(input: String): Pair<List<Long>, List<Mapper>> {
        val seeds = interpretSeeds(input)

        val intermediateStep = input.substringAfter("\n\n").split(endsWithMap).filter(String::isNotBlank)

        val mappers = intermediateStep.map { createMapper(it.trim()) }

        return Pair(seeds, mappers)
    }

    private fun interpretSeeds(seedsInput: String): List<Long> {
        return seedsInput.substringBefore("\n").substringAfter(":").trim().split(" ").map(String::toLong)
    }

    private fun createMapper(rangeMap: String): Mapper {
        return Mapper(rangeMap.split("\n").map(DestinationSourceRange::from))
    }

    override fun solvePart2(): String {
        val input = super.readFile("day_5.txt")
        val (naiveSeeds, mappers) = interpretInput(input)

        val seeds = naiveSeeds.asSequence()
            .windowed(2, 2) { (seed, range) -> seed..<seed + range }.flatten()

        return runBlocking { parallelSeedMapping(seeds, mappers).toString() }
    }

    private suspend fun parallelSeedMapping(seeds: Sequence<Long>, mappers: List<Mapper>): Long {
        return coroutineScope {
            val deferredResults = seeds.map { seed ->
                async {
                    mappers.fold(seed) { acc, mapper -> mapper.convert(acc) }
                }
            }

            val mappedSeeds = deferredResults.toList().awaitAll()
            mappedSeeds.minOrNull() ?: throw NoSuchElementException("No minimum value found")
        }
    }

    private data class Mapper(val map: List<DestinationSourceRange>) {
        fun convert(value: Long): Long {
            for ((destination, source, range) in map) {
                if (source <= value && value < source + range) {
                    return value + (destination - source)
                }
            }
            return value
        }
    }

    private data class DestinationSourceRange(val destination: Long, val source: Long, val range: Long) {
        companion object {
            fun from(formattedString: String): DestinationSourceRange {
                val splitFormattedString = formattedString.split(" ").map(String::toLong)
                return DestinationSourceRange(splitFormattedString[0], splitFormattedString[1], splitFormattedString[2])
            }
        }
    }
}