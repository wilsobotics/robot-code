package org.firstinspires.ftc.teamcode;

import com.pedropathing.ftc.InvertedFTCCoordinates;
import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import com.pedropathing.geometry.Pose;
import com.pedropathing.ftc.PoseConverter;
import com.pedropathing.geometry.PedroCoordinates;

import dev.nextftc.core.subsystems.Subsystem;


// 2. Drop "extends NextFTCOpMode" and use "implements Subsystem" instead
public class LocalisationMT2 implements Subsystem {

    public Limelight3A limelight;
    public Follower follower;

    // You have to declare Telemetry bc a Subsystem doesn't magically have it
    public Telemetry telemetry;

    public Pose actualPose = new Pose(0, 0);

    // 3. THIS IS THE CONSTRUCTOR INJECTION
    // We deleted "initLocalisationHardware()" and replaced it with this block.
    // Notice how it demands hardwareMap, telemetry, and follower right when it is created.
    public LocalisationMT2(HardwareMap hardwareMap, Telemetry telemetry, Follower follower) {
        // Save the injected tools to the class variables so the rest of the file can use them
        this.follower = follower;
        this.telemetry = telemetry;

        // Now use the injected hardwareMap to find the Limelight
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(8);
        limelight.start(); // Start it immediately here so we don't need a separate start() method
    }

    // 4. Rename "updateLocalisation()" to "periodic()"
    // NextFTC will automatically run this method every loop
    @Override
    public void periodic() {

        double headingDegrees = Math.toDegrees(follower.getHeading());

        double normalizedHeading = AngleUnit.normalizeDegrees(headingDegrees);

        limelight.updateRobotOrientation(normalizedHeading);
        telemetry.addData("Heading (deg)", normalizedHeading);

        LLResult llResult = limelight.getLatestResult();

        if (llResult != null && llResult.isValid()) {
            Pose3D botpose = llResult.getBotpose_MT2();

            double limelightX = botpose.getPosition().x * 100;
            double limelightY = botpose.getPosition().y * 100;

            telemetry.addData("Limelight X (cm)", limelightX);
            telemetry.addData("Limelight Y (cm)", limelightY);

            Pose2D rawVisionPose = new Pose2D(DistanceUnit.CM, limelightX, limelightY, AngleUnit.DEGREES, headingDegrees);

            Pose ftcStandard = PoseConverter.pose2DToPose(rawVisionPose, InvertedFTCCoordinates.INSTANCE);
            Pose convertedPedroPose = ftcStandard.getAsCoordinateSystem(PedroCoordinates.INSTANCE);

            actualPose = new Pose(convertedPedroPose.getX(), convertedPedroPose.getY());

            telemetry.addData("Pedro Converted X (in)", actualPose.getX());
            telemetry.addData("Pedro Converted Y (in)", actualPose.getY());
        }
    }
}