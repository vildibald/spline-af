import com.sun.media.sound.InvalidFormatException
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.math.roundToInt

val trainingFile = "testData.txt"

fun generateXorTrainingData(fileName: String): File {
    val lines = mutableListOf<String>()
    lines.add("2 4 1")
    for (i in 2000 downTo 0) {
        val n1 = (2.0 * Math.random()).toInt()
        val n2 = (2.0 * Math.random()).toInt()
        val o = n1 xor n2
        lines.add("in: $n1 $n2")
        lines.add("out: $o")
    }
    val path = Files.write(Paths.get(fileName), lines, Charset.defaultCharset())
    return File(path.toUri())
}

fun testNetwork(trainingData: TrainingData, network: Network): Int {
    val inputs = trainingData.inputs
    val outputs = trainingData.outputs

    if (inputs.size != outputs.size) throw InvalidFormatException("Corrupted training data. Number " +
            "of inputs does not match number of outputs.")

    var lastWrongPass = -1

    for (i in 0 until inputs.size) {
        println("Pass: $i")

        println("Input: ${Arrays.toString(inputs[i])}")
        network.feedForward(*inputs[i])

        val results = network.results()
        println("NN output: ${Arrays.toString(results)}")

        println("True output: ${Arrays.toString(outputs[i])}")
        network.backPropagation(*outputs[i])

        if (!(results.map { it.roundToInt() }.toIntArray()
                        contentEquals
                        outputs[i].map { it.roundToInt() }.toIntArray()))
            lastWrongPass = i

        println("Recent average error: ${network.recentAverageError}\n")

    }

    println("Last missed pass: $lastWrongPass")
    return lastWrongPass
}


fun main(args: Array<String>) {
//    generateXorTrainingData(trainingFile)
    val trainingData = TrainingData(File(trainingFile))
    val topology = trainingData.topology

    val activationFunction = SimpleActivationFunction.TANH
    val knots = Knots(-20.0, 20.0, 200, function = { activationFunction(it) })
    val catMulRomSplineActivationFunction = CatmulRomSpline(knots)

    val simpleNetwork = DefaultNetwork(activationFunction, *topology)
    val catmulRomSplineNetwork = SplineNetwork(catMulRomSplineActivationFunction, *topology)

//    val lastWrongSimple = testNetwork(trainingData, simpleNetwork)
    val lastWrongCMRSpline = testNetwork(trainingData, catmulRomSplineNetwork)

//    println("Last missed pass standard NN: $lastWrongSimple")
    println("Last missed pass CMRS NN: $lastWrongCMRSpline")

}