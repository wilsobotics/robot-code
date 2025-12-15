import java.util.*
import com.qualcomm.robotcore.util.*

class PID {
    fun pid_value(value: float, targetValue: float) {
        val kp: float = 0
        val ki: float = 0
        val kd: float = 0

        var integralSum = 0
        var lastError: float = 0

        val timer = ElapsedTime()

        if (value != targetValue) {
            val currentValue = value //set to current value
            val error: float = targetValue - currentValue
            val derivative = (error - lastError) / timer.seconds()

            integralSum += (error * timer.seconds())
            val out = (kp * error) + (ki + integralSum) + (kd * derivative)

            return out
            lastError = error
            timer.reset()
        }
    }
}
