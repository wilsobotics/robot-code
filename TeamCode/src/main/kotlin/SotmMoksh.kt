import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt
import kotlin.math.tan

const val TARGET_HEIGHT = 0.85
const val SHOOTER_HEIGHT = 0
const val GRAVITY = 9.81
const val MAX_SHOOTER_SPEED = 10 // ms-1
const val LAUNCH_LATENCY = 0

data class Vector2D(
    var x: Double,
    var y: Double
)

object SotmMoksh {

    const val C4 = 0.25 * GRAVITY * GRAVITY
    const val C3 = 0

    class ShooterConfigParams {
        var targetRpm = 0.0;
        var targetTurretPos = 0.0;
    }

    fun calculateT0(
        A: Double,
        T: Double,
        dz: Double
    ): Double? {
        if (A * T*T <= dz*dz) return null

        val inner = dz*dz + A*T*T
        if (inner < 0.0) {
            return null
        }

        val innerRoot = sqrt(inner)
        val num = 2 * (dz + innerRoot)

        if (num <= 0.0) return null;

        return sqrt(num / GRAVITY)
    }

    fun calculateShooterParams(
        targetPos: Vector2D,
        shooterPos: Vector2D,
        pitch: Double,
        rv: Vector2D, // robot velocity
        result: ShooterConfigParams
    ): Boolean {
        val dz = TARGET_HEIGHT - SHOOTER_HEIGHT

        val predShooterPos = Vector2D(
            shooterPos.x + rv.x*LAUNCH_LATENCY,
            shooterPos.y + rv.y*LAUNCH_LATENCY
        )

        val d = Vector2D(
            targetPos.x - predShooterPos.x,
            targetPos.y - predShooterPos.y
        )

        val A = d.x*d.x + d.y*d.y
        val B = d.x*rv.x + d.y*rv.y
        val C = rv.x*rv.x + rv.y*rv.y
        val T = tan(pitch)

        val c2 = GRAVITY*dz - T*T * C
        val c1 = 2 * T*T * B
        val c0 = dz*dz - T*T * A

//        val t0 = sqrt((2*(dz + sqrt(dz*dz + A*T*T)))/GRAVITY)

        var t = calculateT0(A, T, dz) ?: return false

        for (_i in 1..5) {
            val f = C4*t*t*t*t + c2*t*t + c1*t + c0
            val fPrime = 4*C4*t*t*t + 2*c2*t + c1

            if (abs(f) < 1e-6) break

            if (abs(fPrime) < 1e-6) {
                return false
            }

            t -= f / fPrime
        }

        if (t <= 0.0 || t.isNaN() || t.isInfinite()) return false

        val vRelX = d.x / t - rv.x
        val vRelY = d.y / t - rv.y
        val yaw = atan2(vRelY, vRelX)

        val shooterSpeed = sqrt(vRelX * vRelX + vRelY*vRelY)

        if (shooterSpeed > MAX_SHOOTER_SPEED) return false

        result.targetRpm = calculateRpm(shooterSpeed)
        result.targetTurretPos = calculateTurretPos(yaw)
        return true
    }
}

fun calculateRpm(velocity: Double): Double {
    return velocity
}

fun calculateTurretPos(yaw: Double): Double {
    return yaw
}