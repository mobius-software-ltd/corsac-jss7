package org.restcomm.protocols.ss7.map;

/**
 * read/write MAP layer configuration *.xml file
 */
public class MAPStackConfigurationManagement {
    private int shortTimer = 10000;
    private int mediumTimer = 30000;
    private int longTimer = 600000;

    public MAPStackConfigurationManagement() {
    }

    public int getShortTimer() {
        return shortTimer;
    }

    public int getMediumTimer() {
        return mediumTimer;
    }

    public int getLongTimer() {
        return longTimer;
    }

    public void setShortTimer(int shortTimer) {
        this.shortTimer = shortTimer;
    }

    public void setMediumTimer(int mediumTimer) {
        this.mediumTimer = mediumTimer;
    }

    public void setLongTimer(int longTimer) {
        this.longTimer = longTimer;
    }
}