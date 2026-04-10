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
    I -- Draft --> J[Save plan with status = INCOMPLETE]
    I -- Final --> K[Save plan with status = SAVED]

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
    participant Astronomer
    participant UI
    participant Validator
    participant StarCatalog
    participant TelescopeSchedule
    participant SciencePlanDB

    %% === 1. Start Creating Plan ===
    Astronomer->>UI: Click "Create Science Plan"
    UI-->>Astronomer: Display form

    %% === 2. Enter Details ===
    Astronomer->>UI: Enter plan details (Name, Funding, Objective, Dates)
    Astronomer->>UI: Select telescope
    Astronomer->>UI: Select target star
    UI->>StarCatalog: checkTargetInCatalogue()
    StarCatalog-->>UI: targetValid / invalid

    %% === 3. Enter Data Processing ===
    Astronomer->>UI: Enter data processing settings (FileType, Quality, ColorType, Contrast, etc.)

    %% === 4. Validation ===
    UI->>Validator: validateAllInputs(data)
    Validator->>TelescopeSchedule: checkDateConflict(telescope, dates)
    TelescopeSchedule-->>Validator: conflictFound / OK
    Validator-->>UI: return validationResults

    alt Invalid Input
        UI-->>Astronomer: Show specific error message
        Note right of UI: e.g. Missing fields, Date error, Name exists,<br/>Funding invalid, Target not found,<br/>ColorType dependency missing,<br/>OCS incompatibility, network failure
    else Valid Input
        Astronomer->>UI: Choose Save or Save as Draft

        alt Save as Draft
            UI->>SciencePlanDB: savePlan(status=INCOMPLETE)
            SciencePlanDB-->>UI: Return confirmation
            UI-->>Astronomer: Draft saved. You can resume later.
        else Save Final Plan
            UI->>SciencePlanDB: savePlan(status=SAVED)
            alt Network/System error
                SciencePlanDB-->>UI: Error: Unable to save
                UI-->>Astronomer: Show error: Try again later
            else
                SciencePlanDB-->>UI: Return confirmation
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
                UI->>SciencePlanDB: deletePlan()
                SciencePlanDB-->>UI: Deleted
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
    A([Start]) --> B[Selected Science Plan]

    B --> C{Any Science Plan?}

    C -- "No" --> D[Show blank page button Go to Create Science Plan]
    D --> Z([End])

    C -- "Yes" --> E{Status filter?}

    %% Status filter branches
    E -- "Created" --> F1[Show Created ONLY]
    E -- "Tested" --> F2[Show Tested ONLY]
    E -- "Submitted" --> F3[Show Submitted ONLY]
    E -- "Validated" --> F4[Show Validated ONLY]
    E -- "Executed" --> F5[Show Executed ONLY]
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

    I -- "Draft" --> J[Remain Created status and stored in Science Plan List]
    J --> Z

    I -- "Submit" --> K{Fully filled or Tested?}

    %% Fully tested?
    K -- "No" --> L[Show This science plan is incomplete. Please complete and test itbefore submission.]
    L --> Z

    K -- "Yes" --> M{Confirm?}

    %% Confirm?
    M -- "No" --> N[Return to submitted science plan page]
    N --> Z

    M -- "Yes" --> O[Submit selected Science Plan]

    %% Already submitted/executed?
    O --> P{Already submitted?}

    P -- "Yes" --> Q[Show: This science plan has already been submitted or executed.]
    Q --> Z

    P -- "No" --> Z
```
## Submit Science Plan Sequence Diagram
```mermaid
sequenceDiagram
    actor User
    participant UI as User Interface
    participant System as Science Plan System
    participant DB as Database

    %% Start
    User->>UI: Open Science Plan Page
    UI->>System: Request science plans
    System->>DB: Fetch science plans
    DB-->>System: Return list
    System-->>UI: Display plans list

    alt No science plan exists
        UI-->>User: Show blank page + "Go to Create Science Plan"
    else Science plans exist
        UI-->>User: Show status filter options

        User->>UI: Select status filter
        UI->>System: Apply filter
        System-->>UI: Show filtered science plans

        User->>UI: Select science plan

        alt Draft
            UI->>System: Save as Draft
            System->>DB: Store plan as Created
            System-->>UI: Done

        else Submit
            UI->>System: Validate completeness

            alt Not fully filled or tested
                System-->>UI: Show "Plan incomplete" message
                UI-->>User: Display warning

            else Fully filled/tested
                UI-->>User: Ask for confirmation

                alt User cancels
                    UI-->>User: Return to submitted science plan page

                else User confirms
                    UI->>System: Submit science plan
                    System->>DB: Check submission status

                    alt Already submitted?
                        System-->>UI: Show "This science plan has already been submitted or executed"
                        UI-->>User: Display message

                    else Not submitted
                        System->>DB: Mark plan as submitted
                        System-->>UI: Submission successful
                    end
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

