import io.BufferedSource
import io.Source.fromResource             // this can "see" the resources folder
import io.StdIn.readLine
import util.Random.between
import util.control.Breaks._
import cats.effect.*

type Hand = Map[Char, Int]
type Words = List[String]

val FILENAME: String = "words.txt"
val VOWELS: String = "aeiou"
val CONSONANTS: String = "bcdfghjklmnpqrstvwxyz"
val HANDSIZE: Int = 10
val VOWELRATIO: Int = 3
val BONUS: Int = 50
val SCRABBLE: Hand = Map(
  'a' -> 1, 'b' -> 3, 'c' -> 3, 'd' -> 2, 'e' -> 1, 'f' -> 4, 'g' -> 2,
  'h' -> 4, 'i' -> 1, 'j' -> 8, 'k' -> 5, 'l' -> 1, 'm' -> 3, 'n' -> 1,
  'o' -> 1, 'p' -> 3, 'q' -> 10, 'r' -> 1, 's' -> 1, 't' -> 1, 'u' -> 1,
  'v' -> 4, 'w' -> 4, 'x' -> 8, 'y' -> 4, 'z' -> 10)


/**
 * @param filename string name of a file in /src/main/resources, like "words.txt"
 *                 each line in file contains exactly one word.
 * @return list of words in the file.
 */
def loadWords(filename: String): Words =
  val file: BufferedSource = fromResource(filename)
  val words: Words = file.getLines.toList
  file.close
  println(f"${words.size} words loaded.")
  words


/**
 * @param word String e.g. "hello"
 * @return map of char -> int pairs indicating how many times a letter occurs
 *         e.g. Map('h' -> 1, 'e' -> 1, 'l' -> 2, 'o' -> 1)
 */
def getFreqMap(word: String): Hand = word
  .groupBy(identity)
  .view.mapValues(_.length)
  .toMap


/**
 * @param word string (the word that is played in that hand)
 * @param num integer (usually HANDSIZE)
 * @return the score for the word. It is calculated as follows:
 *         sum of scrabble scores of each letter in word,
 *         multiplied by the length of the word,
 *         plus a potential bonus (if max number of letters were used).
 */
def getWordScore(word: String, num: Int): Int =
  val letterScores: Int = (for letter <- word yield SCRABBLE(letter)).sum
  letterScores * word.length + (if word.length == num then BONUS else 0)


/**
 * @param hand map of char, integer pairs; like 'a' -> 5
 *             prints each letter as many times as they appear in hand.
 */
def displayHand(hand: Hand): Unit =
  val letters: Iterable[String] =
    for (letter, count) <- hand
    yield f"$letter " * count
  println(f"Current hand: ${letters.mkString}")


/**
 * @param handSize integer, the total number of letters in hand being dealt
 * @param numVowels integer, the number of vowels in hand being dealt
 * @return hand: a map of char -> int pairs, showing how many of each letter.
 */
def dealHand(handSize: Int, numVowels: Int): Hand =
  val vowels: Iterable[Char] =
    for _ <- 1 to numVowels
    yield VOWELS(between(0, VOWELS.size))

  val consonants: Iterable[Char] =
    for _ <- numVowels + 1 to handSize
    yield CONSONANTS(between(0, CONSONANTS.size))

  getFreqMap(vowels.mkString + consonants.mkString)


/**
 * @param hand current hand: map of char -> int pairs
 * @param word currently guessed word to be removed from hand.
 *             assume all letters in word (however many times) appear in hand.
 * @return updated hand with letters of word removed from hand.
 */
def updateHand(hand: Hand, word: String): Hand =
  val handFromWord: Hand = getFreqMap(word)
  hand.map((letter, count) => handFromWord.get(letter) match
    case Some(num) => (letter, count - num)
    case None => (letter, count))


/**
 * @param word String, like "in"
 * @param hand Hand, like Map('e' -> 2, 'i' -> 1, 'n' -> 3, 'c' -> 1)
 * @param wordList List of ALL UPPERCASE Strings, a dictionary to look up word
 * @return true if word consists entirely of letters in hand, and is in wordList
 */
def isValidWord(word: String, hand: Hand, wordList: Words): Boolean =
  val wordHand: Hand = getFreqMap(word)
  val wordIsInHand: Boolean = word.forall(char =>
    hand.contains(char) && wordHand(char) <= hand(char))
  wordIsInHand && wordList.contains(word.toUpperCase)


/**
 * @param hand a Map of char, int pairs. Hand of letters to play.
 * @param words the dictionary to look up valid words.
 * @param num hand size. Needed to calculate score.
 *            Plays one hand of the word guessing game.
 *            Uses var (mutation), breakable while loops and break keyword.
 */
