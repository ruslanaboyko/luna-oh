import better.files._
import org.javacord.api.{DiscordApi, DiscordApiBuilder}

object Luna {
  val TOKEN_FILE: String = "token.txt"
  val PREFIX: Char = '!'
  val OH_QUEUE_CHANNEL: String = "office-hours-queue"
  val BOT_CHANNEL: String = "office-hours-queue"

  def main(args: Array[String]): Unit = {
    val api: DiscordApi = new DiscordApiBuilder().setToken(readToken(File(TOKEN_FILE))).login.join

    OfficeHoursBot.start(api)
    //owoifier.start(api)
    //jessefier.start(api)
    kittycat.start(api)
  }

  def readToken(tokenFile: File): String = {
    if (tokenFile.notExists) {
      tokenFile.createIfNotExists()
      throw new RuntimeException("Token not found.\n" +
        "Please insert your Discord bot token in ./token.txt and rerun the server.")
    }
    else {
      tokenFile.newBufferedReader.readLine
    }
  }
}
