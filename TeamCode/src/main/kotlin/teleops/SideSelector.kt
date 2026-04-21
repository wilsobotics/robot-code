package teleops

import com.pedropathing.geometry.Pose
import dev.nextftc.bindings.button
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.nextftc.bindings.BindingManager
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent

@TeleOp(name = "SideSelector")
class SideSelector : NextFTCOpMode() {
    init {
        addComponents(
            BulkReadComponent,
            BindingsComponent,
        )
    }

    override fun onStartButtonPressed() {
        button {gamepad1.right_bumper}
            .whenBecomesTrue {
                KtConstants.SIDE = "RED"
                KtConstants.GOAL_X = 147.0
                KtConstants.GOAL_Y = 140.0
                telemetry.addData("Side:", "RED")
                KtConstants.RESET_POS = Pose(108.0, 136.0, Math.toRadians(0.0))
                telemetry.update()
            }
        button {gamepad1.left_bumper}
            .whenBecomesTrue {
                KtConstants.SIDE = "BLUE"
                KtConstants.GOAL_X = -3.0
                KtConstants.GOAL_Y = 140.0
                KtConstants.RESET_POS = Pose(36.0, 136.0, Math.toRadians(180.0))
                telemetry.addData("Side:", "BLUE")
                telemetry.update()
            }
    }

    override fun onUpdate() {
        super.onUpdate()
        BindingManager.update()
    }
}