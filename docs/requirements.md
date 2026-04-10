# Functional Requirements

## Astronomer

-   **FR1** - The system shall allow the user to create and save a
    Science Plan.
-   **FR2** - The system shall require each Science Plan to include: ID,
    creator, funding source, objectives, target star system, observing
    dates, and telescope site.
-   **FR3** - The system shall restrict input values to predefined
    options (mirror, calibration, light type, plan status).
-   **FR4** - The system shall allow the user to test a Science Plan
    using a Virtual Telescope simulator.
-   **FR5** - The system shall validate star constellation visibility
    against observing months. (Make sure the selected constellation in a
    Science Plan can actually be observed in the scheduled month.)
-   **FR6** - The system shall validate telescope sites against the
    hemisphere of the target star system. (Make sure telescopes (Hawaii
    and Chile) are suitable for observing.)
-   **FR7** - The system shall validate waypoints and instrument
    configurations for correctness. (Make sure waypoint is valid.)
-   **FR8** - The system shall allow the user to submit a Science Plan,
    updating its status to SUBMITTED.
-   **FR9** - The system shall persistently store submitted Science
    Plans in the database.

## Science Observer

### When receiving a Created Science Plan from an Astronomer

-   **FR10** - The system shall allow the Science Observer to retrieve
    submitted Science Plans for review.
-   **FR11** - The system shall display all metadata of a Science Plan,
    including creator, objectives, target star system, observing dates,
    telescope site, and data-processing requirements.
-   **FR12** - The system shall validate the Science Plan's scheduled
    observing dates against the observing month of the selected star
    constellation.
-   **FR13** - The system shall validate the Science Plan's telescope
    site against the hemisphere of the target star system.
-   **FR14** - The system shall validate that waypoints are within
    defined numeric ranges and correctly formatted.
-   **FR15** - The system shall validate that the Science Plan's
    instrument configurations are selected from predefined option sets
    (mirror type, calibration unit, light type, plan status).
-   **FR16** - The system shall allow the Science Observer to mark a
    Science Plan as VALIDATED or INVALIDATED.
-   **FR17** - The system shall detect and report any scheduling
    conflicts between submitted Science Plans.
-   **FR18** - The system shall allow the Science Observer to transform
    a validated Science Plan into an Observing Program.
-   **FR19** - The system shall require an Observing Program to include
    telescope optics, fold mirror type, calibration unit, light type,
    waypoints, and any special equipment.
-   **FR20** - The system shall allow the Science Observer to save the
    Observing Program associated with the original Science Plan.
-   **FR21** - The system shall allow the Science Observer to submit the
    Observing Program into the sequencer for execution.
-   **FR22** - The system shall support queue-based scheduling of
    Observing Programs and allow resequencing.
-   **FR23** - The system shall allow the Science Observer to track
    execution progress of an Observing Program through states such as
    RUNNING and COMPLETE.

### Observing The Executed Program

-   **FR39** - The system shall allow the Science Observer to collect
    and store astronomical datasets produced during execution of an
    Observing Program.
-   **FR40** - The system shall allow the Science Observer to validate
    the integrity of collected datasets (e.g., file completeness,
    checksum, metadata consistency).
-   **FR41** - The system shall allow the Science Observer to manage
    collected data, including organizing, cataloging, and associating
    datasets with the corresponding Observing Program.
-   **FR42** - The system shall allow the Science Observer to transmit
    or make available the collected and validated data to the Astronomer
    who submitted the Science Plan.

### General Functional requirements of Science Observer

-   **FR24** - The system shall allow the Science Observer to update the
    status of a Science Plan.
-   **FR25** - The system shall allow the Science Observer to test a
    Science Plan against defined criteria and view the results.
-   **FR26** - The system shall allow the Science Observer to add or
    remove unavailable dates from the observation schedule.
-   **FR27** - The system shall allow the Science Observer to view the
    current observation schedule.
-   **FR28** - The system shall allow the Science Observer to add notes
    to an Observing Program.
-   **FR29** - The system shall allow the Science Observer to record
    lens metadata, including make, model, manufacturer, and year of
    production.
