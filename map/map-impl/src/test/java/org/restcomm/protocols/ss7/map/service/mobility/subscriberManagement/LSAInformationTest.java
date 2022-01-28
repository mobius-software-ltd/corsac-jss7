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
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.LSAIdentityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAOnlyAccessIndicator;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class LSAInformationTest {

	public byte[] getData() {
        return new byte[] { 48, 101, 5, 0, -127, 1, 1, -94, 53, 48, 51, -128, 3, 12, 34, 26, -127, 1, 5, -126, 0, -93, 39, -96,
                32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24,
                25, 26, -95, 3, 31, 32, 33, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3,
                6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    public byte[] getDataLSAIdentity() {
        return new byte[] { 12, 34, 26 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LSAInformationImpl.class);
    	
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LSAInformationImpl);
        LSAInformationImpl prim = (LSAInformationImpl)result.getResult();
        
        assertTrue(prim.getCompleteDataListIncluded());
        assertEquals(prim.getLSAOnlyAccessIndicator(), LSAOnlyAccessIndicator.accessOutsideLSAsRestricted);

        List<LSAData> lsaDataList = prim.getLSADataList();
        assertNotNull(lsaDataList);
        assertEquals(lsaDataList.size(), 1);
        LSAData lsaData = lsaDataList.get(0);

        assertTrue(ByteBufUtil.equals(lsaData.getLSAIdentity().getValue(), Unpooled.wrappedBuffer(this.getDataLSAIdentity())));
        assertEquals(lsaData.getLSAAttributes().getData(), 5);
        assertTrue(lsaData.getLsaActiveModeIndicator());
        assertNotNull(lsaData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(lsaData.getExtensionContainer()));

        MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LSAInformationImpl.class);
    	
        boolean completeDataListIncluded = true;
        LSAOnlyAccessIndicator lsaOnlyAccessIndicator = LSAOnlyAccessIndicator.accessOutsideLSAsRestricted;
        List<LSAData> lsaDataList = new ArrayList<LSAData>();
        LSAIdentityImpl lsaIdentity = new LSAIdentityImpl(Unpooled.wrappedBuffer(this.getDataLSAIdentity()));
        LSAAttributesImpl lsaAttributes = new LSAAttributesImpl(5);
        boolean lsaActiveModeIndicator = true;
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        LSADataImpl lsaData = new LSADataImpl(lsaIdentity, lsaAttributes, lsaActiveModeIndicator, extensionContainer);
        lsaDataList.add(lsaData);

        LSAInformation prim = new LSAInformationImpl(completeDataListIncluded, lsaOnlyAccessIndicator, lsaDataList,
                extensionContainer);
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData); 
        byte[] rawData = this.getData();
        assertTrue(Arrays.equals(encodedData, rawData));
    }
}