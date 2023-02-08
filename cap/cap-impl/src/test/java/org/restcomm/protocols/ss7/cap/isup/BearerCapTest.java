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

package org.restcomm.protocols.ss7.cap.isup;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.isup.BearerIsupImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.UserServiceInformationImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class BearerCapTest {

    public byte[] getData() {
        return new byte[] { 4, 3, (byte) 128, (byte) 144, (byte) 163 };
    }

    public byte[] getIntData() {
        return new byte[] { (byte) 128, (byte) 144, (byte) 163 };
    }

    @Test(groups = { "functional.decode", "isup" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(BearerIsupImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof BearerIsupImpl);
        
        BearerIsupImpl elem = (BearerIsupImpl)result.getResult();        
        assertTrue(ByteBufUtil.equals(elem.getValue(), Unpooled.wrappedBuffer(this.getIntData())));
        //UserServiceInformation usi = elem.getUserServiceInformation();
        
        // TODO: implement UserServiceInformation (ISUP) and then implement CAP unit tests for UserServiceInformation usi

        // assertEquals(ci.getCodingStandard(), 0);
    }

    @Test(groups = { "functional.encode", "isup" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(BearerIsupImpl.class);
    	
    	UserServiceInformationImpl usi=new UserServiceInformationImpl(Unpooled.wrappedBuffer(this.getIntData()));
        BearerIsupImpl elem = new BearerIsupImpl(usi);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // TODO: implement UserServiceInformation (ISUP) and then implement CAP unit tests for UserServiceInformation usi

        // UserServiceInformation usi = new UserServiceInformationImpl(cdata);
        // elem = new BearerCapImpl(usi);
        // elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 0);
        // assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));
    }
}
