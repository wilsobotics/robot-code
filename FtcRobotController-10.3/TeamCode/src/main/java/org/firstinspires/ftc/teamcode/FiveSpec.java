package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.ArrayList;

@Autonomous(name = "MAIN_AUTO")
public class FiveSpec extends LinearOpMode {
    private GoBildaPinpointDriver odo;

    public DcMotor LeftFrontMotor;
    public DcMotor LeftBackMotor;
    public DcMotor RightFrontMotor;
    public DcMotor RightBackMotor;
    ElapsedTime myElapsedTime;
    ArrayList<String> instructions = new ArrayList<String>();
    private Servo rightslide;
    private Servo leftslide;
    private Servo hsclaw;
    private Servo hspitch;
    private Servo hsyaw;
    private DcMotor righths;
    private Servo vsclaw;
    private Servo vsyaw;
    private Servo leftarm;
    private Servo rightarm;
    private DcMotor vyperslide1;
    private DcMotor vyperslide2;
    private Servo sweeper;
    private Servo webcam_arm;
    private int vstargetpos = 0;
    private boolean vsactive = false;
    private double vspower = 0;

    private double current_yaw = 0.0;

    @Override
    public void runOpMode() {
        rightslide = hardwareMap.get(Servo.class, "right horizontal slide");
        sweeper = hardwareMap.get(Servo.class, "sweeper");
        leftslide = hardwareMap.get(Servo.class, "left slide");
        hsclaw = hardwareMap.get(Servo.class, "hs claw");
        hspitch = hardwareMap.get(Servo.class, "hs pitch");
        webcam_arm = hardwareMap.get(Servo.class, "webcam arm");
        hsyaw = hardwareMap.get(Servo.class, "hs yaw");
        vsclaw = hardwareMap.get(Servo.class, "vs claw");
        vsyaw = hardwareMap.get(Servo.class, "vs yaw");
        leftarm = hardwareMap.get(Servo.class, "leftarm");
        rightarm = hardwareMap.get(Servo.class, "rightarm");
        vyperslide1 = hardwareMap.get(DcMotor.class, "vyperslide1");
        vyperslide2 = hardwareMap.get(DcMotor.class, "vyperslide2");
        righths = hardwareMap.get(DcMotor.class, "righths");


        myElapsedTime = new ElapsedTime();
        myElapsedTime.reset();

        odo = hardwareMap.get(GoBildaPinpointDriver.class, "odo");
        odo.setOffsets(-74, 0);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.resetPosAndIMU();

        LeftFrontMotor = hardwareMap.get(DcMotor.class, "LeftFrontMotor");
        LeftBackMotor = hardwareMap.get(DcMotor.class, "LeftBackMotor");
        RightFrontMotor = hardwareMap.get(DcMotor.class, "RightFrontMotor");
        RightBackMotor = hardwareMap.get(DcMotor.class, "RightBackMotor");
        for (DcMotor motor : new DcMotor[]{LeftFrontMotor, LeftBackMotor, RightFrontMotor, RightBackMotor}) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }



