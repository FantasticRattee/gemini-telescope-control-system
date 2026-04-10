package edu.gemini;

import java.util.Calendar;
import java.util.Date;

import edu.gemini.model.AbstractTelePositionPair;
import edu.gemini.model.AbstractObservingProgramConfigs;


public class Main {
    public static void main(String[] args) {
        OCS ocs = new OCS();

        // Create Astronomer
        Astronomer as = new Astronomer();
        //as.setId(1); ////Auto-Gen
        as.setInstitution("Mahidol University");
        as.setFirstName("Rattee");
        as.setLastName("Watperatam");

        // Create a valid Science Plan
        SciencePlan sp = new SciencePlan();
        sp.setName("Galaxy Observation Plan");
        sp.setFunding(999999.8888);
        sp.setObjective("Observe galaxy clusters for dark matter distribution analysis.");

        // Set start and end dates using java.util.Date
        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.FEBRUARY, 1);
        Date startDate = cal.getTime();
        cal.set(2025, Calendar.FEBRUARY, 5);
        Date endDate = cal.getTime();

        sp.setStartDate(startDate);
        sp.setEndDate(endDate);
        sp.setTelescope("CHILE");
        sp.setTarget("Gemini");
        
        sp.setFileType(SciencePlan.FileType.RAW);
        sp.setFileQuality(SciencePlan.FileQuality.FINE);
        sp.setColorType(SciencePlan.ColorType.BW_MODE);
        
        sp.setContrast(1.2);
        sp.setExposure(0.8);
        sp.setHighlights(0.1);
        sp.setShadows(0.2);
        sp.setBlacks(0.5);
        sp.setWhites(0.1);

        
        // === Create the Science Plan ===
        String result = ocs.createSciencePlan(sp, as);
        System.out.println("=== Create Science Plan Output ===");
        System.out.println("Result: " + result);

        // === Test the Science Plan ===
        String testResult = ocs.testSciencePlan(sp);
        System.out.println("Test Result: " + testResult);

        // === Submit the Science Plan ===
        String submitResult = ocs.submitSciencePlan(sp, as);
        System.out.println("Submit Result: " + submitResult);

        // === Create TelePositionPair ===
        AbstractTelePositionPair[] pairs = new AbstractTelePositionPair[1];
        pairs[0] = new TelePositionPair(90.0, 45.0); // sample azimuth/elevation

        // === Create Observing Program ===
        ScienceObserver so = new ScienceObserver(1);
        
        ObservingProgram op = ocs.createObservingProgram(
                sp,
                "GNZ",
                4.0,
                5.5,
                35.0,
                AbstractObservingProgramConfigs.FoldMirrorType.CASSEGRAIN,
                2,
                AbstractObservingProgramConfigs.CalibrationUnit.ARGON,
                AbstractObservingProgramConfigs.LightType.MAUNA_KEA_SKY_EMISSION,
                pairs,
                so
        );
        
        

        // Print output
        System.out.println("=== Create Science Plan Output ===");
        System.out.println("Result: " + result);
        System.out.println("Created By: " + sp.getCreator());
        System.out.println("Plan Status: " + sp.getStatus());
        System.out.println("Plan Name: " + sp.getName());
        System.out.println("Details: " + sp);
        System.out.println("File Type: " + sp.getFileType());
        System.out.println("File Quality: " + sp.getFileQuality());
        System.out.println("Color Type: " + sp.getColorType());
        System.out.println("Contrast: " + sp.getContrast());
        System.out.println("Exposure: " + sp.getExposure());
        System.out.println("Highlights: " + sp.getHighlights());
        System.out.println("Shadows: " + sp.getShadows());
        System.out.println("Blacks: " + sp.getBlacks());
        System.out.println("Whites: " + sp.getWhites());
        
        System.out.println("Science Observer: " + so);
        System.out.println("Observing Program: " + op);
        

    }
}