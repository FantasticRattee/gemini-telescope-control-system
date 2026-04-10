package edu.gemini;

import edu.gemini.model.AbstractScienceObserver;

public class ScienceObserver extends AbstractScienceObserver {


    private int id;  // Re-declared because 'id' in superclass is private

    public ScienceObserver(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    

    @Override
    public String toString() {
        return "ScienceObserver{id=" + getId() + "}";
    }
}
