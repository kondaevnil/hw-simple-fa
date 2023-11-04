import java.io.File

class DfaMinimization(dfaConfig: String) {
    private var stateCount: Int = 0
    private var alphabetSize: Int = 0
    private var acceptStates: MutableSet<Int> = mutableSetOf()
    private var startState: Int
    private var transitionFunction: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
    private var graph: MutableMap<Int, MutableList<Int>> = mutableMapOf()

    init {
        val fileLines = File(dfaConfig).readLines()

        if (fileLines.size < 5)
            throw IllegalArgumentException("чото с форматом не так")

        stateCount = fileLines[0].toInt()
        alphabetSize = fileLines[1].toInt()
        startState = fileLines[2].toInt()
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

            if (graph.containsKey(parsedLine[0])) {
                graph[parsedLine[0]]!!.add(parsedLine[2])
            } else {
                graph[parsedLine[0]] = mutableListOf(parsedLine[2])
            }

            transitionFunction[Pair(parsedLine[0], parsedLine[1])] = parsedLine[2]
        }

        println("Loaded!")
    }

    fun minimize(filename: String) {
        val result = meow()

        val file = File(filename)
        file.appendText(result.size.toString() + "\n")
        file.appendText(alphabetSize.toString() + "\n")
        file.appendText(startState.toString() + "\n")
        file.appendText(result.filter { acceptStates.contains(it) } .joinToString(" ") + "\n")
        for (func in transitionFunction.filter { result.contains(it.key.first) }) {
            file.appendText("%d %d %d\n".format(func.key.first, func.key.second, func.value))
        }
    }

    private fun meow(): MutableSet<Int> {
        val visited = Array(stateCount) { false }
        dfs(startState, visited)

        for (pair in transitionFunction.keys) {
            if (!visited[pair.first])
                transitionFunction.remove(pair)
        }

        var canSplit = true
        var split = false;
        val tmpClasses = mutableListOf(acceptStates, graph.keys.filter { !acceptStates.contains(it) }.toMutableSet() )

        while (canSplit) {
            canSplit = false
            split = false

            for (eqClass in tmpClasses) {
                for (letter in 0..< alphabetSize) {
                    val newClasses = mutableMapOf<MutableSet<Int>, MutableSet<Int>>()
                    for (q in eqClass) {
                        for (c in tmpClasses) {
                            if (transitionFunction[Pair(q, letter)] in c) {
                                if (newClasses.containsKey(c)) {
                                    newClasses[c]!!.add(q)
                                } else {
                                    newClasses[c] = mutableSetOf(q)
                                }
                                break
                            }
                        }
                    }

                    if (newClasses.size > 1) {
                        tmpClasses.remove(eqClass)
                        for (newClass in newClasses.values)
                            tmpClasses.add(newClass)
                        canSplit = true
                        split = true
                    }
                }

                if (split) {
                    break
                }
            }
        }

        return tmpClasses.map { it.first() }.toMutableSet()
    }

    private fun dfs(v: Int, visited: Array<Boolean>) {
        visited[v] = true

        for (u in graph[v]!!) {
            if (!visited[u])
                dfs(u, visited)
        }
    }
}