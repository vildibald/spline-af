import golem.pow
import java.util.stream.IntStream
import kotlin.math.sqrt

class SplineNetwork(val activationFunction: Spline, vararg topology: Int) : Network
(activationFunction, *topology) {

    var lambdaQ = 0.00001

    init {
        lambdaW = 0.001
    }

    override fun computeError(targets: DoubleArray): Double {
        val q = activationFunction.noisedKnots
        val q0 = activationFunction.knots
        val diffSum = q.mapIndexed { i, value -> pow(value - q0[i], 2) }.sumByDouble { it }
        return super.computeError(targets) + lambdaQ * sqrt(diffSum)
    }

}