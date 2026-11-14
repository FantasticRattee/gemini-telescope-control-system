> **Phase 2 design (D4).** These diagrams cover the three assigned use cases. Method, field and status names follow the implementation in `gemini-1/implementations/ocs` where it exists. The design separates `SciencePlanFacade`, `ObservingProgramFacade`, repositories and helper classes; the implementation consolidates the two facades and the repositories into the single class `OCS` (which implements `GeminiAPI`), and does not implement `Validator`, `StarCatalog` or `TelescopeSchedule` (see the README).

## Create Science Plan Activity Diagram
```mermaid
flowchart TD
    A((Start)) --> B[Click Create Science Plan]
    B --> C[System displays form]
    C --> D[Enter plan details: Name, Funding, Objective, Dates]
    D --> E[Select telescope and star target]
    E --> F[Enter data-processing settings]
    F --> G[System validates inputs and dependencies]
    G --> H{All inputs valid?}


    H -- No --> H1{What is invalid?}
    H1 -- Missing fields --> M1[Error Message: Please complete all required fields before saving the science plan]
    H1 -- Invalid values --> M2[Error Message: Invalid input value. Please enter valid decimal numbers within the allowed range.]
    H1 -- Start date > End date --> M3[Error Message: Start date cannot be after the end date]
    H1 -- target is not available --> M4[Error Message: Selected target not found in the star catalogue. Please choose a valid target]
    H1 -- Name exists --> M5[Error Message: A science plan with this name already exists. Please use a different plan name.]
    H1 -- Dependency missing --> M6[Error Message: Please complete all required fields for the selected data processing mode.]
    H1 -- OCS incompatibility --> M7[Error Message: Input data not compatible with legacy OCS system]
    H1 -- Funding invalid --> M8[Error Message: Funding must be a positive number]
    H1 -- Date conflict --> M9[Error Message: Selected date range conflicts with another scheduled plan for this telescope.]
    H1 -- Out-of-range numeric --> M10[Error Message: Please enter valid decimal numbers within the allowed range]
    H1 -- system loses connection --> M11[Error Message: Unable to save the science plan due to network or system error. Please try again later.]

    
    M1 --> U[Show Error Message]
    M2 --> U
    M3 --> U
    M4 --> U
    M5 --> U
    M6 --> U
    M7 --> U
    M8 --> U
    M9 --> U
    M10 --> U
    M11 --> U



    U --> D

    H -- Yes --> I{Save or Save as Draft?}
    I -- Draft --> J[Save plan as draft: status = CREATED, marked incomplete]
    I -- Final --> K[Save plan with status = CREATED]

    J --> L[Show confirmation to Astronomer]
    K --> L

    L --> M[Choose next action: Edit, Delete, Test]
    M --> N{Which action?}
    N -- Edit --> D
    N -- Delete --> O[Confirm delete?]
    O --> P{Confirmed?}
    P -- Yes --> Q[Delete plan]
    Q --> R[Show delete success]
    P -- No --> M
    N -- Test --> S[Proceed to Test Science Plan]
    S --> T((End))
    R --> T
```
## Create Science Plan Sequence Diagram
```mermaid
sequenceDiagram
    actor Astronomer
    participant UI
    participant SPF as SciencePlanFacade
    participant StarCatalog
    participant Validator
    participant TelescopeSchedule
    participant SPR as SciencePlanRepository

    %% === 1. Start Creating Plan ===
    Astronomer->>UI: Click "Create Science Plan"
    UI-->>Astronomer: Display form

    %% === 2. Enter Details ===
    Astronomer->>UI: Enter plan details (Name, Funding, Objective, Dates)
    Astronomer->>UI: Select telescope
    Astronomer->>UI: Select target star

    %% === 3. Enter Data Processing ===
    Astronomer->>UI: Enter data processing settings (FileType, FileQuality, ColorType, Contrast, etc.)
    Astronomer->>UI: Click Save or Save as Draft

    %% === 4. Validation ===
    UI->>SPF: createSciencePlan(sp, astronomer)
    SPF->>StarCatalog: containsTarget(target)
    StarCatalog-->>SPF: true / false
    SPF->>Validator: validateInputs(sp)
    Validator-->>SPF: true / false
    SPF->>SPR: existsByName(name)
    SPR-->>SPF: true / false
    SPF->>TelescopeSchedule: hasConflict(telescope, startDate, endDate)
    TelescopeSchedule-->>SPF: true / false

    alt Invalid Input
        SPF-->>UI: Error message
        UI-->>Astronomer: Show specific error message
        Note right of UI: e.g. Missing fields, Date error, Name exists,<br/>Funding invalid, Target not found,<br/>ColorType dependency missing,<br/>OCS incompatibility, network failure
    else Valid Input
        SPF->>SPF: assign planNo and creator, setStatus(CREATED)

        alt Save as Draft
            SPF->>SPR: save(sp) (draft, marked incomplete)
            SPR-->>SPF: Saved
            SPF-->>UI: Return confirmation
            UI-->>Astronomer: Draft saved. You can resume later.
        else Save Final Plan
            SPF->>SPR: save(sp)
            alt Network/System error
                SPR-->>SPF: Error: Unable to save
                SPF-->>UI: Error message
                UI-->>Astronomer: Show error: Try again later
            else
                SPR-->>SPF: Saved
                SPF-->>UI: "Science Plan created successfully with Plan No: N"
                UI-->>Astronomer: Plan saved successfully
            end
        end
    end

    %% === 5. Post-save options ===
    loop While plan exists
        Astronomer->>UI: Choose Edit / Delete / Test

        alt Edit
            UI-->>Astronomer: Show form with current values

        else Delete
            UI-->>Astronomer: Confirm delete?
            Astronomer-->>UI: Confirm = Yes/No
            alt Confirm = Yes
                UI->>SPF: deleteSciencePlanByNo(planNo)
                SPF->>SPR: delete(sp)
                SPR-->>SPF: Deleted
                SPF-->>UI: true
                UI-->>Astronomer: Show success message
            else
                UI-->>Astronomer: Cancel delete
            end

        else Test
            UI-->>Astronomer: Redirect to Test Science Plan
        end
    end
```
## Submit Science Plan Activity Diagram
```mermaid
flowchart TD

    %% Top
    A([Start]) --> B[Select 'Science Plans' menu]

    B --> C{Any Science Plan?}

    C -- "No" --> D[Show blank page button Go to Create Science Plan]
    D --> Z([End])

    C -- "Yes" --> E{Status filter?}

    %% Status filter branches
    E -- "Created" --> F1[Show CREATED ONLY]
    E -- "Tested" --> F2[Show TESTED ONLY]
    E -- "Submitted" --> F3[Show SUBMITTED ONLY]
    E -- "Validated" --> F4[Show VALIDATED ONLY]
    E -- "Executed" --> F5[Show RUNNING / COMPLETE ONLY]
    E -- "No filter" --> F6[Show All Science Plans]

    F1 --> G[Display Science Plan List]
    F2 --> G
    F3 --> G
    F4 --> G
    F5 --> G
    F6 --> G

    %% Select plan
    G --> H[Select Science Plan]

    H --> I{Draft or Submit?}

    I -- "Draft" --> J[Remain CREATED status and stored in Science Plan List]
    J --> Z

    I -- "Submit" --> M{Confirm?}

    %% Confirm?
    M -- "No" --> N[Return to Science Plans page]
    N --> Z

    %% Already submitted/executed? (checked before anything is changed)
    M -- "Yes" --> P{Already SUBMITTED, VALIDATED,<br/>RUNNING or COMPLETE?}

    P -- "Yes" --> Q[Show: This science plan has already been submitted or executed.]
    Q --> Z

    %% Only a TESTED plan can be submitted
    P -- "No" --> K{Status = TESTED?}

    K -- "No" --> L[Show: This science plan is incomplete. Please complete and test it before submission.]
    L --> Z

    K -- "Yes" --> O[Submit selected Science Plan: set status = SUBMITTED]
    O --> R[Show: Science plan N submitted successfully.]
    R --> Z
```
## Submit Science Plan Sequence Diagram
```mermaid
sequenceDiagram
    actor Astronomer
    participant UI
    participant SPF as SciencePlanFacade
    participant SP as SciencePlan
    participant SPR as SciencePlanRepository

    %% Start
    Astronomer->>UI: Open Science Plans page
    UI->>SPF: getAllSciencePlans()
    SPF->>SPR: findByAstronomer(astronomer)
    SPR-->>SPF: Return list
    SPF-->>UI: Display plans list

    alt No science plan exists
        UI-->>Astronomer: Show blank page + "Go to Create Science Plan"
    else Science plans exist
        UI-->>Astronomer: Show status filter options

        Astronomer->>UI: Select status filter
        UI->>SPF: filterSciencePlansByStatus(status)
        SPF->>SPR: findByStatus(status)
        SPR-->>SPF: Return filtered list
        SPF-->>UI: Show filtered science plans

        Astronomer->>UI: Select science plan

        alt Draft
            UI-->>Astronomer: Plan stays CREATED in the Science Plans list

        else Submit
            UI-->>Astronomer: Ask for confirmation

            alt Astronomer cancels
                UI-->>Astronomer: Return to Science Plans page

            else Astronomer confirms
                UI->>SPF: submitSciencePlan(sp, astronomer)
                SPF->>SP: getStatus()
                SP-->>SPF: status

                alt status is SUBMITTED, VALIDATED, RUNNING or COMPLETE
                    SPF-->>UI: "This science plan has already been submitted or executed."
                    UI-->>Astronomer: Display message

                else status is not TESTED
                    SPF-->>UI: "This science plan is incomplete. Please complete and test it before submission."
                    UI-->>Astronomer: Display warning

                else status is TESTED
                    SPF->>SP: setStatus(SUBMITTED)
                    SPF->>SPR: save(sp)
                    SPF-->>UI: "Science plan N submitted successfully."
                    UI-->>Astronomer: Display confirmation
                end
            end
        end
    end
```
## Create Observing Program Activity Diagram
```mermaid
flowchart TD

A0([Start]) --> A1[Science Observer: Open 'Create Observing Program' page]
A1 --> A2[System: List Science Plans with status = VALIDATED]
A2 --> A3{Any validated<br/>plan available?}
A3 -- No --> E1[Display: 'No validated science plans found.']
E1 --> A99([End])

A3 -- Yes --> A4[Observer: Select a validated Science Plan by Plan No]
A4 --> A5[System: Load base data from Science Plan:<br/> Plan No, Plan Name, Creator, Funding, Objective, Start and End dates, Telescope assigned, Target]

A5 --> A5b{Use template?}
A5b -- Yes --> A5c[System: Load predefined template values<br/>and populate fields]
A5b -- No --> A6

A5c --> A6

A6[Observer: Provide required details<br/> Optics Primary, F-stop, Optics Secondary RMS, Science Fold Mirror Degree, Science Fold Mirror Type, Module Content, Calibration Unit, Light Type, Telescope Position Pairs<br/>Gemini location = telescope assigned in the plan]

A6 --> A11[Observer: Click <br/> 'Create Observing Program']
A11 --> A7{All required<br/>fields filled?}
A7 -- No --> E2[Display error: Missing required fields<br/>Please enter the necessary information before proceeding]
E2 --> A6

A7 -- Yes --> A8{All values valid<br/>within defined lists?}
A8 -- No --> E3[Display error: Invalid value<br/>e.g., Calibration Unit must be<br/>one of ARGON, XENON, THAR, CUAR]
E3 --> A6

A8 -- Yes --> A9[System: Validate all entries<br/>Check completeness, data format, allowed values]
A9 --> A10{All validation<br/>checks passed?}
A10 -- No --> E4[System: Record the Error and </br>Display: Show error Summary]
E4 --> A6

A10 -- Yes --> A12[System Update: Create new Observing Program record<br/>Status = CREATED<br/>Link with original Science Plan and Science Observer id]

A12 --> A13[System: Save to database]
A13 --> A14{Database save<br/>successful?}
A14 -- No --> E5[System: Record Error Display: Failed to save &<br/>Please try again or contact admin.]
E5 --> A11

A14 -- Yes --> A15[System: Display confirmation<br/>'Observing Program created and linked<br/>to Science Plan #XXXX']
A15 --> A16[System: Notify Astronomer<br/>'Your Science Plan has been<br/>transformed to Observing Program']
A16 --> A99
```
## Create Observing Program Sequence Diagram
```mermaid
sequenceDiagram
    autonumber
    actor SO as ScienceObserver
    participant UI as UI
    participant SPF as SciencePlanFacade
    participant OPF as ObservingProgramFacade
    participant V as Validator
    participant OP as ObservingProgram
    participant OPR as ObservingProgramRepository
    actor A as Astronomer

    SO->>UI: openCreateObservingProgramPage()
    UI->>SPF: filterSciencePlansByStatus(VALIDATED)

    alt No validated plans found
        SPF-->>UI: return(emptyList)
        UI-->>SO: display("No validated science plans found.")
    else Validated plans available
        SPF-->>UI: return(validatedPlansList)
        UI-->>SO: displayValidatedPlans()

        SO->>UI: selectSciencePlan(planNo)
        UI->>SPF: getSciencePlanByNo(planNo)
        SPF-->>UI: return(sp)
        UI-->>SO: displayPlanDetails()

        SO->>UI: chooseTemplate(templateID)
        UI-->>SO: populateFields(templateData)

        SO->>UI: enterProgramDetails(...)
        SO->>UI: clickCreateObservingProgram()
        UI->>OPF: createObservingProgram(sp, opticsPrimary, fStop, opticsSecondaryRMS, scienceFoldMirrorDegree, scienceFoldMirrorType, moduleContent, calibrationUnit, lightType, telePositionPairs, so)

        OPF->>V: validateRequiredFields(op)
        alt Missing Fields
            V-->>OPF: false
            OPF-->>UI: return(null)
            UI-->>SO: display("Missing required fields.")
        else All fields present
            V-->>OPF: true

            OPF->>V: validateAllowedValues(op)
            alt Invalid Values
                V-->>OPF: false
                OPF-->>UI: return(null)
                UI-->>SO: display("Invalid value.")
            else Valid
                V-->>OPF: true

                OPF->>V: validateObservingProgram(op)
                alt Validation Failed
                    V-->>OPF: false (errorSummary)
                    OPF-->>UI: return(null)
                    UI-->>SO: display(errorSummary)
                else Passed
                    V-->>OPF: true

                    OPF->>OP: setStatus(CREATED)
                    OPF->>OPR: save(op)

                    alt Save Failed
                        OPR-->>OPF: return("FAILED")
                        OPF-->>UI: return(null)
                        UI-->>SO: display("Save failed.")
                    else Save Successful
                        OPR-->>OPF: return("SUCCESS")
                        OPF-->>UI: return(op)

                        UI-->>SO: display("Program linked to Plan #" + planNo)
                        UI->>A: notify("Plan transformed to Observing Program.")
                    end
                end
            end
        end
    end
```
# Class Diagram
```mermaid
classDiagram

%% ===========================
%% MAIN ACTORS
%% ===========================
class Astronomer {
    -id : int
    -firstName : String
    -lastName : String
    -institution : String
    +getName() String
}

class ScienceObserver {
    -id : int
    +getId() int
}

%% ===========================
%% DOMAIN ENTITIES
%% ===========================
class SciencePlan {
    -planNo : int
    -name : String
    -creator : String
    -funding : double
    -objective : String
    -startDate : Date
    -endDate : Date
    -telescope : String
    -target : String
    -fileType : FileType
    -fileQuality : FileQuality
    -colorType : ColorType
    -contrast : Double
    -brightness : Double
    -saturation : Double
    -highlights : Double
    -exposure : Double
    -shadows : Double
    -whites : Double
    -blacks : Double
    -luminance : Double
    -hue : Double
    -status : STATUS

    +getStatus() STATUS
    +setStatus(status : STATUS)
}

class ObservingProgram {
    -sciencePlan : SciencePlan
    -opticsPrimary : String
    -fStop : double
    -opticsSecondaryRMS : double
    -scienceFoldMirrorDegree : double
    -scienceFoldMirrorType : FoldMirrorType
    -moduleContent : int
    -calibrationUnit : CalibrationUnit
    -lightType : LightType
    -telePositionPairs : TelePositionPair[]
    -observerId : int
    -status : STATUS

    +setStatus(status : STATUS)
}

class TelePositionPair {
    -direction : double
    -degree : double
}

%% ===========================
%% REPOSITORIES
%% ===========================
class SciencePlanRepository {
    +save(sp:SciencePlan)
    +delete(sp:SciencePlan)
    +findById(id:int) : SciencePlan
    +findByAstronomer(a:Astronomer) : List~SciencePlan~
    +findByStatus(status:STATUS) : List~SciencePlan~
    +existsByName(name:String) : boolean
    +hasDateConflict(telescope:String, start:Date, end:Date) : boolean
}

class ObservingProgramRepository {
    +save(op:ObservingProgram)
    +findBySciencePlan(sp:SciencePlan) : ObservingProgram
}

%% ===========================
%% SUPPORTING SUBSYSTEMS
%% ===========================
class Validator {
    +validateInputs(sp:SciencePlan) : boolean
    +validateRequiredFields(op:ObservingProgram) : boolean
    +validateAllowedValues(op:ObservingProgram) : boolean
    +validateObservingProgram(op:ObservingProgram) : boolean
}

class StarCatalog {
    +containsTarget(target:String) : boolean
}

class TelescopeSchedule {
    +hasConflict(telescope:String, start:Date, end:Date) : boolean
}

%% ===========================
%% FACADE PATTERN (with stereotype)
%% ===========================
class SciencePlanFacade {
    <<Facade>>
    +createSciencePlan(sp:SciencePlan, an:Astronomer) String
    +testSciencePlan(sp:SciencePlan) String
    +submitSciencePlan(sp:SciencePlan, an:Astronomer) String
    +validateSciencePlan(sp:SciencePlan, so:ScienceObserver) SciencePlan
    +getAllSciencePlans() List~SciencePlan~
    +filterSciencePlansByStatus(status:STATUS) List~SciencePlan~
    +getSciencePlanByNo(planNo:int) SciencePlan
    +deleteSciencePlanByNo(planNo:int) boolean
}

class ObservingProgramFacade {
    <<Facade>>
    +createObservingProgram(sp, opticsPrimary, fStop, opticsSecondaryRMS, scienceFoldMirrorDegree, scienceFoldMirrorType, moduleContent, calibrationUnit, lightType, telePositionPairs, so) ObservingProgram
    +getObservingProgramBySciencePlan(sp:SciencePlan) ObservingProgram
}

note for SciencePlanFacade "Implemented together with ObservingProgramFacade\nas class OCS, which implements GeminiAPI.\nThe repositories are in-memory lists inside OCS."

%% ===========================
%% ENUMS
%% ===========================
class STATUS {
    <<enumeration>>
    CREATED
    TESTED
    SUBMITTED
    VALIDATED
    RUNNING
    INVALIDATED
    COMPLETE
    CANCELLED
}

class FileType {
    <<enumeration>>
    RAW
    JPEG
    PNG
}

class FileQuality {
    <<enumeration>>
    FINE
    NORMAL
    BASIC
}

class ColorType {
    <<enumeration>>
    BW_MODE
    COLOR_MODE
}

class FoldMirrorType {
    <<enumeration>>
    REFLECTIVE_CONVERGING_BEAM
    CASSEGRAIN
}

class CalibrationUnit {
    <<enumeration>>
    ARGON
    XENON
    THAR
    CUAR
}

class LightType {
    <<enumeration>>
    MAUNA_KEA_SKY_EMISSION
    CERRO_PACHON_SKY_EMISSION
}

%% ===========================
%% RELATIONSHIPS
%% ===========================
Astronomer "1" --> "*" SciencePlan : creates
ScienceObserver "1" --> "*" ObservingProgram : creates

ObservingProgram "*" --> "1" SciencePlan : based on
ObservingProgram "1" --> "*" TelePositionPair

SciencePlan --> STATUS
SciencePlan --> FileType
SciencePlan --> FileQuality
SciencePlan --> ColorType
ObservingProgram --> FoldMirrorType
ObservingProgram --> CalibrationUnit
ObservingProgram --> LightType

SciencePlanRepository --> SciencePlan
ObservingProgramRepository --> ObservingProgram

Validator --> SciencePlan
Validator --> ObservingProgram

StarCatalog --> SciencePlan
TelescopeSchedule --> SciencePlan

SciencePlanFacade --> SciencePlanRepository
SciencePlanFacade --> Validator
SciencePlanFacade --> StarCatalog
SciencePlanFacade --> TelescopeSchedule

ObservingProgramFacade --> ObservingProgramRepository
ObservingProgramFacade --> Validator
ObservingProgramFacade --> SciencePlanRepository
```
