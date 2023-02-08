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

package org.restcomm.protocols.ss7.map.service.supplementary;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.primitives.AlertingPatternImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.restcomm.protocols.ss7.map.datacoding.ASNCBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.primitives.USSDStringImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class UnstructuredSSRequestImpl extends SupplementaryMessageImpl implements UnstructuredSSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0)
	private ASNCBSDataCodingSchemeImpl ussdDataCodingSch;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1, defaultImplementation = USSDStringImpl.class)
	private USSDString ussdString;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1, defaultImplementation = AlertingPatternImpl.class)
	private AlertingPattern alertingPattern = null;

    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
	private ISDNAddressString msISDNAddressString = null;
	
	/**
     * @param ussdDataCodingSch
     * @param ussdString
     */
    public UnstructuredSSRequestImpl() {
        super();
    }

    public UnstructuredSSRequestImpl(CBSDataCodingScheme ussdDataCodingSch, USSDString ussdString,
    		AlertingPattern alertingPattern, ISDNAddressString msISDNAddressString) {
    	if(ussdDataCodingSch!=null)
    		this.ussdDataCodingSch=new ASNCBSDataCodingSchemeImpl(ussdDataCodingSch);
    	
        this.ussdString=ussdString;
        this.alertingPattern = alertingPattern;
        this.msISDNAddressString = msISDNAddressString;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.unstructuredSSRequest_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.unstructuredSS_Request;
    }
    
	@Override
	public CBSDataCodingScheme getDataCodingScheme() {
		if(this.ussdDataCodingSch==null)
			return null;
		
		return ussdDataCodingSch.getDataCoding();		
	}

	@Override
	public USSDString getUSSDString() {
		return ussdString;
	}

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.supplementary.
     * ProcessUnstructuredSSRequestIndication#getMSISDNAddressString()
     */
    public ISDNAddressString getMSISDNAddressStringImpl() {
        return this.msISDNAddressString;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.supplementary.
     * ProcessUnstructuredSSRequestIndication#getAlertingPattern()
     */
    public AlertingPattern getAlertingPattern() {
        return this.alertingPattern;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UnstructuredSSRequest [");
        if (this.getMAPDialog() != null) {
            sb.append("DialogId=").append(this.getMAPDialog().getLocalDialogId());
        }
        
        if(this.ussdDataCodingSch!=null) {
        	sb.append(", alertingPattern=");
            sb.append(this.ussdDataCodingSch.toString());
        }
        
        if(this.ussdString!=null) {
        	sb.append(", ussdString=");
            sb.append(this.ussdString.toString());
        }

        if (alertingPattern != null) {
            sb.append(", alertingPattern=");
            sb.append(alertingPattern.toString());
        }
        if (msISDNAddressString != null) {
            sb.append(", msisdn=");
            sb.append(msISDNAddressString.toString());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(ussdDataCodingSch==null)
			throw new ASNParsingComponentException("ussd data coding sch should be set for unstructured SS request", ASNParsingComponentExceptionReason.MistypedRootParameter);

		if(ussdString==null)
			throw new ASNParsingComponentException("ussd string should be set for unstructured SS request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
