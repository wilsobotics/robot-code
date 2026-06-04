//package autos
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
//import org.firstinspires.ftc.teamcode.pedroPathing.Constants
//import subsystems.Shooter
//import subsystems.Transfer
//
//@Autonomous(name = "12 AUTO greedy mode")
//class Auto12close : NextFTCOpMode() {
//    init {
//        addComponents(
//            SubsystemComponent(Shooter, Transfer),
//            BulkReadComponent,
//            PedroComponent(Constants::createFollower)
//        )
//    }
//
//    private lateinit var shootPreload: PathChain
//    private lateinit var intakeMiddleSpike: PathChain
//    private lateinit var openGate: PathChain
//    private lateinit var secondShoot: PathChain
//    private lateinit var intakeFirstSpike: PathChain
//    private lateinit var thirdShoot: PathChain
//    private lateinit var intakeThirdSpike: PathChain
//    private lateinit var fourthShoot: PathChain
//    private lateinit var leave: PathChain
//
//    val intakeTime = 0.3
//    val waitTime = 0.3
//    val shootTime = 0.8
//    val kickTime = 0.3
//
//
//    fun side(pose: Pose): Pose {
//        if (KtConstants.SIDE == "BLUE") {
//            val x = 144 - pose.x
//            val y = pose.y
//            val heading = Math.PI - pose.heading
//            return Pose(x, y, heading)
//        } else {
//            return pose
//        }
//    }
//
//    val startPose = side(Pose(127.0, 116.0, Math.toRadians(270.0)))
//    val shootPose = side(Pose(96.000, 92.000))
//    val gateOpenPose = side(Pose(131.0, 70.188))
//    val firstSpikePose = side(Pose(127.0, 82.000))
//    val middleSpikePose = side(Pose(133.000, 61.0))
//    val thirdSpikePose = side(Pose(133.0, 41.000))
//    val defaultHeading = side(Pose(0.0, 0.0, Math.toRadians(0.0))).heading
//
//
//
//    val shoot = SequentialGroup(
//        Delay(waitTime),
//        Transfer.shoot,
//        Delay(shootTime),
//        Transfer.kickBall,
//        Shooter.turnFlywheelOff,
//        Transfer.intake
//    )
//
//    val preShoot = SequentialGroup(
//        Shooter.turnFlywheelOn,
//        Transfer.preShoot,
//    )
//
//
//    override fun onInit() {
//        shootPreload = follower.pathBuilder()
//            .addPath(
//                BezierLine(
//                    startPose,
//                    shootPose
//                )
//            )
//            .setLinearHeadingInterpolation(startPose.heading, defaultHeading)
//            .build()
//
//        intakeMiddleSpike = follower.pathBuilder()
//            .addPath(
//                BezierCurve(
//                    shootPose,
//                    side(Pose(92.000, 58.700)),
//                    middleSpikePose
//                )
//            )
//            .setConstantHeadingInterpolation(defaultHeading)
//            .build()
//
//        openGate = follower.pathBuilder()
//            .addPath(
//                BezierCurve(
//                    middleSpikePose,
//                    side(Pose(107.910, 60.661)),
//                    gateOpenPose
//                )
//            )
//            .setConstantHeadingInterpolation(defaultHeading)
//            .build()
//
//        secondShoot = follower.pathBuilder()
//            .addPath(
//                BezierCurve(
//                    gateOpenPose,
//                    side(Pose(92.000, 58.700)),
//                    shootPose
//                )
//            )
//            .setConstantHeadingInterpolation(defaultHeading)
//            .build()
//
//        intakeFirstSpike = follower.pathBuilder()
//            .addPath(
//                BezierLine(
//                    shootPose,
//                    firstSpikePose
//                )
//            )
//            .setConstantHeadingInterpolation(defaultHeading)
//            .build()
//
//        thirdShoot = follower.pathBuilder()
//            .addPath(
//                BezierLine(
//                    firstSpikePose,
//                    shootPose
//                )
//            )
//            .setConstantHeadingInterpolation(defaultHeading)
//            .build()
//
//        intakeThirdSpike = follower.pathBuilder()
//            .addPath(
//                BezierCurve(
//                    shootPose,
//                    side(Pose(85.000, 29.000)),
//                    thirdSpikePose
//                )
//            )
//            .setConstantHeadingInterpolation(defaultHeading)
//            .build()
//
//        fourthShoot = follower.pathBuilder()
//            .addPath(
//                BezierLine(
//                    thirdSpikePose,
//                    shootPose
//                )
//            )
//            .setConstantHeadingInterpolation(defaultHeading)
//            .build()
//
//        leave = follower.pathBuilder()
//            .addPath(
//                BezierLine(
//                    shootPose,
//                    side(Pose(107.000, 80.000))
//                )
//            )
//            .setConstantHeadingInterpolation(defaultHeading)
//            .build()
//    }
//
//    private val autonomousRoutine: Command
//    get() = SequentialGroup(
//        preShoot,
//        FollowPath(shootPreload),
//        SetPosition( Transfer.door, KtConstants.DOOR_CLOSE),
//        shoot,
//        FollowPath(intakeMiddleSpike),
//        Delay(intakeTime),
//        FollowPath(openGate),
//        Delay(1.0),
//        preShoot,
//        FollowPath(secondShoot),
//        shoot,
//        FollowPath(intakeFirstSpike),
//        Delay(intakeTime),
//        preShoot,
//        FollowPath(thirdShoot),
//        shoot,
//        FollowPath(intakeThirdSpike),
//        Delay(intakeTime),
//        preShoot,
//        FollowPath(fourthShoot),
//        shoot,
//        FollowPath(leave)
//        )
//
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
//
//    }
//}