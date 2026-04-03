package subsystems

import sotm.Evan
import com.bylazar.telemetry.PanelsTelemetry
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
import subsystems.Transfer.backRollers
import subsystems.Transfer.frontRollers
import kotlin.math.IEEErem
import kotlin.math.PI
import kotlin.math.abs

object Shooter : Subsystem {
    private val evanResult = Evan.EvanResult()
    private val panelsTelemetry = PanelsTelemetry.telemetry
    var isEnabled = false
    val flywheel = MotorGroup(
        MotorEx("flywheel").floatMode(),
        MotorEx("flywheel2").reversed().floatMode()
    )
    val turret = CRServoEx("turret1").reversed()
    val turret2 = CRServoEx("turret2").reversed()
    val hood = ServoEx("hood")
    var hood_angle = 40.0
    val turretEncoder = MotorEx("rear_left").zeroed()
    private val flywheelController = controlSystem {
        velPid(PIDCoefficients(KtConstants.FLYWHEEL_KP, KtConstants.FLYWHEEL_KI, KtConstants.FLYWHEEL_KD))
        basicFF(BasicFeedforwardParameters(KtConstants.FLYWHEEL_KV, KtConstants.FLYWHEEL_KA, KtConstants.FLYWHEEL_KS))
    }
    private val turretController = controlSystem {
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
        return abs(flywheel.state.velocity - flywheelController.goal.velocity) < KtConstants.FLYWHEEL_THRESHOLD
                && (
                abs(turretEncoder.state.position - turretController.goal.position) < KtConstants.TURRET_THRESHOLD
                        && abs(turretController.goal.position) != KtConstants.TURRET_ENCODER_LIMIT
                )
                && (
                follower.pose.y > follower.pose.x && follower.pose.y > -follower.pose.x
                        || follower.pose.y > follower.pose.x - 48 && follower.pose.y > -follower.pose.x + 96
                )
    }

    override fun initialize() {

    }

    override fun periodic() {
        if (isEnabled) {
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
            hood_angle = if (evanResult.targetHoodPos == KtConstants.HOOD_CLOSE) {
                57.0
            } else {
                40.0
            }

            val robotHeadingTicks = Evan.yawToPos(follower.heading.IEEErem(2 * PI))
            turretController.goal = KineticState(
                (evanResult.targetTurretPos - robotHeadingTicks).coerceIn(
                    -KtConstants.TURRET_ENCODER_LIMIT,
                    KtConstants.TURRET_ENCODER_LIMIT
                )
            )
            val turretPower = turretController.calculate(turretEncoder.state)
            turret.power = turretPower
            turret2.power = turretPower

            hood.position = evanResult.targetHoodPos

            if (powerFlywheel) {
                val flywheelPower =
                    flywheelController.calculate(KineticState(0.0, flywheel.velocity))
                flywheel.power = flywheelPower
            } else {
                flywheel.power = 0.0
            }
        }
    }
}