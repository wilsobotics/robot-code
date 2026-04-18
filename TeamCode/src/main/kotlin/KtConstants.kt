import com.bylazar.configurables.annotations.Configurable
import com.pedropathing.geometry.Pose

@Configurable
class KtConstants {
    companion object {

        // GOAL + ROBOT dimensions
        const val GOAL_HEIGHT = 40
        @JvmStatic var GOAL_X = 144.0
        @JvmStatic var GOAL_Y = 144.0
        @JvmStatic var TURRET_SWITCH_ENCODER = -1
        const val SHOOTER_HEIGHT = 15.5
        val RESET_POS = Pose(108.0, 136.0, Math.toRadians(0.0))
        @JvmStatic var ROBOT_X = 72.0
        @JvmStatic var ROBOT_Y = 72.0
        @JvmStatic var ROBOT_HEADING = 0.0
        @JvmStatic var TURRET_POS = 0.0
        @JvmStatic var SIDE = "RED"

        // Transfer constants (67)
        @JvmField
        var TRANSFER_ACTIVE_POWER = 1.0
        @JvmField
        var TRANSFER_BLOCKING_POWER = -0.6
        @JvmField
        var INTAKE_ACTIVE_POWER = 1.0
        @JvmField
        var INTAKE_RESTING_POWER = 0.9
        @JvmField
        var PUSHER_PUSH = 0.0
        @JvmField
        var PUSHER_REST = 1.0

        // Shooter constants
        const val TICKS_PER_DEGREE = -64.67
        const val FLYWHEEL_TOLERANCE = 100.0
        const val ROBOT_RADIUS = 7.0
        const val TURRET_TOLERANCE = 250.0
        const val TURRET_ENCODER_LIMIT = 8000.0

        @JvmField
        var HOOD_CLOSE_POS = 0.13
        var HOOD_FAR_POS = 0.25
        var HOOD_CLOSE_ANGLE = 50.0
        var HOOD_FAR_ANGLE = 40.0

        const val FLYWHEEL_KP = 0.01
        const val FLYWHEEL_KI = 0.0
        const val FLYWHEEL_KD = 0.0
        const val FLYWHEEL_KV = 0.001
        const val FLYWHEEL_KA = 0.0
        const val FLYWHEEL_KS = 0.15

        const val TURRET_KP = 0.00020
        const val TURRET_KI = 0.0
        const val TURRET_KD = 0.000010
        const val TURRET_KV = 0.0
        const val TURRET_KA = 0.0
        const val TURRET_KS = 0.32

        @JvmStatic var currentPose = Pose(0.0, 0.0, 0.0)

        fun side(pose: Pose): Pose {
            return if (SIDE == "BLUE") {
                Pose(144 - pose.x, pose.y, Math.PI - pose.heading)
            } else {
                pose
            }
        }
    }
}