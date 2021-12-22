/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.map.service.supplementary;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.primitives.AlertingPatternImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.restcomm.protocols.ss7.map.datacoding.ASNCBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.primitives.USSDStringImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ProcessUnstructuredSSRequestImpl extends SupplementaryMessageImpl implements ProcessUnstructuredSSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0)
	private ASNCBSDataCodingSchemeImpl ussdDataCodingSch;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1, defaultImplementation = USSDStringImpl.class)
	private USSDString ussdString;
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
	private ISDNAddressString msISDNAddressStringImpl = null;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1,defaultImplementation = AlertingPatternImpl.class)
	private AlertingPattern alertingPattern = null;

    /**
     * @param ussdDataCodingSch
     * @param ussdString
     */
    public ProcessUnstructuredSSRequestImpl() {
        super();
    }

    public ProcessUnstructuredSSRequestImpl(CBSDataCodingScheme ussdDataCodingSch, USSDString ussdString,
    		AlertingPattern alertingPattern, ISDNAddressString msISDNAddressStringImpl) {
    	if(ussdDataCodingSch!=null)
    		this.ussdDataCodingSch=new ASNCBSDataCodingSchemeImpl(ussdDataCodingSch);
    	
        this.ussdString=ussdString;
        this.alertingPattern = alertingPattern;
        this.msISDNAddressStringImpl = msISDNAddressStringImpl;
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
     * ProcessUnstructuredSSRequestIndication#getMSISDNAddressStringImpl()
     */
    public ISDNAddressString getMSISDNAddressStringImpl() {
        return this.msISDNAddressStringImpl;
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

    public MAPMessageType getMessageType() {
        return MAPMessageType.processUnstructuredSSRequest_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.processUnstructuredSS_Request;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProcessUnstructuredSSRequest [");

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
        if (msISDNAddressStringImpl != null) {
            sb.append(", msisdn=");
            sb.append(msISDNAddressStringImpl.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
