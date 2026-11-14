package edu.gemini;

import edu.gemini.model.*;
import edu.gemini.model.AbstractSciencePlan.STATUS;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class OCS implements GeminiAPI<SciencePlan, ObservingProgram, ObservingProgramConfigs,
        AstronomicalData, Astronomer, ScienceObserver> {
	
	//CreateSciencePlan
	private ArrayList<SciencePlan> sciencePlans = new ArrayList<>();
	private int nextPlanNo = 1;

	//CreateObservingProgram
	private ArrayList<ObservingProgram> observingPrograms = new ArrayList<>();

	
/////////////////////////////////////////////
    @Override
    public ArrayList<SciencePlan> getAllSciencePlans() {
    	return sciencePlans;
    }

    @Override
    public SciencePlan getSciencePlanByNo(int planNo) {
        for (SciencePlan plan : sciencePlans) {
            if (plan.getPlanNo() == planNo) {
                return plan;
            }
        }
        return null;
    }

    @Override
    public String createSciencePlan(SciencePlan sciencePlan, Astronomer an) {
        // Basic required field validation
        if (sciencePlan.getName() == null || sciencePlan.getName().isEmpty()) {
            return "Error: Plan name is required.";
        }

        if (sciencePlan.getFunding() <= 0) {
            return "Error: Funding must be a positive number.";
        }

        if (sciencePlan.getStartDate() != null && sciencePlan.getEndDate() != null &&
            sciencePlan.getStartDate().after(sciencePlan.getEndDate())) {
            return "Error: Start date cannot be after end date.";
        }

        // Check for duplicate name
        for (SciencePlan plan : sciencePlans) {
            if (plan.getName().equals(sciencePlan.getName())) {
                return "Error: A science plan with this name already exists.";
            }
        }

        // Assign auto-generated fields
        sciencePlan.setPlanNo(nextPlanNo++);
        sciencePlan.setCreator(an.getName());
        sciencePlan.setStatus(AbstractSciencePlan.STATUS.CREATED);

        // Add to in-memory list
        sciencePlans.add(sciencePlan);

        return "Science Plan created successfully with Plan No: " + sciencePlan.getPlanNo();
    }
    
    
    /////////////////////////////////////////////////////////////////////////////
    
    private void updatePlanInList(SciencePlan sciencePlan) {
        for (int i = 0; i < sciencePlans.size(); i++) {
            if (sciencePlans.get(i).getPlanNo() == sciencePlan.getPlanNo()) {
                sciencePlans.set(i, sciencePlan);
                return;
            }
        }
    }
    public ArrayList<SciencePlan> filterSciencePlansByStatus(AbstractSciencePlan.STATUS status) {
        ArrayList<SciencePlan> filteredPlans = new ArrayList<>();
        for (SciencePlan plan : sciencePlans) {
            if (plan.getStatus() == status) {
                filteredPlans.add(plan);
            }
        }
        return filteredPlans;
    }

//    @Override
//    public String submitSciencePlan(SciencePlan sciencePlan, Astronomer an) {
//        return null;
//    }
    @Override
    public String submitSciencePlan(SciencePlan sciencePlan, Astronomer an) {
        if (sciencePlan == null) {
            return "Error: No science plan selected.";
        }

        AbstractSciencePlan.STATUS status = sciencePlan.getStatus();

        if (status == AbstractSciencePlan.STATUS.SUBMITTED ||
            status == AbstractSciencePlan.STATUS.VALIDATED ||
            status == AbstractSciencePlan.STATUS.RUNNING ||
            status == AbstractSciencePlan.STATUS.COMPLETE) {
            return "This science plan has already been submitted or executed.";
        }

        if (status != AbstractSciencePlan.STATUS.TESTED) {
            return "This science plan is incomplete. Please complete and test it before submission.";
        }

        // If everything is okay, submit the plan
        sciencePlan.setStatus(AbstractSciencePlan.STATUS.SUBMITTED);
        updatePlanInList(sciencePlan);
        return "Science plan " + sciencePlan.getPlanNo() + " submitted successfully.";
    }
    
//
//    @Override
//    public boolean updateSciencePlanStatus(int planno, AbstractSciencePlan.STATUS stssp) {
//        return false;
//    }
    @Override
    public boolean updateSciencePlanStatus(int planno, AbstractSciencePlan.STATUS stssp) {
        for (SciencePlan plan : sciencePlans) {
            if (plan.getPlanNo() == planno) {
                plan.setStatus(stssp);
                updatePlanInList(plan);
                return true;
            }
        }
        return false;
    }
//
//    @Override
//    public String testSciencePlan(SciencePlan sciencePlan) {
//        return null;
//    }
    @Override
    public String testSciencePlan(SciencePlan sciencePlan) {
        if (sciencePlan.getStatus() == AbstractSciencePlan.STATUS.CREATED) {
            sciencePlan.setStatus(AbstractSciencePlan.STATUS.TESTED);

            // Optional: update it in the list
            // updatePlanInList(sciencePlan);

            return "Science plan tested successfully.";
        } else {
            return "Science plan is not in CREATED status, cannot test.";
        }
    }

    @Override
    public void deleteAllSciencePlans() {
    	sciencePlans.clear();
    }

    @Override
    public boolean deleteSciencePlanByNo(int planNo) {
    	return sciencePlans.removeIf(plan -> plan.getPlanNo() == planNo);
    }
    /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String addUnavailableDate(Date datevalue) {
        return null;
    }

    @Override
    public String deleteUnavailableDate(Date datevalue) {
        return null;
    }

    @Override
    public ArrayList<Date> getAllObservationSchedule() {
        return null;
    }

    @Override
    public AstronomicalData getAstronomicalData(SciencePlan sciencePlan) throws IOException {
        return null;
    }

    @Override
    public AstronomicalData removeAstronomicalData(SciencePlan sciencePlan, int index) throws IOException {
        return null;
    }

    @Override
    public String accessTelescopeLiveView() throws IOException {
        return null;
    }

    @Override
    public String executeCommand(String command) {
        return null;
    }

    @Override
    public String getConfigurations() {
        return null;
    }

    @Override
    public boolean addConfiguration(String confFilePath) {
        return false;
    }

    @Override
    public boolean removeConfiguration(int confNo) {
        return false;
    }

    @Override
    public AbstractObservingProgramConfigs.FoldMirrorType[] getFoldMirrorTypes() {
        return new AbstractObservingProgramConfigs.FoldMirrorType[0];
    }

    @Override
    public AbstractObservingProgramConfigs.CalibrationUnit[] getCalibrationUnits() {
        return new AbstractObservingProgramConfigs.CalibrationUnit[0];
    }

    @Override
    public AbstractObservingProgramConfigs.LightType[] getLightTypes() {
        return new AbstractObservingProgramConfigs.LightType[0];
    }

    
    
 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    @Override
//    public ObservingProgram createObservingProgram(AbstractSciencePlan sp, String opticsPrimary, double fStop, double opticsSecondaryRMS, double scienceFoldMirrorDegree, AbstractObservingProgramConfigs.FoldMirrorType scienceFoldMirrorType, int moduleContent, AbstractObservingProgramConfigs.CalibrationUnit calibrationUnit, AbstractObservingProgramConfigs.LightType lightType, AbstractTelePositionPair[] telePositionPair, ScienceObserver so) {
//        return null;
//    }
    @Override
    public ObservingProgram createObservingProgram(
        AbstractSciencePlan sp,
        String opticsPrimary,
        double fStop,
        double opticsSecondaryRMS,
        double scienceFoldMirrorDegree,
        AbstractObservingProgramConfigs.FoldMirrorType scienceFoldMirrorType,
        int moduleContent,
        AbstractObservingProgramConfigs.CalibrationUnit calibrationUnit,
        AbstractObservingProgramConfigs.LightType lightType,
        AbstractTelePositionPair[] telePositionPair,
        ScienceObserver so
    ) {
        // Step 0: Only a VALIDATED science plan can be turned into an observing program
        if (!(sp instanceof SciencePlan) || so == null
                || ((SciencePlan) sp).getStatus() != STATUS.VALIDATED) {
            return null;
        }

        // Step 1: Create and populate new ObservingProgram
        ObservingProgram op = new ObservingProgram();
        op.setSciencePlan(sp);
        op.setOpticsPrimary(opticsPrimary);
        op.setfStop(fStop);
        op.setOpticsSecondaryRMS(opticsSecondaryRMS);
        op.setScienceFoldMirrorDegree(scienceFoldMirrorDegree);
        op.setScienceFoldMirrorType(scienceFoldMirrorType);
        op.setModuleContent(moduleContent);
        op.setCalibrationUnit(calibrationUnit);
        op.setLightType(lightType);
        op.setTelePositionPairs(telePositionPair);
        op.setObserverId(so.getId()); // or whatever identifies ScienceObserver
        op.setStatus(STATUS.CREATED); // Example status enum

        // Step 2: Save to the in-memory list (like sciencePlans)
        observingPrograms.add(op);

        // Step 3: Return created OP
        return op;
    }


    @Override
    public SciencePlan validateSciencePlan(AbstractSciencePlan sp, ScienceObserver so) {
        // A science observer can only validate a plan that has been SUBMITTED
        if (!(sp instanceof SciencePlan) || so == null) {
            return null;
        }
        SciencePlan plan = (SciencePlan) sp;
        if (plan.getStatus() != STATUS.SUBMITTED) {
            return null;
        }

        // Required plan details must be present, otherwise the plan is rejected
        boolean complete = plan.getPlanNo() > 0
                && !isBlank(plan.getName())
                && !isBlank(plan.getCreator())
                && plan.getFunding() > 0
                && !isBlank(plan.getObjective());

        plan.setStatus(complete ? STATUS.VALIDATED : STATUS.INVALIDATED);
        updatePlanInList(plan);
        return plan;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Override
    public boolean saveObservingProgram(AbstractObservingProgram op) {
        return false;
    }

    @Override
    public ObservingProgram getObservingProgramBySciencePlan(AbstractSciencePlan sp) {
        for (ObservingProgram op : observingPrograms) {
            if (op.getSciencePlan() == sp) {
                return op;
            }
        }
        return null;
    }

    @Override
    public void getDefaultConfiguration() throws IOException {

    }

    @Override
    public void getCurrentConfiguration() throws IOException {

    }

    @Override
    public String updateConfiguration() throws FileNotFoundException {
        return null;
    }
}
