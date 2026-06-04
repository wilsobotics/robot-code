package teleops

import KtConstants
import KtConstants.Companion.side
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.nextftc.bindings.BindingManager
import dev.nextftc.bindings.button
import dev.nextftc.core.commands.utility.LambdaCommand
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
import dev.nextftc.extensions.pedro.PedroDriverControlled
import dev.nextftc.ftc.Gamepads
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import org.firstinspires.ftc.teamcode.Prism.PrismAnimations.Solid
import org.firstinspires.ftc.teamcode.kotlinleds
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import subsystems.Leds
import subsystems.Shooter
import subsystems.Transfer

@TeleOp(name = "awdas TeleOp")
class LEDTEST : NextFTCOpMode() {
    init {
        addComponents(
            SubsystemComponent(Shooter, Transfer, Leds),
            BulkReadComponent,
            BindingsComponent,
            PedroComponent(Constants::createFollower),
        )
    }

    // see if these methods work
    override fun onStartButtonPressed() {
        var autoShoot = true
       button {gamepad1.dpad_up} .whenBecomesTrue{
           Leds.intaking
       }
        button {gamepad1.dpad_down} .whenBecomesTrue{
            Leds.shooting
        }
        button {gamepad1.dpad_right} .whenBecomesTrue{
            Leds.neutral
        }
        button {gamepad1.dpad_left} .whenBecomesTrue{
            Leds.reverseIntaking
        }
    }

    override fun onUpdate() {
        super.onUpdate()
        BindingManager.update()
    }

    override fun onStop() {
    }
}