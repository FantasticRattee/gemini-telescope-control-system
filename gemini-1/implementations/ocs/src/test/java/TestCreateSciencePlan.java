import edu.gemini.Astronomer;
import edu.gemini.OCS;
import edu.gemini.SciencePlan;
import edu.gemini.model.AbstractSciencePlan;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

public class TestCreateSciencePlan {
    @Test
    public void testCreateSciencePlan1() {
        OCS ocs = new OCS();
        SciencePlan sp = new SciencePlan();
        
        Astronomer as = new Astronomer();
        as.setId(1);
        as.setInstitution("Mahidol University");
        as.setFirstName("Chaiyong");
        as.setLastName("Ragkhitwetsagul");
        
        sp.setName("This is a test science plan.");
        sp.setFunding(1000.1);
        
     // Set minimal required dates to avoid validation error
        sp.setStartDate(new Date());
        sp.setEndDate(new Date());

        String result = ocs.createSciencePlan(sp, as);

        assertEquals("Chaiyong Ragkhitwetsagul", sp.getCreator()); // getName() in AbstractAstronomer uses full name
        assertEquals("This is a test science plan.", sp.getName()); 
        assertTrue(result.startsWith("Science Plan created successfully"));
        assertEquals(AbstractSciencePlan.STATUS.CREATED, sp.getStatus());
    }
}