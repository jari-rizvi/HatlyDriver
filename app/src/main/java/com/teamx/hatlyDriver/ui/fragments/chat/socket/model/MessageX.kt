package  com.teamx.hatlyDriver.ui.fragments.chat.socket.model
import androidx.annotation.Keep

@Keep
data class MessageX(
    val __v: Int,
    val _id: String,
    val content: String,
    var createdAt: String,
    val from: String,
    val read: Boolean,
    val room_id: String,
    val updatedAt: String
)