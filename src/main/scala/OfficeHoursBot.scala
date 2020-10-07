import org.javacord.api.DiscordApi
import org.javacord.api.entity.user.User
import org.javacord.api.event.message.MessageCreateEvent

import scala.collection.immutable.Queue

object OfficeHoursBot {
  var OHQueue: Queue[User] = Queue()

  def start(api: DiscordApi): Unit = {
    api.addMessageCreateListener((message: MessageCreateEvent) => {
      if (message.getMessageAuthor.asUser.get != api.getYourself && message.getChannel.asServerTextChannel.get.getName == "office-hours-queue") {
        if (message.getMessageContent.startsWith(Luna.prefix + "enqueue") ||
          message.getMessageContent.startsWith(Luna.prefix + "E")) enqueue(message)
        else if (message.getMessageContent.startsWith(Luna.prefix + "leave") ||
          message.getMessageContent.startsWith(Luna.prefix + "L")) leave(message)
        else if (message.getMessageContent.startsWith(Luna.prefix + "show") ||
          message.getMessageContent.startsWith(Luna.prefix + "S")) show(message)
        else if (message.getMessageContent.startsWith(Luna.prefix + "pet") &&
          message.getMessageAuthor.asUser.get.getRoles(message.getServer.get).contains(message.getServer.get.getRolesByName("TA").get(0))) message.getChannel.sendMessage("purr")
        else if ((message.getMessageContent.startsWith(Luna.prefix + "dequeue") ||
          message.getMessageContent.startsWith(Luna.prefix + "D")) &&
          message.getMessageAuthor.asUser.get.getRoles(message.getServer.get).contains(message.getServer.get.getRolesByName("TA").get(0))) dequeue(message)
        else if ((message.getMessageContent.startsWith(Luna.prefix + "clear") ||
          message.getMessageContent.startsWith(Luna.prefix + "C")) &&
          message.getMessageAuthor.asUser.get.getRoles(message.getServer.get).contains(message.getServer.get.getRolesByName("TA").get(0))) clear(message)
        else if (message.getMessageContent.startsWith(Luna.prefix + "help") ||
          message.getMessageContent.startsWith(Luna.prefix + "H")) message.getChannel.sendMessage(
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
      message.getChannel.sendMessage(s"The queue has the following students, in order:\n" +
        s"${extractedNames.mkString("\n")}")
    }
    else {
      message.getChannel.sendMessage("The queue is currently empty.")
    }
  }


  def enqueue(message: MessageCreateEvent): Unit = {
    val author: User = message.getMessageAuthor.asUser.get
    val authorDiscriminatedName: String = message.getMessageAuthor.asUser.get.getMentionTag
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

  def leave(message: MessageCreateEvent): Unit = {
    val author: User = message.getMessageAuthor.asUser.get
    val authorDiscriminatedName: String = message.getMessageAuthor.asUser.get.getMentionTag
    OHQueue = OHQueue.filter(_ != author)
    message.getChannel.sendMessage(s"${authorDiscriminatedName}, if you were present in the queue, you have been removed.")
  }
}