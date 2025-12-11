package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.DcMotor;
import static org.firstinspires.ftc.teamcode.Constants.*;



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

    public double turn_to_basket_pid(double desired_angle, double current_angle) {
        double error = desired_angle - current_angle;
        double k = 0.01;
        return error * k;
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

        LeftFrontMotor.setPower(- (adjustedY + adjustedX + rx) * DRIVE_SPEED);
        LeftBackMotor.setPower(- (adjustedY - adjustedX + rx) * DRIVE_SPEED);
        RightFrontMotor.setPower((adjustedY - adjustedX - rx) * DRIVE_SPEED);
        RightBackMotor.setPower((adjustedY + adjustedX - rx) * DRIVE_SPEED);
    }
}
