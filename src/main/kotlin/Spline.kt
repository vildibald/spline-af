import golem.ceil
import golem.end
import golem.mat
import golem.matrix.Matrix
import kotlin.math.floor

abstract class Spline(val knots: Knots) : ActivationFunction {

     abstract val basis: Matrix<Double>

    val noisedKnots = DoubleArray(knots.size, init = {
        val count = knots.size
        val numberToNoise = ceil(0.05 * count)
        val indexToNoise = count / numberToNoise
        if (indexToNoise % (it + 1) != 0) knots[it] else knots[it] //+ gaussianNoise()
    })


    override fun invoke(s: Double) = (referenceVector(s) * basis * closestKnots(s)).sumByDouble { it }

    override fun derivative(s: Double) = (referenceVectorDerivative(s) * basis * closestKnots(s)
            / knots.diff)[0]

    fun referenceVector(s: Double): Matrix<Double> {
        val sh = s / knots.diff
        val u = sh - floor(sh)
        val u2 = u * u
        val u3 = u2 * u

        return mat[1.0, u, u2, u3] //mat[u3, u2, u, 1]
    }

    private fun referenceVectorDerivative(s: Double): Matrix<Double> {
        val sh = s / knots.diff
        val u = sh - floor(sh)
        val u2 = u * u

        return mat[0.0, 1.0, 2.0 * u, 3.0 * u2]//mat[3.0 * u2, 2.0 * u, 1.0, 0.0]
    }

    private fun closestKnots(s: Double): Matrix<Double> {
        val i = knots.nearestLeftIndex(s)
        return mat[noisedKnots[i - 1], noisedKnots[i], noisedKnots[i + 1], noisedKnots[i + 2]].T
    }
}