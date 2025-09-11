package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "TEST_AUTO")
public class TestAuto extends LinearOpMode {
    RobotHardware robot = new RobotHardware();
    AutoMovement autoMovement;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
//        autoMovement = new AutoMovement(robot, telemetry);

        waitForStart();
    }
}
