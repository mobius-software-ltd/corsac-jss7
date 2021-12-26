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

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.DataItemInformation;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class DataItemInformationImpl implements DataItemInformation {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNOctetString attribute0;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNOctetString attribute1;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false, index=-1)
    private ASNOctetString attribute2;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false, index=-1)
    private ASNOctetString attribute3;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false, index=-1)
    private ASNOctetString attribute4;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false, index=-1)
    private ASNOctetString attribute5;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false, index=-1)
    private ASNOctetString attribute6;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = false, index=-1)
    private ASNOctetString attribute7;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = false, index=-1)
    private ASNOctetString attribute8;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 9,constructed = false, index=-1)
    private ASNOctetString attribute9;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = false, index=-1)
    private ASNOctetString attribute10;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 11,constructed = false, index=-1)
    private ASNOctetString attribute11;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 12,constructed = false, index=-1)
    private ASNOctetString attribute12;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 13,constructed = false, index=-1)
    private ASNOctetString attribute13;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 14,constructed = false, index=-1)
    private ASNOctetString attribute14;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 15,constructed = false, index=-1)
    private ASNOctetString attribute15;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 16,constructed = false, index=-1)
    private ASNOctetString attribute16;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 17,constructed = false, index=-1)
    private ASNOctetString attribute17;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 18,constructed = false, index=-1)
    private ASNOctetString attribute18;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 19,constructed = false, index=-1)
    private ASNOctetString attribute19;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 20,constructed = false, index=-1)
    private ASNOctetString attribute20;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 21,constructed = false, index=-1)
    private ASNOctetString attribute21;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 22,constructed = false, index=-1)
    private ASNOctetString attribute22;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 23,constructed = false, index=-1)
    private ASNOctetString attribute23;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 24,constructed = false, index=-1)
    private ASNOctetString attribute24;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 25,constructed = false, index=-1)
    private ASNOctetString attribute25;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 26,constructed = false, index=-1)
    private ASNOctetString attribute26;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 27,constructed = false, index=-1)
    private ASNOctetString attribute27;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 28,constructed = false, index=-1)
    private ASNOctetString attribute28;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 29,constructed = false, index=-1)
    private ASNOctetString attribute29;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 30,constructed = false, index=-1)
    private ASNOctetString attribute30;
	
	public DataItemInformationImpl() {
    }

    public DataItemInformationImpl(byte[] attribute0,byte[] attribute1,byte[] attribute2,byte[] attribute3,
    		byte[] attribute4,byte[] attribute5,byte[] attribute6,byte[] attribute7,byte[] attribute8,
    		byte[] attribute9,byte[] attribute10,byte[] attribute11,byte[] attribute12,byte[] attribute13,
    		byte[] attribute14,byte[] attribute15,byte[] attribute16,byte[] attribute17,byte[] attribute18,
    		byte[] attribute19,byte[] attribute20,byte[] attribute21,byte[] attribute22,byte[] attribute23,
    		byte[] attribute24,byte[] attribute25,byte[] attribute26,byte[] attribute27,byte[] attribute28,
    		byte[] attribute29,byte[] attribute30) {
    	
    	if(attribute0!=null) {
    		this.attribute0=new ASNOctetString();
    		this.attribute0.setValue(Unpooled.wrappedBuffer(attribute0));    		
    	}
    	
    	if(attribute1!=null) {
    		this.attribute1=new ASNOctetString();
    		this.attribute1.setValue(Unpooled.wrappedBuffer(attribute1));    		
    	}
    	
    	if(attribute2!=null) {
    		this.attribute2=new ASNOctetString();
    		this.attribute2.setValue(Unpooled.wrappedBuffer(attribute2));    		
    	}
    	
    	if(attribute3!=null) {
    		this.attribute3=new ASNOctetString();
    		this.attribute3.setValue(Unpooled.wrappedBuffer(attribute3));    		
    	}
    	
    	if(attribute4!=null) {
    		this.attribute4=new ASNOctetString();
    		this.attribute4.setValue(Unpooled.wrappedBuffer(attribute4));    		
    	}
    	
    	if(attribute5!=null) {
    		this.attribute5=new ASNOctetString();
    		this.attribute5.setValue(Unpooled.wrappedBuffer(attribute5));    		
    	}
    	
    	if(attribute6!=null) {
    		this.attribute6=new ASNOctetString();
    		this.attribute6.setValue(Unpooled.wrappedBuffer(attribute6));    		
    	}
    	
    	if(attribute7!=null) {
    		this.attribute7=new ASNOctetString();
    		this.attribute7.setValue(Unpooled.wrappedBuffer(attribute7));    		
    	}
    	
    	if(attribute8!=null) {
    		this.attribute8=new ASNOctetString();
    		this.attribute8.setValue(Unpooled.wrappedBuffer(attribute8));    		
    	}
    	
    	if(attribute9!=null) {
    		this.attribute9=new ASNOctetString();
    		this.attribute9.setValue(Unpooled.wrappedBuffer(attribute9));    		
    	}
    	
    	if(attribute10!=null) {
    		this.attribute10=new ASNOctetString();
    		this.attribute10.setValue(Unpooled.wrappedBuffer(attribute10));    		
    	}
    	
    	if(attribute11!=null) {
    		this.attribute11=new ASNOctetString();
    		this.attribute11.setValue(Unpooled.wrappedBuffer(attribute11));    		
    	}
    	
    	if(attribute12!=null) {
    		this.attribute12=new ASNOctetString();
    		this.attribute12.setValue(Unpooled.wrappedBuffer(attribute12));    		
    	}
    	
    	if(attribute13!=null) {
    		this.attribute13=new ASNOctetString();
    		this.attribute13.setValue(Unpooled.wrappedBuffer(attribute13));    		
    	}
    	
    	if(attribute14!=null) {
    		this.attribute14=new ASNOctetString();
    		this.attribute14.setValue(Unpooled.wrappedBuffer(attribute14));    		
    	}
    	
    	if(attribute15!=null) {
    		this.attribute15=new ASNOctetString();
    		this.attribute15.setValue(Unpooled.wrappedBuffer(attribute15));    		
    	}
    	
    	if(attribute16!=null) {
    		this.attribute16=new ASNOctetString();
    		this.attribute16.setValue(Unpooled.wrappedBuffer(attribute16));    		
    	}
    	
    	if(attribute17!=null) {
    		this.attribute17=new ASNOctetString();
    		this.attribute17.setValue(Unpooled.wrappedBuffer(attribute17));    		
    	}
    	
    	if(attribute18!=null) {
    		this.attribute18=new ASNOctetString();
    		this.attribute18.setValue(Unpooled.wrappedBuffer(attribute18));    		
    	}
    	
    	if(attribute19!=null) {
    		this.attribute19=new ASNOctetString();
    		this.attribute19.setValue(Unpooled.wrappedBuffer(attribute19));    		
    	}
    	
    	if(attribute20!=null) {
    		this.attribute20=new ASNOctetString();
    		this.attribute20.setValue(Unpooled.wrappedBuffer(attribute20));    		
    	}
    	
    	if(attribute21!=null) {
    		this.attribute21=new ASNOctetString();
    		this.attribute21.setValue(Unpooled.wrappedBuffer(attribute21));    		
    	}
    	
    	if(attribute22!=null) {
    		this.attribute22=new ASNOctetString();
    		this.attribute22.setValue(Unpooled.wrappedBuffer(attribute22));    		
    	}
    	
    	if(attribute23!=null) {
    		this.attribute23=new ASNOctetString();
    		this.attribute23.setValue(Unpooled.wrappedBuffer(attribute23));    		
    	}
    	
    	if(attribute24!=null) {
    		this.attribute24=new ASNOctetString();
    		this.attribute24.setValue(Unpooled.wrappedBuffer(attribute24));    		
    	}
    	
    	if(attribute25!=null) {
    		this.attribute25=new ASNOctetString();
    		this.attribute25.setValue(Unpooled.wrappedBuffer(attribute25));    		
    	}
    	
    	if(attribute26!=null) {
    		this.attribute26=new ASNOctetString();
    		this.attribute26.setValue(Unpooled.wrappedBuffer(attribute26));    		
    	}
    	
    	if(attribute27!=null) {
    		this.attribute27=new ASNOctetString();
    		this.attribute27.setValue(Unpooled.wrappedBuffer(attribute27));    		
    	}
    	
    	if(attribute28!=null) {
    		this.attribute28=new ASNOctetString();
    		this.attribute28.setValue(Unpooled.wrappedBuffer(attribute28));    		
    	}
    	
    	if(attribute29!=null) {
    		this.attribute29=new ASNOctetString();
    		this.attribute29.setValue(Unpooled.wrappedBuffer(attribute29));    		
    	}
    	
    	if(attribute30!=null) {
    		this.attribute30=new ASNOctetString();
    		this.attribute30.setValue(Unpooled.wrappedBuffer(attribute30));    		
    	}
    }

    public byte[] getAttribute0() {
    	if(attribute0==null || attribute0.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute0.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute1() {
    	if(attribute1==null || attribute1.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute1.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute2() {
    	if(attribute2==null || attribute2.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute2.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute3() {
    	if(attribute3==null || attribute3.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute3.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute4() {
    	if(attribute4==null || attribute4.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute4.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute5() {
    	if(attribute5==null || attribute5.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute5.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute6() {
    	if(attribute6==null || attribute6.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute6.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute7() {
    	if(attribute7==null || attribute7.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute7.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute8() {
    	if(attribute8==null || attribute8.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute8.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute9() {
    	if(attribute9==null || attribute9.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute9.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute10() {
    	if(attribute10==null || attribute10.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute10.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute11() {
    	if(attribute11==null || attribute11.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute11.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute12() {
    	if(attribute12==null || attribute12.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute12.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute13() {
    	if(attribute13==null || attribute13.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute13.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute14() {
    	if(attribute14==null || attribute14.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute14.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute15() {
    	if(attribute15==null || attribute15.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute15.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute16() {
    	if(attribute16==null || attribute16.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute16.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute17() {
    	if(attribute17==null || attribute17.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute17.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute18() {
    	if(attribute18==null || attribute18.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute18.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute19() {
    	if(attribute19==null || attribute19.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute19.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute20() {
    	if(attribute20==null || attribute20.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute20.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute21() {
    	if(attribute21==null || attribute21.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute21.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute22() {
    	if(attribute22==null || attribute22.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute22.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute23() {
    	if(attribute23==null || attribute23.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute23.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute24() {
    	if(attribute24==null || attribute24.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute24.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute25() {
    	if(attribute25==null || attribute25.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute25.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute26() {
    	if(attribute26==null || attribute26.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute26.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute27() {
    	if(attribute27==null || attribute27.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute27.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute28() {
    	if(attribute28==null || attribute28.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute28.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute29() {
    	if(attribute29==null || attribute29.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute29.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getAttribute30() {
    	if(attribute30==null || attribute30.getValue()==null)
    		return null;
    	
    	ByteBuf value=attribute30.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DataItemID [");

        if (this.attribute0 != null && this.attribute0.getValue()!=null) {
            sb.append(", attribute0=");
            sb.append(ASNOctetString.printDataArr(getAttribute0()));
        }

        if (this.attribute1 != null && this.attribute1.getValue()!=null) {
            sb.append(", attribute1=");
            sb.append(ASNOctetString.printDataArr(getAttribute1()));
        }
        
        if (this.attribute2 != null && this.attribute2.getValue()!=null) {
            sb.append(", attribute2=");
            sb.append(ASNOctetString.printDataArr(getAttribute2()));
        }

        if (this.attribute3 != null && this.attribute3.getValue()!=null) {
            sb.append(", attribute3=");
            sb.append(ASNOctetString.printDataArr(getAttribute3()));
        }
        
        if (this.attribute4 != null && this.attribute4.getValue()!=null) {
            sb.append(", attribute4=");
            sb.append(ASNOctetString.printDataArr(getAttribute4()));
        }

        if (this.attribute5 != null && this.attribute5.getValue()!=null) {
            sb.append(", attribute5=");
            sb.append(ASNOctetString.printDataArr(getAttribute5()));
        }
        
        if (this.attribute6 != null && this.attribute6.getValue()!=null) {
            sb.append(", attribute6=");
            sb.append(ASNOctetString.printDataArr(getAttribute6()));
        }

        if (this.attribute7 != null && this.attribute7.getValue()!=null) {
            sb.append(", attribute7=");
            sb.append(ASNOctetString.printDataArr(getAttribute7()));
        }

        if (this.attribute8 != null && this.attribute8.getValue()!=null) {
            sb.append(", attribute8=");
            sb.append(ASNOctetString.printDataArr(getAttribute8()));
        }
        
        if (this.attribute9 != null && this.attribute9.getValue()!=null) {
            sb.append(", attribute9=");
            sb.append(ASNOctetString.printDataArr(getAttribute9()));
        }

        if (this.attribute10 != null && this.attribute10.getValue()!=null) {
            sb.append(", attribute10=");
            sb.append(ASNOctetString.printDataArr(getAttribute10()));
        }

        if (this.attribute11 != null && this.attribute11.getValue()!=null) {
            sb.append(", attribute11=");
            sb.append(ASNOctetString.printDataArr(getAttribute11()));
        }
        
        if (this.attribute12 != null && this.attribute12.getValue()!=null) {
            sb.append(", attribute12=");
            sb.append(ASNOctetString.printDataArr(getAttribute12()));
        }

        if (this.attribute13 != null && this.attribute13.getValue()!=null) {
            sb.append(", attribute13=");
            sb.append(ASNOctetString.printDataArr(getAttribute13()));
        }
        
        if (this.attribute14 != null && this.attribute14.getValue()!=null) {
            sb.append(", attribute14=");
            sb.append(ASNOctetString.printDataArr(getAttribute14()));
        }

        if (this.attribute15 != null && this.attribute15.getValue()!=null) {
            sb.append(", attribute15=");
            sb.append(ASNOctetString.printDataArr(getAttribute15()));
        }
        
        if (this.attribute16 != null && this.attribute16.getValue()!=null) {
            sb.append(", attribute16=");
            sb.append(ASNOctetString.printDataArr(getAttribute16()));
        }

        if (this.attribute17 != null && this.attribute17.getValue()!=null) {
            sb.append(", attribute17=");
            sb.append(ASNOctetString.printDataArr(getAttribute17()));
        }

        if (this.attribute18 != null && this.attribute18.getValue()!=null) {
            sb.append(", attribute18=");
            sb.append(ASNOctetString.printDataArr(getAttribute18()));
        }
        
        if (this.attribute19 != null && this.attribute19.getValue()!=null) {
            sb.append(", attribute19=");
            sb.append(ASNOctetString.printDataArr(getAttribute19()));
        }

        if (this.attribute20 != null && this.attribute20.getValue()!=null) {
            sb.append(", attribute20=");
            sb.append(ASNOctetString.printDataArr(getAttribute20()));
        }

        if (this.attribute21 != null && this.attribute21.getValue()!=null) {
            sb.append(", attribute21=");
            sb.append(ASNOctetString.printDataArr(getAttribute21()));
        }
        
        if (this.attribute22 != null && this.attribute22.getValue()!=null) {
            sb.append(", attribute22=");
            sb.append(ASNOctetString.printDataArr(getAttribute22()));
        }

        if (this.attribute23 != null && this.attribute23.getValue()!=null) {
            sb.append(", attribute23=");
            sb.append(ASNOctetString.printDataArr(getAttribute23()));
        }
        
        if (this.attribute24 != null && this.attribute24.getValue()!=null) {
            sb.append(", attribute24=");
            sb.append(ASNOctetString.printDataArr(getAttribute24()));
        }

        if (this.attribute25 != null && this.attribute25.getValue()!=null) {
            sb.append(", attribute25=");
            sb.append(ASNOctetString.printDataArr(getAttribute25()));
        }
        
        if (this.attribute26 != null && this.attribute26.getValue()!=null) {
            sb.append(", attribute26=");
            sb.append(ASNOctetString.printDataArr(getAttribute26()));
        }

        if (this.attribute27 != null && this.attribute27.getValue()!=null) {
            sb.append(", attribute27=");
            sb.append(ASNOctetString.printDataArr(getAttribute27()));
        }

        if (this.attribute28 != null && this.attribute28.getValue()!=null) {
            sb.append(", attribute28=");
            sb.append(ASNOctetString.printDataArr(getAttribute28()));
        }
        
        if (this.attribute29 != null && this.attribute29.getValue()!=null) {
            sb.append(", attribute29=");
            sb.append(ASNOctetString.printDataArr(getAttribute29()));
        }

        if (this.attribute30 != null && this.attribute30.getValue()!=null) {
            sb.append(", attribute30=");
            sb.append(ASNOctetString.printDataArr(getAttribute30()));
        }
        
        sb.append("]");

        return sb.toString();
    }
}