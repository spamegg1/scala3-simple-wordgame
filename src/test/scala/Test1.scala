import org.junit.Test
import org.junit.Assert.*

val wordList: Words = loadWords(FILENAME)

class WordScore: // name of the class does not matter as long as it's not "Test"
  @Test def `testing getWordScore`(): Unit =
    val wordScores: List[(String, Int)] = List(
      ("", 0),
      ("it", 4),
      ("was", 18),
      ("fork", 44),
      ("scored", 54),
      ("outgnaw", 127),
      ("waybill", 155)
    )
    for
      (word, score) <- wordScores
    do
      assertEquals(getWordScore(word, 7), score)

class UpdateHand:
  @Test def `testing updateHand: 1`(): Unit =
    val hand: Hand = Map(
      'a' -> 1, 'q' -> 1, 'l' -> 2, 'm' -> 1, 'u' -> 1, 'i' -> 1
    )
    val word: String = "quail"
    val expected: Hand = Map(
      'a' -> 0, 'q' -> 0, 'l' -> 1, 'm' -> 1, 'u' -> 0, 'i' -> 0
    )
    assertEquals(updateHand(hand, word), expected)

  @Test def `testing updateHand: 2`(): Unit =
    val hand: Hand = Map('e' -> 1, 'v' -> 2, 'n' -> 1, 'l' -> 2, 'i' -> 1)
    val word: String = "evil"
    val expected: Hand = Map('e' -> 0, 'v' -> 1, 'n' -> 1, 'l' -> 1, 'i' -> 0)
    assertEquals(updateHand(hand, word), expected)

  @Test def `testing updateHand: 3`(): Unit =
    val hand: Hand = Map('e' -> 1, 'h' -> 1, 'o' -> 1, 'l' -> 2)
    val word: String = "hello"
    val expected: Hand = Map('e' -> 0, 'h' -> 0, 'o' -> 0, 'l' -> 0)
    assertEquals(updateHand(hand, word), expected)

class IsValidWord:
  @Test def `testing isValidWord: 1`(): Unit =
    val word1: String = "hello"
    val hand1: Hand = Map('h' -> 1, 'e' -> 1, 'l' -> 2, 'o' -> 1)
    assertEquals(isValidWord(word1, hand1, wordList), true)

  @Test def `testing isValidWord: 2`(): Unit =
    val word2: String = "hello"
    val hand2: Hand = Map('h' -> 1, 'e' -> 1, 'l' -> 1, 'o' -> 1)
    assertEquals(isValidWord(word2, hand2, wordList), false)

  @Test def `testing isValidWord: 3`(): Unit =
    val word3: String = "rapture"
    val hand3: Hand = Map(
      'r' -> 1, 'a' -> 3, 'p' -> 2, 't' -> 1, 'u' -> 1, 'e' -> 1
    )
    assertEquals(isValidWord(word3, hand3, wordList), false)

  @Test def `testing isValidWord: 4`(): Unit =
    val word4: String = "rapture"
    val hand4: Hand = Map(
      'r' -> 2, 'a' -> 1, 'p' -> 1, 't' -> 1, 'u' -> 1, 'e' -> 1
    )
    assertEquals(isValidWord(word4, hand4, wordList), true)
