import java.util.*
import kotlin.math.sqrt

fun gaussianNoise(mean: Double, variance: Double): Double {
    val random = Random()
    return random.nextGaussian() * sqrt(variance) + mean
}

fun gaussianNoise() = gaussianNoise(0.0, 0.0001)