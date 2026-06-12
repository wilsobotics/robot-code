package autos
import KtConstants
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


@Autonomous(name = "18 AUTO close")
class OP18ballauto : NextFTCOpMode() {
    init {
        addComponents(
            SubsystemComponent(Shooter, Transfer),
            BulkReadComponent,
            PedroComponent(Constants::createFollower)
        )
    }

    //
//    static
//    class Paths(follower: Follower) {
//        var Shoot1: PathChain?
//        var Intakemiddlespike: PathChain?
//        var Shoot2: PathChain?
//        var Gateintake1: PathChain?
//        var Shoot3: PathChain?
//        var Gateintake2: PathChain?
//        var Shoot4: PathChain?
//        var Gateintake3: PathChain?
//        var Shoot5: PathChain?
//        var Intakefirstspike: PathChain?
//        var Path11: PathChain?
//
    private lateinit var Shoot1: PathChain
    private lateinit var Intakemiddlespike: PathChain
    private lateinit var Shoot2: PathChain
    private lateinit var Gateintake1: PathChain
    private lateinit var Shoot3: PathChain
    private lateinit var Gateintake2: PathChain
    private lateinit var Shoot4: PathChain
    private lateinit var Gateintake3: PathChain
    private lateinit var Shoot5: PathChain
    private lateinit var Intakefirstspike: PathChain
    private lateinit var Path11: PathChain
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
        Shoot1 = follower.pathBuilder().addPath(
            BezierLine(
                Pose(125.720, 109.879),

                Pose(90.000, 88.000)
            )
        ).setLinearHeadingInterpolation(Math.toRadians(0.0), Math.toRadians(300.0))

            .build()

        Intakemiddlespike = follower.pathBuilder().addPath(
            BezierCurve(
                Pose(90.000, 88.000),
                Pose(106.862, 57.601),
                Pose(131.000, 57.000)
            )
        ).setTangentHeadingInterpolation()

            .build()

        Shoot2 = follower.pathBuilder().addPath(
            BezierCurve(
                Pose(131.000, 57.000),
                Pose(122.288, 57.033),
                Pose(96.635, 74.800),
                Pose(91.000, 80.000)
            )
        ).setTangentHeadingInterpolation()
            .setReversed()
            .build()

        Gateintake1 = follower.pathBuilder().addPath(
            BezierCurve(
                Pose(91.000, 80.000),
                Pose(107.478, 63.599),
                Pose(131.000, 58.000)
            )
        ).setLinearHeadingInterpolation(Math.toRadians(320.0), Math.toRadians(30.0))

            .build()

        Shoot3 = follower.pathBuilder().addPath(
            BezierCurve(
                Pose(131.000, 58.000),
                Pose(116.265, 55.116),
                Pose(104.393, 69.681),
                Pose(91.000, 80.000)
            )
        ).setTangentHeadingInterpolation()
            .setReversed()
            .build()

        Gateintake2 = follower.pathBuilder().addPath(
            BezierCurve(
                Pose(91.000, 80.000),
                Pose(107.478, 63.599),
                Pose(131.000, 57.800)
            )
        ).setLinearHeadingInterpolation(Math.toRadians(-38.0), Math.toRadians(30.0))

            .build()

        Shoot4 = follower.pathBuilder().addPath(
            BezierCurve(
                Pose(131.000, 57.800),
                Pose(116.669, 54.914),
                Pose(104.393, 69.681),
                Pose(91.000, 80.000)
            )
        ).setTangentHeadingInterpolation()
            .setReversed()
            .build()

        Gateintake3 = follower.pathBuilder().addPath(
            BezierCurve(
                Pose(91.000, 80.000),
                Pose(107.478, 63.599),
                Pose(131.000, 57.800)
            )
        ).setLinearHeadingInterpolation(Math.toRadians(-38.0), Math.toRadians(30.0))

            .build()

        Shoot5 = follower.pathBuilder().addPath(
            BezierCurve(
                Pose(131.000, 57.800),
                Pose(115.861, 54.510),
                Pose(104.393, 69.680),
                Pose(91.000, 80.000)
            )
        ).setTangentHeadingInterpolation()
            .setReversed()
            .build()

        Intakefirstspike = follower.pathBuilder().addPath(
            BezierCurve(
                Pose(91.000, 80.000),
                Pose(100.123, 71.069),
                Pose(106.557, 83.734),
                Pose(128.231, 83.508)
            )
        ).setTangentHeadingInterpolation()

            .build()

        Path11 = follower.pathBuilder().addPath(
            BezierLine(
                Pose(128.231, 83.508),

                Pose(84.924, 112.861)
            )
        ).setLinearHeadingInterpolation(Math.toRadians(0.0), Math.toRadians(0.0))
            .setReversed()
            .build()
        gateIntakeCycle = SequentialGroup(
            FollowPath(Gateintake1),
            Delay(2.0),
            Transfer.rest,
            FollowPath(Shoot3),
            shoot
        )
    }

    val autonomousRoutine: Command
        get() = SequentialGroup(
            ParallelGroup(FollowPath(Shoot1), SequentialGroup(Delay(0.5), shoot)),

            FollowPath(Intakemiddlespike),
            Transfer.rest,
            FollowPath(Shoot2),
            shoot,

            gateIntakeCycle,
            gateIntakeCycle,
            gateIntakeCycle,

            FollowPath(Intakefirstspike),
            Transfer.rest,
            FollowPath(Shoot5),
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