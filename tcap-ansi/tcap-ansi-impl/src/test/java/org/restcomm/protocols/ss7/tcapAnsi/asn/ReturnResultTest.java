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

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ReturnResultLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ReturnResultNotLastImpl;
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
public class ReturnResultTest {

    private byte[] data1 = new byte[] { (byte) 0xea, 0x1f, (byte) 0xcf, 0x01, 0x00, (byte) 0xf2, 0x1a, 0x04, 0x18, (byte) 0x89, 0x04, (byte) 0xfe, 0x3a, 0x2f, (byte) 0xe5,
            (byte) 0x9f, (byte) 0x81, 0x38, 0x05, 0x00, 0x00, 0x00, 0x26, 0x31, (byte) 0x95, 0x03, 0x00, 0x0c, 0x06, (byte) 0x9f, 0x31, 0x01, 0x00 };

    private byte[] data2 = new byte[] { -18, 5, -49, 1, -1, -14, 0 };

    private byte[] parData = new byte[] { (byte) 0x89, 0x04, (byte) 0xfe, 0x3a, 0x2f, (byte) 0xe5, (byte) 0x9f, (byte) 0x81, 0x38, 0x05, 0x00, 0x00, 0x00,
            0x26, 0x31, (byte) 0x95, 0x03, 0x00, 0x0c, 0x06, (byte) 0x9f, 0x31, 0x01, 0x00 };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(ReturnResultLastImpl.class);
    	parser.loadClass(ReturnResultNotLastImpl.class);
    	
        // 1
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(this.data1));
    	assertTrue(result.getResult() instanceof ReturnResultLastImpl);        

    	ReturnResultLastImpl rrl = (ReturnResultLastImpl)result.getResult();
        
        assertEquals((long) rrl.getCorrelationId(), 0);        
        assertTrue(rrl.getParameter() instanceof ASNOctetString);
        UserInformationElementTest.byteBufEquals(((ASNOctetString)rrl.getParameter()).getValue(), Unpooled.wrappedBuffer(parData));

        // 2
        result=parser.decode(Unpooled.wrappedBuffer(this.data2));
    	assertTrue(result.getResult() instanceof ReturnResultNotLastImpl);        

    	ReturnResultNotLastImpl rrnl = (ReturnResultNotLastImpl)result.getResult();
        
        assertEquals((long) rrnl.getCorrelationId(), -1);
        assertNull(rrnl.getParameter());
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(ReturnResultLastImpl.class);
    	parser.loadClass(ReturnResultNotLastImpl.class);
    	
        // 1
        ReturnResultLastImpl rrl = TcapFactory.createComponentReturnResultLast();
        rrl.setCorrelationId(0L);
        ASNOctetString p=new ASNOctetString();
        p.setValue(Unpooled.wrappedBuffer(parData));
        rrl.setSetParameter(p);        

        ByteBuf encodedData=parser.encode(rrl);
        ByteBuf expectedData = Unpooled.wrappedBuffer(data1);
        UserInformationElementTest.byteBufEquals(encodedData, expectedData);

        // 2
        ReturnResultNotLastImpl rrnl = TcapFactory.createComponentReturnResultNotLast();
        rrnl.setCorrelationId(-1L);
        rrl.setSetParameter(null);

        encodedData=parser.encode(rrl);
        expectedData = Unpooled.wrappedBuffer(data2);
        UserInformationElementTest.byteBufEquals(encodedData, expectedData);
    }
}