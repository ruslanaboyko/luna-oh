import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

object owoifier {
  def start(api: DiscordApi): Unit = {
    api.addMessageCreateListener((message: MessageCreateEvent) => {
      if(message.getMessageContent.startsWith(Luna.prefix + "owo")) {
        message.getChannel.sendMessage(message.getMessageContent
          .replace("L", "W")
          .replace("l","w")
          .replace("R", "W")
          .replace("r", "w")
          .replace("!owo", ""))
      }
    })
  }
}
