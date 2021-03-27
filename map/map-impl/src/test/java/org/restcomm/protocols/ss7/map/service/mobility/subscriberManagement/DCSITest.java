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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DPAnalysedInfoCriteriumImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
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
public class DCSITest {

    public byte[] getData() {
        return new byte[] { 48, 123, -96, 67, 48, 65, 4, 4, -111, 34, 50, -12, 2, 1, 7, 4, 4, -111, 34, 50, -11, 2, 1, 0, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -127, 1, 2, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -125, 0, -124, 0 };
    };

    public byte[] getData2() {
        return new byte[] { 48, -127, -116, -96, -127, -122, 48, 65, 4, 4, -111, 34, 50, -12, 2, 1, 7, 4, 4, -111, 34, 50, -11, 2, 1, 0, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 48, 65, 4, 4, -111, 34, 50, -12, 2, 1, 8, 4, 4, -111, 34, 50, -11, 2, 1, 1, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -127, 1, 2 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(DCSIImpl.class);
    	                
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DCSIImpl);
        DCSIImpl prim = (DCSIImpl)result.getResult(); 
        
    	MAPExtensionContainerImpl extensionContainer = prim.getExtensionContainer();
    	List<DPAnalysedInfoCriteriumImpl> dpAnalysedInfoCriteriaList = prim.getDPAnalysedInfoCriteriaList();
    	assertNotNull(dpAnalysedInfoCriteriaList);
    	assertEquals(dpAnalysedInfoCriteriaList.size(), 1);
    	DPAnalysedInfoCriteriumImpl dpAnalysedInfoCriterium = dpAnalysedInfoCriteriaList.get(0);
    	assertNotNull(dpAnalysedInfoCriterium);
    	ISDNAddressStringImpl dialledNumber = dpAnalysedInfoCriterium.getDialledNumber();
    	assertTrue(dialledNumber.getAddress().equals("22234"));
    	assertEquals(dialledNumber.getAddressNature(), AddressNature.international_number);
    	assertEquals(dialledNumber.getNumberingPlan(), NumberingPlan.ISDN);
    	assertEquals(dpAnalysedInfoCriterium.getServiceKey(), 7);
    	ISDNAddressStringImpl gsmSCFAddress = dpAnalysedInfoCriterium.getGsmSCFAddress();
    	assertTrue(gsmSCFAddress.getAddress().equals("22235"));
    	assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
    	assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
    	assertEquals(dpAnalysedInfoCriterium.getDefaultCallHandling(), DefaultCallHandling.continueCall);
    	assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
    	assertEquals(prim.getCamelCapabilityHandling().intValue(), 2);
    	assertTrue(prim.getCsiActive());
    	assertTrue(prim.getNotificationToCSE());
        
    	data = this.getData2();
    	result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DCSIImpl);
        prim = (DCSIImpl)result.getResult(); 

    	extensionContainer = prim.getExtensionContainer();
    	dpAnalysedInfoCriteriaList = prim.getDPAnalysedInfoCriteriaList();
    	assertNotNull(dpAnalysedInfoCriteriaList);
    	assertEquals(dpAnalysedInfoCriteriaList.size(), 2);
    	dpAnalysedInfoCriterium = dpAnalysedInfoCriteriaList.get(0);
    	assertNotNull(dpAnalysedInfoCriterium);
    	dialledNumber = dpAnalysedInfoCriterium.getDialledNumber();
    	assertTrue(dialledNumber.getAddress().equals("22234"));
    	assertEquals(dialledNumber.getAddressNature(), AddressNature.international_number);
    	assertEquals(dialledNumber.getNumberingPlan(), NumberingPlan.ISDN);
    	assertEquals(dpAnalysedInfoCriterium.getServiceKey(), 7);
    	gsmSCFAddress = dpAnalysedInfoCriterium.getGsmSCFAddress();
    	assertTrue(gsmSCFAddress.getAddress().equals("22235"));
    	assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
    	assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
    	assertEquals(dpAnalysedInfoCriterium.getDefaultCallHandling(), DefaultCallHandling.continueCall);
    	assertNull(extensionContainer);
    	assertEquals(prim.getCamelCapabilityHandling().intValue(), 2);
    	assertTrue(!prim.getCsiActive());
    	assertTrue(!prim.getNotificationToCSE());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(DCSIImpl.class);
    	                
        MAPExtensionContainerImpl extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        ISDNAddressStringImpl dialledNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22234");
        ISDNAddressStringImpl gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22235");

        DPAnalysedInfoCriteriumImpl dpAnalysedInfoCriterium = new DPAnalysedInfoCriteriumImpl(dialledNumber, 7, gsmSCFAddress,
                DefaultCallHandling.continueCall, extensionContainer);

        ArrayList<DPAnalysedInfoCriteriumImpl> dpAnalysedInfoCriteriaList = new ArrayList<DPAnalysedInfoCriteriumImpl>();
        dpAnalysedInfoCriteriaList.add(dpAnalysedInfoCriterium);

        DCSIImpl prim = new DCSIImpl(dpAnalysedInfoCriteriaList, 2, extensionContainer, true, true);

        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData=this.getData();
        assertTrue(Arrays.equals(encodedData, rawData));

        extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        DPAnalysedInfoCriteriumImpl dpAnalysedInfoCriterium2 = new DPAnalysedInfoCriteriumImpl(dialledNumber, 8, gsmSCFAddress,
                DefaultCallHandling.releaseCall, extensionContainer);
        dpAnalysedInfoCriteriaList.add(dpAnalysedInfoCriterium2);
        prim = new DCSIImpl(dpAnalysedInfoCriteriaList, 2, null, false, false);

        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData=this.getData2();
        assertTrue(Arrays.equals(encodedData, rawData));
    }
}