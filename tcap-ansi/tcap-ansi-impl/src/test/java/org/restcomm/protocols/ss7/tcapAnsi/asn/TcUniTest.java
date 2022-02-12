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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCUniMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.WrappedComponent;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.ComponentPortionImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.WrappedComponentImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class TcUniTest {

    private byte[] data1 = new byte[] { -31, 22, -57, 0, -24, 18, -23, 16, -49, 1, 0, -47, 2, 9, 53, -14, 7, 4, 5, 3, 4, 5, 6, 7 };

    private byte[] data2 = new byte[] { -31, 27, -57, 0, -7, 3, -37, 1, 66, -24, 18, -23, 16, -49, 1, 0, -47, 2, 9, 53, -14, 7, 4, 5, 3, 4, 5, 6, 7 };

    private byte[] parData = new byte[] { 3, 4, 5, 6, 7 };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(TCUniMessageImpl.class);
    	
        // 1
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(this.data1));
        assertTrue(result.getResult() instanceof TCUniMessageImpl);
        TCUniMessage tcm = (TCUniMessage)result.getResult();

        assertNull(tcm.getDialogPortion());
        assertEquals(tcm.getComponent().getComponents().size(), 1);
        WrappedComponent cmp = tcm.getComponent().getComponents().get(0);
        assertEquals(cmp.getType(), ComponentType.InvokeLast);
        Invoke inv = cmp.getInvokeLast();
        assertFalse(inv.isNotLast());
        assertEquals((long) inv.getInvokeId(), 0);
        assertNull(inv.getCorrelationId());
        assertEquals(inv.getOperationCode().getPrivateOperationCode(), new Integer(2357));
        assertTrue(inv.getParameter() instanceof ASNOctetString);
        UserInformationElementTest.byteBufEquals(((ASNOctetString)inv.getParameter()).getValue(), Unpooled.wrappedBuffer(parData));

        // 2
        result=parser.decode(Unpooled.wrappedBuffer(this.data2));
        assertTrue(result.getResult() instanceof TCUniMessageImpl);
        tcm = (TCUniMessage)result.getResult();

        assertEquals(tcm.getComponent().getComponents().size(), 1);
        cmp = tcm.getComponent().getComponents().get(0);
        assertEquals(cmp.getType(), ComponentType.InvokeLast);
        inv = cmp.getInvokeLast();
        assertFalse(inv.isNotLast());
        assertEquals((long) inv.getInvokeId(), 0);
        assertNull(inv.getCorrelationId());
        assertEquals(inv.getOperationCode().getPrivateOperationCode(), new Integer(2357));
        assertTrue(inv.getParameter() instanceof ASNOctetString);
        UserInformationElementTest.byteBufEquals(((ASNOctetString)inv.getParameter()).getValue(), Unpooled.wrappedBuffer(parData));

        DialogPortion dp = tcm.getDialogPortion();
        assertNull(dp.getProtocolVersion());
        ApplicationContext ac = dp.getApplicationContext();
        assertEquals(ac.getInt(), new Integer(66));
        assertNull(dp.getConfidentiality());
        assertNull(dp.getSecurityContext());
        assertNull(dp.getUserInformation());
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(TCUniMessageImpl.class);
    	        
        // 1
        List<WrappedComponent> cc = new ArrayList<WrappedComponent>(1);
        Invoke inv = TcapFactory.createComponentInvokeNotLast();
        WrappedComponentImpl component=new WrappedComponentImpl();
        component.setInvoke(inv);
        cc.add(component);
        inv.setInvokeId(0L);
        OperationCode oc = TcapFactory.createPrivateOperationCode(2357);        
        inv.setOperationCode(oc);
        ASNOctetString p = new ASNOctetString(Unpooled.wrappedBuffer(parData),null,null,null,false);
        inv.setSetParameter(p);

        TCUniMessage tcm = TcapFactory.createTCUniMessage();
        ComponentPortionImpl cp=new ComponentPortionImpl();
        cp.setComponents(cc);
        tcm.setComponent(cp);

        ByteBuf encodedData=parser.encode(tcm);
        ByteBuf expectedData = Unpooled.wrappedBuffer(data1);
        UserInformationElementTest.byteBufEquals(encodedData, expectedData);

        // 2
        tcm = TcapFactory.createTCUniMessage();
        tcm.setComponent(cp);

        DialogPortion dp = TcapFactory.createDialogPortion();
        ApplicationContext ac = TcapFactory.createApplicationContext(66);
        dp.setApplicationContext(ac);
        tcm.setDialogPortion(dp);

        encodedData=parser.encode(tcm);
        expectedData = Unpooled.wrappedBuffer(data1);
        UserInformationElementTest.byteBufEquals(encodedData, expectedData);
    }
}