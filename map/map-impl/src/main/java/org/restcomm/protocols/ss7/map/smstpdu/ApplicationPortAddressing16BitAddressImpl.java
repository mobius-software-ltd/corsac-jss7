/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.map.smstpdu;

import org.restcomm.protocols.ss7.map.api.smstpdu.ApplicationPortAddressing16BitAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class ApplicationPortAddressing16BitAddressImpl implements ApplicationPortAddressing16BitAddress {
	private static final long serialVersionUID = 1L;
	
	private int destinationPort;
    private int originatorPort;

    /**
     * @param destinationPort
     *            These octets contain a number indicating the receiving port,
     *            i.e. application, in the receiving device.
     * @param originatorPort
     *            These octets contain a number indicating the sending port,
     *            i.e. application, in the sending device.
     */
    public ApplicationPortAddressing16BitAddressImpl(int destinationPort, int originatorPort) {
        this.destinationPort = destinationPort;
        this.originatorPort = originatorPort;
    }

    public ApplicationPortAddressing16BitAddressImpl(ByteBuf encodedInformationElementData) {

        if (encodedInformationElementData == null || encodedInformationElementData.readableBytes() != 4)
            return;

        this.destinationPort = ((encodedInformationElementData.readByte() & 0xFF) << 8) + (encodedInformationElementData.readByte() & 0xFF);
        this.originatorPort = ((encodedInformationElementData.readByte() & 0xFF) << 8) + (encodedInformationElementData.readByte() & 0xFF);
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public int getOriginatorPort() {
        return originatorPort;
    }

    public int getEncodedInformationElementIdentifier() {
        return UserDataHeaderImpl._InformationElementIdentifier_ApplicationPortAddressingScheme16BitAddress;
    }

    public ByteBuf getEncodedInformationElementData() {
    	ByteBuf res = Unpooled.buffer(4);
        res.writeByte((byte) ((this.destinationPort & 0xFF00) >> 8));
        res.writeByte((byte) (this.destinationPort & 0x00FF));
        res.writeByte((byte) ((this.originatorPort & 0xFF00) >> 8));
        res.writeByte((byte) (this.originatorPort & 0x00FF));
        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ApplicationPortAddressing16BitAddress [");
        sb.append("destinationPort=");
        sb.append(this.destinationPort);
        sb.append(", originatorPort=");
        sb.append(this.originatorPort);
        sb.append("]");

        return sb.toString();
    }

}
