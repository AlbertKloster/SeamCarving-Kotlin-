package seamcarving

import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

class ImageIOHandler : ImageProcessor {
    override fun processImage(inputFile: String, outputFile: String, reductionWidth: Int?, reductionHeight: Int?) {
        try {
            val bufferedImage = ImageIO.read(File(inputFile))
            val processor = SeamCarver()

            val reducedBufferedImage = processor.reduceImage(bufferedImage, reductionWidth, reductionHeight)

            ImageIO.write(reducedBufferedImage, "png", File(outputFile))
        } catch (e: IOException) {
            println("Input file $inputFile not found")
        }
    }
}
