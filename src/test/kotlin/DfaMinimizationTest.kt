import org.junit.jupiter.api.Test

class DfaMinimizationTest {
    @Test
    fun dfaMinimizationTest() {
        DfaMinimization("./sample/dfa-min/1-dfa.txt").minimize("./sample/res/min.txt")
    }
}