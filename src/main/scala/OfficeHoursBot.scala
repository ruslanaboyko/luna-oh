import org.javacord.api.DiscordApi
import org.javacord.api.entity.user.User
import org.javacord.api.event.message.MessageCreateEvent

import scala.collection.immutable.Queue

object OfficeHoursBot {
  var OHQueue: Queue[User] = Queue()

  def start(api: DiscordApi): Unit = {
    api.addMessageCreateListener((message: MessageCreateEvent) => {
      if (message.getMessageAuthor.asUser.get != api.getYourself && message.getChannel.asServerTextChannel.get.getName == Luna.OH_QUEUE_CHANNEL) {
        if (message.getMessageContent.startsWith(Luna.PREFIX + "enqueue") ||
          message.getMessageContent.startsWith(Luna.PREFIX + "E")) enqueue(message)
        else if (message.getMessageContent.startsWith(Luna.PREFIX + "leave") ||
          message.getMessageContent.startsWith(Luna.PREFIX + "L")) leave(message)
        else if (message.getMessageContent.startsWith(Luna.PREFIX + "show") ||
          message.getMessageContent.startsWith(Luna.PREFIX + "S")) show(message)
        else if ((message.getMessageContent.startsWith(Luna.PREFIX + "dequeue") ||
          message.getMessageContent.startsWith(Luna.PREFIX + "D")) &&
          message.getMessageAuthor.asUser.get.getRoles(message.getServer.get).contains(message.getServer.get.getRolesByName("TA").get(0))) dequeue(message)
        else if ((message.getMessageContent.startsWith(Luna.PREFIX + "clear") ||
          message.getMessageContent.startsWith(Luna.PREFIX + "C")) &&
          message.getMessageAuthor.asUser.get.getRoles(message.getServer.get).contains(message.getServer.get.getRolesByName("TA").get(0))) clear(message)
        else if (message.getMessageContent.startsWith(Luna.PREFIX + "help") ||
          message.getMessageContent.startsWith(Luna.PREFIX + "H")) message.getChannel.sendMessage(
          "To enqueue yourself, send a \"!enqueue\" or \"!E\" message.\n" +
            "To see the current Queue, send a \"!show\" or \"!S\" message.\n" +
            "TA's will dequeue you with a \"!dequeue\" or \"!D\" message.\n" +
            "To leave the queue, you can use \"!leave\" or \"!L\".")
      }
    })
  }

  def dequeue(message: MessageCreateEvent): Unit = {
    if (OHQueue.nonEmpty) {
      val userInFront: User = OHQueue.front
      OHQueue = OHQueue.filter(_ != OHQueue.front)
      message.getChannel.sendMessage(s"${userInFront.getMentionTag}, you are next! ${message.getMessageAuthor.asUser.get.getMentionTag} is available to help you now!")
    }
    else message.getChannel.sendMessage("Good job TAs! The queue is empty!")
  }

  def clear(message: MessageCreateEvent): Unit = {
    OHQueue = Queue()
    message.getChannel.sendMessage("The queue has been cleared.")
  }

  def show(message: MessageCreateEvent): Unit = {
    if (OHQueue.nonEmpty) {
      val extractedNames: Seq[String] = for (member <- OHQueue) yield member.getDisplayName(message.getServer.get)
      //      message.getChannel.sendMessage(s"The queue has the following students, in order:\n" +
      //        s"${extractedNames.mkString("\n")}")
      // With numbering and sanitized names:
      val queueOrderMessage: StringBuilder = new StringBuilder("The queue has the following students, in order:\n")
      for (((name), i) <- extractedNames.zipWithIndex) {
        queueOrderMessage.append(s"${i + 1}. ${Sanitizer.cleanName(name)}")
      }
      message.getChannel.sendMessage(queueOrderMessage.toString())
    }
    else {
      message.getChannel.sendMessage("The queue is currently empty.")
    }
  }

  def enqueue(message: MessageCreateEvent): Unit = {
    val author: User = message.getMessageAuthor.asUser.get
    val authorDiscriminatedName: String = message.getMessageAuthor.asUser.get.getMentionTag
    if(!message.getMessageAuthor.asUser.get.getRoles(message.getServer.get).contains(message.getServer.get.getRolesByName("TA").get(0))) {
      if (!OHQueue.contains(author)) {
        OHQueue = OHQueue.enqueue(author)
        // OHWaitTime :+= (author, (System.nanoTime(), null))
        if (OHQueue.length == 1) {
          message.getChannel.sendMessage(s"${authorDiscriminatedName}, you have been successfully added to the queue and you are first in line!")
        }
        else {
          message.getChannel.sendMessage(s"${authorDiscriminatedName}, you have been successfully added to the queue and you are in position: ${OHQueue.length}")
        }
      }
      else {
        OHQueue = OHQueue.filter(_ != author)
        OHQueue = OHQueue.enqueue(author)
        message.getChannel.sendMessage(s"${authorDiscriminatedName}, you were already in the queue! You have been moved to the back.")
      }
    }
    else {
      message.getChannel.sendMessage("Meow?")
    }
  }

  def leave(message: MessageCreateEvent): Unit = {
    val author: User = message.getMessageAuthor.asUser.get
    val authorDiscriminatedName: String = message.getMessageAuthor.asUser.get.getMentionTag
    OHQueue = OHQueue.filter(_ != author)
    message.getChannel.sendMessage(s"${authorDiscriminatedName}, if you were present in the queue, you have been removed.")
  }
}