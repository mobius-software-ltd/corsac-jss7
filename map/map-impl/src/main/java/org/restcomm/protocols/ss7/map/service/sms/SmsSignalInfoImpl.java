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

package org.restcomm.protocols.ss7.map.service.sms;

import java.nio.charset.Charset;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsTpdu;
import org.restcomm.protocols.ss7.map.smstpdu.SmsTpduImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public class SmsSignalInfoImpl extends ASNOctetString implements SmsSignalInfo {
	private Charset gsm8Charset;

    public SmsSignalInfoImpl() {
    	super("SmsSignalInfo",1,200,false);
    }

    public SmsSignalInfoImpl(ByteBuf buffer) {
    	super(buffer,"SmsSignalInfo",1,200,false);
    }
    
    public SmsSignalInfoImpl(SmsTpdu tpdu, Charset gsm8Charset) throws MAPException {
    	super(translate(tpdu, gsm8Charset),"SmsSignalInfo",1,200,false);
    	this.setGsm8Charset(gsm8Charset);        
    }
    
    public static ByteBuf translate(SmsTpdu tpdu, Charset gsm8Charset) throws MAPException {
        if (tpdu == null)
            throw new MAPException("SmsTpdu must not be null");

        ByteBuf value=Unpooled.buffer();
        tpdu.encodeData(value);
        return value;
    }

    public Charset getGsm8Charset() {
        return gsm8Charset;
    }

    public void setGsm8Charset(Charset gsm8Charset) {
        this.gsm8Charset = gsm8Charset;
    }

    public SmsTpduImpl decodeTpdu(boolean mobileOriginatedMessage) throws MAPException {
    	ByteBuf buffer=getValue();
    	buffer.retain();
        return SmsTpduImpl.createInstance(buffer, mobileOriginatedMessage, this.getGsm8Charset());
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("SmsSignalInfo [");

        boolean moExists = false;
        try {
        	ByteBuf buffer=getValue();
        	buffer.retain();
            SmsTpduImpl tpdu = SmsTpduImpl.createInstance(buffer, true, getGsm8Charset());
            sb.append("MO case: ");
            sb.append(tpdu.toString());
            moExists = true;
        } catch (MAPException e) {
        }
        try {
            if (moExists)
                sb.append("\n");
            
            ByteBuf buffer=getValue();
        	buffer.retain();
            SmsTpduImpl tpdu = SmsTpduImpl.createInstance(buffer, false, getGsm8Charset());
            sb.append("MT case: ");
            sb.append(tpdu.toString());
        } catch (MAPException e) {
        }

        sb.append("]");

        return sb.toString();
    }
}
