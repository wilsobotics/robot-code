import dev.nextftc.core.commands.delays.Delay
import dev.nextftc.core.commands.groups.SequentialGroup
import dev.nextftc.core.commands.utility.LambdaCommand
import kotlin.time.Duration.Companion.milliseconds

object Shoot {
    val shoot = LambdaCommand()
        .setStart {
            SequentialGroup(Door.open, Delay(300.milliseconds), Door.close)
        }
        .requires(Door)
        .setInterruptible(false)
}