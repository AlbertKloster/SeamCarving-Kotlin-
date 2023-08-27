package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.sqrt

class SeamCarver {
    fun reduceImage(inputImage: BufferedImage, reductionWidth: Int?, reductionHeight: Int?): BufferedImage {
        var reducedBufferedImage = copyBufferedImage(inputImage)

        reductionWidth?.let {
            repeat(it) {
                reducedBufferedImage = removeVerticalSeam(reducedBufferedImage)
            }
        }

        reducedBufferedImage = reducedBufferedImage.rotateImage90DegreesLeft()

        reductionHeight?.let {
            repeat(it) {
                reducedBufferedImage = removeVerticalSeam(reducedBufferedImage)
            }
        }

        reducedBufferedImage = reducedBufferedImage.rotateImage90DegreesRight()

        return reducedBufferedImage
    }

    private fun copyBufferedImage(bufferedImage: BufferedImage): BufferedImage {
        val colorModel = bufferedImage.colorModel
        val isAlphaPremultiplied = bufferedImage.isAlphaPremultiplied
        val raster = bufferedImage.copyData(null)
        return BufferedImage(colorModel, raster, isAlphaPremultiplied, null)
    }

    private fun removeVerticalSeam(bufferedImage: BufferedImage): BufferedImage {
        val reducedBufferedImage = BufferedImage(bufferedImage.width - 1, bufferedImage.height, BufferedImage.TYPE_INT_RGB)
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
        val verticalSeam = getVerticalSeam(matrix)

        for (y in 0 until bufferedImage.height) {
            var shift = false
            for (x in 0 until bufferedImage.width) {
                if (isSeam(Coordinate(x, y), verticalSeam)) {
                    shift = true
                } else {
                    reducedBufferedImage.setRGB(if (shift) x - 1 else x, y, bufferedImage.getRGB(x, y))
                }
            }
        }
        return reducedBufferedImage
    }

    private fun BufferedImage.rotateImage90DegreesRight(): BufferedImage {
        val width = this.width
        val height = this.height
        val rotatedImage = BufferedImage(height, width, this.type)

        for (x in 0 until width) {
            for (y in 0 until height) {
                val pixel = this.getRGB(x, y)
                rotatedImage.setRGB(height - 1 - y, x, pixel)
            }
        }
        return rotatedImage
    }

    private fun BufferedImage.rotateImage90DegreesLeft(): BufferedImage {
        val width = this.width
        val height = this.height
        val rotatedImage = BufferedImage(height, width, this.type)

        for (x in 0 until width) {
            for (y in 0 until height) {
                val pixel = this.getRGB(x, y)
                rotatedImage.setRGB(y, width - 1 - x, pixel)
            }
        }
        return rotatedImage
    }
}