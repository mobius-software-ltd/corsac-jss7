/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAI_GSM0224;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class CAI_GSM0224Impl implements CAI_GSM0224 {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0, constructed = false,index = -1)
    private ASNInteger e1;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1, constructed = false,index = -1)
    private ASNInteger e2;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2, constructed = false,index = -1)
    private ASNInteger e3;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3, constructed = false,index = -1)
    private ASNInteger e4;
   
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4, constructed = false,index = -1)
    private ASNInteger e5;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5, constructed = false,index = -1)
    private ASNInteger e6;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6, constructed = false,index = -1)
    private ASNInteger e7;

    public CAI_GSM0224Impl() {
    }

    public CAI_GSM0224Impl(Integer e1, Integer e2, Integer e3, Integer e4, Integer e5, Integer e6, Integer e7) {
    	if(e1!=null) {
    		this.e1 = new ASNInteger();
    		this.e1.setValue(e1.longValue());
    	}
    	
    	if(e2!=null) {
    		this.e2 = new ASNInteger();
    		this.e2.setValue(e2.longValue());
    	}
    	
    	if(e3!=null) {
    		this.e3 = new ASNInteger();
    		this.e3.setValue(e3.longValue());
    	}
    	
    	if(e4!=null) {
    		this.e4 = new ASNInteger();
    		this.e4.setValue(e4.longValue());
    	}
    	
    	if(e5!=null) {
    		this.e5 = new ASNInteger();
    		this.e5.setValue(e5.longValue());
    	}

    	if(e6!=null) {
    		this.e6 = new ASNInteger();
    		this.e6.setValue(e6.longValue());
    	}
    	
    	if(e7!=null) {
    		this.e7 = new ASNInteger();
    		this.e7.setValue(e7.longValue());
    	}
    }

    public Integer getE1() {
    	if(e1==null || e1.getValue()==null)
    		return null;
    	
        return e1.getValue().intValue();
    }

    public Integer getE2() {
    	if(e2==null || e2.getValue()==null)
    		return null;
    	
        return e2.getValue().intValue();
    }

    public Integer getE3() {
    	if(e3==null || e3.getValue()==null)
    		return null;
    	
        return e3.getValue().intValue();
    }

    public Integer getE4() {
    	if(e4==null || e4.getValue()==null)
    		return null;
    	
        return e4.getValue().intValue();
    }

    public Integer getE5() {
    	if(e5==null || e5.getValue()==null)
    		return null;
    	
        return e5.getValue().intValue();
    }

    public Integer getE6() {
    	if(e6==null || e6.getValue()==null)
    		return null;
    	
        return e6.getValue().intValue();
    }

    public Integer getE7() {
    	if(e7==null || e7.getValue()==null)
    		return null;
    	
        return e7.getValue().intValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CAI_GSM0224 [");

        if (this.e1 != null && this.e1.getValue()!=null) {
            sb.append("e1=");
            sb.append(this.e1.getValue());
        }
        if (this.e2 != null && this.e2.getValue()!=null) {
            sb.append(", e2=");
            sb.append(this.e2.getValue());
        }
        if (this.e3 != null && this.e3.getValue()!=null) {
            sb.append(", e3=");
            sb.append(this.e3.getValue());
        }
        if (this.e4 != null && this.e4.getValue()!=null) {
            sb.append(", e4=");
            sb.append(this.e4.getValue());
        }
        if (this.e5 != null && this.e5.getValue()!=null) {
            sb.append(", e5=");
            sb.append(this.e5.getValue());
        }
        if (this.e6 != null && this.e6.getValue()!=null) {
            sb.append(", e6=");
            sb.append(this.e6.getValue());
        }
        if (this.e7 != null && this.e7.getValue()!=null) {
            sb.append(", e7=");
            sb.append(this.e7.getValue());
        }

        sb.append("]");

        return sb.toString();
    }
}