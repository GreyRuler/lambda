data class Chat(
    val ID: Int,
    val messages: MutableList<Message>,
    val userID: Int
)