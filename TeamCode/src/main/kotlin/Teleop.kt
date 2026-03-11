import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.nextftc.bindings.BindingManager
import dev.nextftc.bindings.button
import dev.nextftc.core.commands.delays.Delay
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.ftc.Gamepads
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import dev.nextftc.hardware.impl.MotorEx
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.extensions.pedro.PedroDriverControlled
import org.firstinspires.ftc.teamcode.pedroPathing.Constants


@TeleOp(name = "NextFTC TeleOp")
class TeleOpProgram : NextFTCOpMode() {
    init {
        addComponents(
            SubsystemComponent(Shooter, Transfer),
            BulkReadComponent,
            BindingsComponent,
            PedroComponent ( Constants::createFollower )
        )
    }

    override fun onStartButtonPressed() {
        val driverControlled = PedroDriverControlled(
            Gamepads.gamepad1.leftStickY,
            Gamepads.gamepad1.leftStickX,
            Gamepads.gamepad1.rightStickX
        )

        driverControlled()
        BindingManager.layer = "intake"

        button { BindingManager.layer == "pre_shoot"}
            .whenBecomesTrue { Transfer.initialize() }
            .whenTrue { Shooter.pre_shoot() }

        button { BindingManager.layer == "intake"}
            .whenBecomesTrue {
                Shooter.flywheel.power = 0.0
                Transfer.intake
            }

        button {Shooter.can_shoot }
            .whenBecomesTrue { Transfer.shoot }
            .whenBecomesFalse {
                Transfer.initialize()
                BindingManager.layer = "intake"
            }

        button { gamepad1.a }
            .inLayer("pre_shoot")
            .whenBecomesTrue { BindingManager.layer = "intake" }
            .inLayer("intake")
            .whenBecomesTrue { BindingManager.layer = "pre_shoot" }

        button { gamepad1.b }
            .whenBecomesTrue {
                Transfer.shoot
                Delay(0.3)
                Transfer.initialize()
            }
    }

    override fun onUpdate() {
        super.onUpdate()
        BindingManager.update()
    }

    override fun onStop() {
        super.onStop()
        BindingManager.reset()
    }
 }