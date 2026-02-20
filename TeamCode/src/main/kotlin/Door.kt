import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.hardware.impl.ServoEx
import dev.nextftc.hardware.positionable.SetPosition


object Door : Subsystem {
    val servo =  ServoEx("door")

    val open = SetPosition(servo, 0.0).requires(this)

    val close = SetPosition(servo, 1.0).requires(this)
}