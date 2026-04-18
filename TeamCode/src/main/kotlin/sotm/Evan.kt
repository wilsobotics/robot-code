package sotm

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

object Evan {

    data class VelocityVector(val x: Double, val y: Double)
    const val GRAVITY = 9.81
    class EvanResult (
        var targetRPM: Double = 0.0,
        var targetTurretPos: Double = 0.0,
        var targetHoodPos: Double = 0.0
    )

    fun calculateEvan (
        targetHeight: Double, targetX: Double, targetY: Double,
        shooterHeight: Double, shooterX: Double, shooterY: Double,
        shooterAngle: Double,
        robotYV: Double, robotXV: Double,
        result: EvanResult
    ) {
        val deltaY = targetY - shooterY
        val deltaX = targetX - shooterX
        val deltaH = targetHeight - shooterHeight
        val distance = sqrt(deltaY * deltaY + deltaX * deltaX)

        // 1. Calculate the TOTAL initial launch velocity (v0)
        // This is the speed required if the robot were stationary.
        val cosAngle = cos(shooterAngle)
        val denominator = 2 * cosAngle.pow(2) * (distance * tan(shooterAngle) - deltaH)

        val ballSpeed = sqrt((distance * distance * GRAVITY) / denominator)

        // 2. Project onto Field-Relative components
        // We separate the 3D velocity into a horizontal plane (ground) and a vertical component.
        val horizontalSpeedField = ballSpeed * cos(shooterAngle)
        val verticalSpeedField = ballSpeed * sin(shooterAngle)

        // Use atan2 to find the direction from the robot to the target
        val ballYawField = atan2(deltaY, deltaX)
        val ballVXField = horizontalSpeedField * cos(ballYawField)
        val ballVYField = horizontalSpeedField * sin(ballYawField)

        // 3. Compensate for Robot Velocity
        // To hit the target, the shooter must provide the difference between
        // the required field velocity and the robot's current movement.
        val shooterVX = ballVXField - robotXV
        val shooterVY = ballVYField - robotYV

        // 4. Final Recomposition
        // Find the new horizontal speed magnitude after robot velocity compensation
        val horizontalShooterSpeed = sqrt(shooterVX * shooterVX + shooterVY * shooterVY)

        // Total flywheel speed: combine horizontal compensation with the fixed vertical speed
        val velocity = sqrt(horizontalShooterSpeed.pow(2) + verticalSpeedField.pow(2))

        // New turret yaw (angle to point the shooter relative to the field)
        val yaw = atan2(shooterVY, shooterVX)

        val rpm = velocityToRpm(velocity)
        val turretPos = yawToPos(yaw)
        result.targetRPM = rpm
        result.targetTurretPos = turretPos
        if (distance < 2.8) {
            result.targetHoodPos = KtConstants.HOOD_CLOSE_POS
        } else {
            result.targetHoodPos = KtConstants.HOOD_FAR_POS
            result.targetRPM = 1450.0
        }
    }

    fun velocityToRpm(velocity: Double): Double {

        return (velocity)*187+75.32
    }

    fun yawToPos(yaw: Double): Double {
        return Math.toDegrees(yaw)*KtConstants.TICKS_PER_DEGREE
    }

    fun posToYaw(pos: Double): Double {
        return Math.toRadians(pos/KtConstants.TICKS_PER_DEGREE)
    }

}