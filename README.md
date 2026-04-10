# Gemini Telescope Control System

An Operational Control System (OCS) for the **Gemini Observatory** — a real-world twin-telescope facility operated by an international partnership of seven countries. This project implements the software that astronomers use to plan, validate, and execute telescope observations.

Built as part of **ITCS382 — Software Design and Development** at the Faculty of Information and Communication Technology, Mahidol University.

## What This Project Does

Astronomers cannot simply point a telescope at the sky and start observing. Every observation requires a structured plan that specifies what to look at, when, with which instruments, and how to process the captured data. This system manages that entire workflow:

1. **Astronomers** create science plans — defining targets, observation windows, funding, and data processing settings
2. **The system** validates the plan against the star catalogue, telescope schedule, and instrument constraints
3. **Science Observers** review and transform approved plans into executable observing programs
4. **Operations Staff** execute the programs, controlling telescope optics, mirror positioning, and calibration

The system enforces a strict status lifecycle to ensure only validated plans reach the telescope:

```
CREATED --> TESTED --> SUBMITTED --> VALIDATED --> RUNNING --> COMPLETE
```

## Architecture

### Design Patterns

**Facade Pattern** — `SciencePlanFacade` and `ObservingProgramFacade` provide simplified entry points that hide the complexity of repositories, validators, star catalogues, and telescope scheduling from the actors.

**Template Method** — Abstract base classes in the `model` package define the extension points that the Gemini API requires, while concrete implementations handle domain-specific logic.

**Strategy Pattern** — Configurable enums (FileType, CalibrationUnit, LightType, FoldMirrorType) allow flexible instrument and data processing configuration without modifying core logic.

### Class Diagram

```mermaid
classDiagram

    class Astronomer {
      +createSciencePlan()
      +submitSciencePlan()
    }

    class ScienceObserver {
      +reviewSciencePlan()
      +validateSciencePlan()
      +transformToObservingProgram()
    }

    class SciencePlan {
      +planID: String
      +objectives: String
      +starSystem: StarSystem
      +observationDates: DateRange
      +telescopeSite: TelescopeSite
      +status: Status
      +validate()
      +submit()
    }

    class ObservingProgram {
      +programID: String
      +linkedPlan: SciencePlan
      +optics: String
      +foldMirror: MirrorType
      +calibrationUnit: CalibrationUnit
      +lightType: LightType
      +waypoints: Waypoint[]
      +status: Status
      +execute()
    }

    class StarSystem {
      +name: String
      +quadrant: String
      +observingMonth: String
    }

    class OCS {
      +createSciencePlan()
      +testSciencePlan()
      +submitSciencePlan()
      +createObservingProgram()
      +filterSciencePlansByStatus()
    }

    Astronomer "1" --> "*" SciencePlan : creates
    ScienceObserver "1" --> "*" ObservingProgram : transforms
    SciencePlan "1" --> "1" StarSystem
    ObservingProgram "1" --> "1" SciencePlan
    OCS --> SciencePlan : manages
    OCS --> ObservingProgram : manages
```

## Design Documents

### Use Case Diagram

![Use Case Diagram](docs/diagrams/usecase.png)

The system supports four actor types: Astronomer, Science Observer, Operations Staff, and Visitor — each with distinct responsibilities in the observation pipeline.

### Activity Diagram — Create Science Plan

```mermaid
flowchart TD
    A((Start)) --> B[Click Create Science Plan]
    B --> C[System displays input form]
    C --> D[Enter required fields: objectives, star system, dates, site]
    D --> E[Enter data-processing settings]
    E --> F[System validates inputs]
    F --> G{All inputs valid?}
    G -- No --> H[Show specific error message]
    H --> D
    G -- Yes --> I{Save or Save as Draft?}
    I -- Draft --> J[Save with status = INCOMPLETE]
    I -- Final --> K[Save with status = SAVED]
    J --> L[Show confirmation]
    K --> L
    L --> M[Choose next action: Edit, Delete, or Test]
    M --> N{Action?}
    N -- Edit --> D
    N -- Delete --> O[Confirm deletion]
    O --> P{Confirmed?}
    P -- Yes --> Q[Delete plan] --> R((End))
    P -- No --> M
    N -- Test --> S[Proceed to Test Science Plan] --> R
```

### Sequence Diagram — Create Science Plan

