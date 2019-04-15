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

import org.restcomm.protocols.ss7.tcap.asn.ASNAbortSource;
import org.restcomm.protocols.ss7.tcap.asn.AbortSourceType;
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
public class DialogAbortAPDUTest {

    private byte[] getData() {
        return new byte[] { 100, 3, (byte) 128, 1, 0 };
    }

    private byte[] getData2() {
        return new byte[] { 100, 23, (byte) 128, 1, 1, (byte) 190, 18, 40, 16, 6, 7, 4, 0, 0, 1, 1, 1, 1, (byte) 160, 5, (byte) 160, 3, 1, 2,
                3 };
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    	ASNGeneric.clear(ASNUserInformationObjectImpl.class);
    	ASNGeneric.registerAlternative(ASNUserInformationObjectImpl.class, TCBeginTestASN3.class);    	
    }
    
    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(DialogAbortAPDUImpl.class);
    	
        Object output=parser.decode(Unpooled.wrappedBuffer(getData())).getResult();
        assertTrue(output instanceof DialogAbortAPDUImpl);
        DialogAbortAPDUImpl d = (DialogAbortAPDUImpl)output;
                
        ASNAbortSource as = d.getAbortSource();
        assertEquals(AbortSourceType.User, as.getAbortSourceType());
        UserInformationImpl ui = d.getUserInformation();
        assertNull(ui);

        ByteBuf buffer=parser.encode(d);
        assertTrue(Arrays.equals(getData(), buffer.array()));

        output=parser.decode(Unpooled.wrappedBuffer(getData2())).getResult();
        assertTrue(output instanceof DialogAbortAPDUImpl);
        d = (DialogAbortAPDUImpl)output;
        
        as = d.getAbortSource();
        assertEquals(AbortSourceType.Provider, as.getAbortSourceType());
        ui = d.getUserInformation();
        assertNotNull(ui);
        assertTrue(ui.getExternal().getChild().getValue() instanceof TCBeginTestASN3);
        assertTrue(InvokeTest.byteBufEquals(Unpooled.wrappedBuffer(new byte[] { 1, 2, 3 }), ((TCBeginTestASN3)ui.getExternal().getChild().getValue()).getValue()));

        buffer=parser.encode(d);
        assertTrue(Arrays.equals(getData2(), buffer.array()));
    }
}