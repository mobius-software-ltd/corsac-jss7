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

package org.restcomm.protocols.ss7.map.smstpdu;

import org.restcomm.protocols.ss7.map.api.smstpdu.ApplicationPortAddressing16BitAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
*
* @author sergey vetyutnev
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
