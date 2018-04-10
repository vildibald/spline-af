class Knots(val from: Double, val to: Double, count: Int = 2, private val function: (Double) ->
Double) :
        List<Double> {

    val diff = (to - from) / (count - 1)

    val elements = DoubleArray(count, init = {
        function(from + it * diff)
    })


    override val size: Int
        get() = elements.size

    override fun contains(element: Double) = elements.contains(element)

    override fun containsAll(elements: Collection<Double>): Boolean {
        elements.forEach { if (contains(it)) return true }
        return false
    }

    override fun get(index: Int) = elements[index]

    override fun indexOf(element: Double) = elements.indexOf(element)


    override fun isEmpty() = elements.isEmpty()

    override fun iterator() = elements.iterator()

    override fun lastIndexOf(element: Double) = elements.lastIndexOf(element)

    override fun listIterator() = elements.asList().listIterator()

    override fun listIterator(index: Int) = elements.asList().listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int) = elements.asList().subList(fromIndex, toIndex)

    fun nearestLeftIndex(number: Double): Int {
        var index = 0
        while (from + index * diff < number) index++
        return index
    }

}