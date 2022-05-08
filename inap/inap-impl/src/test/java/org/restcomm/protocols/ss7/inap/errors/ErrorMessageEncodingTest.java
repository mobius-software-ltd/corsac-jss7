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

package org.restcomm.protocols.ss7.inap.errors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.inap.api.errors.CancelProblem;
import org.restcomm.protocols.ss7.inap.api.errors.RequestedInfoErrorParameter;
import org.restcomm.protocols.ss7.inap.api.errors.TaskRefusedParameter;
import org.restcomm.protocols.ss7.inap.api.errors.UnavailableNetworkResource;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author yulian.oifa
 *
 */
public class ErrorMessageEncodingTest {

	public byte[] getDataTaskRefused() {
		return new byte[] { 10, 1, 2 };
	}

	public byte[] getDataSystemFailure() {
		return new byte[] { 10, 1, 3 };
	}

	public byte[] getDataRequestedInfoError() {
		return new byte[] { 10, 1, 1 };
	}

	public byte[] getDataCancelFailed() {
		return new byte[] { 10, 1, 1 };
	}

	@Test(groups = { "functional.decode", "errors.primitive" })
	public void testDecode() throws Exception {
		ASNParser parser = new ASNParser(true);
		parser.replaceClass(INAPErrorMessageTaskRefusedImpl.class);

		byte[] rawData = this.getDataTaskRefused();
		ASNDecodeResult result = parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof INAPErrorMessageTaskRefusedImpl);

		INAPErrorMessageTaskRefusedImpl elem = (INAPErrorMessageTaskRefusedImpl) result.getResult();
		assertEquals(elem.getTaskRefusedParameter(), TaskRefusedParameter.congestion);

		parser.replaceClass(INAPErrorMessageSystemFailureImpl.class);
		rawData = this.getDataSystemFailure();
		result = parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof INAPErrorMessageSystemFailureImpl);

		INAPErrorMessageSystemFailureImpl elem2 = (INAPErrorMessageSystemFailureImpl) result.getResult();
		assertEquals(elem2.getUnavailableNetworkResource(), UnavailableNetworkResource.resourceStatusFailure);

		parser.replaceClass(INAPErrorMessageRequestedInfoErrorImpl.class);
		rawData = this.getDataRequestedInfoError();
		result = parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof INAPErrorMessageRequestedInfoErrorImpl);

		INAPErrorMessageRequestedInfoErrorImpl elem3 = (INAPErrorMessageRequestedInfoErrorImpl) result.getResult();
		assertEquals(elem3.getRequestedInfoErrorParameter(), RequestedInfoErrorParameter.unknownRequestedInfo);

		parser.replaceClass(INAPErrorMessageCancelFailedImpl.class);
		rawData = this.getDataCancelFailed();
		result = parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof INAPErrorMessageCancelFailedImpl);

		INAPErrorMessageCancelFailedImpl elem4 = (INAPErrorMessageCancelFailedImpl) result.getResult();
		assertEquals(elem4.getCancelProblem(), CancelProblem.tooLate);
	}

	@Test(groups = { "functional.encode", "errors.primitive" })
	public void testEncode() throws Exception {
		ASNParser parser = new ASNParser(true);

		INAPErrorMessageTaskRefusedImpl elem = new INAPErrorMessageTaskRefusedImpl(TaskRefusedParameter.congestion);
		byte[] rawData = this.getDataTaskRefused();
		ByteBuf buffer = parser.encode(elem);
		byte[] encodedData = new byte[buffer.readableBytes()];
		buffer.readBytes(encodedData);
		assertTrue(Arrays.equals(rawData, encodedData));

		INAPErrorMessageSystemFailureImpl elem2 = new INAPErrorMessageSystemFailureImpl(
				UnavailableNetworkResource.resourceStatusFailure);
		rawData = this.getDataSystemFailure();
		buffer = parser.encode(elem2);
		encodedData = new byte[buffer.readableBytes()];
		buffer.readBytes(encodedData);
		assertTrue(Arrays.equals(rawData, encodedData));

		INAPErrorMessageRequestedInfoErrorImpl elem3 = new INAPErrorMessageRequestedInfoErrorImpl(
				RequestedInfoErrorParameter.unknownRequestedInfo);
		rawData = this.getDataRequestedInfoError();
		buffer = parser.encode(elem3);
		encodedData = new byte[buffer.readableBytes()];
		buffer.readBytes(encodedData);
		assertTrue(Arrays.equals(rawData, encodedData));

		INAPErrorMessageCancelFailedImpl elem4 = new INAPErrorMessageCancelFailedImpl(CancelProblem.tooLate);
		rawData = this.getDataCancelFailed();
		buffer = parser.encode(elem4);
		encodedData = new byte[buffer.readableBytes()];
		buffer.readBytes(encodedData);
		assertTrue(Arrays.equals(rawData, encodedData));
	}
}