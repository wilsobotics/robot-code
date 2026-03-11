import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

object Evan {

    const val GRAVITY = 9.81
    const val TARGET_HEIGHT = 0.85

    data class VelocityVector(val x: Double, val y: Double)
    class EvanResult (
        var targetRPM: Double = 0.0,
        var targetTurretPos: Double = 0.0
    )

    fun calculateEvan (
        targetHeight: Double, targetX: Double, targetY: Double,
        shooterHeight: Double, shooterX: Double, shooterY: Double,
        angle: Double,
        robotYV: Double, robotXV: Double,
        result: EvanResult
    ) {
        val deltaY = targetY - shooterY
        val deltaX = targetX - shooterX
        val distance = sqrt(deltaY*deltaY + deltaX*deltaX)
        val ballSpeed = sqrt((distance*distance * GRAVITY) / (2 * cos(angle)*cos(angle) * (distance * tan(angle) - TARGET_HEIGHT)))
        val ballYaw = atan2(deltaY, deltaX)
        val ballVelocityVector = VelocityVector(ballSpeed * cos(ballYaw), ballSpeed * sin(ballYaw))
        val robotVelocityVector = VelocityVector(robotXV, robotYV)
        val finalVector = VelocityVector(ballVelocityVector.x - robotVelocityVector.x, ballVelocityVector.y - robotVelocityVector.y)
        val velocity = sqrt(finalVector.x*finalVector.x + finalVector.y*finalVector.y)
        val yaw = atan2(finalVector.y, finalVector.x)

        val rpm = velocityToRpm(velocity)
        val turretPos = yawToPos(yaw)
        result.targetRPM = rpm
        result.targetTurretPos = turretPos
    }

    fun velocityToRpm(velocity: Double): Double {
        return velocity
    }

    fun yawToPos(yaw: Double): Double {
        return yaw
    }

}