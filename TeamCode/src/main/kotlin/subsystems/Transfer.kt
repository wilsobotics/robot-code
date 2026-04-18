package subsystems

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

    val pusher = ServoEx("door")

    val intake = LambdaCommand()
        .setStart {
            frontRollers.power = KtConstants.INTAKE_ACTIVE_POWER
            backRollers.power = KtConstants.TRANSFER_BLOCKING_POWER
        }

    val preShoot = LambdaCommand()
        .setStart {
            frontRollers.power = KtConstants.INTAKE_RESTING_POWER
            backRollers.power = KtConstants.TRANSFER_BLOCKING_POWER
        }

    val rest = LambdaCommand()
        .setStart {
            frontRollers.power = 0.0
            backRollers.power = 0.0
            pusher.position = KtConstants.PUSHER_REST
        }

    val shoot = LambdaCommand()
        .setStart {
            frontRollers.power = KtConstants.INTAKE_ACTIVE_POWER
            backRollers.power = KtConstants.TRANSFER_ACTIVE_POWER
        }

    val kickBall = SequentialGroup(
        SetPosition(pusher, KtConstants.PUSHER_PUSH),
        Delay(0.5),
        SetPosition(pusher, KtConstants.PUSHER_REST)

    )
        .requires(this)
        .setInterruptible(false)
}