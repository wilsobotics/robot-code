package autos

import com.pedropathing.geometry.BezierCurve
import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import dev.nextftc.extensions.pedro.PedroComponent

class Paths15close() {
    val firstShoot = PedroComponent.Companion.follower.pathBuilder()
        .addPath(
            BezierLine(
                Pose(120.700, 120.900),
                Pose(96.000, 84.000)
            )
        )
        .setLinearHeadingInterpolation(Math.toRadians(0.0), Math.toRadians(0.0))
        .build()

    val intakeSecondSpike = PedroComponent.Companion.follower.pathBuilder()
        .addPath(
            BezierCurve(
                Pose(96.000, 84.000),
                Pose(92.000, 58.700),
                Pose(136.000, 57.000)
            )
        )
        .setConstantHeadingInterpolation(Math.toRadians(0.0))
        .build()

    val secondShoot = PedroComponent.Companion.follower.pathBuilder()
        .addPath(
            BezierCurve(
                Pose(136.000, 57.000),
                Pose(92.000, 58.700),
                Pose(96.000, 84.000)
            )
        )
        .setConstantHeadingInterpolation(Math.toRadians(0.0))
        .build()

    val firstOpenGate = PedroComponent.Companion.follower.pathBuilder()
        .addPath(
            BezierCurve(
                Pose(96.000, 84.000),
                Pose(92.000, 58.700),
                Pose(130.700, 59.200)
            )
        )
        .setLinearHeadingInterpolation(Math.toRadians(0.0), Math.toRadians(30.0))
        .build()

    val firstIntakeGate = PedroComponent.Companion.follower.pathBuilder()
        .addPath(
            BezierLine(
                Pose(130.700, 59.200),
                Pose(131.800, 54.000)
            )
        )
        .setLinearHeadingInterpolation(Math.toRadians(30.0), Math.toRadians(60.0))
        .build()

    val thirdShoot = PedroComponent.Companion.follower.pathBuilder()
        .addPath(
            BezierCurve(
                Pose(131.800, 54.000),
                Pose(92.000, 58.700),
                Pose(96.000, 84.000)
            )
        )
        .setLinearHeadingInterpolation(Math.toRadians(60.0), Math.toRadians(0.0))
        .build()

    val intakeFirstSpike = PedroComponent.Companion.follower.pathBuilder()
        .addPath(
            BezierLine(
                Pose(96.000, 84.000),
                Pose(129.000, 84.000)
            )
        )
        .setLinearHeadingInterpolation(Math.toRadians(0.0), Math.toRadians(0.0))
        .build()

    val fourthShoot = PedroComponent.Companion.follower.pathBuilder()
        .addPath(
            BezierLine(
                Pose(129.000, 84.000),
                Pose(96.000, 84.000)
            )
        )
        .setLinearHeadingInterpolation(Math.toRadians(0.0), Math.toRadians(0.0))
        .build()

    val intakeThirdSpike = PedroComponent.Companion.follower.pathBuilder()
        .addPath(
            BezierCurve(
                Pose(96.000, 84.000),
                Pose(85.000, 29.000),
                Pose(135.000, 35.000)
            )
        )
        .setConstantHeadingInterpolation(Math.toRadians(0.0))
        .build()

    val fifthShoot = PedroComponent.Companion.follower.pathBuilder()
        .addPath(
            BezierLine(
                Pose(135.000, 35.000),
                Pose(96.000, 84.000)
            )
        )
        .setLinearHeadingInterpolation(Math.toRadians(0.0), Math.toRadians(0.0))
        .build()

    val leave = PedroComponent.Companion.follower.pathBuilder()
        .addPath(
            BezierLine(
                Pose(96.000, 84.000),
                Pose(107.000, 80.000)
            )
        )
        .setConstantHeadingInterpolation(Math.toRadians(0.0))
        .build()
}