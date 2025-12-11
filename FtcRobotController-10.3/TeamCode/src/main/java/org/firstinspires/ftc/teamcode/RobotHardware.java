package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Constants.*;
import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;

public class RobotHardware {
    public DcMotor leftFront, rightFront, leftBack, rightBack;
    public DcMotorEx intake, shooter;
    public Servo door;
    public GoBildaPinpointDriver odo;
    public ElapsedTime myElapsedTime;
    public ArrayList<String> instructions = new ArrayList<>();

    public void init(HardwareMap hardwareMap) {
        leftFront = hardwareMap.get(DcMotor.class, "LeftFrontMotor");
        rightFront = hardwareMap.get(DcMotor.class, "RightFrontMotor");
        leftBack = hardwareMap.get(DcMotor.class, "LeftBackMotor");
        rightBack = hardwareMap.get(DcMotor.class, "RightBackMotor");

        door = hardwareMap.get(Servo.class, "door");
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");

        myElapsedTime = new ElapsedTime();
        myElapsedTime.reset();

        odo = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        odo.setOffsets(-74, 0);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.resetPosAndIMU();
    }
    public void update() {
//        do_instructions();
//        rpm_pid(calc_rpm(distance_to_basket()), shooter.getVelocity());
    }
    public double distance_to_basket() {
        return Math.sqrt(Math.abs(Math.pow(odo.getPosX() - BASKET_X, 2)) + Math.abs(Math.pow(odo.getPosY() - BASKET_Y, 2)));
    }
    public double angle_to_basket() {
        return Math.toDegrees(Math.atan2(Math.pow(odo.getPosY() - BASKET_Y, 2), Math.pow(odo.getPosX() - BASKET_X, 2)));
    }
    public double time() {
        return myElapsedTime.milliseconds();
    }

//    public void do_instructions() {
//        int len = instructions.size();
//        double time = time();
//        if (len > 0) {
//            String instruction = instructions.get(0);
//            String[] task = instruction.split("\\s+");
//            String component = task[1];
//
//            double p = Double.parseDouble(task[2]);
//            double requiredTime = Double.parseDouble(task[0]);
//            if (time > requiredTime) {
//
//                instructions.remove(0);
//            }
//        }
//    }
//
    public void open_door() {
        door.setPosition(DOOR_OPEN);
    }
    public void close_door() {
        door.setPosition(DOOR_CLOSE);
    }
    public void calcThrowRPM(double targetRPM) {

        // TODO: tune these values
        double Kp = 1;
        double Ki = 1;
        double Kd = 1;

        double integralSum = 0;
        double lastError = 0;

        double tempMotorValue = shooter.getVelocity(); // replace with your motor's RPM reading

        ElapsedTime timer = new ElapsedTime();

        while (tempMotorValue != targetRPM) {

            double currentRPM = shooter.getVelocity();  // TODO: replace with actual motor.getVelocity()
            double error = targetRPM - currentRPM;

            double dt = timer.seconds();  // time since last loop
            double derivative = (error - lastError) / dt;

            integralSum += error * dt;

            double output = (Kp * error) + (Ki * integralSum) + (Kd * derivative);

            // TODO: set motor power using 'output'
            shooter.setPower(output);

            lastError = error;
            timer.reset();

            // update for loop condition
            tempMotorValue = currentRPM;
        }
    }

//    public void intake_on() {
//        intake.setPower(INTAKE_POWER);
//    }
//    public void intake_off() {
//        intake.setPower(0);
//    }

//    public double calc_rpm(double distance) {
//        return 0.1;
//    }

}