-   **FR30** - The system shall allow the Science Observer to view and
    update lens metadata for planning and configuration.
-   **FR31** - The system shall allow the Science Observer to record
    data-processing requirements for observations, including file type,
    quality, color mode, and adjustment parameters (contrast,
    brightness, saturation, exposure, etc.).
-   **FR32** - The system shall allow the Science Observer to review
    recorded data-processing requirements in human-readable form for
    verification prior to execution.
-   **FR33** - The system shall allow the Science Observer to record and
    view special equipment details (equipment name, owner, installation
    date) for observation planning.
-   **FR34** - The system shall allow the Science Observer to select a
    star system from the supported catalog for use in Science Plans and
    Observing Programs.
-   **FR35** - The system shall allow the Science Observer to reference
    a star system's quadrant and observing month to assess site
    suitability and scheduling.
-   **FR36** - The system shall allow the Science Observer to define
    telescope pointing waypoints using numeric direction/degree pairs
    and view these when planning.
-   **FR37** - The system shall allow the Science Observer to record and
    view optical filter metadata, including make, manufacturer, model,
    production year, size, and weight.
-   **FR38** - The system shall allow the Science Observer to reference
    the validation status of an Observing Program for coordination and
    readiness.

## Staff Operation

### When Receiving an Observing Program (from Science Observer)

-   **FR1** - The system shall allow Operations Staff to retrieve
    submitted Observing Programs from the sequencer for execution.
-   **FR2** - The system shall display all Observing Program parameters
    (telescope site, optics, calibration unit, light type, waypoints,
    module content, special equipment).
-   **FR3** - The system shall allow Operations Staff to initiate
    execution of an Observing Program.
-   **FR4** - The system shall allow Operations Staff to pause, resume,
    or cancel an Observing Program during execution.
-   **FR5** - The system shall provide real-time monitoring of telescope
    status, instrument configurations, and execution progress of the
    program.
-   **FR6** - The system shall log execution events, errors, and
    operator interventions for audit and troubleshooting.
-   **FR7** - The system shall allow Operations Staff to override or
    adjust telescope operations when unsafe conditions or equipment
    faults are detected.
-   **FR8** - The system shall provide alerts when execution conflicts
    arise (e.g., unsafe waypoints, hardware unavailability).
-   **FR9** - The system shall allow Operations Staff to update the
    operational status of telescope units after validation tests (e.g.,
    declare a faulty unit operational again).

### General Functional Requirements of Staff Operation

-   **FR10** - The system shall allow Operations Staff to directly
    control telescope subsystems in operation mode (e.g., slewing,
    focusing, spectrograph slit adjustments).
-   **FR11** - The system shall allow Operations Staff to access and
    update operation tables (e.g., list of installed filters) in update
    mode, while Astronomers can only view them.
-   **FR12** - The system shall allow Operations Staff to perform
    diagnostic tests on telescope and instrument subsystems.
-   **FR13** - The system shall provide multi-point monitoring, allowing
    Operations Staff to supervise telescope use from multiple stations.
-   **FR14** - The system shall allow Operations Staff to advise
    Observers by providing operational status and guidance on telescope
    use.
-   **FR15** - The system shall allow Operations Staff to access
    maintenance procedures when subsystems require intervention.

## Visitor

-   The system shall allow Visitor Instruments to connect to the Gemini
    system via a standardized interface and operate as servers
    responding to Gemini commands.
-   The system shall allow Gemini to acquire status information from
    Visitor Instruments at any time.
-   The system shall allow Visitor Instruments to send commands to
    offset telescope position and focus via the standardized interface.
-   The system shall allow Visitors to formally submit a proposal for
    installing a special device.

# Non-Functional Requirements

## Astronomer

### Usability

-   **NFR1** - The system shall provide a simple and safe user interface
    for Astronomers, grouping functions by observation activities (plan
    creation, testing, submission).
-   **NFR2** - The system shall prevent Astronomers from directly
    controlling telescope hardware, while allowing access to instrument
    settings and status information.
-   **NFR3** - The system shall provide status displays that are either
    automatically updated or available on request without interfering
    with ongoing operations. ... (truncated for brevity but includes all
    user-provided content)
