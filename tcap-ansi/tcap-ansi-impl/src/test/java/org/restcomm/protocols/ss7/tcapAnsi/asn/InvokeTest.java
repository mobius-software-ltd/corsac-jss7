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

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.ASNInvokeSetParameterImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.InvokeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.InvokeLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.InvokeNotLastImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class InvokeTest {

    private byte[] data1 = new byte[] { (byte) 233, 43, (byte) 207, 1, 0, (byte) 209, 2, 9, 53, (byte) 242, 34, 4, 32, (byte) 159, 105, 0, (byte) 159, 116, 0,
            (byte) 159, (byte) 129, 0, 1, 8, (byte) 136, 5, 22, 25, 50, 4, 0, (byte) 159, (byte) 129, 65, 1, 1, (byte) 159, (byte) 129, 67, 5, 34, 34, 34, 34,
            34 };    

    private byte[] data2 = new byte[] { -19, 9, -49, 2, 20, 10, -48, 1, -13, -14, 0 };    

    private byte[] data3 = new byte[] { -19, 7, -49, 0, -48, 1, -13, -14, 0 };    

    private byte[] parData = new byte[] { -97, 105, 0, -97, 116, 0, -97, -127, 0, 1, 8, -120, 5, 22, 25, 50, 4, 0, -97, -127, 65, 1, 1, -97, -127, 67, 5, 34,
            34, 34, 34, 34 };

    @Test
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

    @Test
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
