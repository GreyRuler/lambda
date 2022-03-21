import kotlinx.datetime.Clock

object ChatService {
    val chats = mutableListOf<Chat>()
    var chatID = 0
    var messageID = 0

    fun addChat(userID: Int): Int {
        chats.add(
            Chat(
                ID = ++chatID,
                mutableListOf(),
                userID = userID
            )
        )
        return chatID
    }

    fun deleteChat(chatID: Int): Boolean {
        val index = chats.indexOfFirst { chat -> chat.ID == chatID }
        return if (index != -1) {
            chats.removeAt(index)
            true
        } else {
            throw ChatNotFoundException("Chat not found")
        }
    }

    fun addMessage(userID: Int, chatID: Int, message: Message) {
        val chat = chats.find { chat -> chat.ID == chatID }
        if(chat?.userID == userID) {
            chat.messages.add(message)
        } else {
            throw ChatNotFoundException("Chat not found")
        }
    }

    fun editMessage(text: String, messageID: Int): Boolean {
        chats.forEach {
            chat -> val message = chat.messages.find { message -> message.ID == messageID }
            if (message != null) {
                message.text = text
                message.isRead = false
                return true
            }
        }
        throw MessageNotFoundException("Message not found")
    }

    fun deleteMessage(messageID: Int): Boolean {
        chats.forEach {
            chat -> val index = chat.messages.indexOfFirst { message -> message.ID == messageID }
            if (chat.messages.count() == 1) {
                chats.remove(chat)
                return true
            }
            if (index != -1) {
                chat.messages.removeAt(index)
                return true
            }
        }
        throw MessageNotFoundException("Message not found")
    }

    fun getUnreadChatsCount(): Int {
        var count = 0
        chats.forEach {
            chat -> if(chat.messages.any { message -> !message.isRead }) { count++ }
        }
        return count
    }

    fun getChats(userID: Int): List<Chat> {
        return chats.filter { it.userID == userID }
    }

    fun getMessagesFromChat(chatID: Int, messageID: Int, count: Int): List<Message>? {
        val chat = chats.find { chat -> chat.ID == chatID }
        if (chat != null) {
            val messages = chat.messages
            val index = messages.indexOfFirst { message -> message.ID == messageID }
            messages.slice(index until messages.size).take(count).forEach { it.isRead = true }
            return messages.slice(index until messages.size).take(count)
        }
        throw ChatNotFoundException("Chat not found")
    }

    fun createMessage(userID: Int, text: String): Message {
        return Message(
            ID = messageID++,
            text = text,
            data = Clock.System.now().epochSeconds,
            isRead = false,
            userID = userID
        )
    }
}