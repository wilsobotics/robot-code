import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.nextftc.bindings.BindingManager
import dev.nextftc.bindings.button
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

@TeleOp(name = "NextFTC TeleOp")
class TeleOpProgram : NextFTCOpMode() {
    init {
        addComponents(
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
    private val evanResult = Evan.EvanResult()

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

        BindingManager.layer = "intake"

        button { BindingManager.layer == "shooting"}
            .whenBecomesTrue { Transfer.initialize() }
            .whenTrue { Shooter.flywheelRPM(1500.0) }
        button { BindingManager.layer == "intake"}
            .whenBecomesTrue {
                Shooter.flywheelRPM(0.0)
                Transfer.intake
            }

        button { gamepad1.b }
            .inLayer("shooting")
            .whenBecomesTrue {
                Transfer.shoot
                BindingManager.layer = "intake"
            }
            .inLayer("intake")
            .whenBecomesTrue { BindingManager.layer = "shooting" }
    }

    override fun onUpdate() {
        super.onUpdate()
        BindingManager.update()
        Evan.calculateEvan(tY, tX, sY, sX, angle, rYV, rXV, evanResult)
        Shooter.hoodAngle(40.0)
        Shooter.turretAngle(0.0)
    }

    override fun onStop() {
        super.onStop()
        BindingManager.reset()
    }
 }