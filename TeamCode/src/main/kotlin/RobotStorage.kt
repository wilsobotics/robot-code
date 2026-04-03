import java.io.File

object RobotStorage {
    private val file = File("/sdcard/FIRST/robot_state.txt")

    // Default values if loading fails
    data class RobotState(
        val x: Double = 72.0,
        val y: Double = 72.0,
        val heading: Double = 0.0,
        val side: String = "RED"
    )

    fun saveState(state: RobotState) {
        file.writeText("${state.x},${state.y},${state.heading},${state.side}")
    }

    fun loadState(): RobotState {
        return try {
            val content = file.readText().split(",")
            RobotState(
                content[0].toDouble(),
                content[1].toDouble(),
                content[2].toDouble(),
                content[3]
            )
        } catch (e: Exception) {
            RobotState() // Return defaults if file doesn't exist
        }
    }
}