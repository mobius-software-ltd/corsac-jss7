/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 * @author amit bhayani
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
        super(4);
    }

    public SupportedLCSCapabilitySetsImpl(boolean lcsCapabilitySetRelease98_99, boolean lcsCapabilitySetRelease4,
            boolean lcsCapabilitySetRelease5, boolean lcsCapabilitySetRelease6, boolean lcsCapabilitySetRelease7) {
    	super(4);
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
