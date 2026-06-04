package subsystems

import KtConstants
import Panels
import com.skeletonarmy.marrow.zones.Point
import com.skeletonarmy.marrow.zones.PolygonZone
import dev.nextftc.control.KineticState
import dev.nextftc.control.builder.controlSystem
import dev.nextftc.control.feedback.PIDCoefficients
import dev.nextftc.control.feedforward.BasicFeedforwardParameters
import dev.nextftc.core.commands.utility.LambdaCommand
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
import dev.nextftc.hardware.controllable.MotorGroup
import dev.nextftc.hardware.impl.MotorEx
import dev.nextftc.hardware.impl.ServoEx
import sotm.Evan
import kotlin.math.IEEErem
import kotlin.math.abs

object Shooter : Subsystem {
    val robotZone = PolygonZone(14.0-KtConstants.MARROW_THRESHOLD, 17.5-KtConstants.MARROW_THRESHOLD)
    val closeLaunchZone: PolygonZone =
        PolygonZone(Point(144.0, 144.0), Point(72.0, 72.0), Point(0.0, 144.0))
    val farLaunchZone: PolygonZone = PolygonZone(Point(48.0, 0.0), Point(72.0, 24.0), Point(96.0, 0.0))
    val evanResult = Evan.EvanResult()
    var zeroTurret = false
    var turretEnabled = false
    val flywheel = MotorGroup(
        MotorEx("shooterB").floatMode(),
        MotorEx("shooterA").reversed().floatMode()
    )
    val turret = MotorEx("turret").floatMode()
    val hood = ServoEx("hood")
    var hood_angle = 50.0
    val turretOffset = KtConstants.TURRET_POS
    val flywheelController = controlSystem {
        velPid(PIDCoefficients(KtConstants.FLYWHEEL_KP, KtConstants.FLYWHEEL_KI, KtConstants.FLYWHEEL_KD))
        basicFF(BasicFeedforwardParameters(KtConstants.FLYWHEEL_KV, KtConstants.FLYWHEEL_KA, KtConstants.FLYWHEEL_KS))
    }
    val turretController = controlSystem {
        posPid(PIDCoefficients(KtConstants.TURRET_KP, KtConstants.TURRET_KI, KtConstants.TURRET_KD))
        basicFF(BasicFeedforwardParameters(KtConstants.TURRET_KV, KtConstants.TURRET_KA, KtConstants.TURRET_KS))
    }

    var powerFlywheel = false
    val turnFlywheelOn = LambdaCommand()
        .setStart {
            powerFlywheel = true
        }
    val turnFlywheelOff = LambdaCommand()
        .setStart {
            powerFlywheel = false
        }



    fun canShoot(): Boolean {
        return abs(flywheel.state.velocity - flywheelController.goal.velocity) < KtConstants.FLYWHEEL_TOLERANCE
                && (
                abs(turret.currentPosition - turretController.goal.position) < KtConstants.TURRET_TOLERANCE
                        && abs(turretController.goal.position) != KtConstants.TURRET_ENCODER_LIMIT
                )
                && (robotZone.isInside(closeLaunchZone) || robotZone.isInside(farLaunchZone))
    }

    override fun initialize() {
        turretEnabled = true
        turretController.reset()
//        turretController.goal = KineticState(0.0)
//        if (KtConstants.TURRET_SWITCH_ENCODER == -1) {
//            KtConstants.TURRET_SWITCH_ENCODER = 1
//        } else {
//            KtConstants.TURRET_SWITCH_ENCODER = -1
//        }
    }

    override fun periodic() {
        robotZone.setPosition(follower.pose.x, follower.pose.y)
        robotZone.setRotation(follower.heading)
        if (turretEnabled) {
            Evan.calculateEvan(
                KtConstants.GOAL_HEIGHT * 0.0254,
                KtConstants.GOAL_X * 0.0254,
                KtConstants.GOAL_Y * 0.0254,
                KtConstants.SHOOTER_HEIGHT * 0.0254,
                follower.pose.x * 0.0254,
                follower.pose.y * 0.0254,
                Math.toRadians(hood_angle),
                follower.velocity.yComponent * 0.0254,
                follower.velocity.xComponent * 0.0254,
                evanResult
            )
            hood_angle = if (evanResult.targetHoodPos == KtConstants.HOOD_CLOSE_POS) {
                KtConstants.HOOD_CLOSE_ANGLE
            } else {
                KtConstants.HOOD_FAR_ANGLE
            }

            // 1. Get current robot heading in radians
            val currentHeadingRad = follower.heading

// 2. Calculate the target angle in radians (converting Evan's ticks back to rads)
// Assuming Evan.posToYaw exists, or use your TICKS_PER_DEGREE constant:
            val ticksPerRadian = (KtConstants.TICKS_PER_DEGREE * 180.0 / Math.PI)
            val targetAngleRad = evanResult.targetTurretPos / ticksPerRadian

// 3. Find the SHORTEST angular distance (this prevents the 360-degree flip)
            val shortestErrorRad = (targetAngleRad - currentHeadingRad).IEEErem(2 * Math.PI)

            if (!zeroTurret) {
                // 4. Convert that clean error back into ticks for your PID controller
                val targetTicks = (shortestErrorRad * ticksPerRadian).coerceIn(
                    -KtConstants.TURRET_ENCODER_LIMIT,
                    KtConstants.TURRET_ENCODER_LIMIT
                )
                turretController.goal = KineticState(targetTicks)
            } else {
                turretController.goal = KineticState(0.0)
            }
            val turretPower = turretController.calculate(turret.state)
            turret.power = -turretPower * KtConstants.TURRET_SWITCH_ENCODER

            hood.position = evanResult.targetHoodPos

            if (powerFlywheel) {
                flywheelController.goal = KineticState(0.0, evanResult.targetRPM)
                val flywheelPower =
                    flywheelController.calculate(KineticState(0.0, flywheel.velocity))
                flywheel.power = -flywheelPower
            } else {
                flywheel.power = 0.0
            }
            Panels.telemetry()
        } else {
            turret.power = 0.0
            flywheel.power = 0.0
        }
    }
}