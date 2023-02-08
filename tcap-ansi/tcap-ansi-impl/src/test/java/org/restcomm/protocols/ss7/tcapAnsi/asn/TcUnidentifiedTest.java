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

import static org.testng.Assert.*;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCUnifiedMessage;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
/**
 * 
 * @author yulianoifa
 *
 */
@Test(groups = { "asn" })
public class TcUnidentifiedTest {

    private byte[] data1 = new byte[] { -26, 15, -57, 8, 1, 1, 2, 2, 3, 3, 4, 4, -7, 3, -37, 1, 66 };

    private byte[] trIdO = new byte[] { 1, 1, 2, 2 };
    private byte[] trIdD = new byte[] { 3, 3, 4, 4 };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(TCUniMessageImpl.class);
    	parser.loadClass(TCAbortMessageImpl.class);
    	parser.loadClass(TCConversationMessageImpl.class);
    	parser.loadClass(TCConversationMessageImplWithPerm.class);
    	parser.loadClass(TCQueryMessageImplWithPerm.class);
    	parser.loadClass(TCQueryMessageImpl.class);
    	parser.loadClass(TCResponseMessageImpl.class);
    	
        // 1
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data1));
        assertTrue(result.getResult() instanceof TCUnifiedMessage);
        TCUnifiedMessage tcm = (TCUnifiedMessage)result.getResult();        

        assertTrue(ByteBufUtil.equals(tcm.getOriginatingTransactionId(), Unpooled.wrappedBuffer(trIdO)));
        assertTrue(ByteBufUtil.equals(tcm.getDestinationTransactionId(), Unpooled.wrappedBuffer(trIdD)));
        assertTrue(tcm.isDialogPortionExists());
    }
}