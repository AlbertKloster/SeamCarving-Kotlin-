package seamcarving

import java.util.*

fun main(args: Array<String>) {
    val inputFile = getInputFile(args)
    val outputFile = getOutputFile(args)
    val reductionWidth = getReductionWidth(args)
    val reductionHeight = getReductionHeight(args)

    if (inputFile == null || outputFile == null) {
        println("Wrong arguments")
        return
    }

    val imageProcessor: ImageProcessor = ImageIOHandler()
    imageProcessor.processImage(inputFile, outputFile, reductionWidth, reductionHeight)
}

fun isSeam(coordinate: Coordinate, seam: List<Coordinate>): Boolean {
    return seam.any { it.x == coordinate.x && it.y == coordinate.y }
}

fun getReductionHeight(args: Array<String>): Int? {
    val indexIn = args.indexOf("-height")
    if (indexIn != -1 && args.size > indexIn + 1) return args[indexIn + 1].toInt()
    return null
}

fun getReductionWidth(args: Array<String>): Int? {
    val indexIn = args.indexOf("-width")
    if (indexIn != -1 && args.size > indexIn + 1) return args[indexIn + 1].toInt()
    return null
}

private fun getInputFile(args: Array<String>): String? {
    val indexIn = args.indexOf("-in")
    if (indexIn != -1 && args.size > indexIn + 1) return args[indexIn + 1]
    return null
}

private fun getOutputFile(args: Array<String>): String? {
    val indexIn = args.indexOf("-out")
    if (indexIn != -1 && args.size > indexIn + 1) return args[indexIn + 1]
    return null
}

fun getVerticalSeam(matrix: List<List<Double>>): List<Coordinate> {
    val numRows = matrix.size
    val numCols = matrix[0].size

    // Create a priority queue to store vertices with their distances
    val priorityQueue = PriorityQueue<Pair<Coordinate, Double>>(compareBy { it.second })

    // Create a map to keep track of the distances
    val distanceMap = mutableMapOf<Coordinate, Double>()

    // Initialize the priority queue with the starting vertex
    val start = Coordinate(0, 0)
    val end = Coordinate(numCols - 1, numRows - 1)
    priorityQueue.offer(start to matrix[0][0])
    distanceMap[start] = matrix[0][0]

    // Initialize a map to keep track of the previous vertex in the shortest path
    val previousMap = mutableMapOf<Coordinate, Coordinate>()

    while (priorityQueue.isNotEmpty()) {
        val (current, currentDistance) = priorityQueue.poll()

        if (current == end) {
            // We reached the last element, break the loop
            break
        }

        // Explore neighboring vertices in the same row in the first and the last rows only
        if (current.y == 0 || current.y == numRows - 1)
            explore(current, currentDistance, numCols, numRows, matrix, distanceMap, priorityQueue, previousMap, 0)

        // Explore neighboring vertices in the next row
        explore(current, currentDistance, numCols, numRows, matrix, distanceMap, priorityQueue, previousMap, 1)
    }

    // Reconstruct the shortest path
    val shortestPath = mutableListOf<Coordinate>()
    var current = end

    while (current != start) {
        shortestPath.add(current)
        current = previousMap[current] ?: break
    }

    shortestPath.add(start)
    shortestPath.reverse()

    // return a list with path coordinates excluding the first and the last rows
    // and shift y-coordinate by one to match the initial energy matrix
    return shortestPath.filter { coordinate -> coordinate.y > 0 && coordinate.y < numRows - 1 }
        .map { coordinate -> Coordinate(coordinate.x, coordinate.y - 1) }
}

private fun explore(
    current: Coordinate,
    currentDistance: Double,
    numCols: Int,
    numRows: Int,
    matrix: List<List<Double>>,
    distanceMap: MutableMap<Coordinate, Double>,
    priorityQueue: PriorityQueue<Pair<Coordinate, Double>>,
    previousMap: MutableMap<Coordinate, Coordinate>,
    offset: Int
) {
    for (dx in -1..1) {
        val nextX = current.x + dx
        if (nextX < 0 || nextX >= numCols) continue

        val next = Coordinate(nextX, current.y + offset)
        if (next.y < 0 || next.y >= numRows) continue // Check for valid row
        val edgeWeight = matrix[next.y][next.x]
        val newDistance = currentDistance + edgeWeight

        if (newDistance < (distanceMap[next] ?: Double.MAX_VALUE)) {
            distanceMap[next] = newDistance
            priorityQueue.offer(next to newDistance)
            previousMap[next] = current
        }
    }
}
