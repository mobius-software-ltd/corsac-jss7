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

package org.restcomm.protocols.ss7.map.api.datacoding;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.CharacterCodingException;
import java.util.BitSet;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class GSMCharsetEncoder {

    private int bitpos = 0;
    private int carryOver;
    private GSMCharset cs;
    private GSMCharsetEncodingData encodingData;

    // The mask to check if corresponding bit in read byte is 1 or 0 and hence
    // store it i BitSet accordingly
    byte[] mask = new byte[] { 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40 };

    // BitSet to hold the bits of passed char to be encoded
    BitSet bitSet = new BitSet();

    static final byte ESCAPE = 0x1B;

    protected GSMCharsetEncoder(GSMCharset cs) {
        this.cs = cs;
    }

    public void setGSMCharsetEncodingData(GSMCharsetEncodingData encodingData) {
        this.encodingData = encodingData;
    }

    public GSMCharsetEncodingData getGSMCharsetEncodingData() {
        return this.encodingData;
    }

    byte rawData = 0;

    protected void encodeLoop(String in, ByteBuf out) {
        if (this.encodingData != null && this.encodingData.leadingBuffer != null && !this.encodingData.leadingBufferIsEncoded) {
        	if (this.encodingData.encodingStyle != Gsm7EncodingStyle.bit8_smpp_style) {
                int septetCount = (this.encodingData.leadingBuffer.readableBytes() * 8 + 6) / 7;
                bitpos = septetCount % 8;
                this.encodingData.totalSeptetCount = septetCount;
            }
        	
        	out.writeBytes(this.encodingData.leadingBuffer);
            this.encodingData.leadingBufferIsEncoded = true;
        }

        char lastChar = ' ';
        for(int j=0;j<in.length();j++) {
            // Read the first char
            char c = in.charAt(j);
            lastChar = c;

            boolean found = false;
            // searching a char in the main character table
            for (int i = 0; i < this.cs.mainTable.length; i++) {
                if (this.cs.mainTable[i] == c) {
                    found = true;
                    this.putByte(i, out);
                    break;
                }
            }

            // searching a char in the extension character table
            if (!found && this.cs.extensionTable != null) {
                for (int i = 0; i < this.cs.mainTable.length; i++) {
                    if (c != 0 && this.cs.extensionTable[i] == c) {
                        found = true;
                        this.putByte(GSMCharsetEncoder.ESCAPE, out);
                        this.putByte(i, out);
                        break;
                    }
                }
            }

            if (!found) {
                // found no suitable symbol - encode a space char
                this.putByte(0x20, out);
            }
        }

        if (this.encodingData == null || this.encodingData.encodingStyle != Gsm7EncodingStyle.bit8_smpp_style) {
            if (bitpos != 0) {
                // USSD: replace 7-bit pad with <CR>
                if (this.encodingData != null && this.encodingData.encodingStyle == Gsm7EncodingStyle.bit7_ussd_style
                        && bitpos == 7)
                    carryOver |= 0x1A;

                // writing a carryOver data
                out.writeByte((byte) carryOver);
            } else {
                // USSD: adding extra <CR> if the last symbol is <CR> and no padding
                if (this.encodingData != null && this.encodingData.encodingStyle == Gsm7EncodingStyle.bit7_ussd_style
                        && lastChar == '\r')
                    out.writeByte((byte) 0x0D);
            }
        }
    }

    private void putByte(int data, ByteBuf out) {
        if (this.encodingData != null && this.encodingData.encodingStyle == Gsm7EncodingStyle.bit8_smpp_style) {
            out.writeByte((byte) data);
        } else {

            if (bitpos == 0) {
                carryOver = data;
            } else {
                int i1 = data << (8 - bitpos);
                out.writeByte((byte) (i1 | carryOver));
                carryOver = data >>> bitpos;
            }

            bitpos++;
            if (bitpos == 8) {
                bitpos = 0;
            }

            if (this.encodingData != null)
                this.encodingData.totalSeptetCount++;
        }
    }
    
    public final ByteBuf encode(String in) throws CharacterCodingException {
    	ByteBuf out = Unpooled.buffer();
    	if (in.length() == 0)
    		return out;
            
    	encodeLoop(in, out);
    	return out;
    }  
    
    public final void encode(String in,ByteBuf out) throws CharacterCodingException {
    	if (in.length() == 0)
    		return;
            
    	encodeLoop(in, out);    	
    }  
}