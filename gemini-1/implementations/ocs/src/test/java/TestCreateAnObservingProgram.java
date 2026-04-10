import static org.junit.jupiter.api.Assertions.*;

import edu.gemini.*;
import edu.gemini.model.*;

import org.junit.jupiter.api.Test;

class TestCreateAnObservingProgram {

    @Test
    void testCreateObservingProgramWithValidInputs() {
        // Prepare input data
        OCS ocs = new OCS();

        // Science Plan
        SciencePlan sp = new SciencePlan();
        sp.setPlanNo(1);
        sp.setName("Test Plan");

        // Science Observer
        ScienceObserver so = new ScienceObserver(1);
        so.setId(42); // Make sure setId exists

        // TelePositionPair (mock one, even if empty)
        AbstractTelePositionPair[] pairs = new AbstractTelePositionPair[1];
        pairs[0] = new TelePositionPair(90.0, 45.0); // Example: East at 45 degrees

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
    }
}