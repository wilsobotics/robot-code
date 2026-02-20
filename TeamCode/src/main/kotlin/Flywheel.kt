import dev.nextftc.control.KineticState
import dev.nextftc.control.builder.controlSystem
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.hardware.controllable.MotorGroup
import dev.nextftc.hardware.controllable.RunToVelocity
import dev.nextftc.hardware.impl.MotorEx

object Flywheel : Subsystem {
    val motorGroup = MotorGroup(
        MotorEx("motor1").floatMode(),
        MotorEx("motor2").reversed().floatMode()
    )
    private val controller = controlSystem {
        posPid(0.019, 0.0, 0.002)
        basicFF(0.003, 0.08, 0.0)
    }

    fun setRPM(rpm: Double) {
        controller.goal = KineticState(0.0, rpm, 0.0)
    }

    override fun periodic() {
        motorGroup.power = controller.calculate(motorGroup.state)
    }
}