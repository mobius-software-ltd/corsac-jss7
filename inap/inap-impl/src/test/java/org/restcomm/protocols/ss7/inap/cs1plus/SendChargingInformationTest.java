package org.restcomm.protocols.ss7.inap.cs1plus;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.api.charging.EventTypeCharging;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ChargeMessage;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ChargingInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.EventSpecificInfoCharging;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.IntervalAccuracy;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.SCIBillingChargingCharacteristicsCS1;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.TariffInformation;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.SendChargingInformationCS1RequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.ChargeMessageImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.ChargingInformationImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.EventSpecificInfoChargingImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.SCIBillingChargingCharacteristicsCS1Impl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.TariffInformationImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/**
 * 
 * @author yulianoifa
 *
 */
public class SendChargingInformationTest 
{
	protected final transient Logger logger=LogManager.getLogger(SendChargingInformationTest.class);

	private byte[] message1=new byte[] {0x30,0x24,(byte)0xa0,0x1d,(byte)0xa0,0x1b,(byte)0xa1,0x19,
			(byte)0x81,0x01,0x01,(byte)0xa2,0x14,(byte)0xa0,0x12,(byte)0x80,0x01,0x01,(byte)0x81,
			0x01,0x01,(byte)0x82,0x01,0x01,(byte)0x83,0x01,0x01,(byte)0x84,0x01,0x01,(byte)0x85,
			0x01,0x03,(byte)0xa1,0x03,(byte)0x80,0x01,0x01
	};
	
	@BeforeClass
	public static void initTests()
	{
		Configurator.initialize(new DefaultConfiguration());
	}
	
	@Test
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(false);
		parser.replaceClass(SendChargingInformationCS1RequestImpl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof SendChargingInformationCS1RequestImpl);
	        
		SendChargingInformationCS1RequestImpl elem = (SendChargingInformationCS1RequestImpl)result.getResult();
		assertNotNull(elem.getPartyToCharge());
		assertEquals(elem.getPartyToCharge(),LegType.leg1);
		assertNotNull(elem.getSCIBillingChargingCharacteristics());
		assertNotNull(((SCIBillingChargingCharacteristicsCS1)elem.getSCIBillingChargingCharacteristics()).getChargingInformation());
		assertNotNull(((SCIBillingChargingCharacteristicsCS1)elem.getSCIBillingChargingCharacteristics()).getChargingInformation().getChargeMessage());
		assertEquals(((SCIBillingChargingCharacteristicsCS1)elem.getSCIBillingChargingCharacteristics()).getChargingInformation().getChargeMessage().getEventTypeCharging(),EventTypeCharging.tariffInformation);
		assertNotNull(((SCIBillingChargingCharacteristicsCS1)elem.getSCIBillingChargingCharacteristics()).getChargingInformation().getChargeMessage().getEventSpecificInfoCharging());
		assertNotNull(((SCIBillingChargingCharacteristicsCS1)elem.getSCIBillingChargingCharacteristics()).getChargingInformation().getChargeMessage().getEventSpecificInfoCharging().getTariffInformation());
		TariffInformation tariffInformation=((SCIBillingChargingCharacteristicsCS1)elem.getSCIBillingChargingCharacteristics()).getChargingInformation().getChargeMessage().getEventSpecificInfoCharging().getTariffInformation();
		assertEquals(tariffInformation.getNumberOfStartPulses(),new Integer(1));
		assertEquals(tariffInformation.getStartInterval(),new Integer(1));
		assertEquals(tariffInformation.getStartIntervalAccuracy(),IntervalAccuracy.tenMilliSeconds);
		assertEquals(tariffInformation.getNumberOfPeriodicPulses(),new Integer(1));
		assertEquals(tariffInformation.getPeriodicInterval(),new Integer(1));
		assertEquals(tariffInformation.getPeriodicIntervalAccuracy(),IntervalAccuracy.seconds);
		assertNull(tariffInformation.getActivationTime());
		logger.info(elem);		
	}
	
	@Test
	public void testEncode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(SendChargingInformationCS1RequestImpl.class);
	    	
		TariffInformation tariffInformation=new TariffInformationImpl(1,1,IntervalAccuracy.tenMilliSeconds,1,1,IntervalAccuracy.seconds,null);
		EventSpecificInfoCharging eventSpecificCharging=new EventSpecificInfoChargingImpl(tariffInformation);
		ChargeMessage chargeMessage=new ChargeMessageImpl(EventTypeCharging.tariffInformation,eventSpecificCharging);
		ChargingInformation chargingInformation=new ChargingInformationImpl(false, chargeMessage, null, false);
		SCIBillingChargingCharacteristicsCS1 sciBillingChargingCharacteristics=new SCIBillingChargingCharacteristicsCS1Impl(chargingInformation);
		SendChargingInformationCS1RequestImpl elem = new SendChargingInformationCS1RequestImpl(sciBillingChargingCharacteristics,LegType.leg1, null);
	    byte[] rawData = this.message1;
	    ByteBuf buffer=parser.encode(elem);
	    byte[] encodedData = new byte[buffer.readableBytes()];
	    buffer.readBytes(encodedData);
	    assertTrue(Arrays.equals(rawData, encodedData));
	}
}