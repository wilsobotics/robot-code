package autos
import com.pedropathing.geometry.BezierCurve
import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.pedropathing.paths.PathChain
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import dev.nextftc.core.commands.Command
import dev.nextftc.core.commands.delays.Delay
import dev.nextftc.core.commands.groups.ParallelGroup
import dev.nextftc.core.commands.groups.SequentialGroup
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.extensions.pedro.FollowPath
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import subsystems.Shooter
import subsystems.Transfer

@Autonomous(name = "9 AUTO far")
class VatsalFarAuto : NextFTCOpMode() {
    init {
        addComponents(
            SubsystemComponent(Shooter, Transfer),
            BulkReadComponent,
            PedroComponent(Constants::createFollower)
        )
    }

    private lateinit var shootPath: PathChain
    private lateinit var intakeMiddleSpike: PathChain
    private lateinit var gateIntake: PathChain
    private lateinit var shootPreload: PathChain
    private lateinit var shootFirstSpike: PathChain
    private lateinit var shootMiddleSpike: PathChain
    private lateinit var intakeFirstSpike: PathChain
    private lateinit var gateIntakeCycle: SequentialGroup

    val waitTime = 0.0
    val shootTime = 0.5

    fun side(pose: Pose): Pose {
        if (KtConstants.SIDE == "BLUE") {
            val x = 144 - pose.x
            val y = pose.y
            val heading = Math.PI - pose.heading
            return Pose(x, y, heading)
        } else {
            return pose
        }
    }

    // Extracted directly from your Pedro Visualizer dump
    val startPose = side(Pose(80.0, 7.9, Math.toRadians(0.0)))
    val preloadShootPose = side(Pose(85.0, 20.0))
    val shootPose = side(Pose(85.0, 20.0))
    val middleSpikePose = side(Pose(133.0, 34.24))
    val firstSpikePose = side(Pose(133.0, 8.0))
    val gateIntakePose = side(Pose(133.0, 8.0))
    val leavePose = side(Pose(90.0, 28.0))

    val shoot = SequentialGroup(
        Delay(waitTime),
        Transfer.shoot,
        Delay(shootTime),
        Transfer.intake
    )

    override fun onInit() {
        shootPreload = follower.pathBuilder()
            .addPath(BezierLine(startPose, preloadShootPose))
            .setConstantHeadingInterpolation(0.0)
            .build()

        intakeMiddleSpike = follower.pathBuilder()
            .addPath(
                BezierCurve(
                    preloadShootPose,
                    side(Pose(80.543, 41.604)), // Avoiding submersible
                    side(Pose(111.895, 34.967)), // Sweeping in
                    middleSpikePose
                )
            )
            .setTangentHeadingInterpolation()
            .build()

        shootMiddleSpike = follower.pathBuilder()
            .addPath(BezierLine(middleSpikePose, shootPose))
            .setConstantHeadingInterpolation(0.0)
            .setReversed()
            .build()

        shootPath = follower.pathBuilder()
            .addPath(BezierLine(gateIntakePose, shootPose))
            .setConstantHeadingInterpolation(0.0)
            .setReversed()
            .build()

        gateIntake = follower.pathBuilder()
            .addPath(BezierLine(shootPose, gateIntakePose))
            .setConstantHeadingInterpolation(0.0)
            .build()

        intakeFirstSpike = follower.pathBuilder()
            .addPath(BezierLine(shootPose, firstSpikePose))
            .setConstantHeadingInterpolation(0.0)
            .build()

        shootFirstSpike = follower.pathBuilder()
            .addPath(BezierLine(firstSpikePose, leavePose))
            .setConstantHeadingInterpolation(0.0)
            .setReversed()
            .build()

        gateIntakeCycle = SequentialGroup(
            FollowPath(gateIntake),
            Delay(1.2), // Pulled from your 1200ms wait
            Transfer.rest,
            FollowPath(shootPath),
            shoot
        )
    }

    val autonomousRoutine: Command
        get() = SequentialGroup(
            ParallelGroup(FollowPath(shootPreload), SequentialGroup(Delay(0.5), shoot)),

            FollowPath(intakeMiddleSpike),
            Transfer.rest,
            FollowPath(shootMiddleSpike),
            shoot,

            gateIntakeCycle,
            gateIntakeCycle,
            gateIntakeCycle,

            FollowPath(intakeFirstSpike),
            Transfer.rest,
            FollowPath(shootFirstSpike),
            shoot
        )

    override fun onStartButtonPressed() {
        follower.setStartingPose(startPose)
        Shooter.turretEnabled = true
        autonomousRoutine()
    }

    override fun onUpdate() {
        super.onUpdate()
        KtConstants.ROBOT_X = follower.pose.x
        KtConstants.ROBOT_Y = follower.pose.y
        KtConstants.TURRET_POS = Shooter.turret.currentPosition
        KtConstants.ROBOT_HEADING = follower.pose.heading
    }
}