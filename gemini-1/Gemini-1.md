# Gemini Interfaces (OCS sample)


An educational Maven project that implements a lightweight Operational Control System (OCS) against the `GeminiAPI` interface. It models science plans and observing programs for the Gemini telescopes using simple in-memory storage and comes with unit tests and a runnable demo.


## Prerequisites
- Java 19+
- Maven 3.9+


## Project layout
- `ocs/src/main/java/edu/gemini` — concrete OCS implementation and domain classes.
- `ocs/src/main/java/edu/gemini/model` — abstract interfaces/base classes supplied by the API.
- `ocs/src/test/java` — JUnit 5 tests for creating and submitting plans and observing programs.
- `ocs/src/main/java/edu/gemini/Main.java` — runnable example wiring everything together.


## Build, run, and test
From the repository root:
```bash
cd ocs
mvn test                     # run the JUnit 5 suite
mvn -DskipTests package      # build the jar
java -cp target/ocs-1.0-SNAPSHOT.jar edu.gemini.Main  # run the demo
```


### Step-by-step execution
1) `cd ocs` 
2) `mvn clean package` (use `mvn test` first if you want to only run tests) 
3) `java -cp target/ocs-1.0-SNAPSHOT.jar edu.gemini.Main` 
You should see logs for creating, testing, and submitting a science plan plus an observing program summary.


### How to run in an IDE
- Clone the repo (or download the ZIP) and open the `ocs` module in Eclipse or IntelliJ.
- Ensure a Java SDK 17+ is configured for the project (code targets Java 19 in `pom.xml`).
- Run `src/main/java/edu/gemini/Main.java` to see the sample flow.


### Features
- Create science plans with validation and auto-numbering.
- Test and submit plans only after they pass testing.
- Assign astronomers and science observers.
- Create observing programs from submitted plans.
- Configure telescope optics, fold mirrors, light sources, and module content.
- Simulate telescope pointing via azimuth/elevation pairs.


### Running tests
- Tests use JUnit 5 under `src/test/java`.
- From the command line: `mvn test`.
- From an IDE: right-click a test class and run as JUnit.


## Project structure
```
GeminiInterfaces-master/
├── README.md
├── LICENSE
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
- `edu.gemini.OCS` implements `GeminiAPI` with in-memory plan storage, auto-incremented plan numbers, validation when creating plans (required name, funding, date order), testing (`CREATED -> TESTED`), submission gating on `TESTED`, status updates, and deletion helpers.
- `createObservingProgram` assembles an `ObservingProgram` from a science plan, optical settings, and telescope position pairs, marking it `CREATED`.
- Domain models (`SciencePlan`, `ObservingProgram`, `Astronomer`, `ScienceObserver`, `TelePositionPair`, etc.) extend the abstract types supplied in `model`.
- `Main` demonstrates creating a plan, testing and submitting it, and constructing an observing program.



