import dev.nextftc.control.KineticState
import dev.nextftc.control.builder.controlSystem
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.ftc.ActiveOpMode
import dev.nextftc.hardware.controllable.MotorGroup
import dev.nextftc.hardware.impl.CRServoEx
import dev.nextftc.hardware.impl.MotorEx
import dev.nextftc.hardware.impl.ServoEx
import dev.nextftc.hardware.positionable.ServoGroup
import dev.nextftc.hardware.positionable.SetPosition

object Shooter : Subsystem {
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

    fun flywheelRPM(rpm: Double) {
        flywheelController.goal = KineticState(0.0, rpm, 0.0)
    }

    fun turretAngle(angle: Double) {
        turretController.goal = KineticState(angle, 0.0, 0.0)
    }

    fun hoodAngle(angle: Double) {
        SetPosition(hood, angle)
    }

    override fun initialize() {
        flywheelRPM(0.0)
        hoodAngle(37.0)
        turretAngle(0.0)
    }

    override fun periodic() {
        flywheel.power = flywheelController.calculate(flywheel.state)
        turret.power = turretController.calculate(turretEncoder.state)
    }
}