package org.firstinspires.ftc.teamcode;

import dev.nextftc.ftc.NextFTCOpMode;

import com.pedropathing.follower.Follower;
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

public abstract class Localisation extends NextFTCOpMode { //abstract means that it can be used to extend in any other class


    public Limelight3A limelight; // class scope variable of the limelight
//    public GoBildaPinpointDriver pinpoint; // same thing for pinpoint

    public Pose currentPedroPose = null; // Stores the active Pedro pose to avoid re-calculating

    public void initLocalisationHardware() { //method to use in the init of the other class
        limelight = hardwareMap.get(Limelight3A.class, "limelight"); // this searches the Control Hub's config. profile to find the device. The deviceName should be exactly as named in the profile
        limelight.pipelineSwitch(8); // switches to the correct configured pipeline. I have used 8 for all April Tags

        //pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint"); // same thing as limelight, just for pinpoint now
        //pinpoint.resetPosAndIMU(); //reset position and calibrate IMU for accuracy  NEED TO FIND A WAY TO DO THIS SAFELY
    }

    public void startLocalisation() { // required method for the limelight
        limelight.start(); // starts up the limelight. If there is delay, move to init() method
    }

    public void updateLocalisation(Follower follower) { //method to use in the loop other class

        //pinpoint.update(); // update for latest readings
        //double headingRadians = pinpoint.getHeading(AngleUnit.RADIANS); //get the heading, returns in radians
        //double headingDegrees = Math.toDegrees(headingRadians); // convert radians to degrees
        double headingDegrees = Math.toDegrees(follower.getHeading());

        limelight.updateRobotOrientation(headingDegrees); // feed the heading to the limelight

        LLResult llResult = limelight.getLatestResult();

        if(llResult != null && llResult.isValid()){
            Pose3D botpose = llResult.getBotpose_MT2();
            telemetry.addData("Tx", llResult.getTx()); // horizontal angle from center of camera lens to center of AprilTag
            telemetry.addData("Ty", llResult.getTy()); // vertical angle from center of camera lens to Tag
            telemetry.addData("Ta", llResult.getTa()); // percentage of image that the Tag takes, if closer, then Ta larger, if further, Ta smaller

            double limelightXInCentimeters = botpose.getPosition().x * 100; // gets the limelight x position and converts into centimeters
            double limelightYInCentimeters = botpose.getPosition().y * 100; // gets the limelight y position and converts into centimeters

            telemetry.addData("Limelight X (cm)", limelightXInCentimeters); //sends all telemetry to the driver station
            telemetry.addData("Limelight Y (cm)", limelightYInCentimeters);
            telemetry.addData("Limelight Heading (deg)", headingDegrees);

            double pinpointX = limelightXInCentimeters - 182.88; // changes the x position to account for the differences in limelight and pinpoint positioning
            double pinpointY = limelightYInCentimeters - 182.88; // limelight 0,0 is at the blue goal while pinpoint 0,0 is at the centre

            //pinpoint.setPosition(new Pose2D(DistanceUnit.CM, pinpointX, pinpointY, AngleUnit.DEGREES, headingDegrees));
            //updates pinpoint pose also updates the heading because the heading could've changed, meaning that the pose would be from different timings

            telemetry.addData("Pinpoint X (cm)", pinpointX); //sends all telemetry to the driver station
            telemetry.addData("Pinpoint Y (cm)", pinpointY);
            telemetry.addData("Pinpoint Heading (deg)", headingDegrees);

            double limelightXInInches = botpose.getPosition().x * 39.37; // meters to inches
            double limelightYInInches = botpose.getPosition().y * 39.37; // meters to inches

            // Limelight (Blue Goal Origin) to FTC Standard (Center Origin)
            double ftcX = limelightXInInches - 70.866; // Shift 1.8m (70.866in) to center
            double ftcY = limelightYInInches - 70.866; // Shift 1.8m (70.866in) to center

            // Generate the FTC Pose and convert to Pedro Coordinates (0-144, center 72,72)
            Pose2D ftcPose = new Pose2D(DistanceUnit.INCH, ftcX, ftcY, AngleUnit.DEGREES, headingDegrees); // x/y/heading is created to be ftc standard
            Pose ftcStandard = PoseConverter.pose2DToPose(ftcPose, FTCCoordinates.INSTANCE); // flags the line above as the ftc standard
            currentPedroPose = ftcStandard.getAsCoordinateSystem(PedroCoordinates.INSTANCE); // maps ftc standard to the pedro corner origin

        }

    }

    public Pose getPedroLocation(){ // method to use to get the co-ordinates that pedro supports
        telemetry.addData("Pedro X (in)", currentPedroPose.getX());
        telemetry.addData("Pedro Y (in)", currentPedroPose.getY());
        return currentPedroPose;
    }
}