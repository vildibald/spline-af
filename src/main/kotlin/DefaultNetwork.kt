import kotlin.math.sqrt

class DefaultNetwork(activationFunction: ActivationFunction, vararg topology: Int) :
        Network(activationFunction, *topology)

