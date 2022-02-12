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
package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeatures;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class SupportedFeaturesImpl extends ASNBitString implements SupportedFeatures {
	private static final int _INDEX_odbAllApn = 0;
    private static final int _INDEX_odbHPLMNApn = 1;
    private static final int _INDEX_odbVPLMNApn = 2;
    private static final int _INDEX_odbAllOg = 3;
    private static final int _INDEX_odbAllInternationalOg = 4;
    private static final int _INDEX_odbAllIntOgNotToHPLMNCountry = 5;
    private static final int _INDEX_odbAllInterzonalOg = 6;
    private static final int _INDEX_odbAllInterzonalOgNotToHPLMNCountry = 7;
    private static final int _INDEX_odbAllInterzonalOgandInternatOgNotToHPLMNCountry = 8;
    private static final int _INDEX_regSub = 9;
    private static final int _INDEX_trace = 10;
    private static final int _INDEX_lcsAllPrivExcep = 11;
    private static final int _INDEX_lcsUniversal = 12;
    private static final int _INDEX_lcsCallSessionRelated = 13;
    private static final int _INDEX_lcsCallSessionUnrelated = 14;
    private static final int _INDEX_lcsPLMNOperator = 15;
    private static final int _INDEX_lcsServiceType = 16;
    private static final int _INDEX_lcsAllMOLRSS = 17;
    private static final int _INDEX_lcsBasicSelfLocation = 18;
    private static final int _INDEX_lcsAutonomousSelfLocation = 19;
    private static final int _INDEX_lcsTransferToThirdParty = 20;
    private static final int _INDEX_smMoPp = 21;
    private static final int _INDEX_barringOutgoingCalls = 22;
    private static final int _INDEX_baoc = 23;
    private static final int _INDEX_boic = 24;
    private static final int _INDEX_boicExHC = 25;

    public SupportedFeaturesImpl() {
    }

    public SupportedFeaturesImpl(boolean odbAllApn, boolean odbHPLMNApn, boolean odbVPLMNApn, boolean odbAllOg,
            boolean odbAllInternationalOg, boolean odbAllIntOgNotToHPLMNCountry, boolean odbAllInterzonalOg,
            boolean odbAllInterzonalOgNotToHPLMNCountry, boolean odbAllInterzonalOgandInternatOgNotToHPLMNCountry,
            boolean regSub, boolean trace, boolean lcsAllPrivExcep, boolean lcsUniversal, boolean lcsCallSessionRelated,
            boolean lcsCallSessionUnrelated, boolean lcsPLMNOperator, boolean lcsServiceType, boolean lcsAllMOLRSS,
            boolean lcsBasicSelfLocation, boolean lcsAutonomousSelfLocation, boolean lcsTransferToThirdParty, boolean smMoPp,
            boolean barringOutgoingCalls, boolean baoc, boolean boic, boolean boicExHC) {
    	super("SupportedFeatures",25,39,false);
        if (odbAllApn)
            this.setBit(_INDEX_odbAllApn);
        if (odbHPLMNApn)
            this.setBit(_INDEX_odbHPLMNApn);
        if (odbVPLMNApn)
            this.setBit(_INDEX_odbVPLMNApn);
        if (odbAllOg)
            this.setBit(_INDEX_odbAllOg);
        if (odbAllInternationalOg)
            this.setBit(_INDEX_odbAllInternationalOg);
        if (odbAllIntOgNotToHPLMNCountry)
            this.setBit(_INDEX_odbAllIntOgNotToHPLMNCountry);
        if (odbAllInterzonalOg)
            this.setBit(_INDEX_odbAllInterzonalOg);
        if (odbAllInterzonalOgNotToHPLMNCountry)
            this.setBit(_INDEX_odbAllInterzonalOgNotToHPLMNCountry);
        if (odbAllInterzonalOgandInternatOgNotToHPLMNCountry)
            this.setBit(_INDEX_odbAllInterzonalOgandInternatOgNotToHPLMNCountry);
        if (regSub)
            this.setBit(_INDEX_regSub);
        if (trace)
            this.setBit(_INDEX_trace);
        if (lcsAllPrivExcep)
            this.setBit(_INDEX_lcsAllPrivExcep);
        if (lcsUniversal)
            this.setBit(_INDEX_lcsUniversal);
        if (lcsCallSessionRelated)
            this.setBit(_INDEX_lcsCallSessionRelated);
        if (lcsCallSessionUnrelated)
            this.setBit(_INDEX_lcsCallSessionUnrelated);
        if (lcsPLMNOperator)
            this.setBit(_INDEX_lcsPLMNOperator);
        if (lcsServiceType)
            this.setBit(_INDEX_lcsServiceType);
        if (lcsAllMOLRSS)
            this.setBit(_INDEX_lcsAllMOLRSS);
        if (lcsBasicSelfLocation)
            this.setBit(_INDEX_lcsBasicSelfLocation);
        if (lcsAutonomousSelfLocation)
            this.setBit(_INDEX_lcsAutonomousSelfLocation);
        if (lcsTransferToThirdParty)
            this.setBit(_INDEX_lcsTransferToThirdParty);
        if (smMoPp)
            this.setBit(_INDEX_smMoPp);
        if (barringOutgoingCalls)
            this.setBit(_INDEX_barringOutgoingCalls);
        if (baoc)
            this.setBit(_INDEX_baoc);
        if (boic)
            this.setBit(_INDEX_boic);
        if (boicExHC)
            this.setBit(_INDEX_boicExHC);
    }

    public boolean getOdbAllApn() {
        return this.isBitSet(_INDEX_odbAllApn);
    }

    public boolean getOdbHPLMNApn() {
        return this.isBitSet(_INDEX_odbHPLMNApn);
    }

    public boolean getOdbVPLMNApn() {
        return this.isBitSet(_INDEX_odbVPLMNApn);
    }

    public boolean getOdbAllOg() {
        return this.isBitSet(_INDEX_odbAllOg);
    }

    public boolean getOdbAllInternationalOg() {
        return this.isBitSet(_INDEX_odbAllInternationalOg);
    }

    public boolean getOdbAllIntOgNotToHPLMNCountry() {
        return this.isBitSet(_INDEX_odbAllIntOgNotToHPLMNCountry);
    }

    public boolean getOdbAllInterzonalOg() {
        return this.isBitSet(_INDEX_odbAllInterzonalOg);
    }

    public boolean getOdbAllInterzonalOgNotToHPLMNCountry() {
        return this.isBitSet(_INDEX_odbAllInterzonalOgNotToHPLMNCountry);
    }

    public boolean getOdbAllInterzonalOgandInternatOgNotToHPLMNCountry() {
        return this.isBitSet(_INDEX_odbAllInterzonalOgandInternatOgNotToHPLMNCountry);
    }

    public boolean getRegSub() {
        return this.isBitSet(_INDEX_regSub);
    }

    public boolean getTrace() {
        return this.isBitSet(_INDEX_trace);
    }

    public boolean getLcsAllPrivExcep() {
        return this.isBitSet(_INDEX_lcsAllPrivExcep);
    }

    public boolean getLcsUniversal() {
        return this.isBitSet(_INDEX_lcsUniversal);
    }

    public boolean getLcsCallSessionRelated() {
        return this.isBitSet(_INDEX_lcsCallSessionRelated);
    }

    public boolean getLcsCallSessionUnrelated() {
        return this.isBitSet(_INDEX_lcsCallSessionUnrelated);
    }

    public boolean getLcsPLMNOperator() {
        return this.isBitSet(_INDEX_lcsPLMNOperator);
    }

    public boolean getLcsServiceType() {
        return this.isBitSet(_INDEX_lcsServiceType);
    }

    public boolean getLcsAllMOLRSS() {
        return this.isBitSet(_INDEX_lcsAllMOLRSS);
    }

    public boolean getLcsBasicSelfLocation() {
        return this.isBitSet(_INDEX_lcsBasicSelfLocation);
    }

    public boolean getLcsAutonomousSelfLocation() {
        return this.isBitSet(_INDEX_lcsAutonomousSelfLocation);
    }

    public boolean getLcsTransferToThirdParty() {
        return this.isBitSet(_INDEX_lcsTransferToThirdParty);
    }

    public boolean getSmMoPp() {
        return this.isBitSet(_INDEX_smMoPp);
    }

    public boolean getBarringOutgoingCalls() {
        return this.isBitSet(_INDEX_barringOutgoingCalls);
    }

    public boolean getBaoc() {
        return this.isBitSet(_INDEX_baoc);
    }

    public boolean getBoic() {
        return this.isBitSet(_INDEX_boic);
    }

    public boolean getBoicExHC() {
        return this.isBitSet(_INDEX_boicExHC);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SupportedFeatures [");
        if (this.getOdbAllApn())
            sb.append("odbAllApn, ");
        if (this.getOdbHPLMNApn())
            sb.append("odbHPLMNApn, ");
        if (this.getOdbVPLMNApn())
            sb.append("odbVPLMNApn, ");
        if (this.getOdbAllOg())
            sb.append("odbAllOg, ");
        if (this.getOdbAllInternationalOg())
            sb.append("odbAllInternationalOg, ");
        if (this.getOdbAllIntOgNotToHPLMNCountry())
            sb.append("odbAllIntOgNotToHPLMNCountry, ");
        if (this.getOdbAllInterzonalOg())
            sb.append("odbAllInterzonalOg, ");
        if (this.getOdbAllInterzonalOgNotToHPLMNCountry())
            sb.append("odbAllInterzonalOgNotToHPLMNCountry, ");
        if (this.getOdbAllInterzonalOgandInternatOgNotToHPLMNCountry())
            sb.append("odbAllInterzonalOgandInternatOgNotToHPLMNCountry, ");
        if (this.getRegSub())
            sb.append("regSub, ");
        if (this.getTrace())
            sb.append("trace, ");
        if (this.getLcsAllPrivExcep())
            sb.append("lcsAllPrivExcep, ");
        if (this.getLcsUniversal())
            sb.append("lcsUniversal, ");
        if (this.getLcsCallSessionRelated())
            sb.append("lcsCallSessionRelated, ");
        if (this.getLcsCallSessionUnrelated())
            sb.append("lcsCallSessionUnrelated, ");
        if (this.getLcsPLMNOperator())
            sb.append("lcsPLMNOperator, ");
        if (this.getLcsServiceType())
            sb.append("lcsServiceType, ");
        if (this.getLcsAllMOLRSS())
            sb.append("lcsAllMOLRSS, ");
        if (this.getLcsBasicSelfLocation())
            sb.append("lcsBasicSelfLocation, ");
        if (this.getLcsAutonomousSelfLocation())
            sb.append("lcsAutonomousSelfLocation, ");
        if (this.getLcsTransferToThirdParty())
            sb.append("lcsTransferToThirdParty, ");
        if (this.getSmMoPp())
            sb.append("smMoPp, ");
        if (this.getBarringOutgoingCalls())
            sb.append("barringOutgoingCalls, ");
        if (this.getBaoc())
            sb.append("baoc, ");
        if (this.getBoic())
            sb.append("boic, ");
        if (this.getBoicExHC())
            sb.append("boicExHC ");

        sb.append("]");
        return sb.toString();
    }

}
