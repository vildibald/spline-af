typealias Layer = List<Neuron>

class Neuron(private val index: Int, val activationFunction: ActivationFunction, outputCount: Int) {
    var output = 1.0
    val connections: Connections = Array(outputCount, init = { Connection.random() })
    private var gradient: Double = 0.0

    internal fun feedForward(previousLayer: Layer) {
        val sum = (0 until previousLayer.size).sumByDouble {
            previousLayer[it].output *
                    previousLayer[it].connections[index].weight
        }
        output = activationFunction(sum)
    }

    internal fun outputGradients(target: Double) {
        val delta = target - output
        gradient = delta * activationFunction.derivative(output)
    }

    internal fun hiddenGradients(nextLayer: Layer) {
        val dow = (0 until nextLayer.size - 1).sumByDouble {
            connections[it].weight * nextLayer[it].gradient
        }
        gradient = dow * activationFunction.derivative(output)
    }



    internal fun updateInputWeights(previousLayer: Layer) {
        previousLayer.forEach {
            val oldDeltaWeight = it.connections[index].delta
            val newDeltaWeight = ETA * it.output * gradient + ALPHA * oldDeltaWeight
            it.connections[index] = it.connections[index].copy(
                    weight = it.connections[index].weight + newDeltaWeight,
                    delta = newDeltaWeight
            )

        }
    }


}