package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Constants.*;
import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;

public class RobotHardware {
    public DcMotor leftFront, rightFront, leftBack, rightBack, vyperslide1, vyperslide2, righths;
    public Servo hsclaw, hspitch, hsyaw, vsclaw, vsyaw, rightarm, leftarm;
    public GoBildaPinpointDriver odo;
    public ElapsedTime myElapsedTime;
    public ArrayList<String> instructions = new ArrayList<>();
    public boolean disable_slides, vsactive = false;
    public double vspower, vsdistance;
    public int vstargetpos;

    public void init(HardwareMap hardwareMap) {
        leftFront = hardwareMap.get(DcMotor.class, "LeftFrontMotor");
        rightFront = hardwareMap.get(DcMotor.class, "RightFrontMotor");
        leftBack = hardwareMap.get(DcMotor.class, "LeftBackMotor");
        rightBack = hardwareMap.get(DcMotor.class, "RightBackMotor");

        hsclaw = hardwareMap.get(Servo.class, "hs claw");
        hspitch = hardwareMap.get(Servo.class, "hs pitch");
        hsyaw = hardwareMap.get(Servo.class, "hs yaw");
        vsclaw = hardwareMap.get(Servo.class, "vs claw");
        righths = hardwareMap.get(DcMotor.class, "righths");
        vsyaw = hardwareMap.get(Servo.class, "vs yaw");
        leftarm = hardwareMap.get(Servo.class, "leftarm");
        rightarm = hardwareMap.get(Servo.class, "rightarm");
        vyperslide1 = hardwareMap.get(DcMotor.class, "vyperslide1");
        vyperslide2 = hardwareMap.get(DcMotor.class, "vyperslide2");
        vyperslide1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        vyperslide2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        vyperslide1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        vyperslide2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        myElapsedTime = new ElapsedTime();
        myElapsedTime.reset();

        odo = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        odo.setOffsets(-74, 0);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.resetPosAndIMU();
    }
    public void update() {
        check_vertical_pos();
        do_instructions();
    }
    public double time() {
        return myElapsedTime.milliseconds();
    }

    public void set_vertical_pos(int pos) {
        vstargetpos = pos;
        vsactive = true;
    }

    public double apply_deceleration() {
        vsdistance = vyperslide2.getCurrentPosition() - vstargetpos;
        return Math.max(Math.abs(vsdistance) / VS_DECELERATION_LIMIT, MIN_VS_SPEED);
    }

    public void check_vertical_pos() {
         vspower = apply_deceleration() * (Math.abs(vsdistance) / vsdistance);
         if (vspower < 0) {
             vspower -= EXTRA_VS_UP_POWER;
         } else {
             vspower -= EXTRA_VS_UP_POWER/3;
         }
         vyperslide1.setPower(vspower);
         vyperslide2.setPower(vspower);
    }

    public void do_instructions() {
        int len = instructions.size();
        double time = time();
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
                    hspitch.setPosition(p);
                }
                if (component.equals("righths")) {
                    righths.setPower(p);
                }
                instructions.remove(0);
            }
        }
    }
    public void hsslides_out() {
        myElapsedTime.reset();
        set_vertical_pos(VS_TRANSFER);
        leftarm.setPosition(LEFTARM_TRANSFER);
        rightarm.setPosition(RIGHTARM_TRANSFER);
        vsclaw.setPosition(VSCLAW_OPEN);
        hsyaw.setPosition(HSYAW_STRAIGHT);
        vsyaw.setPosition(VSYAW_TRANSFER);
        righths.setPower(1);
        hsclaw.setPosition(HSCLAW_OPEN);
        hspitch.setPosition(HSPITCH_DOWN);
        hsyaw.setPosition(HSYAW_STRAIGHT);
        instructions.add("500 righths 0.4");
    }
    public void hsyaw_left() {
        hsyaw.setPosition(HSYAW_LEFT);
    }
    public void hsyaw_right() {
        hsyaw.setPosition(HSYAW_RIGHT);
    }
    public void reset() {
        righths.setPower(-0.5);
        set_vertical_pos(0);
        vsclaw.setPosition(VSCLAW_OPEN);
        leftarm.setPosition(LEFTARM_CLIP);
        rightarm.setPosition(RIGHTARM_CLIP);
    }
    public void hsslides_out_auto() {
        myElapsedTime.reset();
        set_vertical_pos(VS_TRANSFER);
        leftarm.setPosition(LEFTARM_TRANSFER);
        rightarm.setPosition(RIGHTARM_TRANSFER);
        vsclaw.setPosition(VSCLAW_OPEN);
        hsyaw.setPosition(HSYAW_STRAIGHT);
        vsyaw.setPosition(VSYAW_TRANSFER);
        righths.setPower(1);
        hsclaw.setPosition(HSCLAW_OPEN);
        hspitch.setPosition(HSPITCH_DOWN);
        hsyaw.setPosition(0.8);
        instructions.add("500 righths 0.4");
    }
    public void grab_and_transfer_sample_auto() throws InterruptedException {
        hsclaw.setPosition(HSCLAW_CLOSED);
        sleep(400);
        myElapsedTime.reset();
        hsyaw.setPosition(HSYAW_STRAIGHT);
        hspitch.setPosition(HSPITCH_TRANSFER);
        righths.setPower(-1);
        instructions.add("750 vsclaw 1");
        instructions.add("750 righths -0.3");
        instructions.add("850 hsclaw 0");
        instructions.add("850 vs 2600");
        instructions.add("1200 leftarm 0.69");
        instructions.add("1200 rightarm 0.4");
        instructions.add("1200 vsyaw 0.2");
        while (time() < 2000) {
            do_instructions();
            check_vertical_pos();
        }
    }
    public void drop_preload() throws InterruptedException {
        myElapsedTime.reset();
        instructions.add("0 vsclaw 1");
        instructions.add("0 vs 2600");
        instructions.add("500 leftarm 0.69");
        instructions.add("500 rightarm 0.4");
        instructions.add("500 vsyaw 0.2");
        while (time() < 500) {
            do_instructions();
            check_vertical_pos();
        }
    }
    public void to_clip() throws InterruptedException {
        vsclaw.setPosition(1);
        sleep(300);
        vsyaw.setPosition(0.4);
        leftarm.setPosition(0.35);
        rightarm.setPosition(0.7);
        set_vertical_pos(1480);
    }
    public void to_pick_up_spec() {
        myElapsedTime.reset();
        vsclaw.setPosition(0.8);
        if (righths.getPower() > 0) {righths.setPower(1);}
        instructions.add("0 leftarm 1");
        instructions.add("0 rightarm 0.1");
        instructions.add("0 vsyaw 0.05");
        instructions.add("0 vs 350");
        instructions.add("700 righths -0.3");
    }
    public void grab_sample() throws InterruptedException {
        hsclaw.setPosition(HSCLAW_CLOSED);
        sleep(400);
    }
    public void let_sample() throws InterruptedException {
        hsclaw.setPosition(HSCLAW_OPEN);
        sleep(200);
    }
    public void preload_spec() throws InterruptedException {
        vsclaw.setPosition(1);
        myElapsedTime.reset();
        while (time() < 500) {
            do_instructions();
            check_vertical_pos();
        }
        vsyaw.setPosition(0.4);
        leftarm.setPosition(0.35);
        rightarm.setPosition(0.7);
        set_vertical_pos(1460);
        while (time() < 800) {
            do_instructions();
            check_vertical_pos();
        }
    }
}