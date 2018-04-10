import golem.end
import golem.mat
import golem.matrix.Matrix

class CatmulRomSpline(knots: Knots) : Spline(knots) {

    override val basis: Matrix<Double> = basisImpl
    companion object {
        private val basisImpl = mat[
                -1, 3, -3, 1 end
                        2, -5, 4, -1 end
                        -1, 0, 1, 0 end
                        0, 2, 0, 0
        ] * 0.5
    }


}