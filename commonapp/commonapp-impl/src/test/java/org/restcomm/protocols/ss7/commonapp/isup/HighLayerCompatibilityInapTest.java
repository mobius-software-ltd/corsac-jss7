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

package org.restcomm.protocols.ss7.commonapp.isup;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.restcomm.protocols.ss7.isup.impl.message.parameter.UserTeleserviceInformationImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class HighLayerCompatibilityInapTest {

    public byte[] getData() {
        return new byte[] { (byte) 151, 2, (byte) 145, (byte) 129 };
    }

    @Test(groups = { "functional.decode", "isup" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(HighLayerCompatibilityIsupImpl.class);
        ByteBuf data = Unpooled.wrappedBuffer(this.getData());
        ASNDecodeResult output=parser.decode(data);
        assertTrue(output.getResult() instanceof HighLayerCompatibilityIsupImpl);
        HighLayerCompatibilityIsupImpl elem = (HighLayerCompatibilityIsupImpl)output.getResult();        
        UserTeleserviceInformation hlc = elem.getHighLayerCompatibility();
        assertEquals(hlc.getCodingStandard(), 0);
        assertEquals(hlc.getEHighLayerCharIdentification(), 0);
        assertEquals(hlc.getEVideoTelephonyCharIdentification(), 0);
        assertEquals(hlc.getInterpretation(), 4);
        assertEquals(hlc.getPresentationMethod(), 1);
        assertEquals(hlc.getHighLayerCharIdentification(), 1);
    }

    @Test(groups = { "functional.encode", "isup" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(HighLayerCompatibilityIsupImpl.class);
        
    	HighLayerCompatibilityIsupImpl elem = new HighLayerCompatibilityIsupImpl(new UserTeleserviceInformationImpl(0, 4, 1, 1));
        ByteBuf data=parser.encode(elem);
        byte[] encodedData=new byte[data.readableBytes()];
        data.readBytes(encodedData);        
    }
}
