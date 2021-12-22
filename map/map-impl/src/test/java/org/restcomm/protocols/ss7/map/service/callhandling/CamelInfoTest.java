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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
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
 *
 * @author sergey vetyutnev
 *
 */
public class CamelInfoTest {
    Logger logger = Logger.getLogger(ExtendedRoutingInfoTest.class);

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

    public static byte[] getData() {
        return new byte[] { (byte) 171, 4, 3, 2, 5, (byte) 224 };
    }

    public static byte[] getDataFull() {
        return new byte[] { -85, 51, 3, 2, 6, -64, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6,
                3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 2, 1, -86 };
    }

    @Test(groups = { "functional.decode", "service.callhandling" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CamelInfoImpl.class);
    	
    	byte[] data = getData();
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CamelInfoImpl);
        CamelInfoImpl impl = (CamelInfoImpl)result.getResult();
        
        SupportedCamelPhases scf = impl.getSupportedCamelPhases();
        assertTrue(scf.getPhase1Supported());
        assertTrue(scf.getPhase2Supported());
        assertTrue(scf.getPhase3Supported());
        assertFalse(scf.getPhase4Supported());

        assertFalse(impl.getSuppressTCSI());
        assertNull(impl.getExtensionContainer());
        assertNull(impl.getOfferedCamel4CSIs());

        data = getDataFull();
    	result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CamelInfoImpl);
        impl = (CamelInfoImpl)result.getResult();
        
        scf = impl.getSupportedCamelPhases();
        assertTrue(scf.getPhase1Supported());
        assertTrue(scf.getPhase2Supported());
        assertFalse(scf.getPhase3Supported());
        assertFalse(scf.getPhase4Supported());

        assertTrue(impl.getSuppressTCSI());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(impl.getExtensionContainer()));
        OfferedCamel4CSIs ofc = impl.getOfferedCamel4CSIs();
        assertTrue(ofc.getOCsi());
        assertFalse(ofc.getDCsi());
        assertTrue(ofc.getVtCsi());
        assertFalse(ofc.getTCsi());
        assertTrue(ofc.getMtSmsCsi());
        assertFalse(ofc.getMgCsi());
        assertTrue(ofc.getPsiEnhancements());
    }

    @Test(groups = { "functional.encode", "service.callhandling" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CamelInfoImpl.class);
    	
        SupportedCamelPhasesImpl scf = new SupportedCamelPhasesImpl(true, true, true, false);
        CamelInfoImpl impl = new CamelInfoImpl(scf, false, null, null);
        byte[] data=getData();
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        scf = new SupportedCamelPhasesImpl(true, true, false, false);
        OfferedCamel4CSIsImpl ofc = new OfferedCamel4CSIsImpl(true, false, true, false, true, false, true);
        impl = new CamelInfoImpl(scf, true, MAPExtensionContainerTest.GetTestExtensionContainer(), ofc);
        data=getDataFull();
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}