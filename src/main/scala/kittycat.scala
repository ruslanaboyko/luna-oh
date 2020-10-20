import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

import scala.util.Random

object kittycat {
  val COMMAND_NAME: String = Luna.PREFIX + "superpet"
  val MAX_PETS: Int = 100 // The maximum number a user can specify for superpet

  // Get the purr message once there is a valid number to use
  def getSuperPetMessage(numberOfPets: Int): String = {
    val petMessage: StringBuilder = new StringBuilder
    // Puns from https://whiskerstotailspetsitting.com/2017/05/purr-fect-list-cat-vocabulary-not-kitten-around/
    val catPuns: Array[String] = Array(
      "Are you fur real?",
      "That was clawful!",
      "That was very clawver.",
      "Purrhaps you should try a positive integer.",
      "That's not a pawsible number.",
      "Try again, furend.",
      "Well that's unfurtunate.",
      "Meow try it with a real number",
      "I'm gonna need to paws you right there.",
      "Are you kittin me?",
      "Pawdon me, but that's not a valid number of pets.",
      "I'm not furmiliar with that number.",
      "That was not pawsome.",
      "Not with that catitude.",
      "I'm apawled.",
      "That would be a catastrophe.",
      "That's not a pawsitive integer."
    )

    if(numberOfPets <= 0) {
      // Return a random pun if they won't pet Luna
      return catPuns(Random.nextInt(catPuns.length - 1))
    }

    if (numberOfPets < 5) {
      for (_ <- 0 until numberOfPets) {
        petMessage.append("purr\n") // For short requests, put each purr on a new line
      }
    } else {
      for (_ <- 0 until (numberOfPets min MAX_PETS)) {
        petMessage.append("purr ") // For long requests, put each purr on the same line
      }
    }

    // If there are too many purrs, stop and add a message

    if (numberOfPets > MAX_PETS) {
      petMessage.append("\nMIAOW!!! That's too many! Calm down!")
    }

    petMessage.toString
  }

  def superPet(message: MessageCreateEvent): String = {
    var messageWithoutCommand: String = Sanitizer.removeCommand(message.getMessageContent)

    var numberOfPets: Int = 0

    val randInt: Int = Random.nextInt(100)
    val notPurrfectMessage: String = s"That wasn't a purrfect integer! Try something like `$COMMAND_NAME $randInt` for better luck!"

    try {
      if (messageWithoutCommand == "") {
        messageWithoutCommand = "1" // Default 1 purr
      }
      numberOfPets = messageWithoutCommand.toInt
    } catch {
      case _: Throwable =>
        return notPurrfectMessage
    }

    getSuperPetMessage(numberOfPets)
  }

  def start(api: DiscordApi): Unit = {
    api.addMessageCreateListener((message: MessageCreateEvent) => {
      if (message.getMessageAuthor.asUser.get != api.getYourself && message.getServerTextChannel.get.getName == Luna.BOT_CHANNEL) {
        if (message.getMessageContent.startsWith(Luna.PREFIX + "superpet")) {
          message.getChannel.sendMessage(superPet(message))
        }
        else if (message.getMessageContent.startsWith(Luna.PREFIX + "pet")) {
          message.getChannel.sendMessage("\\*purr*")
        }
        else if (message.getMessageContent.startsWith(Luna.PREFIX + "observe")) {
          message.getChannel.sendMessage("\\*sniff*")
        }
      }
    })
  }
}
