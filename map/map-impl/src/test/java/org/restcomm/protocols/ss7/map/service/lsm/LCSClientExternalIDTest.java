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

package org.restcomm.protocols.ss7.map.service.lsm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.junit.AfterClass;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * //TODO add ExtensionContainer and test
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class LCSClientExternalIDTest {

    MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    public byte[] getData() {
        return new byte[] { 48, 0x07, (byte) 0x80, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x70 };
    }

    public byte[] getDataFull() {
        return new byte[] { 48, 48, -128, 5, -111, 85, 22, 9, 112, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14,
                15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LCSClientExternalIDImpl.class);
    	
        byte[] data = getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LCSClientExternalIDImpl);
        LCSClientExternalIDImpl lcsClientExterId = (LCSClientExternalIDImpl)result.getResult();

        assertNotNull(lcsClientExterId.getExternalAddress());
        assertEquals(lcsClientExterId.getExternalAddress().getAddress(), "55619007");
        assertNull(lcsClientExterId.getExtensionContainer());

        data = getDataFull();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LCSClientExternalIDImpl);
        lcsClientExterId = (LCSClientExternalIDImpl)result.getResult();
        
        assertNotNull(lcsClientExterId.getExternalAddress());
        assertEquals(lcsClientExterId.getExternalAddress().getAddress(), "55619007");
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(lcsClientExterId.getExtensionContainer()));
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LCSClientExternalIDImpl.class);
    	
        ISDNAddressString externalAddress = MAPParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "55619007");
        LCSClientExternalIDImpl lcsClientExterId = new LCSClientExternalIDImpl(externalAddress, null);
        byte[] data = getData();
        ByteBuf buffer=parser.encode(lcsClientExterId);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        lcsClientExterId = new LCSClientExternalIDImpl(externalAddress, MAPExtensionContainerTest.GetTestExtensionContainer());
        data = getDataFull();
        buffer=parser.encode(lcsClientExterId);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}