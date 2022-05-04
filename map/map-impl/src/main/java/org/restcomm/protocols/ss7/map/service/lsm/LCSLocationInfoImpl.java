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

import org.restcomm.protocols.ss7.commonapp.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SupportedLCSCapabilitySetsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class LCSLocationInfoImpl implements LCSLocationInfo {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString networkNodeNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = LMSIImpl.class)
    private LMSI lmsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNNull gprsNodeIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private AdditionalNumberWrapperImpl additionalNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = SupportedLCSCapabilitySetsImpl.class)
    private SupportedLCSCapabilitySets supportedLCSCapabilitySets;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1, defaultImplementation = SupportedLCSCapabilitySetsImpl.class 
    		)
    private SupportedLCSCapabilitySets additionalLCSCapabilitySets;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1, defaultImplementation = DiameterIdentityImpl.class)
    private DiameterIdentity mmeName;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1, defaultImplementation = DiameterIdentityImpl.class)
    private DiameterIdentity aaaServerName;

    /**
     *
     */
    public LCSLocationInfoImpl() {
    }

    /**
     * @param networkNodeNumber
     * @param lmsi
     * @param extensionContainer
     * @param gprsNodeIndicator
     * @param additionalNumber
     * @param supportedLCSCapabilitySets
     * @param additionalLCSCapabilitySets
     */
    public LCSLocationInfoImpl(ISDNAddressString networkNodeNumber, LMSI lmsi, MAPExtensionContainer extensionContainer,
            boolean gprsNodeIndicator, AdditionalNumber additionalNumber,
            SupportedLCSCapabilitySets supportedLCSCapabilitySets, SupportedLCSCapabilitySets additionalLCSCapabilitySets,
            DiameterIdentity mmeName, DiameterIdentity aaaServerName) {
        this.networkNodeNumber = networkNodeNumber;
        this.lmsi = lmsi;
        this.extensionContainer = extensionContainer;
        
        if(gprsNodeIndicator)
        	this.gprsNodeIndicator = new ASNNull();
        
        if(additionalNumber!=null)
        	this.additionalNumber = new AdditionalNumberWrapperImpl(additionalNumber);
        
        this.supportedLCSCapabilitySets = supportedLCSCapabilitySets;
        this.additionalLCSCapabilitySets = additionalLCSCapabilitySets;
        this.mmeName = mmeName;
        this.aaaServerName = aaaServerName;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo# getNetworkNodeNumber()
     */
    public ISDNAddressString getNetworkNodeNumber() {
        return this.networkNodeNumber;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo#getLMSI()
     */
    public LMSI getLMSI() {
        return this.lmsi;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo# getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo# getGprsNodeIndicator()
     */
    public boolean getGprsNodeIndicator() {
        return this.gprsNodeIndicator!=null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo# getAdditionalNumber()
     */
    public AdditionalNumber getAdditionalNumber() {
    	if(this.additionalNumber==null)
    		return null;
    	
        return this.additionalNumber.getAdditionalNumber();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo# getSupportedLCSCapabilitySets()
     */
    public SupportedLCSCapabilitySets getSupportedLCSCapabilitySets() {
        return this.supportedLCSCapabilitySets;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo# getadditionalLCSCapabilitySets()
     */
    public SupportedLCSCapabilitySets getAdditionalLCSCapabilitySets() {
        return this.additionalLCSCapabilitySets;
    }

    public DiameterIdentity getMmeName() {
        return mmeName;
    }

    public DiameterIdentity getAaaServerName() {
        return aaaServerName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((additionalLCSCapabilitySets == null) ? 0 : additionalLCSCapabilitySets.hashCode());
        result = prime * result + ((additionalNumber == null) ? 0 : additionalNumber.hashCode());
        result = prime * result + ((extensionContainer == null) ? 0 : extensionContainer.hashCode());
        result = prime * result + ((gprsNodeIndicator!=null) ? 1 : 0);
        result = prime * result + ((lmsi == null) ? 0 : lmsi.hashCode());
        result = prime * result + ((networkNodeNumber == null) ? 0 : networkNodeNumber.hashCode());
        result = prime * result + ((supportedLCSCapabilitySets == null) ? 0 : supportedLCSCapabilitySets.hashCode());
        result = prime * result + ((mmeName == null) ? 0 : mmeName.hashCode());
        result = prime * result + ((aaaServerName == null) ? 0 : aaaServerName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LCSLocationInfoImpl other = (LCSLocationInfoImpl) obj;
        if (additionalLCSCapabilitySets == null) {
            if (other.additionalLCSCapabilitySets != null)
                return false;
        } else if (!additionalLCSCapabilitySets.equals(other.additionalLCSCapabilitySets))
            return false;
        if (additionalNumber == null) {
            if (other.additionalNumber != null)
                return false;
        } else if (!additionalNumber.equals(other.additionalNumber))
            return false;
        if (extensionContainer == null) {
            if (other.extensionContainer != null)
                return false;
        } else if (!extensionContainer.equals(other.extensionContainer))
            return false;
        if (gprsNodeIndicator != other.gprsNodeIndicator) {
            return false;
        }
        if (lmsi == null) {
            if (other.lmsi != null)
                return false;
        } else if (!lmsi.equals(other.lmsi))
            return false;
        if (networkNodeNumber == null) {
            if (other.networkNodeNumber != null)
                return false;
        } else if (!networkNodeNumber.equals(other.networkNodeNumber))
            return false;
        if (supportedLCSCapabilitySets == null) {
            if (other.supportedLCSCapabilitySets != null)
                return false;
        } else if (!supportedLCSCapabilitySets.equals(other.supportedLCSCapabilitySets))
            return false;
        if (mmeName == null) {
            if (other.mmeName != null)
                return false;
        } else if (!mmeName.equals(other.mmeName))
            return false;
        if (aaaServerName == null) {
            if (other.aaaServerName != null)
                return false;
        } else if (!aaaServerName.equals(other.aaaServerName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LCSLocationInfo [");

        if (this.networkNodeNumber != null) {
            sb.append("networkNodeNumber=");
            sb.append(this.networkNodeNumber);
        }
        if (this.lmsi != null) {
            sb.append(", lmsi=");
            sb.append(this.lmsi);
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (this.gprsNodeIndicator!=null) {
            sb.append(", gprsNodeIndicator=");
        }
        if (this.additionalNumber != null) {
            sb.append(", additionalNumber=");
            sb.append(this.additionalNumber.getAdditionalNumber());
        }
        if (this.supportedLCSCapabilitySets != null) {
            sb.append(", supportedLCSCapabilitySets=");
            sb.append(this.supportedLCSCapabilitySets);
        }
        if (this.additionalLCSCapabilitySets != null) {
            sb.append(", additionalLCSCapabilitySets=");
            sb.append(this.additionalLCSCapabilitySets);
        }
        if (this.mmeName != null) {
            sb.append(", mmeName=");
            sb.append(this.mmeName);
        }
        if (this.aaaServerName != null) {
            sb.append(", aaaServerName=");
            sb.append(this.aaaServerName);
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(networkNodeNumber==null)
			throw new ASNParsingComponentException("network node number should be set for lcs location info", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
