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

package org.restcomm.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumberImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySetsImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
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
 *
 * @author sergey vetyutnev
 *
 */
public class LCSLocationInfoTest {
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

    public byte[] getEncodedData() {
        return new byte[] { 48, 102, 4, 5, -111, 85, 22, 9, 112, -128, 4, 11, 12, 13, 14, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -126, 0, -93, 8, -128, 6, -111, 34, 34, 17, 34, 34, -124, 2, 6, -64, -123, 2, 5, -32, -122, 9, 21, 22, 23, 23, 25, 26, 27, 28, 29, -120, 9, 31, 32, 33, 33, 35, 36, 37, 38, 39 };
    }

    public byte[] getDataLmsi() {
        return new byte[] { 11, 12, 13, 14 };
    }

    public byte[] getDataMmeName() {
        return new byte[] { 21, 22, 23, 23, 25, 26, 27, 28, 29 };
    }

    public byte[] getDataAaaServerName() {
        return new byte[] { 31, 32, 33, 33, 35, 36, 37, 38, 39 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LCSLocationInfoImpl.class);
    	
        byte[] data = getEncodedData();

        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LCSLocationInfoImpl);
        LCSLocationInfoImpl imp = (LCSLocationInfoImpl)result.getResult();
        
        ISDNAddressStringImpl networkNodeNumber = imp.getNetworkNodeNumber();
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(networkNodeNumber.getAddress().equals("55619007"));

        assertTrue(Arrays.equals(imp.getLMSI().getData(), getDataLmsi()));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(imp.getExtensionContainer()));
        assertTrue(imp.getGprsNodeIndicator());

        assertTrue(imp.getAdditionalNumber().getMSCNumber().getAddress().equals("2222112222"));
        assertNull(imp.getAdditionalNumber().getSGSNNumber());

        SupportedLCSCapabilitySetsImpl supportedLCSCapabilitySets = imp.getSupportedLCSCapabilitySets();
        assertTrue(supportedLCSCapabilitySets.getCapabilitySetRelease98_99());
        assertTrue(supportedLCSCapabilitySets.getCapabilitySetRelease4());
        assertFalse(supportedLCSCapabilitySets.getCapabilitySetRelease5());
        assertFalse(supportedLCSCapabilitySets.getCapabilitySetRelease6());

        SupportedLCSCapabilitySetsImpl additionalLCSCapabilitySets = imp.getAdditionalLCSCapabilitySets();
        assertTrue(additionalLCSCapabilitySets.getCapabilitySetRelease98_99());
        assertTrue(additionalLCSCapabilitySets.getCapabilitySetRelease4());
        assertTrue(additionalLCSCapabilitySets.getCapabilitySetRelease5());
        assertFalse(additionalLCSCapabilitySets.getCapabilitySetRelease6());

        assertTrue(Arrays.equals(imp.getMmeName().getData(), getDataMmeName()));
        assertTrue(Arrays.equals(imp.getAaaServerName().getData(), getDataAaaServerName()));
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LCSLocationInfoImpl.class);
    	
        byte[] data = getEncodedData();

        ISDNAddressStringImpl networkNodeNumber = MAPParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "55619007");
        LMSIImpl lmsi = new LMSIImpl(getDataLmsi());
        ISDNAddressStringImpl mscNumber = MAPParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "2222112222");
        AdditionalNumberImpl additionalNumber = new AdditionalNumberImpl(mscNumber, null);
        SupportedLCSCapabilitySetsImpl supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true, false,
                false, false);
        SupportedLCSCapabilitySetsImpl additionalLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true, true,
                false, false);
        DiameterIdentityImpl mmeName = new DiameterIdentityImpl(getDataMmeName());
        DiameterIdentityImpl aaaServerName = new DiameterIdentityImpl(getDataAaaServerName());

        LCSLocationInfoImpl imp = new LCSLocationInfoImpl(networkNodeNumber, lmsi,
                MAPExtensionContainerTest.GetTestExtensionContainer(), true, additionalNumber, supportedLCSCapabilitySets,
                additionalLCSCapabilitySets, mmeName, aaaServerName);
        
        ByteBuf buffer=parser.encode(imp);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}
