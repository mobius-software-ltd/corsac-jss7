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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientName;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
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
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class LCSClientIDTest {
    MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    public byte[] getData() {
        return new byte[] { 48, 27, -128, 1, 2, -125, 1, 0, -92, 19, -128, 1, 15, -126, 14, 110, 114, -5, 28, -122, -61, 101, 110, 114, -5, 28, -122, -61, 101 };
    }

    public byte[] getDataFull() {
        return new byte[] { 48, 70, -128, 1, 2, -95, 7, -128, 5, -111, 68, 51, 34, 17, -126, 6, -111, 85, 68, 51, 34, 17,
                -125, 1, 0, -92, 19, -128, 1, 15, -126, 14, 110, 114, -5, 28, -122, -61, 101, 110, 114, -5, 28, -122, -61, 101,
                -123, 3, 2, 11, 12, -90, 19, -128, 1, 15, -127, 14, 110, 114, -5, 28, -122, -61, 101, 110, 114, -5, 28, -122, -61,
                101 };
    }

    public byte[] getDataAPN() {
        return new byte[] { 11, 12 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LCSClientIDImpl.class);
    	
        byte[] data = getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LCSClientIDImpl);
        LCSClientIDImpl lcsClientID = (LCSClientIDImpl)result.getResult();
        
        assertNotNull(lcsClientID.getLCSClientType());
        assertEquals(lcsClientID.getLCSClientType(), LCSClientType.plmnOperatorServices);

        assertNotNull(lcsClientID.getLCSClientInternalID());
        assertEquals(lcsClientID.getLCSClientInternalID(), LCSClientInternalID.broadcastService);

        assertNull(lcsClientID.getLCSClientExternalID());
        assertNull(lcsClientID.getLCSClientDialedByMS());
        assertNull(lcsClientID.getLCSAPN());
        assertNull(lcsClientID.getLCSRequestorID());

        LCSClientName lcsClientName = lcsClientID.getLCSClientName();
        assertNotNull(lcsClientName);
        assertEquals(lcsClientName.getDataCodingScheme().getCode(), 0x0f);
        USSDString nameString = lcsClientName.getNameString();
        assertTrue(nameString.getString(null).equals("ndmgapp2ndmgapp2"));

        data = getDataFull();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LCSClientIDImpl);
        lcsClientID = (LCSClientIDImpl)result.getResult();

        assertNotNull(lcsClientID.getLCSClientType());
        assertEquals(lcsClientID.getLCSClientType(), LCSClientType.plmnOperatorServices);

        assertNotNull(lcsClientID.getLCSClientInternalID());
        assertEquals(lcsClientID.getLCSClientInternalID(), LCSClientInternalID.broadcastService);

        lcsClientName = lcsClientID.getLCSClientName();
        assertNotNull(lcsClientName);
        assertEquals(lcsClientName.getDataCodingScheme().getCode(), 0x0f);
        nameString = lcsClientName.getNameString();
        assertEquals(nameString.getString(null), "ndmgapp2ndmgapp2");

        assertTrue(lcsClientID.getLCSClientExternalID().getExternalAddress().getAddress().equals("44332211"));
        assertTrue(lcsClientID.getLCSClientDialedByMS().getAddress().equals("5544332211"));
        assertEquals(lcsClientID.getLCSAPN().getApn(), new String(getDataAPN()));
        assertEquals(lcsClientID.getLCSRequestorID().getDataCodingScheme().getCode(), 0x0f);
        assertTrue(lcsClientID.getLCSRequestorID().getRequestorIDString().getString(null).equals("ndmgapp2ndmgapp2"));
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LCSClientIDImpl.class);
    	
        byte[] data = getData();

        USSDString nameString = MAPParameterFactory.createUSSDString("ndmgapp2ndmgapp2");
        LCSClientNameImpl lcsClientName = new LCSClientNameImpl(new CBSDataCodingSchemeImpl(0x0f), nameString, null);
        LCSClientIDImpl lcsClientID = new LCSClientIDImpl(LCSClientType.plmnOperatorServices, null,LCSClientInternalID.broadcastService, lcsClientName, null, null, null);
        ByteBuf buffer=parser.encode(lcsClientID);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        data = getDataFull();

        ISDNAddressStringImpl externalAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,"44332211");
        LCSClientExternalIDImpl extId = new LCSClientExternalIDImpl(externalAddress, null);
        AddressStringImpl clientDialedByMS = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,"5544332211");
        APNImpl apn = new APNImpl(new String(getDataAPN()));
        LCSRequestorIDImpl reqId = new LCSRequestorIDImpl(new CBSDataCodingSchemeImpl(0x0f), nameString, null);
        lcsClientID = new LCSClientIDImpl(LCSClientType.plmnOperatorServices, extId, LCSClientInternalID.broadcastService,lcsClientName, clientDialedByMS, apn, reqId);
        buffer=parser.encode(lcsClientID);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}