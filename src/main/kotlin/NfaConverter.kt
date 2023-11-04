import java.io.File

class NfaConverter(programFile: String) {
    private var stateCount: Int = 0
    private var alphabetSize: Int = 0
    private var acceptStates: MutableSet<Int> = mutableSetOf()
    private var startStates: MutableList<Int> = mutableListOf()
    private var transitionFunction: MutableMap<Pair<Int, Int>, MutableList<Int>> = mutableMapOf()

    private var convertedStates: MutableMap<MutableSet<Int>, Int> = mutableMapOf()
    private var convertedTransitionFunction: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
    private var convertedAcceptStates: MutableSet<Int> = mutableSetOf()
    private var statesCounter = 0

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

    fun convert(filename: String) {
        val ss = startStates.toMutableSet()
        convertedStates[ss] = statesCounter
        for (state in acceptStates) {
            if (ss.contains(state)) {
                convertedAcceptStates.add(statesCounter)
                break
            }
        }

        statesCounter++
        iteration(ss)

        val file = File(filename)
        file.writeText("")
        file.appendText(convertedStates.size.toString() + "\n")
        file.appendText(alphabetSize.toString() + "\n")
        file.appendText("0\n")
        file.appendText(convertedAcceptStates.joinToString(" ") + "\n")
        for (func in convertedTransitionFunction) {
            file.appendText("%d %d %d\n".format(func.key.first, func.key.second, func.value))
        }
    }

    private fun iteration(states: MutableSet<Int>) {
        for (letter in 0 ..< alphabetSize) {
            val newState = mutableSetOf<Int>()

            for (state in states) {
                if (transitionFunction.containsKey(Pair(state, letter))) {
                    newState.addAll(transitionFunction[Pair(state, letter)]!!)
                }
            }

            if (convertedStates.containsKey(newState)) {
                convertedTransitionFunction[Pair(convertedStates[states]!!, letter)] = convertedStates[newState]!!
                continue
            }

            convertedStates[newState] = statesCounter
            convertedTransitionFunction[Pair(convertedStates[states]!!, letter)] = statesCounter

            for (state in acceptStates) {
                if (newState.contains(state)) {
                    convertedAcceptStates.add(statesCounter)
                    break
                }
            }

            statesCounter++
            iteration(newState)
        }
    }
}