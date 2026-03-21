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
    val backRollers = MotorEx("backRollers").reversed()
    val door = ServoEx("door")

    val intake = SequentialGroup(
        SetPower(frontRollers, KtConstants.INTAKE_ACTIVE_POWER),
        SetPower(backRollers, 0.0),
        SetPosition(door, KtConstants.DOOR_CLOSE)
    ) .setInterruptible(true)
        .requires(this)

    val shoot = SequentialGroup(
        SetPower(frontRollers, KtConstants.INTAKE_ACTIVE_POWER),
        SetPower(backRollers, -KtConstants.TRANSFER_ACTIVE_POWER),
        SetPosition(door, KtConstants.DOOR_OPEN),
    )
        .requires(this)
        .setInterruptible(false)

    val rest = SequentialGroup(
        SetPower(frontRollers, KtConstants.INTAKE_RESTING_POWER),
        SetPower(backRollers, KtConstants.TRANSFER_RESTING_POWER),
        SetPosition(door, KtConstants.DOOR_CLOSE)
    )

    override fun initialize() {
        rest()
    }
}