package edu.gemini;

import edu.gemini.model.AbstractTelePositionPair;

public class TelePositionPair extends AbstractTelePositionPair {

    public TelePositionPair() {
        super(0.0, 0.0); // Default to 0 values
    }

    public TelePositionPair(double direction, double degree) {
        super(direction, degree);
    }
}