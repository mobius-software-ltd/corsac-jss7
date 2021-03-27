package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ODBInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBHPLMNDataImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author vadim subbotin
 */
public class ODBInfoTest {
    private byte[] data = {48, 107, 48, 56, 3, 3, 2, -1, -4, 3, 2, 5, -96, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 5, 0, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33};

    @Test(groups = {"functional.decode", "subscriberInformation"})
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

        ODBGeneralDataImpl odbGeneralData = odbInfo.getOdbData().getODBGeneralData();
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

        ODBHPLMNDataImpl odbhplmnData = odbInfo.getOdbData().getOdbHplmnData();
        assertTrue(odbhplmnData.getPlmnSpecificBarringType1());
        assertFalse(odbhplmnData.getPlmnSpecificBarringType2());
        assertTrue(odbhplmnData.getPlmnSpecificBarringType3());
        assertFalse(odbhplmnData.getPlmnSpecificBarringType4());
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
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