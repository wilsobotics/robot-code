import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.nextftc.bindings.BindingManager
import dev.nextftc.bindings.button
import dev.nextftc.core.commands.delays.Delay
import dev.nextftc.core.commands.groups.ParallelDeadlineGroup
import dev.nextftc.core.commands.groups.ParallelRaceGroup
import dev.nextftc.core.commands.groups.SequentialGroup
import dev.nextftc.core.commands.utility.LambdaCommand
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.ftc.Gamepads
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import dev.nextftc.hardware.driving.FieldCentric
import dev.nextftc.hardware.driving.MecanumDriverControlled
import dev.nextftc.hardware.impl.Direction
import dev.nextftc.hardware.impl.IMUEx
import dev.nextftc.hardware.impl.MotorEx
import kotlin.time.Duration.Companion.milliseconds

@TeleOp(name = "NextFTC TeleOp")
class TeleOpProgram : NextFTCOpMode() {
    init {
        addComponents(
            SubsystemComponent(Flywheel),
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
        val driverControlled = MecanumDriverControlled(
            frontLeftMotor,
            frontRightMotor,
            backLeftMotor,
            backRightMotor,
            -Gamepads.gamepad1.leftStickY,
            Gamepads.gamepad1.leftStickX,
            Gamepads.gamepad1.rightStickX,
            FieldCentric(imu)
        )
        driverControlled()

        val shoot = SequentialGroup(Door.open, Delay(300.milliseconds), Door.close)
            .requires(Door)
            .setInterruptible(false)

        Flywheel.setRPM(1500.0)
        button { gamepad1.b }
            .whenBecomesTrue { shoot }
    }

    override fun onUpdate() {
        super.onUpdate()
        BindingManager.update()
    }

    override fun onStop() {
        super.onStop()
        BindingManager.reset()
    }
 }