package subsystems

import sotm.Evan
import dev.nextftc.control.KineticState
import dev.nextftc.control.builder.controlSystem
import dev.nextftc.control.feedback.PIDCoefficients
import dev.nextftc.control.feedforward.BasicFeedforwardParameters
import dev.nextftc.core.commands.utility.LambdaCommand
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
import dev.nextftc.hardware.controllable.MotorGroup
import dev.nextftc.hardware.impl.CRServoEx
import dev.nextftc.hardware.impl.MotorEx
import dev.nextftc.hardware.impl.ServoEx
import kotlin.math.IEEErem
import kotlin.math.PI
import kotlin.math.abs

object Shooter : Subsystem {
    val evanResult = Evan.EvanResult()
    var turretEnabled = false
    val flywheel = MotorGroup(
        MotorEx("flywheel").floatMode(),
        MotorEx("flywheel2").reversed().floatMode()
    )
    val turret = CRServoEx("turret1").reversed()
    val turret2 = CRServoEx("turret2").reversed()
    val hood = ServoEx("hood")
    var hood_angle = 50.0
    val turretEncoder = MotorEx("rear_left")
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
                abs(turretEncoder.currentPosition - turretController.goal.position) < KtConstants.TURRET_TOLERANCE
                        && abs(turretController.goal.position) != KtConstants.TURRET_ENCODER_LIMIT
                )
                && (
                follower.pose.y > follower.pose.x - KtConstants.ROBOT_RADIUS && follower.pose.y > -follower.pose.x + 144 - KtConstants.ROBOT_RADIUS
                        || follower.pose.y < follower.pose.x - 48 + KtConstants.ROBOT_RADIUS && follower.pose.y < -follower.pose.x + 96 + KtConstants.ROBOT_RADIUS
                )
    }

    override fun initialize() {
        turretEnabled = false
        turretController.reset()
//        turretController.goal = KineticState(0.0)
//        if (KtConstants.TURRET_SWITCH_ENCODER == -1) {
//            KtConstants.TURRET_SWITCH_ENCODER = 1
//        } else {
//            KtConstants.TURRET_SWITCH_ENCODER = -1
//        }
    }

    override fun periodic() {
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

            val robotHeadingTicks = Evan.yawToPos(follower.heading.IEEErem(2 * PI))
            turretController.goal = KineticState(
                (evanResult.targetTurretPos - robotHeadingTicks).coerceIn(
                    -KtConstants.TURRET_ENCODER_LIMIT,
                    KtConstants.TURRET_ENCODER_LIMIT
                )
            )
            val turretPower = turretController.calculate(turretEncoder.state)
            turret.power = -turretPower * KtConstants.TURRET_SWITCH_ENCODER
            turret2.power = -turretPower * KtConstants.TURRET_SWITCH_ENCODER

            hood.position = evanResult.targetHoodPos

            if (powerFlywheel) {
                flywheelController.goal = KineticState(0.0, evanResult.targetRPM)
                val flywheelPower =
                    flywheelController.calculate(KineticState(0.0, flywheel.velocity))
                flywheel.power = flywheelPower
            } else {
                flywheel.power = 0.0
            }
            Panels.telemetry()
        } else {
            turret.power = 0.0
            turret2.power = 0.0
            flywheel.power = 0.0
        }
    }
}