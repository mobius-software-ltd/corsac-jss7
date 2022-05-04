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

package org.restcomm.protocols.ss7.isup.message.parameter;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,lengthIndefinite=false)
public class ReturnResultImpl implements RemoteOperation {
	// mandatory
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=0x02,constructed=false,index=0)
	private ASNInteger invokeId;

	// mandatory
	private ReturnResultInnerImpl inner;
	
	/*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#getInvokeId()
     */
    public Integer getInvokeId() {
    	if(this.invokeId==null)
    		return null;
    	
        return this.invokeId.getIntValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#getOperationCode()
     */
    public OperationCodeImpl getOperationCode() {
    	if(inner==null)
    		return null;
    	
    	return inner.getOperationCode();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#getParameteR()
     */
    public Object getParameter() {
    	if(inner==null)
    		return null;
    	
    	return inner.getParameter();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#setInvokeId(java.lang .Integer)
     */
    public void setInvokeId(Integer i) {
        if ((i == null) || (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
        }
        this.invokeId = new ASNInteger(i,"InvokeID",-128,127,false);        

    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#setOperationCode(org
     * .restcomm.protocols.ss7.tcap.asn.comp.OperationCode)
     */
    public void setOperationCode(OperationCodeImpl i) {
    	if(inner==null)
    		inner=new ReturnResultInnerImpl();
    	
    	inner.setOperationCode(i);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#setParameter(org.restcomm.protocols.ss7.tcap.asn.comp.Parameter)
     */
    public void setParameter(Object p) {
    	if(inner==null)
    		inner=new ReturnResultInnerImpl();
    	
    	inner.setParameter(p);
    }

    public OperationType getType() {

        return OperationType.ReturnResult;
    }

    @Override
    public String toString() {
    	Long invokeIdValue=null;
    	if(this.invokeId!=null)
    		invokeIdValue=this.invokeId.getValue();
    	
    	return "ReturnResult[invokeId=" + invokeIdValue + ", inner=" + inner + " ]";
    }
}
