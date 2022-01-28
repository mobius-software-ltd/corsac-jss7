/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.map.smstpdu;

import java.nio.charset.Charset;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.smstpdu.CommandData;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CommandDataImpl implements CommandData {
	private ByteBuf encodedData;
    private String decodedMessage;

    private boolean isDecoded;
    private boolean isEncoded;

    public CommandDataImpl(ByteBuf data) {
        this.encodedData = data;
        this.isEncoded = true;
    }

    public CommandDataImpl(String decodedMessage) {
        this.decodedMessage = decodedMessage;
        this.isDecoded = true;
    }

    public ByteBuf getEncodedData() {
    	if(this.encodedData==null)
    		return null;
    	
        return Unpooled.wrappedBuffer(this.encodedData);
    }

    public String getDecodedMessage() {
        return decodedMessage;
    }

    public void encode() throws MAPException {

        if (this.isEncoded)
            return;
        this.isEncoded = true;

        this.encodedData = null;

        if (this.decodedMessage == null)
            this.decodedMessage = "";

        // TODO: what is an encoding algorithm ?
        Charset chs = Charset.forName("US-ASCII");
        this.encodedData = Unpooled.wrappedBuffer(this.decodedMessage.getBytes(chs));
    }

    public void decode() throws MAPException {

        if (this.isDecoded)
            return;
        this.isDecoded = true;

        this.decodedMessage = null;

        if (this.encodedData == null)
            throw new MAPException("Error decoding a text from Sms CommandData: encodedData field is null");

        // TODO: what is an encoding algorithm ?
        Charset chs = Charset.forName("US-ASCII");
        this.decodedMessage = Unpooled.wrappedBuffer(this.encodedData).toString(chs);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("TP-Command-Data [");
        if (this.decodedMessage == null) {
            if (this.encodedData != null)
                sb.append(ASNOctetString.printDataArr(Unpooled.wrappedBuffer(this.encodedData)));
        } else {
            sb.append("Msg:[");
            sb.append(this.decodedMessage);
            sb.append("]");
        }
        sb.append("]");

        return sb.toString();
    }
}
