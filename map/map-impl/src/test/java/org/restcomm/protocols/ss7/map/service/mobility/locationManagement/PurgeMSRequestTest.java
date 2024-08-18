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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSRequest;
import org.junit.Test;

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
public class PurgeMSRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 13, 4, 5, 17, 17, 33, 34, 34, 4, 4, -111, 34, 50, -12 };
    };

    public byte[] getData2() {
        return new byte[] { -93, 60, 4, 5, 17, 17, 33, 34, 34, -128, 4, -111, 34, 50, -12, -127, 4, -111, 34, 50, -11, 48, 39,
                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
                24, 25, 26, -95, 3, 31, 32, 33 };
    };

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(PurgeMSRequestImplV1.class);
    	parser.replaceClass(PurgeMSRequestImplV3.class);
    	
        // version 2
        byte[] data = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof PurgeMSRequest);
        PurgeMSRequest prim = (PurgeMSRequest)result.getResult();        
        
        assertTrue(prim.getImsi().getData().equals("1111122222"));

        ISDNAddressString vlrNumber = prim.getVlrNumber();
        assertTrue(vlrNumber.getAddress().equals("22234"));
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);

        // version 3
        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof PurgeMSRequest);
        prim = (PurgeMSRequest)result.getResult();
        
        assertTrue(prim.getImsi().getData().equals("1111122222"));

        vlrNumber = prim.getVlrNumber();
        assertTrue(vlrNumber.getAddress().equals("22234"));
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);

        ISDNAddressString sgsnNumber = prim.getSgsnNumber();
        assertTrue(sgsnNumber.getAddress().equals("22235"));
        assertEquals(sgsnNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(sgsnNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));

    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(PurgeMSRequestImplV1.class);
    	parser.replaceClass(PurgeMSRequestImplV3.class);
    	
        // version 2
        ISDNAddressStringImpl vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22234");
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        IMSIImpl imsi = new IMSIImpl("1111122222");

        PurgeMSRequest prim = new PurgeMSRequestImplV1(imsi, vlrNumber);
        byte[] data=getData1();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        // version 3
        ISDNAddressStringImpl sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22235");
        prim = new PurgeMSRequestImplV3(imsi, vlrNumber, sgsnNumber, extensionContainer);
        data=getData2();
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}