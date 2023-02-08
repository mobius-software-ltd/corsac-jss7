/*
 * Mobius Software LTD
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

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientName;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSRequestorID;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 * @author amit bhayani
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class LCSClientIDImpl implements LCSClientID {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=0)
    private ASNLCSClientType lcsClientType;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1, defaultImplementation = LCSClientExternalIDImpl.class)
    private LCSClientExternalID lcsClientExternalID;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = AddressStringImpl.class)
    private AddressString lcsClientDialedByMS;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNLCSClientInternalID lcsClientInternalID;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1, defaultImplementation = LCSClientNameImpl.class)
    private LCSClientName lcsClientName;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1, defaultImplementation = APNImpl.class)
    private APN lcsAPN;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=true,index=-1, defaultImplementation = LCSRequestorIDImpl.class)
    private LCSRequestorID lcsRequestorID;

    /**
     * @param lcsClientType
     * @param lcsClientExternalID
     * @param lcsClientInternalID
     * @param lcsClientName
     * @param lcsClientDialedByMS
     * @param lcsAPN
     * @param lcsRequestorID
     */
    public LCSClientIDImpl(LCSClientType lcsClientType, LCSClientExternalID lcsClientExternalID,
            LCSClientInternalID lcsClientInternalID, LCSClientName lcsClientName, AddressString lcsClientDialedByMS,
            APN lcsAPN, LCSRequestorID lcsRequestorID) {
        super();
        if(lcsClientType!=null)
        	this.lcsClientType = new ASNLCSClientType(lcsClientType);
        	
        this.lcsClientExternalID = lcsClientExternalID;

        if(lcsClientInternalID!=null)
        	this.lcsClientInternalID = new ASNLCSClientInternalID(lcsClientInternalID);
        	
        this.lcsClientName = lcsClientName;
        this.lcsClientDialedByMS = lcsClientDialedByMS;
        this.lcsAPN = lcsAPN;
        this.lcsRequestorID = lcsRequestorID;
    }

    /**
     *
     */
    public LCSClientIDImpl() {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID#getLCSClientType ()
     */
    public LCSClientType getLCSClientType() {
    	if(this.lcsClientType==null)
    		return null;
    	
        return this.lcsClientType.getType();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID# getLCSClientExternalID()
     */
    public LCSClientExternalID getLCSClientExternalID() {
        return this.lcsClientExternalID;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID# getLCSClientDialedByMS()
     */
    public AddressString getLCSClientDialedByMS() {
        return this.lcsClientDialedByMS;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID# getLCSClientInternalID()
     */
    public LCSClientInternalID getLCSClientInternalID() {
    	if(this.lcsClientInternalID==null)
    		return null;
    	
        return this.lcsClientInternalID.getType();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID#getLCSClientName ()
     */
    public LCSClientName getLCSClientName() {
        return this.lcsClientName;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID#getLCSAPN()
     */
    public APN getLCSAPN() {
        return this.lcsAPN;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID#getLCSRequestorID ()
     */
    public LCSRequestorID getLCSRequestorID() {
        return this.lcsRequestorID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LCSClientID [");

        if (this.lcsClientType != null) {
            sb.append("lcsClientType=");
            sb.append(this.lcsClientType.toString());
        }
        if (this.lcsClientExternalID != null) {
            sb.append(", lcsClientExternalID=");
            sb.append(this.lcsClientExternalID.toString());
        }
        if (this.lcsClientInternalID != null) {
            sb.append(", lcsClientInternalID=");
            sb.append(this.lcsClientInternalID.toString());
        }
        if (this.lcsClientName != null) {
            sb.append(", lcsClientName=");
            sb.append(this.lcsClientName.toString());
        }
        if (this.lcsClientDialedByMS != null) {
            sb.append(", lcsClientDialedByMS=");
            sb.append(this.lcsClientDialedByMS.toString());
        }
        if (this.lcsAPN != null) {
            sb.append(", lcsAPN=");
            sb.append(this.lcsAPN.toString());
        }
        if (this.lcsRequestorID != null) {
            sb.append(", lcsRequestorID=");
            sb.append(this.lcsRequestorID.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(lcsClientType==null)
			throw new ASNParsingComponentException("lcs client type should be set for lcs client ID", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
