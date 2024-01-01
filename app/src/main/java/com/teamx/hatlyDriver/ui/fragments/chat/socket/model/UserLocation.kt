package  com.teamx.hatlyDriver.ui.fragments.chat.socket.model
import androidx.annotation.Keep

@Keep
data class UserLocation(
    val _id: String,
    val coordinates: List<Double>,
    val lat: Double,
    val lng: Double,
    val type: String
)