A3 -- Yes --> A4[Observer: Select a validated Science Plan by Plan ID]
A4 --> A5[System: Load base data from Science Plan:<br/> Plan ID, Plan Name, Creator, Funding, Objective, Start and End dates, Telescope assigned, Target]

A5 --> A5b{Use template?}
A5b -- Yes --> A5c[System: Load predefined template values<br/>and populate fields]
A5b -- No --> A6

A5c --> A6

A6[Observer: Provide required details<br/> Gemini Location, Optics Primary, F-stop, set Optics Secondary RMS, Science Fold Mirror Degree, Science Fold Mirror Type, Module Content, Calibration Unit, Light Type, Telescope Position Pair]

A6 --> A7{All required<br/>fields filled?}
A7 -- No --> E2[Display error: Missing required fields<br/>Please enter the necessary information before proceeding]
E2 --> A6

A7 -- Yes --> A8{All values valid<br/>within defined lists?}
A8 -- No --> E3[Display error: Invalid value<br/>e.g., Calibration Unit must be<br/>one of FLAT, DARK, ARC, BIAS]
E3 --> A6

A8 -- Yes --> A9[System: Validate all entries<br/>Check completeness, data format, allowed values]
A9 --> A10{All validation<br/>checks passed?}
A10 -- No --> E4[System: Record the Error and </br>Display: Show error Summary]
E4 --> A6

A10 -- Yes --> A11[Observer: Click <br/> 'Create Observing Program']
A11 --> A12[System Update: Create new Observing Program record<br/>Status = CREATED<br/>Link with original Science Plan via Plan ID]

A12 --> A13[System: Save to database]
A13 --> A14{Database save<br/>successful?}
A14 -- No --> E5[System: Record Error Display: Failed to save &<br/>Please try again or contact admin.]
E5 --> A11

A14 -- Yes --> A15[System: Display confirmation<br/>'Observing Program created and linked<br/>to Science Plan #XXXX']
A15 --> A16[System: Notify Astronomer<br/>'Your Science Plan has been<br/>transformed to Observing Program']
A16 --> A17[System: Notify Technical Team<br/>'New Observing Program ready for review']
A17 --> A99
```
## Create Observing Program Sequence Diagram
```mermaid
sequenceDiagram
    autonumber
    participant SO as ScienceObserver
    participant UI as UI
    participant SP as SciencePlan
    participant OP as ObservingProgram
    participant V as Validator
    participant DB as Database
    participant A as Astronomer
    participant T as TechnicalTeam

    SO->>UI: openCreateObservingProgramPage()
    UI->>SP: listPlans(status="VALIDATED")

    alt No validated plans found
        SP-->>UI: return(emptyList)
        UI-->>SO: display("No validated science plans found.")
    else Validated plans available
        SP-->>UI: return(validatedPlansList)
        UI-->>SO: displayValidatedPlans()

        SO->>UI: selectSciencePlan(planID)
        UI->>SP: loadPlan(planID)
        SP-->>UI: return(planData)
        UI-->>SO: displayPlanDetails()

        SO->>UI: chooseTemplate(templateID)
        UI-->>SO: populateFields(templateData)

        SO->>UI: enterProgramDetails(...)

        UI->>V: validateRequiredFields(data)
        alt Missing Fields
            V-->>UI: return("FAILED: Missing fields")
            UI-->>SO: display("Missing required fields.")
        else All fields present
            V-->>UI: return("OK")

            UI->>V: validateValues(...)
            alt Invalid Values
                V-->>UI: return("FAILED: Invalid value")
                UI-->>SO: display("Invalid value.")
            else Valid
                V-->>UI: return("OK")

                UI->>V: finalValidation(data)
                alt Validation Failed
                    V-->>UI: return(errorSummary)
                    UI-->>SO: display(errorSummary)
                else Passed
                    V-->>UI: return("PASSED")

                    SO->>UI: clickCreateObservingProgram()
                    UI->>OP: createObservingProgram(planID, programData)
                    OP->>OP: setStatus("CREATED")
                    OP->>DB: saveObservingProgram()

                    alt Save Failed
                        DB-->>OP: return("FAILED")
                        OP-->>UI: return("FAILED")
                        UI-->>SO: display("Save failed.")
                    else Save Successful
                        DB-->>OP: return("SUCCESS")
                        OP-->>UI: return(programID)

                        UI-->>SO: display("Program linked to Plan #" + planID)
                        UI->>A: notify("Plan transformed to Observing Program.")
                        UI->>T: notify("Observing Program ready for review.")
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
    +astronomerId : int
    +name : String
    +Institution: String
    +createSciencePlan()
    +submitSciencePlan(sp:SciencePlan)
}

