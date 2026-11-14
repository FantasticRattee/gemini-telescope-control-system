import static org.junit.jupiter.api.Assertions.*;

import edu.gemini.*;
import edu.gemini.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestCreateAnObservingProgram {

    private OCS ocs;
    private SciencePlan sp;
    private ScienceObserver so;
    private Astronomer as;
    private AbstractTelePositionPair[] pairs;

    @BeforeEach
    void setup() {
        // Prepare input data
        ocs = new OCS();

        as = new Astronomer();
        as.setFirstName("Chaiyong");
        as.setLastName("Ragkhitwetsagul");

        // Science Plan (created through the OCS so it gets a plan number and creator)
        sp = new SciencePlan();
        sp.setName("Test Plan");
        sp.setFunding(1000.0);
        sp.setObjective("Observe the Gemini constellation.");
        ocs.createSciencePlan(sp, as);

        // Science Observer
        so = new ScienceObserver(1);
        so.setId(42); // Make sure setId exists

        // TelePositionPair (mock one, even if empty)
        pairs = new AbstractTelePositionPair[1];
        pairs[0] = new TelePositionPair(90.0, 45.0); // Example: East at 45 degrees
    }

    private ObservingProgram createProgram() {
        return ocs.createObservingProgram(
                sp, "GNZ", 4.0, 5.5, 35.0,
                AbstractObservingProgramConfigs.FoldMirrorType.CASSEGRAIN,
                2,
                AbstractObservingProgramConfigs.CalibrationUnit.ARGON,
                AbstractObservingProgramConfigs.LightType.MAUNA_KEA_SKY_EMISSION,
                pairs, so);
    }

    @Test
    void testCreateObservingProgramWithValidInputs() {
        // The plan must be VALIDATED first: CREATED -> TESTED -> SUBMITTED -> VALIDATED
        ocs.testSciencePlan(sp);
        ocs.submitSciencePlan(sp, as);
        ocs.validateSciencePlan(sp, so);
        assertEquals(AbstractSciencePlan.STATUS.VALIDATED, sp.getStatus());

        // Create observing program
        ObservingProgram op = ocs.createObservingProgram(
                sp,
                "GNZ",                       // opticsPrimary
                4.0,                         // fStop (valid range for GNZ is 1.8–8.1)
                5.5,                         // opticsSecondaryRMS (valid for Chile)
                35.0,                        // scienceFoldMirrorDegree [30–45]
                AbstractObservingProgramConfigs.FoldMirrorType.CASSEGRAIN,
                2,                           // moduleContent
                AbstractObservingProgramConfigs.CalibrationUnit.ARGON,
                AbstractObservingProgramConfigs.LightType.MAUNA_KEA_SKY_EMISSION,
                pairs,
                so
        );

        // Assertions
        assertNotNull(op);
        assertEquals(sp, op.getSciencePlan());
        assertEquals("GNZ", op.getOpticsPrimary());
        assertEquals(4.0, op.getfStop());
        assertEquals(5.5, op.getOpticsSecondaryRMS());
        assertEquals(35.0, op.getScienceFoldMirrorDegree());
        assertEquals(AbstractObservingProgramConfigs.FoldMirrorType.CASSEGRAIN, op.getScienceFoldMirrorType());
        assertEquals(2, op.getModuleContent());
        assertEquals(AbstractObservingProgramConfigs.CalibrationUnit.ARGON, op.getCalibrationUnit());
        assertEquals(AbstractObservingProgramConfigs.LightType.MAUNA_KEA_SKY_EMISSION, op.getLightType());
        assertEquals(pairs, op.getTelePositionPairs());
        assertEquals(42, op.getObserverId());
        assertEquals(AbstractSciencePlan.STATUS.CREATED, op.getStatus()); // Assuming you set this
        assertSame(op, ocs.getObservingProgramBySciencePlan(sp)); // stored and linked to the plan
    }

    @Test
    void testRejectsPlanThatIsNotValidated() {
        // Plan is only CREATED (not tested, submitted or validated)
        ObservingProgram op = createProgram();

        assertNull(op);
        assertNull(ocs.getObservingProgramBySciencePlan(sp));
    }
}