import static org.junit.jupiter.api.Assertions.*;

import edu.gemini.Astronomer;
import edu.gemini.OCS;
import edu.gemini.SciencePlan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

// Error paths of createSciencePlan (the success path is in TestCreateSciencePlan)
class TestCreateSciencePlanTest {

    private OCS ocs;
    private Astronomer astronomer;

    @BeforeEach
    void setup() {
        ocs = new OCS();
        astronomer = new Astronomer();
        astronomer.setFirstName("Chaiyong");
        astronomer.setLastName("Ragkhitwetsagul");
    }

    private SciencePlan newPlan(String name, double funding) {
        SciencePlan sp = new SciencePlan();
        sp.setName(name);
        sp.setFunding(funding);
        return sp;
    }

    @Test
    void testRejectsZeroFunding() {
        String result = ocs.createSciencePlan(newPlan("Zero Funding Plan", 0), astronomer);

        assertEquals("Error: Funding must be a positive number.", result);
        assertTrue(ocs.getAllSciencePlans().isEmpty());
    }

    @Test
    void testRejectsStartDateAfterEndDate() {
        SciencePlan sp = newPlan("Wrong Dates Plan", 100.0);
        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.FEBRUARY, 5);
        sp.setStartDate(cal.getTime());
        cal.set(2025, Calendar.FEBRUARY, 1);
        sp.setEndDate(cal.getTime());

        String result = ocs.createSciencePlan(sp, astronomer);

        assertEquals("Error: Start date cannot be after end date.", result);
        assertTrue(ocs.getAllSciencePlans().isEmpty());
    }

    @Test
    void testRejectsDuplicateName() {
        ocs.createSciencePlan(newPlan("Same Name", 100.0), astronomer);
        String result = ocs.createSciencePlan(newPlan("Same Name", 200.0), astronomer);

        assertEquals("Error: A science plan with this name already exists.", result);
        assertEquals(1, ocs.getAllSciencePlans().size());
    }
}
