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

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.RejectImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

/**
 *
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class RejectTest {

    private byte[] data1 = new byte[] { -20, 9, -49, 1, 5, -43, 2, 2, 3, -16, 0 };

    private byte[] data2 = new byte[] { -20, 8, -49, 0, -43, 2, 2, 3, -16, 0 };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(RejectImpl.class);
    	
        // 1
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(this.data1));
    	assertTrue(result.getResult() instanceof RejectImpl);
        RejectImpl rej = (RejectImpl)result.getResult();
        
        assertEquals((long) rej.getCorrelationId(), 5);
        assertEquals(rej.getProblem(), RejectProblem.invokeIncorrectParameter);
        assertFalse(rej.isLocalOriginated());

        // 2
        result=parser.decode(Unpooled.wrappedBuffer(this.data2));
    	assertTrue(result.getResult() instanceof RejectImpl);
        rej = (RejectImpl)result.getResult();

        assertNull(rej.getCorrelationId());
        assertEquals(rej.getProblem(), RejectProblem.invokeIncorrectParameter);
        assertFalse(rej.isLocalOriginated());
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(RejectImpl.class);
    	        
        // 1
        RejectImpl rej = TcapFactory.createComponentReject();
        rej.setCorrelationId(5L);
        rej.setProblem(RejectProblem.invokeIncorrectParameter);

        ByteBuf encodedData=parser.encode(rej);
        ByteBuf expectedData = Unpooled.wrappedBuffer(data1);
        UserInformationElementTest.byteBufEquals(encodedData, expectedData);

        // 2
        rej = TcapFactory.createComponentReject();
        rej.setProblem(RejectProblem.invokeIncorrectParameter);

        encodedData=parser.encode(rej);
        expectedData = Unpooled.wrappedBuffer(data2);
        UserInformationElementTest.byteBufEquals(encodedData, expectedData);
    }
}
