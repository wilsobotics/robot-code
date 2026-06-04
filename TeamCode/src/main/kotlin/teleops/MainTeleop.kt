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
import sotm.SHOOTER_HEIGHT
import subsystems.Leds
import subsystems.Shooter
import subsystems.Shooter.closeLaunchZone
import subsystems.Shooter.farLaunchZone
import subsystems.Shooter.robotZone
import subsystems.Transfer

@TeleOp(name = "NextFTC TeleOp")
class MainTeleop : NextFTCOpMode() {
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
        var autoShoot = false
        Shooter.turnFlywheelOn()
        follower.setStartingPose(Pose(KtConstants.ROBOT_X, KtConstants.ROBOT_Y, KtConstants.ROBOT_HEADING))
        Shooter.turretEnabled = true
        if (KtConstants.SIDE == "RED") {
            val driverControlled = PedroDriverControlled(
                -Gamepads.gamepad1.leftStickY,
                -Gamepads.gamepad1.leftStickX,
                -Gamepads.gamepad1.rightStickX,
                false
            )
            driverControlled()
        } else {
            val driverControlled = PedroDriverControlled(
                Gamepads.gamepad1.leftStickY,
                Gamepads.gamepad1.leftStickX,
                -Gamepads.gamepad1.rightStickX,
                true
            )
            driverControlled()
        }
        BindingManager.layer = "intake"

        button { Shooter.canShoot() }
            .whenBecomesTrue { Leds.readyToShoot }
            .whenBecomesFalse { Leds.neutral }

        button { gamepad1.right_bumper }
            .whenBecomesTrue {
                Transfer.intake()
                Leds.intaking()
            }
            .whenBecomesFalse{
                Transfer.rest()
                Leds.neutral()
            }
        button { gamepad1.dpad_left }
            .whenBecomesTrue {
                Transfer.rest()
                Shooter.turnFlywheelOff()
                Leds.neutral()
            }


        button { gamepad1.right_trigger > 0.3 || (Shooter.canShoot() && autoShoot) }
            .whenBecomesTrue {
                Transfer.shoot()
                Leds.shooting()
            }
            .whenBecomesFalse {
                Transfer.rest()
                Leds.neutral()
            }

        button { gamepad1.left_trigger > 0.3 }
            .whenBecomesTrue {
                Transfer.intakeMotor.power = 1.0
            }
            .whenBecomesFalse {
                Transfer.rest()
                Leds.neutral()
            }


        button {gamepad1.dpad_down}
            .whenBecomesTrue {
                follower.pose = side(KtConstants.RESET_POS)
            }

        button {gamepad1.dpad_right}
            .whenBecomesTrue {
                if (autoShoot) {autoShoot = false} else {autoShoot = true}
            }
        button {gamepad1.cross}
            .whenBecomesTrue {
                if (Shooter.zeroTurret) {Shooter.zeroTurret = false} else {Shooter.zeroTurret = true}
            }     }

    override fun onUpdate() {
        super.onUpdate()
        BindingManager.update()
        telemetry.addData("Turret pos", Shooter.turret.currentPosition)
        telemetry.addData("turret target", Shooter.turretController.goal.position)
        telemetry.addData("turret power", Shooter.turret.power)
        telemetry.addData("flywheel velo", Shooter.flywheel.velocity)
        telemetry.addData("flywheel target", Shooter.flywheelController.goal.velocity)
        telemetry.addData("in launch zone", robotZone.isInside(closeLaunchZone) || robotZone.isInside(farLaunchZone))
        telemetry.update()
    }

    override fun onStop() {
    }
 }