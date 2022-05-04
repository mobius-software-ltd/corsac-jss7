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

package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageSMDeliveryFailure;
import org.restcomm.protocols.ss7.map.api.errors.SMEnumeratedDeliveryFailureCause;
import org.restcomm.protocols.ss7.map.api.primitives.SignalInfo;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsDeliverReportTpdu;
import org.restcomm.protocols.ss7.map.smstpdu.SmsDeliverReportTpduImpl;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 * @author yulianoifa
 */
public class MAPErrorMessageSMDeliveryFailure1Impl extends EnumeratedMAPErrorMessage1Impl implements
MAPErrorMessageSMDeliveryFailure {	
	public MAPErrorMessageSMDeliveryFailure1Impl(SMEnumeratedDeliveryFailureCause smEnumeratedDeliveryFailureCause) {
        super(MAPErrorCode.smDeliveryFailure,"SMDeliveryFailure",0,6);

        if(smEnumeratedDeliveryFailureCause!=null)
        	setValue(smEnumeratedDeliveryFailureCause.getCode());
    }

    public MAPErrorMessageSMDeliveryFailure1Impl() {
        super(MAPErrorCode.smDeliveryFailure,"SMDeliveryFailure",0,6);
    }

    public boolean isEmSMDeliveryFailure() {
        return true;
    }

    public MAPErrorMessageSMDeliveryFailure getEmSMDeliveryFailure() {
        return this;
    }

    @Override
    public SMEnumeratedDeliveryFailureCause getSMEnumeratedDeliveryFailureCause() {
    	Integer result=getValue();
    	if(result==null)
    		return null;
    	
    	return SMEnumeratedDeliveryFailureCause.getInstance(result.intValue());    	
    }

    @Override
    public void setSMEnumeratedDeliveryFailureCause(SMEnumeratedDeliveryFailureCause val) {
    	if(val!=null)
    		setValue(val.getCode());
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
	public SignalInfo getSignalInfo() {
		return null;
	}

	@Override
	public void setSignalInfo(SignalInfo signalInfo) {		
	}

	@Override
	public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
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
