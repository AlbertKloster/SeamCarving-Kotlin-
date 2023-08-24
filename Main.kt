package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


fun main() {
    println("Enter rectangle width:")
    val width = readln().toInt()

    println("Enter rectangle height:")
    val height = readln().toInt()

    println("Enter output image name:")
    val imageName = readln()

    val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val graphics = bufferedImage.createGraphics()

    graphics.paint = Color.BLACK
    graphics.fillRect(0, 0, width, height)

    graphics.color = Color.RED
    graphics.drawLine(0, 0, width - 1, height - 1)
    graphics.drawLine(0, height - 1, width - 1, 0)

    ImageIO.write(bufferedImage, "png", File(imageName))

}
