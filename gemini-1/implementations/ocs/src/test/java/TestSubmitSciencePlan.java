import static org.junit.jupiter.api.Assertions.*;

import edu.gemini.*;
import edu.gemini.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestSubmitSciencePlan {

    private OCS ocs;
    private SciencePlan plan;
    private Astronomer astronomer;

    @BeforeEach
    void setup() {
        ocs = new OCS();

        // Create a valid science plan through the OCS, then test it (CREATED -> TESTED)
        plan = new SciencePlan();
        plan.setName("Galaxy Study");
        plan.setFunding(1000.0);


        astronomer = new Astronomer();
        astronomer.setFirstName("Chaiyong");
        astronomer.setLastName("Ragkhitwetsagul");

        String created = ocs.createSciencePlan(plan, astronomer);
        assertTrue(created.startsWith("Science Plan created successfully"), created);
        assertEquals(1, plan.getPlanNo());

        ocs.testSciencePlan(plan);
        assertEquals(AbstractSciencePlan.STATUS.TESTED, plan.getStatus());
    }

    @Test
    void testSubmitValidSciencePlan() {
        String result = ocs.submitSciencePlan(plan, astronomer);

        assertEquals("Science plan 1 submitted successfully.", result);
        assertEquals(AbstractSciencePlan.STATUS.SUBMITTED, plan.getStatus());
        assertEquals(AbstractSciencePlan.STATUS.SUBMITTED, ocs.getSciencePlanByNo(1).getStatus());
    }

    @Test
    void testSubmitNullSciencePlan() {
        String result = ocs.submitSciencePlan(null, astronomer);

        assertEquals("Error: No science plan selected.", result);
    }

    @Test
    void testSubmitAlreadySubmittedPlan() {
        ocs.submitSciencePlan(plan, astronomer); // first submission succeeds
        String result = ocs.submitSciencePlan(plan, astronomer);

        assertEquals("This science plan has already been submitted or executed.", result);
    }

    @Test
    void testSubmitUnreadySciencePlan() {
        // A second plan that is created but not tested (status stays CREATED)
        SciencePlan untested = new SciencePlan();
        untested.setName("Untested Plan");
        untested.setFunding(500.0);
        ocs.createSciencePlan(untested, astronomer);

        String result = ocs.submitSciencePlan(untested, astronomer);

        assertEquals("This science plan is incomplete. Please complete and test it before submission.", result);
        assertEquals(AbstractSciencePlan.STATUS.CREATED, untested.getStatus());
    }
}