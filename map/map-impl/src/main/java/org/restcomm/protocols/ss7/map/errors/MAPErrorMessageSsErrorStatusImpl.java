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

import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageSsErrorStatus;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MAPErrorMessageSsErrorStatusImpl extends MAPErrorMessageImpl implements MAPErrorMessageSsErrorStatus {
	public static final int _mask_QBit = 0x08;
    public static final int _mask_PBit = 0x04;
    public static final int _mask_RBit = 0x02;
    public static final int _mask_ABit = 0x01;

    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1)
    private ASNSingleByte data;
    
    public MAPErrorMessageSsErrorStatusImpl(int data) {
        super(MAPErrorCode.ssErrorStatus);

        this.data = new ASNSingleByte(data,"SSErrorStatus",0,15,true);        
    }

    public MAPErrorMessageSsErrorStatusImpl(boolean qBit, boolean pBit, boolean rBit, boolean aBit) {
        super(MAPErrorCode.ssErrorStatus);

        this.data = new ASNSingleByte((qBit ? _mask_QBit : 0) + (pBit ? _mask_PBit : 0) + (rBit ? _mask_RBit : 0) + (aBit ? _mask_ABit : 0),"SSErrorStatus",0,15,true);        
    }

    public MAPErrorMessageSsErrorStatusImpl() {
        super(MAPErrorCode.ssErrorStatus);
    }

    public boolean isEmSsErrorStatus() {
        return true;
    }

    public MAPErrorMessageSsErrorStatus getEmSsErrorStatus() {
        return this;
    }

    @Override
    public int getData() {
        Integer result=data.getValue();
        if(result==null)
        	return 0;
        
        return result;
    }

    @Override
    public boolean getQBit() {
        return (getData() & _mask_QBit) != 0;
    }

    @Override
    public boolean getPBit() {
        return (getData() & _mask_PBit) != 0;
    }

    @Override
    public boolean getRBit() {
        return (getData() & _mask_RBit) != 0;
    }

    @Override
    public boolean getABit() {
        return (getData() & _mask_ABit) != 0;
    }

    @Override
    public void setData(int val) {
    	this.data=new ASNSingleByte(val,"SSErrorStatus",0,15,true);
    }

    @Override
    public void setQBit(boolean val) {
    	int oldVal=getData();
        if (val) {
        	oldVal |= _mask_QBit;
        } else {
        	oldVal &= (_mask_QBit ^ 0xFF);
        }
        
        setData(oldVal);
    }

    @Override
    public void setPBit(boolean val) {
    	int oldVal=getData();
        if (val) {
        	oldVal |= _mask_PBit;
        } else {
        	oldVal &= (_mask_PBit ^ 0xFF);
        }
        
        setData(oldVal);
    }

    @Override
    public void setRBit(boolean val) {
    	int oldVal=getData();
        if (val) {
        	oldVal |= _mask_RBit;
        } else {
        	oldVal &= (_mask_RBit ^ 0xFF);
        }
        
        setData(oldVal);
    }

    @Override
    public void setABit(boolean val) {
    	int oldVal=getData();
        if (val) {
        	oldVal |= _mask_ABit;
        } else {
        	oldVal &= (_mask_ABit ^ 0xFF);
        }
        
        setData(oldVal);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageSsErrorStatus [");

        if (this.getQBit()) {
            sb.append("QBit");
            sb.append(", ");
        }
        if (this.getPBit()) {
            sb.append("PBit");
            sb.append(", ");
        }
        if (this.getRBit()) {
            sb.append("RBit");
            sb.append(", ");
        }
        if (this.getABit()) {
            sb.append("ABit");
            sb.append(", ");
        }
        sb.append("]");

        return sb.toString();
    }
}