```mermaid
sequenceDiagram
    participant Astronomer
    participant UI
    participant SciencePlan
    participant Validator

    Astronomer->>UI: Click "Create Science Plan"
    UI-->>Astronomer: Display input form
    Astronomer->>UI: Enter plan details
    UI->>Validator: validateInputs(data)
    Validator-->>UI: isValid = true/false

    alt Inputs Invalid
        UI-->>Astronomer: Show error message
    else Inputs Valid
        UI->>SciencePlan: savePlan(data)
        SciencePlan-->>UI: status = SAVED
        UI-->>Astronomer: Show confirmation
    end
```

### Activity Diagram — Submit Science Plan

```mermaid
flowchart TD
    A((Start)) --> B[Select a Science Plan from My Plans]
    B --> C[System displays plan details]
    C --> D{Plan status is TESTED?}
    D -- No --> E[Show error: Plan must be tested before submission]
    E --> F((End))
    D -- Yes --> G[Click Submit Science Plan]
    G --> H[System validates plan completeness]
    H --> I{Validation passed?}
    I -- No --> J[Show validation errors]
    J --> K[Return to plan details]
    K --> F
    I -- Yes --> L[Update status to SUBMITTED]
    L --> M[Notify Science Observer for review]
    M --> N[Show submission confirmation]
    N --> F
```

## Project Structure

```
gemini-telescope-control-system/
├── README.md
├── docs/
│   ├── requirements.md                  # Functional & non-functional requirements
│   ├── analysis.md                      # Complete system analysis (use cases, diagrams)
│   ├── design.md                        # Detailed design with activity & sequence diagrams
│   └── diagrams/
│       ├── usecase.png                  # Use case diagram
│       └── usecase.drawio              # Editable source (draw.io)
├── gemini-1/
│   └── (Phase 1 deliverables)
└── ocs/
    ├── pom.xml                          # Maven configuration (Java 19, JUnit 5)
    └── src/
        ├── main/java/edu/gemini/
        │   ├── Main.java                # Runnable demo
        │   ├── OCS.java                 # Core OCS implementation (GeminiAPI)
        │   ├── SciencePlan.java         # Science plan with validation & status
        │   ├── ObservingProgram.java     # Telescope program configuration
        │   ├── Astronomer.java          # Astronomer actor
        │   ├── ScienceObserver.java     # Science observer actor
        │   └── model/                   # Abstract base types (API contract)
        └── test/java/
            ├── TestCreateSciencePlan.java
            ├── TestSubmitSciencePlan.java
            └── TestCreateAnObservingProgram.java
```

## Getting Started

### Prerequisites

- Java 19 or later
- Apache Maven 3.9+

### Build and Run

```bash
cd ocs
mvn test                                                      # run JUnit 5 tests
mvn -DskipTests package                                       # build the JAR
java -cp target/ocs-1.0-SNAPSHOT.jar edu.gemini.Main          # run the demo
```

The demo creates a science plan for observing Andromeda, runs it through testing and submission, then assembles an observing program with telescope optics configuration.

### Run in an IDE

1. Open the `ocs` directory in IntelliJ IDEA or Eclipse
2. Ensure Java SDK 19+ is configured
3. Run `src/main/java/edu/gemini/Main.java`

## Key Features

**Science Plan Management** — Create plans with auto-generated IDs, validate against star catalogue availability, enforce date ranges, check funding, and detect naming conflicts.

**Status Workflow** — Plans progress through a strict lifecycle (CREATED, TESTED, SUBMITTED, VALIDATED) with gating rules that prevent premature submission or execution.

**Observing Program Assembly** — Transform validated plans into executable programs by configuring telescope optics (primary/secondary mirrors), fold mirror types, calibration units, light sources, and azimuth/elevation waypoints.

**Data Processing Configuration** — Define output formats (PNG, JPEG, RAW), quality levels, color modes, and fine-tune image parameters (contrast, brightness, saturation, exposure, highlights, shadows).

**Validation Framework** — Input field requirements, date conflict detection, target star availability checks, and data format verification with specific error messages.

## Tech Stack

- **Language**: Java 19
- **Build**: Apache Maven 3.9
- **Testing**: JUnit 5 (Jupiter)
- **Modeling**: Mermaid (class, activity, sequence diagrams), draw.io (use case diagram)

## Authors

ITCS382 Section 02, Team Gemini5 (No_Comment) — Mahidol University

## License

This project was created for educational purposes as part of university coursework.
