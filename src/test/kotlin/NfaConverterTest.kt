import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class NfaConverterTest {
    fun generateStrings(n: Int, alphabet: String): List<String> {
        val binaryStrings = mutableListOf<String>()
        generateStringsRec(n, alphabet, "", binaryStrings)
        return binaryStrings
    }

    fun generateStringsRec(n: Int, alphabet: String, prefix: String, binaryStrings: MutableList<String>) {
        if (n == 0) {
            binaryStrings.add(prefix)
        } else {
            for (char in alphabet) {
                generateStringsRec(n - 1, alphabet, prefix + char, binaryStrings)
            }
        }
    }

    @Test
    fun test() {
        val nfa = Nfa("./sample/nfa/1.txt")
        NfaConverter("./sample/nfa/1.txt").convert("./sample/res/1-dfa.txt")
        val dfa = Nfa("./sample/res/1-dfa.txt")

        val strings = generateStrings(10, "01")

        for (string in strings) {
            assertEquals(nfa.validate(string), dfa.validate(string))
        }
    }
}