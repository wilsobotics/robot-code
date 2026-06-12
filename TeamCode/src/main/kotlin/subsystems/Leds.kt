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

    val yellow = Color(200, 100, 0)
    val darkyellow = Color(150, 75, 0)
    val darkdarkyellow = Color(100, 50, 0)
    val darkdarkdarkyellow = Color(50, 25, 0)
    val darkdarkdarkdarkyellow = Color(25, 12, 0)


    val readyToShoot  = LambdaCommand() .setStart{
        val ready = configureSolid(0, 23, 0, 255, 0)
        prism.insertAndUpdateAnimation(LayerHeight.LAYER_4, ready)

    }

    val intake3  = LambdaCommand() .setStart{
        val ready = configureSolid(0, 23, 255, 0, 0)
        prism.insertAndUpdateAnimation(LayerHeight.LAYER_3, ready)
    }


    /*
    * States:
    * Neutral: Yellow background colour
    * Intaking: Blue Snake with Yellow background colour
    * Reverse Intaking: Flashing Yellow
    * Shooting: Blue Progress bar with Yellow background Colour
    * */

    val neutral = LambdaCommand() .setStart{
        prism.clearAllAnimations()
        var snakes = Snakes()
        if (KtConstants.SIDE == "RED") {
            snakes.setColors(Color.TRANSPARENT, darkdarkdarkdarkyellow, darkdarkdarkyellow, darkdarkyellow, darkyellow, yellow, darkyellow, darkdarkyellow, darkdarkdarkyellow, darkdarkdarkdarkyellow)
        } else {
            snakes.setColors(Color.TRANSPARENT, darkdarkdarkdarkyellow, darkdarkdarkyellow, darkdarkyellow, darkyellow, yellow, darkyellow, darkdarkyellow, darkdarkdarkyellow, darkdarkdarkdarkyellow)
        }
        snakes.setIndexes(0, 11)
        snakes.setSnakeLength(1)
        snakes.setSpacingBetween(0)
        snakes.setSpeed(0.25f)
        snakes.setBackgroundColor(Color.TRANSPARENT)
        prism.insertAndUpdateAnimation(LayerHeight.LAYER_0, snakes)

        snakes = Snakes()
        if (KtConstants.SIDE == "RED") {
            snakes.setColors(Color.TRANSPARENT, darkdarkdarkdarkyellow, darkdarkdarkyellow, darkdarkyellow, darkyellow, yellow, darkyellow, darkdarkyellow, darkdarkdarkyellow, darkdarkdarkdarkyellow)
        } else {
            snakes.setColors(Color.TRANSPARENT, darkdarkdarkdarkyellow, darkdarkdarkyellow, darkdarkyellow, darkyellow, yellow, darkyellow, darkdarkyellow, darkdarkdarkyellow, darkdarkdarkdarkyellow)
        }
        snakes.setIndexes(12, 23)
        snakes.setSnakeLength(1)
        snakes.setSpacingBetween(0)
        snakes.setSpeed(0.25f)
        snakes.setBackgroundColor(Color.TRANSPARENT)
        prism.insertAndUpdateAnimation(LayerHeight.LAYER_1, snakes)
    }
    val intaking = LambdaCommand() .setStart {
        val snakes = Snakes()
        if (KtConstants.SIDE == "RED") {
            snakes.setColors(Color.TRANSPARENT)
        } else {
            snakes.setColors(Color.TRANSPARENT)
        }
        snakes.setSnakeLength(7)
        snakes.setSpacingBetween(5)
        snakes.setSpeed(0.5f)
        snakes.setBackgroundColor(yellow)
        prism.insertAndUpdateAnimation(LayerHeight.LAYER_2, snakes)
    }
    val reverseIntaking = LambdaCommand() .setStart {
        prism.insertAndUpdateAnimation(LayerHeight.LAYER_2,
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
        snakes.setSnakeLength(7)
        snakes.setSpacingBetween(5)
        snakes.setSpeed(0.5f)
        snakes.setBackgroundColor(Color.GREEN)
        prism.insertAndUpdateAnimation(LayerHeight.LAYER_4, snakes)
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