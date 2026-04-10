package edu.gemini.model;

public abstract class AbstractObservingProgramConfigs {

    public enum FoldMirrorType {
        REFLECTIVE_CONVERGING_BEAM,
        CASSEGRAIN
    }

    public enum CalibrationUnit {
        ARGON, XENON, THAR, CUAR
    }

    public enum LightType {
        MAUNA_KEA_SKY_EMISSION,
        CERRO_PACHON_SKY_EMISSION
    }

    public static FoldMirrorType[] getFoldMirrorType() {
        return FoldMirrorType.values();
    }

    public static CalibrationUnit[] getCalibrationUnit() {
        return CalibrationUnit.values();
    }

    public static LightType[] getLightType() {
        return LightType.values();
    }
}