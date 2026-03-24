import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.nextftc.bindings.BindingManager
import dev.nextftc.bindings.button
import dev.nextftc.core.commands.delays.Delay
import dev.nextftc.core.commands.groups.SequentialGroup
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.ftc.Gamepads
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
import dev.nextftc.extensions.pedro.PedroDriverControlled
import org.firstinspires.ftc.teamcode.pedroPathing.Constants


@TeleOp(name = "NextFTC TeleOp")
class RedTeleop : NextFTCOpMode() {
    init {
        addComponents(
            SubsystemComponent(Shooter, Transfer),
            BulkReadComponent,
            BindingsComponent,
            PedroComponent ( Constants::createFollower )
        )
    }

    override fun onStartButtonPressed() {
        KtConstants.GOAL_X = 170.0
        KtConstants.GOAL_Y = 140.0
        follower.setStartingPose(Pose(79.5, 7.5))
        val driverControlled = PedroDriverControlled(
            Gamepads.gamepad1.leftStickY,
            Gamepads.gamepad1.leftStickX,
            Gamepads.gamepad1.rightStickX,
            false
        )

        driverControlled()
        BindingManager.layer = "intake"


        Shooter.flywheel.power = 0.0

        button { BindingManager.layer == "pre_shoot"}
            .whenBecomesTrue { Transfer.rest() }
            .whenTrue {
                Shooter.preShoot()
                Shooter.powerTurret()
            }

        button { BindingManager.layer == "intake"}
            .whenBecomesTrue {
                Transfer.intake()
                Shooter.restFlywheel()
            }
            .whenTrue {
                Shooter.powerTurret()
            }



//        button {Shooter.canShoot() }
//            .whenBecomesTrue { Transfer.shoot() }
//            .whenBecomesFalse {
//                Transfer.initialize()
//                BindingManager.layer = "intake"
//            }

        button { gamepad1.a }
            .inLayer("pre_shoot")
            .whenBecomesTrue { BindingManager.layer = "intake" }
            .inLayer("intake")
            .whenBecomesTrue { BindingManager.layer = "pre_shoot" }

        button { gamepad1.b }
            .whenBecomesTrue {
                val shoot = SequentialGroup(
                    Transfer.shoot,
                    Delay(1.0),
                    Transfer.rest,
                )
                shoot()
                BindingManager.layer = "pre_shoot"
            }
    }

    override fun onUpdate() {
        super.onUpdate()
        BindingManager.update()
    }

    override fun onStop() {
    }
 }