import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

object jessefier {

  def replaceRandomWordsWithAnotherWord(percent: Int, message: String, targetWord: String): String = {
    val newParts: ArrayBuffer[String] = new ArrayBuffer[String]()
    for (word <- message.split(" ")) {
      if (Random.nextInt(100) <= percent) {
        newParts.append(targetWord) // Replace this word with the target word
      } else {
        newParts.append(word) // Keep the original word
      }
    }
    newParts.mkString(" ")
  }

  def start(api: DiscordApi): Unit = {
    api.addMessageCreateListener((message: MessageCreateEvent) => {
      if (message.getMessageContent.startsWith(Luna.prefix + "jessefy")) {
        if (message.getMessageAuthor.asUser.get != api.getYourself) {
          message.getChannel.sendMessage(Sanitizer.removeMentions(message.getMessageContent.replace("Hughes", "Jesse")))
        }
      }

      else if (message.getMessageContent.startsWith(Luna.prefix + "superjessefy")) {
        if (message.getMessageAuthor.asUser.get != api.getYourself) {
          message.getChannel.sendMessage(Sanitizer.removeMentions(replaceRandomWordsWithAnotherWord(23, message.getMessageContent, "Jesse")))
        }
      }

    })
  }
}
