import kotlinx.datetime.Clock
import org.junit.After
import org.junit.Test

import org.junit.Assert.*

class ChatServiceTest {

    private val user1 = 1
    private val user2 = 999

    @After
    fun clearList() {
        ChatService.chats.clear()
        ChatService.chatID = 0
        ChatService.messageID = 0
    }

    @Test
    fun addChat_shouldAddChat() {
        ChatService.addChat(user1)

        assertTrue(ChatService.chats.isNotEmpty())
    }

    @Test
    fun deleteChat_shouldDeleteChat() {
        ChatService.addChat(user1)

        ChatService.deleteChat(user1)

        assertTrue(ChatService.chats.isEmpty())
    }

    @Test(expected = ChatNotFoundException::class)
    fun deleteChat_shouldThrow() {
        ChatService.addChat(user1)

        ChatService.deleteChat(user2)
    }

    @Test
    fun addMessage_shouldAddMessage() {
        val message = ChatService.createMessage(user1, "Hello")
        val chatID = ChatService.addChat(user1)

        ChatService.addMessage(user1, chatID, message)

        assertTrue(ChatService.chats[0].messages.isNotEmpty())
    }

    @Test(expected = ChatNotFoundException::class)
    fun addMessage_shouldThrow() {
        val message = ChatService.createMessage(user1, "Hello")
        val chatID = ChatService.chatID

        ChatService.addMessage(user1, chatID, message)
    }

    @Test
    fun editMessage_shouldEditMessage() {
        val message = ChatService.createMessage(user1, "Hello")
        val chatID = ChatService.addChat(user1)
        ChatService.addMessage(user1, chatID, message)

        val result = ChatService.editMessage("Hello, friend", message.ID)

        assertTrue(result)
    }

    @Test(expected = MessageNotFoundException::class)
    fun editMessage_shouldThrow() {
        ChatService.addChat(user1)
        val messageID = ChatService.messageID

        ChatService.editMessage("Hello, friend", messageID)
    }

    @Test
    fun deleteMessage_shouldDeleteMessage() {
        val message1 = ChatService.createMessage(user1, "Hello")
        val message2 = ChatService.createMessage(user2, "Hello, friend")
        val chatID = ChatService.addChat(user1)
        ChatService.addMessage(user1, chatID, message1)
        ChatService.addMessage(user1, chatID, message2)

        val result1 = ChatService.deleteMessage(message1.ID)
        val result2 = ChatService.deleteMessage(message2.ID)

        assertTrue(result1)
        assertTrue(result2)
    }

    @Test(expected = MessageNotFoundException::class)
    fun deleteMessage_shouldThrow() {
        val message1 = ChatService.createMessage(user1, "Hello")
        val message2 = ChatService.createMessage(user2, "Hello, friend")
        val chatID = ChatService.addChat(user1)
        ChatService.addMessage(user1, chatID, message1)
        ChatService.addMessage(user1, chatID, message2)
        val messageID = -1

        ChatService.deleteMessage(messageID)
    }

    @Test
    fun getUnreadChatsCount() {
        val message1 = ChatService.createMessage(user1, "Hello")
        val message2 = ChatService.createMessage(user2, "Hello, friend")
        val chatID = ChatService.addChat(user1)
        ChatService.addMessage(user1, chatID, message1)
        ChatService.addMessage(user1, chatID, message2)

        val result = ChatService.getUnreadChatsCount()

        assertEquals(result, 1)
    }

    @Test
    fun getChats() {
        ChatService.addChat(user1)

        val result = ChatService.getChats(user1)

        assertTrue(result.isNotEmpty())
    }

    @Test
    fun getMessagesFromChat_shouldGetMessagesFromChat() {
        val message1 = ChatService.createMessage(user1, "Hello")
        val message2 = ChatService.createMessage(user1, "Hello, friend")
        val chatID = ChatService.addChat(user1)
        ChatService.addMessage(user1, chatID, message1)
        ChatService.addMessage(user1, chatID, message2)

        val result = ChatService.getMessagesFromChat(chatID, message1.ID, 2)

        assertTrue(result!!.isNotEmpty())
    }

    @Test(expected = ChatNotFoundException::class)
    fun getMessagesFromChat_shouldThrow() {
        val message1 = ChatService.createMessage(user1, "Hello")
        val message2 = ChatService.createMessage(user1, "Hello, friend")
        val chatID = ChatService.addChat(user1)
        ChatService.addMessage(user1, chatID, message1)
        ChatService.addMessage(user1, chatID, message2)

        ChatService.getMessagesFromChat(chatID = 999, message1.ID, 2)
    }

    @Test
    fun createMessage() {
        val message = Message(
            ID = 0,
            text = "Hello",
            data = Clock.System.now().epochSeconds,
            isRead = false,
            userID = 1
        )

        val result = ChatService.createMessage(user1, "Hello")

        assertEquals(message, result)
    }
}