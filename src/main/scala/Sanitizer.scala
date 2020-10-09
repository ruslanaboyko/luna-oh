object Sanitizer {

  /**
   * Takes someone's display name as an input and cleans it to remove formatting and links
   * This shouldn't tamper with any genuine user's names. It'll just stop trolls.
   *
   * @param name Their display name
   * @return A clean string of their display name.
   */
  def cleanName(name: String): String = {
    val cleanName: StringBuilder = new StringBuilder
    val needToEscape: Array[Char] = Array('*', '`', '~', '_', '>', '|', ':', '.', '\\', '/', '.', '\'', '@', '#') // Characters that need to have an escape character placed in front of them
    for (c <- name) {
      if (needToEscape.contains(c)) {
        // This char needs to be escaped
        cleanName.append(s"\\$c")
      } else {
        // This char doesn't need to be escaped
        cleanName.append(c)
      }
    }
    cleanName.toString()
  }

  /**
   * Removes @ mentions from a message
   *
   * @param message the input message to clean
   */
  def removeMentions(message: String): String = {
    message.replace("@", "")
  }


}
