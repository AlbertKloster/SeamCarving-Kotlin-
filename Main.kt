package seamcarving

import java.awt.Color
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

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

    for (x in 0 until bufferedImage.width) {
        for (y in 0 until bufferedImage.height) {
            val color = Color(bufferedImage.getRGB(x, y))
            val r = 255 - color.red
            val g = 255 - color.green
            val b = 255 - color.blue
            bufferedImage.setRGB(x, y, Color(r, g, b).rgb)
        }
    }

    ImageIO.write(bufferedImage, "png", File(outputFile))



//    println("Enter rectangle width:")
//    val width = readln().toInt()
//
//    println("Enter rectangle height:")
//    val height = readln().toInt()
//
//    println("Enter output image name:")
//    val imageName = readln()
//
//    val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
//    val graphics = bufferedImage.createGraphics()
//
//    graphics.paint = Color.BLACK
//    graphics.fillRect(0, 0, width, height)
//
//    graphics.color = Color.RED
//    graphics.drawLine(0, 0, width - 1, height - 1)
//    graphics.drawLine(0, height - 1, width - 1, 0)
//
//    ImageIO.write(bufferedImage, "png", File(imageName))

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
