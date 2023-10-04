import java.io.File
import java.util.concurrent.atomic.AtomicReference

class Nfa(programFile: String) {
    private var stateCount: Int = 0
    private var alphabetSize: Int = 0
    private var acceptStates: MutableSet<Int> = mutableSetOf()
    private var startStates: MutableList<Int> = mutableListOf()
    private var transitionFunction: MutableMap<Pair<Int, Int>, MutableList<Int>> = mutableMapOf()

    init {
        val fileLines = File(programFile).readLines()

        if (fileLines.size < 5)
            throw IllegalArgumentException("чото с форматом не так")

        stateCount = fileLines[0].toInt()
        alphabetSize = fileLines[1].toInt()
        startStates = fileLines[2].split(" ").map { it.toInt() }.toMutableList()
        acceptStates = fileLines[3].split(" ").map { it.toInt() }.toMutableSet()

        for (line in fileLines.drop(4)) {
            val parsedLine = line.split(" ").map { it.toInt() }

            if (parsedLine.size != 3)
                throw IllegalArgumentException("чото с форматом не так")

            if (parsedLine[0] < 0 || parsedLine[0] >= stateCount
                || parsedLine[1] < 0 || parsedLine[1] >= alphabetSize
                || parsedLine[2] < 0 || parsedLine[2] >= stateCount) {
                throw IllegalArgumentException("Некорректная функция перехода")
            }

            if (transitionFunction.containsKey(Pair(parsedLine[0], parsedLine[1]))) {
                transitionFunction[Pair(parsedLine[0], parsedLine[1])]!!.add(parsedLine[2])
            } else {
                transitionFunction[Pair(parsedLine[0], parsedLine[1])] = mutableListOf(parsedLine[2])
            }
        }

        println("Loaded!")
    }

    fun validate(string: String): Boolean {
        val ref = AtomicReference(string)

        for (state in startStates) {
            if (iteration(state, 0, ref)) {
                return true
            }
        }

        return false
    }

    private fun iteration(state: Int, position: Int, ref: AtomicReference<String>): Boolean {
        if (ref.get().length == position) {
            return state in acceptStates
        }

        for (newState in transitionFunction.getOrDefault(Pair(state, ref.get()[position] - '0'), mutableListOf())) {
            if (iteration(newState, position + 1, ref))
                return true
        }

        return false
    }
}