# Advent of Code 2023 in Java 21

This currently works for up to Day 7, part 1. Part 2 has a solution which probably works, but is impractically slow. At the moment I need to focus my time on work and other things, so this is where it's getting left for now.

### Objectives:

- Use Java 21
- Don't suck

### Building

This project uses make to define standard build targets, and Gradle as its Java build tool. Why use make instead of just building straight through Gradle? It allows me to define the "standard" way to build the project with a clearly-enumerated list of operations, and allows additional steps to be added to tasks easily.

**Requirements**

- Make version 4.0 or later
- Bash shell
- JDK version 21 or later available to Gradle (via system `PATH` or `JAVA_HOME`)

If you can read basic commands out of a makefile and are somewhat familiar with Gradle, you can skip the first two requirements.

**To build**

```
make
```

or

```
make build
```

**To run tests**

```
make test
```

**To run**

```
make run
```

### Notes

- This iteration of Advent of Code was originally done with GraalVM to experiment with building native executables. That was done, but given the slow compile times and the limited number of program runs you do with Advent problems, it really wasn't worth keeping up.
