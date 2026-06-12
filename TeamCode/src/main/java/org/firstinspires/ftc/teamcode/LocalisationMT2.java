package org.firstinspires.ftc.teamcode;

import com.pedropathing.ftc.InvertedFTCCoordinates;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

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

public abstract class LocalisationMT2 extends OpMode { //abstract means that it can be used to extend in any other class

    public Limelight3A limelight; // class scope variable of the limelight

    public com.pedropathing.follower.Follower follower;

    public Pose actualPose = new Pose(0, 0);

    public void initLocalisationHardware() { //method to use in the init of the other class
        limelight = hardwareMap.get(Limelight3A.class, "limelight"); // this searches the Control Hub's config. profile to find the device. The deviceName should be exactly as named in the profile
        limelight.pipelineSwitch(8); // switches to the correct configured pipeline. I have used 8 for all April Tags
    }

    public void startLocalisation() { // required method for the limelight
        limelight.start(); // starts up the limelight. If there is delay, move to init() method
    }

    public void updateLocalisation() { //method to use in the loop other class

        double headingDegrees = Math.toDegrees(follower.getHeading());

        // Add the 180 offset, then normalize it to wrap properly between -180 and +180
        double normalizedHeading = AngleUnit.normalizeDegrees(headingDegrees);

        limelight.updateRobotOrientation(normalizedHeading); // feed the clamped, flipped heading
        telemetry.addData("Heading (deg)", normalizedHeading);

        LLResult llResult = limelight.getLatestResult();

        if (llResult != null && llResult.isValid()) {
            Pose3D botpose = llResult.getBotpose_MT2();

            double limelightX = botpose.getPosition().x * 100;
            double limelightY = botpose.getPosition().y * 100;

            telemetry.addData("Limelight X (cm)", limelightX); //sends all telemetry to the driver station
            telemetry.addData("Limelight Y (cm)", limelightY);

            // --- THE TRANSLATION LOGIC ---
            // 1. Pack the raw CM coordinates. Hardcode heading to 0 to prevent conversion issues.
            Pose2D rawVisionPose = new Pose2D(DistanceUnit.CM, limelightX, limelightY, AngleUnit.DEGREES, headingDegrees);

            // 2. Convert to FTC standard map, then to Pedro's corner map
            Pose ftcStandard = PoseConverter.pose2DToPose(rawVisionPose, InvertedFTCCoordinates.INSTANCE);
            Pose convertedPedroPose = ftcStandard.getAsCoordinateSystem(PedroCoordinates.INSTANCE);

            // 3. Extract purely X and Y to strip out any heading data entirely
            actualPose = new Pose(convertedPedroPose.getX(), convertedPedroPose.getY());

            // 4. Send translated data to driver hub
            telemetry.addData("Pedro Converted X (in)", actualPose.getX());
            telemetry.addData("Pedro Converted Y (in)", actualPose.getY());
        }
    }
}
