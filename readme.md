# Advent of Code 2023 in Java 21

### Objectives:

- Use Java 21
- Use GraalVM to generate native executables
- Don't suck

### Building

This project uses make to define standard build targets, and Gradle as its Java build tool. Why use make instead of just building straight through Gradle? It allows me to define the "standard" way to build the project with a clearly-enumerated list of operations, and allows additional steps to be added to tasks easily.

**Requirements**

- Make version 4.0 or later
- Bash shell
- GraalVM JDK version 21 available to Gradle

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
