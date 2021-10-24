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
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ProtocolVersion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ErrorCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Return;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ReturnError;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCQueryMessage;
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
public class TcQueryTest {

    // no DialogPortion, 1 Invoke
    private byte[] data1 = new byte[] { (byte) 0xe2, 0x35, (byte) 0xc7, 0x04, 0x00, 0x00, 0x00, 0x00, (byte) 0xe8, 0x2d, (byte) 0xe9, 0x2b, (byte) 0xcf, 0x01,
            0x00, (byte) 0xd1, 0x02, 0x09, 0x35, (byte) 0xf2, 0x22, 4, 32, (byte) 0x9f, 0x69, 0x00, (byte) 0x9f, 0x74, 0x00, (byte) 0x9f, (byte) 0x81, 0x00, 0x01,
            0x08, (byte) 0x88, 0x05, 0x16, 0x19, 0x32, 0x04, 0x00, (byte) 0x9f, (byte) 0x81, 0x41, 0x01, 0x01, (byte) 0x9f, (byte) 0x81, 0x43, 0x05, 0x22,
            0x22, 0x22, 0x22, 0x22 };

    // DialogPortion[Version + ACN], ReturnResult + ReturnError
    private byte[] data2 = new byte[] { -29, 42, -57, 4, 3, 3, 4, 4, -7, 7, -38, 1, 3, -36, 2, 48, 11, -24, 25, -22, 12, -49, 1, 1, -14, 7, 4, 5, 3, 4, 5, 6,
            7, -21, 9, -49, 1, 1, -44, 2, 0, -56, -14, 0 };

    // DialogPortion[ACN], no components
    private byte[] data3 = new byte[] { -29, 11, -57, 4, 3, 3, 4, 4, -7, 3, -37, 1, 66 };

    private byte[] trId = new byte[] { 0, 0, 0, 0 };
    private byte[] trId2 = new byte[] { 3, 3, 4, 4 };

    private byte[] parData = new byte[] { -97, 105, 0, -97, 116, 0, -97, -127, 0, 1, 8, -120, 5, 22, 25, 50, 4, 0, -97, -127, 65, 1, 1, -97, -127, 67, 5, 34,
            34, 34, 34, 34 };

    private byte[] parData2 = new byte[] { 3, 4, 5, 6, 7 };

    private List<Long> acn = Arrays.asList(new Long[] { 1L, 8L, 11L });

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(TCQueryMessageImpl.class);
    	parser.loadClass(TCQueryMessageImplWithPerm.class);
    	
