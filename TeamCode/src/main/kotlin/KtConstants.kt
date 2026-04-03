import com.bylazar.configurables.annotations.Configurable
import com.pedropathing.geometry.Pose

@Configurable
class KtConstants {
    companion object {

        // GOAL + ROBOT dimensions
        const val GOAL_HEIGHT = 40
        @JvmStatic var GOAL_X = 160.0
        @JvmStatic var GOAL_Y = 140.0
        const val SHOOTER_HEIGHT = 15.5

        // Transfer constants
        const val TRANSFER_ACTIVE_POWER = 0.8
        const val TRANSFER_BLOCKING_POWER = -0.6
        const val INTAKE_ACTIVE_POWER = 0.8
        const val INTAKE_RESTING_POWER = 0.6
        const val PUSHER_PUSH = 0.0
        const val PUSHER_REST = 1.0

        // Shooter constants
        const val TICKS_PER_DEGREE = 64.67
        const val FLYWHEEL_THRESHOLD = 50.0
        const val TURRET_THRESHOLD = 50.0
        const val TURRET_ENCODER_LIMIT = 6000.0

        const val HOOD_CLOSE = 0.0
        const val HOOD_FAR = 1.0

        const val FLYWHEEL_KP = 0.01
        const val FLYWHEEL_KI = 0.0
        const val FLYWHEEL_KD = 0.0
        const val FLYWHEEL_KV = 0.001
        const val FLYWHEEL_KA = 0.0
        const val FLYWHEEL_KS = 0.15

        const val TURRET_KP = 0.00019
        const val TURRET_KI = 0.0
        const val TURRET_KD = 0.000012
        const val TURRET_KV = 0.0
        const val TURRET_KA = 0.0
        const val TURRET_KS = 0.3

        @JvmStatic var currentPose = Pose(0.0, 0.0, 0.0)
    }
}