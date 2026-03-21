import androidx.core.util.Consumer
import com.bylazar.configurables.annotations.Configurable
import com.bylazar.telemetry.PanelsTelemetry
import dev.nextftc.control.KineticState
import dev.nextftc.control.builder.controlSystem
import dev.nextftc.control.feedback.PIDCoefficients
import dev.nextftc.control.feedforward.BasicFeedforwardParameters
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
import kotlin.math.abs

object Shooter : Subsystem {
    @Configurable
    object ShooterConstants {
        @JvmField
        var FkP: Double = 0.0
        var FkV: Double = 0.0
        var FkS: Double = 0.15
        val flywheelFB = PIDCoefficients(FkP, 0.0, 0.0)
        val flywheelFF = BasicFeedforwardParameters(FkV, 0.0, FkS)
    }
    private val evanResult = Evan.EvanResult()
    private val panelsTelemetry = PanelsTelemetry.telemetry
    val flywheel = MotorGroup(
        MotorEx("flywheel").floatMode(),
        MotorEx("flywheel2").reversed().floatMode()
    )
    val turret = CRServoEx("turret1")
    val turret2 = CRServoEx("turret2")
    val hood = ServoEx("hood")
    val turretEncoder = MotorEx("rear_left")
    private val flywheelController = controlSystem {
        velPid(ShooterConstants.flywheelFB)
        basicFF(ShooterConstants.flywheelFF)
    }
    private val turretController = controlSystem {
        posPid(0.019, 0.0, 0.002)
    }

    val preShoot = SetPower(flywheel, 1.0)

    val restFlywheel = SetPower(flywheel, 0.0)

    fun canShoot(): Boolean {
        return abs(flywheel.state.velocity - evanResult.targetRPM) < 50
                && abs(Math.toDegrees(turretEncoder.state.position - evanResult.targetTurretPos)) < 2
                && (
                follower.pose.y > follower.pose.x && follower.pose.y > -follower.pose.x
                        || follower.pose.y > follower.pose.x - 48 && follower.pose.y > -follower.pose.x + 96
                )
    }

    val highVTest = LambdaCommand ()
        . setStart{
            flywheelController.goal = KineticState(0.0, 1000.0)
        }

    val lowVTest = LambdaCommand ()
        . setStart{
            flywheelController.goal = KineticState(0.0, 500.0)
        }

    override fun initialize() {
    }

    override fun periodic() {
        Evan.calculateEvan(
            KtConstants.GOAL_HEIGHT,
            KtConstants.GOAL_X,
            KtConstants.GOAL_Y,
            KtConstants.GOAL_HEIGHT,
            follower.pose.x,
            follower.pose.y,
            Math.toRadians(40.0),
            follower.velocity.yComponent,
            follower.velocity.xComponent,
            evanResult
        )
        turretController.goal = KineticState(evanResult.targetTurretPos, 0.0, 0.0)
        val flywheelPower = flywheelController.calculate(KineticState(0.0, flywheel.velocity))
        flywheel.power = Math.min(Math.max(flywheelPower, -1.0), 1.0)
        panelsTelemetry.addLine("flywheel velocity${flywheel.state} \n target:${flywheelController.goal}  \n power: ${flywheelPower}")
        panelsTelemetry.update()
//        flywheelController.goal = KineticState(0.0, evanResult.targetRPM, 0.0)


//        SetPower(turret, turretController.calculate(turretEncoder.state))
//        SetPower(turret2, turretController.calculate(turretEncoder.state))
    }
}