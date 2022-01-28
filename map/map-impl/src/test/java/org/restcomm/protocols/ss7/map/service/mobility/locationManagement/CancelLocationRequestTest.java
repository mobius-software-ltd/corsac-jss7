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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
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
public class CancelLocationRequestTest {

	private byte[] getEncodedData() {
		return new byte[] { -93, 72, 4, 5, 17, 17, 33, 34, 34, 10, 1, 1, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11,
				12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32,
				33, -128, 1, 0, -125, 4, -111, 34, 34, -8, -124, 4, -111, 34, 34, -7, -123, 4, 0, 3, 98, 39 };
	}

	private byte[] getEncodedData1() {
		return new byte[] { -93, 84, 48, 13, 4, 5, 17, 17, 33, 34, 34, 4, 4, 0, 3, 98, 39, 10, 1, 1, 48, 39, -96, 32,
				48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
				24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -127, 0, -126, 0, -125, 4, -111, 34, 34, -8, -124, 4, -111,
				34, 34, -7, -123, 4, 0, 3, 98, 39 };
	}

	private byte[] getEncodedData2() {
		return new byte[] { 4, 5, 17, 17, 33, 34, 34 };
	}

	private byte[] getEncodedData3() {
		return new byte[] { 48, 13, 4, 5, 17, 17, 33, 34, 34, 4, 4, 0, 3, 98, 39 };
	}

	public byte[] getDataLmsi() {
		return new byte[] { 0, 3, 98, 39 };
	}

	@Test(groups = { "functional.decode", "locationManagement" })
	public void testDecode() throws Exception {
		ASNParser parser = new ASNParser();
		parser.replaceClass(CancelLocationRequestImplV1.class);
		parser.replaceClass(CancelLocationRequestImplV3.class);

		byte[] data = getEncodedData();
		ASNDecodeResult result = parser.decode(Unpooled.wrappedBuffer(data));
		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof CancelLocationRequest);
		CancelLocationRequest asc = (CancelLocationRequest) result.getResult();

		IMSI imsi = asc.getImsi();
		IMSIWithLMSI imsiWithLmsi = asc.getImsiWithLmsi();
		CancellationType cancellationType = asc.getCancellationType();
		MAPExtensionContainer extensionContainer = asc.getExtensionContainer();
		TypeOfUpdate typeOfUpdate = asc.getTypeOfUpdate();
		boolean mtrfSupportedAndAuthorized = asc.getMtrfSupportedAndAuthorized();
		boolean mtrfSupportedAndNotAuthorized = asc.getMtrfSupportedAndNotAuthorized();
		ISDNAddressString newMSCNumber = asc.getNewMSCNumber();
		ISDNAddressString newVLRNumber = asc.getNewVLRNumber();
		LMSI newLmsi = asc.getNewLmsi();
		long mapProtocolVersion = asc.getMapProtocolVersion();

		assertTrue(imsi.getData().equals("1111122222"));
		assertNull(imsiWithLmsi);
		assertEquals(cancellationType.getCode(), 1);
		assertNotNull(extensionContainer);
		assertEquals(typeOfUpdate.getCode(), 0);
		assertFalse(mtrfSupportedAndAuthorized);
		assertFalse(mtrfSupportedAndNotAuthorized);
		assertTrue(newMSCNumber.getAddress().equals("22228"));
		assertEquals(newMSCNumber.getAddressNature(), AddressNature.international_number);
		assertEquals(newMSCNumber.getNumberingPlan(), NumberingPlan.ISDN);
		assertTrue(newVLRNumber.getAddress().equals("22229"));
		assertEquals(newVLRNumber.getAddressNature(), AddressNature.international_number);
		assertEquals(newVLRNumber.getNumberingPlan(), NumberingPlan.ISDN);
		assertTrue(ByteBufUtil.equals(newLmsi.getValue(),Unpooled.wrappedBuffer(getDataLmsi())));
		assertEquals(mapProtocolVersion, 3);

