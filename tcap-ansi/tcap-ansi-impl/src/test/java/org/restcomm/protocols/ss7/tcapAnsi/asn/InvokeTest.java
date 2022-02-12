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

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.ASNInvokeSetParameterImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.InvokeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.InvokeLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.InvokeNotLastImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class InvokeTest {

    private byte[] data1 = new byte[] { (byte) 233, 43, (byte) 207, 1, 0, (byte) 209, 2, 9, 53, (byte) 242, 34, 4, 32, (byte) 159, 105, 0, (byte) 159, 116, 0,
            (byte) 159, (byte) 129, 0, 1, 8, (byte) 136, 5, 22, 25, 50, 4, 0, (byte) 159, (byte) 129, 65, 1, 1, (byte) 159, (byte) 129, 67, 5, 34, 34, 34, 34,
            34 };    

    private byte[] data2 = new byte[] { -19, 9, -49, 2, 20, 10, -48, 1, -13, -14, 0 };    

    private byte[] data3 = new byte[] { -19, 7, -49, 0, -48, 1, -13, -14, 0 };    

    private byte[] parData = new byte[] { -97, 105, 0, -97, 116, 0, -97, -127, 0, 1, 8, -120, 5, 22, 25, 50, 4, 0, -97, -127, 65, 1, 1, -97, -127, 67, 5, 34,
            34, 34, 34, 34 };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(InvokeNotLastImpl.class);
    	parser.loadClass(InvokeLastImpl.class);
        // 1
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(this.data1));
    	assertTrue(result.getResult() instanceof InvokeLastImpl);
        InvokeImpl inv=(InvokeImpl)result.getResult();

        assertFalse(inv.isNotLast());
        assertEquals((long) inv.getInvokeId(), 0);
        assertNull(inv.getCorrelationId());
        assertEquals(inv.getOperationCode().getPrivateOperationCode(), new Integer(2357));
        assertTrue(inv.getParameter() instanceof ASNOctetString);
        UserInformationElementTest.byteBufEquals(((ASNOctetString)inv.getParameter()).getValue(), Unpooled.wrappedBuffer(parData));

        // 2
        result=parser.decode(Unpooled.wrappedBuffer(this.data2));
    	assertTrue(result.getResult() instanceof InvokeNotLastImpl);
        inv=(InvokeImpl)result.getResult();        
        assertTrue(inv.isNotLast());
        assertEquals((long) inv.getInvokeId(), 20);
        assertEquals((long) inv.getCorrelationId(), 10);
        assertEquals(inv.getOperationCode().getNationalOperationCode(), new Integer(-13));
        assertNull(inv.getParameter());        

        // 3
        result=parser.decode(Unpooled.wrappedBuffer(this.data3));
    	assertTrue(result.getResult() instanceof InvokeNotLastImpl);
        inv=(InvokeImpl)result.getResult();        

        assertTrue(inv.isNotLast());
        assertNull(inv.getInvokeId());
        assertNull(inv.getCorrelationId());
        assertEquals(inv.getOperationCode().getNationalOperationCode(), new Integer(-13));
        assertNull(inv.getParameter());
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(InvokeNotLastImpl.class);
    	parser.loadClass(InvokeLastImpl.class);
        // 1
        Invoke inv = TcapFactory.createComponentInvokeLast();
        inv.setInvokeId(0L);
        OperationCode oc = TcapFactory.createPrivateOperationCode(2357);
        inv.setOperationCode(oc);
        ASNOctetString innerValue=new ASNOctetString(Unpooled.wrappedBuffer(parData),null,null,null,false);
        ASNInvokeSetParameterImpl p=new ASNInvokeSetParameterImpl(innerValue);
        inv.setSetParameter(p);

        ByteBuf encodedData=parser.encode(inv);
        ByteBuf expectedData = Unpooled.wrappedBuffer(data1);
        UserInformationElementTest.byteBufEquals(encodedData, expectedData);

        // 2
        inv = TcapFactory.createComponentInvokeNotLast();
        inv.setInvokeId(20L);
        inv.setCorrelationId(10L);
        OperationCode noc = TcapFactory.createNationalOperationCode(-13);
        inv.setOperationCode(noc);
        p=new ASNInvokeSetParameterImpl();
        inv.setSetParameter(p);

        encodedData=parser.encode(inv);
        expectedData = Unpooled.wrappedBuffer(data2);
        UserInformationElementTest.byteBufEquals(encodedData, expectedData);

        // 3
        inv = TcapFactory.createComponentInvokeNotLast();
        noc = TcapFactory.createNationalOperationCode(-13);
        inv.setOperationCode(noc);
        p=new ASNInvokeSetParameterImpl();
        inv.setSetParameter(p);

        encodedData=parser.encode(inv);
        expectedData = Unpooled.wrappedBuffer(data2);
        UserInformationElementTest.byteBufEquals(encodedData, expectedData);
    }

}
