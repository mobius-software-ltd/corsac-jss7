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

package org.restcomm.protocols.ss7.map.service.callhandling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberResponse;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author cristian veliscu
 * @author yulianoifa
 *
 */
public class ProvideRoamingNumberResponseTest {
	Logger logger = LogManager.getLogger(ProvideRoamingNumberResponseTest.class);

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

	private byte[] getEncodedData() {
		return new byte[] { 48, 59, 4, 7, -111, -108, -120, 115, 0, -110, -14, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4,
				11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31,
				32, 33, 4, 7, -111, -110, 17, 19, 50, 19, -15 };
	}

	private byte[] getEncodedData1() {
		return new byte[] { 4, 7, -111, -108, -120, 115, 0, -110, -14 };
	}

	private byte[] getEncodedDataFull() {
		return new byte[] { 48, 61, 4, 7, -111, -108, -120, 115, 0, -110, -14, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4,
				11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31,
				32, 33, 5, 0, 4, 7, -111, -110, 17, 19, 50, 19, -15 };
	}

	@Test
	public void testDecode() throws Exception {
		ASNParser parser = new ASNParser();
		parser.replaceClass(ProvideRoamingNumberResponseImplV1.class);
		parser.replaceClass(ProvideRoamingNumberResponseImplV3.class);

		byte[] data = this.getEncodedData();
		ASNDecodeResult result = parser.decode(Unpooled.wrappedBuffer(data));
		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ProvideRoamingNumberResponse);
		ProvideRoamingNumberResponse prn = (ProvideRoamingNumberResponse) result.getResult();

		ISDNAddressString roamingNumber = prn.getRoamingNumber();
		MAPExtensionContainer extensionContainer = prn.getExtensionContainer();
		boolean releaseResourcesSupported = prn.getReleaseResourcesSupported();
		ISDNAddressString vmscAddress = prn.getVmscAddress();

		assertNotNull(roamingNumber);
		assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
		assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
		assertEquals(roamingNumber.getAddress(), "49883700292");

		assertNotNull(extensionContainer);
		assertFalse(releaseResourcesSupported);
		assertNotNull(vmscAddress);
		assertEquals(vmscAddress.getAddressNature(), AddressNature.international_number);
		assertEquals(vmscAddress.getNumberingPlan(), NumberingPlan.ISDN);
		assertEquals(vmscAddress.getAddress(), "29113123311");

		data = this.getEncodedDataFull();
		result = parser.decode(Unpooled.wrappedBuffer(data));
		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ProvideRoamingNumberResponse);
		prn = (ProvideRoamingNumberResponse) result.getResult();

		roamingNumber = prn.getRoamingNumber();
		extensionContainer = prn.getExtensionContainer();
		releaseResourcesSupported = prn.getReleaseResourcesSupported();
		vmscAddress = prn.getVmscAddress();

		assertNotNull(roamingNumber);
		assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
		assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
		assertEquals(roamingNumber.getAddress(), "49883700292");

		assertNotNull(extensionContainer);
		assertTrue(releaseResourcesSupported);
		assertNotNull(vmscAddress);
		assertEquals(vmscAddress.getAddressNature(), AddressNature.international_number);
		assertEquals(vmscAddress.getNumberingPlan(), NumberingPlan.ISDN);
		assertEquals(vmscAddress.getAddress(), "29113123311");

		data = this.getEncodedData1();
		result = parser.decode(Unpooled.wrappedBuffer(data));
		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ProvideRoamingNumberResponse);
		prn = (ProvideRoamingNumberResponse) result.getResult();

		roamingNumber = prn.getRoamingNumber();
		extensionContainer = prn.getExtensionContainer();
		releaseResourcesSupported = prn.getReleaseResourcesSupported();
		vmscAddress = prn.getVmscAddress();

		assertNotNull(roamingNumber);
		assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
		assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
		assertEquals(roamingNumber.getAddress(), "49883700292");

		assertNull(extensionContainer);
		assertFalse(releaseResourcesSupported);
		assertNull(vmscAddress);
	}

	@Test
	public void testEncode() throws Exception {
		ASNParser parser = new ASNParser();
		parser.replaceClass(ProvideRoamingNumberResponseImplV1.class);
		parser.replaceClass(ProvideRoamingNumberResponseImplV3.class);

		ISDNAddressStringImpl roamingNumber = new ISDNAddressStringImpl(AddressNature.international_number,
				NumberingPlan.ISDN, "49883700292");
		MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
		boolean releaseResourcesSupported = false;
		ISDNAddressStringImpl vmscAddress = new ISDNAddressStringImpl(AddressNature.international_number,
				NumberingPlan.ISDN, "29113123311");

		ProvideRoamingNumberResponse prn = new ProvideRoamingNumberResponseImplV3(roamingNumber, extensionContainer,
				releaseResourcesSupported, vmscAddress);

		byte[] data = getEncodedData();
		ByteBuf buffer = parser.encode(prn);
		byte[] encodedData = new byte[buffer.readableBytes()];
		buffer.readBytes(encodedData);
		assertTrue(Arrays.equals(data, encodedData));

		releaseResourcesSupported = true;
		prn = new ProvideRoamingNumberResponseImplV3(roamingNumber, extensionContainer, releaseResourcesSupported,
				vmscAddress);

		data = getEncodedDataFull();
		buffer = parser.encode(prn);
		encodedData = new byte[buffer.readableBytes()];
		buffer.readBytes(encodedData);
		assertTrue(Arrays.equals(data, encodedData));

		// 2
		prn = new ProvideRoamingNumberResponseImplV1(roamingNumber);

		data = getEncodedData1();
		buffer = parser.encode(prn);
		encodedData = new byte[buffer.readableBytes()];
		buffer.readBytes(encodedData);
		assertTrue(Arrays.equals(data, encodedData));
	}
}