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

package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CAI_GSM0224;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
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
    	if(e1!=null)
    		this.e1 = new ASNInteger(e1,"E1",0,8191,false);
    		
    	if(e2!=null)
    		this.e2 = new ASNInteger(e2,"E2",0,8191,false);
    		
    	if(e3!=null)
    		this.e3 = new ASNInteger(e3,"E3",0,8191,false);
    		
    	if(e4!=null)
    		this.e4 = new ASNInteger(e4,"E4",0,8191,false);
    		
    	if(e5!=null)
    		this.e5 = new ASNInteger(e5,"E5",0,8191,false);
    		
    	if(e6!=null)
    		this.e6 = new ASNInteger(e6,"E6",0,8191,false);
    		
    	if(e7!=null)
    		this.e7 = new ASNInteger(e7,"E7",0,8191,false);    		
    }

    public Integer getE1() {
    	if(e1==null)
    		return null;
    	
        return e1.getIntValue();
    }

    public Integer getE2() {
    	if(e2==null)
    		return null;
    	
        return e2.getIntValue();
    }

    public Integer getE3() {
    	if(e3==null)
    		return null;
    	
        return e3.getIntValue();
    }

    public Integer getE4() {
    	if(e4==null)
    		return null;
    	
        return e4.getIntValue();
    }

    public Integer getE5() {
    	if(e5==null)
    		return null;
    	
        return e5.getIntValue();
    }

    public Integer getE6() {
    	if(e6==null)
    		return null;
    	
        return e6.getIntValue();
    }

    public Integer getE7() {
    	if(e7==null)
    		return null;
    	
        return e7.getIntValue();
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