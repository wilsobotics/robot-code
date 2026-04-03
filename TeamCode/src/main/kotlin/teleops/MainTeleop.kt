package teleops

import subsystems.Shooter
import subsystems.Transfer
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.nextftc.bindings.BindingManager
import dev.nextftc.bindings.button
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.extensions.pedro.PedroDriverControlled
import dev.nextftc.ftc.Gamepads
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import org.firstinspires.ftc.teamcode.pedroPathing.Constants

@TeleOp(name = "NextFTC TeleOp")
class MainTeleop : NextFTCOpMode() {
    init {
        addComponents(
            SubsystemComponent(Shooter, Transfer),
            BulkReadComponent,
            BindingsComponent,
            PedroComponent(Constants::createFollower)
        )
    }

    override fun onStartButtonPressed() {
        val saved = RobotStorage.loadState()
        PedroComponent.Companion.follower.setStartingPose(Pose(saved.x, saved.y, saved.heading))
        val isRed = saved.side == "RED"
        if (isRed) {
            KtConstants.GOAL_X = 140.0
            KtConstants.GOAL_Y = 140.0
        } else {
            KtConstants.GOAL_X = 0.0
            KtConstants.GOAL_Y = 140.0
        }

        val driverControlled = PedroDriverControlled(
            Gamepads.gamepad1.leftStickY,
            Gamepads.gamepad1.leftStickX,
            Gamepads.gamepad1.rightStickX,
            false
        )
        driverControlled()
        BindingManager.layer = "intake"

        Shooter.isEnabled = true
        Transfer.rest()

        button { BindingManager.layer == "pre_shoot" }
            .whenBecomesTrue {
                Transfer.preShoot()
                Shooter.turnFlywheelOn()
            }

        button { BindingManager.layer == "intake" }
            .whenBecomesTrue {
                Transfer.intake()
                Shooter.turnFlywheelOff()
            }

        button { gamepad1.a }
            .inLayer("pre_shoot")
            .whenBecomesTrue { BindingManager.layer = "intake" }
            .inLayer("intake")
            .whenBecomesTrue { BindingManager.layer = "pre_shoot" }

        button { gamepad1.b && Shooter.canShoot() }
            .whenBecomesTrue { Transfer.shoot() }
    }

    override fun onUpdate() {
        super.onUpdate()
        BindingManager.update()
    }

    override fun onStop() {
    }
 }