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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.SupportedGADShapes;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class SupportedGADShapesImpl extends ASNBitString implements SupportedGADShapes {
	private static final int _INDEX_ELLIPSOID_POINT = 0;
    private static final int _INDEX_ELLIPSOID_POINT_WITH_UNCERTAINTY_CIRCLE = 1;
    private static final int _INDEX_ELLIPSOID_POINT_WITH_UNCERTAINTY_ELLIPSE = 2;
    private static final int _INDEX_POLYGON = 3;
    private static final int _INDEX_ELLIPSOID_POINT_WITH_ALTITUDE = 4;
    private static final int _INDEX_ELLIPSOID_WITH_ALTITUDE_AND_UNCERTAINTY_ELIPSOID = 5;
    private static final int _INDEX_ELLIPSOID_ARC = 6;

    public SupportedGADShapesImpl() {
    	super("SupportedGADShapes",6,15,false);
    }

    public SupportedGADShapesImpl(boolean ellipsoidPoint, boolean ellipsoidPointWithUncertaintyCircle,
            boolean ellipsoidPointWithUncertaintyEllipse, boolean polygon, boolean ellipsoidPointWithAltitude,
            boolean ellipsoidPointWithAltitudeAndUncertaintyElipsoid, boolean ellipsoidArc) {
    	super("SupportedGADShapes",6,15,false);
    	if (ellipsoidPoint)
            this.setBit(_INDEX_ELLIPSOID_POINT);
        if (ellipsoidPointWithUncertaintyCircle)
            this.setBit(_INDEX_ELLIPSOID_POINT_WITH_UNCERTAINTY_CIRCLE);
        if (ellipsoidPointWithUncertaintyEllipse)
            this.setBit(_INDEX_ELLIPSOID_POINT_WITH_UNCERTAINTY_ELLIPSE);
        if (polygon)
            this.setBit(_INDEX_POLYGON);
        if (ellipsoidPointWithAltitude)
            this.setBit(_INDEX_ELLIPSOID_POINT_WITH_ALTITUDE);
        if (ellipsoidPointWithAltitudeAndUncertaintyElipsoid)
            this.setBit(_INDEX_ELLIPSOID_WITH_ALTITUDE_AND_UNCERTAINTY_ELIPSOID);
        if (ellipsoidArc)
            this.setBit(_INDEX_ELLIPSOID_ARC);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.SupportedGADShapes# getEllipsoidPoint()
     */
    public boolean getEllipsoidPoint() {
        return this.isBitSet(_INDEX_ELLIPSOID_POINT);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.SupportedGADShapes# getEllipsoidPointWithUncertaintyCircle()
     */
    public boolean getEllipsoidPointWithUncertaintyCircle() {
        return this.isBitSet(_INDEX_ELLIPSOID_POINT_WITH_UNCERTAINTY_CIRCLE);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.SupportedGADShapes# getEllipsoidPointWithUncertaintyEllipse()
     */
    public boolean getEllipsoidPointWithUncertaintyEllipse() {
        return this.isBitSet(_INDEX_ELLIPSOID_POINT_WITH_UNCERTAINTY_ELLIPSE);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.SupportedGADShapes#getPolygon ()
     */
    public boolean getPolygon() {
        return this.isBitSet(_INDEX_POLYGON);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.SupportedGADShapes# getEllipsoidPointWithAltitude()
     */
    public boolean getEllipsoidPointWithAltitude() {
        return this.isBitSet(_INDEX_ELLIPSOID_POINT_WITH_ALTITUDE);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.SupportedGADShapes#
     * getEllipsoidPointWithAltitudeAndUncertaintyElipsoid()
     */
    public boolean getEllipsoidPointWithAltitudeAndUncertaintyElipsoid() {
        return this.isBitSet(_INDEX_ELLIPSOID_WITH_ALTITUDE_AND_UNCERTAINTY_ELIPSOID);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.SupportedGADShapes# getEllipsoidArc()
     */
    public boolean getEllipsoidArc() {
        return this.isBitSet(_INDEX_ELLIPSOID_ARC);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SupportedGADShapes [");

        if (this.getEllipsoidPoint()) {
            sb.append("ellipsoidPoint");
        }
        if (this.getEllipsoidPointWithUncertaintyCircle()) {
            sb.append(", ellipsoidPointWithUncertaintyCircle");
        }
        if (this.getEllipsoidPointWithUncertaintyEllipse()) {
            sb.append(", ellipsoidPointWithUncertaintyEllipse");
        }
        if (this.getPolygon()) {
            sb.append(", polygon");
        }
        if (this.getEllipsoidPointWithAltitude()) {
            sb.append(", ellipsoidPointWithAltitude");
        }
        if (this.getEllipsoidPointWithAltitudeAndUncertaintyElipsoid()) {
            sb.append(", ellipsoidPointWithAltitudeAndUncertaintyElipsoid");
        }
        if (this.getEllipsoidArc()) {
            sb.append(", ellipsoidArc");
        }

        sb.append("]");

        return sb.toString();
    }
}
