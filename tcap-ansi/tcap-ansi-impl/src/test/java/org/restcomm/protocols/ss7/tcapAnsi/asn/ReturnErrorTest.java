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
import static org.junit.Assert.assertTrue;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ErrorCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ReturnError;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.ReturnErrorImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class ReturnErrorTest {

    private byte[] data1 = new byte[] { -21, 15, -49, 1, 5, -44, 1, 14, -14, 7, 4, 5, 1, 2, 3, 4, 5 };

    private byte[] parData = new byte[] { 1, 2, 3, 4, 5 };

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(ReturnErrorImpl.class);
    	
        // 1
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(this.data1));
    	assertTrue(result.getResult() instanceof ReturnErrorImpl);
    	ReturnErrorImpl re = (ReturnErrorImpl)result.getResult();
        
        assertEquals((long) re.getCorrelationId(), 5);
        assertEquals(re.getErrorCode().getPrivateErrorCode(), new Integer(14));
        assertTrue(re.getParameter() instanceof ASNOctetString);
        assertEquals(((ASNOctetString)re.getParameter()).getValue(), Unpooled.wrappedBuffer(parData));
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(ReturnErrorImpl.class);
    	
        // 1
        ReturnError re = TcapFactory.createComponentReturnError();
        re.setCorrelationId(5L);
        ErrorCode ec = TcapFactory.createPrivateErrorCode(14);
        re.setErrorCode(ec);
        ASNOctetString p=new ASNOctetString(Unpooled.wrappedBuffer(parData),null,null,null,false);
        re.setSetParameter(p);
              
        ByteBuf encodedData=parser.encode(re);
        ByteBuf expectedData = Unpooled.wrappedBuffer(data1);
        UserInformationElementTest.byteBufEquals(encodedData, expectedData);        
    }
}
