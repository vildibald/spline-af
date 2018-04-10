import kotlin.math.sqrt

typealias Layers = Array<Layer>

abstract class Network(activationFunction:ActivationFunction, vararg topology: Int) {
    private val layers: Layers
    var error = 0.0
    var recentAverageError = 0.0

    var lambdaW = 0.001

    companion object {
        var recentAverageSmoothingFactor = 100.0
    }


    init {
        val layerCount = topology.size
        layers = Layers(layerCount, init = { i ->
            val layer = ArrayList<Neuron>(layerCount)
            val outputCount = if (i == layerCount - 1) 0 else topology[i + 1]

            (0..topology[i]).forEach { n ->
                layer.add(Neuron(index = n, outputCount = outputCount, activationFunction =
                activationFunction))
            }

            layer
        })
    }

    fun feedForward(vararg inputs: Double) {
        if (inputs.size != layers[0].size - 1) throw IllegalArgumentException("Number of training " +
                "inputs does not fit the number of neuron input")

        inputs.forEachIndexed { i, d -> layers[0][i].output = d }

        (1 until layers.size).forEach { i ->
            val previousLayer = layers[i - 1]
            (0 until layers[i].size - 1).forEach { n ->
                layers[i][n].feedForward(previousLayer)
            }
        }
    }

    fun backPropagation(vararg targets: Double) {
        error = computeError(targets)
        recentAverageError = computeRecentAverageMeasurement()
        outputLayerGradients(targets)
        hiddenLayerGradients()
        updateConnections()
    }

    private fun updateConnections() {
        (layers.size - 1 downTo 1).forEach { i ->
            val layer = layers[i]
            val previousLayer = layers[i - 1]
            (0 until layer.size - 1).forEach {
                layer[it].updateInputWeights(previousLayer)
            }
        }
    }

    private fun hiddenLayerGradients() {
        (layers.size - 2 downTo 1).forEach {
            val hiddenLayer = layers[it]
            val nextLayer = layers[it + 1]
            hiddenLayer.forEach { it.hiddenGradients(nextLayer) }
        }
    }

    private fun outputLayerGradients(targets: DoubleArray) {
        val outputLayer = layers.last()
        (0 until outputLayer.size - 1).forEach {
            outputLayer[it].outputGradients(targets[it])
        }
    }


    private fun computeRecentAverageMeasurement() =
            (recentAverageError * recentAverageSmoothingFactor + error) /
                    (recentAverageSmoothingFactor + 1.0)


    protected open fun computeError(targets: DoubleArray): Double {
        val outputLayer = layers.last()
        var error = 0.0
        for (n in 0 until outputLayer.size - 1) {
            var delta = targets[n] - outputLayer[n].output
            error += delta * delta
        }
        error /= (outputLayer.size - 1)
        error = sqrt(error)

        error += lambdaW * sqrt(layers.sumByDouble {
            it.sumByDouble { it.connections.sumByDouble { it.weight * it.weight } }
        })

        return error
    }

    fun results() = DoubleArray(layers.last().size - 1, init = {
        layers.last()[it].output
    })

}