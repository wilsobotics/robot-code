package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import static org.firstinspires.ftc.teamcode.Constants.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;



public class AutoMovement {


    RobotHardware robot;

    GoBildaPinpointDriver odo;
    ElapsedTime time;

    DcMotor LeftFrontMotor, RightFrontMotor, LeftBackMotor, RightBackMotor;

    public double distanceToXTarget, distanceToYTarget, distance, ogDistance, distanceToCX, distanceToCY, distanceToC;
    public double currentHeading, headingError, ogHeadingError;
    public boolean turningDone, posDone;
    public double oldTime, oldX, oldY;
    public double rotationSpeed, posSpeed, currentSpeed, currentAcceleration;
    public double velocity_x, velocity_y, vxR, vyR;
    public double frontLeftPP, frontRightPP, backLeftPP, backRightPP, maxMagnitude;

    public double frontLeftPower, frontRightPower, backLeftPower, backRightPower;
    public double desiredOrientation, desiredX, desiredY, desiredSpeed, nextCheckpointX, nextCheckpointY;
    public boolean decelerate, accurate;
    private FtcDashboard dashboard;

    public AutoMovement(RobotHardware robot, FtcDashboard dashboard) {
        this.robot = robot;
        this.dashboard = dashboard;

        // Assign odo and motors from RobotHardware instance
        this.odo = robot.odo;
        this.time = robot.myElapsedTime;

        this.LeftFrontMotor = robot.leftFront;
        this.RightFrontMotor = robot.rightFront;
        this.LeftBackMotor = robot.leftBack;
        this.RightBackMotor = robot.rightBack;
    }


    public void setRotationSpeed() {
        double a = 1;
        if (accurate) {a = 0.5;}
        if (headingError> 0) {
            rotationSpeed = Math.max(headingError * HEADING_CORRECTION_SPEED, MINIMUM_ROTATIONAL_SPEED);
        } else {
            rotationSpeed = Math.min(headingError * HEADING_CORRECTION_SPEED, -MINIMUM_ROTATIONAL_SPEED);
        }
        if (Math.abs(headingError) < MAXIMUM_HEADING_ERROR*a) {
            turningDone = true;
            if (headingError> 0) {
                rotationSpeed = Math.max(headingError * HEADING_CORRECTION_SPEED, MINIMUM_HEADING_CORRECTION_SPEED);
            } else {
                rotationSpeed = Math.min(headingError * HEADING_CORRECTION_SPEED, -MINIMUM_HEADING_CORRECTION_SPEED);
            }
        }
    }

    public double calculateSpeedWithDeceleration(double max_speed, double decel_length, double distance, double multiplier, double min_speed) {
        return Math.max(max_speed - ((decel_length - distance) / decel_length) * multiplier, min_speed*max_speed);
    }

    public void setPositionalSpeed() {
        posSpeed = desiredSpeed;
        double min;
        if (accurate) {
            min = MINIMUM_SPEED_ACCURATE;
        } else {
            min = MINIMUM_SPEED;
        }
        if (distance < DECELERATION_LENGTH && decelerate) {
            posSpeed = calculateSpeedWithDeceleration(desiredSpeed, DECELERATION_LENGTH, distance, DECELERATION_MULTIPLIER_POSITIONAL, min);
        }
        if (distanceToC < DECELERATION_LENGTH) {
            posSpeed = calculateSpeedWithDeceleration(desiredSpeed, DECELERATION_LENGTH, distanceToC, DECELERATION_MULTIPLIER_POSITIONAL, min);
        }
        double max = MAXIMUM_DISTANCE;
        if (!decelerate) {
            max *= 2;
        }
        if (accurate) {
            max /= 2;
        }
        if (distance < max || posDone) {
            posDone = true;
            if (decelerate) {
                posSpeed = 0;
            }
        }
    }

    public void setWheelSpeeds() {
        velocity_x = (distanceToXTarget / distance) * (posSpeed);
        velocity_y = (distanceToYTarget / distance) * (posSpeed);
        double theta = odo.getHeading();
        vxR = velocity_x * Math.cos(theta) + velocity_y * Math.sin(theta);
        vyR = -velocity_x * Math.sin(theta) + velocity_y * Math.cos(theta);
        setDriveSpeed(vxR, vyR, rotationSpeed);
    }

    public double getCurrentSpeed() {
        double velX = odo.getVelY();
        double velY = odo.getVelX();
        return Math.sqrt(velX * velX + velY * velY);
    }

