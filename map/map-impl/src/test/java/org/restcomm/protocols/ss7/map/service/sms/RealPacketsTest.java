package org.restcomm.protocols.ss7.map.service.sms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.dialog.MAPAcceptInfoImpl;
import org.restcomm.protocols.ss7.map.dialog.MAPCloseInfoImpl;
import org.restcomm.protocols.ss7.map.dialog.MAPOpenInfoImpl;
import org.restcomm.protocols.ss7.map.dialog.MAPProviderAbortInfoImpl;
import org.restcomm.protocols.ss7.map.dialog.MAPRefuseInfoImpl;
import org.restcomm.protocols.ss7.map.dialog.MAPUserAbortInfoImpl;
import org.restcomm.protocols.ss7.tcap.asn.ASNComponentPortionObjectImpl;
import org.restcomm.protocols.ss7.tcap.asn.ASNDialogPortionObjectImpl;
import org.restcomm.protocols.ss7.tcap.asn.ASNUserInformationObjectImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ASNReturnResultParameterImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCodeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.RejectImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultInnerImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLastImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogAbortAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogRequestAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogResponseAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCEndMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;

import io.netty.buffer.Unpooled;

public class RealPacketsTest {
	
static ASNParser parser=new ASNParser();
	
	@BeforeClass
	public static void setUp()
	{
		parser.loadClass(TCEndMessageImpl.class);
        
		parser.clearClassMapping(ASNReturnResultParameterImpl.class);
		
		parser.clearClassMapping(ASNDialogPortionObjectImpl.class);
    	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogRequestAPDUImpl.class);
    	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogResponseAPDUImpl.class);
    	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogAbortAPDUImpl.class);
    	
    	parser.clearClassMapping(ASNComponentPortionObjectImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, InvokeImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnResultImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnResultLastImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, RejectImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnErrorImpl.class);
    	
    	parser.registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, MAPOpenInfoImpl.class);
    	parser.registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, MAPAcceptInfoImpl.class);
    	parser.registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, MAPCloseInfoImpl.class);
    	parser.registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, MAPRefuseInfoImpl.class);
    	parser.registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, MAPUserAbortInfoImpl.class);
    	parser.registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, MAPProviderAbortInfoImpl.class);
    	
    	OperationCodeImpl opCode=new OperationCodeImpl();
    	opCode.setLocalOperationCode(MAPOperationCode.informServiceCentre);
    	parser.registerLocalMapping(InvokeImpl.class, opCode, InformServiceCentreRequestImpl.class);
    	
    	opCode=new OperationCodeImpl();
    	opCode.setLocalOperationCode(MAPOperationCode.sendRoutingInfoForSM);
    	parser.registerLocalMapping(ReturnResultInnerImpl.class, opCode, SendRoutingInfoForSMResponseImpl.class);
    	
    	parser.registerAlternativeClassMapping(SendRoutingInfoForSMResponseImpl.class, SendRoutingInfoForSMResponseImpl.class);
    	parser.registerAlternativeClassMapping(InformServiceCentreRequestImpl.class, InformServiceCentreRequestImpl.class);    	
	}
	
	@Test
	public void testUnknownPacket() throws ASNException {
		byte[] data = {0x64,0x65,0x49,0x04,0x00,0x01,(byte)0xe6,0x78,0x6b,0x2a,0x28,0x28,0x06,0x07,0x00,0x11,(byte)0x86,0x05,0x01,0x01,0x01,(byte)0xa0,0x1d,0x61,0x1b,(byte)0x80,0x02,0x07,(byte)0x80,(byte)0xa1,0x09,0x06,0x07,0x04,0x00,0x00,0x01,0x00,0x14,0x03,(byte)0xa2,0x03,0x02,0x01,0x00,(byte)0xa3,0x05,(byte)0xa1,0x03,0x02,0x01,0x00,0x6c,(byte)0x80,(byte)0xa2,0x1f,0x02,0x01,0x00,0x30,0x1a,0x02,0x01,0x2d,0x30,0x15,0x04,0x08,0x24,0x05,0x69,0x00,0x31,0x08,(byte)0x96,(byte)0xf8,(byte)0xa0,0x09,(byte)0x81,0x07,(byte)0x91,0x79,0x52,0x12,0x01,0x00,0x30,(byte)0xa1,0x0c,0x02,0x01,0x00,0x02,0x01,0x3f,0x30,0x04,0x03,0x02,0x02,(byte)0xa0,0x00,0x00};
		
		ASNDecodeResult decodeResult=parser.decode(Unpooled.wrappedBuffer(data));
        assertTrue(decodeResult.getResult() instanceof TCEndMessageImpl);
        TCEndMessageImpl tcm = (TCEndMessageImpl)decodeResult.getResult();
        assertEquals(tcm.getComponents().size(),2);
        assertFalse(decodeResult.getHadErrors());
        assertTrue(tcm.getComponents().get(0) instanceof ReturnResultLast);
        assertTrue(tcm.getComponents().get(1) instanceof Invoke);
        assertNotNull(((ReturnResultLast)tcm.getComponents().get(0)).getParameter());
        assertNotNull(((Invoke)tcm.getComponents().get(1)).getParameter());
        
        ASNParsingComponentException exception = parser.validateObject(tcm);
        assertNull(exception);
	}
}