		// encode data 1
		data = getEncodedData1();
		result = parser.decode(Unpooled.wrappedBuffer(data));
		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof CancelLocationRequest);
		asc = (CancelLocationRequest) result.getResult();

		imsi = asc.getImsi();
		imsiWithLmsi = asc.getImsiWithLmsi();
		cancellationType = asc.getCancellationType();
		extensionContainer = asc.getExtensionContainer();
		typeOfUpdate = asc.getTypeOfUpdate();
		mtrfSupportedAndAuthorized = asc.getMtrfSupportedAndAuthorized();
		mtrfSupportedAndNotAuthorized = asc.getMtrfSupportedAndNotAuthorized();
		newMSCNumber = asc.getNewMSCNumber();
		newVLRNumber = asc.getNewVLRNumber();
		newLmsi = asc.getNewLmsi();
		mapProtocolVersion = asc.getMapProtocolVersion();

		assertNull(imsi);
		assertNotNull(imsiWithLmsi);
		assertTrue(imsiWithLmsi.getImsi().getData().equals("1111122222"));
		LMSI lmsi = imsiWithLmsi.getLmsi();
		assertTrue(ByteBufUtil.equals(lmsi.getValue(),Unpooled.wrappedBuffer(getDataLmsi())));

		assertEquals(cancellationType.getCode(), 1);
		assertNotNull(extensionContainer);
		assertEquals(typeOfUpdate.getCode(), 0);
		assertTrue(mtrfSupportedAndAuthorized);
		assertTrue(mtrfSupportedAndNotAuthorized);
		assertTrue(newMSCNumber.getAddress().equals("22228"));
		assertEquals(newMSCNumber.getAddressNature(), AddressNature.international_number);
		assertEquals(newMSCNumber.getNumberingPlan(), NumberingPlan.ISDN);
		assertTrue(newVLRNumber.getAddress().equals("22229"));
		assertEquals(newVLRNumber.getAddressNature(), AddressNature.international_number);
		assertEquals(newVLRNumber.getNumberingPlan(), NumberingPlan.ISDN);
		assertTrue(ByteBufUtil.equals(newLmsi.getValue(),Unpooled.wrappedBuffer(getDataLmsi())));
		assertEquals(mapProtocolVersion, 3);

		// encode data 2
		data = getEncodedData2();
		result = parser.decode(Unpooled.wrappedBuffer(data));
		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof CancelLocationRequest);
		asc = (CancelLocationRequest) result.getResult();

		imsi = asc.getImsi();
		imsiWithLmsi = asc.getImsiWithLmsi();
		cancellationType = asc.getCancellationType();
		extensionContainer = asc.getExtensionContainer();
		typeOfUpdate = asc.getTypeOfUpdate();
		mtrfSupportedAndAuthorized = asc.getMtrfSupportedAndAuthorized();
		mtrfSupportedAndNotAuthorized = asc.getMtrfSupportedAndNotAuthorized();
		newMSCNumber = asc.getNewMSCNumber();
		newVLRNumber = asc.getNewVLRNumber();
		newLmsi = asc.getNewLmsi();
		mapProtocolVersion = asc.getMapProtocolVersion();

		assertTrue(imsi.getData().equals("1111122222"));
		assertNull(imsiWithLmsi);
		assertNull(cancellationType);
		assertNull(extensionContainer);
		assertNull(typeOfUpdate);
		assertFalse(mtrfSupportedAndAuthorized);
		assertFalse(mtrfSupportedAndNotAuthorized);
		assertNull(newMSCNumber);
		assertNull(newVLRNumber);
		assertNull(newLmsi);
		assertEquals(mapProtocolVersion, 2);

		// encode data 3
		data = getEncodedData3();
		result = parser.decode(Unpooled.wrappedBuffer(data));
		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof CancelLocationRequest);
		asc = (CancelLocationRequest) result.getResult();

		imsi = asc.getImsi();
		imsiWithLmsi = asc.getImsiWithLmsi();
		cancellationType = asc.getCancellationType();
		extensionContainer = asc.getExtensionContainer();
		typeOfUpdate = asc.getTypeOfUpdate();
		mtrfSupportedAndAuthorized = asc.getMtrfSupportedAndAuthorized();
		mtrfSupportedAndNotAuthorized = asc.getMtrfSupportedAndNotAuthorized();
		newMSCNumber = asc.getNewMSCNumber();
		newVLRNumber = asc.getNewVLRNumber();
		newLmsi = asc.getNewLmsi();
		mapProtocolVersion = asc.getMapProtocolVersion();

		assertNull(imsi);
		// assertNotNull(imsiWithLmsi);
		assertTrue(imsiWithLmsi.getImsi().getData().equals("1111122222"));
		lmsi = imsiWithLmsi.getLmsi();
		assertTrue(ByteBufUtil.equals(lmsi.getValue(),Unpooled.wrappedBuffer(getDataLmsi())));
		assertNull(cancellationType);
		assertNull(extensionContainer);
		assertNull(typeOfUpdate);
		assertFalse(mtrfSupportedAndAuthorized);
		assertFalse(mtrfSupportedAndNotAuthorized);
		assertNull(newMSCNumber);
		assertNull(newVLRNumber);
		assertNull(newLmsi);
		assertEquals(mapProtocolVersion, 2);
	}

	@Test(groups = { "functional.encode", "locationManagement" })
	public void testEncode() throws Exception {
		ASNParser parser = new ASNParser();
		parser.replaceClass(CancelLocationRequestImplV1.class);
		parser.replaceClass(CancelLocationRequestImplV3.class);

		IMSIImpl imsi = new IMSIImpl("1111122222");
		LMSIImpl lmsi = new LMSIImpl(Unpooled.wrappedBuffer(getDataLmsi()));
		IMSIWithLMSIImpl imsiWithLmsi = new IMSIWithLMSIImpl(imsi, lmsi);
		CancellationType cancellationType = CancellationType.getInstance(1);
		MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
		TypeOfUpdate typeOfUpdate = TypeOfUpdate.getInstance(0);
		boolean mtrfSupportedAndAuthorized = false;
		boolean mtrfSupportedAndNotAuthorized = false;
		ISDNAddressStringImpl newMSCNumber = new ISDNAddressStringImpl(AddressNature.international_number,
				NumberingPlan.ISDN, "22228");
		ISDNAddressStringImpl newVLRNumber = new ISDNAddressStringImpl(AddressNature.international_number,
				NumberingPlan.ISDN, "22229");
		LMSIImpl newLmsi = new LMSIImpl(Unpooled.wrappedBuffer(getDataLmsi()));
		long mapProtocolVersion = 3;

		CancelLocationRequest asc = new CancelLocationRequestImplV3(imsi, null, cancellationType, extensionContainer,
				typeOfUpdate, mtrfSupportedAndAuthorized, mtrfSupportedAndNotAuthorized, newMSCNumber, newVLRNumber,
				newLmsi, mapProtocolVersion);

		byte[] data = getEncodedData();
		ByteBuf buffer = parser.encode(asc);
		byte[] encodedData = new byte[buffer.readableBytes()];
		buffer.readBytes(encodedData);
		assertTrue(Arrays.equals(data, encodedData));

		mtrfSupportedAndAuthorized = true;
		mtrfSupportedAndNotAuthorized = true;
		asc = new CancelLocationRequestImplV3(null, imsiWithLmsi, cancellationType, extensionContainer, typeOfUpdate,
				mtrfSupportedAndAuthorized, mtrfSupportedAndNotAuthorized, newMSCNumber, newVLRNumber, newLmsi,
				mapProtocolVersion);

		data = getEncodedData1();
		buffer = parser.encode(asc);
		encodedData = new byte[buffer.readableBytes()];
		buffer.readBytes(encodedData);
		assertTrue(Arrays.equals(data, encodedData));

		mapProtocolVersion = 2;
		asc = new CancelLocationRequestImplV1(imsi, null, mapProtocolVersion);

		data = getEncodedData2();
		buffer = parser.encode(asc);
		encodedData = new byte[buffer.readableBytes()];
		buffer.readBytes(encodedData);
		assertTrue(Arrays.equals(data, encodedData));

		asc = new CancelLocationRequestImplV1(null, imsiWithLmsi, mapProtocolVersion);

		data = getEncodedData3();
		buffer = parser.encode(asc);
		encodedData = new byte[buffer.readableBytes()];
		buffer.readBytes(encodedData);
		assertTrue(Arrays.equals(data, encodedData));
	}
}