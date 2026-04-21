package subsystems

import dev.nextftc.core.commands.delays.Delay
import dev.nextftc.core.commands.groups.SequentialGroup
import dev.nextftc.core.commands.utility.LambdaCommand
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.hardware.impl.MotorEx
import dev.nextftc.hardware.impl.ServoEx
import dev.nextftc.hardware.positionable.SetPosition
import dev.nextftc.hardware.powerable.SetPower
import sotm.Evan

object Transfer : Subsystem {
    val frontRollers = MotorEx("intake")
    val backRollers = MotorEx("backRollers")

    val pusher = ServoEx("door")

    val intake = LambdaCommand()
        .setStart {
            frontRollers.power = KtConstants.INTAKE_ACTIVE_POWER
            backRollers.power = KtConstants.TRANSFER_BLOCKING_POWER
        }
        .requires(this)

    val preShoot = LambdaCommand()
        .setStart {
            frontRollers.power = KtConstants.INTAKE_RESTING_POWER
            backRollers.power = KtConstants.TRANSFER_BLOCKING_POWER
        }
        .requires(this)

    val rest = LambdaCommand()
        .setStart {
            frontRollers.power = 0.5
            backRollers.power = 0.0
        }
        .requires(this)

    val shoot = LambdaCommand()
        .setStart {
            frontRollers.power = KtConstants.INTAKE_ACTIVE_POWER
            backRollers.power = KtConstants.TRANSFER_ACTIVE_POWER
        }
        .requires(this)

    val kickBall = SequentialGroup(
        shoot,
        Delay(0.3),
        SetPosition(pusher, KtConstants.PUSHER_PUSH),
        Delay(0.5),
        preShoot,
        SetPosition(pusher, KtConstants.PUSHER_REST)
    )
        .requires(this)
        .setInterruptible(false)

//    val checkKickBall = LambdaCommand()
//        .setStart {
//            if (Shooter.flywheelController.goal.velocity > 1330) {
//                if (Shooter.flywheel.velocity > 1330) {
//                    val kick = SequentialGroup(
//                        shoot,
//                        Delay(0.5),
//                        kickBall,
//                        Delay(0.3),
//                        preShoot
//                    )
//                    kick()
//                }
//            } else {
//                kickBall()
//            }
//        }
//        .requires(this)
//        .setInterruptible(false)
//    val kickBall = SequentialGroup(
//        Delay(0.2),
//        SetPosition(pusher, KtConstants.PUSHER_PUSH),
//        rest,
//        Delay(0.5),
//        SetPosition(pusher, KtConstants.PUSHER_REST),
//        preShoot
//    )
//        .requires(this)
//        .setInterruptible(false)
}