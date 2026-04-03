package autos

import autos.Paths15close
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import dev.nextftc.core.commands.Command
import dev.nextftc.core.commands.delays.Delay
import dev.nextftc.core.commands.groups.SequentialGroup
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.extensions.pedro.FollowPath
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import subsystems.Shooter
import subsystems.Transfer

@Autonomous(name = "NextFTC Autonomous Program Kotlin")
class Auto : NextFTCOpMode() {
    init {
        addComponents(
            SubsystemComponent(Shooter, Transfer),
            BulkReadComponent
        )
    }

    val shoot = SequentialGroup(
        Transfer.shoot,
        Shooter.turnFlywheelOff,
        Transfer.intake
    )

    val preShoot = SequentialGroup(
        Shooter.turnFlywheelOn,
        Transfer.preShoot,
    )

    val paths = Paths15close()

    private val autonomousRoutine: Command
        get() = SequentialGroup(
            preShoot,
            FollowPath(paths.firstShoot),
            shoot,
            FollowPath(paths.intakeSecondSpike),
            preShoot,
            FollowPath(paths.secondShoot),
            shoot,
            Transfer.rest,
            FollowPath(paths.firstOpenGate),
            Transfer.intake,
            FollowPath(paths.firstIntakeGate),
            Delay(1.0),
            preShoot,
            FollowPath(paths.thirdShoot),
            shoot,
            FollowPath(paths.intakeFirstSpike),
            preShoot,
            FollowPath(paths.fourthShoot),
            shoot,
            FollowPath(paths.intakeThirdSpike),
            preShoot,
            FollowPath(paths.fifthShoot),
            shoot,
            FollowPath(paths.leave)
        )


    override fun onStartButtonPressed() {
        Shooter.isEnabled = true
        autonomousRoutine()
    }
}