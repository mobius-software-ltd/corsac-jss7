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
import static org.testng.Assert.assertTrue;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ErrorCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ReturnError;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.ReturnErrorImpl;
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
public class ReturnErrorTest {

    private byte[] data1 = new byte[] { -21, 15, -49, 1, 5, -44, 1, 14, -14, 7, 4, 5, 1, 2, 3, 4, 5 };

    private byte[] parData = new byte[] { 1, 2, 3, 4, 5 };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(ReturnErrorImpl.class);
    	
        // 1
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(this.data1));
    	assertTrue(result.getResult() instanceof ReturnErrorImpl);
    	ReturnErrorImpl re = (ReturnErrorImpl)result.getResult();
        
        assertEquals((long) re.getCorrelationId(), 5);
        assertEquals(re.getErrorCode().getPrivateErrorCode(), new Long(14L));
        assertTrue(re.getParameter() instanceof ASNOctetString);
        assertEquals(((ASNOctetString)re.getParameter()).getValue(), Unpooled.wrappedBuffer(parData));
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(ReturnErrorImpl.class);
    	
        // 1
        ReturnError re = TcapFactory.createComponentReturnError();
        re.setCorrelationId(5L);
        ErrorCode ec = TcapFactory.createPrivateErrorCode(14L);
        re.setErrorCode(ec);
        ASNOctetString p=new ASNOctetString(Unpooled.wrappedBuffer(parData),null,null,null,false);
        re.setSetParameter(p);
              
        ByteBuf encodedData=parser.encode(re);
        ByteBuf expectedData = Unpooled.wrappedBuffer(data1);
        UserInformationElementTest.byteBufEquals(encodedData, expectedData);        
    }
}
