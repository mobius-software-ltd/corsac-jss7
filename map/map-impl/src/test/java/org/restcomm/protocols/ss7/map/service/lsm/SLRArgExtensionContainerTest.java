/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.primitives.MAPPrivateExtensionImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgPCSExtensionsImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
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

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    public byte[] getEncodedData() {
        return new byte[] { 48, 16, -96, 12, 48, 10, 6, 3, 42, 22, 33, 4, 3, 1, 2, 3, -95, 0 };
    }

    public Long[] getDataOId() {
        return new Long[] { 1L, 2L, 22L, 33L };
    }

    public byte[] getDataPe() {
        return new byte[] { 1, 2, 3 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
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
        
        ByteBuf value=((ASNOctetString)imp.getPrivateExtensionList().get(0).getData()).getValue();
        data=new byte[value.readableBytes()];
        value.readBytes(data);
        assertTrue(Arrays.equals(data, getDataPe()));
        assertFalse(imp.getSlrArgPcsExtensions().getNaEsrkRequest());

    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SLRArgExtensionContainerImpl.class);
    	
        ArrayList<MAPPrivateExtensionImpl> privateExtensionList = new ArrayList<MAPPrivateExtensionImpl>();
        ASNOctetString value=new ASNOctetString();
        value.setValue(Unpooled.wrappedBuffer(getDataPe()));
        MAPPrivateExtensionImpl pe = new MAPPrivateExtensionImpl(Arrays.asList(getDataOId()), value);
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
