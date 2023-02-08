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

package org.restcomm.protocols.ss7.map.service.mobility.faultRecovery;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.VLRCapability;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.VLRCapabilityImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class RestoreDataRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 9, 4, 7, 17, 33, 34, 51, 67, 68, 85 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 64, 4, 7, 17, 33, 34, 51, 67, 68, 85, 4, 4, 22, 33, 44, 55, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 166, 4, (byte) 128, 2, 6, (byte) 192,
                (byte) 135, 0 };
    }

    private byte[] getLmsiData() {
        return new byte[] { 22, 33, 44, 55 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(RestoreDataRequestImpl.class);
    	
        byte[] data = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof RestoreDataRequestImpl);
        RestoreDataRequestImpl prim = (RestoreDataRequestImpl)result.getResult();
        
        IMSI imsi = prim.getImsi();
        assertTrue(imsi.getData().equals("11122233344455"));

        assertNull(prim.getLmsi());
        assertNull(prim.getExtensionContainer());
        assertNull(prim.getVLRCapability());
        assertFalse(prim.getRestorationIndicator());


        data = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof RestoreDataRequestImpl);
        prim = (RestoreDataRequestImpl)result.getResult();

        imsi = prim.getImsi();
        assertTrue(imsi.getData().equals("11122233344455"));

        assertTrue(ByteBufUtil.equals(prim.getLmsi().getValue(), Unpooled.wrappedBuffer(getLmsiData())));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));
        VLRCapability vlrCapability = prim.getVLRCapability();
        SupportedCamelPhases supportedCamelPhases = vlrCapability.getSupportedCamelPhases();
        assertTrue(supportedCamelPhases.getPhase1Supported());
        assertTrue(supportedCamelPhases.getPhase2Supported());
        assertFalse(supportedCamelPhases.getPhase3Supported());
        assertFalse(supportedCamelPhases.getPhase4Supported());

        assertTrue(prim.getRestorationIndicator());

    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(RestoreDataRequestImpl.class);
    	
        IMSIImpl imsi = new IMSIImpl("11122233344455");
        RestoreDataRequestImpl prim = new RestoreDataRequestImpl(imsi, null, null, null, false);
        
        byte[] data=this.getEncodedData();
    	ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        LMSIImpl lmsi = new LMSIImpl(Unpooled.wrappedBuffer(getLmsiData()));
        SupportedCamelPhasesImpl supportedCamelPhases = new SupportedCamelPhasesImpl(true, true, false, false);
        VLRCapabilityImpl vlrCapability = new VLRCapabilityImpl(supportedCamelPhases, null, false, null, null, false, null, null, null, false, false);
        prim = new RestoreDataRequestImpl(imsi, lmsi, vlrCapability, MAPExtensionContainerTest.GetTestExtensionContainer(), true);

        data=this.getEncodedData2();
    	buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}