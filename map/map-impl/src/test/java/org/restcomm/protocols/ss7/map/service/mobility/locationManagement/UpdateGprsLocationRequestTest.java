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
package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UESRVCCCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class UpdateGprsLocationRequestTest {

    public byte[] getData() {
        return new byte[] { 48, -127, -107, 4, 3, 17, 33, 34, 4, 4, -111, 34, 34, -8, 4, 5, 4, 38, 48, 81, 5, 48, 39, -96,
                32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24,
                25, 26, -95, 3, 31, 32, 33, -96, 43, 5, 0, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5,
                6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 0, -126, 0, -125, 5,
                4, 38, 48, 81, 5, -92, 6, -128, 4, 33, 67, 33, 67, -91, 4, -127, 2, 5, -32, -122, 0, -121, 0, -120, 1, 2,
                -119, 0, -118, 0, -117, 0, -116, 0, -115, 0, -114, 1, 1 };
    };

    public byte[] getGSNAddressData() {
        return new byte[] { 38, 48, 81, 5 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(UpdateGprsLocationRequestImpl.class);
    	
    	byte[] data = this.getData();
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof UpdateGprsLocationRequestImpl);
        UpdateGprsLocationRequestImpl prim = (UpdateGprsLocationRequestImpl)result.getResult();
        
        assertTrue(prim.getImsi().getData().equals("111222"));
        assertTrue(prim.getSgsnNumber().getAddress().equals("22228"));
        assertEquals(prim.getSgsnNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(prim.getSgsnNumber().getNumberingPlan(), NumberingPlan.ISDN);
        
        assertEquals(prim.getSgsnAddress().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertTrue(ByteBufUtil.equals(prim.getSgsnAddress().getGSNAddressData(), Unpooled.wrappedBuffer(getGSNAddressData())));
        
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));
        assertTrue(prim.getSGSNCapability().getSolsaSupportIndicator());
        assertTrue(prim.getInformPreviousNetworkEntity());
        assertTrue(prim.getPsLCSNotSupportedByUE());
        
        assertEquals(prim.getVGmlcAddress().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertTrue(ByteBufUtil.equals(prim.getVGmlcAddress().getGSNAddressData(), Unpooled.wrappedBuffer(getGSNAddressData())));
        
        assertTrue(prim.getADDInfo().getImeisv().getIMEI().equals("12341234"));
        assertTrue(prim.getEPSInfo().getIsrInformation().getCancelSGSN());
        assertTrue(prim.getServingNodeTypeIndicator());
        assertTrue(prim.getSkipSubscriberDataUpdate());
        assertEquals(prim.getUsedRATType(), UsedRATType.gan);
        assertTrue(prim.getGprsSubscriptionDataNotNeeded());
        assertTrue(prim.getNodeTypeIndicator());
        assertTrue(prim.getAreaRestricted());
        assertTrue(prim.getUeReachableIndicator());
        assertTrue(prim.getEpsSubscriptionDataNotNeeded());
        assertEquals(prim.getUESRVCCCapability(), UESRVCCCapability.ueSrvccSupported);

    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(UpdateGprsLocationRequestImpl.class);
    	
    	IMSIImpl imsi = new IMSIImpl("111222");
        ISDNAddressStringImpl sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        GSNAddressImpl sgsnAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4,Unpooled.wrappedBuffer(getGSNAddressData()));
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        SGSNCapabilityImpl sgsnCapability = new SGSNCapabilityImpl(true, extensionContainer, null, false, null, null, null, false,
                null, null, false, null);
        boolean informPreviousNetworkEntity = true;
        boolean psLCSNotSupportedByUE = true;
        GSNAddressImpl vGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4,Unpooled.wrappedBuffer(getGSNAddressData()));
        ADDInfoImpl addInfo = new ADDInfoImpl(new IMEIImpl("12341234"), false);
        EPSInfoImpl epsInfo = new EPSInfoImpl(new ISRInformationImpl(true, true, true));
        boolean servingNodeTypeIndicator = true;
        boolean skipSubscriberDataUpdate = true;
        UsedRATType usedRATType = UsedRATType.gan;
        boolean gprsSubscriptionDataNotNeeded = true;
        boolean nodeTypeIndicator = true;
        boolean areaRestricted = true;
        boolean ueReachableIndicator = true;
        boolean epsSubscriptionDataNotNeeded = true;
        UESRVCCCapability uesrvccCapability = UESRVCCCapability.ueSrvccSupported;

        UpdateGprsLocationRequestImpl prim = new UpdateGprsLocationRequestImpl(imsi, sgsnNumber, sgsnAddress,
                extensionContainer, sgsnCapability, informPreviousNetworkEntity, psLCSNotSupportedByUE, vGmlcAddress, addInfo,
                epsInfo, servingNodeTypeIndicator, skipSubscriberDataUpdate, usedRATType, gprsSubscriptionDataNotNeeded,
                nodeTypeIndicator, areaRestricted, ueReachableIndicator, epsSubscriptionDataNotNeeded, uesrvccCapability);

        byte[] data=getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}
