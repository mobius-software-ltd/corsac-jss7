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
import static org.junit.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

import org.restcomm.protocols.ss7.tcap.asn.tx.DialogRequestAPDUImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class DialogUniAPDUTest {

    private byte[] getData() {
        return new byte[] { 96, 29, (byte) 128, 2, 7, (byte) 128, (byte) 161, 6, 6, 4, 4, 2, 2, 2, (byte) 190, 15, 40, 13, 6,
                4, 1, 1, 2, 3, (byte) 160, 5, (byte) 160, 3, 11, 22, 33 };
    }

    static ASNParser parser=new ASNParser();
	
    @BeforeClass
    public static void setUpClass() throws Exception {
    	parser.loadClass(DialogRequestAPDUImpl.class);
    	
    	parser.clearClassMapping(ASNUserInformationObjectImpl.class);
    	parser.registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, TCBeginTestASN3.class);    	
    }

    @Test
    public void testDecode() throws Exception {

    	Object output=parser.decode(Unpooled.wrappedBuffer(getData())).getResult();
        assertTrue(output instanceof DialogRequestAPDUImpl);
        DialogRequestAPDUImpl d = (DialogRequestAPDUImpl)output;
        
        assertEquals(Arrays.asList(new Long[] { 0L, 4L, 2L, 2L, 2L }), d.getApplicationContextName().getOid());
        UserInformation ui = d.getUserInformation();
        assertNotNull(ui);
        assertTrue(ui.isValueObject());
        assertTrue(ui.getChild() instanceof TCBeginTestASN3);
        assertTrue(InvokeTest.byteBufEquals(Unpooled.wrappedBuffer(new byte[] { 11, 22, 33 }), ((TCBeginTestASN3)ui.getChild()).getValue()));

        ByteBuf buffer=parser.encode(d);
        assertTrue(Arrays.equals(getData(), buffer.array()));
    }
}
