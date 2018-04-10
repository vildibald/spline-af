typealias Connections = Array<Connection>

data class Connection(val weight: Double, val delta: Double) {
    companion object {
        fun random() = Connection(Math.random(), 0.0)
    }
}