def playHand(hand: Hand, words: Words, num: Int): Unit =
  var score: Int = 0
  var currentHand: Hand = hand

  breakable(
    while
      currentHand.values.sum > 0
    do
      displayHand(currentHand)
      val word: String = readLine("Enter a word, or a . to indicate you are finished: ")
      if word == "." then
        break
      else if !isValidWord(word, hand, words) then
        println("Invalid word, please try again.")
      else
        val wordScore: Int = getWordScore(word, num)
        score += wordScore
        println(f"$word earned $wordScore points. Total: $score points.")
        currentHand = updateHand(currentHand, word)
  )
  if currentHand.values.sum == 0 then
    println(f"Ran out of letters. Total score: $score points.")
  else
    println(f"Goodbye! Total score: $score points.")


/**
 * @param hand a Map of char, int pairs. Hand of letters to play.
 * @param words the dictionary to look up valid words.
 * @param num hand size. Needed to calculate score.
 *            Loopless, var-less, recursive version of playHand.
 *            Still has side effects unwrapped in IO.
 */
def playHand2(hand: Hand, words: Words, num: Int): Unit =
  def helper(score: Int, currentHand: Hand): Unit =
    if currentHand.values.sum == 0 then
      println(f"Ran out of letters. Total score: $score points.")
    else
      displayHand(currentHand)
      val word: String = readLine("Enter a word, or a . to indicate you are finished: ")

      if word == "." then
        println(f"Goodbye! Total score: $score points.")

      else if !isValidWord(word, hand, words) then
        println("Invalid word, please try again.")
        helper(score, currentHand)

      else
        val wordScore: Int = getWordScore(word, num)
        val newScore: Int = score + wordScore
        val newHand: Hand = updateHand(currentHand, word)
        println(f"$word earned $wordScore points. Total: $newScore points.")
        helper(newScore, newHand)

  helper(0, hand)


/**
 * @param words list of strings, a dictionary to look up words.
 * @param handSize integer, the number of letters in a hand
 * @param vowelRatio divide handSize by this to find how many should be vowels.
 *                   uses var (mutation) and nested while loops.
 */
def playGame(words: Words, handSize: Int, vowelRatio: Int): Unit =
  var lastHand: Hand = Map()
  var command: String = ""

  while
    true
  do
    command = readLine("Enter n to deal a new hand, r to replay last hand, or e to end the game: ")
    while
      command == "r" && lastHand.isEmpty
    do
      println("You have not played a hand yet. Please play a new hand first!")
      command = readLine("Enter n to deal a new hand, r to replay last hand, or e to end the game: ")
    if command == "n" then
      val newHand = dealHand(handSize, handSize / vowelRatio)
      lastHand = newHand
      playHand(newHand, words, handSize)
    if command == "r" then
      playHand(lastHand, words, handSize)
    if command == "e" then
      return
    if !List("e", "n", "r").contains(command) then
      println("Invalid command.")


/**
 * @param words list of strings, a dictionary to look up words.
 * @param handSize integer, the number of letters in a hand
 * @param vowelRatio divide handSize by this to find how many should be vowels.
 *                   Loopless, var-less, recursive version of playGame.
 *                   Still has side effects unwrapped in IO.
 */
def playGame2(words: Words, handSize: Int, vowelRatio: Int): Unit =
  def helper(command: String, lastHand: Hand): String =
    if command == "r" && lastHand.isEmpty then
      println("You have not played a hand yet. Please play a new hand first!")
      val newCommand: String =
        readLine("Enter n to deal a new hand, r to replay last hand, or e to end the game: ")
      helper(newCommand, lastHand)
    else command

  def helper2(lastHand: Hand): Unit =
    val firstCommand: String =
      readLine("Enter n to deal a new hand, r to replay last hand, or e to end the game: ")
    val command: String = helper(firstCommand, lastHand)
    if command == "n" then
      val newHand = dealHand(handSize, handSize / vowelRatio)
      playHand2(newHand, words, handSize)
      helper2(newHand)
    if command == "r" then
      playHand2(lastHand, words, handSize)
      helper2(lastHand)
    if command == "e" then
      ()
    if !List("e", "n", "r").contains(command) then
      println("Invalid command.")
      helper2(lastHand)

  helper2(Map())


object Main extends IOApp.Simple:
  val run: IO[Unit] =
    for
      _ <- IO(playGame2(loadWords(FILENAME), HANDSIZE, VOWELRATIO))
    yield ()
