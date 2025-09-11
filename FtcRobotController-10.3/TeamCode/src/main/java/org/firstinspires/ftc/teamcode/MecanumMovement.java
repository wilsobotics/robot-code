package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.DcMotor;


public class MecanumMovement {
    RobotHardware robot;
    GoBildaPinpointDriver odo;
    DcMotor LeftFrontMotor, RightFrontMotor, LeftBackMotor, RightBackMotor;

    public double joystickAngle, joystickMagnitude, adjustedAngle, adjustedX, adjustedY;

    public MecanumMovement(RobotHardware robot) {
        this.robot = robot;

        this.odo = robot.odo;

        this.LeftFrontMotor = robot.leftFront;
        this.RightFrontMotor = robot.rightFront;
        this.LeftBackMotor = robot.leftBack;
        this.RightBackMotor = robot.rightBack;
    }

    public double convert_yaw(double yaw) {
        return ((yaw + 180) % 360 + 360) % 360 - 180;
    }
    public void mecanum_drive(double lx, double ly, double rx) {
        double yaw = 90 - convert_yaw(Math.toDegrees(odo.getHeading()) * -1);

        ly = -ly;

        odo.update();
        joystickAngle = convert_yaw(Math.toDegrees(Math.atan2(ly, lx)));
        joystickMagnitude = Math.sqrt(lx * lx + ly * ly);

        yaw = yaw - 90;
        yaw = ((yaw + 180) % 360) - 180;
        adjustedAngle = joystickAngle - yaw;

        adjustedX = joystickMagnitude * Math.cos(Math.toRadians(adjustedAngle));
        adjustedY = joystickMagnitude * Math.sin(Math.toRadians(adjustedAngle));

        double drivespeed = 0.5;
        LeftFrontMotor.setPower(- (adjustedY + adjustedX + rx) * drivespeed);
        LeftBackMotor.setPower(- (adjustedY - adjustedX + rx) * drivespeed);
        RightFrontMotor.setPower((adjustedY - adjustedX - rx) * drivespeed);
        RightBackMotor.setPower((adjustedY + adjustedX - rx) * drivespeed);
    }
}
