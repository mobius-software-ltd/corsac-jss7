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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.DataItemID;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString2;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class DataItemIDImpl implements DataItemID {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNOctetString2 attribute0;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNOctetString2 attribute1;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false, index=-1)
    private ASNOctetString2 attribute2;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false, index=-1)
    private ASNOctetString2 attribute3;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false, index=-1)
    private ASNOctetString2 attribute4;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false, index=-1)
    private ASNOctetString2 attribute5;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false, index=-1)
    private ASNOctetString2 attribute6;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = false, index=-1)
    private ASNOctetString2 attribute7;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = false, index=-1)
    private ASNOctetString2 attribute8;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 9,constructed = false, index=-1)
    private ASNOctetString2 attribute9;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = false, index=-1)
    private ASNOctetString2 attribute10;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 11,constructed = false, index=-1)
    private ASNOctetString2 attribute11;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 12,constructed = false, index=-1)
    private ASNOctetString2 attribute12;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 13,constructed = false, index=-1)
    private ASNOctetString2 attribute13;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 14,constructed = false, index=-1)
    private ASNOctetString2 attribute14;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 15,constructed = false, index=-1)
    private ASNOctetString2 attribute15;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 16,constructed = false, index=-1)
    private ASNOctetString2 attribute16;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 17,constructed = false, index=-1)
    private ASNOctetString2 attribute17;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 18,constructed = false, index=-1)
    private ASNOctetString2 attribute18;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 19,constructed = false, index=-1)
    private ASNOctetString2 attribute19;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 20,constructed = false, index=-1)
    private ASNOctetString2 attribute20;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 21,constructed = false, index=-1)
    private ASNOctetString2 attribute21;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 22,constructed = false, index=-1)
    private ASNOctetString2 attribute22;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 23,constructed = false, index=-1)
    private ASNOctetString2 attribute23;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 24,constructed = false, index=-1)
    private ASNOctetString2 attribute24;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 25,constructed = false, index=-1)
    private ASNOctetString2 attribute25;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 26,constructed = false, index=-1)
    private ASNOctetString2 attribute26;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 27,constructed = false, index=-1)
    private ASNOctetString2 attribute27;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 28,constructed = false, index=-1)
    private ASNOctetString2 attribute28;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 29,constructed = false, index=-1)
    private ASNOctetString2 attribute29;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 30,constructed = false, index=-1)
    private ASNOctetString2 attribute30;
	
	public DataItemIDImpl() {
    }

    public DataItemIDImpl(ByteBuf attribute0,ByteBuf attribute1,ByteBuf attribute2,ByteBuf attribute3,
    		ByteBuf attribute4,ByteBuf attribute5,ByteBuf attribute6,ByteBuf attribute7,ByteBuf attribute8,
    		ByteBuf attribute9,ByteBuf attribute10,ByteBuf attribute11,ByteBuf attribute12,ByteBuf attribute13,
    		ByteBuf attribute14,ByteBuf attribute15,ByteBuf attribute16,ByteBuf attribute17,ByteBuf attribute18,
    		ByteBuf attribute19,ByteBuf attribute20,ByteBuf attribute21,ByteBuf attribute22,ByteBuf attribute23,
    		ByteBuf attribute24,ByteBuf attribute25,ByteBuf attribute26,ByteBuf attribute27,ByteBuf attribute28,
    		ByteBuf attribute29,ByteBuf attribute30) {
    	
    	if(attribute0!=null)
    		this.attribute0=new ASNOctetString2(attribute0);    		
    	
    	if(attribute1!=null)
    		this.attribute1=new ASNOctetString2(attribute1);    		
    	
    	if(attribute2!=null)
    		this.attribute2=new ASNOctetString2(attribute2);    		
    	
    	if(attribute3!=null)
    		this.attribute3=new ASNOctetString2(attribute3);    		
    	
    	if(attribute4!=null)
    		this.attribute4=new ASNOctetString2(attribute4);    		
    	
    	if(attribute5!=null)
    		this.attribute5=new ASNOctetString2(attribute5);    		
    	
    	if(attribute6!=null)
    		this.attribute6=new ASNOctetString2(attribute6);    		
    	
    	if(attribute7!=null)
    		this.attribute7=new ASNOctetString2(attribute7);    		
    	
    	if(attribute8!=null)
    		this.attribute8=new ASNOctetString2(attribute8);    		
    	
    	if(attribute9!=null)
    		this.attribute9=new ASNOctetString2(attribute9);    		
    	
    	if(attribute10!=null)
    		this.attribute10=new ASNOctetString2(attribute10);    		
    	
    	if(attribute11!=null)
    		this.attribute11=new ASNOctetString2(attribute11);    		
    	
    	if(attribute12!=null)
    		this.attribute12=new ASNOctetString2(attribute12);    		
    	
    	if(attribute13!=null)
    		this.attribute13=new ASNOctetString2(attribute13);    		
    	
    	if(attribute14!=null)
    		this.attribute14=new ASNOctetString2(attribute14);    		
    	
    	if(attribute15!=null)
    		this.attribute15=new ASNOctetString2(attribute15);    		
    	
    	if(attribute16!=null)
    		this.attribute16=new ASNOctetString2(attribute16);    		
    	
    	if(attribute17!=null)
    		this.attribute17=new ASNOctetString2(attribute17);    		
    	
    	if(attribute18!=null)
    		this.attribute18=new ASNOctetString2(attribute18);    		
    	
    	if(attribute19!=null)
    		this.attribute19=new ASNOctetString2(attribute19);    		
    	
    	if(attribute20!=null)
    		this.attribute20=new ASNOctetString2(attribute20);    		
    	
    	if(attribute21!=null)
    		this.attribute21=new ASNOctetString2(attribute21);    		
    	
    	if(attribute22!=null)
    		this.attribute22=new ASNOctetString2(attribute22);    		
    	
    	if(attribute23!=null)
    		this.attribute23=new ASNOctetString2(attribute23);    		
    	
    	if(attribute24!=null)
    		this.attribute24=new ASNOctetString2(attribute24);    		
    	
    	if(attribute25!=null)
    		this.attribute25=new ASNOctetString2(attribute25);    		
    	
    	if(attribute26!=null)
    		this.attribute26=new ASNOctetString2(attribute26);    		
    	
    	if(attribute27!=null)
    		this.attribute27=new ASNOctetString2(attribute27);    		
    	
    	if(attribute28!=null)
    		this.attribute28=new ASNOctetString2(attribute28);    		
    	
    	if(attribute29!=null)
    		this.attribute29=new ASNOctetString2(attribute29);    		
    	
    	if(attribute30!=null)
    		this.attribute30=new ASNOctetString2(attribute30);    
    }

    public ByteBuf getAttribute0() {
    	if(attribute0==null)
    		return null;
    	
    	return attribute0.getValue();
    }

    public ByteBuf getAttribute1() {
    	if(attribute1==null)
    		return null;
    	
    	return attribute1.getValue();
    }

    public ByteBuf getAttribute2() {
    	if(attribute2==null)
    		return null;
    	
    	return attribute2.getValue();
    }

    public ByteBuf getAttribute3() {
    	if(attribute3==null)
    		return null;
    	
    	return attribute3.getValue();
    }

    public ByteBuf getAttribute4() {
    	if(attribute4==null)
    		return null;
    	
    	return attribute4.getValue();
    }

    public ByteBuf getAttribute5() {
    	if(attribute5==null)
    		return null;
    	
    	return attribute5.getValue();
    }

    public ByteBuf getAttribute6() {
    	if(attribute6==null)
    		return null;
    	
    	return attribute6.getValue();
    }

    public ByteBuf getAttribute7() {
    	if(attribute7==null)
    		return null;
    	
    	return attribute7.getValue();
    }

    public ByteBuf getAttribute8() {
    	if(attribute8==null)
    		return null;
    	
    	return attribute8.getValue();
    }

    public ByteBuf getAttribute9() {
    	if(attribute9==null)
    		return null;
    	
    	return attribute9.getValue();
    }

    public ByteBuf getAttribute10() {
    	if(attribute10==null)
    		return null;
    	
    	return attribute10.getValue();
    }

    public ByteBuf getAttribute11() {
    	if(attribute11==null)
    		return null;
    	
    	return attribute11.getValue();
    }

    public ByteBuf getAttribute12() {
    	if(attribute12==null)
    		return null;
    	
    	return attribute12.getValue();
    }

    public ByteBuf getAttribute13() {
    	if(attribute13==null)
    		return null;
    	
    	return attribute13.getValue();
    }

    public ByteBuf getAttribute14() {
    	if(attribute14==null)
    		return null;
    	
    	return attribute14.getValue();
    }

    public ByteBuf getAttribute15() {
    	if(attribute15==null)
    		return null;
    	
    	return attribute15.getValue();
    }

    public ByteBuf getAttribute16() {
    	if(attribute16==null)
    		return null;
    	
    	return attribute16.getValue();
    }

    public ByteBuf getAttribute17() {
    	if(attribute17==null)
    		return null;
    	
    	return attribute17.getValue();
    }

    public ByteBuf getAttribute18() {
    	if(attribute18==null)
    		return null;
    	
    	return attribute18.getValue();
    }

    public ByteBuf getAttribute19() {
    	if(attribute19==null)
    		return null;
    	
    	return attribute19.getValue();
    }

    public ByteBuf getAttribute20() {
    	if(attribute20==null)
    		return null;
    	
    	return attribute20.getValue();
    }

    public ByteBuf getAttribute21() {
    	if(attribute21==null)
    		return null;
    	
    	return attribute21.getValue();
    }

    public ByteBuf getAttribute22() {
    	if(attribute22==null)
    		return null;
    	
    	return attribute22.getValue();
    }

    public ByteBuf getAttribute23() {
    	if(attribute23==null)
    		return null;
    	
    	return attribute23.getValue();
    }

    public ByteBuf getAttribute24() {
    	if(attribute24==null)
    		return null;
    	
    	return attribute24.getValue();
    }

    public ByteBuf getAttribute25() {
    	if(attribute25==null)
    		return null;
    	
    	return attribute25.getValue();
    }

    public ByteBuf getAttribute26() {
    	if(attribute26==null)
    		return null;
    	
    	return attribute26.getValue();
    }

    public ByteBuf getAttribute27() {
    	if(attribute27==null)
    		return null;
    	
    	return attribute27.getValue();
    }

    public ByteBuf getAttribute28() {
    	if(attribute28==null)
    		return null;
    	
    	return attribute28.getValue();
    }

    public ByteBuf getAttribute29() {
    	if(attribute29==null)
    		return null;
    	
    	return attribute29.getValue();
    }

    public ByteBuf getAttribute30() {
    	if(attribute30==null)
    		return null;
    	
    	return attribute30.getValue();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DataItemID [");

        if (this.attribute0 != null && this.attribute0.getValue()!=null) {
            sb.append(", attribute0=");
            sb.append(attribute0.printDataArr());
        }

        if (this.attribute1 != null && this.attribute1.getValue()!=null) {
            sb.append(", attribute1=");
            sb.append(attribute1.printDataArr());
        }
        
        if (this.attribute2 != null && this.attribute2.getValue()!=null) {
            sb.append(", attribute2=");
            sb.append(attribute2.printDataArr());
        }

        if (this.attribute3 != null && this.attribute3.getValue()!=null) {
            sb.append(", attribute3=");
            sb.append(attribute3.printDataArr());
        }
        
        if (this.attribute4 != null && this.attribute4.getValue()!=null) {
            sb.append(", attribute4=");
            sb.append(attribute4.printDataArr());
        }

        if (this.attribute5 != null && this.attribute5.getValue()!=null) {
            sb.append(", attribute5=");
            sb.append(attribute5.printDataArr());
        }
        
        if (this.attribute6 != null && this.attribute6.getValue()!=null) {
            sb.append(", attribute6=");
            sb.append(attribute6.printDataArr());
        }

        if (this.attribute7 != null && this.attribute7.getValue()!=null) {
            sb.append(", attribute7=");
            sb.append(attribute7.printDataArr());
        }

        if (this.attribute8 != null && this.attribute8.getValue()!=null) {
            sb.append(", attribute8=");
            sb.append(attribute8.printDataArr());
        }
        
        if (this.attribute9 != null && this.attribute9.getValue()!=null) {
            sb.append(", attribute9=");
            sb.append(attribute9.printDataArr());
        }

        if (this.attribute10 != null && this.attribute10.getValue()!=null) {
            sb.append(", attribute10=");
            sb.append(attribute10.printDataArr());
        }

        if (this.attribute11 != null && this.attribute11.getValue()!=null) {
            sb.append(", attribute11=");
            sb.append(attribute11.printDataArr());
        }
        
        if (this.attribute12 != null && this.attribute12.getValue()!=null) {
            sb.append(", attribute12=");
            sb.append(attribute12.printDataArr());
        }

        if (this.attribute13 != null && this.attribute13.getValue()!=null) {
            sb.append(", attribute13=");
            sb.append(attribute13.printDataArr());
        }
        
        if (this.attribute14 != null && this.attribute14.getValue()!=null) {
            sb.append(", attribute14=");
            sb.append(attribute14.printDataArr());
        }

        if (this.attribute15 != null && this.attribute15.getValue()!=null) {
            sb.append(", attribute15=");
            sb.append(attribute15.printDataArr());
        }
        
        if (this.attribute16 != null && this.attribute16.getValue()!=null) {
            sb.append(", attribute16=");
            sb.append(attribute16.printDataArr());
        }

        if (this.attribute17 != null && this.attribute17.getValue()!=null) {
            sb.append(", attribute17=");
            sb.append(attribute17.printDataArr());
        }

        if (this.attribute18 != null && this.attribute18.getValue()!=null) {
            sb.append(", attribute18=");
            sb.append(attribute18.printDataArr());
        }
        
        if (this.attribute19 != null && this.attribute19.getValue()!=null) {
            sb.append(", attribute19=");
            sb.append(attribute19.printDataArr());
        }

        if (this.attribute20 != null && this.attribute20.getValue()!=null) {
            sb.append(", attribute20=");
            sb.append(attribute20.printDataArr());
        }

        if (this.attribute21 != null && this.attribute21.getValue()!=null) {
            sb.append(", attribute21=");
            sb.append(attribute21.printDataArr());
        }
        
        if (this.attribute22 != null && this.attribute22.getValue()!=null) {
            sb.append(", attribute22=");
            sb.append(attribute22.printDataArr());
        }

        if (this.attribute23 != null && this.attribute23.getValue()!=null) {
            sb.append(", attribute23=");
            sb.append(attribute23.printDataArr());
        }
        
        if (this.attribute24 != null && this.attribute24.getValue()!=null) {
            sb.append(", attribute24=");
            sb.append(attribute24.printDataArr());
        }

        if (this.attribute25 != null && this.attribute25.getValue()!=null) {
            sb.append(", attribute25=");
            sb.append(attribute25.printDataArr());
        }
        
        if (this.attribute26 != null && this.attribute26.getValue()!=null) {
            sb.append(", attribute26=");
            sb.append(attribute26.printDataArr());
        }

        if (this.attribute27 != null && this.attribute27.getValue()!=null) {
            sb.append(", attribute27=");
            sb.append(attribute27.printDataArr());
        }

        if (this.attribute28 != null && this.attribute28.getValue()!=null) {
            sb.append(", attribute28=");
            sb.append(attribute28.printDataArr());
        }
        
        if (this.attribute29 != null && this.attribute29.getValue()!=null) {
            sb.append(", attribute29=");
            sb.append(attribute29.printDataArr());
        }

        if (this.attribute30 != null && this.attribute30.getValue()!=null) {
            sb.append(", attribute30=");
            sb.append(attribute30.printDataArr());
        }
        
        sb.append("]");

        return sb.toString();
    }
}