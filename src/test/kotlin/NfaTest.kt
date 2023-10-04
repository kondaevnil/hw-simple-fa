import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import kotlin.test.assertEquals

class NfaTest {
    @Test
    fun loadFileTestOk() {
        try {
            Nfa("./sample/nfa/1.txt")
        } catch (_: Exception) { fail("Не должно падать..") }
    }

    @Test
    fun loadFileTestNotFound() {
        try {
            Nfa("./sample/nfa/1.p") // file not found
            fail("должно падать..")
        } catch (_: Exception) {  }
    }

    @Test
    fun loadFileTestIlligalState() {
        try {
            Nfa("./sample/nfa/bad-state.txt") // file not found
            fail("должно падать..")
        } catch (_: Exception) {  }
    }

    @Test
    fun loadFileTestIlligalLetter() {
        try {
            Nfa("./sample/nfa/bad-letter.txt") // file not found
            fail("должно падать..")
        } catch (_: Exception) {  }
    }

    @Test
    fun acceptStringTest1() {
        val nfa = Nfa("./sample/nfa/1.txt")
        assertEquals(true, nfa.validate("000000"))
    }

    @Test
    fun acceptStringTest2() {
        val nfa = Nfa("./sample/nfa/1.txt")
        assertEquals(true, nfa.validate("1100"))
    }

    @Test
    fun acceptStringTest3() {
        val nfa = Nfa("./sample/nfa/1.txt")
        assertEquals(true, nfa.validate("00100100"))
    }

    @Test
    fun acceptStringTest4() {
        val nfa = Nfa("./sample/nfa/1.txt")
        assertEquals(true, nfa.validate("0000"))
    }

    @Test
    fun rejectStringTest1() {
        val nfa = Nfa("./sample/nfa/1.txt")
        assertEquals(false, nfa.validate("1"))
    }

    @Test
    fun rejectStringTest2() {
        val nfa = Nfa("./sample/nfa/1.txt")
        assertEquals(false, nfa.validate("01"))
    }

    @Test
    fun rejectStringTest3() {
        val nfa = Nfa("./sample/nfa/1.txt")
        assertEquals(false, nfa.validate("00"))
    }

    @Test
    fun rejectStringTest4() {
        val nfa = Nfa("./sample/nfa/1.txt")
        assertEquals(false, nfa.validate("10"))
    }
}