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

package org.restcomm.protocols.ss7.map.primitives;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.restcomm.protocols.ss7.map.api.primitives.ProtocolId;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author cristian veliscu
 * @author yulianoifa
 *
 */
public class ExternalSignalInfoTest {
	Logger logger = LogManager.getLogger(ExternalSignalInfoTest.class);

	@Test
	public void testDecode() throws Exception {
		ASNParser parser = new ASNParser();
		parser.loadClass(ExternalSignalInfoImpl.class);

		byte[] data = new byte[] { 48, 9, 10, 1, 2, 4, 4, 10, 20, 30, 40 };
		byte[] data_ = new byte[] { 10, 20, 30, 40 };

		ASNDecodeResult result = parser.decode(Unpooled.wrappedBuffer(data));
		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ExternalSignalInfoImpl);
		ExternalSignalInfoImpl extSignalInfo = (ExternalSignalInfoImpl) result.getResult();

		ProtocolId protocolId = extSignalInfo.getProtocolId();
		ByteBuf signalInfo = extSignalInfo.getSignalInfo().getValue();

		assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(data_), signalInfo));
		assertNotNull(protocolId);
		assertTrue(protocolId == ProtocolId.gsm_0806);
	}

	@Test
	public void testEncode() throws Exception {
		ASNParser parser = new ASNParser();
		parser.loadClass(ExternalSignalInfoImpl.class);

		byte[] data = new byte[] { 48, 9, 10, 1, 2, 4, 4, 10, 20, 30, 40 };
		byte[] data_ = new byte[] { 10, 20, 30, 40 };

		SignalInfoImpl signalInfo = new SignalInfoImpl(Unpooled.wrappedBuffer(data_));
		ProtocolId protocolId = ProtocolId.gsm_0806;
		ExternalSignalInfoImpl extSignalInfo = new ExternalSignalInfoImpl(signalInfo, protocolId, null);

		ByteBuf buffer = parser.encode(extSignalInfo);
		byte[] encodedData = new byte[buffer.readableBytes()];
		buffer.readBytes(encodedData);
		assertTrue(Arrays.equals(data, encodedData));
	}
}
