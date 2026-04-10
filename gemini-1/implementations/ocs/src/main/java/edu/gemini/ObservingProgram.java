package edu.gemini;

import edu.gemini.model.AbstractObservingProgram;
import edu.gemini.model.AbstractObservingProgramConfigs;
import edu.gemini.model.AbstractSciencePlan;
import edu.gemini.model.AbstractSciencePlan.STATUS;
import edu.gemini.model.AbstractTelePositionPair;

public class ObservingProgram extends AbstractObservingProgram {



    
	private AbstractSciencePlan sciencePlan;
    private String opticsPrimary;
    private double fStop;
    private double opticsSecondaryRMS;
    private double scienceFoldMirrorDegree;
    private AbstractObservingProgramConfigs.FoldMirrorType scienceFoldMirrorType;
    private int moduleContent;
    private AbstractObservingProgramConfigs.CalibrationUnit calibrationUnit;
    private AbstractObservingProgramConfigs.LightType lightType;
    private AbstractTelePositionPair[] telePositionPairs;
    private int observerId; // science observer who created this program

    private STATUS status;

    // Required setters

    public void setSciencePlan(AbstractSciencePlan sp) {
        this.sciencePlan = sp;
    }

    public void setOpticsPrimary(String opticsPrimary) {
        this.opticsPrimary = opticsPrimary;
    }

    public void setfStop(double fStop) {
        this.fStop = fStop;
    }

    public void setOpticsSecondaryRMS(double opticsSecondaryRMS) {
        this.opticsSecondaryRMS = opticsSecondaryRMS;
    }

    public void setScienceFoldMirrorDegree(double degree) {
        this.scienceFoldMirrorDegree = degree;
    }

    public void setScienceFoldMirrorType(AbstractObservingProgramConfigs.FoldMirrorType type) {
        this.scienceFoldMirrorType = type;
    }

    public void setModuleContent(int moduleContent) {
        this.moduleContent = moduleContent;
    }

    public void setCalibrationUnit(AbstractObservingProgramConfigs.CalibrationUnit unit) {
        this.calibrationUnit = unit;
    }

    public void setLightType(AbstractObservingProgramConfigs.LightType type) {
        this.lightType = type;
    }

    public void setTelePositionPairs(AbstractTelePositionPair[] pairs) {
        this.telePositionPairs = pairs;
    }

    public void setObserverId(int id) {
        this.observerId = id;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    // Optional getters (in case you need them later)

    public AbstractSciencePlan getSciencePlan() {
        return sciencePlan;
    }

    public String getOpticsPrimary() {
        return opticsPrimary;
    }

    public double getfStop() {
        return fStop;
    }

    public double getOpticsSecondaryRMS() {
        return opticsSecondaryRMS;
    }

    public double getScienceFoldMirrorDegree() {
        return scienceFoldMirrorDegree;
    }

    public AbstractObservingProgramConfigs.FoldMirrorType getScienceFoldMirrorType() {
        return scienceFoldMirrorType;
    }

    public int getModuleContent() {
        return moduleContent;
    }

    public AbstractObservingProgramConfigs.CalibrationUnit getCalibrationUnit() {
        return calibrationUnit;
    }

    public AbstractObservingProgramConfigs.LightType getLightType() {
        return lightType;
    }

    public AbstractTelePositionPair[] getTelePositionPairs() {
        return telePositionPairs;
    }

    public int getObserverId() {
        return observerId;
    }

    public STATUS getStatus() {
        return status;
    }
    
    @Override
    public String toString() {
        return "ObservingProgram{" +
               "opticsPrimary='" + getOpticsPrimary() + '\'' +
               ", fStop=" + getfStop() +
               ", opticsSecondaryRMS=" + getOpticsSecondaryRMS() +
               ", foldMirrorDegree=" + getScienceFoldMirrorDegree() +
               ", foldMirrorType=" + getScienceFoldMirrorType() +
               ", moduleContent=" + getModuleContent() +
               ", calibrationUnit=" + getCalibrationUnit() +
               ", lightType=" + getLightType() +
               ", observerId=" + getObserverId() +
               '}';
    }
    
}