package seamcarving

import java.awt.Color
import java.io.File
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.sqrt

fun main(args: Array<String>) {

    val inputFile = getInputFile(args)
    val outputFile = getOutputFile(args)

    if (inputFile == null || outputFile == null) {
        println("Wrong arguments")
        return
    }

    val bufferedImage = try {
        ImageIO.read(File(inputFile))
    } catch (e: IOException) {
        println("Input file $inputFile not found")
        return
    }

    val energyMatrix = mutableListOf<MutableList<Double>>()

    for (x in 0 until bufferedImage.width) {
        val energyColumn = mutableListOf<Double>()
        for (y in 0 until bufferedImage.height) {
            val xCorr = if (x == 0) 1 else if (x == bufferedImage.width - 1) x - 1 else x
            val yCorr = if (y == 0) 1 else if (y == bufferedImage.height - 1) y - 1 else y
            val colorLeft = Color(bufferedImage.getRGB(xCorr - 1, y))
            val colorRight = Color(bufferedImage.getRGB(xCorr + 1, y))
            val colorUp = Color(bufferedImage.getRGB(x, yCorr - 1))
            val colorDown = Color(bufferedImage.getRGB(x, yCorr + 1))
            val gradX =
                (colorLeft.red - colorRight.red) * (colorLeft.red - colorRight.red).toDouble() + (colorLeft.green - colorRight.green) * (colorLeft.green - colorRight.green).toDouble() + (colorLeft.blue - colorRight.blue) * (colorLeft.blue - colorRight.blue).toDouble()

            val gradY =
                (colorUp.red - colorDown.red) * (colorUp.red - colorDown.red).toDouble() + (colorUp.green - colorDown.green) * (colorUp.green - colorDown.green).toDouble() + (colorUp.blue - colorDown.blue) * (colorUp.blue - colorDown.blue).toDouble()

            val energy = sqrt(gradX + gradY)

            energyColumn.add(energy)
        }
        energyMatrix.add(energyColumn)
    }

    val matrix = mutableListOf<MutableList<Double>>()

    // row of zeros as entry and exit point for the path
    val zeroRow = MutableList(energyMatrix.size) { 0.0 }
    matrix.add(zeroRow)

    for (y in 0 until energyMatrix.first().size) {
        val row = mutableListOf<Double>()
        for (x in 0 until energyMatrix.size) {
            row.add(energyMatrix[x][y])
        }
        matrix.add(row)
    }
    matrix.add(zeroRow)
    val shortestPath = findShortestPath(matrix)

    for (coordinates in shortestPath) {
        bufferedImage.setRGB(coordinates.x, coordinates.y, Color(255, 0, 0).rgb)
    }

    ImageIO.write(bufferedImage, "png", File(outputFile))

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

data class Coordinate(val x: Int, val y: Int)

fun findShortestPath(matrix: List<List<Double>>): List<Coordinate> {
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
        if (current.y == 0 || current.y == numRows - 1) {
            for (dx in -1..1) {
                val nextX = current.x + dx
                if (nextX < 0 || nextX >= numCols || nextX == current.x) continue

                val next = Coordinate(nextX, current.y)
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

        // Explore neighboring vertices in the next row
        for (dx in -1..1) {
            val nextX = current.x + dx
            if (nextX < 0 || nextX >= numCols) continue

            val next = Coordinate(nextX, current.y + 1)
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