package teleops

import KtConstants.Companion.side
import subsystems.Shooter
import subsystems.Transfer
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.nextftc.bindings.BindingManager
import dev.nextftc.bindings.button
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
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
        follower.setStartingPose(Pose(KtConstants.ROBOT_X, KtConstants.ROBOT_Y, KtConstants.ROBOT_HEADING))
        Shooter.turretEnabled = true
        Shooter.turretEncoder.currentPosition = KtConstants.TURRET_POS
        val driverControlled = PedroDriverControlled(
            -Gamepads.gamepad1.leftStickY,
            -Gamepads.gamepad1.leftStickX,
            -Gamepads.gamepad1.rightStickX,
            false
        )
        driverControlled()
        BindingManager.layer = "intake"

        Transfer.rest()

        button {gamepad1.dpad_up}
            .whenBecomesTrue {
                BindingManager.layer == "rest"
                Transfer.rest()
            }

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

        button { gamepad1.left_bumper}
            .inLayer("pre_shoot")
            .whenBecomesTrue { BindingManager.layer = "intake" }
            .inLayer("intake")
            .whenBecomesTrue { BindingManager.layer = "pre_shoot" }
            .inLayer("rest")
            .whenBecomesTrue { BindingManager.layer = "intake" }

        button { gamepad1.right_trigger > 0.3 || Shooter.canShoot() }
            .inLayer("pre_shoot")
            .whenBecomesTrue {
                Transfer.shoot()
            }
            .whenBecomesFalse {
                Transfer.preShoot()
            }

        button { gamepad1.left_trigger > 0.3 }
            .inLayer("intake")
            .whenBecomesTrue { Transfer.frontRollers.power = -1.0 }
            .whenBecomesFalse { Transfer.intake() }

        button { gamepad1.right_bumper }
            .inLayer("pre_shoot")
            .whenBecomesTrue { Transfer.kickBall()}

        button {gamepad1.a}
            .whenBecomesTrue {
                follower.pose = side(KtConstants.RESET_POS)
            }
    }

    override fun onUpdate() {
        super.onUpdate()
        BindingManager.update()
    }

    override fun onStop() {
    }
 }