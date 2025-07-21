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

package org.restcomm.protocols.ss7.commonapp.datacoding;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CoderMalfunctionError;
import java.nio.charset.CoderResult;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class GSMCharsetDecoder {

	private byte[] mask = new byte[] { 0x7F, 0x3F, 0x1F, 0x0F, 0x07, 0x03, 0x01 };

	private int bitpos = 0;
	private int decodedBytes = 0;
	private byte leftOver;
	private GSMCharset cs;
	private boolean escape;
	private GSMCharsetDecodingData encodingData;

	/**
	 * Constructs a Decoder.
	 */
	protected GSMCharsetDecoder(GSMCharset cs) {
		this.cs = cs;
	}

	public void setGSMCharsetDecodingData(GSMCharsetDecodingData encodingData) {
		this.encodingData = encodingData;
	}

	public GSMCharsetDecodingData getGSMCharsetDecodingData() {
		return this.encodingData;
	}

	protected CoderResult decodeLoop(ByteBuf in, StringBuilder out) {

		while (in.readableBytes() > 0) {

			// Read the first byte
			byte data = in.readByte();

			if (this.encodingData != null && this.encodingData.encodingStyle == Gsm7EncodingStyle.bit8_smpp_style)
				putChar(data, out);
			else {
				// take back-up of byte
				byte tempData = data;

				// the rest of bits that we don't need now but for next iteration
				byte tempCurrHol = (byte) ((data & 0xFF) >>> (7 - bitpos));

				// The bits that will be consumed for formation of current char
				data = (byte) (data & mask[bitpos]);

				if (bitpos != 0) {
					// we don't have enough bits to form char, we need to use the
					// bits
					// from previous iteration

					// Move the curent bits read to left and append the previous
					// bits
					data = (byte) (data << bitpos);
					data = (byte) (data | leftOver);

					// We have 7 bits now to form char
					putChar(data, out);

					// This means we not only used previous 6 bits and 1 bit from
					// current byte to form char, but now we also have 7 bits left
					// over from current byte and hence we can get another char
					if (bitpos == 6) {
						data = (byte) (((tempData & 0xFE)) >>> 1);

						if (this.encodingData != null
								&& this.encodingData.encodingStyle == Gsm7EncodingStyle.bit7_ussd_style && data == '\r'
								&& in.readableBytes() == 0) {
							// case when found '\r' at the byte border if USSD style: skip final '\r' char
						} else
							putChar(data, out);
					}
				} else
					// For this iteration we have all 7 bits to form the char
					// from byte that we read
					putChar(data, out);

				// assign the left over bits
				leftOver = tempCurrHol;

				bitpos++;
				if (bitpos == 7)
					bitpos = 0;
			}
		}

		return CoderResult.UNDERFLOW;
	}

	private void putChar(byte data, StringBuilder out) {

		this.decodedBytes++;
		if (this.encodingData != null)
			if (this.encodingData.septetCount >= 0 && this.decodedBytes > this.encodingData.septetCount)
				return;

		int code = 0;
		if (data >= 0 && data < 128)
			if (escape) {
				escape = false;
				if (this.cs.extensionTable != null)
					code = this.cs.extensionTable[data];
			} else if (data == GSMCharset.ESCAPE) {
				escape = true;
				return;
			} else
				code = this.cs.mainTable[data];

		if (code == 0)
			out.append(' ');
		else
			out.append((char) code);
	}

	public final String decode(ByteBuf in) throws CharacterCodingException {
		StringBuilder sb = new StringBuilder();
		if (encodingData.leadingBitsSkipCount != 0) {
			int currByte = in.readByte() & 0x0FF;
			if (encodingData.leadingBitsSkipCount == 1) {
				currByte = (byte) (((currByte & 0xFE)) >>> 1);

				if (this.encodingData != null && this.encodingData.encodingStyle == Gsm7EncodingStyle.bit7_ussd_style
						&& currByte == '\r' && in.readableBytes() == 0) {
					// case when found '\r' at the byte border if USSD style: skip final '\r' char
				} else
					putChar((byte) currByte, sb);
			} else {
				bitpos = 7 - encodingData.leadingBitsSkipCount;
				leftOver = (byte) (currByte >>> (7 - bitpos));

				bitpos++;
				if (bitpos == 7)
					bitpos = 0;
			}
		}

		try {
			decodeLoop(in, sb);
		} catch (BufferUnderflowException x) {
			throw new CoderMalfunctionError(x);
		} catch (BufferOverflowException x) {
			throw new CoderMalfunctionError(x);
		}

		return sb.toString();
	}
}