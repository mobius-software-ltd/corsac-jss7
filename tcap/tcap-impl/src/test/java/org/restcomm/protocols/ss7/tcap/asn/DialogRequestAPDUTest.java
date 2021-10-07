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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

/**
 *
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class DialogRequestAPDUTest {

    private byte[] getData() {
        return new byte[] { 96, 15, (byte) 128, 2, 7, (byte) 128, (byte) 161, 9, 6, 7, 4, 0, 0, 1, 0, 21, 2 };
    }

    private byte[] getData2() {
        return new byte[] { 96, 32, (byte) 128, 2, 7, (byte) 128, (byte) 161, 9, 6, 7, 4, 0, 0, 1, 0, 25, 2, (byte) 190, 15,
                40, 13, 6, 7, 4, 0, 0, 1, 1, 1, 1, (byte) 160, 2, (byte) 160, 0 };
    }

    static ASNParser parser=new ASNParser();
	
    @BeforeClass
    public static void setUpClass() throws Exception {
    	parser.loadClass(DialogRequestAPDUImpl.class);
    	
    	parser.clearClassMapping(ASNUserInformationObjectImpl.class);
    	parser.registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, TCBeginTestASN3.class);    	
    }

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
    	Object output=parser.decode(Unpooled.wrappedBuffer(getData())).getResult();
        assertTrue(output instanceof DialogRequestAPDUImpl);
        DialogRequestAPDUImpl d = (DialogRequestAPDUImpl)output;
        
        assertEquals(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 21L, 2L }), d.getApplicationContextName().getOid());
        UserInformation ui = d.getUserInformation();
        assertNull(ui);

        ByteBuf buffer=parser.encode(d);
        assertTrue(Arrays.equals(getData(), buffer.array()));

        output=parser.decode(Unpooled.wrappedBuffer(getData2())).getResult();
        assertTrue(output instanceof DialogRequestAPDUImpl);
        d = (DialogRequestAPDUImpl)output;
        
        assertEquals(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 25L, 2L }), d.getApplicationContextName().getOid());
        ui = d.getUserInformation();
        assertNotNull(ui);
        assertEquals(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 1L, 1L, 1L }), ui.getObjectIdentifier());
        assertTrue(ui.isValueObject());
        assertTrue(ui.getChild() instanceof TCBeginTestASN3);
        assertTrue(((TCBeginTestASN3)ui.getChild()).getLength(parser)==0);
        
        buffer=parser.encode(d);
        assertTrue(Arrays.equals(getData2(), buffer.array()));
    }
}
