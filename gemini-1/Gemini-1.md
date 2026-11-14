# Gemini Interfaces (OCS sample)


An educational Maven project that implements a lightweight Operational Control System (OCS) against the `GeminiAPI` interface. It models science plans and observing programs for the Gemini telescopes using simple in-memory storage and comes with unit tests and a runnable demo.


## Prerequisites
- Java 19+
- Maven 3.9+


## Project layout
All paths below are relative to `gemini-1/implementations/`.
- `ocs/src/main/java/edu/gemini` — concrete OCS implementation and domain classes.
- `ocs/src/main/java/edu/gemini/model` — abstract interfaces/base classes supplied by the API.
- `ocs/src/test/java` — JUnit 5 tests for creating, submitting and validating plans and for creating observing programs.
- `ocs/src/main/java/edu/gemini/Main.java` — runnable example wiring everything together.
- The Phase 2 designs (activity, sequence and class diagrams) are in `docs/design.md` at the repository root (see `gemini-1/designs/README.md`).


## Build, run, and test
From the repository root:
```bash
cd gemini-1/implementations/ocs
mvn test                     # run the JUnit 5 suite
mvn -DskipTests package      # build the jar
java -cp target/ocs-1.0-SNAPSHOT.jar edu.gemini.Main  # run the demo
```


### Step-by-step execution
1) `cd gemini-1/implementations/ocs` 
2) `mvn clean package` (use `mvn test` first if you want to only run tests) 
3) `java -cp target/ocs-1.0-SNAPSHOT.jar edu.gemini.Main` 
You should see the result of creating, testing, submitting and validating a science plan, then creating an observing program from the validated plan, followed by a summary. The demo plan targets the Gemini constellation (quadrant NQ2) on the Hawaii telescope (Gemini North).


### How to run in an IDE
- Clone the repo (or download the ZIP) and open the `gemini-1/implementations/ocs` module in Eclipse or IntelliJ.
- Ensure a Java SDK 19+ is configured for the project (code targets Java 19 in `pom.xml`).
- Run `src/main/java/edu/gemini/Main.java` to see the sample flow.


### Features
- Create science plans with validation and auto-numbering.
- Test and submit plans only after they pass testing.
- Record the astronomer as the plan creator and the science observer's id on the observing program.
- Validate submitted plans (minimal: `SUBMITTED` -> `VALIDATED`, or `INVALIDATED` if required details are missing).
- Create observing programs only from `VALIDATED` plans.
- Configure telescope optics, fold mirrors, light sources, and module content.
- Store telescope position pairs (direction/degree) with the observing program.


### Running tests
- Tests use JUnit 5 under `src/test/java`.
- From the command line: `mvn test`.
- From an IDE: right-click a test class and run as JUnit.


## Project structure
```
gemini-1/
├── Gemini-1.md
├── designs/README.md            # Points to docs/design.md
└── implementations/
    ├── LICENSE
    ├── readme.md
    └── ocs
        ├── pom.xml
        ├── src
        │   ├── main
        │   │   └── java/edu/gemini
        │   │       ├── Main.java                   # Demo entry point
        │   │       ├── OCS.java                    # GeminiAPI implementation (in-memory)
        │   │       ├── *.java domain classes       # SciencePlan, ObservingProgram, etc.
        │   │       └── model/*.java                # Abstract base types from the API
        │   └── test/java                           # JUnit 5 tests
        └── target                                  # Build output (generated)
```


## Implemented behavior
- `edu.gemini.OCS` implements `GeminiAPI` with in-memory plan storage, auto-incremented plan numbers, validation when creating plans (required name, positive funding, date order, unique name), testing (`CREATED -> TESTED`), submission gating on `TESTED`, status updates, and deletion helpers.
- `validateSciencePlan` (minimal) accepts only a `SUBMITTED` plan and sets `VALIDATED` when plan number, name, creator, funding and objective are present, otherwise `INVALIDATED`. Star-system checks (quadrant vs. telescope, observing month) are not implemented.
- `createObservingProgram` requires a `VALIDATED` plan and a science observer, assembles an `ObservingProgram` from the plan, optical settings, and telescope position pairs, marks it `CREATED`, and stores it in memory (`getObservingProgramBySciencePlan` returns it).
- Domain models (`SciencePlan`, `ObservingProgram`, `Astronomer`, `ScienceObserver`, `TelePositionPair`, etc.) extend the abstract types supplied in `model`.
- `Main` demonstrates creating a plan, testing, submitting and validating it, and constructing an observing program from the validated plan.



