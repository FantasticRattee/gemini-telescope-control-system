import static org.junit.jupiter.api.Assertions.*;

import edu.gemini.*;
import edu.gemini.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestValidateSciencePlan {

    private OCS ocs;
    private Astronomer astronomer;
    private ScienceObserver observer;

    @BeforeEach
    void setup() {
        ocs = new OCS();
        astronomer = new Astronomer();
        astronomer.setFirstName("Chaiyong");
        astronomer.setLastName("Ragkhitwetsagul");
        observer = new ScienceObserver(7);
    }

    // Create -> test -> submit through the OCS
    private SciencePlan submittedPlan(String name, String objective) {
        SciencePlan sp = new SciencePlan();
        sp.setName(name);
        sp.setFunding(1000.0);
        sp.setObjective(objective);
        ocs.createSciencePlan(sp, astronomer);
        ocs.testSciencePlan(sp);
        ocs.submitSciencePlan(sp, astronomer);
        assertEquals(AbstractSciencePlan.STATUS.SUBMITTED, sp.getStatus());
        return sp;
    }

    @Test
    void testValidatesSubmittedPlan() {
        SciencePlan sp = submittedPlan("Complete Plan", "Observe the Gemini constellation.");

        SciencePlan result = ocs.validateSciencePlan(sp, observer);

        assertSame(sp, result);
        assertEquals(AbstractSciencePlan.STATUS.VALIDATED, result.getStatus());
    }

    @Test
    void testInvalidatesSubmittedPlanWithMissingObjective() {
        SciencePlan sp = submittedPlan("No Objective Plan", null);

        SciencePlan result = ocs.validateSciencePlan(sp, observer);

        assertEquals(AbstractSciencePlan.STATUS.INVALIDATED, result.getStatus());
    }

    @Test
    void testRejectsPlanThatIsNotSubmitted() {
        SciencePlan sp = new SciencePlan();
        sp.setName("Only Created");
        sp.setFunding(1000.0);
        sp.setObjective("Not submitted yet.");
        ocs.createSciencePlan(sp, astronomer);

        assertNull(ocs.validateSciencePlan(sp, observer));
        assertEquals(AbstractSciencePlan.STATUS.CREATED, sp.getStatus());
    }
}
