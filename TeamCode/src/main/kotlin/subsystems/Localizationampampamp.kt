//package subsystems
//
//import KtConstants
//import com.pedropathing.ftc.FTCCoordinates
//import com.pedropathing.ftc.PoseConverter
//import com.pedropathing.geometry.PedroCoordinates
//import com.pedropathing.geometry.Pose
//import com.qualcomm.hardware.limelightvision.Limelight3A
//import dev.nextftc.core.commands.utility.LambdaCommand
//import dev.nextftc.core.subsystems.Subsystem
//import dev.nextftc.ftc.ActiveOpMode.hardwareMap
//import dev.nextftc.hardware.impl.MotorEx
//import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
//import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
//import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
//import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
//import dev.nextftc.ftc.ActiveOpMode.telemetry
//
//object Localizationampampamp : Subsystem {
//    val limelight = hardwareMap.get<Limelight3A?>(
//        Limelight3A::class.java,
//        "limelight"
//    )!!
//    var currentPedroPose: Pose? = null
//    override fun initialize() {
//        limelight.start()
//        limelight.pipelineSwitch(0)
//    }
//
//    override fun periodic() {
//
//
//        //method to use in the loop other class
//
//        //pinpoint.update(); // update for latest readings
//        //double headingRadians = pinpoint.getHeading(AngleUnit.RADIANS); //get the heading, returns in radians
//        //double headingDegrees = Math.toDegrees(headingRadians); // convert radians to degrees
//        val headingDegrees = Math.toDegrees(follower.getHeading())
//
//        limelight.updateRobotOrientation(headingDegrees) // feed the heading to the limelight
//
//        val llResult = limelight.getLatestResult()
//
//        if (llResult != null && llResult.isValid()) {
//            val botpose = llResult.getBotpose_MT2()
//            telemetry.addData(
//                "Tx",
//                llResult.getTx()
//            ) // horizontal angle from center of camera lens to center of AprilTag
//            telemetry.addData(
//                "Ty",
//                llResult.getTy()
//            ) // vertical angle from center of camera lens to Tag
//            telemetry.addData(
//                "Ta",
//                llResult.getTa()
//            ) // percentage of image that the Tag takes, if closer, then Ta larger, if further, Ta smaller
//
//            val limelightXInCentimeters =
//                botpose.getPosition().x * 100 // gets the limelight x position and converts into centimeters
//            val limelightYInCentimeters =
//                botpose.getPosition().y * 100 // gets the limelight y position and converts into centimeters
//
//            telemetry.addData(
//                "Limelight X (cm)",
//                limelightXInCentimeters
//            ) //sends all telemetry to the driver station
//            telemetry.addData("Limelight Y (cm)", limelightYInCentimeters)
//            telemetry.addData("Limelight Heading (deg)", headingDegrees)
//
//            val pinpointX =
//                limelightXInCentimeters - 182.88 // changes the x position to account for the differences in limelight and pinpoint positioning
//            val pinpointY =
//                limelightYInCentimeters - 182.88 // limelight 0,0 is at the blue goal while pinpoint 0,0 is at the centre
//
//            //pinpoint.setPosition(new Pose2D(DistanceUnit.CM, pinpointX, pinpointY, AngleUnit.DEGREES, headingDegrees));
//            //updates pinpoint pose also updates the heading because the heading could've changed, meaning that the pose would be from different timings
//            telemetry.addData(
//                "Pinpoint X (cm)",
//                pinpointX
//            ) //sends all telemetry to the driver station
//            telemetry.addData("Pinpoint Y (cm)", pinpointY)
//            telemetry.addData("Pinpoint Heading (deg)", headingDegrees)
//
//            val limelightXInInches = botpose.getPosition().x * 39.37 // meters to inches
//            val limelightYInInches = botpose.getPosition().y * 39.37 // meters to inches
//
//            // Limelight (Blue Goal Origin) to FTC Standard (Center Origin)
//            val ftcX = limelightXInInches - 70.866 // Shift 1.8m (70.866in) to center
//            val ftcY = limelightYInInches - 70.866 // Shift 1.8m (70.866in) to center
//
//            // Generate the FTC Pose and convert to Pedro Coordinates (0-144, center 72,72)
//            val ftcPose = Pose2D(
//                DistanceUnit.INCH,
//                ftcX,
//                ftcY,
//                AngleUnit.DEGREES,
//                headingDegrees
//            ) // x/y/heading is created to be ftc standard
//            val ftcStandard = PoseConverter.pose2DToPose(
//                ftcPose,
//                FTCCoordinates.INSTANCE
//            ) // flags the line above as the ftc standard
//            currentPedroPose =
//                ftcStandard.getAsCoordinateSystem(PedroCoordinates.INSTANCE) // maps ftc standard to the pedro corner origin
//        }
//}}