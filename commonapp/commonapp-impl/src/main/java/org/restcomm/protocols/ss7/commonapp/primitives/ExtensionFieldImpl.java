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

package org.restcomm.protocols.ss7.commonapp.primitives;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CriticalityType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ExtensionField;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNObjectIdentifier;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ExtensionFieldImpl implements ExtensionField {

	@ASNProperty(asnClass = ASNClass.UNIVERSAL,tag = 2,constructed = false,index = 0)
    private ASNInteger localCode;

	@ASNProperty(asnClass = ASNClass.UNIVERSAL,tag = 6,constructed = false,index = 0)
	private ASNObjectIdentifier globalCode;
    
	private ASNCriticalityType criticalityType;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    public ASNOctetString data;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
    public ASNOctetString data1;
    
    public ExtensionFieldImpl() {
    }

    public ExtensionFieldImpl(Integer localCode, CriticalityType criticalityType, ByteBuf data, boolean isConstructred) {
    	if(localCode!=null)
    		this.localCode = new ASNInteger(localCode);
    		
        if(criticalityType!=null && criticalityType!=CriticalityType.typeIgnore)
        	this.criticalityType = new ASNCriticalityType(criticalityType);
        	       
        if(data!=null) {
        	if(!isConstructred)
        		this.data = new ASNOctetString(data);        		
        	else if(isConstructred)
        		this.data1 = new ASNOctetString(data);        		
        }
    }

    public ExtensionFieldImpl(List<Long> globalCode, CriticalityType criticalityType, ByteBuf data, boolean isConstructred) {
    	if(globalCode!=null)
    		this.globalCode = new ASNObjectIdentifier(globalCode);
    		
        if(criticalityType!=null)
        	this.criticalityType = new ASNCriticalityType(criticalityType);
        	
        if(data!=null) {
        	if(!isConstructred)
        		this.data = new ASNOctetString(data);        	
        	else if(isConstructred)
        		this.data1 = new ASNOctetString(data);        	
        }
    }

    public Integer getLocalCode() {
    	if(localCode==null)
    		return null;
    	
        return localCode.getIntValue();
    }

    public List<Long> getGlobalCode() {
    	if(globalCode==null)
    		return null;
    	
        return globalCode.getValue();
    }

    public CriticalityType getCriticalityType() {
    	if(criticalityType==null)
    		return CriticalityType.typeIgnore;
    	
        return criticalityType.getType();
    }

    public ByteBuf getValue() {
    	if(data==null && data1==null)
    		return null;
    	
    	if(data!=null)
    		return data.getValue();
    	else
    		return data1.getValue();    	
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ExtensionField [");
        if (this.localCode != null) {
            sb.append("localCode=");
            sb.append(this.localCode.getValue());
        }
        if (this.globalCode != null && this.globalCode.getValue()!=null) {
            sb.append("globalCode=[");
            sb.append(ASNObjectIdentifier.printDataArrLong(this.globalCode.getValue()));
            sb.append("]");
        }
        if (this.criticalityType != null) {
            sb.append(", criticalityType=");
            sb.append(criticalityType);
        }
        if (this.data != null && this.data.getValue()!=null) {
            sb.append(", data=[");
            sb.append(this.data.printDataArr());
            sb.append("]");
        }
        if (this.data1 != null && this.data1.getValue()!=null) {
            sb.append(", data=[");
            sb.append(this.data1.printDataArr());
            sb.append("]");
        }
        sb.append("]");

        return sb.toString();
    }
}
