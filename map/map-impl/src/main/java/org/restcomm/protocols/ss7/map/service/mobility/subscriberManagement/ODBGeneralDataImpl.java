/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ODBGeneralDataImpl extends ASNBitString implements ODBGeneralData {
	private static final int _INDEX_allOGCallsBarred = 0;
    private static final int _INDEX_internationalOGCallsBarred = 1;
    private static final int _INDEX_internationalOGCallsNotToHPLMNCountryBarred = 2;
    private static final int _INDEX_interzonalOGCallsBarred = 6;
    private static final int _INDEX_interzonalOGCallsNotToHPLMNCountryBarred = 7;
    private static final int _INDEX_interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred = 8;
    private static final int _INDEX_premiumRateInformationOGCallsBarred = 3;
    private static final int _INDEX_premiumRateEntertainementOGCallsBarred = 4;
    private static final int _INDEX_ssAccessBarred = 5;
    private static final int _INDEX_allECTBarred = 9;
    private static final int _INDEX_chargeableECTBarred = 10;
    private static final int _INDEX_internationalECTBarred = 11;
    private static final int _INDEX_interzonalECTBarred = 12;
    private static final int _INDEX_doublyChargeableECTBarred = 13;
    private static final int _INDEX_multipleECTBarred = 14;
    private static final int _INDEX_allPacketOrientedServicesBarred = 15;
    private static final int _INDEX_roamerAccessToHPLMNAPBarred = 16;
    private static final int _INDEX_roamerAccessToVPLMNAPBarred = 17;
    private static final int _INDEX_roamingOutsidePLMNOGCallsBarred = 18;
    private static final int _INDEX_allICCallsBarred = 19;
    private static final int _INDEX_roamingOutsidePLMNICCallsBarred = 20;
    private static final int _INDEX_roamingOutsidePLMNICountryICCallsBarred = 21;
    private static final int _INDEX_roamingOutsidePLMNBarred = 22;
    private static final int _INDEX_roamingOutsidePLMNCountryBarred = 23;
    private static final int _INDEX_registrationAllCFBarred = 24;
    private static final int _INDEX_registrationCFNotToHPLMNBarred = 25;
    private static final int _INDEX_registrationInterzonalCFBarred = 26;
    private static final int _INDEX_registrationInterzonalCFNotToHPLMNBarred = 27;
    private static final int _INDEX_registrationInternationalCFBarred = 28;

    public ODBGeneralDataImpl() {
    	super("ODBGeneralData",15,31,false);
    }

    public ODBGeneralDataImpl(boolean allOGCallsBarred, boolean internationalOGCallsBarred,
            boolean internationalOGCallsNotToHPLMNCountryBarred, boolean interzonalOGCallsBarred,
            boolean interzonalOGCallsNotToHPLMNCountryBarred,
            boolean interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred,
            boolean premiumRateInformationOGCallsBarred, boolean premiumRateEntertainementOGCallsBarred,
            boolean ssAccessBarred, boolean allECTBarred, boolean chargeableECTBarred, boolean internationalECTBarred,
            boolean interzonalECTBarred, boolean doublyChargeableECTBarred, boolean multipleECTBarred,
            boolean allPacketOrientedServicesBarred, boolean roamerAccessToHPLMNAPBarred, boolean roamerAccessToVPLMNAPBarred,
            boolean roamingOutsidePLMNOGCallsBarred, boolean allICCallsBarred, boolean roamingOutsidePLMNICCallsBarred,
            boolean roamingOutsidePLMNICountryICCallsBarred, boolean roamingOutsidePLMNBarred,
            boolean roamingOutsidePLMNCountryBarred, boolean registrationAllCFBarred, boolean registrationCFNotToHPLMNBarred,
            boolean registrationInterzonalCFBarred, boolean registrationInterzonalCFNotToHPLMNBarred,
            boolean registrationInternationalCFBarred) {
    	super("ODBGeneralData",15,31,false);
        if (allOGCallsBarred)
            this.setBit(_INDEX_allOGCallsBarred);
        if (internationalOGCallsBarred)
            this.setBit(_INDEX_internationalOGCallsBarred);
        if (internationalOGCallsNotToHPLMNCountryBarred)
            this.setBit(_INDEX_internationalOGCallsNotToHPLMNCountryBarred);
        if (interzonalOGCallsBarred)
            this.setBit(_INDEX_interzonalOGCallsBarred);
        if (interzonalOGCallsNotToHPLMNCountryBarred)
            this.setBit(_INDEX_interzonalOGCallsNotToHPLMNCountryBarred);
        if (interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred)
            this.setBit(_INDEX_interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred);
        if (premiumRateInformationOGCallsBarred)
            this.setBit(_INDEX_premiumRateInformationOGCallsBarred);
        if (premiumRateEntertainementOGCallsBarred)
            this.setBit(_INDEX_premiumRateEntertainementOGCallsBarred);
        if (ssAccessBarred)
            this.setBit(_INDEX_ssAccessBarred);
        if (allECTBarred)
            this.setBit(_INDEX_allECTBarred);
        if (chargeableECTBarred)
            this.setBit(_INDEX_chargeableECTBarred);
        if (internationalECTBarred)
            this.setBit(_INDEX_internationalECTBarred);
        if (interzonalECTBarred)
            this.setBit(_INDEX_interzonalECTBarred);
        if (doublyChargeableECTBarred)
            this.setBit(_INDEX_doublyChargeableECTBarred);
        if (multipleECTBarred)
            this.setBit(_INDEX_multipleECTBarred);
        if (allPacketOrientedServicesBarred)
            this.setBit(_INDEX_allPacketOrientedServicesBarred);
        if (roamerAccessToHPLMNAPBarred)
            this.setBit(_INDEX_roamerAccessToHPLMNAPBarred);
        if (roamerAccessToVPLMNAPBarred)
            this.setBit(_INDEX_roamerAccessToVPLMNAPBarred);
        if (roamingOutsidePLMNOGCallsBarred)
            this.setBit(_INDEX_roamingOutsidePLMNOGCallsBarred);
        if (allICCallsBarred)
            this.setBit(_INDEX_allICCallsBarred);
        if (roamingOutsidePLMNICCallsBarred)
            this.setBit(_INDEX_roamingOutsidePLMNICCallsBarred);
        if (roamingOutsidePLMNICountryICCallsBarred)
            this.setBit(_INDEX_roamingOutsidePLMNICountryICCallsBarred);
        if (roamingOutsidePLMNBarred)
            this.setBit(_INDEX_roamingOutsidePLMNBarred);
        if (roamingOutsidePLMNCountryBarred)
            this.setBit(_INDEX_roamingOutsidePLMNCountryBarred);
        if (registrationAllCFBarred)
            this.setBit(_INDEX_registrationAllCFBarred);
        if (registrationCFNotToHPLMNBarred)
            this.setBit(_INDEX_registrationCFNotToHPLMNBarred);
        if (registrationInterzonalCFBarred)
            this.setBit(_INDEX_registrationInterzonalCFBarred);
        if (registrationInterzonalCFNotToHPLMNBarred)
            this.setBit(_INDEX_registrationInterzonalCFNotToHPLMNBarred);
        if (registrationInternationalCFBarred)
            this.setBit(_INDEX_registrationInternationalCFBarred);
    }

    public boolean getAllOGCallsBarred() {
        return this.isBitSet(_INDEX_allOGCallsBarred);
    }

    public boolean getInternationalOGCallsBarred() {
        return this.isBitSet(_INDEX_internationalOGCallsBarred);
    }

    public boolean getInternationalOGCallsNotToHPLMNCountryBarred() {
        return this.isBitSet(_INDEX_internationalOGCallsNotToHPLMNCountryBarred);
    }

    public boolean getInterzonalOGCallsBarred() {
        return this.isBitSet(_INDEX_interzonalOGCallsBarred);
    }

    public boolean getInterzonalOGCallsNotToHPLMNCountryBarred() {
        return this.isBitSet(_INDEX_interzonalOGCallsNotToHPLMNCountryBarred);
    }

    public boolean getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred() {
        return this.isBitSet(_INDEX_interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred);
    }

    public boolean getPremiumRateInformationOGCallsBarred() {
        return this.isBitSet(_INDEX_premiumRateInformationOGCallsBarred);
    }

    public boolean getPremiumRateEntertainementOGCallsBarred() {
        return this.isBitSet(_INDEX_premiumRateEntertainementOGCallsBarred);
    }

    public boolean getSsAccessBarred() {
        return this.isBitSet(_INDEX_ssAccessBarred);
    }

    public boolean getAllECTBarred() {
        return this.isBitSet(_INDEX_allECTBarred);
    }

    public boolean getChargeableECTBarred() {
        return this.isBitSet(_INDEX_chargeableECTBarred);
    }

    public boolean getInternationalECTBarred() {
        return this.isBitSet(_INDEX_internationalECTBarred);
    }

    public boolean getInterzonalECTBarred() {
        return this.isBitSet(_INDEX_interzonalECTBarred);
    }

    public boolean getDoublyChargeableECTBarred() {
        return this.isBitSet(_INDEX_doublyChargeableECTBarred);
    }

    public boolean getMultipleECTBarred() {
        return this.isBitSet(_INDEX_multipleECTBarred);
    }

    public boolean getAllPacketOrientedServicesBarred() {
        return this.isBitSet(_INDEX_allPacketOrientedServicesBarred);
    }

    public boolean getRoamerAccessToHPLMNAPBarred() {
        return this.isBitSet(_INDEX_roamerAccessToHPLMNAPBarred);
    }

    public boolean getRoamerAccessToVPLMNAPBarred() {
        return this.isBitSet(_INDEX_roamerAccessToVPLMNAPBarred);
    }

    public boolean getRoamingOutsidePLMNOGCallsBarred() {
        return this.isBitSet(_INDEX_roamingOutsidePLMNOGCallsBarred);
    }

    public boolean getAllICCallsBarred() {
        return this.isBitSet(_INDEX_allICCallsBarred);
    }

    public boolean getRoamingOutsidePLMNICCallsBarred() {
        return this.isBitSet(_INDEX_roamingOutsidePLMNICCallsBarred);
    }

    public boolean getRoamingOutsidePLMNICountryICCallsBarred() {
        return this.isBitSet(_INDEX_roamingOutsidePLMNICountryICCallsBarred);
    }

    public boolean getRoamingOutsidePLMNBarred() {
        return this.isBitSet(_INDEX_roamingOutsidePLMNBarred);
    }

    public boolean getRoamingOutsidePLMNCountryBarred() {
        return this.isBitSet(_INDEX_roamingOutsidePLMNCountryBarred);
    }

    public boolean getRegistrationAllCFBarred() {
        return this.isBitSet(_INDEX_registrationAllCFBarred);
    }

    public boolean getRegistrationCFNotToHPLMNBarred() {
        return this.isBitSet(_INDEX_registrationCFNotToHPLMNBarred);
    }

    public boolean getRegistrationInterzonalCFBarred() {
        return this.isBitSet(_INDEX_registrationInterzonalCFBarred);
    }

    public boolean getRegistrationInterzonalCFNotToHPLMNBarred() {
        return this.isBitSet(_INDEX_registrationInterzonalCFNotToHPLMNBarred);
    }

    public boolean getRegistrationInternationalCFBarred() {
        return this.isBitSet(_INDEX_registrationInternationalCFBarred);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ODBGeneralData [");
        if (getAllOGCallsBarred())
            sb.append("allOGCallsBarred, ");
        if (getInternationalOGCallsBarred())
            sb.append("internationalOGCallsBarred, ");
        if (getInternationalOGCallsNotToHPLMNCountryBarred())
            sb.append("internationalOGCallsNotToHPLMNCountryBarred, ");
        if (getInterzonalOGCallsBarred())
            sb.append("interzonalOGCallsBarred, ");
        if (getInterzonalOGCallsNotToHPLMNCountryBarred())
            sb.append("interzonalOGCallsNotToHPLMNCountryBarred, ");
        if (getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred())
            sb.append("interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred, ");
        if (getPremiumRateInformationOGCallsBarred())
            sb.append("premiumRateInformationOGCallsBarred, ");
        if (getPremiumRateEntertainementOGCallsBarred())
            sb.append("premiumRateEntertainementOGCallsBarred, ");
        if (getSsAccessBarred())
            sb.append("ssAccessBarred, ");
        if (getAllECTBarred())
            sb.append("allECTBarred, ");
        if (getChargeableECTBarred())
            sb.append("chargeableECTBarred, ");
        if (getInternationalECTBarred())
            sb.append("internationalECTBarred, ");
        if (getInterzonalECTBarred())
            sb.append("interzonalECTBarred, ");
        if (getDoublyChargeableECTBarred())
            sb.append("doublyChargeableECTBarred, ");
        if (getMultipleECTBarred())
            sb.append("multipleECTBarred, ");
        if (getAllPacketOrientedServicesBarred())
            sb.append("allPacketOrientedServicesBarred, ");
        if (getRoamerAccessToHPLMNAPBarred())
            sb.append("roamerAccessToHPLMNAPBarred, ");
        if (getRoamerAccessToVPLMNAPBarred())
            sb.append("roamerAccessToVPLMNAPBarred, ");
        if (getRoamingOutsidePLMNOGCallsBarred())
            sb.append("roamingOutsidePLMNOGCallsBarred");
        if (getAllICCallsBarred())
            sb.append("allICCallsBarred, ");
        if (getRoamingOutsidePLMNICCallsBarred())
            sb.append("roamingOutsidePLMNICCallsBarred, ");
        if (getRoamingOutsidePLMNICountryICCallsBarred())
            sb.append("roamingOutsidePLMNICountryICCallsBarred, ");
        if (getRoamingOutsidePLMNBarred())
            sb.append("roamingOutsidePLMNBarred, ");
        if (getRoamingOutsidePLMNCountryBarred())
            sb.append("roamingOutsidePLMNCountryBarred, ");
        if (getRegistrationAllCFBarred())
            sb.append("registrationAllCFBarred, ");
        if (getRegistrationCFNotToHPLMNBarred())
            sb.append("registrationCFNotToHPLMNBarred, ");
        if (getRegistrationInterzonalCFBarred())
            sb.append("registrationInterzonalCFBarred, ");
        if (getRegistrationInterzonalCFNotToHPLMNBarred())
            sb.append("registrationInterzonalCFNotToHPLMNBarred, ");
        if (getRegistrationInternationalCFBarred())
            sb.append("registrationInternationalCFBarred, ");
        sb.append("]");

        return sb.toString();
    }

}
