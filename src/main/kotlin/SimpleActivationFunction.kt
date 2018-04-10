import kotlin.math.tanh

enum class SimpleActivationFunction : ActivationFunction {
    TANH{
        override fun invoke(s: Double) = tanh(s)

        override fun derivative(s: Double) = 1.0 - s*s // approximation of 1.0 - tanh(s)*tanh(s)
    }
}