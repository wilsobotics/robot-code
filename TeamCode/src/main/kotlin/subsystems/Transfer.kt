package subsystems

import dev.nextftc.core.commands.delays.Delay
import dev.nextftc.core.commands.groups.SequentialGroup
import dev.nextftc.core.commands.utility.LambdaCommand
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.hardware.impl.MotorEx
import dev.nextftc.hardware.impl.ServoEx
import dev.nextftc.hardware.positionable.SetPosition
import kotlin.math.abs

object Transfer : Subsystem {
    val intakeMotor = MotorEx("intake")
    val door = ServoEx("door")
    var threeBalls = false

    val intake = LambdaCommand()
        .setStart {
            intakeMotor.power = KtConstants.INTAKE_ACTIVE_POWER
            door.position = KtConstants.DOOR_CLOSE
        }
        .setUpdate {
            if (abs(intakeMotor.velocity) < 250) {
                threeBalls = true
            } else {
                threeBalls = false
            }
        }

    val rest = LambdaCommand()
        .setStart {
            intakeMotor.power = KtConstants.INTAKE_RESTING_POWER
            threeBalls = false
        }

    val shoot = LambdaCommand()
        .setStart {
            intakeMotor.power = KtConstants.INTAKE_ACTIVE_POWER
            door.position = KtConstants.DOOR_OPEN
        }
}