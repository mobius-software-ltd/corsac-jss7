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

package org.restcomm.protocols.ss7.tcapAnsi.asn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.Confidentiality;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ProtocolVersion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.SecurityContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformationElement;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@Test(groups = { "asn" })
public class DialogPortionTest {

    private byte[] data1 = new byte[] { -7, 33, -38, 1, 3, -3, 20, 40, 18, 6, 7, 4, 0, 0, 1, 1, 1, 1, -96, 7, 4, 5, 3, 4, 5, 6, 7, -128, 1, 10, -94, 3, -128,
            1, 20 };

    private byte[] data2 = new byte[] { -7, 11, -37, 1, 30, -127, 1, 42, -94, 3, -127, 1, 44 };    

    private byte[] dataValue = new byte[] { 3, 4, 5, 6, 7 };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(DialogPortionImpl.class);
        // 1
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data1));
    	assertTrue(result.getResult() instanceof DialogPortionImpl);
    	DialogPortionImpl dp=(DialogPortionImpl)result.getResult();
        
    	assertNull(dp.getApplicationContext());
        assertTrue(dp.getProtocolVersion().isT1_114_2000Supported());
        UserInformation ui = dp.getUserInformation();
        assertEquals(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 1L, 1L, 1L }), ui.getUserInformationElements().get(0).getObjectIdentifier());
        UserInformationElementTest.byteBufEquals(Unpooled.wrappedBuffer(dataValue), ((ASNOctetString)ui.getUserInformationElements().get(0).getChild()).getValue());

        SecurityContext sc = dp.getSecurityContext();
        assertEquals(sc.getInt(), new Integer(10));
        Confidentiality con = dp.getConfidentiality();
        assertEquals((long) con.getIntegerConfidentialityId(), 20);

        // 2
        result=parser.decode(Unpooled.wrappedBuffer(data2));
    	assertTrue(result.getResult() instanceof DialogPortionImpl);
    	dp=(DialogPortionImpl)result.getResult();
        
    	assertNull(dp.getProtocolVersion());
    	assertEquals(dp.getApplicationContext().getInt(), new Integer(30));
        assertNull(dp.getUserInformation());

        sc = dp.getSecurityContext();
        assertEquals(sc.getObj(), Arrays.asList(new Long[] { 1L, 2L }));
        con = dp.getConfidentiality();
        assertEquals(con.getObjectConfidentialityId(), Arrays.asList(new Long[] { 1L, 4L }));
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(DialogPortionImpl.class);
        // 1
        DialogPortion dp = TcapFactory.createDialogPortion();
        ProtocolVersion pv = TcapFactory.createProtocolVersionFull();
        dp.setProtocolVersion(pv);
        UserInformation ui = TcapFactory.createUserInformation();
        List<UserInformationElement> uie = new ArrayList<UserInformationElement>(1);
        UserInformationElementImpl currElement=new UserInformationElementImpl();
        currElement.setIdentifier(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 1L, 1L, 1L }));
        
        ASNOctetString innerValue=new ASNOctetString(Unpooled.wrappedBuffer(dataValue),null,null,null,false);        
        currElement.setChildAsObject(new ASNUserInformationObjectImpl(innerValue));
        uie.add(currElement);
        ui.setUserInformationElements(uie);
        dp.setUserInformation(ui);
        SecurityContextImpl sc = new SecurityContextImpl();
        sc.setInt(10);
        dp.setSecurityContext(sc);
        ConfidentialityImpl con = new ConfidentialityImpl();
        con.setIntegerConfidentialityId(20);
        dp.setConfidentiality(con);

        ByteBuf output=parser.encode(dp);
        UserInformationElementTest.byteBufEquals(Unpooled.wrappedBuffer(data1),output);

        // 2
        dp = TcapFactory.createDialogPortion();
        ApplicationContext ac = TcapFactory.createApplicationContext(30);
        dp.setApplicationContext(ac);
        SecurityContextImpl osc = new SecurityContextImpl();
        osc.setObj(Arrays.asList(new Long[] { 1L, 2L }));
        dp.setSecurityContext(osc);
        con = new ConfidentialityImpl();
        con.setObjectConfidentialityId(Arrays.asList(new Long[] { 1L, 4L }));
        dp.setConfidentiality(con);

        output=parser.encode(dp);
        UserInformationElementTest.byteBufEquals(Unpooled.wrappedBuffer(data2),output);
    }
}