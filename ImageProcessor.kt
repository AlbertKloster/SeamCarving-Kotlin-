package seamcarving

interface ImageProcessor {
    fun processImage(inputFile: String, outputFile: String, reductionWidth: Int?, reductionHeight: Int?)
}