/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

import org.restcomm.protocols.ss7.tcap.asn.tx.DialogAbortAPDUImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
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

    static ASNParser parser=new ASNParser();
	
    @BeforeClass
    public static void setUpClass() throws Exception {
    	parser.loadClass(DialogAbortAPDUImpl.class);
    	
    	parser.clearClassMapping(ASNUserInformationObjectImpl.class);
    	parser.registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, TCBeginTestASN3.class);    	
    }
    
    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
    	Object output=parser.decode(Unpooled.wrappedBuffer(getData())).getResult();
        assertTrue(output instanceof DialogAbortAPDUImpl);
        DialogAbortAPDUImpl d = (DialogAbortAPDUImpl)output;
                
        AbortSourceType as = d.getAbortSource();
        assertEquals(AbortSourceType.User, as);
        UserInformation ui = d.getUserInformation();
        assertNull(ui);

        ByteBuf buffer=parser.encode(d);
        assertTrue(Arrays.equals(getData(), buffer.array()));

        output=parser.decode(Unpooled.wrappedBuffer(getData2())).getResult();
        assertTrue(output instanceof DialogAbortAPDUImpl);
        d = (DialogAbortAPDUImpl)output;
        
        as = d.getAbortSource();
        assertEquals(AbortSourceType.Provider, as);
        ui = d.getUserInformation();
        assertNotNull(ui);
        assertTrue(ui.getChild() instanceof TCBeginTestASN3);
        assertTrue(InvokeTest.byteBufEquals(Unpooled.wrappedBuffer(new byte[] { 1, 2, 3 }), ((TCBeginTestASN3)ui.getChild()).getValue()));

        buffer=parser.encode(d);
        assertTrue(Arrays.equals(getData2(), buffer.array()));
    }
}