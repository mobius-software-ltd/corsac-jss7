/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

import org.restcomm.protocols.ss7.tcap.asn.DialogServiceProviderType;
import org.restcomm.protocols.ss7.tcap.asn.DialogServiceUserType;
import org.restcomm.protocols.ss7.tcap.asn.ResultType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNGeneric;

/**
 *
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
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

    @BeforeClass
    public static void setUpClass() throws Exception {
    	ASNGeneric.clear(ASNUserInformationObjectImpl.class);
    	ASNGeneric.registerAlternative(ASNUserInformationObjectImpl.class, DialogResponseAPDUASN.class);    	
    }

    @Test(groups = { "functional.encode", "functional.decode" })
    public void testResponseAPDU() throws Exception {

    	ASNParser parser=new ASNParser();
    	parser.loadClass(DialogResponseAPDUImpl.class);
    	
    	Object output=parser.decode(Unpooled.wrappedBuffer(getData())).getResult();
        assertTrue(output instanceof DialogResponseAPDUImpl);
        DialogResponseAPDUImpl d = (DialogResponseAPDUImpl)output;
        
        assertEquals(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 21L, 2L }), d.getApplicationContextName().getValue());
        ResultImpl r = d.getResult();
        assertEquals(ResultType.Accepted, r.getResultType());
        ResultSourceDiagnosticImpl diag = d.getResultSourceDiagnostic();
        assertNotNull(diag.getDialogServiceUserType());
        assertEquals(DialogServiceUserType.Null, diag.getDialogServiceUserType());
        UserInformationImpl ui = d.getUserInformation();
        assertNotNull(ui);
        assertTrue(ui.getExternal().isValueObject());
        assertTrue(ui.getExternal().getChild().getValue() instanceof DialogResponseAPDUASN);
        assertTrue(((DialogResponseAPDUASN)ui.getExternal().getChild().getValue()).getLength()==0);
        
        ByteBuf buffer=parser.encode(d);
        assertTrue(Arrays.equals(getData(), buffer.array()));

        output=parser.decode(Unpooled.wrappedBuffer(getData2())).getResult();
        assertTrue(output instanceof DialogResponseAPDUImpl);
        d = (DialogResponseAPDUImpl)output;
        
        
        assertEquals(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 25L, 2L }), d.getApplicationContextName().getValue());
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
        
        assertEquals(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 25L, 2L }), d.getApplicationContextName().getValue());
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