class ScienceObserver {
    +observerId : int
    +name : String
    +createObservingProgram(sp:SciencePlan)
    +validateSciencePlan(sp:SciencePlan)
}

%% ===========================
%% DOMAIN ENTITIES
%% ===========================
class SciencePlan {
    +planId : int
    +name : String
    +funding : double
    +objective : String
    +startDate : Date
    +endDate : Date
    +telescope : String
    +target : String
    +status : SciencePlanStatus
    +processing : DataProcessingSettings

    +updateDetails(...)
    +markAsDraft()
    +markAsCreated()
    +markAsTested()
    +markAsSubmitted()
}

class DataProcessingSettings {
    +fileType : FileType
    +fileQuality : FileQuality
    +colorType : ColorType
    +contrast : double
    +brightness : double
    +saturation : double
    +highlights : double
    +exposure : double
    +shadows : double
    +whites : double
    +blacks : double
    +luminance : double
    +hue : double
}

class ObservingProgram {
    +programId : int
    +geminiLocation : String
    +opticsPrimary : String
    +fStop : double
    +opticsSecondaryRMS : double
    +foldMirrorDegree : double
    +foldMirrorType : String
    +moduleContent : int
    +calibrationUnit : String
    +lightType : String
    +status : String

    +addTelePositionPair(pair:TelePositionPair)
    +LoadValidatedSciencePlan()
}

class TelePositionPair {
    +azimuth : double
    +altitude : double
}

%% ===========================
%% REPOSITORIES
%% ===========================
class SciencePlanRepository {
    +save(sp:SciencePlan)
    +delete(sp:SciencePlan)
    +findById(id:int) : SciencePlan
    +findByAstronomer(a:Astronomer) : List~SciencePlan~
    +findByStatus(status:SciencePlanStatus) : List~SciencePlan~
    +existsByName(name:String) : boolean
    +hasDateConflict(telescope:String, start:Date, end:Date) : boolean
}

class ObservingProgramRepository {
    +save(op:ObservingProgram)
    +findById(id:int) : ObservingProgram
}

%% ===========================
%% SUPPORTING SUBSYSTEMS
%% ===========================
class Validator {
    +validateSciencePlan(sp:SciencePlan) : boolean
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
    +createSciencePlan(data)
    +validateSciencePlan(data)
    +saveSciencePlan(sp)
    +submitSciencePlan(planId)
}

class ObservingProgramFacade {
    <<Facade>>
    +loadValidatedPlans()
    +createObservingProgram(planId, details)
    +validateObservingDetails(details)
    +saveProgram(op)
}

%% ===========================
%% ENUMS
%% ===========================
class SciencePlanStatus {
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
    PNG
    JPEG
    RAW
}

class FileQuality {
    <<enumeration>>
    LOW
    FINE
}

class ColorType {
    <<enumeration>>
    COLOR_MODE
    BW_MODE
}

%% ===========================
%% RELATIONSHIPS
%% ===========================
Astronomer "1" --> "*" SciencePlan
ScienceObserver "1" --> "*" ObservingProgram

SciencePlan "1" --> "1" DataProcessingSettings
ObservingProgram "1" --> "*" TelePositionPair

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

DataProcessingSettings --> Validator
```
