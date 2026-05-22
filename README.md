# Barebones interpreter

This project was written in my first year at the University of Southampton as part of a series of programming challenges called "Space cadets". The 2nd/3rd part of the challenge involved writing
an interpreter for a simple barebones language in Java.

## Files

### `Main.java`

The `Main.java` file is simply responsible for creating a new instance of our interpretator, loading in the user specified file, and executing it.

### `Memory.java`

`Memory.java` contains the class `Memory`, which is a custom dynamic data storage class that works like a simple key-value database. It stores pairs of
index names and data values using arrays, keeps them sorted alphabetically, and uses binary search for fast lookups. The class supports adding, retrieving,
modifying, removing, and displaying stored entries while automatically resizing the arrays as needed.

### `Interpreter.java`

`Interpreter.java` contains the class `Interpretator`, which is a simple custom interpreter that reads and executes a small barebones command-based language.
It stores variables using a custom Memory structure and queues commands from a file or input line-by-line for execution. The interpreter supports basic operations
like variable creation, increment/decrement, input/output, conditional if statements, and while loops, using regex pattern matching to identify each command type.
It processes commands sequentially with a pointer-based execution flow and handles control structures by evaluating conditions and repeatedly executing
blocks of queued instructions when needed.

### `sample-program.bb`

This file contains a simple program written in our barebones language. It multiplies two user-input numbers using only basic operations. It first clears and
initialises variables, then asks the user to enter two numbers stored in `in1` and `in2`. These values are copied into counters `X` and `Y` using loops,
then multiplication is performed through repeated addition: for each decrement of `X`, the program repeatedly increments `Z` by `Y`, using `W` as temporary
storage to restore `Y` after each inner loop. Finally, it prints `Z`, which contains the product of the two input numbers.
