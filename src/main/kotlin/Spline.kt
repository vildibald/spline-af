import golem.ceil
import golem.end
import golem.mat
import golem.matrix.Matrix
import kotlin.math.floor

abstract class Spline(val knots: Knots) : ActivationFunction {

    protected abstract val basis: Matrix<Double>

    val noisedKnots = DoubleArray(knots.size, init = {
        val count = knots.size
        val numberToNoise = ceil(0.05 * count)
        val indexToNoise = count / numberToNoise
        if (indexToNoise % (it+1) != 0) knots[it] else knots[it] + gaussianNoise()
    })


    override fun invoke(s: Double) = (referenceVector(s) * basis * closestKnots(s))[0]

    override fun derivative(s: Double) = (referenceVectorDerivative(s) * basis * closestKnots(s)
            /knots.diff)[0]

    private fun referenceVector(s: Double): Matrix<Double> {
        val sh = s / knots.diff
        val u = sh - floor(sh)
        val u2 = u * u
        val u3 = u2 * u

        return mat[u3, u2, u, 1]
    }

    private fun referenceVectorDerivative(s: Double): Matrix<Double> {
        val sh = s / knots.diff
        val u = sh - floor(sh)
        val u2 = u * u

        return mat[3 * u2, 2 * u, 1, 0]
    }

    private fun closestKnots(s: Double): Matrix<Double> {
        val i = knots.nearestLeftIndex(s)
        return mat[noisedKnots[i], noisedKnots[i + 1], noisedKnots[i + 2], noisedKnots[i + 3]].T
    }
}