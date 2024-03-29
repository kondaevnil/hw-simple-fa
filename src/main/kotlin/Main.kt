fun main(args: Array<String>) {
    when (args.size) {
        3 -> {
            if (args[0] == "check") {
                val nfa = Nfa(args[0])
                println("Checking!")
                println(nfa.validate(args[1]))
            } else if (args[0] == "convert") {
                val converter = NfaConverter(args[1])
                println("Converting!")
                converter.convert(args[2])
                println("Done!")
            } else if (args[0] == "regex") {
                // var vm = RegexVm(args[1])
            } else if (args[0] == "minimize") {
                val min = DfaMinimization(args[1])
                min.minimize(args[2])
            } else {
                println("Illegal program")
            }
        }
        else -> {
            println("Invalid arguments count")
        }
    }
}