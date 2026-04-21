import com.bylazar.field.PanelsField
import com.bylazar.telemetry.PanelsTelemetry
import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
import sotm.Evan
import subsystems.Shooter.evanResult
import subsystems.Shooter.flywheel
import subsystems.Shooter.flywheelController
import subsystems.Shooter.turret
import subsystems.Shooter.turretController
import subsystems.Shooter.turretEncoder
import kotlin.math.cos

object Panels {
    val panelsTelemetry = PanelsTelemetry.telemetry
    val panelsField = PanelsField.field
    fun telemetry() {
        panelsTelemetry.addData("Turret actual pos", turretEncoder.state.position)
        panelsTelemetry.addData("Turret target", turretController.goal.position)
        panelsTelemetry.addData("Turret power", turret.power)
        panelsTelemetry.addData("Flywheel vel", flywheel.state.velocity)
        panelsTelemetry.addData("Flywheel target", evanResult.targetRPM)
        panelsTelemetry.addData("Xvelo", follower.velocity.xComponent)
        panelsTelemetry.addData("Yvelo", follower.velocity.yComponent)
        panelsTelemetry.addData("Yaw", Math.toDegrees(follower.pose.heading))
        panelsTelemetry.addData("Target hood", evanResult.targetHoodPos)
        panelsTelemetry.addData("Flywheel power", flywheel.power)
        panelsTelemetry.update()
    }

    fun field() {
//        panelsField.setOffsets(PanelsField.presets.PEDRO_PATHING)
//        panelsField.setStyle("red", "blue", 2.0)
//        panelsField.moveCursor(follower.pose.x, follower.pose.y)
//        panelsField.cursorHeading = follower.pose.heading
//        val globalTurretHeading = (follower.heading + Evan.posToYaw(turretEncoderPos())) % 360
//        val magnitude = Evan.distance * (flywheel.velocity/flywheelController.goal.velocity)
//        panelsField.line(cos(globalTurretHeading)*magnitude + follower.pose.x, cos(globalTurretHeading)*magnitude+follower.pose.y)
//        panelsField.line(Evan.robotVelocityVector.x*39.37 + follower.pose.x, Evan.robotVelocityVector.y*39.37 + follower.pose.y)
//        panelsField.rect(16.0, 18.0)
//        panelsField.update()
    }
}