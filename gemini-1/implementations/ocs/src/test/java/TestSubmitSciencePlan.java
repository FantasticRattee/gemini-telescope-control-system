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

        // Create a science plan and set status to TESTED
        plan = new SciencePlan();
        plan.setPlanNo(1);
        plan.setName("Galaxy Study");
        plan.setStatus(AbstractSciencePlan.STATUS.TESTED);

        
        astronomer = new Astronomer();
        astronomer.setFirstName("Chaiyong");
        astronomer.setLastName("Ragkhitwetsagul");
        
        ocs.createSciencePlan(plan, astronomer); 
    }

    @Test
    void testSubmitValidSciencePlan() {
        String result = ocs.submitSciencePlan(plan, astronomer);

        assertEquals("Science plan 1 submitted successfully.", result);
        assertEquals(AbstractSciencePlan.STATUS.SUBMITTED, plan.getStatus());
    }

    @Test
    void testSubmitNullSciencePlan() {
        String result = ocs.submitSciencePlan(null, astronomer);

        assertEquals("Error: No science plan selected.", result);
    }

    @Test
    void testSubmitAlreadySubmittedPlan() {
        plan.setStatus(AbstractSciencePlan.STATUS.SUBMITTED);
        String result = ocs.submitSciencePlan(plan, astronomer);

        assertEquals("This science plan has already been submitted or executed.", result);
    }

    @Test
    void testSubmitUnreadySciencePlan() {
        plan.setStatus(AbstractSciencePlan.STATUS.CREATED); // not TESTED
        String result = ocs.submitSciencePlan(plan, astronomer);

        assertEquals("This science plan is incomplete. Please complete and test it before submission.", result);
    }
}