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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGFeatureImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlockImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGSubscriptionImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InterCUGRestrictionsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InterCUGRestrictionsValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.IntraCUGOptions;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class CUGInfoTest {

    public byte[] getData() {
        return new byte[] { 48, -127, -81, 48, 66, 48, 64, 2, 1, 1, 4, 4, 1, 2, 3, 4, 10, 1, 0, 48, 3, -126, 1, 38, -96, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 48, 58, 48, 56, -126, 1, 38, 2, 1, 1, 4, 1, 0, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -96, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33 };
    };

    private byte[] getGugData() {
        return new byte[] { 1, 2, 3, 4 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CUGInfoImpl.class);
    	                
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CUGInfoImpl);
        CUGInfoImpl prim = (CUGInfoImpl)result.getResult(); 
        
        MAPExtensionContainerImpl extensionContainer = prim.getExtensionContainer();
        assertNotNull(prim.getCUGSubscriptionList());
        assertTrue(prim.getCUGSubscriptionList().size() == 1);
        CUGSubscriptionImpl cugSub = prim.getCUGSubscriptionList().get(0);
        assertNotNull(cugSub);
        assertEquals(cugSub.getCUGIndex(), 1);
        assertTrue(Arrays.equals(cugSub.getCugInterlock().getData(), getGugData()));
        assertEquals(cugSub.getIntraCugOptions(), IntraCUGOptions.noCUGRestrictions);
        List<ExtBasicServiceCodeImpl> basicServiceList = cugSub.getBasicServiceGroupList();
        assertEquals(basicServiceList.size(), 1);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(cugSub.getExtensionContainer()));

        assertNotNull(prim.getCUGFeatureList());
        assertTrue(prim.getCUGFeatureList().size() == 1);
        CUGFeatureImpl cugF = prim.getCUGFeatureList().get(0);
        assertNotNull(cugF);
        assertEquals(cugF.getBasicService().getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.padAccessCA_9600bps);
        assertEquals((int) cugF.getPreferentialCugIndicator(), 1);
        assertEquals(cugF.getInterCugRestrictions().getInterCUGRestrictionsValue(), InterCUGRestrictionsValue.CUGOnlyFacilities);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(cugF.getExtensionContainer()));

        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CUGInfoImpl.class);
        
    	MAPExtensionContainerImpl extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        ExtBasicServiceCodeImpl basicService = new ExtBasicServiceCodeImpl(b);
        Integer preferentialCugIndicator = 1;
        InterCUGRestrictionsImpl interCugRestrictions = new InterCUGRestrictionsImpl(InterCUGRestrictionsValue.CUGOnlyFacilities);
        CUGFeatureImpl cugFeature = new CUGFeatureImpl(basicService, preferentialCugIndicator, interCugRestrictions,
                extensionContainer);
        ArrayList<CUGFeatureImpl> cugFeatureList = new ArrayList<CUGFeatureImpl>();
        cugFeatureList.add(cugFeature);

        ArrayList<CUGSubscriptionImpl> cugSubscriptionList = new ArrayList<CUGSubscriptionImpl>();
        int cugIndex = 1;
        CUGInterlockImpl cugInterlock = new CUGInterlockImpl(getGugData());
        IntraCUGOptions intraCugOptions = IntraCUGOptions.noCUGRestrictions;
        ArrayList<ExtBasicServiceCodeImpl> basicServiceList = new ArrayList<ExtBasicServiceCodeImpl>();
        basicServiceList.add(basicService);
        CUGSubscriptionImpl cugSubscription = new CUGSubscriptionImpl(cugIndex, cugInterlock, intraCugOptions,
                basicServiceList, extensionContainer);
        cugSubscriptionList.add(cugSubscription);

        CUGInfoImpl prim = new CUGInfoImpl(cugSubscriptionList, cugFeatureList, extensionContainer);

        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData=this.getData();
        assertTrue(Arrays.equals(encodedData, rawData));
    }
}
