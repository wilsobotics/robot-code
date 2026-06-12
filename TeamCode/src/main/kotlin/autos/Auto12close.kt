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
import dev.nextftc.hardware.positionable.SetPosition
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import subsystems.Shooter
import subsystems.Transfer

@Autonomous(name = "18 AUTO close")
class Auto12close : NextFTCOpMode() {
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

    val startPose = side(Pose(125.7, 109.9, Math.toRadians(0.0)))
    val shootPose = side(Pose(91.0, 80.0))
    val preloadShootPose = side(Pose(90.0, 88.0))
    val gateIntakePose = side(Pose(129.0, 58.0))
    val firstSpikePose = side(Pose(127.0, 82.000))
    val middleSpikePose = side(Pose(133.000, 61.0))
    val leavePose = side(Pose(91.2, 109.0))


    val shoot = SequentialGroup(
        Delay(waitTime),
        Transfer.shoot,
        Delay(shootTime),
        Transfer.intake
    )


    override fun onInit() {
        shootPreload = follower.pathBuilder()
            .addPath(
                BezierCurve(
                    startPose,
                    preloadShootPose
                )
            )
            .setLinearHeadingInterpolation(0.0, 320.0)
            .build()

        intakeMiddleSpike = follower.pathBuilder()
            .addPath(
                BezierCurve(
                    preloadShootPose,
                    side(Pose(106.800, 57.600)),
                    middleSpikePose
                )
            )
            .setTangentHeadingInterpolation()
            .build()

        shootMiddleSpike = follower.pathBuilder()
            .addPath(
                BezierCurve(
                    middleSpikePose,
                    shootPose
                )
            )
            .setTangentHeadingInterpolation()
            .build()

        shootPath = follower.pathBuilder()
            .addPath(
                BezierCurve(
                    gateIntakePose,
                    side(Pose(113.600, 45.800)),
                    side(Pose(104.400, 69.690)),
                    shootPose
                )
            )
            .setTangentHeadingInterpolation()
            .setReversed()
            .build()

        gateIntake = follower.pathBuilder()
            .addPath(
                BezierCurve(
                    shootPose,
                    side(Pose(107.4, 63.5)),
                    gateIntakePose
                )
            )
            .setLinearHeadingInterpolation(320.0, 30.0)
            .build()
        intakeFirstSpike = follower.pathBuilder()
            .addPath(
                BezierCurve(
                    shootPose,
                    firstSpikePose
                )
            )
            .setConstantHeadingInterpolation(0.0)
            .build()
        shootFirstSpike = follower.pathBuilder()
            .addPath(
                BezierCurve(
                    firstSpikePose,
                    leavePose
                )
            )
            .setTangentHeadingInterpolation()
            .setReversed()
            .build()

        gateIntakeCycle = SequentialGroup(
            FollowPath(gateIntake),
            Delay(2.0),
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
                shoot,
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