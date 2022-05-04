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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DPAnalysedInfoCriterium;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class DCSITest {

	public byte[] getData() {
        return new byte[] { 48, 111, -96, 61, 48, 59, 4, 4, -111, 34, 50, -12, 2, 1, 7, 4, 4, -111, 34, 50, -11, 2, 1, 0, 48,
                39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22,
                23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 2, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0, -124, 0 };
    };

    public byte[] getData2() {
        return new byte[] { 48, 127, -96, 122, 48, 59, 4, 4, -111, 34, 50, -12, 2, 1, 7, 4, 4, -111, 34, 50, -11, 2, 1, 0, 48,
                39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22,
                23, 24, 25, 26, -95, 3, 31, 32, 33, 48, 59, 4, 4, -111, 34, 50, -12, 2, 1, 8, 4, 4, -111, 34, 50, -11, 2, 1, 1,
                48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21,
                22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 2 };
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
        
    	MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
    	List<DPAnalysedInfoCriterium> dpAnalysedInfoCriteriaList = prim.getDPAnalysedInfoCriteriaList();
    	assertNotNull(dpAnalysedInfoCriteriaList);
    	assertEquals(dpAnalysedInfoCriteriaList.size(), 1);
    	DPAnalysedInfoCriterium dpAnalysedInfoCriterium = dpAnalysedInfoCriteriaList.get(0);
    	assertNotNull(dpAnalysedInfoCriterium);
    	ISDNAddressString dialledNumber = dpAnalysedInfoCriterium.getDialledNumber();
    	assertTrue(dialledNumber.getAddress().equals("22234"));
    	assertEquals(dialledNumber.getAddressNature(), AddressNature.international_number);
    	assertEquals(dialledNumber.getNumberingPlan(), NumberingPlan.ISDN);
    	assertEquals(dpAnalysedInfoCriterium.getServiceKey(), 7);
    	ISDNAddressString gsmSCFAddress = dpAnalysedInfoCriterium.getGsmSCFAddress();
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
    	                
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        ISDNAddressStringImpl dialledNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22234");
        ISDNAddressStringImpl gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22235");

        DPAnalysedInfoCriteriumImpl dpAnalysedInfoCriterium = new DPAnalysedInfoCriteriumImpl(dialledNumber, 7, gsmSCFAddress,
                DefaultCallHandling.continueCall, extensionContainer);

        List<DPAnalysedInfoCriterium> dpAnalysedInfoCriteriaList = new ArrayList<DPAnalysedInfoCriterium>();
        dpAnalysedInfoCriteriaList.add(dpAnalysedInfoCriterium);

        DCSI prim = new DCSIImpl(dpAnalysedInfoCriteriaList, 2, extensionContainer, true, true);

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