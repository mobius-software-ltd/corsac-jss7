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

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MatchType;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
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
public class DestinationNumberCriteriaTest {

    public byte[] getData() {
        return new byte[] { 48, 28, -128, 1, 1, -95, 12, 4, 4, -111, 34, 50, -12, 4, 4, -111, 34, 50, -11, -94, 9, 2, 1, 2, 2,
                1, 4, 2, 1, 1 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(DestinationNumberCriteriaImpl.class);
    	                
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DestinationNumberCriteriaImpl);
        DestinationNumberCriteriaImpl prim = (DestinationNumberCriteriaImpl)result.getResult(); 
        
        List<ISDNAddressString> destinationNumberList = prim.getDestinationNumberList();
        assertNotNull(destinationNumberList);
        assertEquals(destinationNumberList.size(), 2);
        ISDNAddressString destinationNumberOne = destinationNumberList.get(0);
        assertNotNull(destinationNumberOne);
        assertTrue(destinationNumberOne.getAddress().equals("22234"));
        assertEquals(destinationNumberOne.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberOne.getNumberingPlan(), NumberingPlan.ISDN);
        ISDNAddressString destinationNumberTwo = destinationNumberList.get(1);
        assertNotNull(destinationNumberTwo);
        assertTrue(destinationNumberTwo.getAddress().equals("22235"));
        assertEquals(destinationNumberTwo.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberTwo.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(prim.getMatchType().getCode(), MatchType.enabling.getCode());
        List<Integer> destinationNumberLengthList = prim.getDestinationNumberLengthList();
        assertNotNull(destinationNumberLengthList);
        assertEquals(destinationNumberLengthList.size(), 3);
        assertEquals(destinationNumberLengthList.get(0).intValue(), 2);
        assertEquals(destinationNumberLengthList.get(1).intValue(), 4);
        assertEquals(destinationNumberLengthList.get(2).intValue(), 1);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(DestinationNumberCriteriaImpl.class);

        ISDNAddressStringImpl destinationNumberOne = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "22234");
        ISDNAddressStringImpl destinationNumberTwo = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "22235");

        List<ISDNAddressString> destinationNumberList = new ArrayList<ISDNAddressString>();
        destinationNumberList.add(destinationNumberOne);
        destinationNumberList.add(destinationNumberTwo);
        ArrayList<Integer> destinationNumberLengthList = new ArrayList<Integer>();
        destinationNumberLengthList.add(2);
        destinationNumberLengthList.add(4);
        destinationNumberLengthList.add(1);

        DestinationNumberCriteriaImpl prim = new DestinationNumberCriteriaImpl(MatchType.enabling, destinationNumberList,
                destinationNumberLengthList);

        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, this.getData()));
    }
}