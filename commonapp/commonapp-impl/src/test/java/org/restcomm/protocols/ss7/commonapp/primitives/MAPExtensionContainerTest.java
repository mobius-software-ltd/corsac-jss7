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

package org.restcomm.protocols.ss7.commonapp.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPPrivateExtension;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class MAPExtensionContainerTest {
    public static MAPExtensionContainer GetTestExtensionContainer() {
        List<MAPPrivateExtension> al = new ArrayList<MAPPrivateExtension>();
        
        al.add(new MAPPrivateExtensionImpl(Arrays.asList(1L, 2L, 3L, 4L), Unpooled.wrappedBuffer(new byte[] { 11, 12, 13, 14, 15 })));
        al.add(new MAPPrivateExtensionImpl(Arrays.asList(1L, 2L, 3L, 6L), null));        
        al.add(new MAPPrivateExtensionImpl(Arrays.asList(1L, 2L, 3L, 5L), Unpooled.wrappedBuffer(new byte[] { 21, 22, 23, 24, 25, 26 })));

        MAPExtensionContainer cnt = new MAPExtensionContainerImpl(al, Unpooled.wrappedBuffer(new byte[] { 31, 32, 33 }));

        return cnt;
    }

    public static Boolean CheckTestExtensionContainer(MAPExtensionContainer extContainer) {
        if (extContainer == null || extContainer.getPrivateExtensionList().size() != 3)
            return false;

        for (int i = 0; i < 3; i++) {
            MAPPrivateExtension pe = extContainer.getPrivateExtensionList().get(i);
            Long[] lx = null;
            byte[] bx = null;

            switch (i) {
                case 0:
                    lx = new Long[] { 1L, 2L, 3L, 4L };
                    bx = new byte[] { 11, 12, 13, 14, 15 };
                    break;
                case 1:
                    lx = new Long[] { 1L, 2L, 3L, 6L };
                    bx = null;
                    break;
                case 2:
                    lx = new Long[] { 1L, 2L, 3L, 5L };
                    bx = new byte[] { 21, 22, 23, 24, 25, 26 };
                    break;
            }

            Long[] iddata=new Long[4];
            iddata=pe.getOId().toArray(iddata);
            if (pe.getOId() == null || !Arrays.equals(iddata, lx))
                return false;
            
            if (bx == null) {
                if (pe.getData() != null)
                    return false;
            } else {
            	ByteBuf value=pe.getData();
            	byte[] data=new byte[value.readableBytes()];
            	value.readBytes(data);
            	
            	if (pe.getData() == null || !Arrays.equals(data, bx))
                    return false;
            }
        }

        byte[] by = new byte[] { 31, 32, 33 };
        ByteBuf value=extContainer.getPcsExtensions();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        if (extContainer.getPcsExtensions() == null || !Arrays.equals(data, by))
            return false;

        return true;
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(false);
    	parser.replaceClass(MAPExtensionContainerImpl.class);
    	
        byte[] data = this.getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MAPExtensionContainerImpl);
        MAPExtensionContainerImpl extCont = (MAPExtensionContainerImpl)result.getResult();
        assertEquals(CheckTestExtensionContainer(extCont), Boolean.TRUE);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(false);
    	parser.replaceClass(MAPExtensionContainerImpl.class);
    	
    	byte[] data = this.getEncodedData();

        MAPExtensionContainerImpl extCont = (MAPExtensionContainerImpl) GetTestExtensionContainer();
        ByteBuf buffer=parser.encode(extCont);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }

    private byte[] getEncodedData() {
        return new byte[] { 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11,
                6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33 };
    }
}
