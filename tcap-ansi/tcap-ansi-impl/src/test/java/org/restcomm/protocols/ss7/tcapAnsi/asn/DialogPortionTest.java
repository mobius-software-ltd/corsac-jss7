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

package org.restcomm.protocols.ss7.tcapAnsi.asn;

import static org.testng.Assert.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ASNUserInformationObjectImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ConfidentialityImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.DialogPortionImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.IntegerApplicationContextNameImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.IntegerSecurityContextImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ObjectSecurityContextImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ProtocolVersionImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.SecurityContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformationExternalImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformationImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

/**
*
* @author sergey vetyutnev
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
        UserInformationImpl ui = dp.getUserInformation();
        assertEquals(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 1L, 1L, 1L }), ui.getExternal().get(0).getObjectIdentifier());
        UserInformationElementTest.byteBufEquals(Unpooled.wrappedBuffer(dataValue), ((ASNOctetString)ui.getExternal().get(0).getChild().getValue()).getValue());

        SecurityContext sc = dp.getSecurityContext();
        assertEquals((long) ((IntegerSecurityContextImpl)sc).getInteger(), 10);
        ConfidentialityImpl con = dp.getConfidentiality();
        assertEquals((long) con.getIntegerConfidentialityId(), 20);

        // 2
        result=parser.decode(Unpooled.wrappedBuffer(data2));
    	assertTrue(result.getResult() instanceof DialogPortionImpl);
    	dp=(DialogPortionImpl)result.getResult();
        
    	assertNull(dp.getProtocolVersion());
    	assertEquals(((IntegerApplicationContextNameImpl)dp.getApplicationContext()).getValue(), new Long(30L));
        assertNull(dp.getUserInformation());

        sc = dp.getSecurityContext();
        assertEquals(((ObjectSecurityContextImpl)sc).getObjectId(), Arrays.asList(new Long[] { 1L, 2L }));
        con = dp.getConfidentiality();
        assertEquals(con.getObjectConfidentialityId(), Arrays.asList(new Long[] { 1L, 4L }));
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(DialogPortionImpl.class);
        // 1
        DialogPortionImpl dp = TcapFactory.createDialogPortion();
        ProtocolVersionImpl pv = TcapFactory.createProtocolVersionFull();
        dp.setProtocolVersion(pv);
        UserInformationImpl ui = TcapFactory.createUserInformation();
        List<UserInformationExternalImpl> uie = new ArrayList<UserInformationExternalImpl>(1);
        UserInformationExternalImpl currElement=new UserInformationExternalImpl();
        currElement.setIdentifier(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 1L, 1L, 1L }));
        
        ASNOctetString innerValue=new ASNOctetString();
        innerValue.setValue(Unpooled.wrappedBuffer(dataValue));        
        ASNUserInformationObjectImpl childObject=new ASNUserInformationObjectImpl();
        childObject.setValue(innerValue);
        currElement.setChildAsObject(childObject);
        uie.add(currElement);
        ui.setExternal(uie);
        dp.setUserInformation(ui);
        IntegerSecurityContextImpl sc = new IntegerSecurityContextImpl();
        sc.setInteger(10L);
        dp.setSecurityContext(sc);
        ConfidentialityImpl con = new ConfidentialityImpl();
        con.setIntegerConfidentialityId(20L);
        dp.setConfidentiality(con);

        ByteBuf output=parser.encode(dp);
        UserInformationElementTest.byteBufEquals(Unpooled.wrappedBuffer(data1),output);

        // 2
        dp = TcapFactory.createDialogPortion();
        ApplicationContext ac = TcapFactory.createApplicationContext(30);
        dp.setApplicationContext(ac);
        ObjectSecurityContextImpl osc = new ObjectSecurityContextImpl();
        osc.setObjectId(Arrays.asList(new Long[] { 1L, 2L }));
        dp.setSecurityContext(osc);
        con = new ConfidentialityImpl();
        con.setObjectConfidentialityId(Arrays.asList(new Long[] { 1L, 4L }));
        dp.setConfidentiality(con);

        output=parser.encode(dp);
        UserInformationElementTest.byteBufEquals(Unpooled.wrappedBuffer(data2),output);
    }
}