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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class SupportedLCSCapabilitySetsImpl extends ASNBitString implements SupportedLCSCapabilitySets {
	private static final int _INDEX_LCS_CAPABILITY_SET1 = 0;
    private static final int _INDEX_LCS_CAPABILITY_SET2 = 1;
    private static final int _INDEX_LCS_CAPABILITY_SET3 = 2;
    private static final int _INDEX_LCS_CAPABILITY_SET4 = 3;
    private static final int _INDEX_LCS_CAPABILITY_SET5 = 4;

    /**
     *
     */
    public SupportedLCSCapabilitySetsImpl() {
        super("SupportedLCSCapabilitySets",1,15,false);
    }

    public SupportedLCSCapabilitySetsImpl(boolean lcsCapabilitySetRelease98_99, boolean lcsCapabilitySetRelease4,
            boolean lcsCapabilitySetRelease5, boolean lcsCapabilitySetRelease6, boolean lcsCapabilitySetRelease7) {
    	super("SupportedLCSCapabilitySets",1,15,false);
        if (lcsCapabilitySetRelease98_99)
            this.setBit(_INDEX_LCS_CAPABILITY_SET1);
        if (lcsCapabilitySetRelease4)
            this.setBit(_INDEX_LCS_CAPABILITY_SET2);
        if (lcsCapabilitySetRelease5)
            this.setBit(_INDEX_LCS_CAPABILITY_SET3);
        if (lcsCapabilitySetRelease6)
            this.setBit(_INDEX_LCS_CAPABILITY_SET4);
        if (lcsCapabilitySetRelease7)
            this.setBit(_INDEX_LCS_CAPABILITY_SET5);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.SupportedLCSCapabilitySets#getLcsCapabilitySet1()
     */
    public boolean getCapabilitySetRelease98_99() {
        return this.isBitSet(_INDEX_LCS_CAPABILITY_SET1);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.SupportedLCSCapabilitySets#getLcsCapabilitySet2()
     */
    public boolean getCapabilitySetRelease4() {
        return this.isBitSet(_INDEX_LCS_CAPABILITY_SET2);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.SupportedLCSCapabilitySets#getLcsCapabilitySet3()
     */
    public boolean getCapabilitySetRelease5() {
        return this.isBitSet(_INDEX_LCS_CAPABILITY_SET3);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.SupportedLCSCapabilitySets#getLcsCapabilitySet4()
     */
    public boolean getCapabilitySetRelease6() {
        return this.isBitSet(_INDEX_LCS_CAPABILITY_SET4);
    }

    public boolean getCapabilitySetRelease7() {
        return this.isBitSet(_INDEX_LCS_CAPABILITY_SET5);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SupportedLCSCapabilitySets [");

        if (getCapabilitySetRelease98_99())
            sb.append("CapabilitySetRelease98_99, ");
        if (getCapabilitySetRelease4())
            sb.append("CapabilitySetRelease4, ");
        if (getCapabilitySetRelease5())
            sb.append("CapabilitySetRelease5, ");
        if (getCapabilitySetRelease6())
            sb.append("CapabilitySetRelease6, ");
        if (getCapabilitySetRelease7())
            sb.append("CapabilitySetRelease7, ");

        sb.append("]");

        return sb.toString();
    }
}
