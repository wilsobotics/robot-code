package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * This OpMode illustrates the concept of driving a path based on time.
 * The code is structured as a LinearOpMode
 *
 * The code assumes that you do NOT have encoders on the wheels,
 *   otherwise you would use: RobotAutoDriveByEncoder;
 *
 *   The desired path in this example is:
 *   - Drive forward for 3 seconds
 *   - Spin right for 1.3 seconds
 *   - Drive Backward for 1 Second
 *
 *  The code is written in a simple form with no optimizations.
 *  However, there are several ways that this type of sequence could be streamlined,
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@Autonomous(name="autos.Auto RP", group="Robot")
public class AutoDECODE extends LinearOpMode {

    /* Declare OpMode members. */
    private DcMotor lfm   = null;
    private DcMotor rfm  = null;
    private DcMotor lbm   = null;
    private DcMotor rbm  = null;

    private ElapsedTime     runtime = new ElapsedTime();

    static final double     FORWARD_SPEED = 0.6;

    @Override
    public void runOpMode() {

        // Initialize the drive system variables.
        lfm  = hardwareMap.get(DcMotor.class, "front_left");
        rfm = hardwareMap.get(DcMotor.class, "front_right");
        lbm  = hardwareMap.get(DcMotor.class, "rear_left");
        rbm = hardwareMap.get(DcMotor.class, "rear_right");

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // When run, this OpMode should start both motors driving forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        lbm.setDirection(DcMotor.Direction.REVERSE);
        lfm.setDirection(DcMotor.Direction.REVERSE); // ???

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();

        // Step through each leg of the path, ensuring that the OpMode has not been stopped along the way.

        // Step 1:  Drive forward for 2 seconds
        lfm.setPower(FORWARD_SPEED);
        rfm.setPower(FORWARD_SPEED);
        lbm.setPower(FORWARD_SPEED);
        rbm.setPower(FORWARD_SPEED);
        runtime.reset();

        while (opModeIsActive() && (runtime.seconds() < 0.7)) {
            telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        // Step 4:  Stop
        lfm.setPower(0);
        rfm.setPower(0);
        lbm.setPower(0);
        rbm.setPower(0);

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }
}