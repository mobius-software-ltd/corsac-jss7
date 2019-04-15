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
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

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
public class DialogUniAPDUTest {

    private byte[] getData() {
        return new byte[] { 96, 29, (byte) 128, 2, 7, (byte) 128, (byte) 161, 6, 6, 4, 4, 2, 2, 2, (byte) 190, 15, 40, 13, 6,
                4, 1, 1, 2, 3, (byte) 160, 5, (byte) 160, 3, 11, 22, 33 };
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    	ASNGeneric.clear(ASNUserInformationObjectImpl.class);
    	ASNGeneric.registerAlternative(ASNUserInformationObjectImpl.class, TCBeginTestASN3.class);    	
    }

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {

    	ASNParser parser=new ASNParser();
    	parser.loadClass(DialogRequestAPDUImpl.class);
    	
    	Object output=parser.decode(Unpooled.wrappedBuffer(getData())).getResult();
        assertTrue(output instanceof DialogRequestAPDUImpl);
        DialogRequestAPDUImpl d = (DialogRequestAPDUImpl)output;
        
        assertEquals(Arrays.asList(new Long[] { 0L, 4L, 2L, 2L, 2L }), d.getApplicationContextName().getValue());
        UserInformationImpl ui = d.getUserInformation();
        assertNotNull(ui);
        assertTrue(ui.getExternal().isValueObject());
        assertTrue(ui.getExternal().getChild().getValue() instanceof TCBeginTestASN3);
        assertTrue(InvokeTest.byteBufEquals(Unpooled.wrappedBuffer(new byte[] { 11, 22, 33 }), ((TCBeginTestASN3)ui.getExternal().getChild().getValue()).getValue()));

        ByteBuf buffer=parser.encode(d);
        assertTrue(Arrays.equals(getData(), buffer.array()));
    }
}
