package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Constants.*;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
@TeleOp(name="TestTeleop")
public class TestTeleop extends LinearOpMode {
    RobotHardware robot = new RobotHardware();
    MecanumMovement mecanum;
    FtcDashboard dashboard = FtcDashboard.getInstance();
    public double SHOOTER_POWER = 0;
    public double INTAKE_POWER = 0;
    public double SERVO_POS = 0;
    public boolean align_shooter = false;
    public double rx;


    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        mecanum = new MecanumMovement(robot);
        waitForStart();
        while (opModeIsActive()) {
            double deltaX = 4.5*600 -300 - robot.odo.getPosY();
            double deltaY = 5.5*600  + robot.odo.getPosX();
            double desired_angle = 90 - Math.toDegrees(Math.atan2(deltaY, deltaX));
            if (align_shooter) {
                rx = mecanum.turn_to_basket_pid(desired_angle, mecanum.convert_yaw(Math.toDegrees(robot.odo.getHeading()) * -1));
            } else {
                rx = gamepad1.right_stick_x;
            }
            mecanum.mecanum_drive(gamepad1.left_stick_x, gamepad1.left_stick_y, rx);
            controls();
            robot.update();
            robot.shooter.setPower(SHOOTER_POWER);
            robot.intake.setPower(INTAKE_POWER);
            robot.door.setPosition(SERVO_POS);
            TelemetryPacket packet = new TelemetryPacket();
            packet.put("X", robot.odo.getPosY());
            packet.put("Y", robot.odo.getPosX());
            packet.put("DeltaX", deltaX);
            packet.put("DeltaY", deltaY);
            packet.put("Current_heading", mecanum.convert_yaw(Math.toDegrees(robot.odo.getHeading()) * -1));
            packet.put("Desired_heading", desired_angle);
            packet.put("Speed", robot.shooter.getVelocity());
            packet.put("Power", SHOOTER_POWER);
            packet.put("DOOR", SERVO_POS);
            dashboard.sendTelemetryPacket(packet);
        }
    }
    public void controls() throws InterruptedException {
        if (gamepad1.dpadUpWasPressed()) {
            SHOOTER_POWER += 0.1;
        }
        if (gamepad1.dpadDownWasPressed()) {
            SHOOTER_POWER -= 0.1;
        }
        if (gamepad1.crossWasPressed())
        { if (INTAKE_POWER == 0) {
            INTAKE_POWER = 1;
          } else {
            INTAKE_POWER = 0;
          }
        }
        if (gamepad1.circleWasPressed()) {
            if (SERVO_POS == DOOR_CLOSE) {
                SERVO_POS = DOOR_OPEN;
            } else {
                SERVO_POS = DOOR_CLOSE;
            }
        }
        if (gamepad1.triangleWasPressed()) {
            if (align_shooter) {
                align_shooter = false;
            } else {
                align_shooter = true;
            }
        }
    }
}
