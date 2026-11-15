# Complete Analysis Documentation

**Gemini Observatory - Science Plan Management System**  
**Team: No_Comment**  
**Date: October 12, 2025**

---

## Table of Contents

1. [Class Diagram](#class-diagram)
2. [UC01: Create Science Plan](#uc01-create-science-plan)
   - Use Case Description
   - Activity Diagram
   - Sequence Diagram
3. [UC02: Submit Science Plan](#uc02-submit-science-plan)
   - Use Case Description
   - Activity Diagram
   - Sequence Diagram
4. [UC03: Review & Validate Submitted Plan](#uc03-review--validate-submitted-plan)
   - Use Case Description
   - Activity Diagram
   - Sequence Diagram
5. [UC04: Create Observing Program](#uc04-create-observing-program)
   - Use Case Description
   - Activity Diagram
   - Sequence Diagram
6. [UC05: Execute Observing Program](#uc05-execute-observing-program)
   - Use Case Description
   - Activity Diagram
   - Sequence Diagram

---

# Class Diagram

## Gemini Class Diagram

```mermaid
classDiagram

%% ==== Actors ====
class Astronomer {
  +createSciencePlan()
  +submitSciencePlan()
}

class ScienceObserver {
  +reviewSciencePlan()
  +validateSciencePlan()
  +createObservingProgram()
}

class OperationsStaff {
  +executeObservingProgram()
  +monitorExecution()
}

%% ==== Core Entities ====
class SciencePlan {
  +planID: String
  +creator: String
  +funding: Double
  +objectives: String
  +starSystem: StarSystem
  +observationDates: DateRange
  +telescopeSite: TelescopeSite
  +status: Status
  +dataProcessingRequirements: String
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
  +specialEquipment: Equipment[]
  +status: Status
  +save()
  +execute()
}

class StarSystem {
  +name: String
  +quadrant: String
  +observingMonth: String
}

class TelescopeSite {
  +siteName: String
  +hemisphere: String
}

class Waypoint {
  +azimuth: Float
  +altitude: Float
}

class Equipment {
  +name: String
  +owner: String
  +installationDate: Date
}

class CalibrationUnit {
  +type: String
}

class MirrorType {
  +type: String
}

class LightType {
  +type: String
}

class DateRange {
  +startDate: Date
  +endDate: Date
}

class Status {
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

class Role {
  <<enumeration>>
  ASTRONOMER
  SCIENCE_OBSERVER
  OPERATIONS_STAFF
}

class ExecutionLog {
  +startTime: DateTime
  +endTime: DateTime
  +status: String
  +logDetails: String
}

%% ==== Relationships with Cardinality ====

Astronomer "1" --> "*" SciencePlan : creates/submits
ScienceObserver "1" --> "*" SciencePlan : reviews/validates
ScienceObserver "1" --> "*" ObservingProgram : creates
OperationsStaff "1" --> "*" ObservingProgram : executes

SciencePlan "*" --> "1" StarSystem
SciencePlan "*" --> "1" TelescopeSite
SciencePlan "1" --> "1" DateRange : observationDates
SciencePlan --> Status

ObservingProgram "0..1" --> "1" SciencePlan : linkedTo
ObservingProgram "1" --> "*" Waypoint : uses
ObservingProgram "1" --> "*" Equipment : uses
ObservingProgram "*" --> "1" CalibrationUnit : configures
ObservingProgram "*" --> "1" MirrorType : configures
ObservingProgram "*" --> "1" LightType : configures
ObservingProgram "1" --> "0..1" ExecutionLog : has
```

---

# UC01: Create Science Plan

## Use Case Description

### Use Case Name

Create Science Plan

### Use Case ID

01

### Use Case Type

Detail and Essential

### Primary Actor

Astronomer

### Importance Level

High

### Stakeholders and Interest

- Astronomer
- Science Observer

### Brief Description

The Astronomer creates a new science plan by entering required information such as plan ID, objectives, target star system, observation dates, and telescope location. After finishing input, the plan will be in "CREATED" status for the Astronomer to edit, delete and test later before submitting.

### Trigger

External trigger

---

## Activity Diagram

```mermaid
flowchart TD
    A0([Start]) --> A1[Astronomer: Click 'Create Science Plan']
    A1 --> A2[System: Display input form]
    A2 --> A3[Astronomer: Enter required fields<br/>ID, creator, funding, objectives,<br/>star system, dates, telescope site]

    A3 --> A4[System: Validate inputs]
    A4 --> A5{Inputs valid?}
    A5 -- No --> A6[Show error message]
    A6 --> A3

    A5 -- Yes --> A7[System: Check constellation visibility]
    A7 --> A8{Observation month<br/>matches constellation?}
    A8 -- No --> A9[Error: Selected constellation<br/>cannot be observed in scheduled month]
    A9 --> A3

    A8 -- Yes --> A10[System: Check telescope location]
    A10 --> A11{Telescope site suitable<br/>for target hemisphere?}
    A11 -- No --> A12[Error: Telescope site not suitable<br/>NQ→HAWAII, SQ→CHILE]
    A12 --> A3

    A11 -- Yes --> A13[Astronomer: Enter data-processing requirements<br/>file type, file quality, color type,<br/>contrast, brightness, exposure, etc.]
    A13 --> A14[System: Validate data-processing values]
    A14 --> A15{Data-processing<br/>values valid?}
    A15 -- No --> A16[Error: Invalid input value or missing value<br/>for the selected data processing mode]
    A16 --> A13

    A15 -- Yes --> A17[System: Save plan with status = CREATED]
    A17 --> A18[System: Save to database]
    A18 --> A19{Database<br/>save successful?}
    A19 -- No --> A20[Error: Failed to save.<br/>Please try again.]
    A20 --> A3

    A19 -- Yes --> A21[System: Show confirmation<br/>Plan created with ID: XXXX]
    A21 --> A22[Astronomer: Choose next action]
    A22 --> A23{Which action?}

    A23 -- Edit --> A24[Load plan for editing]
    A24 --> A3

    A23 -- Delete --> A25[System: Confirm delete?]
    A25 --> A26{Confirmed?}
    A26 -- No --> A22
    A26 -- Yes --> A27[System: Delete plan from database]
    A27 --> A28[Show delete success message]
    A28 --> A99([End])

    A23 -- Test --> A29[Proceed to Submit/Test Science Plan use case]
    A29 --> A99

    A23 -- Done --> A99
```

---

## Sequence Diagram

```mermaid
sequenceDiagram
    autonumber
    participant A as Astronomer
    participant UI as UI
    participant SP as SciencePlan
    participant V as Validator
    participant SS as StarSystem
    participant TS as TelescopeSite
    participant DB as Database

    %% === 1. Initiate Plan Creation ===
    A->>UI: clickCreateSciencePlan()
    UI-->>A: displayInputForm()

    %% === 2. Enter Plan Details ===
    A->>UI: enterPlanDetails(creator, funding, objectives, targetStar, dates, telescope)
    UI->>V: validateInputs(data)

    alt Inputs Invalid
        V-->>UI: return("FAILED: Invalid input")
        UI-->>A: showErrorMessage("Please correct highlighted fields")
        A->>UI: enterPlanDetails()
    else Inputs Valid
        V-->>UI: return("OK")

        %% === 3. Validate Star System and Telescope ===
        UI->>SS: checkObservationWindow(observationDates)
        alt Observation month mismatch
            SS-->>UI: return("FAILED")
            UI-->>A: showError("Selected constellation cannot be observed in scheduled month")
        else OK
            SS-->>UI: return("OK")

            UI->>TS: checkTelescopeLocation(starSystem.quadrant)
            alt Telescope location mismatch
                TS-->>UI: return("FAILED")
                UI-->>A: showError("Telescope site not suitable for target hemisphere")
            else OK
                TS-->>UI: return("OK")

                %% === 4. Validate Data-Processing Requirements ===
                A->>UI: enterDataProcessing(fileType, fileQuality, colorType, imageSettings)
                UI->>V: validateDataProcessing(fileType, fileQuality, colorType, imageSettings)
                alt Validation failed
                    V-->>UI: return("FAILED")
                    UI-->>A: showError("Invalid input value. Please enter valid decimal numbers within the allowed range.")
                else All validations passed
                    V-->>UI: return("OK")

                    %% === 5. Save Science Plan ===
                    UI->>SP: createSciencePlan(data)
                    SP->>SP: setStatus("CREATED")
                    SP->>DB: savePlan()
                    DB-->>SP: return("SUCCESS")
                    SP-->>UI: return(planID)
                    UI-->>A: showConfirmation("Science Plan created successfully with ID: " + planID)
                end
            end
        end
    end

    %% === 6. Manage Plan (Edit/Delete/Test/Done) ===
    loop Manage Plan
        A->>UI: chooseAction()
        UI-->>A: displayOptions("Edit / Delete / Test / Done")

        alt Done
            UI-->>A: returnToDashboard()
            note over A,UI: Exit loop

        else Edit
            A->>UI: clickEdit()
            UI->>SP: loadPlan(planID)
            SP-->>UI: return(planData)
            UI-->>A: showEditableForm()
            A->>UI: updatePlanDetails()
            UI->>V: validateInputs(updatedData)
            alt Valid
                V-->>UI: return("OK")
                UI->>SP: updatePlan(updatedData)
                SP->>DB: savePlan()
                DB-->>SP: return("SUCCESS")
                SP-->>UI: return("UPDATED")
                UI-->>A: showConfirmation("Plan updated successfully")
            else Invalid
                V-->>UI: return("FAILED")
                UI-->>A: showError("Invalid input")
            end

        else Delete
            A->>UI: clickDelete()
            UI-->>A: confirmDelete("Are you sure you want to delete this plan?")
            A->>UI: confirmYes()
            UI->>SP: deletePlan(planID)
            SP->>DB: removePlan(planID)
            DB-->>SP: return("DELETED")
            SP-->>UI: return("SUCCESS")
            UI-->>A: showSuccess("Plan deleted successfully")

        else Test
            A->>UI: clickTest()
            note over A,UI: Proceed to UC02: Submit Science Plan
        end
    end
```

---

# UC02: Submit Science Plan

## Use Case Description

### Use Case Name

Submit Science Plan

### Use Case ID

02

### Use Case Type

Detail and Essential

### Primary Actor

Astronomer

### Importance Level

High

### Stakeholders and Interest

- Astronomer
- Science Observer
- System

### Brief Description

After completing and testing the science plan, the Astronomer submits it for review. The system checks that the plan has passed required tests and contains valid information. If all checks pass, the system updates the plan's status to "SUBMITTED" and stores it for later validation by the Science Observer.

### Trigger

External trigger

---

## Activity Diagram

```mermaid
flowchart TD

A0([Start]) --> A1[Astronomer: Open 'Submit Plan' page]
A1 --> A2[System: List plans with status = CREATED or TESTED]
A2 --> A3[Astronomer: Select plan by Plan ID]
A3 --> A4{Plan found?}
A4 -- No --> A5[Notify: 'Plan not found']
A5 --> A3
A4 -- Yes --> A6[System: Load plan metadata and schedule]

A6 --> A6a{Status = TESTED?}
A6a -- Yes --> A25[Astronomer: Click 'Submit Plan']
A6a -- No --> A7[Astronomer: Click 'Test Plan']
A7 --> A8[System: Run validation tests]

%% Validation checks
A8 --> A9[Check: Observation time window<br/>Start-end date within observing month?]
A9 --> A10[Check: Telescope location vs constellation quadrant]
A10 --> A11{Quadrant match?}
A11 -- Northern Quadrant --> A12[Verify: HAWAII telescope]
A11 -- Southern Quadrant --> A13[Verify: CHILE telescope]
A12 --> A14[Check: Data Processing Requirements]
A13 --> A14
A14 --> A17[Check: Schedule conflicts with other plans]

A17 --> A18{All test<br/>conditions passed?}
A18 -- No --> A19[System: Keep plan in CREATED state]
A19 --> A20[System: Display 'Test failed' message<br/>Show failure reasons to Astronomer:<br/>❌ Observation month mismatch<br/>❌ Telescope location mismatch<br/>❌ Missing data-processing requirement<br/>❌ Schedule conflict]
A20 --> A21{Astronomer choice?}
A21 -- Fix and retry --> A3
A21 -- Cancel --> A99([End])

A18 -- Yes --> A22[System: Set status = TESTED]
A22 --> A23[System: Save test results to database]
A23 --> A24[Display: '✅ Science Plan tested successfully']

A24 --> A25[Astronomer: Click 'Submit Plan']
A25 --> A26[System: Check current status]
A26 --> A27{Status = TESTED?}
A27 -- No --> A28[Stop: Notify 'Plan must be tested first']
A28 --> A3

A27 -- Yes --> A29[System: Update plan status to SUBMITTED]
A29 --> A30[System: Save to database]
A30 --> A31[System: Prepare for review by Science Observer]
A31 --> A32[System: Notify Science Observer<br/>'New Science Plan ready for review']
A32 --> A33[Display confirmation to Astronomer<br/>'Plan submitted successfully']
A33 --> A99
```

---

## Sequence Diagram

```mermaid
sequenceDiagram
    autonumber
    participant A as Astronomer
    participant UI as UI
    participant SP as SciencePlan
    participant SS as StarSystem
    participant TS as TelescopeSite
    participant V as Validator
    participant DB as Database
    participant SO as ScienceObserver

    %% === Phase 1: Retrieve existing plan ===
    A->>UI: openSubmitPlanPage()
    UI->>SP: listPlans(status="CREATED or TESTED")
    SP-->>UI: return(plansList)
    UI-->>A: displayPlans()

    A->>UI: selectSciencePlan(planID)
    UI->>SP: retrieveSciencePlan(planID)
    alt Plan not found
        SP-->>UI: return("NOT_FOUND")
        UI-->>A: notify("Plan not found — try again")
    else Plan found
        SP-->>UI: return(planData)
        UI-->>A: loadMetadataAndSchedule()
    end

    %% === Phase 2: Testing phase ===
    A->>UI: clickTestPlan()
    UI->>SP: runTesting()

    SP->>SS: checkObservationWindow(observationDates)
    alt Observation month mismatch
        SS-->>SP: return("FAILED: Month mismatch")
    else OK
        SS-->>SP: return("OK")
    end

    SP->>TS: checkTelescopeLocation(starSystem.quadrant)
    alt Telescope location mismatch
        TS-->>SP: return("FAILED: Telescope mismatch")
    else OK
        TS-->>SP: return("OK")
    end

    SP->>V: validateDataProcessing(fileType, fileQuality, colorType, imageSettings)
    alt Data processing invalid
        V-->>SP: return("FAILED: Missing data-processing requirement")
    else OK
        V-->>SP: return("OK")
    end

    SP->>SP: evaluateTestConditions()
    alt Any check FAILED
        SP-->>UI: return(testResults: "FAILED")
        UI-->>A: showMessage("❌ Test failed")
        UI-->>A: displayFailureReasons(["Month mismatch","Telescope mismatch","Missing data-processing requirement"])
    else All checks PASSED
        SP->>SP: updateStatus("TESTED")
        SP->>DB: savePlan()
        DB-->>SP: return("SUCCESS")
        SP-->>UI: return(testResults: "PASSED")
        UI-->>A: showMessage("✅ Science Plan tested successfully")
    end

    %% === Phase 3: Submission phase ===
    A->>UI: clickSubmit()
    UI->>SP: checkStatus()
    alt Status != TESTED
        SP-->>UI: return("NOT_TESTED")
        UI-->>A: notify("Plan must be tested before submission")
    else Status == TESTED
        UI->>SP: submitSciencePlan()
        SP->>SP: updateStatus("SUBMITTED")
        SP->>DB: savePlan()
        DB-->>SP: return("SUCCESS")
        SP-->>UI: return("SUBMITTED")
        UI-->>A: confirm("Plan submitted successfully")
        UI->>SO: notify("New Science Plan ready for review")
        SO->>SO: receiveNotification("Plan ID: " + planID + " submitted for review")
    end
```

---

# UC03: Review & Validate Submitted Plan

## Use Case Description

### Use Case Name

Review & Validate Submitted Plan

### Use Case ID

03

### Use Case Type

Detail and Essential

### Primary Actor

Science Observer

### Importance Level

High

### Stakeholders and Interest

- Science Observer
- Astronomer
- System

### Brief Description

The Science Observer retrieves a submitted science plan and reviews its details. The system supports validation by checking the plan's star system visibility, telescope site suitability, plan metadata and data-processing requirements. Based on the results, the observer marks the plan as either "VALIDATED" or "INVALIDATED".

### Trigger

External trigger

---

## Activity Diagram

```mermaid
flowchart TD

A0([Start]) --> A1[Science Observer: Open 'Review Submitted Plans' page]
A1 --> A2[System: List all plans with status = SUBMITTED]
A2 --> A3[Observer: Select a plan by Plan ID]
A3 --> A4[System: Load and display plan details<br/>Metadata, Objectives, Target Star,<br/>Telescope, Data-processing requirements]

%% === Begin Validation Block ===
A4 --> A5[Observer: Click 'Validate Plan']
A5 --> A6[System: Begin automated validation]

%% Step 1: Star System Visibility
A6 --> A7[Step 1: Check Star System Visibility]
A7 --> A8{Observing month consistent<br/>with date range?}
A8 -- No --> F1[Mark validation FAILED<br/>Reason: Observation time<br/>not in visibility month]
F1 --> A13

A8 -- Yes --> A9[Step 2: Check Telescope Site Suitability]
A9 --> A10{Telescope matches quadrant?<br/>NQ→HAWAII, SQ→CHILE}
A10 -- No --> F2[Mark validation FAILED<br/>Reason: Telescope<br/>location mismatch]
F2 --> A13

A10 -- Yes --> A11[Step 3: Check Plan Metadata]
A11 --> A12{Creator, funding and<br/>objective complete?}
A12 -- No --> F3[Mark validation FAILED<br/>Reason: Missing plan metadata]
F3 --> A13

A12 -- Yes --> A14[Step 4: Check Data Processing Requirements]
A14 --> A15{File type, quality and<br/>image settings complete and valid?}
A15 -- No --> F4[Mark validation FAILED<br/>Reason: Missing or invalid<br/>data-processing requirements]
F4 --> A13

A15 -- Yes --> A16[Step 5: Check Scheduling Conflicts]
A16 --> A17{Conflicts with<br/>other plans?}
A17 -- Yes --> A18[System: Display warning<br/>'⚠️ Scheduling conflicts detected']
A18 --> A19[Show conflicting plan details:<br/>Plan IDs, dates, telescopes]
A19 --> A20a[System: Show summary<br/>'⚠️ Validation passed but conflicts exist<br/>Review and decide']

A17 -- No --> A20b[System: Show summary<br/>'✅ All checks passed']

%% === Decision by Observer ===
A13[System: Show validation errors] --> A21{Observer decision?}
A20a --> A21
A20b --> A21

A21 -- Validate --> P1[System: Mark plan as VALIDATED<br/>Status = VALIDATED<br/>Record reviewer and timestamp]
A21 -- Reject --> P2[System: Mark plan as INVALIDATED<br/>Status = INVALIDATED<br/>Record rejection reason]

P1 --> A22[System: Save to database]
P2 --> A22

A22 --> A23[System: Notify Astronomer<br/>Validation result and comments]
A23 --> A24{Status?}
A24 -- VALIDATED --> A25[Astronomer receives:<br/>'Your plan has been VALIDATED']
A24 -- INVALIDATED --> A26[Astronomer receives:<br/>'Your plan has been REJECTED<br/>Please correct and resubmit']

A25 --> A99([End])
A26 --> A99
```

---

## Sequence Diagram

```mermaid
sequenceDiagram
    autonumber
    actor SO as ScienceObserver
    participant UI as UI
    participant SP as SciencePlan
    participant SS as StarSystem
    participant TS as TelescopeSite
    participant V as Validator
    participant DB as Database
    actor A as Astronomer

    %% === 1. Review Submitted Plans ===
    SO->>UI: openValidationPage()
    UI->>SP: listPlans(status="SUBMITTED")
    SP->>DB: queryPlans()
    DB-->>SP: return(submittedPlansList)
    SP-->>UI: return(submittedPlansList)
    UI-->>SO: displaySubmittedPlans()

    SO->>UI: selectSciencePlan(planID)
    UI->>SP: retrievePlanDetails(planID)
    SP->>DB: loadPlan(planID)
    DB-->>SP: return(planData)
    SP-->>UI: return(metadata, objectives, targetStar, telescope, dataProcessing)
    UI-->>SO: displayPlanDetails()

    %% === 2. Begin Validation ===
    SO->>UI: clickValidate()
    UI->>SP: validateSciencePlan()

    %% Step 1: Star System Visibility
    SP->>SS: checkObservationWindow(observationDates)
    alt Observation month mismatch
        SS-->>SP: return("FAILED: Observation not in visibility month")
        SP-->>UI: return(validationResult: "FAILED")
        UI-->>SO: displayFailureReasons("Observation time not in visibility month")
    else OK
        SS-->>SP: return("OK")

        %% Step 2: Telescope Site Suitability
        SP->>TS: checkTelescopeLocation(starSystem.quadrant)
        alt Telescope mismatch
            TS-->>SP: return("FAILED: Telescope location mismatch")
            SP-->>UI: return(validationResult: "FAILED")
            UI-->>SO: displayFailureReasons("Telescope location mismatch")
        else OK
            TS-->>SP: return("OK")

            %% Step 3: Plan Metadata Validation
            SP->>V: validateMetadata(creator, funding, objective)
            alt Metadata incomplete
                V-->>SP: return("FAILED: Missing plan metadata")
                SP-->>UI: return(validationResult: "FAILED")
                UI-->>SO: displayFailureReasons("Missing plan metadata")
            else OK
                V-->>SP: return("OK")

                %% Step 4: Data Processing Check
                SP->>V: validateDataProcessing(fileType, fileQuality, colorType, imageSettings)
                alt Data processing invalid
                    V-->>SP: return("FAILED: Invalid data-processing values")
                    SP-->>UI: return(validationResult: "FAILED")
                    UI-->>SO: displayFailureReasons("Missing or invalid data-processing requirements")
                else All checks passed
                    V-->>SP: return("OK")

                    %% Step 5: Check Scheduling Conflicts (FR17)
                    SP->>DB: checkSchedulingConflicts(observationDates, telescopeSite)
                    DB-->>SP: return(conflictsList)
                    alt Conflicts detected
                        SP-->>UI: return(conflictsList)
                        UI-->>SO: displayWarning("⚠️ Scheduling conflicts detected")
                        UI-->>SO: showConflictingPlanDetails(planIDs, dates, telescopes)
                        UI-->>SO: showSummary("⚠️ Validation passed but conflicts exist - Review and decide")
                    else No conflicts
                        SP-->>UI: return("NO_CONFLICTS")
                        UI-->>SO: showSummary("✅ All checks passed")
                    end
                end
            end
        end
    end

    %% === 3. Observer Decision ===
    SO->>UI: makeDecision()
    UI-->>SO: promptDecision("Validate | Reject")

    alt decision == VALIDATE
        SO->>UI: confirmValidation()
        UI->>SP: approvePlan()
        SP->>SP: updateStatus("VALIDATED")
        SP->>DB: savePlan()
        DB-->>SP: return("SUCCESS")
        SP-->>UI: return("VALIDATED")
        UI-->>SO: notify("Plan validated successfully")
        UI->>A: notifyAstronomer("Your plan has been VALIDATED")
        A->>A: receiveNotification("Plan validated successfully")

    else decision == REJECT
        SO->>UI: clickReject(rejectionReason)
        UI->>SP: rejectPlan(reason)
        SP->>SP: updateStatus("INVALIDATED")
        SP->>SP: addRejectionNotes(reason)
        SP->>DB: savePlan()
        DB-->>SP: return("SUCCESS")
        SP-->>UI: return("INVALIDATED")
        UI-->>SO: notify("Plan rejected — corrections required")
        UI->>A: notifyAstronomer("Your plan has been REJECTED", reason)
        A->>A: receiveNotification("Plan rejected — please correct and resubmit")
    end
```

---

# UC04: Create Observing Program

## Use Case Description

### Use Case Name

Create Observing Program (transform a validated plan into an observing program)

### Use Case ID

04

### Use Case Type

Detail and Essential

### Primary Actor

Science Observer

### Importance Level

Medium

### Stakeholders and Interest

- Science Observer
- Astronomer

### Brief Description

After a science plan is validated, the Science Observer can transform it into an Observing Program. The Science Observer can provide additional required information such as optics, fold mirror, calibration unit, light type, waypoints, and special equipment. Once complete, the system saves the program and links it to the original science plan.

### Trigger

External trigger

---

## Activity Diagram

```mermaid
flowchart TD

A0([Start]) --> A1[Science Observer: Open 'Create Observing Program' page]
A1 --> A2[System: List Science Plans with status = VALIDATED]
A2 --> A3{Any validated<br/>plan available?}
A3 -- No --> E1[Display: 'No validated science plan<br/>available to transform.']
E1 --> A99([End])

A3 -- Yes --> A4[Observer: Select a validated Science Plan by Plan ID]
A4 --> A5[System: Load base data from Science Plan<br/>Objectives, Target Star, Dates,<br/>Telescope, Data-Processing, Plan ID]

%% Step 3: Input additional technical details
A5 --> A6[Observer: Provide additional technical information<br/>Optics, Fold Mirror, Calibration Unit,<br/>Light Type, Waypoints, Equipment, Module Content]

A6 --> A7{All required<br/>fields filled?}
A7 -- No --> E2[Display error: Missing required fields<br/>e.g., Fold Mirror Type, Waypoints]
E2 --> A6

A7 -- Yes --> A8{All values valid<br/>within defined lists?}
A8 -- No --> E3[Display error: Invalid value<br/>e.g., Calibration Unit must be<br/>one of ARGON, XENON, THAR, CUAR]
E3 --> A6

A8 -- Yes --> A9[System: Validate all entries<br/>Check completeness, data format, allowed values]
A9 --> A10{All validation<br/>checks passed?}
A10 -- No --> E4[Show error summary<br/>User must correct invalid fields]
E4 --> A6

A10 -- Yes --> A11[Observer: Click 'Save Observing Program']
A11 --> A12[System: Create new Observing Program record<br/>Status = CREATED<br/>Link with original Science Plan via Plan ID]

A12 --> A13[System: Save to database]
A13 --> A14{Database save<br/>successful?}
A14 -- No --> E5[Display error: Failed to save.<br/>Please try again or contact admin.]
E5 --> A11

A14 -- Yes --> A15[System: Display confirmation<br/>'Observing Program created and linked<br/>to Science Plan #XXXX']
A15 --> A16[System: Notify Astronomer<br/>'Your Science Plan has been<br/>transformed to Observing Program']
A16 --> A99
```

---

## Sequence Diagram

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

    %% === 1. Retrieve Validated Plans ===
    SO->>UI: openCreateObservingProgramPage()
    UI->>SP: listPlans(status="VALIDATED")
    alt No validated plans available
        SP-->>UI: return(emptyList)
        UI-->>SO: display("No validated science plan available to transform.")
    else Validated plans exist
        SP-->>UI: return(validatedPlansList)
        UI-->>SO: displayValidatedPlans()

        %% === 2. Select Plan ===
        SO->>UI: selectSciencePlan(planID)
        UI->>SP: loadPlan(planID)
        SP-->>UI: return(planData: objectives, targetStar, dates, telescope, dataProcessing)
        UI-->>SO: displayPlanBaseData()

        %% === 3. Input Technical Details ===
        SO->>UI: inputTechnicalDetails(optics, foldMirror, calibrationUnit, lightType, waypoints, equipment, moduleContent)

        %% === 4. Validate Required Fields ===
        UI->>V: validateRequiredFields(data)
        alt Missing required fields
            V-->>UI: return("FAILED: Missing required fields")
            UI-->>SO: displayError("Missing required fields (e.g., Fold Mirror Type, Waypoints)")
            SO->>UI: inputTechnicalDetails()
        else All required fields present
            V-->>UI: return("OK")

            %% === 5. Validate Values ===
            UI->>V: validateValues(calibrationUnit, foldMirror, lightType)
            alt Invalid values
                V-->>UI: return("FAILED: Invalid value")
                UI-->>SO: displayError("Invalid value (e.g., Calibration Unit must be one of [ARGON, XENON, THAR, CUAR])")
                SO->>UI: inputTechnicalDetails()
            else All values valid
                V-->>UI: return("OK")

                %% === 6. Final Validation ===
                UI->>V: validateAllEntries(completeness, dataFormat, allowedValues)
                alt Validation failed
                    V-->>UI: return(errorSummary)
                    UI-->>SO: showErrorSummary("User must correct invalid fields")
                    SO->>UI: inputTechnicalDetails()
                else All validation checks passed
                    V-->>UI: return("PASSED")

                    %% === 7. Save Observing Program ===
                    SO->>UI: clickSaveObservingProgram()
                    UI->>OP: createObservingProgram(planID, technicalData)
                    OP->>OP: linkToSciencePlan(planID)
                    OP->>OP: setStatus("CREATED")

                    OP->>DB: saveObservingProgram()
                    alt Database save failed
                        DB-->>OP: return("FAILED")
                        OP-->>UI: return("FAILED")
                        UI-->>SO: displayError("Failed to save. Please try again or contact admin.")
                    else Database save successful
                        DB-->>OP: return("SUCCESS")
                        OP-->>UI: return(programID)

                        %% === 8. Confirmation and Notification ===
                        UI-->>SO: displayConfirmation("Observing Program created and linked to Science Plan No. " + planID)
                        UI->>A: notifyAstronomer("Your Science Plan has been transformed to Observing Program")
                        A->>A: receiveNotification("Plan transformed successfully")
                    end
                end
            end
        end
    end
```

---

# UC05: Execute Observing Program

## Use Case Description

### Use Case Name

Execute Observing Program

### Use Case ID

05

### Use Case Type

Detail and Essential

### Primary Actor

Operations Staff

### Importance Level

High

### Stakeholders and Interest

- Operations Staff
- Science Observer

### Brief Description

Operations Staff selects a submitted Observing Program and starts the execution process. Throughout the program, the staff actively monitors telescope status, confirms critical steps, and manually performs actions such as pausing, resuming, or canceling as needed. The system assists by displaying real-time feedback, logging actions, and alerting the staff to potential issues. The staff is responsible for deciding how to respond during execution.

### Trigger

External trigger

---

## Activity Diagram

```mermaid
flowchart TD

A0([Start]) --> A1[Operations Staff: Open Execute Page]
A1 --> A2[System: List programs with status = CREATED]
A2 --> A3{Any program<br/>available?}
A3 -- No --> E1[Display: 'No program to run']
E1 --> A99([End])

A3 -- Yes --> A4[Ops Staff: Select program]
A4 --> A5[System: Show details<br/>target, telescope, schedule,<br/>waypoints, equipment]

A5 --> A6[System: Run pre-execution checks]
A6 --> A7{Any issues?}
A7 -- Yes --> E2[Display issues found:<br/>• Calibration error<br/>• Telescope busy<br/>• Missing waypoint]
E2 --> A5

A7 -- No --> A8{Another program RUNNING<br/>on same telescope?}
A8 -- Yes --> E3[Display: 'Program already running<br/>on this telescope']
E3 --> A5

A8 -- No --> A9[System: Enable Start button]
A9 --> A10[Ops Staff: Click Start]
A10 --> A11[System: Update status → RUNNING]
A11 --> A12[System: Create execution log<br/>Log: Started, timestamp]
A12 --> A13[System: Save status to database]
A13 --> A14[System: Show real-time monitor]

%% Execution loop
A14 --> A15[System: Begin waypoint processing]
A15 --> A16{Process next waypoint}
A16 -- Yes --> A17[Move telescope to waypoint<br/>azimuth, altitude]
A17 --> A18[Capture astronomical data]
A18 --> A19[Store dataset for this waypoint]

A19 --> A20{Problem detected?}
A20 -- Yes --> A21[System: Alert operator<br/>• Telescope failure<br/>• Weather bad<br/>• Hardware error]
A21 --> A22[Ops Staff: Choose action]
A22 --> A23{Which action?}

A23 -- Pause --> P1[System: Pause execution<br/>status stays RUNNING]
P1 --> LP[Log: Paused, reason, timestamp]
LP --> A24[System: Save to database]
A24 --> A25[Monitor shows: Paused]
A25 --> A26[Wait for operator action]
A26 --> A27[Ops Staff: Click Resume]
A27 --> P2[System: Update status → RUNNING]
P2 --> LR[Log: Resumed, timestamp]
LR --> A28[System: Save to database]
A28 --> A16

A23 -- Resume --> P2

A23 -- Cancel --> P3[System: Update status → CANCELLED]
P3 --> LC[Log: Cancelled, reason, timestamp]
LC --> A29[System: Save to database]
A29 --> A30[System: Show execution summary<br/>Completed waypoints, logs]
A30 --> A99

A20 -- No --> A31[Update progress display]
A31 --> A32{All waypoints<br/>processed?}
A32 -- No --> A16
A32 -- Yes --> A33[System: Update status → COMPLETE]

%% Data collection and management (FR39-FR42)
A33 --> A34[System: Collect all datasets from waypoints]
A34 --> A35[System: Validate data integrity<br/>Check file completeness,<br/>checksum, metadata consistency]
A35 --> A36{Data validation<br/>passed?}
A36 -- No --> A37[Log: Data validation warnings<br/>List incomplete/corrupt files]
A37 --> A38[System: Flag datasets for review]
A38 --> A39

A36 -- Yes --> A39[System: Catalog datasets<br/>Organize by program ID,<br/>waypoint, timestamp]
A39 --> A40[System: Save datasets to database]
A40 --> A41[System: Associate data with<br/>Observing Program record]

A41 --> A42[Log: Completed, timestamp]
A42 --> A43[System: Save execution log to database]
A43 --> A44[System: Generate execution summary<br/>Start-end time, waypoints completed,<br/>logs, weather conditions, dataset info]

A44 --> A45[System: Notify Science Observer<br/>'Program completed successfully']
A45 --> A46[System: Display completion summary to Ops Staff<br/>Execution details, data status]

A46 --> A47[Science Observer: Review completion and datasets]
A47 --> A48[Science Observer: Prepare data package<br/>Include metadata, execution summary, datasets]
A48 --> A49[Science Observer: Transmit data to Astronomer]

A49 --> A50[Astronomer: Receive astronomical data]
A50 --> A51[Astronomer: Acknowledge receipt]
A51 --> A52[Science Observer: Receive confirmation<br/>'Data successfully delivered']
A52 --> A99
```

---

## Sequence Diagram

```mermaid
sequenceDiagram
    autonumber
    actor Ops as OperationsStaff
    participant UI as ExecutePageUI
    participant OP as ObservingProgram
    participant TEL as TelescopeSite
    participant LOG as ExecutionLog
    participant DB as Database
    participant SO as ScienceObserver
    participant A as Astronomer

    %% === 1. Load and Select Program ===
    Ops->>UI: openExecutePage()
    UI->>OP: listPrograms(status="CREATED")
    OP->>DB: queryPrograms()
    DB-->>OP: return(programsList)
    OP-->>UI: return(programsList)

    alt No program available
        UI-->>Ops: display("No program to run")
    else Programs available
        UI-->>Ops: displayPrograms()
        Ops->>UI: selectProgram(programID)
        UI->>OP: loadProgramDetails(programID)
        OP->>DB: retrieveProgram(programID)
        DB-->>OP: return(programData)
        OP-->>UI: return(target, telescope, schedule, waypoints, equipment)
        UI-->>Ops: displayDetails()

        %% === 2. Pre-Execution Checks ===
        UI->>OP: runPreCheck()
        OP->>TEL: getTelescopeStatus()
        TEL-->>OP: return(calibration, occupancy)

        alt Issues found
            OP-->>UI: return(issues: calibration/busy/missing waypoint)
            UI-->>Ops: displayIssues("Review & adjust required")
        else No issues
            OP->>TEL: checkRunningPrograms()
            TEL-->>OP: return(isRunning)

            alt Another program running
                OP-->>UI: return("CONFLICT")
                UI-->>Ops: display("Program already running on this telescope")
            else Telescope free
                UI-->>Ops: enableStartButton()

                %% === 3. Execute Program ===
                Ops->>UI: clickStart()
                UI->>OP: execute()
                OP->>OP: updateStatus("RUNNING")
                OP->>DB: saveStatus()
                DB-->>OP: return("SUCCESS")
                OP->>LOG: createLog("Started", timestamp)
                LOG->>DB: save()
                DB-->>LOG: return("SUCCESS")
                LOG-->>OP: return(logID)
                OP-->>UI: return("STARTED")
                UI-->>Ops: showRealTimeMonitor()

                %% === 4. Execution Loop ===
                loop For each Waypoint
                    OP->>TEL: moveToWaypoint(azimuth, altitude)
                    OP->>TEL: captureData()

                    alt Problem detected
                        TEL-->>OP: return("ERROR: telescope fail | weather bad | hardware error")
                        OP-->>UI: alert(errorType)
                        UI-->>Ops: promptAction("Pause | Resume | Cancel")

                        alt Pause
                            Ops->>UI: clickPause()
                            UI->>OP: pauseExecution()
                            OP->>DB: saveStatus()
                            DB-->>OP: return("SUCCESS")
                            OP->>LOG: appendLog("Paused", reason)
                            LOG->>DB: save()
                            DB-->>LOG: return("SUCCESS")
                            LOG-->>OP: return("OK")
                            OP-->>UI: return("RUNNING (paused)")
                            UI-->>Ops: showMonitor("Paused")

                        else Resume
                            Ops->>UI: clickResume()
                            UI->>OP: updateStatus("RUNNING")
                            OP->>DB: saveStatus()
                            DB-->>OP: return("SUCCESS")
                            OP->>LOG: appendLog("Resumed", timestamp)
                            LOG->>DB: save()
                            DB-->>LOG: return("SUCCESS")
                            LOG-->>OP: return("OK")
                            OP-->>UI: return("RUNNING")
                            UI-->>Ops: showMonitor("Running")

                        else Cancel
                            Ops->>UI: clickCancel(reason)
                            UI->>OP: updateStatus("CANCELLED")
                            OP->>DB: saveStatus()
                            DB-->>OP: return("SUCCESS")
                            OP->>LOG: appendLog("Cancelled", reason)
                            LOG->>DB: save()
                            DB-->>LOG: return("SUCCESS")
                            LOG-->>OP: return("OK")
                            OP-->>UI: return(executionSummary)
                            UI-->>Ops: showSummary(completedWaypoints, logs)
                        end

                    else No problem
                        TEL-->>OP: return(dataCollected)
                        OP->>OP: storeDataset(waypointID, data)
                        OP-->>UI: updateProgress(currentWaypoint, totalWaypoints)
                        UI-->>Ops: refreshMonitor()
                    end
                end

                %% === 5. Completion & Data Management ===
                OP->>OP: updateStatus("COMPLETE")
                OP->>DB: saveStatus()
                DB-->>OP: return("SUCCESS")
                OP->>LOG: appendLog("Completed", timestamp)
                LOG->>DB: save()
                DB-->>LOG: return("SUCCESS")
                LOG-->>OP: return("OK")

                %% === 6. Data Collection & Validation (FR39-FR42) ===
                OP->>OP: collectDatasets()
                OP->>OP: validateDataIntegrity(checksum, metadata)
                OP->>DB: saveDatasets()
                DB-->>OP: return("SUCCESS")
                OP->>OP: catalogData(programID, waypointData)

                alt Data validation issues detected
                    OP-->>UI: return(executionSummary, warnings)
                    UI-->>Ops: showSummary(startTime, endTime, waypoints, logs, weather, datasetInfo)
                    UI-->>Ops: displayWarning("⚠️ Some data quality issues detected - review required")
                else All data valid
                    OP-->>UI: return(executionSummary)
                    UI-->>Ops: showSummary(startTime, endTime, waypoints, logs, weather, datasetInfo)
                end

                %% === 7. Notify Science Observer ===
                OP->>SO: notify("Program completed", programID, datasetInfo)
                SO->>SO: receiveNotification()
                SO->>SO: reviewCompletionAndDatasets()
                SO->>SO: prepareDataPackage(datasets, metadata, executionSummary)

                %% === 8. Transmit Data to Astronomer ===
                SO->>A: transmitData(dataPackage)
                A->>A: receiveData()
                A-->>SO: acknowledge("Data received successfully")
                SO->>SO: logTransmissionComplete()
            end
        end
    end
```

---
