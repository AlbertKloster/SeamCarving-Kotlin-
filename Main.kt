package seamcarving

import java.awt.Color
import java.io.File
import java.io.IOException
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
            val gradX = (colorLeft.red - colorRight.red) * (colorLeft.red - colorRight.red).toDouble() +
                    (colorLeft.green - colorRight.green) * (colorLeft.green - colorRight.green).toDouble() +
                    (colorLeft.blue - colorRight.blue) * (colorLeft.blue - colorRight.blue).toDouble()

            val gradY = (colorUp.red - colorDown.red) * (colorUp.red - colorDown.red).toDouble() +
                    (colorUp.green - colorDown.green) * (colorUp.green - colorDown.green).toDouble() +
                    (colorUp.blue - colorDown.blue) * (colorUp.blue - colorDown.blue).toDouble()

            val energy = sqrt(gradX + gradY)

            energyColumn.add(energy)
        }
        energyMatrix.add(energyColumn)
    }

    val maxEnergyValue = energyMatrix.flatten().maxOrNull()

    for (x in 0 until bufferedImage.width) {
        for (y in 0 until bufferedImage.height) {
            val intensity = (255.0 * energyMatrix[x][y] / maxEnergyValue!!).toInt()
            bufferedImage.setRGB(x, y, Color(intensity, intensity, intensity).rgb)
        }
    }

    ImageIO.write(bufferedImage, "png", File(outputFile))

}

private fun getInputFile(args: Array<String>): String? {
    val indexIn = args.indexOf("-in")
    if (indexIn != -1 && args.size > indexIn + 1)
        return args[indexIn + 1]
    return null
}

private fun getOutputFile(args: Array<String>): String? {
    val indexIn = args.indexOf("-out")
    if (indexIn != -1 && args.size > indexIn + 1)
        return args[indexIn + 1]
    return null
}
