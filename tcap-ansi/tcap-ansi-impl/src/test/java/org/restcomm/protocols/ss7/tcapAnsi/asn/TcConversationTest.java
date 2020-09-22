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

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContextNameImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.DialogPortionImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentPortionImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.InvokeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.InvokeLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCodeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCConversationMessage;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

@Test(groups = { "asn" })
public class TcConversationTest {

    private byte[] data1 = new byte[] { -27, 30, -57, 8, 1, 1, 2, 2, 3, 3, 4, 4, -24, 18, -23, 16, -49, 1, 0, -47, 2, 9, 53, -14, 7, 4, 5, 3, 4, 5, 6, 7 };

    private byte[] data2 = new byte[] { -26, 15, -57, 8, 1, 1, 2, 2, 3, 3, 4, 4, -7, 3, -37, 1, 66 };

    private byte[] parData = new byte[] { 3, 4, 5, 6, 7 };

    private byte[] trIdO = new byte[] { 1, 1, 2, 2 };
    private byte[] trIdD = new byte[] { 3, 3, 4, 4 };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(TCConversationMessageImpl.class);
    	parser.loadClass(TCConversationMessageImplWithPerm.class);
    	
        // 1
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(this.data1));
    	assertTrue(result.getResult() instanceof TCConversationMessageImplWithPerm);
        
        TCConversationMessage tcm = (TCConversationMessage)result.getResult();

        assertTrue(tcm.getDialogTermitationPermission());
        assertEquals(tcm.getOriginatingTransactionId(), trIdO);
        assertEquals(tcm.getDestinationTransactionId(), trIdD);
        assertNull(tcm.getDialogPortion());
        assertEquals(tcm.getComponent().getComponents().size(), 1);
        ComponentImpl cmp = tcm.getComponent().getComponents().get(0);
        assertEquals(cmp.getType(), ComponentType.InvokeLast);
        InvokeImpl inv = cmp.getInvokeLast();
        assertFalse(inv.isNotLast());
        assertEquals((long) inv.getInvokeId(), 0);
        assertNull(inv.getCorrelationId());
        assertEquals(inv.getOperationCode().getPrivateOperationCode(), new Long(2357L));
        assertTrue(inv.getParameter() instanceof ASNOctetString); 
        ByteBuf realData=((ASNOctetString)inv.getParameter()).getValue();
        UserInformationElementTest.byteBufEquals(realData, Unpooled.wrappedBuffer(parData));

        // 2
        result=parser.decode(Unpooled.wrappedBuffer(this.data2));
    	assertTrue(result.getResult() instanceof TCConversationMessageImpl);
        
        tcm = (TCConversationMessage)result.getResult();

        assertFalse(tcm.getDialogTermitationPermission());
        assertEquals(tcm.getOriginatingTransactionId(), trIdO);
        assertEquals(tcm.getDestinationTransactionId(), trIdD);
        DialogPortionImpl dp = tcm.getDialogPortion();
        assertNull(dp.getProtocolVersion());
        ApplicationContextNameImpl ac = dp.getApplicationContext();
        assertEquals(ac.getInt(), new Long(66l));
        assertNull(dp.getConfidentiality());
        assertNull(dp.getSecurityContext());
        assertNull(dp.getUserInformation());

        assertNull(tcm.getComponent());

    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(TCConversationMessageImpl.class);
    	parser.loadClass(TCConversationMessageImplWithPerm.class);
    	
    	// 1
        List<ComponentImpl> cc = new ArrayList<ComponentImpl>(1);
        InvokeLastImpl inv = TcapFactory.createComponentInvokeLast();
        
        ComponentImpl component=new ComponentImpl();
        component.setInvokeLast(inv);
        cc.add(component);
        inv.setInvokeId(0L);
        OperationCodeImpl oc = TcapFactory.createPrivateOperationCode(2357L);
        inv.setOperationCode(oc);
        ASNOctetString innerValue=new ASNOctetString();
        innerValue.setValue(Unpooled.wrappedBuffer(parData));        
        inv.setSetParameter(innerValue);

        TCConversationMessage tcm = TcapFactory.createTCConversationMessage(true);
        tcm.setOriginatingTransactionId(trIdO);
        tcm.setDestinationTransactionId(trIdD);
        
        ComponentPortionImpl cp=new ComponentPortionImpl();
        cp.setComponents(cc);
        tcm.setComponent(cp);

        ByteBuf encodedData=parser.encode(tcm);
        ByteBuf expectedData = Unpooled.wrappedBuffer(data1);
        UserInformationElementTest.byteBufEquals(encodedData, expectedData);

        // 2
        tcm = TcapFactory.createTCConversationMessage(false);
        tcm.setOriginatingTransactionId(trIdO);
        tcm.setDestinationTransactionId(trIdD);

        DialogPortionImpl dp = TcapFactory.createDialogPortion();
        ApplicationContextNameImpl ac = TcapFactory.createApplicationContext(66);
        dp.setApplicationContext(ac);
        tcm.setDialogPortion(dp);

        encodedData=parser.encode(tcm);
        expectedData = Unpooled.wrappedBuffer(data2);
        UserInformationElementTest.byteBufEquals(encodedData, expectedData);

    }

}
