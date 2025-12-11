//package org.firstinspires.ftc.teamcode;
//
//import static org.firstinspires.ftc.teamcode.Constants.*;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.robot.Robot;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//
//@Config
//@TeleOp(name="MainTeleop")
//public class MainTeleop extends LinearOpMode {
//    RobotHardware robot = new RobotHardware();
//    MecanumMovement mecanum;
//
//
//    public void runOpMode() throws InterruptedException {
//        robot.init(hardwareMap);
//        mecanum = new MecanumMovement(robot);
//        waitForStart();
//        while (opModeIsActive()) {
//            mecanum.mecanum_drive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
//            controls();
//            robot.update();
//        }
//    }
//    public void controls() throws InterruptedException {
//        if (gamepad1.crossWasPressed()) {
//            if (robot.door.getPosition() == DOOR_OPEN) {
//                robot.close_door();
//            } else {
//                robot.open_door();
//            }
//        }
//        if (gamepad1.triangleWasPressed()) {
//            if (robot.intake.getPower() == INTAKE_POWER) {
//                robot.intake_off();
//            } else {
//                robot.intake_on();
//            }
//        }
//    }
//}
