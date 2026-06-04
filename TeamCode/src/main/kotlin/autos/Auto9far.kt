//package autos
//import KtConstants.Companion.side
//import com.pedropathing.geometry.BezierCurve
//import com.pedropathing.geometry.BezierLine
//import com.pedropathing.geometry.Pose
//import com.pedropathing.paths.PathChain
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous
//import dev.nextftc.core.commands.Command
//import dev.nextftc.core.commands.delays.Delay
//import dev.nextftc.core.commands.groups.SequentialGroup
//import dev.nextftc.core.components.SubsystemComponent
//import dev.nextftc.extensions.pedro.FollowPath
//import dev.nextftc.extensions.pedro.PedroComponent
//import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
//import dev.nextftc.ftc.NextFTCOpMode
//import dev.nextftc.ftc.components.BulkReadComponent
//import dev.nextftc.hardware.positionable.SetPosition
//import dev.nextftc.hardware.powerable.SetPower
//import org.firstinspires.ftc.teamcode.pedroPathing.Constants
//import subsystems.Shooter
//import subsystems.Transfer
//
//
//@Autonomous(name = "9 FAR")
//class Auto9far : NextFTCOpMode() {
//    init {
//        addComponents(
//            SubsystemComponent(Shooter, Transfer),
//            BulkReadComponent,
//            PedroComponent(Constants::createFollower)
//        )
//    }
//
//    // PathChain definitions in requested order
//    private lateinit var intakeThirdSpike: PathChain
//    private lateinit var shootSecond: PathChain
//    private lateinit var intakeLoadingFirst: PathChain
//    private lateinit var ensureLoadingBack: PathChain
//    private lateinit var ensureLoadingFront: PathChain
//    private lateinit var shootThird: PathChain
//    private lateinit var intakeLoadingSecond: PathChain
//    private lateinit var shootFourth: PathChain
//    private lateinit var leave: PathChain
//
//    val intakeTime = 0.5 // Updated to 0.5s
//    val waitTime = 0.3
//    val shootTime = 1.0
//    val kickTime = 1.0
//
//    val startPose = side(Pose(80.0, 8.0, Math.toRadians(0.0)))
//    val defaultHeading = side(Pose(0.0, 0.0, Math.toRadians(0.0))).heading
//    val thirdSpikePos = side(Pose(133.0, 41.0))
//    val shootPos = side(Pose(85.0, 20.0))
//    val loadingZonePos = Pose(133.0, 10.0)
//    val ensureLoadingPos = side(Pose(125.0, 10.0))
//    val leavePos = side(Pose(90.0, 30.0))
//
//
//
//    val shoot = SequentialGroup(
//        Transfer.kickBallSafe,
//        Delay(kickTime),
//        Transfer.kickBallSafe,
//        Delay(kickTime),
//        Transfer.kickBallSafe,
//        Transfer.intake
//    )
//
//    val preShoot = SequentialGroup(
//        Shooter.turnFlywheelOn,
//        Transfer.preShoot,
//    )
//
//    override fun onInit() {
//        intakeThirdSpike = follower.pathBuilder()
//            .addPath(BezierCurve(startPose, side(Pose(100.591, 38.107)), thirdSpikePos))
//            .setConstantHeadingInterpolation(defaultHeading)
//            .build()
//
//        shootSecond = follower.pathBuilder()
//            .addPath(BezierLine(thirdSpikePos, shootPos))
//            .setConstantHeadingInterpolation(defaultHeading)
//            .build()
//
//        intakeLoadingFirst = follower.pathBuilder()
//            .addPath(BezierLine(shootPos, loadingZonePos))
//            .setConstantHeadingInterpolation(defaultHeading)
//            .build()
//
//        ensureLoadingBack = follower.pathBuilder()
//            .addPath(BezierLine(loadingZonePos, ensureLoadingPos))
//            .setConstantHeadingInterpolation(defaultHeading)
//            .build()
//
//        ensureLoadingFront = follower.pathBuilder()
//            .addPath(BezierLine(ensureLoadingPos, loadingZonePos))
//            .setConstantHeadingInterpolation(defaultHeading)
//            .build()
//
//        shootThird = follower.pathBuilder()
//            .addPath(BezierLine(loadingZonePos, shootPos))
//            .setConstantHeadingInterpolation(defaultHeading)
//            .build()
//
//        intakeLoadingSecond = follower.pathBuilder()
//            .addPath(BezierLine(shootPos, loadingZonePos))
//            .setConstantHeadingInterpolation(defaultHeading)
//            .build()
//
//        shootFourth = follower.pathBuilder()
//            .addPath(BezierLine(loadingZonePos, shootPos))
//            .setConstantHeadingInterpolation(defaultHeading)
//            .build()
//
//        leave = follower.pathBuilder()
//            .addPath(BezierLine(shootPos, leavePos))
//            .setConstantHeadingInterpolation(defaultHeading)
//            .build()
//    }
//
//    private val autonomousRoutine: Command
//        get() = SequentialGroup(
//            SetPower(Transfer.intakeMotor, 1.0),
//            preShoot,
//            SetPosition(Transfer.door, KtConstants.DOOR_OPEN),
//            Delay(3.0),
//            SetPosition(Transfer.door, KtConstants.DOOR_CLOSE),
//            shoot,
//
//            FollowPath(intakeThirdSpike),
//            Delay(intakeTime),
//            FollowPath(shootSecond),
//            shoot,
//
//            FollowPath(intakeLoadingFirst),
//            Delay(intakeTime),
//            FollowPath(ensureLoadingBack),
//            FollowPath(ensureLoadingFront),
//            Delay(intakeTime),
//            FollowPath(shootThird),
//            shoot,
//
//            FollowPath(leave)
//        )
//
//    override fun onStartButtonPressed() {
//        follower.setStartingPose(startPose)
//        Shooter.turretEnabled = true
//        autonomousRoutine()
//    }
//
//    override fun onUpdate() {
//        super.onUpdate()
//        KtConstants.ROBOT_X = follower.pose.x
//        KtConstants.ROBOT_Y = follower.pose.y
//        KtConstants.TURRET_POS = Shooter.turretEncoder.currentPosition
//        KtConstants.ROBOT_HEADING = follower.pose.heading
//    }
//}