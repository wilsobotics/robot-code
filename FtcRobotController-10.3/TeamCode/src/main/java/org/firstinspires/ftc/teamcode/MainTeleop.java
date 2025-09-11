package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name="MainTeleop")
public class MainTeleop extends LinearOpMode {
    RobotHardware robot = new RobotHardware();
    MecanumMovement mecanum;

    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        mecanum = new MecanumMovement(robot);
        waitForStart();
        while (opModeIsActive()) {
            mecanum.mecanum_drive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
            controls();
            robot.update();
        }
    }
    public void controls() throws InterruptedException {
        if (gamepad1.crossWasPressed()) {
            robot.hsslides_out();
        }
        if (gamepad1.triangleWasPressed()) {
            robot.grab_and_transfer_sample_auto();
        }
        if (gamepad1.squareWasPressed()) {
            robot.reset();
        }
    }
}
