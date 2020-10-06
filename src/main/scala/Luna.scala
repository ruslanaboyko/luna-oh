import better.files._
import org.javacord.api.{DiscordApi, DiscordApiBuilder}

object Luna {
  val tokenFile: String = "token.txt"
  //  val serversFile: String = "servers.json"
  val prefix: Char = '!'

  def main(args: Array[String]): Unit = {
    val api: DiscordApi = new DiscordApiBuilder().setToken(readToken(File(tokenFile))).login.join
    //    val serverList: Map[String, String] = {
    //      if (File(serversFile).notExists) {
    //        File(serversFile).createFileIfNotExists()
    //        File(serversFile).append("{}")
    //        Map()
    //      }
    //      else {
    //        Json.parse(File(serversFile).contentAsString).as[Map[String, String]]
    //      }
    //    }

    OfficeHoursBot.start(api)
    owoifier.start(api)

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
