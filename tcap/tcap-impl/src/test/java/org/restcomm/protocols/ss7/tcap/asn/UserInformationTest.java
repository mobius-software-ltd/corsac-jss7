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

package org.restcomm.protocols.ss7.tcap.asn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNException;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class UserInformationTest {

	ASNParser parser=new ASNParser();
    
	@BeforeClass
	public void setUp()
	{
		parser.loadClass(UserInformationImpl.class);
        
		parser.clearClassMapping(ASNUserInformationObjectImpl.class);
        parser.registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, UserInformationTestASN.class);		
	}
	
    @Test(groups = { "functional.decode" })
    public void testUserInformationDecode() throws Exception {

        // This raw data is from wireshark trace of TCAP - MAP
        byte[] data = new byte[] { (byte) 0xbe, 0x25, 0x28, 0x23, 0x06, 0x07, 0x04, 0x00, 0x00, 0x01, 0x01, 0x01, 0x01,
                (byte) 0xa0, 0x18, (byte) 0xa0, (byte) 0x80, (byte) 0x80, 0x09, (byte) 0x96, 0x02, 0x24, (byte) 0x80, 0x03,
                0x00, (byte) 0x80, 0x00, (byte) 0xf2, (byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26, (byte) 0x98, (byte) 0x86,
                0x03, (byte) 0xf0, 0x00, 0x00 };

        ByteBuf buffer=Unpooled.wrappedBuffer(data);
        Object output=parser.decode(buffer).getResult();
        UserInformationImpl userInformation = (UserInformationImpl)output;
        
        assertTrue(userInformation.isIDObjectIdentifier());

        List<Long> ids=Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 1L, 1L, 1L });
        assertEquals(ids, userInformation.getObjectIdentifier());

        assertFalse(userInformation.isIDIndirect());

        assertTrue(userInformation.isValueObject());
        
        ByteBuf innerBuffer=parser.encode(userInformation.getChild());
        byte[] outputData=innerBuffer.array();
        byte[] expectedData=new byte[] { (byte) 0xa0, (byte) 0x80, (byte) 0x80, 0x09, (byte) 0x96, 0x02, 0x24,
                (byte) 0x80, 0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2, (byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26,
                (byte) 0x98, (byte) 0x86, 0x03, (byte) 0xf0, 0x00, 0x00 };
        assertTrue(Arrays.equals(expectedData, outputData));

    }

    @Test(groups = { "functional.encode" })
    public void testUserInformationEncode() throws IOException, EncodeException {

    	byte[] encodedData = new byte[] { (byte) 0xbe, 0x25, 0x28, 0x23, 0x06, 0x07, 0x04, 0x00, 0x00, 0x01, 0x01, 0x01, 0x01,
                (byte) 0xa0, 0x18, (byte) 0xa0, (byte) 0x80, (byte) 0x80, 0x09, (byte) 0x96, 0x02, 0x24, (byte) 0x80, 0x03,
                0x00, (byte) 0x80, 0x00, (byte) 0xf2, (byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26, (byte) 0x98, (byte) 0x86,
                0x03, (byte) 0xf0, 0x00, 0x00 };

        UserInformationImpl userInformation = new UserInformationImpl();
        userInformation.setIdentifier(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 1L, 1L, 1L }));

        UserInformationTestASN demo=new UserInformationTestASN(Unpooled.wrappedBuffer(new byte[] { (byte) 0x80, 0x09, (byte) 0x96, 0x02, 0x24,
                (byte) 0x80, 0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2, (byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26,
                (byte) 0x98, (byte) 0x86, 0x03, (byte) 0xf0 }));
               
        userInformation.setChildAsObject(demo);
        ByteBuf data=null;
        try {
        	data=parser.encode(userInformation);
        }
        catch(ASNException ex) {
        	
        }

        assertFalse(data==null);
        byte[] userInfData = data.array();

        TcBeginTest.dump(userInfData, userInfData.length, false);
        assertTrue(Arrays.equals(encodedData, userInfData));

    }

    public static final List<Long> _ACN_ = Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 19L, 2L });

    @Test(groups = { "functional.encode", "functional.decode" })
    public void testFailuuure() throws Exception {
    	
    	byte[] encoded = new byte[] { -66, 15, 40, 13, 6, 7, 4, 0, 0, 1, 0, 19, 2, -126, 2, 4, -112 };

        UserInformationImpl _ui = new UserInformationImpl();
        ASNBitString bitString=new ASNBitString(null);
        bitString.setBit(0);
        bitString.setBit(3);
        _ui.setChild(bitString);
        _ui.setIdentifier(_ACN_);
        ByteBuf data=parser.encode(_ui);
        byte[] buf = data.array();
        assertTrue(Arrays.equals(encoded, buf));

        Object value=parser.decode(Unpooled.wrappedBuffer(buf)).getResult();
        UserInformationImpl _ui2 = (UserInformationImpl)value;
        assertTrue(_ui2.isIDObjectIdentifier());
        assertTrue(_ui2.isValueBitString());
        assertEquals(_ACN_, _ui2.getObjectIdentifier());
        ASNBitString bs2 = _ui2.getBitString();
        assertTrue(bs2.isBitSet(0));
        assertFalse(bs2.isBitSet(1));
        assertFalse(bs2.isBitSet(2));
        assertTrue(bs2.isBitSet(3));
    }
}
