package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBHPLMNData;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ODBDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ODBGeneralDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ODBHPLMNDataImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author vadim subbotin
 * @author yulianoifa
 */
public class ODBInfoTest {
    private byte[] data = {48, 95, 48, 50, 3, 3, 0, -1, -4, 3, 2, 4, -96, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33};

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ODBInfoImpl.class);
    	        
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ODBInfoImpl);
        ODBInfoImpl odbInfo = (ODBInfoImpl)result.getResult();

        assertNotNull(odbInfo.getOdbData());
        assertTrue(odbInfo.getNotificationToCSE());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(odbInfo.getExtensionContainer()));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(odbInfo.getOdbData().getExtensionContainer()));

        ODBGeneralData odbGeneralData = odbInfo.getOdbData().getODBGeneralData();
        assertTrue(odbGeneralData.getAllOGCallsBarred());
        assertTrue(odbGeneralData.getInternationalOGCallsBarred());
        assertTrue(odbGeneralData.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(odbGeneralData.getInterzonalOGCallsBarred());
        assertTrue(odbGeneralData.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertTrue(odbGeneralData.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(odbGeneralData.getPremiumRateInformationOGCallsBarred());
        assertTrue(odbGeneralData.getPremiumRateEntertainementOGCallsBarred());
        assertTrue(odbGeneralData.getSsAccessBarred());
        assertTrue(odbGeneralData.getAllECTBarred());
        assertTrue(odbGeneralData.getChargeableECTBarred());
        assertTrue(odbGeneralData.getInternationalECTBarred());
        assertTrue(odbGeneralData.getInterzonalECTBarred());
        assertTrue(odbGeneralData.getDoublyChargeableECTBarred());
        assertFalse(odbGeneralData.getMultipleECTBarred());
        assertFalse(odbGeneralData.getAllPacketOrientedServicesBarred());
        assertFalse(odbGeneralData.getRoamerAccessToHPLMNAPBarred());
        assertFalse(odbGeneralData.getRoamerAccessToVPLMNAPBarred());
        assertFalse(odbGeneralData.getRoamingOutsidePLMNOGCallsBarred());
        assertFalse(odbGeneralData.getAllICCallsBarred());
        assertFalse(odbGeneralData.getRoamingOutsidePLMNICCallsBarred());
        assertFalse(odbGeneralData.getRoamingOutsidePLMNICountryICCallsBarred());
        assertFalse(odbGeneralData.getRoamingOutsidePLMNBarred());
        assertFalse(odbGeneralData.getRoamingOutsidePLMNCountryBarred());
        assertFalse(odbGeneralData.getRegistrationAllCFBarred());
        assertFalse(odbGeneralData.getRegistrationCFNotToHPLMNBarred());
        assertFalse(odbGeneralData.getRegistrationInterzonalCFBarred());
        assertFalse(odbGeneralData.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertFalse(odbGeneralData.getRegistrationInternationalCFBarred());

        ODBHPLMNData odbhplmnData = odbInfo.getOdbData().getOdbHplmnData();
        assertTrue(odbhplmnData.getPlmnSpecificBarringType1());
        assertFalse(odbhplmnData.getPlmnSpecificBarringType2());
        assertTrue(odbhplmnData.getPlmnSpecificBarringType3());
        assertFalse(odbhplmnData.getPlmnSpecificBarringType4());
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ODBInfoImpl.class);
    	        
    	ODBGeneralDataImpl odbGeneralData = new ODBGeneralDataImpl(true, true, true, true, true, true, true, true, true,
                true, true, true, true, true, false, false, false, false, false, false, false, false, false, false, false,
                false, false, false, false);
        ODBHPLMNDataImpl odbhplmnData = new ODBHPLMNDataImpl(true, false, true, false);
        ODBDataImpl odbData = new ODBDataImpl(odbGeneralData, odbhplmnData, MAPExtensionContainerTest.GetTestExtensionContainer());

        ODBInfoImpl odbInfo = new ODBInfoImpl(odbData, true, MAPExtensionContainerTest.GetTestExtensionContainer());

        ByteBuf buffer=parser.encode(odbInfo);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, data));
    }
}