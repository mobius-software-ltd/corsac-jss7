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

package org.restcomm.protocols.ss7.tcap.asn.comp;

import java.util.List;

import org.restcomm.protocols.ss7.tcap.api.OperationCodeWithACN;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNExclude;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNGenericMapping;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNPreprocess;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWildcard;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,lengthIndefinite=false)
@ASNPreprocess
public class InvokeImpl implements Invoke {
	// mandatory
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=0x02,constructed=false,index=0)
    private ASNInteger invokeId;

    // optional
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x00,constructed=false,index=1)
	private ASNInteger linkedId;
	
	@ASNExclude
    private OperationCode linkedOperationCode;

    // mandatory
	@ASNChoise(defaultImplementation = OperationCodeImpl.class)
    private OperationCode operationCode;
    
    // optional
    @ASNWildcard
    private ASNInvokeParameterImpl parameter;
    
	@ASNExclude
    private ApplicationContextName acn;
    
	public InvokeImpl() {
    }

    @ASNGenericMapping
    public Class<?> getMapping(ASNParser parser) {
    	if(operationCode!=null)
    	{
    		if(acn!=null) {
    			OperationCodeWithACN operationWithACN=new OperationCodeWithACN(operationCode, acn.getOid());
    			Class<?> result=parser.getLocalMapping(this.getClass(), operationWithACN);
        		if(result!=null)
        			return result;
    		}
    		
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
    public Integer getInvokeId() {
    	if(this.invokeId==null)
    		return null;
    	
        return this.invokeId.getIntValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#getLinkedId()
     */
    public Integer getLinkedId() {
    	if(this.linkedId==null)
    		return null;
    	
        return this.linkedId.getIntValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#getLinkedOperationCode()
     */
    public OperationCode getLinkedOperationCode() {
        return linkedOperationCode;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#getOperationCode()
     */
    public OperationCode getOperationCode() {
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
    public void setInvokeId(Integer i) {
        if ((i == null) || (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
        }
        this.invokeId = new ASNInteger(i,"InvokeID",-128,127,false);        
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#setLinkedId(java.lang .Integer)
     */
    public void setLinkedId(Integer i) {
        if ((i == null) || (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
        }
        this.linkedId = new ASNInteger(i,"InvokeID",-128,127,false);
    }

    public void setLinkedOperationCode(OperationCode linkedOperationCode) {
        this.linkedOperationCode = linkedOperationCode;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#setOperationCode(Long)
     */
    public void setOperationCode(Integer i) {    
    	if(i==null)
    		this.operationCode=null;
    	else {
    		this.operationCode=new OperationCodeImpl();
    		this.operationCode.setLocalOperationCode(i);
    	}
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#setOperationCode(List<Long>)
     */
    public void setOperationCode(List<Long> i) {
    	if(i==null)
    		this.operationCode=null;
    	else {
    		this.operationCode=new OperationCodeImpl();
    		this.operationCode.setGlobalOperationCode(i);
    	}
    }
    
    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#setParameter(org.restcomm .protocols.ss7.tcap.asn.comp.Parameter)
     */
    public void setParameter(Object p) {
    	this.parameter=new ASNInvokeParameterImpl(p);    	
    }

    public ComponentType getType() {

        return ComponentType.Invoke;
    }

    @Override
    public String toString() {
    	Object oc=null;
    	if(this.operationCode!=null) {
    		switch(this.operationCode.getOperationType()) {
				case Global:
					oc=this.operationCode.getGlobalOperationCode();
					break;
				case Local:
					oc=this.operationCode.getLocalOperationCode();
					break;
				default:
					break;
    		}
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

    public void setACN(ApplicationContextName acn) {
    	this.acn=acn;
    }
}