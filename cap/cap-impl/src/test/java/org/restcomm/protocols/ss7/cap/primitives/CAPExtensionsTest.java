/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.primitives;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CriticalityType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ExtensionField;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ExtensionFieldImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CAPExtensionsTest {

    public byte[] getData1() {
        return new byte[] { 48, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CAPINAPExtensionsImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CAPINAPExtensionsImpl);
        
        CAPINAPExtensionsImpl elem = (CAPINAPExtensionsImpl)result.getResult();
        assertTrue(checkTestCAPExtensions(elem));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CAPINAPExtensionsImpl.class);
    	
    	CAPINAPExtensions elem = createTestCAPExtensions();
    	byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    public static CAPINAPExtensions createTestCAPExtensions() {
        ExtensionFieldImpl a1 = new ExtensionFieldImpl(2, CriticalityType.typeIgnore, Unpooled.wrappedBuffer(new byte[] {}), false);
        ExtensionFieldImpl a2 = new ExtensionFieldImpl(3, CriticalityType.typeAbort, Unpooled.wrappedBuffer(new byte[] { -1 }), false);
        List<ExtensionField> flds = new ArrayList<ExtensionField>();
        flds.add(a1);
        flds.add(a2);
        CAPINAPExtensions elem = new CAPINAPExtensionsImpl(flds);
        return elem;
    }

    public static boolean checkTestCAPExtensions(CAPINAPExtensions elem) {
        if (elem.getExtensionFields() == null || elem.getExtensionFields().size() != 2)
            return false;

        ExtensionField a1 = elem.getExtensionFields().get(0);
        ExtensionField a2 = elem.getExtensionFields().get(1);
        if (a1.getLocalCode() != 2 || a2.getLocalCode() != 3)
            return false;
        if (a1.getCriticalityType() != CriticalityType.typeIgnore || a2.getCriticalityType() != CriticalityType.typeAbort)
            return false;
        if (a1.getValue() == null || a1.getValue().readableBytes() != 0)
            return false;
        if (a2.getValue() == null || a2.getValue().readableBytes() != 1 || (a2.getValue().readByte()) != -1)
            return false;

        return true;
    }

    public long[] getDataOid() {
        return new long[] { 1, 0, 22 };
    }
}
