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

package org.restcomm.protocols.ss7.tcapAnsi.asn;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentPortion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCUniMessage;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.ComponentPortionImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

/**
 * @author yulian.oifa
 *
 */
public class TCUnknownMessageImpl extends TCUnifiedMessageImpl implements TCUniMessage {
	public static String NAME="Unknown";

	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=0x08,constructed=true,index=-1,defaultImplementation = ComponentPortionImpl.class)
	private ComponentPortion component;

    
    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.TCUniMessage#getComponent()
     */
    public ComponentPortion getComponent() {

        return component;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.TCUniMessage#setComponent(org
     * .restcomm.protocols.ss7.tcap.asn.comp.Component[])
     */
    public void setComponent(ComponentPortion c) {
        this.component = c;

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TCUnkownMessage [");

        if(this.getOriginatingTransactionId()!=null) {
            sb.append("originating TX=");
            sb.append(ASNOctetString.printDataArr(this.getOriginatingTransactionId()));
            sb.append(", ");
        }
        
        if(this.getDestinationTransactionId()!=null) {
            sb.append("destination TX=");
            sb.append(ASNOctetString.printDataArr(this.getDestinationTransactionId()));
            sb.append(", ");
        }
        
        if (this.getDialogPortion() != null) {
            sb.append("DialogPortion=");
            sb.append(this.getDialogPortion());
            sb.append(", ");
        }
        if (this.component != null && this.component.getComponents()!=null && this.component.getComponents().size() > 0) {
            sb.append("Components=[");
            int i1 = 0;
            for (Component comp : this.component.getComponents()) {
                if (i1 == 0)
                    i1 = 1;
                else
                    sb.append(", ");
                sb.append(comp.toString());
            }
            sb.append("], ");
        }
        sb.append("]");
        return sb.toString();
    }

    public boolean isDialogPortionExists() {
    	return true;
    }

	@Override
	public String getName() {
		return NAME;
	}
}