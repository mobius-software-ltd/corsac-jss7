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

package org.restcomm.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * TODO Self generated trace. Please test from real trace
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class AdditionalNumberTest {
    MAPParameterFactory MAPParameterFactory = null;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
        MAPParameterFactory = new MAPParameterFactoryImpl();
    }

    @AfterTest
    public void tearDown() {
    }

    public byte[] getEncodedMscNumber() {
        return new byte[] { 48, 7, -128, 5, -111, 85, 22, 9, 112 };
    }

    public byte[] getEncodedSgsnNumber() {
        return new byte[] { 48, 7, -127, 5, -111, 85, 22, 9, 112 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AdditionalNumberImpl.class);
    	
        byte[] data = getEncodedMscNumber();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AdditionalNumberImpl);
        AdditionalNumberImpl addNum = (AdditionalNumberImpl)result.getResult();

        assertNotNull(addNum.getMSCNumber());
        assertNull(addNum.getSGSNNumber());
        ISDNAddressString isdnAdd = addNum.getMSCNumber();
        assertEquals(isdnAdd.getAddressNature(), AddressNature.international_number);
        assertEquals(isdnAdd.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(isdnAdd.getAddress().equals("55619007"));

        data = getEncodedSgsnNumber();

        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AdditionalNumberImpl);
        addNum = (AdditionalNumberImpl)result.getResult();
        
        assertNull(addNum.getMSCNumber());
        assertNotNull(addNum.getSGSNNumber());
        isdnAdd = addNum.getSGSNNumber();
        assertEquals(isdnAdd.getAddressNature(), AddressNature.international_number);
        assertEquals(isdnAdd.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(isdnAdd.getAddress().equals("55619007"));
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AdditionalNumberImpl.class);
    	
        ISDNAddressString isdnAdd = MAPParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "55619007");
        AdditionalNumberImpl addNum = new AdditionalNumberImpl(isdnAdd, null);

        byte[] data = getEncodedMscNumber();
        ByteBuf buffer=parser.encode(addNum);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        addNum = new AdditionalNumberImpl(null, isdnAdd);

        data = getEncodedSgsnNumber();
        buffer=parser.encode(addNum);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}