        try {
            waitForStart();
            resetRuntime();
            vyperslide1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            vyperslide2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            vyperslide1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            vyperslide2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            if (opModeIsActive()) {
                // clip_spec_best();
                // clip_spec_best();
                // clip_spec_best();
                // clip_spec_best();
                // clip_spec_best();
                righths.setPower(-0.4);
                get_samples_floor_nosweeper();
                clip_spec_best(false);
                clip_spec_best(false);
                clip_spec_best(false);
                clip_spec_best(false);
                clip_spec_best(true);
                // myElapsedTime.reset();
                // set_vertical_pos(3000);
                // instructions.add("0 leftarm 0.79");
                // instructions.add("0 rightarm 0.3");
                // instructions.add("0 vsyaw 0.2");
                // instructions.add("0 righths 0");
                // goToPosition(1760, -200, 1, -90);
                // vsclaw.setPosition(0.9);
                // leftarm.setPosition(0.35);
                // rightarm.setPosition(0.7);
                // set_vertical_pos(0);
                // goToPosition(1660, -200, 1, 180);
                // sleep(2000);
            }
        } finally {
            if (odo!=null) {
                odo.resetPosAndIMU();
            }
            odo = null;
        }

    }

    public void clip_spec_best(boolean rotate) {
        myElapsedTime.reset();

        vsyaw.setPosition(0.2);
        leftarm.setPosition(0.75);
        rightarm.setPosition(0.3);
        vsclaw.setPosition(1);
        set_vertical_pos(620);
        goToPosition(650, -730, 1, 0);
        vsclaw.setPosition(0.8);
        vsyaw.setPosition(0.9);
        myElapsedTime.reset();
        instructions.add("300 rightarm 0.7");
        instructions.add("300 leftarm 0.35");
        instructions.add("500 vs 360");
        if (rotate) {
            goToPosition(-240, 0, 1, 180);
            set_vertical_pos(0);
        } else {
            goToPosition(-240, 0, 1, 0);
            set_vertical_pos(800);
        }
        vsclaw.setPosition(1);
        sleep(200);
    }


    public void get_samples_floor_nosweeper() {
        goToPosition(-420, -1200, 1, 0);
        goToPosition(-470, -1200, 1, 0);
        goToPosition(-450, -300, 1, 0);
        //goToPosition(-400, -1200, 1, 0);
        goToPosition(-480, -1200, 1, 0);
        goToPosition(-700, -1200, 1, 0);
        goToPosition(-700, -300, 1, 0);
        goToPosition(-700, -1200, 1, 0);
        goToPosition(-910, -1250, 1, 0);
        leftarm.setPosition(0.35);
        rightarm.setPosition(0.7);
        vsyaw.setPosition(0.9);
        vsclaw.setPosition(0.8);
        set_vertical_pos(260);
        goToPosition(-920, -300, 1, 0);
        goToPosition(-300, -300, 1, 0);
        goToPosition(-240, 0, 1, 0);
        vsclaw.setPosition(1);
        sleep(200);
    }

    // public void clip_spec_new_nosweeper(double i) {
    //     sweeper.setPosition(1);
    //     vsclaw.setPosition(0);
    //     myElapsedTime.reset();
    //     rightarm.setPosition(0.88);
    //     leftarm.setPosition(0.12);
    //     vsyaw.setPosition(0);
    //     set_vertical_pos(250, 1);
    //     instructions.add("650 vs 0");
    //     goToPosition(550, -570, 1, 0);
    //     RightFrontMotor.setPower(-0.4);
    //     LeftFrontMotor.setPower(0.4);
    //     RightBackMotor.setPower(-0.4);
    //     LeftBackMotor.setPower(0.4);
    //     sleep(100);
    //     set_vertical_pos(1200, 1);
    //     myElapsedTime.reset();
    //     instructions.add("800 rightarm 0.2");
    //     instructions.add("800 leftarm 0.7");
    //     instructions.add("800 vs 0");
    //     instructions.add("800 vsyaw 0.8");
    //     instructions.add("1000 vsclaw 1");
    //     instructions.add("1300 vsyaw 0.6");
    //     double time = get_time();
    //     while (opModeIsActive() && time < 170) {
    //         check_vertical_pos();
    //         time = get_time();
    //         do_instructions();
    //     }
    //     RightFrontMotor.setPower(1);
    //     LeftFrontMotor.setPower(-1);
    //     RightBackMotor.setPower(1);
    //     LeftBackMotor.setPower(-1);
    //     while (opModeIsActive() && time < 450) {
    //         check_vertical_pos();
    //         time = get_time();
    //         do_instructions();
    //     }
    //     // goToPosition((120), (100), 1, 180);
    //     goToPosition((-270), (-120), 1, 0);
    //     vsclaw.setPosition(0);
    //     sleep(300);
    // }

    // public void get_samples_floor() {
    //     myElapsedTime.reset();
    //     vsclaw.setPosition(0);
    //     leftslide.setPosition(1);
    //     rightslide.setPosition(0);
    //     sweeper.setPosition(0.5);
    //     rightarm.setPosition(0.35);
    //     leftarm.setPosition(0.55);
    //     goToPosition(340, 600, 1, 20);
    //     myElapsedTime.reset();
    //     instructions.add("150 leftslide 0");
    //     instructions.add("150 rightslide 1");
    //     goToPosition(700, 200, 1, 125);
    //     sweeper.setPosition(0.7);
    //     goToPosition(270, 570, 1, 30);
    //     sweeper.setPosition(0.5);
    //     rightslide.setPosition(0.80);
    //     leftslide.setPosition(0.20);
    //     rotateToHeading(-110);
    //     sweeper.setPosition(0.7);
    //     goToPosition(500, 550, 1, 30);
    //     sweeper.setPosition(0.5);
    //     myElapsedTime.reset();
    //     instructions.add("0 leftslide 1");
    //     instructions.add("0 rightslide 0");
    //     instructions.add("500 sweeper 1");
    //     instructions.add("600 vsclaw 1");
    //     instructions.add("1000 rightarm 0.26");
    //     instructions.add("1000 leftarm 0.64");
    //     instructions.add("1000 vsyaw 0.5");
    //     goToPosition(220, -140, 1, 180);
    //     sleep(100);
    //     vsclaw.setPosition(0);
    //     sleep(300);
    // }

    // public void clip_spec_new(double i) {
    //     sweeper.setPosition(1);
    //     vsclaw.setPosition(0);
    //     myElapsedTime.reset();
    //     rightarm.setPosition(0.95);
    //     leftarm.setPosition(0.05);
    //     vsyaw.setPosition(0);
    //     set_vertical_pos(250, 1);
    //     instructions.add("500 vs 0");
    //     goToPosition(-1*(650), -1*(-540), 1, 180);
    //     RightFrontMotor.setPower(-0.4);
    //     LeftFrontMotor.setPower(0.4);
    //     RightBackMotor.setPower(-0.4);
    //     LeftBackMotor.setPower(0.4);
    //     sleep(100);
    //     set_vertical_pos(900, 1);
    //     myElapsedTime.reset();
    //     instructions.add("800 rightarm 0.24");
    //     instructions.add("800 leftarm 0.66");
    //     instructions.add("800 vs 0");
    //     instructions.add("800 vsyaw 0.8");
    //     instructions.add("1000 vsclaw 1");
    //     instructions.add("1300 vsyaw 0.6");
    //     double time = get_time();
    //     while (opModeIsActive() && time < 170) {
    //         check_vertical_pos();
    //         time = get_time();
    //         do_instructions();
    //     }
    //     RightFrontMotor.setPower(1);
    //     LeftFrontMotor.setPower(-1);
    //     RightBackMotor.setPower(1);
    //     LeftBackMotor.setPower(-1);
    //     while (opModeIsActive() && time < 450) {
    //         check_vertical_pos();
    //         time = get_time();
    //         do_instructions();
    //     }
    //     // goToPosition((120), (100), 1, 180);
    //     goToPosition((270), (-120), 1, 180);
    //     vsclaw.setPosition(0);
    //     sleep(200);
    // }

    // public void clip_spec_without_start(double i) {
    //     sweeper.setPosition(1);
    //     vsclaw.setPosition(0);
    //     myElapsedTime.reset();
    //     rightarm.setPosition(0.95);
    //     leftarm.setPosition(0.05);
    //     vsyaw.setPosition(0);
    //     set_vertical_pos(250, 1);
    //     instructions.add("500 vs 0");
    //     goToPosition(900, -550, 1, 0);
    //     RightFrontMotor.setPower(-0.4);
    //     LeftFrontMotor.setPower(0.4);
    //     RightBackMotor.setPower(-0.4);
    //     LeftBackMotor.setPower(0.4);
    //     sleep(150);
    //     set_vertical_pos(750, 1);
    //     myElapsedTime.reset();
    //     instructions.add("800 rightarm 0.15");
    //     instructions.add("800 leftarm 0.75");
    //     instructions.add("800 vsclaw 1");
    //     instructions.add("800 vs 0");
    //     instructions.add("800 vsyaw 0.8");
    //     instructions.add("1300 vsyaw 0.6");
    //     double time = get_time();
    //     while (opModeIsActive() && time < 100) {
    //         check_vertical_pos();
    //         time = get_time();
    //         do_instructions();
    //     }
    //     RightFrontMotor.setPower(1);
    //     LeftFrontMotor.setPower(-1);
    //     RightBackMotor.setPower(1);
    //     LeftBackMotor.setPower(-1);
    //     while (opModeIsActive() && time < 400) {
    //         check_vertical_pos();
    //         time = get_time();
    //         do_instructions();
    //     }
    //     goToPosition(100, -150, 1, 0);
    //     goToPosition(50, 20, 0.6, 0);
    //     vsclaw.setPosition(0);
    //     sleep(200);
    // }

    // public void clip_spec() {
    //     vsclaw.setPosition(1);
    //     myElapsedTime.reset();
    //     instructions.add("500 rightarm 1");
    //     instructions.add("500 leftarm 0");
    //     set_vertical_pos(2300, 1);
    //     vsyaw.setPosition(0.15);
    //     goToPosition(1500, -650, 1, 0);
    //     RightFrontMotor.setPower(-0.2);
    //     LeftFrontMotor.setPower(0.2);
    //     RightBackMotor.setPower(-0.2);
    //     LeftBackMotor.setPower(0.2);
    //     set_vertical_pos(3800, 1);
    //     sleep(300);
    //     vsclaw.setPosition(0);
    //     set_vertical_pos(0, -1);
    //     rightarm.setPosition(0.33);
    //     leftarm.setPosition(0.78);
    //     vsyaw.setPosition(0.15);
    //     goToPosition(700, -100, 1, 0);
    //     sleep(700);
    // }
    public double get_time() {
        return myElapsedTime.milliseconds();
    }

    public void set_vertical_pos(int pos) {
        vstargetpos = pos;
        vsactive = true;
    }

    public void check_vertical_pos() {
        telemetry.addData("VS2_POS", vyperslide2.getCurrentPosition());
        telemetry.addData("VS2_targetPOS", vstargetpos);
        telemetry.addData("vsactive", vsactive);
        if (Math.abs(vyperslide2.getCurrentPosition() - vstargetpos) < 30) {
            vsactive = false;
        }
        if (vyperslide2.getCurrentPosition() > vstargetpos) {
            vspower = -1;
        } else {
            vspower = 1;
        }
        if (vsactive) {
            vyperslide1.setPower(-vspower);
            vyperslide2.setPower(-vspower);
        } else {
            vyperslide1.setPower(-0.06);
            vyperslide2.setPower(-0.06);
        }
    }

    public void do_instructions() {
        int len = instructions.size();
        double time = get_time();
        if (len > 0) {
            String instruction = instructions.get(0);
            String[] task = instruction.split("\\s+");
            String component = task[1];

            double p = Double.parseDouble(task[2]);
            double requiredTime = Double.parseDouble(task[0]);
            if (time > requiredTime) {
                if (component.equals("vs")) {
                    int P = (int) p;
                    set_vertical_pos(P);
                }
                if (component.equals("vyperslide1")) {
                    vyperslide1.setPower(p);
                }
                if (component.equals("vyperslide2")) {
                    vyperslide1.setPower(p);
                }
                if (component.equals("leftslide")) {
                    leftslide.setPosition(p);
                }
                if (component.equals("sweeper")) {
                    sweeper.setPosition(p);
                }
                if (component.equals("vsclaw")) {
                    vsclaw.setPosition(p);
                }
                if (component.equals("vsyaw")) {
                    vsyaw.setPosition(p);
                }
                if (component.equals("leftarm")) {
                    leftarm.setPosition(p);
                }
                if (component.equals("rightarm")) {
                    rightarm.setPosition(p);
                }
                if (component.equals("hsclaw")) {
                    hsclaw.setPosition(p);
                }
                if (component.equals("hspitch")) {
                    hsclaw.setPosition(p);
                }
                if (component.equals("leftslide")) {
                    leftslide.setPosition(p);
                }
                if (component.equals("rightslide")) {
                    rightslide.setPosition(p);
                }
                instructions.remove(0);
            }
        }
    }


    public static double normaliseDegrees(double degrees) {
        double temp = (degrees + 180.0) / 360.0;
        return (temp - Math.floor(temp) - 0.5) * 360.0;
    }

    public void goToPosition(double desiredX, double desiredY, double desiredSpeed, double desiredOrientation) {
        desiredOrientation  = desiredOrientation * -1;
        double distanceToXTarget, distanceToYTarget, distance;
        distanceToXTarget = desiredX + odo.getPosY(); // Switched X and Y. + as we want X to increase to the right.
        distanceToYTarget = desiredY - odo.getPosX(); // Switched X and Y
        double currentHeading = normaliseDegrees(Math.toDegrees(odo.getHeading()));
        double ogHeadingError = normaliseDegrees(desiredOrientation - currentHeading);
        double speed;
        double headingError;
        double rotationSpeed;
        double ogDistance = Math.hypot(distanceToXTarget, distanceToYTarget);
        boolean turningDone = false;
        boolean posDone = false;

        while (opModeIsActive()) {
            odo.update();
            distanceToXTarget = desiredX + odo.getPosY();
            distanceToYTarget = desiredY - odo.getPosX();
            distance = Math.hypot(distanceToXTarget, distanceToYTarget);

            double turningDecel = 0.02;
            currentHeading = normaliseDegrees(Math.toDegrees(odo.getHeading()));
            headingError = normaliseDegrees(desiredOrientation - currentHeading);
            telemetry.addData("heading error", headingError);
            telemetry.addData("desiredOrientation", desiredOrientation);
            telemetry.addData("currentHeading", odo.getHeading());
            telemetry.update();
            if (desiredOrientation == current_yaw) {
                rotationSpeed = headingError * turningDecel;
            }
            else {
                if (headingError > 0) {
                    rotationSpeed = 0.3 + headingError * (turningDecel);
                } else {
                    rotationSpeed = -0.3 + headingError * (turningDecel);
                }
            }
            if (Math.abs(headingError) < 2) {
                rotationSpeed = headingError * turningDecel;
                turningDone = true;
            }
            speed = desiredSpeed - (ogDistance-distance)/(ogDistance*1.5)*0.5;
            telemetry.addData("speed",(ogDistance-distance)/(ogDistance*1.5)*desiredSpeed);
            if (distance < 50 || posDone == true) {
                speed = 0;
                posDone = true;
            }

            double velocity_x = (distanceToXTarget / distance) * (speed);
            double velocity_y = (distanceToYTarget / distance) * (speed);
            double theta = odo.getHeading();
            double vxR = velocity_x * Math.cos(theta) + velocity_y * Math.sin(theta);
            double vyR = -velocity_x * Math.sin(theta) + velocity_y * Math.cos(theta);
            setDriveSpeed(vxR, vyR, rotationSpeed);

            if (posDone && turningDone) break;

            do_instructions();
            check_vertical_pos();
        }
        current_yaw = desiredOrientation;

        // Stop the robot
        setDriveSpeed(0, 0, 0);
    }

    public void setDriveSpeed(double vxR, double vyR, double rotationSpeed) {
        double frontLeft = vxR + vyR;
        double frontRight = -vxR + vyR;
        double backLeft = vxR - vyR;
        double backRight = -vxR - vyR;

        double maxMagnitude = Math.max(1.0, Math.max(Math.abs(frontLeft),
                Math.max(Math.abs(frontRight), Math.max(Math.abs(backLeft), Math.abs(backRight)))));

        frontLeft /= maxMagnitude;
        frontRight /= maxMagnitude;
        backLeft /= maxMagnitude;
        backRight /= maxMagnitude;

        LeftFrontMotor.setPower(-frontLeft + rotationSpeed);
        RightFrontMotor.setPower(frontRight + rotationSpeed);
        LeftBackMotor.setPower(backLeft + rotationSpeed);
        RightBackMotor.setPower(-backRight + rotationSpeed);
    }
    public void rotateToHeading(double desiredOrientation) {
        double currentHeading;
        double headingError;
        double rotationSpeed;

        while (opModeIsActive()) {
            odo.update();
            currentHeading = normaliseDegrees(Math.toDegrees(odo.getHeading()));
            headingError = normaliseDegrees(desiredOrientation - currentHeading);
            if (Math.abs(headingError) < 2) {
                break;
            }

            double turningDecel = 0.01;

            if (headingError > 0) {
                rotationSpeed = 0.7 + headingError * (turningDecel);
            } else {
                rotationSpeed = -0.7 + headingError * (turningDecel);
            }
            setRotationSpeed(rotationSpeed);
            do_instructions();
        }

        setDriveSpeed(0, 0, 0);
    }
    public void setRotationSpeed(double rotationSpeed) {
        double frontLeft = rotationSpeed;
        double frontRight = rotationSpeed;
        double backLeft = rotationSpeed;
        double backRight = rotationSpeed;

        LeftFrontMotor.setPower(frontLeft);
        RightFrontMotor.setPower(frontRight);
        LeftBackMotor.setPower(backLeft);
        RightBackMotor.setPower(backRight);
    }

}