        // 1
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(this.data1));
    	assertTrue(result.getResult() instanceof TCQueryMessageImplWithPerm);
        TCQueryMessage tcm = (TCQueryMessage)result.getResult();

        assertTrue(tcm.getDialogTermitationPermission());
        assertEquals(tcm.getOriginatingTransactionId(), trId);
        assertNull(tcm.getDialogPortion());
        assertEquals(tcm.getComponent().getComponents().size(), 1);
        WrappedComponent cmp = tcm.getComponent().getComponents().get(0);
        assertEquals(cmp.getType(), ComponentType.InvokeLast);
        Invoke inv = cmp.getInvokeLast();
        assertFalse(inv.isNotLast());
        assertEquals((long) inv.getInvokeId(), 0);
        assertNull(inv.getCorrelationId());
        assertEquals(inv.getOperationCode().getPrivateOperationCode(), new Long(2357L));
        assertTrue(inv.getParameter() instanceof ASNOctetString);
        UserInformationElementTest.byteBufEquals(((ASNOctetString)inv.getParameter()).getValue(), Unpooled.wrappedBuffer(parData));

        // 2
        result=parser.decode(Unpooled.wrappedBuffer(this.data2));
    	assertTrue(result.getResult() instanceof TCQueryMessageImpl);
        tcm = (TCQueryMessage)result.getResult();

        assertFalse(tcm.getDialogTermitationPermission());
        assertEquals(tcm.getOriginatingTransactionId(), trId2);
        DialogPortion dp = tcm.getDialogPortion();
        ProtocolVersion pv = dp.getProtocolVersion();
        assertTrue(pv.isT1_114_1996Supported());
        assertTrue(pv.isT1_114_2000Supported());
        ApplicationContext ac = dp.getApplicationContext();
        assertEquals(ac.getObj(), acn);
        assertNull(dp.getConfidentiality());
        assertNull(dp.getSecurityContext());
        assertNull(dp.getUserInformation());

        assertEquals(tcm.getComponent().getComponents().size(), 2);
        cmp = tcm.getComponent().getComponents().get(0);
        assertEquals(cmp.getType(), ComponentType.ReturnResultLast);
        Return rrl = cmp.getReturnResultLast();
        assertEquals((long)rrl.getCorrelationId(), 1);
        ASNOctetString p = (ASNOctetString)rrl.getParameter();
        UserInformationElementTest.byteBufEquals(p.getValue(), Unpooled.wrappedBuffer(parData2));

        cmp = tcm.getComponent().getComponents().get(1);
        assertEquals(cmp.getType(), ComponentType.ReturnError);
        ReturnError re = cmp.getReturnError();
        assertEquals((long) re.getCorrelationId(), 1);
        ErrorCode ec = re.getErrorCode();
        assertEquals(ec.getPrivateErrorCode(), new Long(200L));
        assertNull(re.getParameter());
        
        // 3
        result=parser.decode(Unpooled.wrappedBuffer(this.data3));
    	assertTrue(result.getResult() instanceof TCQueryMessageImpl);
        tcm = (TCQueryMessage)result.getResult();

        assertFalse(tcm.getDialogTermitationPermission());
        assertEquals(tcm.getOriginatingTransactionId(), trId2);
        dp = tcm.getDialogPortion();
        assertNull(dp.getProtocolVersion());
        ac = dp.getApplicationContext();        
        assertEquals(ac.getInt(), new Long(66L));
        assertNull(dp.getConfidentiality());
        assertNull(dp.getSecurityContext());
        assertNull(dp.getUserInformation());

        assertNull(tcm.getComponent());           
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(TCQueryMessageImpl.class);
    	parser.loadClass(TCQueryMessageImplWithPerm.class);
    	
        // 1
        List<WrappedComponent> cc = new ArrayList<WrappedComponent>(1);
        Invoke inv = TcapFactory.createComponentInvokeNotLast();
        WrappedComponentImpl component=new WrappedComponentImpl();
        component.setInvoke(inv);
        cc.add(component);
        inv.setInvokeId(0L);
        OperationCode oc = TcapFactory.createPrivateOperationCode(2357L);        
        inv.setOperationCode(oc);
        ASNOctetString p=new ASNOctetString();
        p.setValue(Unpooled.wrappedBuffer(parData));
        inv.setSetParameter(p);

        TCQueryMessage tcm = TcapFactory.createTCQueryMessage(true);
        tcm.setOriginatingTransactionId(trId);
        ComponentPortionImpl cp=new ComponentPortionImpl();
        cp.setComponents(cc);
        tcm.setComponent(cp);
        
        ByteBuf encodedData=parser.encode(tcm);
        ByteBuf expectedData = Unpooled.wrappedBuffer(data1);
        UserInformationElementTest.byteBufEquals(encodedData, expectedData);

        // 2
        cc = new ArrayList<WrappedComponent>(2);
        Return rr = TcapFactory.createComponentReturnResultLast();
        component=new WrappedComponentImpl();
        component.setReturnResultLast(rr);
        cc.add(component);
        rr.setCorrelationId(1L);
        p=new ASNOctetString();
        p.setValue(Unpooled.wrappedBuffer(parData2));
        rr.setSetParameter(p);
        ReturnError re = TcapFactory.createComponentReturnError();
        component=new WrappedComponentImpl();
        component.setReturnError(re);
        cc.add(component);
        re.setCorrelationId(1L);
        ErrorCode ec = TcapFactory.createPrivateErrorCode(200L);
        re.setErrorCode(ec);
        re.setSetParameter(null);

        tcm = TcapFactory.createTCQueryMessage(false);
        tcm.setOriginatingTransactionId(trId2);
        cp=new ComponentPortionImpl();
        cp.setComponents(cc);
        tcm.setComponent(cp);
        
        DialogPortion dp = TcapFactory.createDialogPortion();
        ProtocolVersion pv = TcapFactory.createProtocolVersionFull();
        dp.setProtocolVersion(pv);
        ApplicationContext ac = TcapFactory.createApplicationContext(acn);
        dp.setApplicationContext(ac);
        tcm.setDialogPortion(dp);

        encodedData=parser.encode(tcm);
        expectedData = Unpooled.wrappedBuffer(data2);
        UserInformationElementTest.byteBufEquals(encodedData, expectedData);

        // 3
        tcm = TcapFactory.createTCQueryMessage(false);
        tcm.setOriginatingTransactionId(trId2);
        
        dp = TcapFactory.createDialogPortion();
        ac = TcapFactory.createApplicationContext(66);
        dp.setApplicationContext(ac);
        tcm.setDialogPortion(dp);

        encodedData=parser.encode(tcm);
        expectedData = Unpooled.wrappedBuffer(data3);
        UserInformationElementTest.byteBufEquals(encodedData, expectedData);
    }

}
