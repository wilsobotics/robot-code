package subsystems

import dev.nextftc.core.commands.utility.LambdaCommand
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.ftc.ActiveOpMode.hardwareMap
import org.firstinspires.ftc.teamcode.Prism.Color
import org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver
import org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver.LayerHeight
import org.firstinspires.ftc.teamcode.Prism.PrismAnimations

object leds2         : Subsystem {

    // Use lateinit since hardwareMap is only available at OpMode runtime
    lateinit var prism: GoBildaPrismDriver
        private set

    override fun initialize() {
        // Initializes your prism hardware device seamlessly
        prism = hardwareMap.get<GoBildaPrismDriver?>(GoBildaPrismDriver::class.java, "prism")

        // Optional: Set a default color at initialization
        yellow()
    }

    val yellow = LambdaCommand() .setStart{
        val snakes = PrismAnimations.Solid().apply {
setPrimaryColor(255, 200, 0)        }
        prism.insertAndUpdateAnimation(LayerHeight.LAYER_0, snakes)
    }
    val red = LambdaCommand() .setStart{
        val snakes = PrismAnimations.Solid().apply {
            setPrimaryColor(255, 2, 2)        }
        prism.insertAndUpdateAnimation(LayerHeight.LAYER_0, snakes)
    }

}