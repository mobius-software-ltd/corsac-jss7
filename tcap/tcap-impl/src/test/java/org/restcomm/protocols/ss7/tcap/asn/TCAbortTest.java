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

package org.restcomm.protocols.ss7.tcap.asn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

import org.restcomm.protocols.ss7.tcap.TCAPTestUtils;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogAbortAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogRequestAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogResponseAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCAbortMessageImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNException;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class TCAbortTest {

    private byte[] getDataDialogPort() {
        return new byte[] { 0x67, 0x2E, 0x49, 0x04, 0x7B, (byte) 0xA5, 0x34, 0x13, 0x6B, 0x26, 0x28, 0x24, 0x06, 0x07, 0x00,
                0x11, (byte) 0x86, 0x05, 0x01, 0x01, 0x01, (byte) 0xA0, 0x19, 0x64, 0x17, (byte) 0x80, 0x01, 0x00, (byte) 0xBE,
                0x12, 0x28, 0x10, 0x06, 0x07, 0x04, 0x00, 0x00, 0x01, 0x01, 0x01, 0x01, (byte) 0xA0, 0x05, (byte) 0xA3, 0x03,
                0x0A, 0x01, 0x00 };
    }

    private byte[] getDataAbortCause() {
        return new byte[] { 103, 9, 73, 4, 123, -91, 52, 19, 74, 1, 126 };
    }

    private ByteBuf getDestTrId() {
        return Unpooled.wrappedBuffer(new byte[] { 0x7B, (byte) 0xA5, 0x34, 0x13 });
    }

    ASNParser parser=new ASNParser();
    
    @BeforeClass
	public void setUp()
	{		
    	parser.loadClass(TCAbortMessageImpl.class);
        
    	parser.clearClassMapping(ASNUserInformationObjectImpl.class);
    	parser.registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, TCAbortTestASN.class);
    	
    	parser.clearClassMapping(ASNDialogPortionObjectImpl.class);
    	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogRequestAPDUImpl.class);
    	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogResponseAPDUImpl.class);
    	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogAbortAPDUImpl.class);
	}
	
    @Test(groups = { "functional.encode" })
    public void testBasicTCAbortTestEncode() throws ParseException, ASNException {

    	// This Raw data is taken from ussd-abort- from msc2.txt
        byte[] expected = getDataDialogPort();

        TCAbortMessageImpl tcAbortMessage = new TCAbortMessageImpl();
        tcAbortMessage.setDestinationTransactionId(getDestTrId());

        DialogPortion dp = TcapFactory.createDialogPortion();
        dp.setUnidirectional(false);
        DialogAbortAPDU dapdu = TcapFactory.createDialogAPDUAbort();
        dapdu.setAbortSource(AbortSourceType.User);

        UserInformationImpl userInformation = new UserInformationImpl();
        
        userInformation.setIdentifier(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 1L, 1L, 1L }));

        TCAbortTestASN innerASN=new TCAbortTestASN(Unpooled.wrappedBuffer(new byte[] { (byte) 0x0A, 0x01, 0x00 }));
        
        userInformation.setChildAsObject(innerASN);

        dapdu.setUserInformation(userInformation);

        dp.setDialogAPDU(dapdu);

        tcAbortMessage.setDialogPortion(dp);

        ByteBuf buffer=parser.encode(tcAbortMessage);
        byte[] data = buffer.array();

        System.out.println(dump(data, data.length, false));

        TCAPTestUtils.compareArrays(expected, data);

        expected = getDataAbortCause();

        tcAbortMessage = new TCAbortMessageImpl();
        tcAbortMessage.setDestinationTransactionId(getDestTrId());
        tcAbortMessage.setPAbortCause(PAbortCauseType.AbnormalDialogue);

        buffer=parser.encode(tcAbortMessage);        
        data = buffer.array();

        TCAPTestUtils.compareArrays(expected, data);
    }

    @Test(groups = { "functional.decode" })
    public void testBasicTCAbortTestDecode() throws ParseException, ASNException {

    	// This Raw data is taken from ussd-abort- from msc2.txt
        byte[] data = getDataDialogPort();

        Object output=parser.decode(Unpooled.wrappedBuffer(data)).getResult();
        assertTrue(output instanceof TCAbortMessageImpl, "Expected TCAbort");

        TCAbortMessageImpl impl = (TCAbortMessageImpl)output;
        assertTrue(InvokeTest.byteBufEquals(impl.getDestinationTransactionId(), getDestTrId()));

        DialogPortion dp = impl.getDialogPortion();

        assertNotNull(dp);
        assertFalse(dp.isUnidirectional());

        DialogAPDU dialogApdu = dp.getDialogAPDU();

        assertNotNull(dialogApdu);

        data = getDataAbortCause();
        output=parser.decode(Unpooled.wrappedBuffer(data)).getResult();
        assertTrue(output instanceof TCAbortMessageImpl, "Expected TCAbort");

        impl = (TCAbortMessageImpl)output;
        
        assertTrue(InvokeTest.byteBufEquals(impl.getDestinationTransactionId(), getDestTrId()));
        // assertTrue(2074424339 == impl.getDestinationTransactionId());

        dp = impl.getDialogPortion();
        assertNull(dp);
        assertEquals(PAbortCauseType.AbnormalDialogue, impl.getPAbortCause());
    }

    public static final String dump(byte[] buff, int size, boolean asBits) {
        String s = "";
        for (int i = 0; i < size; i++) {
            String ss = null;
            if (!asBits) {
                ss = Integer.toHexString(buff[i] & 0xff);
            } else {
                ss = Integer.toBinaryString(buff[i] & 0xff);
            }
            ss = fillInZeroPrefix(ss, asBits);
            s += " " + ss;
        }
        return s;
    }

    public static final String fillInZeroPrefix(String ss, boolean asBits) {
        if (asBits) {
            if (ss.length() < 8) {
                for (int j = ss.length(); j < 8; j++) {
                    ss = "0" + ss;
                }
            }
        } else {
            // hex
            if (ss.length() < 2) {

                ss = "0" + ss;
            }
        }

        return ss;
    }

}
