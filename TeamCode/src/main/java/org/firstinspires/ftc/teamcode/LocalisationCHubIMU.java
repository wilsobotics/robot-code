package org.firstinspires.ftc.teamcode;

import com.pedropathing.ftc.InvertedFTCCoordinates;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import com.pedropathing.geometry.Pose;
import com.pedropathing.ftc.PoseConverter;
import com.pedropathing.ftc.FTCCoordinates;
import com.pedropathing.geometry.PedroCoordinates;

/* How to use this class:
1. In another class, extend Localisation
2. Use the method "initLocalisationHardware()" in the init() of the OpMode
3. Use the "startLocalisation()" between the init() and loop() in the start()
4. Use "updateLocalisation()" in the loop() of the OpMode
5. Use "getPedroLocation()" wherever you want pedro coordinates
 */

public abstract class LocalisationCHubIMU extends OpMode {

    public Limelight3A limelight;
    public com.pedropathing.follower.Follower follower;

    public IMU controlHubImu;

    public Pose actualPose = new Pose(0, 0);

    public void initLocalisationHardware() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(8);

        // --- CONTROL HUB IMU SETUP ---
        controlHubImu = hardwareMap.get(IMU.class, "imu");

        // Orientation locked in: Logo RIGHT, USB UP
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        ));

        controlHubImu.initialize(parameters);
        controlHubImu.resetYaw(); // Zero the gyro on init
    }

    public void startLocalisation() {
        limelight.start();
    }

    public void updateLocalisation() {

        // Grab the heading directly from the Control Hub IMU
        double headingDegrees = controlHubImu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

        // Normalize it to wrap properly between -180 and +180
        double normalizedHeading = AngleUnit.normalizeDegrees(headingDegrees);

        limelight.updateRobotOrientation(normalizedHeading);
        telemetry.addData("Hub Heading (deg)", normalizedHeading);

        LLResult llResult = limelight.getLatestResult();

        if (llResult != null && llResult.isValid()) {
            Pose3D botpose = llResult.getBotpose_MT2();

            double limelightX = botpose.getPosition().x * 100;
            double limelightY = botpose.getPosition().y * 100;

            telemetry.addData("Limelight X (cm)", limelightX);
            telemetry.addData("Limelight Y (cm)", limelightY);

            // --- THE TRANSLATION LOGIC ---
            // Pack the raw CM coordinates. Hardcode heading to 0.
            Pose2D rawVisionPose = new Pose2D(DistanceUnit.CM, limelightX, limelightY, AngleUnit.DEGREES, headingDegrees);

            // Convert to FTC standard map, then to Pedro's corner map
            Pose ftcStandard = PoseConverter.pose2DToPose(rawVisionPose, InvertedFTCCoordinates.INSTANCE);
            Pose convertedPedroPose = ftcStandard.getAsCoordinateSystem(PedroCoordinates.INSTANCE);

            // Extract purely X and Y to strip out any heading data entirely
            actualPose = new Pose(convertedPedroPose.getX(), convertedPedroPose.getY());

            // Send translated data to driver hub
            telemetry.addData("Pedro Converted X (in)", actualPose.getX());
            telemetry.addData("Pedro Converted Y (in)", actualPose.getY());
        }
    }
}