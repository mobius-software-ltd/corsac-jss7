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

package org.restcomm.protocols.ss7.map.service.lsm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPPrivateExtension;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPPrivateExtensionImpl;
import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.junit.AfterClass;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class SLRArgExtensionContainerTest {

    MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    public byte[] getEncodedData() {
        return new byte[] { 48, 14, -96, 10, 48, 8, 6, 3, 42, 22, 33, 1, 2, 3, -95, 0 };
    }

    public Long[] getDataOId() {
        return new Long[] { 1L, 2L, 22L, 33L };
    }

    public byte[] getDataPe() {
        return new byte[] { 1, 2, 3 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SLRArgExtensionContainerImpl.class);
    	
        byte[] data = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SLRArgExtensionContainerImpl);
        SLRArgExtensionContainerImpl imp = (SLRArgExtensionContainerImpl)result.getResult();
        
        assertEquals(imp.getPrivateExtensionList().size(), 1);
        Long[] oids=new Long[imp.getPrivateExtensionList().get(0).getOId().size()];
        oids=imp.getPrivateExtensionList().get(0).getOId().toArray(oids);
        assertTrue(Arrays.equals(oids, getDataOId()));
        
        ByteBuf value=imp.getPrivateExtensionList().get(0).getData();
        data=new byte[value.readableBytes()];
        value.readBytes(data);
        assertTrue(Arrays.equals(data, getDataPe()));
        assertFalse(imp.getSlrArgPcsExtensions().getNaEsrkRequest());

    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SLRArgExtensionContainerImpl.class);
    	
        List<MAPPrivateExtension> privateExtensionList = new ArrayList<MAPPrivateExtension>();
        MAPPrivateExtensionImpl pe = new MAPPrivateExtensionImpl(Arrays.asList(getDataOId()), Unpooled.wrappedBuffer(getDataPe()));
        privateExtensionList.add(pe);
        SLRArgPCSExtensionsImpl slrArgPcsExtensions = new SLRArgPCSExtensionsImpl(false);

        SLRArgExtensionContainerImpl imp = new SLRArgExtensionContainerImpl(privateExtensionList, slrArgPcsExtensions);
        byte[] data=getEncodedData();
        ByteBuf buffer=parser.encode(imp);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}
