package autos

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.bylazar.telemetry.TelemetryManager
import com.pedropathing.follower.Follower
import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.paths.PathChain
import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.BezierCurve
import com.pedropathing.geometry.Pose
import dev.nextftc.ftc.NextFTCOpMode
import org.firstinspires.ftc.teamcode.pedroPathing.Constants


@Autonomous(name = "9 AUTO far")
class VatsalFarAuto9 : NextFTCOpMode() {
    private var panelsTelemetry: TelemetryManager? = null // Panels Telemetry instance
    var follower: Follower? = null // Pedro Pathing follower instance
    private var pathState = 0 // Current autonomous path state (state machine)
    private var paths: Paths? = null // Paths defined in the Paths class

    override fun onInit() {
        panelsTelemetry = PanelsTelemetry.telemetry

        follower = Constants.createFollower(hardwareMap)
        follower!!.setStartingPose(Pose(72.0, 8.0, Math.toRadians(90.0)))

        paths = VatsalFarAuto9.Paths(follower!!) // Build paths

        panelsTelemetry!!.debug("Status", "Initialized")
        panelsTelemetry!!.update(telemetry)
    }

    override fun onUpdate() {
        follower!!.update() // Update Pedro Pathing
        pathState = autonomousPathUpdate() // Update autonomous state machine

        // Log values to Panels and Driver Station
        panelsTelemetry!!.debug("Path State", pathState)
        panelsTelemetry!!.debug("X", follower!!.getPose().getX())
        panelsTelemetry!!.debug("Y", follower!!.getPose().getY())
        panelsTelemetry!!.debug("Heading", follower!!.getPose().getHeading())
        panelsTelemetry!!.update(telemetry)
    }

    class Paths(follower: Follower) {
        var MainChain: PathChain?

        init {
            MainChain = follower.pathBuilder()
                .addPath(
                    BezierLine(
                        Pose(85.000, 20.000),
                        Pose(133.000, 8.000)
                    )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0.0))
                .addPath(
                    BezierLine(
                        Pose(133.000, 8.000),
                        Pose(85.000, 20.000)
                    )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0.0))
                .setReversed()
                .addPath(
                    BezierCurve(
                        Pose(80.000, 7.900),
                        Pose(97.496, 39.998),
                        Pose(113.676, 38.784),
                        Pose(133.516, 36.014)
                    )
                )
                .setTangentHeadingInterpolation()
                .addPath(
                    BezierLine(
                        Pose(133.516, 36.014),
                        Pose(85.000, 20.000)
                    )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0.0))
                .setReversed()
                .addPath(
                    BezierLine(
                        Pose(85.000, 20.000),
                        Pose(133.000, 8.000)
                    )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0.0))
                .addPath(
                    BezierLine(
                        Pose(133.000, 8.000),
                        Pose(85.000, 20.000)
                    )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0.0))
                .addPath(
                    BezierLine(
                        Pose(85.000, 20.000),
                        Pose(133.000, 8.000)
                    )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0.0))
                .addPath(
                    BezierLine(
                        Pose(133.000, 8.000),
                        Pose(85.000, 20.000)
                    )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0.0))
                .addPath(
                    BezierLine(
                        Pose(85.000, 20.000),
                        Pose(90.000, 28.000)
                    )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0.0))
                .addPath(
                    BezierLine(
                        Pose(85.000, 20.000),
                        Pose(133.000, 8.000)
                    )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0.0))
                .addPath(
                    BezierLine(
                        Pose(133.000, 8.000),
                        Pose(85.000, 20.000)
                    )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0.0))
                .build()
        }
    }

    fun autonomousPathUpdate(): Int {
        // Add your state machine Here
        // Access paths with paths.pathName
        // Refer to the Pedro Pathing Docs (Auto Example) for an example state machine
        return 0
    }
}