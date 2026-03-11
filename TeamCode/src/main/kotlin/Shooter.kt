import dev.nextftc.control.KineticState
import dev.nextftc.control.builder.controlSystem
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
import dev.nextftc.hardware.controllable.MotorGroup
import dev.nextftc.hardware.impl.CRServoEx
import dev.nextftc.hardware.impl.MotorEx
import dev.nextftc.hardware.impl.ServoEx
import dev.nextftc.hardware.positionable.SetPosition
import kotlin.math.abs

object Shooter : Subsystem {
    private val evanResult = Evan.EvanResult()
    val flywheel = MotorGroup(
        MotorEx("flywheel").floatMode(),
        MotorEx("flywheel2").reversed().floatMode()
    )
    val turret = CRServoEx("turret1")
    val turret2 = CRServoEx("turret2")
    val hood = ServoEx("hood")
    val turretEncoder = MotorEx("turretEncoder")
    private val flywheelController = controlSystem {
        posPid(0.019, 0.0, 0.002)
        basicFF(0.003, 0.08, 0.0)
    }
    private val turretController = controlSystem {
        posPid(0.019, 0.0, 0.002)
        basicFF(0.003, 0.08, 0.0)
    }

    fun flywheelVelocity(velocity: Double) {
        flywheelController.goal = KineticState(0.0, velocity, 0.0)
    }

    fun turretAngle(angle: Double) {
        turretController.goal = KineticState(angle, 0.0, 0.0)
    }

    fun hoodAngle(angle: Double) {
        SetPosition(hood, angle)
    }

    fun pre_shoot() {
        flywheelVelocity(evanResult.targetRPM)
        flywheel.power = flywheelController.calculate(flywheel.state)
    }

    val can_shoot =
                abs(flywheel.state.velocity - evanResult.targetRPM) < 50
                && abs(Math.toDegrees(turretEncoder.state.position - evanResult.targetTurretPos)) < 2
                && (
                    follower.pose.y > follower.pose.x && follower.pose.y > -follower.pose.x
                    || follower.pose.y > follower.pose.x - 48 && follower.pose.y > -follower.pose.x + 96
                )


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
        hoodAngle(40.0)
        turretAngle(evanResult.targetTurretPos)
        turret.power = turretController.calculate(turretEncoder.state)
    }
}