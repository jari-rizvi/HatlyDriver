package  com.teamx.hatlyDriver.ui.fragments.chat.socket.model
import androidx.annotation.Keep

@Keep
data class ShippingAddress(
    val _id: String,
    val city: String,
    val country: String,
    val state: String,
    val street_address: String,
    val zip: String
)