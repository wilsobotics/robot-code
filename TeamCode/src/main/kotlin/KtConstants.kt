import com.bylazar.configurables.annotations.Configurable

@Configurable
class KtConstants {
    companion object {
        const val GOAL_HEIGHT = 40
        var GOAL_X = 160.0
        var GOAL_Y = 140.0
        const val SHOOTER_HEIGHT = 15.5
        const val FAR_HOOD_ANGLE = 40.0
        const val TRANSFER_ACTIVE_POWER = 0.8
        const val TRANSFER_RESTING_POWER = 0.0
        const val INTAKE_ACTIVE_POWER = 0.8
        const val INTAKE_RESTING_POWER = 0.1
        const val DOOR_CLOSE = 0.33
        const val DOOR_OPEN = 0.5
        const val TICKS_PER_DEGREE = 64.67
    }
}