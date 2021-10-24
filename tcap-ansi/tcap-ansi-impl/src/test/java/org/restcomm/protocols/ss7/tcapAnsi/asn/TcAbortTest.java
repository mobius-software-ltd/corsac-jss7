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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCAbortMessage;
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
public class TcAbortTest {

    private byte[] data1 = new byte[] { -10, 9, -57, 4, 20, 0, 0, 0, -41, 1, 6 };

    private byte[] data2 = new byte[] { -10, 13, -57, 4, 20, 0, 0, 0, -7, 3, -37, 1, 111, -8, 0 };

    private byte[] data3 = new byte[] { -10, 28, -57, 4, 20, 0, 0, 0, -8, 20, 40, 18, 6, 7, 4, 0, 0, 1, 1, 1, 1, -96, 7, 4, 5, 3, 4, 5, 6, 7 };

    private byte[] trId = new byte[] { 20, 0, 0, 0 };

    private byte[] dataValue = new byte[] { 3, 4, 5, 6, 7 };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(TCAbortMessageImpl.class);
    	
        // 1
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(this.data1));
        assertTrue(result.getResult() instanceof TCAbortMessageImpl);
        TCAbortMessage tcm = (TCAbortMessageImpl)result.getResult();

        assertEquals(tcm.getDestinationTransactionId(), trId);
        assertNull(tcm.getDialogPortion());
        assertNull(tcm.getUserAbortInformation());
        assertEquals(tcm.getPAbortCause(), PAbortCause.ResourceUnavailable);

        // 2
        result=parser.decode(Unpooled.wrappedBuffer(this.data2));
        assertTrue(result.getResult() instanceof TCAbortMessageImpl);
        tcm = (TCAbortMessageImpl)result.getResult();

        assertEquals(tcm.getDestinationTransactionId(), trId);
        assertNull(tcm.getUserAbortInformation().getUserInformationElements());
        assertNull(tcm.getPAbortCause());
        DialogPortion dp = tcm.getDialogPortion();
        assertEquals(dp.getApplicationContext().getInt(), new Long(111L));

        // 3
        result=parser.decode(Unpooled.wrappedBuffer(this.data3));
        assertTrue(result.getResult() instanceof TCAbortMessageImpl);
        tcm = (TCAbortMessageImpl)result.getResult();

        assertEquals(tcm.getDestinationTransactionId(), trId);
        assertNull(tcm.getPAbortCause());
        assertNull(tcm.getDialogPortion());

        UserInformation uie = tcm.getUserAbortInformation();
        assertTrue(uie.getUserInformationElements().get(0).isIDObjectIdentifier());
        assertEquals(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 1L, 1L, 1L }), uie.getUserInformationElements().get(0).getObjectIdentifier());
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(TCAbortMessageImpl.class);
    	
        // 1
        TCAbortMessage tcm = TcapFactory.createTCAbortMessage();
        tcm.setDestinationTransactionId(trId);
        tcm.setPAbortCause(PAbortCause.ResourceUnavailable);

        ByteBuf buffer=parser.encode(tcm);
        ByteBuf expectedData = Unpooled.wrappedBuffer(data1);
        UserInformationElementTest.byteBufEquals(buffer, expectedData);

        // 2
        tcm = TcapFactory.createTCAbortMessage();
        tcm.setDestinationTransactionId(trId);
        DialogPortion dp = TcapFactory.createDialogPortion();
        ApplicationContext ac = TcapFactory.createApplicationContext(111);
        dp.setApplicationContext(ac);
        tcm.setDialogPortion(dp);

        buffer=parser.encode(tcm);
        expectedData = Unpooled.wrappedBuffer(data2);
        UserInformationElementTest.byteBufEquals(buffer, expectedData);

        // 3
        tcm = TcapFactory.createTCAbortMessage();
        tcm.setDestinationTransactionId(trId);
        UserInformationElementImpl uai = new UserInformationElementImpl();
        uai.setIdentifier(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 1L, 1L, 1L }));

        ASNOctetString innerValue=new ASNOctetString();
        innerValue.setValue(Unpooled.wrappedBuffer(dataValue));
        
        ASNUserInformationObjectImpl value=new ASNUserInformationObjectImpl();
        value.setValue(innerValue);
        uai.setChildAsObject(value);
        
        UserInformationImpl abortInfo=new UserInformationImpl();
        abortInfo.setUserInformationElements(Arrays.asList(new UserInformationElementImpl[] { uai }));
        tcm.setUserAbortInformation(abortInfo);

        buffer=parser.encode(tcm);
        expectedData = Unpooled.wrappedBuffer(data3);
        UserInformationElementTest.byteBufEquals(buffer, expectedData);

    }

}
