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

package org.restcomm.protocols.ss7.tcap.asn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

import org.restcomm.protocols.ss7.tcap.asn.tx.DialogResponseAPDUImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class DialogResponseAPDUTest {

    private byte[] getData() {
        return new byte[] { 97, 44, (byte) 128, 2, 7, (byte) 128, (byte) 161, 9, 6, 7, 4, 0, 0, 1, 0, 21, 2, (byte) 162, 3, 2,
                1, 0, (byte) 163, 5, (byte) 161, 3, 2, 1, 0, (byte) 190, 15, 40, 13, 6, 7, 4, 0, 0, 1, 1, 1, 1, (byte) 160, 2,
                (byte) 161, 0 };
    }

    private byte[] getData2() {
        return new byte[] { 97, 27, (byte) 128, 2, 7, (byte) 128, (byte) 161, 9, 6, 7, 4, 0, 0, 1, 0, 25, 2, (byte) 162, 3, 2,
                1, 1, (byte) 163, 5, (byte) 161, 3, 2, 1, 2 };
    }

    private byte[] getData3() {
        return new byte[] { 97, 27, (byte) 128, 2, 7, (byte) 128, (byte) 161, 9, 6, 7, 4, 0, 0, 1, 0, 25, 2, (byte) 162, 3, 2,
                1, 1, (byte) 163, 5, (byte) 162, 3, 2, 1, 2 };
    }

    static ASNParser parser=new ASNParser();
	
    @BeforeClass
    public static void setUpClass() throws Exception {
    	parser.loadClass(DialogResponseAPDUImpl.class);
    	
    	parser.clearClassMapping(ASNUserInformationObjectImpl.class);
    	parser.registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, DialogResponseAPDUASN.class);    	
    }

    @Test
    public void testResponseAPDU() throws Exception {

    	Object output=parser.decode(Unpooled.wrappedBuffer(getData())).getResult();
        assertTrue(output instanceof DialogResponseAPDUImpl);
        DialogResponseAPDUImpl d = (DialogResponseAPDUImpl)output;
        
        assertEquals(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 21L, 2L }), d.getApplicationContextName().getOid());
        Result r = d.getResult();
        assertEquals(ResultType.Accepted, r.getResultType());
        ResultSourceDiagnostic diag = d.getResultSourceDiagnostic();
        assertNotNull(diag.getDialogServiceUserType());
        assertEquals(DialogServiceUserType.Null, diag.getDialogServiceUserType());
        UserInformation ui = d.getUserInformation();
        assertNotNull(ui);
        assertTrue(ui.isValueObject());
        assertTrue(ui.getChild() instanceof DialogResponseAPDUASN);
        assertTrue(((DialogResponseAPDUASN)ui.getChild()).getLength(parser)==0);
        
        ByteBuf buffer=parser.encode(d);
        assertTrue(Arrays.equals(getData(), buffer.array()));

        output=parser.decode(Unpooled.wrappedBuffer(getData2())).getResult();
        assertTrue(output instanceof DialogResponseAPDUImpl);
        d = (DialogResponseAPDUImpl)output;
        
        
        assertEquals(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 25L, 2L }), d.getApplicationContextName().getOid());
        r = d.getResult();
        assertEquals(ResultType.RejectedPermanent, r.getResultType());
        diag = d.getResultSourceDiagnostic();
        assertNotNull(diag.getDialogServiceUserType());
        assertEquals(DialogServiceUserType.AcnNotSupported, diag.getDialogServiceUserType());
        ui = d.getUserInformation();
        assertNull(ui);

        buffer=parser.encode(d);
        assertTrue(Arrays.equals(getData2(), buffer.array()));

        output=parser.decode(Unpooled.wrappedBuffer(getData3())).getResult();
        assertTrue(output instanceof DialogResponseAPDUImpl);
        d = (DialogResponseAPDUImpl)output;
        
        assertEquals(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 25L, 2L }), d.getApplicationContextName().getOid());
        r = d.getResult();
        assertEquals(ResultType.RejectedPermanent, r.getResultType());
        diag = d.getResultSourceDiagnostic();
        assertNotNull(diag.getDialogServiceProviderType());
        assertEquals(DialogServiceProviderType.NoCommonDialogPortion, diag.getDialogServiceProviderType());
        ui = d.getUserInformation();
        assertNull(ui);

        buffer=parser.encode(d);
        assertTrue(Arrays.equals(getData3(), buffer.array()));
    }

}
