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

package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageSMDeliveryFailure;
import org.restcomm.protocols.ss7.map.api.errors.SMEnumeratedDeliveryFailureCause;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsDeliverReportTpdu;
import org.restcomm.protocols.ss7.map.smstpdu.SmsDeliverReportTpduImpl;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
public class MAPErrorMessageSMDeliveryFailure1Impl extends EnumeratedMAPErrorMessage1Impl implements
MAPErrorMessageSMDeliveryFailure {	
	private long mapProtocolVersion = 1;
    
    public MAPErrorMessageSMDeliveryFailure1Impl(SMEnumeratedDeliveryFailureCause smEnumeratedDeliveryFailureCause) {
        super((long) MAPErrorCode.smDeliveryFailure);

        if(smEnumeratedDeliveryFailureCause!=null)
        	setValue(Long.valueOf(smEnumeratedDeliveryFailureCause.getCode()));
    }

    public MAPErrorMessageSMDeliveryFailure1Impl() {
        super((long) MAPErrorCode.smDeliveryFailure);
    }

    public boolean isEmSMDeliveryFailure() {
        return true;
    }

    public MAPErrorMessageSMDeliveryFailure getEmSMDeliveryFailure() {
        return this;
    }

    @Override
    public SMEnumeratedDeliveryFailureCause getSMEnumeratedDeliveryFailureCause() {
    	Long result=getValue();
    	if(result==null)
    		return null;
    	
    	return SMEnumeratedDeliveryFailureCause.getInstance(result.intValue());    	
    }

    @Override
    public void setSMEnumeratedDeliveryFailureCause(SMEnumeratedDeliveryFailureCause val) {
    	if(val!=null)
    		setValue(Long.valueOf(val.getCode()));
    	else
    		setValue(null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageSystemFailure [");

        SMEnumeratedDeliveryFailureCause smEnumeratedDeliveryFailureCause=getSMEnumeratedDeliveryFailureCause();
        if (smEnumeratedDeliveryFailureCause != null)
            sb.append("smEnumeratedDeliveryFailureCause=" + smEnumeratedDeliveryFailureCause.toString());
        
        sb.append("]");

        return sb.toString();
    }

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return null;
	}

	@Override
	public long getMapProtocolVersion() {
		return mapProtocolVersion;
	}

	@Override
	public byte[] getSignalInfo() {
		return null;
	}

	@Override
	public void setSignalInfo(byte[] signalInfo) {		
	}

	@Override
	public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
	}

	@Override
	public void setMapProtocolVersion(long mapProtocolVersion) {
		this.mapProtocolVersion=mapProtocolVersion;				
	}

	@Override
	public SmsDeliverReportTpduImpl getSmsDeliverReportTpdu() throws MAPException {
		return null;
	}

	@Override
	public void setSmsDeliverReportTpdu(SmsDeliverReportTpdu tpdu)
			throws MAPException {		
	}
}
