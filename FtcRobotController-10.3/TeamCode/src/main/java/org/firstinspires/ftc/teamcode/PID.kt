package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.util.*
import org.firstinspires.ftc.teamcode.Constants.*

class PID {
    fun pidValue(value: Double, targetValue: Double, timer: ElapsedTime, lastError: Double): Array<Double> {
        var integralSum: Double = 0.0
        var out: Double = 0.0

        if (value != targetValue) {
            val error: Double = targetValue - value
            val derivative = (error - lastError) / timer.seconds()
            integralSum += (error * timer.seconds())
            out = (KP * error) + (KI + integralSum) + (KD * derivative)

        }
        return arrayOf(out, lastError)
    }
}
