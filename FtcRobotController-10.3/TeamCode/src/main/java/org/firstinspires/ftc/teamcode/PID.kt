import java.util.*
import com.qualcomm.robotcore.util.*
import org.firstinspires.ftc.teamcode.Constants.*

class PID {
    fun pidValue(value: float, targetValue: float) {
        var integralSum: float = 0
        var lastError: float = 0

        val timer = ElapsedTime()

        if (value != targetValue) {
            val currentValue = value //set to current value
            val error: float = targetValue - currentValue
            val derivative = (error - lastError) / timer.seconds()
            integralSum += (error * timer.seconds())
            val out = (KP * error) + (KI + integralSum) + (KD * derivative)

            lastError = error
            timer.reset()
            return out
        }
    }
}
