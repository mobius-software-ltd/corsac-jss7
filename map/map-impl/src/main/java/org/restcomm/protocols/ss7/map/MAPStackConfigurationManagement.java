package org.restcomm.protocols.ss7.map;
/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
/**
 * read/write MAP layer configuration *.xml file
 * @author yulianoifa
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