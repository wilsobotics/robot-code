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
        SetPower(frontRollers, 0.9),
        SetPower(backRollers, 0.3),
        SetPosition(door, 0.0)
    ) .setInterruptible(true)
        .requires(this)

    val shoot = SequentialGroup(
        SetPower(frontRollers, 0.9),
        SetPower(backRollers, 0.9),
        SetPosition(door, 1.0),
        Delay(0.3)
    )
        .requires(this)
        .setInterruptible(false)

    override fun initialize() {
        SetPower(frontRollers, 0.3)
        SetPower(backRollers, 0.3)
        SetPosition(door, 0.0)
    }
}