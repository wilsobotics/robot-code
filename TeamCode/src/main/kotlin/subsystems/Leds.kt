package subsystems

import dev.nextftc.core.commands.utility.LambdaCommand
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.ftc.ActiveOpMode.hardwareMap
import org.firstinspires.ftc.teamcode.Prism.Color
import org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver
import org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver.LayerHeight
import org.firstinspires.ftc.teamcode.Prism.PrismAnimations
import org.firstinspires.ftc.teamcode.Prism.PrismAnimations.Snakes

object Leds : Subsystem {

    // Use lateinit since hardwareMap is only available at OpMode runtime
    lateinit var prism: GoBildaPrismDriver
        private set

    override fun initialize() {
        // Initializes your prism hardware device seamlessly
        prism = hardwareMap.get<GoBildaPrismDriver?>(GoBildaPrismDriver::class.java, "prism")

        // Optional: Set a default color at initialization
        neutral()
    }

    val yellow = Color(240, 137, 0)


    val readyToShoot  = LambdaCommand() .setStart{
        val ready = configureSolid(0, 23, 0, 255, 0)
        prism.insertAndUpdateAnimation(LayerHeight.LAYER_0, ready)
    }

    val intake3  = LambdaCommand() .setStart{
        val ready = configureSolid(0, 23, 255, 0, 0)
        prism.insertAndUpdateAnimation(LayerHeight.LAYER_0, ready)
    }


    /*
    * States:
    * Neutral: Yellow background colour
    * Intaking: Blue Snake with Yellow background colour
    * Reverse Intaking: Flashing Yellow
    * Shooting: Blue Progress bar with Yellow background Colour
    * */

    val neutral = LambdaCommand() .setStart{
        val snakes = Snakes()
        if (KtConstants.SIDE == "RED") {
                snakes.setColors(Color.RED)
        } else {
            snakes.setColors(Color.BLUE)
        }
        snakes.setSnakeLength(5)
        snakes.setSpacingBetween(3)
        snakes.setSpeed(0.2f)
        snakes.setBackgroundColor(yellow)
        prism.insertAndUpdateAnimation(LayerHeight.LAYER_0, snakes)
    }
    val intaking = LambdaCommand() .setStart {
        val snakes = Snakes()
        if (KtConstants.SIDE == "RED") {
            snakes.setColors(Color.RED)
        } else {
            snakes.setColors(Color.BLUE)
        }
        snakes.setSnakeLength(5)
        snakes.setSpacingBetween(3)
        snakes.setSpeed(1.0f)
        snakes.setBackgroundColor(yellow)
        prism.insertAndUpdateAnimation(LayerHeight.LAYER_0, snakes)
    }
    val reverseIntaking = LambdaCommand() .setStart {
        prism.insertAndUpdateAnimation(LayerHeight.LAYER_0,
            PrismAnimations.Pulse().apply {
                primaryColor = Color.YELLOW
                secondaryColor = Color.CYAN
                period = 250
            startIndex = 0
                stopIndex = 23
            })
    }
    val shooting = LambdaCommand().setStart {
        val snakes = Snakes()
        snakes.setColors(Color.TRANSPARENT)
        snakes.setSnakeLength(5)
        snakes.setSpacingBetween(3)
        snakes.setSpeed(1.0f)
        snakes.setBackgroundColor(Color.GREEN)
        prism.insertAndUpdateAnimation(LayerHeight.LAYER_0, snakes)
    }
    private fun configureSolid(start: Int, stop: Int, red: Int, green: Int, blue: Int): PrismAnimations.Solid {
        return PrismAnimations.Solid().apply {
            startIndex = start
            setPrimaryColor(red, green, blue)
            stopIndex = stop
            brightness = 100
        }
    }
}