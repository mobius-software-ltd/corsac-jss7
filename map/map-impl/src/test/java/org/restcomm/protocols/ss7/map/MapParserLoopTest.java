package org.restcomm.protocols.ss7.map;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.tcap.TCAPProviderImpl;
import org.restcomm.protocols.ss7.tcap.TCAPStackImpl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class MapParserLoopTest
{
	@Test
	public void testLop()
	{ 
		System.out.println("Starting..."); 
		
		try 
		{ 
			String hexString = "0980030e190b12070012047952120130470b12060012047951008106089f64819c4904000000046b2a2828060700118605010101a01d611b80020780a109060704000001000e03a203020100a305a1030201006c80a264020100305f020138a380a18030520410fd64776829377694c4aafd7b759733850408cff064891b6bdb010410e1d3e1c790eec41329552ed442f528a204100bd24194a30d47906d4d0c2d71e001ad0410c108a2d426ca000039f62268c2528551000000000000"; 
			ByteBuf buf = Unpooled.wrappedBuffer(DatatypeConverter.parseHexBinary(hexString)); 
			SccpStackImpl sccpStack = new SccpStackImpl(".sccp");
			sccpStack.start();
			TCAPStackImpl tcapStack = new TCAPStackImpl(".tcap", sccpStack.getSccpProvider(), 6, 1); 
			tcapStack.start();
			TCAPProviderImpl provider = (TCAPProviderImpl) tcapStack.getProvider(); 
			MAPStackImpl mapStack = new MAPStackImpl(".map", provider);
			mapStack.start();
			
			int type = buf.readByte();
			SccpDataMessage m = (SccpDataMessage) ((MessageFactoryImpl) sccpStack.getSccpProvider().getMessageFactory()).createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);
			provider.onMessage(m); 
		} 
		catch (Exception e) 
		{
			e.printStackTrace(); 
		} 

		System.out.println("Done..."); 
	}
}