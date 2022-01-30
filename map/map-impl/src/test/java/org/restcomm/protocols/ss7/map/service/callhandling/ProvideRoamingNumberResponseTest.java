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

package org.restcomm.protocols.ss7.map.service.callhandling;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberResponse;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/*
 *
 * @author cristian veliscu
 *
 */
public class ProvideRoamingNumberResponseTest {
    Logger logger = Logger.getLogger(ProvideRoamingNumberResponseTest.class);

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

    private byte[] getEncodedData() {
        return new byte[] { 48, 59, 4, 7, -111, -108, -120, 115, 0, -110, -14, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12,
                13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 4, 7,
                -111, -110, 17, 19, 50, 19, -15 };
    }

    private byte[] getEncodedData1() {
        return new byte[] { 4, 7, -111, -108, -120, 115, 0, -110, -14 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { 48, 61, 4, 7, -111, -108, -120, 115, 0, -110, -14, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12,
                13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 5, 0, 4,
                7, -111, -110, 17, 19, 50, 19, -15 };
    }

    @Test(groups = { "functional.decode", "service.callhandling" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ProvideRoamingNumberResponseImplV1.class);
    	parser.replaceClass(ProvideRoamingNumberResponseImplV3.class);

    	byte[] data=this.getEncodedData();
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ProvideRoamingNumberResponse);
        ProvideRoamingNumberResponse prn = (ProvideRoamingNumberResponse)result.getResult();
        
        ISDNAddressString roamingNumber = prn.getRoamingNumber();
        MAPExtensionContainer extensionContainer = prn.getExtensionContainer();
        boolean releaseResourcesSupported = prn.getReleaseResourcesSupported();
        ISDNAddressString vmscAddress = prn.getVmscAddress();
        
        assertNotNull(roamingNumber);
        assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(roamingNumber.getAddress(), "49883700292");

        assertNotNull(extensionContainer);
        assertFalse(releaseResourcesSupported);
        assertNotNull(vmscAddress);
        assertEquals(vmscAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(vmscAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(vmscAddress.getAddress(), "29113123311");
        
        data=this.getEncodedDataFull();
    	result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ProvideRoamingNumberResponse);
        prn = (ProvideRoamingNumberResponse)result.getResult();

        roamingNumber = prn.getRoamingNumber();
        extensionContainer = prn.getExtensionContainer();
        releaseResourcesSupported = prn.getReleaseResourcesSupported();
        vmscAddress = prn.getVmscAddress();
        
        assertNotNull(roamingNumber);
        assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(roamingNumber.getAddress(), "49883700292");

        assertNotNull(extensionContainer);
        assertTrue(releaseResourcesSupported);
        assertNotNull(vmscAddress);
        assertEquals(vmscAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(vmscAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(vmscAddress.getAddress(), "29113123311");
        
        data=this.getEncodedData1();
    	result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ProvideRoamingNumberResponse);
        prn = (ProvideRoamingNumberResponse)result.getResult();

        roamingNumber = prn.getRoamingNumber();
        extensionContainer = prn.getExtensionContainer();
        releaseResourcesSupported = prn.getReleaseResourcesSupported();
        vmscAddress = prn.getVmscAddress();
        
        assertNotNull(roamingNumber);
        assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(roamingNumber.getAddress(), "49883700292");

        assertNull(extensionContainer);
        assertFalse(releaseResourcesSupported);
        assertNull(vmscAddress);

        // System.out.println("Success");
    }

    @Test(groups = { "functional.encode", "service.callhandling" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ProvideRoamingNumberResponseImplV1.class);
    	parser.replaceClass(ProvideRoamingNumberResponseImplV3.class);

        ISDNAddressStringImpl roamingNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "49883700292");
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        boolean releaseResourcesSupported = false;
        ISDNAddressStringImpl vmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "29113123311");
       
        ProvideRoamingNumberResponse prn = new ProvideRoamingNumberResponseImplV3(roamingNumber, extensionContainer,
                releaseResourcesSupported, vmscAddress);

        byte[] data=getEncodedData();
        ByteBuf buffer=parser.encode(prn);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        releaseResourcesSupported = true;
        prn = new ProvideRoamingNumberResponseImplV3(roamingNumber, extensionContainer, releaseResourcesSupported, vmscAddress);

        data=getEncodedDataFull();
        buffer=parser.encode(prn);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        // 2
        prn = new ProvideRoamingNumberResponseImplV1(roamingNumber);

        data=getEncodedData1();
        buffer=parser.encode(prn);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}