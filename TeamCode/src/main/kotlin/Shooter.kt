import androidx.core.util.Consumer
import com.bylazar.configurables.annotations.Configurable
import com.bylazar.telemetry.PanelsTelemetry
import dev.nextftc.control.KineticState
import dev.nextftc.control.builder.controlSystem
import dev.nextftc.control.feedback.PIDCoefficients
import dev.nextftc.control.feedforward.BasicFeedforwardParameters
import dev.nextftc.core.commands.groups.SequentialGroup
import dev.nextftc.core.commands.utility.LambdaCommand
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
import dev.nextftc.hardware.controllable.MotorGroup
import dev.nextftc.hardware.controllable.RunToState
import dev.nextftc.hardware.controllable.RunToVelocity
import dev.nextftc.hardware.impl.CRServoEx
import dev.nextftc.hardware.impl.MotorEx
import dev.nextftc.hardware.impl.ServoEx
import dev.nextftc.hardware.powerable.SetPower
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.processNextEventInCurrentThread
import kotlin.math.IEEErem
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

object Shooter : Subsystem {
    @Configurable
    object ShooterConstants {
        @JvmField
        var FkP: Double = 0.01
        var FkV: Double = 0.001
        var FkS: Double = 0.15

        val flywheelFB = PIDCoefficients(FkP, 0.0, 0.0)
        val flywheelFF = BasicFeedforwardParameters(FkV, 0.0, FkS)

        var TkP: Double = 0.00019
        var TkI: Double = 0.0
        var TkD: Double = 0.000012
        val turretFB = PIDCoefficients(TkP, TkI, TkD)
        var TkV: Double = 0.0
        var TkS: Double = 0.3
        val turretFF = BasicFeedforwardParameters(TkV, 0.0, TkS)
    }
    private val evanResult = Evan.EvanResult()
    private val panelsTelemetry = PanelsTelemetry.telemetry
    val flywheel = MotorGroup(
        MotorEx("flywheel").floatMode(),
        MotorEx("flywheel2").reversed().floatMode()
    )
    val turret = CRServoEx("turret1").reversed()
    val turret2 = CRServoEx("turret2").reversed()
    val hood = ServoEx("hood")
    val turretEncoder = MotorEx("rear_left").zeroed()
    private val flywheelController = controlSystem {
        velPid(ShooterConstants.flywheelFB)
        basicFF(ShooterConstants.flywheelFF)
    }
    private val turretController = controlSystem {
        posPid(ShooterConstants.turretFB)
        basicFF(ShooterConstants.turretFF)
    }

    val preShoot = LambdaCommand ()
        .setStart {
            val flywheelPower = flywheelController.calculate(KineticState(0.0, flywheel.velocity))
            flywheel.power = Math.max(Math.min(flywheelPower, 1.0), -1.0)
        }

    val restFlywheel = LambdaCommand ()
        .setStart {
            flywheel.power = 0.0
        }

    val powerTurret = LambdaCommand ()
        .setStart {
            val turretPower = turretController.calculate(turretEncoder.state)
            turret.power = turretPower
            turret2.power = turretPower
        }

    fun canShoot(): Boolean {
        return abs(flywheel.state.velocity - evanResult.targetRPM) < 50
                && abs(Math.toDegrees(turretEncoder.state.position - evanResult.targetTurretPos)) < 2
                && (
                follower.pose.y > follower.pose.x && follower.pose.y > -follower.pose.x
                        || follower.pose.y > follower.pose.x - 48 && follower.pose.y > -follower.pose.x + 96
                )
    }

    override fun initialize() {
    }

    override fun periodic() {
        Evan.calculateEvan(
            KtConstants.GOAL_HEIGHT*0.0254,
            KtConstants.GOAL_X*0.0254,
            KtConstants.GOAL_Y*0.0254,
            KtConstants.SHOOTER_HEIGHT*0.0254,
            follower.pose.x*0.0254,
            follower.pose.y*0.0254,
            Math.toRadians(57.0),
            follower.velocity.yComponent*0.0254,
            follower.velocity.xComponent*0.0254,
            evanResult
        )


        val robotHeadingTicks = Evan.yawToPos(follower.heading.IEEErem(2 * PI))
        turretController.goal = KineticState(
            Math.min(
                Math.max(
                    evanResult.targetTurretPos - robotHeadingTicks,
                    -8000.0
                ), 8000.0
            )
        )
        val deltaY = KtConstants.GOAL_Y - follower.pose.y
        val deltaX = KtConstants.GOAL_X - follower.pose.x
        val distance = atan2(deltaY, deltaX)
//        if (distance < 75 ) {
//            flywheelController.goal = KineticState(0.0, 1200.0)
//        }
//        else if (distance < 120 ) {
//            flywheelController.goal = KineticState(0.0, 1300.0)
//        }
//        else if (distance < 140 ) {
//            flywheelController.goal = KineticState(0.0, 1400.0)
//        }
        flywheelController.goal = KineticState(0.0, 1100.0 + distance*2.0)
    }
}