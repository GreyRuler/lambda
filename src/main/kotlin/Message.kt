data class Message(
    val ID: Int,
    var text: String,
    val data: Long,
    var isRead: Boolean,
    val userID: Int
)