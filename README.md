# hw-simple-fa
Theoretical computer science. Homework task 3. Finite machines and friends

## Description
A simple console application capable of simulating the operation of an NFA and converting NFA to DFA using the "Subset Construction" method.

## Usage
Program arguments: ``check/convert <in.file> <string>/<out_file_name>``
### Check
Displays whether the given string is recognized automatically
### Convert
Creates a DFA file with the format described below
## Format of ``<in.file>``
``n // number of states``

``m // alphabet size``

``0 2 3 // numbers of initial states separated by spaces``

``0 2 3 // numbers of final states separated by spaces``

``2 2 4 // transition using the symbol "2" from state 2 to state 4``

``...``

``3 0 1 // transition using the symbol "0" from state 3 to state 1``

*Numbering for states: Q = {0, 1, ..., n − 1}*

*Alphabet: Σ = {0, 1, ..., m − 1}*

