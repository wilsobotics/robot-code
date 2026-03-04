import com.pedropathing.ftc.drivetrains.MecanumConstants
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import dev.nextftc.hardware.impl.Direction
import dev.nextftc.hardware.impl.IMUEx
import dev.nextftc.hardware.impl.MotorEx
import org.firstinspires.ftc.teamcode.pedroPathing.Constants

@Autonomous(name = "NextFTC Autonomous")
class Auto : NextFTCOpMode() {
    init {
        addComponents(
            PedroComponent(Constants::createFollower),
            SubsystemComponent(Shooter, Transfer),
            BulkReadComponent,
            BindingsComponent
        )
    }

    private val frontLeftMotor = MotorEx("front_left").brakeMode().reversed()
    private val frontRightMotor = MotorEx("front_right").brakeMode()
    private val backLeftMotor = MotorEx("back_left").brakeMode().reversed()
    private val backRightMotor = MotorEx("back_right").brakeMode()
    private val imu = IMUEx("imu", Direction.LEFT, Direction.FORWARD).zeroed()

    override fun onStartButtonPressed() {
        super.onStartButtonPressed()
    }
}