package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import static org.firstinspires.ftc.teamcode.Constants.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

@TeleOp(name = "MAKE_AUTO")
public class MakeAuto extends LinearOpMode {
    RobotHardware robot = new RobotHardware();
    MecanumMovement mecanumMovement;

    AutoMovement autoMovement;
    FtcDashboard dashboard = FtcDashboard.getInstance();
    public ArrayList<String> instructions = new ArrayList<>();



    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        mecanumMovement = new MecanumMovement(robot);
        autoMovement = new AutoMovement(robot, dashboard);
        waitForStart();
        while (opModeIsActive()) {
            TelemetryPacket packet = new TelemetryPacket();
            packet.fieldOverlay()
                    .setFill("blue")
                    .fillRect(robot.odo.getPosY()/25.4 - 129, -robot.odo.getPosX()/25.4 + 9, 18, 18);
            dashboard.sendTelemetryPacket(packet);
            controls();
            robot.do_instructions();
            robot.check_vertical_pos();
            mecanumMovement.mecanum_drive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
            String x = Double.toString(robot.odo.getPosX());
            String y = Double.toString(robot.odo.getPosY());
            String o = Double.toString(Math.toDegrees(robot.odo.getHeading()));
            if (gamepad1.crossWasPressed()) {
                String position = "POS " + x + " " + y +  " false false " + o;
                instructions.add(position);
            }
            if (gamepad1.circleWasPressed()) {
                String position = "POS " + x + " " + y  +" true false " + o;
                instructions.add(position);
            }
            telemetry.addData("Instructions", instructions);
            telemetry.update();
            if (gamepad1.squareWasPressed()) {
                try {
                    writeToFile(instructions);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (gamepad1.triangleWasPressed()) {
                try{
                    ArrayList<String []> path = scanGeneratedAuto();
                    runGeneratedAuto(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (gamepad1.rightBumperWasPressed()) {
                String position = "POS " + x + " " + y + " true" + " true " + o;
                instructions.add(position);
            }
        }
    }
    public void writeToFile(ArrayList<String> list) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("/sdcard/FIRST/GeneratedAuto.txt"));
        for (String item : list) {
            writer.write(item);
            writer.newLine();
        }
        writer.close();
    }
    public ArrayList<String []> scanGeneratedAuto() throws IOException {
        Scanner scanner = new Scanner(new File("/sdcard/FIRST/GeneratedAuto.txt"));
        ArrayList<String []> path = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (!line.isEmpty()) {
                String[] parts = line.split("\\s+"); // split by any whitespace

                if (parts.length >= 2) {
                    try {
                        path.add(parts);
                    } catch (NumberFormatException e) {
                        telemetry.addLine("Invalid number format in line: " + line);
                        telemetry.update();
                    }
                } else {
                    telemetry.addLine("Invalid line: " + line);
                    telemetry.update();
                }
            }
        }
        scanner.close();
        return path;
    }

    public void runGeneratedAuto(ArrayList<String []> path) throws InterruptedException {
        ArrayList<String []> copy_path = new ArrayList<>(path);
        for (String [] instruction: path) {
            if (instruction[0].equals("POS")) {
                double x = -Double.parseDouble(instruction[2]);
                double y = Double.parseDouble(instruction[1]);
                boolean decelerate = Boolean.parseBoolean(instruction[3]);
                boolean accurate = Boolean.parseBoolean(instruction[4]);
                double heading = Double.parseDouble(instruction[5]);
                double speed = Double.parseDouble(instruction[6]);
                double cx = x;
                double cy = y;
                for (String[] b : copy_path ) {
                    if (b[0].equals("POS")) {
                        if (Boolean.parseBoolean(b[3]) || Boolean.parseBoolean(b[4])) {
                            cx = -Double.parseDouble(b[2]);
                            cy = Double.parseDouble(b[1]);
                            break;
                        }
                    }
                }
                autoMovement.goToPosition(x, y, speed, heading, decelerate, cx, cy, accurate);
            } else {
                if (instruction[1].equals("hsslides_out_auto")) {robot.hsslides_out_auto();}
                if (instruction[1].equals("transfer_sample_auto")) {robot.grab_and_transfer_sample_auto();}
                if (instruction[1].equals("drop_preload")) {robot.drop_preload();}
                if (instruction[1].equals("to_pick_up_spec")) {robot.to_pick_up_spec();}
                if (instruction[1].equals("to_clip")) {robot.to_clip();}
                if (instruction[1].equals("grab_sample")) {robot.grab_sample();}
                if (instruction[1].equals("let_sample")) {robot.let_sample();}
                if (instruction[1].equals("preload_spec")) {robot.preload_spec();}
            }
            copy_path.remove(instruction);
        }
    }
    public void controls() throws InterruptedException {
//        if (gamepad1.dpadLeftWasPressed()) {
//            robot.hsslides_out_auto();
//            String action = "ACT hsslides_out_auto";
//            instructions.add(action);
//        }
//        if (gamepad1.dpadRightWasPressed()) {
//            robot.grab_and_transfer_sample_auto();
//            String action = "ACT transfer_sample_auto";
//            instructions.add(action);
//        }
//        if (gamepad1.dpadDownWasPressed()) {
//            robot.drop_preload();
//            String action = "ACT drop_preload";
//            instructions.add(action);
//        }
        if (gamepad1.dpadLeftWasPressed()) {
            robot.to_pick_up_spec();
            String action = "ACT to_pick_up_spec";
            instructions.add(action);
        }
        if (gamepad1.dpadRightWasPressed()) {
            robot.to_clip();
            String action = "ACT to_clip";
            instructions.add(action);
        }
        if (gamepad1.dpadDownWasPressed()) {
            robot.grab_sample();
            String action = "ACT grab_sample";
            instructions.add(action);
        }
        if (gamepad1.dpadUpWasPressed()) {
            robot.hsslides_out_auto();
            String action = "ACT hsslides_out_auto";
            instructions.add(action);
        }
        if (gamepad1.leftBumperWasPressed()) {
            robot.let_sample();
            String action = "ACT let_sample";
            instructions.add(action);
        }
        if (gamepad1.leftStickButtonWasPressed()) {
            robot.preload_spec();
            String action = "ACT preload_spec";
            instructions.add(action);
        }
    }
}
