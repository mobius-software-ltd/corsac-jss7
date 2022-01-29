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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ODBGeneralDataTest {

    private byte[] getEncodedData() {
        return new byte[] { 3, 5, 3, 74, -43, 85, 80 };
    }

    private byte[] getEncodedData1() {
        return new byte[] { 3, 5, 3, -75, 42, -86, -88 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ODBGeneralDataImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ODBGeneralDataImpl);
        ODBGeneralDataImpl imp = (ODBGeneralDataImpl)result.getResult();
        
        assertTrue(!imp.getAllOGCallsBarred());
        assertTrue(imp.getInternationalOGCallsBarred());
        assertTrue(!imp.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(imp.getInterzonalOGCallsBarred());
        assertTrue(!imp.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertTrue(imp.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(!imp.getPremiumRateInformationOGCallsBarred());
        assertTrue(imp.getPremiumRateEntertainementOGCallsBarred());
        assertTrue(!imp.getSsAccessBarred());
        assertTrue(imp.getAllECTBarred());
        assertTrue(!imp.getChargeableECTBarred());
        assertTrue(imp.getInternationalECTBarred());
        assertTrue(!imp.getInterzonalECTBarred());
        assertTrue(imp.getDoublyChargeableECTBarred());
        assertTrue(!imp.getMultipleECTBarred());
        assertTrue(imp.getAllPacketOrientedServicesBarred());
        assertTrue(!imp.getRoamerAccessToHPLMNAPBarred());
        assertTrue(imp.getRoamerAccessToVPLMNAPBarred());
        assertTrue(!imp.getRoamingOutsidePLMNOGCallsBarred());
        assertTrue(imp.getAllICCallsBarred());
        assertTrue(!imp.getRoamingOutsidePLMNICCallsBarred());
        assertTrue(imp.getRoamingOutsidePLMNICountryICCallsBarred());
        assertTrue(!imp.getRoamingOutsidePLMNBarred());
        assertTrue(imp.getRoamingOutsidePLMNCountryBarred());
        assertTrue(!imp.getRegistrationAllCFBarred());
        assertTrue(imp.getRegistrationCFNotToHPLMNBarred());
        assertTrue(!imp.getRegistrationInterzonalCFBarred());
        assertTrue(imp.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertTrue(!imp.getRegistrationInternationalCFBarred());

        rawData = getEncodedData1();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ODBGeneralDataImpl);
        imp = (ODBGeneralDataImpl)result.getResult();

        assertTrue(imp.getAllOGCallsBarred());
        assertTrue(!imp.getInternationalOGCallsBarred());
        assertTrue(imp.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(!imp.getInterzonalOGCallsBarred());
        assertTrue(imp.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertTrue(!imp.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(imp.getPremiumRateInformationOGCallsBarred());
        assertTrue(!imp.getPremiumRateEntertainementOGCallsBarred());
        assertTrue(imp.getSsAccessBarred());
        assertTrue(!imp.getAllECTBarred());
        assertTrue(imp.getChargeableECTBarred());
        assertTrue(!imp.getInternationalECTBarred());
        assertTrue(imp.getInterzonalECTBarred());
        assertTrue(!imp.getDoublyChargeableECTBarred());
        assertTrue(imp.getMultipleECTBarred());
        assertTrue(!imp.getAllPacketOrientedServicesBarred());
        assertTrue(imp.getRoamerAccessToHPLMNAPBarred());
        assertTrue(!imp.getRoamerAccessToVPLMNAPBarred());
        assertTrue(imp.getRoamingOutsidePLMNOGCallsBarred());
        assertTrue(!imp.getAllICCallsBarred());
        assertTrue(imp.getRoamingOutsidePLMNICCallsBarred());
        assertTrue(!imp.getRoamingOutsidePLMNICountryICCallsBarred());
        assertTrue(imp.getRoamingOutsidePLMNBarred());
        assertTrue(!imp.getRoamingOutsidePLMNCountryBarred());
        assertTrue(imp.getRegistrationAllCFBarred());
        assertTrue(!imp.getRegistrationCFNotToHPLMNBarred());
        assertTrue(imp.getRegistrationInterzonalCFBarred());
        assertTrue(!imp.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertTrue(imp.getRegistrationInternationalCFBarred());
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ODBGeneralDataImpl.class);
    	
        ODBGeneralDataImpl imp = new ODBGeneralDataImpl(false, true, false, true, false, true, false, true, false, true, false,
                true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true,
                false);

        ByteBuf buffer=parser.encode(imp);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = this.getEncodedData();
        assertTrue(Arrays.equals(encodedData, rawData));

        imp = new ODBGeneralDataImpl(true, false, true, false, true, false, true, false, true, false, true, false, true, false,
                true, false, true, false, true, false, true, false, true, false, true, false, true, false, true);

        buffer=parser.encode(imp);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = this.getEncodedData1();
        assertTrue(Arrays.equals(encodedData, rawData));
    }
}
