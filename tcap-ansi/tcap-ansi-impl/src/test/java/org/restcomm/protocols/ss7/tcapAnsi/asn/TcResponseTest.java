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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Return;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCResponseMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.WrappedComponent;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.ComponentPortionImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.WrappedComponentImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

@Test(groups = { "asn" })
public class TcResponseTest {

    private byte[] data1 = new byte[] { (byte) 0xe4, 0x3e, (byte) 0xc7, 0x04, 0x14, 0x00, 0x00, 0x00, (byte) 0xe8, 0x36, (byte) 0xea, 0x34, (byte) 0xcf, 0x01,
            0x01, (byte) 0xf2, 0x2f, 0x04, 0x2d, (byte) 0x96, 0x01, 0x13, (byte) 0x8e, 0x02, 0x06, 0x00, (byte) 0x95, 0x03, 0x00, 0x0c, 0x10, (byte) 0x9f, 0x4e, 0x01,
            0x01, (byte) 0x99, 0x03, 0x7a, 0x0d, 0x11, (byte) 0x9f, 0x5d, 0x07, 0x00, 0x00, 0x21, 0x06, 0x36, 0x54, 0x10, (byte) 0x97, 0x01, 0x07, (byte) 0x9f,
            0x73, 0x01, 0x00, (byte) 0x9f, 0x75, 0x01, 0x00, (byte) 0x98, 0x01, 0x02 };

    private byte[] data2 = new byte[] { -28, 11, -57, 4, 20, 0, 0, 0, -7, 3, -37, 1, 66 };

    private byte[] trId = new byte[] { 20, 0, 0, 0 };

    private byte[] parData = new byte[] { -106, 1, 19, -114, 2, 6, 0, -107, 3, 0, 12, 16, -97, 78, 1, 1, -103, 3, 122, 13, 17, -97, 93, 7, 0, 0, 33, 6, 54, 84,
            16, -105, 1, 7, -97, 115, 1, 0, -97, 117, 1, 0, -104, 1, 2 };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(TCResponseMessageImpl.class);
        // 1
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(this.data1));
        assertTrue(result.getResult() instanceof TCResponseMessageImpl);
        TCResponseMessage tcm = (TCResponseMessage)result.getResult();

        assertTrue(ByteBufUtil.equals(tcm.getDestinationTransactionId(), Unpooled.wrappedBuffer(trId)));
        assertNull(tcm.getDialogPortion());
        assertEquals(tcm.getComponent().getComponents().size(), 1);
        WrappedComponent cmp = tcm.getComponent().getComponents().get(0);
        assertEquals(cmp.getType(), ComponentType.ReturnResultLast);
        Return rrl = cmp.getReturnResultLast();
        assertEquals((long) rrl.getCorrelationId(), 1);
        assertTrue(rrl.getParameter() instanceof ASNOctetString);
        UserInformationElementTest.byteBufEquals(((ASNOctetString)rrl.getParameter()).getValue(), Unpooled.wrappedBuffer(parData));

        // 2
        result=parser.decode(Unpooled.wrappedBuffer(this.data2));
        assertTrue(result.getResult() instanceof TCResponseMessageImpl);
        tcm = (TCResponseMessage)result.getResult();

        assertTrue(ByteBufUtil.equals(tcm.getDestinationTransactionId(), Unpooled.wrappedBuffer(trId)));
        DialogPortion dp = tcm.getDialogPortion();
        assertNull(dp.getProtocolVersion());
        ApplicationContext ac = dp.getApplicationContext();
        assertEquals(ac.getInt(), new Long(66L));
        assertNull(dp.getConfidentiality());
        assertNull(dp.getSecurityContext());
        assertNull(dp.getUserInformation());

        assertNull(tcm.getComponent());

    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(TCResponseMessageImpl.class);
        // 1
        List<WrappedComponent> cc = new ArrayList<WrappedComponent>(1);
        Return rrl = TcapFactory.createComponentReturnResultLast();
        WrappedComponentImpl component=new WrappedComponentImpl();
        component.setReturnResultLast(rrl);
        cc.add(component);
        rrl.setCorrelationId(1L);
        ASNOctetString p=new ASNOctetString(Unpooled.wrappedBuffer(parData),null,null,null,false);
        rrl.setSetParameter(p);

        TCResponseMessage tcm = TcapFactory.createTCResponseMessage();
        tcm.setDestinationTransactionId(Unpooled.wrappedBuffer(trId));
        ComponentPortionImpl cp=new ComponentPortionImpl();
        cp.setComponents(cc);
        tcm.setComponent(cp);

        ByteBuf encodedData=parser.encode(tcm);
        ByteBuf expectedData = Unpooled.wrappedBuffer(data1);
        UserInformationElementTest.byteBufEquals(encodedData, expectedData);

        // 2
        tcm = TcapFactory.createTCResponseMessage();
        tcm.setDestinationTransactionId(Unpooled.wrappedBuffer(trId));

        DialogPortion dp = TcapFactory.createDialogPortion();
        ApplicationContext ac = TcapFactory.createApplicationContext(66);
        dp.setApplicationContext(ac);
        tcm.setDialogPortion(dp);

        encodedData=parser.encode(tcm);
        expectedData = Unpooled.wrappedBuffer(data2);
        UserInformationElementTest.byteBufEquals(encodedData, expectedData);
    }

}

