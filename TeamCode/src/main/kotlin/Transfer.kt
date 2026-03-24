import dev.nextftc.core.commands.delays.Delay
import dev.nextftc.core.commands.groups.SequentialGroup
import dev.nextftc.core.commands.utility.LambdaCommand
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.hardware.impl.MotorEx
import dev.nextftc.hardware.impl.ServoEx
import dev.nextftc.hardware.positionable.SetPosition
import dev.nextftc.hardware.powerable.SetPower

object Transfer : Subsystem {
    val frontRollers = MotorEx("intake")
    val backRollers = MotorEx("backRollers")
    val door = ServoEx("door")

    val intake = LambdaCommand()
        .setStart {
            frontRollers.power = KtConstants.INTAKE_ACTIVE_POWER
           backRollers.power = -0.8
        }

    val shoot = LambdaCommand ()
        .setStart {
            frontRollers.power = KtConstants.INTAKE_ACTIVE_POWER
            backRollers.power = KtConstants.TRANSFER_ACTIVE_POWER
        }



    val rest = LambdaCommand ()
        .setStart {
            frontRollers.power = KtConstants.INTAKE_RESTING_POWER
            backRollers.power = KtConstants.TRANSFER_RESTING_POWER
        }

    override fun initialize() {
        rest()
    }
}