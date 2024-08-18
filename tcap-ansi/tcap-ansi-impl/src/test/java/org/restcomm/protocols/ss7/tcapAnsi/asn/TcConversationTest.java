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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCConversationMessage;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.ASNComponentPortionObjectImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.ComponentPortionImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.InvokeLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.InvokeNotLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.RejectImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.ReturnErrorImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.ReturnResultLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.ReturnResultNotLastImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
/**
 * 
 * @author yulianoifa
 *
 */
public class TcConversationTest {

    private byte[] data1 = new byte[] { -27, 30, -57, 8, 1, 1, 2, 2, 3, 3, 4, 4, -24, 18, -23, 16, -49, 1, 0, -47, 2, 9, 53, -14, 7, 4, 5, 3, 4, 5, 6, 7 };

    private byte[] data2 = new byte[] { -26, 15, -57, 8, 1, 1, 2, 2, 3, 3, 4, 4, -7, 3, -37, 1, 66 };

    private byte[] parData = new byte[] { 3, 4, 5, 6, 7 };

    private byte[] trIdO = new byte[] { 1, 1, 2, 2 };
    private byte[] trIdD = new byte[] { 3, 3, 4, 4 };

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(TCConversationMessageImpl.class);
    	parser.loadClass(TCConversationMessageImplWithPerm.class);
    	
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, InvokeNotLastImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, InvokeLastImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnResultNotLastImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnResultLastImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, RejectImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnErrorImpl.class);        

        // 1
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(this.data1));
    	assertTrue(result.getResult() instanceof TCConversationMessageImplWithPerm);
        
        TCConversationMessage tcm = (TCConversationMessage)result.getResult();

        assertTrue(tcm.getDialogTermitationPermission());
        assertTrue(ByteBufUtil.equals(tcm.getOriginatingTransactionId(), Unpooled.wrappedBuffer(trIdO)));
        assertTrue(ByteBufUtil.equals(tcm.getDestinationTransactionId(), Unpooled.wrappedBuffer(trIdD)));
        assertNull(tcm.getDialogPortion());
        assertEquals(tcm.getComponent().getComponents().size(), 1);
        Component cmp = tcm.getComponent().getComponents().get(0);
        assertEquals(cmp.getType(), ComponentType.InvokeLast);
        Invoke inv = (Invoke)cmp;
        assertFalse(inv.isNotLast());
        assertEquals((long) inv.getInvokeId(), 0);
        assertNull(inv.getCorrelationId());
        assertEquals(inv.getOperationCode().getPrivateOperationCode(), new Integer(2357));
        assertTrue(inv.getParameter() instanceof ASNOctetString); 
        ByteBuf realData=((ASNOctetString)inv.getParameter()).getValue();
        UserInformationElementTest.byteBufEquals(realData, Unpooled.wrappedBuffer(parData));

        // 2
        result=parser.decode(Unpooled.wrappedBuffer(this.data2));
    	assertTrue(result.getResult() instanceof TCConversationMessageImpl);
        
        tcm = (TCConversationMessage)result.getResult();

        assertFalse(tcm.getDialogTermitationPermission());
        assertTrue(ByteBufUtil.equals(tcm.getOriginatingTransactionId(), Unpooled.wrappedBuffer(trIdO)));
        assertTrue(ByteBufUtil.equals(tcm.getDestinationTransactionId(), Unpooled.wrappedBuffer(trIdD)));
        DialogPortion dp = tcm.getDialogPortion();
        assertNull(dp.getProtocolVersion());
        ApplicationContext ac = dp.getApplicationContext();
        assertEquals(ac.getInt(), new Integer(66));
        assertNull(dp.getConfidentiality());
        assertNull(dp.getSecurityContext());
        assertNull(dp.getUserInformation());

        assertNull(tcm.getComponent());

    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(TCConversationMessageImpl.class);
    	parser.loadClass(TCConversationMessageImplWithPerm.class);
    	
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, InvokeNotLastImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, InvokeLastImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnResultNotLastImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnResultLastImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, RejectImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnErrorImpl.class);        

        // 1
        List<Component> cc = new ArrayList<Component>(1);
        Invoke inv = TcapFactory.createComponentInvokeLast();
        
        cc.add(inv);
        inv.setInvokeId(0L);
        OperationCode oc = TcapFactory.createPrivateOperationCode(2357);
        inv.setOperationCode(oc);
        ASNOctetString innerValue=new ASNOctetString(Unpooled.wrappedBuffer(parData),null,null,null,false);        
        inv.setSetParameter(innerValue);

        TCConversationMessage tcm = TcapFactory.createTCConversationMessage(true);
        tcm.setOriginatingTransactionId(Unpooled.wrappedBuffer(trIdO));
        tcm.setDestinationTransactionId(Unpooled.wrappedBuffer(trIdD));
        
        ComponentPortionImpl cp=new ComponentPortionImpl();
        cp.setComponents(cc);
        tcm.setComponent(cp);

        ByteBuf encodedData=parser.encode(tcm);
        ByteBuf expectedData = Unpooled.wrappedBuffer(data1);
        UserInformationElementTest.byteBufEquals(encodedData, expectedData);

        // 2
        tcm = TcapFactory.createTCConversationMessage(false);
        tcm.setOriginatingTransactionId(Unpooled.wrappedBuffer(trIdO));
        tcm.setDestinationTransactionId(Unpooled.wrappedBuffer(trIdD));

        DialogPortion dp = TcapFactory.createDialogPortion();
        ApplicationContext ac = TcapFactory.createApplicationContext(66);
        dp.setApplicationContext(ac);
        tcm.setDialogPortion(dp);

        encodedData=parser.encode(tcm);
        expectedData = Unpooled.wrappedBuffer(data2);
        UserInformationElementTest.byteBufEquals(encodedData, expectedData);

    }

}
