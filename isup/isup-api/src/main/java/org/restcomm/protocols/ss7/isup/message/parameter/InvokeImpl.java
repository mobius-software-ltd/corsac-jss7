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

package org.restcomm.protocols.ss7.isup.message.parameter;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNGenericMapping;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWildcard;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,lengthIndefinite=false)
public class InvokeImpl implements RemoteOperation {
	// mandatory
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=0x02,constructed=false,index=0)
    private ASNInteger invokeId;

    // optional
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x00,constructed=false,index=1)
    private ASNInteger linkedId;
	
	// mandatory
	@ASNChoise
    private OperationCodeImpl operationCode;
    
    // optional
    @ASNWildcard
    private ASNInvokeParameterImpl parameter;
    
    @ASNGenericMapping
    public Class<?> getMapping(ASNParser parser) {
    	if(operationCode!=null)
    	{
    		Class<?> result=parser.getLocalMapping(this.getClass(), operationCode);
    		if(result==null)
    			result=parser.getDefaultLocalMapping(this.getClass());
    		
    		return result;
    	}
    	
    	return null;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#getInvokeId()
     */
    public Long getInvokeId() {
    	if(this.invokeId==null)
    		return null;
    	
        return this.invokeId.getValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#getLinkedId()
     */
    public Long getLinkedId() {
    	if(this.linkedId==null)
    		return null;
    	
        return this.linkedId.getValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#getOperationCode()
     */
    public OperationCodeImpl getOperationCode() {
    	return operationCode;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#getParameteR()
     */
    public Object getParameter() {
    	if(this.parameter==null)
    		return null;
    	
        return this.parameter.getValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#setInvokeId(java.lang .Integer)
     */
    public void setInvokeId(Long i) {
        if ((i == null) || (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
        }
        this.invokeId = new ASNInteger();
        this.invokeId.setValue(i);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#setLinkedId(java.lang .Integer)
     */
    public void setLinkedId(Long i) {
        if ((i == null) || (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
        }
        this.linkedId = new ASNInteger();
        this.linkedId.setValue(i);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#setOperationCode(org
     * .mobicents.protocols.ss7.tcap.asn.comp.OperationCode)
     */
    public void setOperationCode(OperationCodeImpl i) {
    	this.operationCode=i;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#setParameter(org.restcomm.protocols.ss7.tcap.asn.comp.Parameter)
     */
    public void setParameter(Object p) {
    	this.parameter=new ASNInvokeParameterImpl();
    	this.parameter.setValue(p);
    }

    public OperationType getType() {

        return OperationType.Invoke;
    }

    @Override
    public String toString() {
    	Object oc=null;
    	if(this.operationCode!=null) {
    		if(this.operationCode.getLocalOperationCode()!=null)
    			oc=this.operationCode.getLocalOperationCode();
    		else if(this.operationCode.getGlobalOperationCode()!=null)
    			oc=this.operationCode.getGlobalOperationCode();
    	}
    	
    	Long invokeIdValue=null;
    	if(this.invokeId!=null)
    		invokeIdValue=this.invokeId.getValue();
    	
    	Long linkedInvokeIdValue=null;
    	if(this.linkedId!=null)
    		linkedInvokeIdValue=this.linkedId.getValue();
    	
    	Object p=null;
    	if(this.parameter!=null)
    		p=this.parameter.getValue();
    	
        return "Invoke[invokeId=" + invokeIdValue + ", linkedId=" + linkedInvokeIdValue + ", operationCode=" + oc + ", parameter="
                + p + "]";
    }
}
