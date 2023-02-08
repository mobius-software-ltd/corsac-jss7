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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.RejectImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLastImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogAbortAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogPortionImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogRequestAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogResponseAPDUImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 */
@Test(groups = { "asn" })
public class DialogPortionTest {

	static ASNParser parser=new ASNParser();
	
	@BeforeClass
    public static void setUpClass() throws Exception {
    	parser.loadClass(DialogPortionImpl.class);
    	
    	parser.clearClassMapping(ASNUserInformationObjectImpl.class);
    	parser.registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, UserInformationTestASN.class);
    	
    	parser.clearClassMapping(ASNDialogPortionObjectImpl.class);
    	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogRequestAPDUImpl.class);
    	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogResponseAPDUImpl.class);
    	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogAbortAPDUImpl.class);
    	
    	parser.clearClassMapping(ASNComponentPortionObjectImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, InvokeImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnResultImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnResultLastImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, RejectImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnErrorImpl.class); 
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() {

    }

    @AfterMethod
    public void tearDown() {

    }

    @Test(groups = { "functional.encode", "functional.decode" })
    public void testDialogPortion_UserInformation() throws Exception {

    	// Hex dump is from wireshark trace for TCAP - MAP/USSD
        byte[] b = new byte[] { 0x6b, 0x41, 0x28, 0x3f, 0x06, 0x07, 0x00, 0x11, (byte) 0x86, 0x05, 0x01, 0x01, 0x01,
                (byte) 0xa0, 0x34, 0x60, 0x32, (byte) 0xa1, 0x09, 0x06, 0x07, 0x04, 0x00, 0x00, 0x01, 0x00, 0x13, 0x02,
                (byte) 0xbe, 0x25, 0x28, 0x23, 0x06, 0x07, 0x04, 0x00, 0x00, 0x01, 0x01, 0x01, 0x01, (byte) 0xa0, 0x18,
                (byte) 0xa0, (byte) 0x80, (byte) 0x80, 0x09, (byte) 0x96, 0x02, 0x24, (byte) 0x80, 0x03, 0x00, (byte) 0x80,
                0x00, (byte) 0xf2, (byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26, (byte) 0x98, (byte) 0x86, 0x03, (byte) 0xf0,
                0x00, 0x00 };

        Object output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        assertTrue(output instanceof DialogPortionImpl);
        DialogPortionImpl dpi = (DialogPortionImpl)output;
        
        assertFalse(dpi.isUnidirectional());

        DialogAPDU dialogAPDU = dpi.getDialogAPDU();

        assertNotNull(dialogAPDU);

        assertEquals(DialogAPDUType.Request, dialogAPDU.getType());

        DialogRequestAPDUImpl dialogRequestAPDU = (DialogRequestAPDUImpl) dialogAPDU;

        ApplicationContextName acn = dialogRequestAPDU.getApplicationContextName();

        assertNotNull(acn);

        assertEquals(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 19L, 2L }), acn.getOid());

        UserInformation userInfos = dialogRequestAPDU.getUserInformation();

        assertNotNull(userInfos);

        assertTrue(userInfos.isIDObjectIdentifier());

        assertEquals(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 1L, 1L, 1L }), userInfos.getObjectIdentifier());

        assertFalse(userInfos.isIDIndirect());

        assertTrue(userInfos.isValueObject());
        
        ByteBuf innerBuffer=parser.encode(userInfos.getChild());
        byte[] outputData=innerBuffer.array();
        
        assertTrue(Arrays.equals(new byte[] { (byte) 0xa0, (byte) 0x80, (byte) 0x80, 0x09, (byte) 0x96, 0x02, 0x24,
                (byte) 0x80, 0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2, (byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26,
                (byte) 0x98, (byte) 0x86, 0x03, (byte) 0xf0, 0x00, 0x00 }, outputData));

        ByteBuf buffer=parser.encode(dpi);
        byte[] encoded = buffer.array();
        assertTrue(Arrays.equals(b, encoded));

    }

    @Test(groups = { "functional.encode", "functional.decode" })
    public void testDialogPortion_DialogRequestAPDU() throws Exception {
    	// trace
        byte[] b = new byte[] { 107, 30, 40, 28, 6, 7, 0, 17, (byte) 134, 5, 1, 1, 1, (byte) 160, 17, 0x60, 15, (byte) 128, 2,
                7, (byte) 128, (byte) 161, 9, 6, 7, 4, 0, 1, 1, 1, 3, 0 };
        // In HEX
        // 6B 1E 28 1C 06 07 00 11 86 05 01 01 01 A0 11 60 0F 80 02 07 80 A1 09
        // 06 07 04 00 01 01 01 03 00

        Object output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        assertTrue(output instanceof DialogPortionImpl);
        DialogPortionImpl dpi = (DialogPortionImpl)output;

        DialogAPDU d = dpi.getDialogAPDU();
        assertNotNull(d);
        assertEquals(DialogAPDUType.Request, d.getType());
        DialogRequestAPDUImpl dr = (DialogRequestAPDUImpl) d;
        assertEquals(Arrays.asList(new Long[] { 0L, 4L, 0L, 1L, 1L, 1L, 3L, 0L }), dr.getApplicationContextName().getOid());
        assertNull(dr.getUserInformation());

        ByteBuf buffer=parser.encode(dpi);
        byte[] encoded = buffer.array();
        assertTrue(Arrays.equals(b, encoded));        

        DialogAPDU _apid = dpi.getDialogAPDU();
        assertEquals(DialogAPDUType.Request, _apid.getType());
        assertFalse(_apid.isUniDirectional());        
        // no idea how to check rest...?

    }

    @Test(groups = { "functional.encode", "functional.decode" })
    public void testDialogPortion_DialogAbortAPDU() throws Exception {
    	// trace
        byte[] b = new byte[] { 0x6B, 0x12, 0x28, 0x10, 0x06, 0x07, 0x00, 0x11, (byte) 0x86, 0x05, 0x01, 0x01, 0x01,
                (byte) 0xA0, 0x05, 0x64, 0x03, (byte) 0x80, 0x01, 0x01 };
        
        Object output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        assertTrue(output instanceof DialogPortionImpl);
        DialogPortionImpl dpi = (DialogPortionImpl)output;
        
        ByteBuf buffer=parser.encode(dpi);
        byte[] encoded = buffer.array();
        assertTrue(Arrays.equals(b, encoded));        

        DialogAPDU _apid = dpi.getDialogAPDU();
        assertEquals(DialogAPDUType.Abort, _apid.getType());
        assertFalse(_apid.isUniDirectional());        
    }
}