    public void updateOdometry() {
        odo.update();
        double time = robot.time();
        double x = odo.getPosY();
        double y = odo.getPosX();

        distanceToXTarget = desiredX + x;
        distanceToYTarget = desiredY - y;

        double previousSpeed = currentSpeed;

        try {
            currentSpeed = getCurrentSpeed();
        } catch (Exception e) {
            currentSpeed = 0;
        }

        double deltaTime = time - oldTime;
        if (deltaTime == 0) {
            currentAcceleration = 0;
        } else {
            currentAcceleration = (currentSpeed - previousSpeed) / deltaTime;
        }

        oldX = x;
        oldY = y;
        oldTime = time;

        distanceToCX = nextCheckpointX + odo.getPosY();
        distanceToCY = nextCheckpointY - odo.getPosX();
        distanceToC = Math.hypot(distanceToCX, distanceToCY);
        distance = Math.hypot(distanceToXTarget, distanceToYTarget);

        currentHeading = normaliseDegrees(Math.toDegrees(odo.getHeading()));
        headingError = normaliseDegrees(desiredOrientation - currentHeading);
    }



    public void goToPosition(double desiredX, double desiredY, double desiredSpeed, double desiredOrientation, boolean decelerate, double nextCheckpointX, double nextCheckpointY, boolean accurate) {

        this.desiredX = desiredX;
        this.desiredY = desiredY;
        this.desiredSpeed = desiredSpeed;
        this.decelerate = decelerate;
        this.desiredOrientation = desiredOrientation;
        this.nextCheckpointX = nextCheckpointX;
        this.nextCheckpointY = nextCheckpointY;
        this.accurate = accurate;

        desiredOrientation = desiredOrientation * -1;

        System.out.println(time.milliseconds());

        distanceToXTarget = desiredX + odo.getPosY();
        distanceToYTarget = desiredY - odo.getPosX();

        currentHeading = normaliseDegrees(Math.toDegrees(odo.getHeading()));
        headingError = normaliseDegrees(desiredOrientation - currentHeading);
        ogHeadingError = headingError;

        ogDistance = Math.hypot(distanceToXTarget, distanceToYTarget);

        turningDone = false;
        posDone = false;


        while  (!(posDone && (turningDone || !decelerate))) {
//            robot.do_instructions();
            updateOdometry();
            setRotationSpeed();
            setPositionalSpeed();
            setWheelSpeeds();
            TelemetryPacket packet = new TelemetryPacket();
            packet.put("Speed", currentSpeed);
            packet.put("Acceleration", currentAcceleration);
            packet.fieldOverlay()
                    .setFill("blue")
                    .fillRect(robot.odo.getPosY()/25.4, robot.odo.getPosX()/25.4, 5, 5);
            dashboard.sendTelemetryPacket(packet);
        }
        if (accurate) {
            setDriveSpeed(-vxR/2, -vyR/2, -rotationSpeed/4);
        }
    }

    public void setDriveSpeed(double vxR, double vyR, double rotationSpeed) {
        frontLeftPP = vxR + vyR;
        frontRightPP = -vxR + vyR;
        backLeftPP = vxR - vyR;
        backRightPP = -vxR - vyR;

        maxMagnitude = Math.max(1.0, Math.max(Math.abs(frontLeftPP),
                Math.max(Math.abs(frontRightPP), Math.max(Math.abs(backLeftPP), Math.abs(backRightPP)))));

        frontLeftPower = -frontLeftPP/maxMagnitude + rotationSpeed;
        frontRightPower = frontRightPP/maxMagnitude + rotationSpeed;
        backLeftPower = backLeftPP/maxMagnitude + rotationSpeed;
        backRightPower = -backRightPP/maxMagnitude + rotationSpeed;

        LeftFrontMotor.setPower(frontLeftPower);
        RightFrontMotor.setPower(frontRightPower);
        LeftBackMotor.setPower(backLeftPower);
        RightBackMotor.setPower(backRightPower);
    }






    public void rotateToHeading(double desiredOrientation) {
        this.desiredOrientation = desiredOrientation;

        while (Math.abs(headingError) > MAXIMUM_HEADING_ERROR) {
            updateOdometry();
//            robot.do_instructions();


            if (headingError > 0) {
                rotationSpeed = MINIMUM_TURNING_SPEED + headingError * DECELERATION_MULTIPLIER_TURNING;
            } else {
                rotationSpeed = -MINIMUM_TURNING_SPEED + headingError * DECELERATION_MULTIPLIER_TURNING;
            }
            setRotationWheels(rotationSpeed);
//            do_instructions();
        }

        setDriveSpeed(0, 0, 0);
    }

    public void setRotationWheels(double rotationSpeed) {
        frontLeftPower = rotationSpeed;
        frontRightPower = rotationSpeed;
        backLeftPower = rotationSpeed;
        backRightPower = rotationSpeed;

        LeftFrontMotor.setPower(frontLeftPower);
        RightFrontMotor.setPower(frontRightPower);
        LeftBackMotor.setPower(backLeftPower);
        RightBackMotor.setPower(backRightPower);
    }

    public static double normaliseDegrees(double degrees) {
        double temp = (degrees + 180.0) / 360.0;
        return (temp - Math.floor(temp) - 0.5) * 360.0;
    }
}