/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.NetworkSpecificFacility;

/**
 * Start time:09:37:50 2009-04-02<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class NetworkSpecificFacilityImpl extends AbstractISUPParameter implements NetworkSpecificFacility {
	/**
     * This tells us to include byte 1a - sets lengthOfNetworkIdentification to 1+networkdIdentification.length
     */
    private boolean includeNetworkIdentification;

    private int lengthOfNetworkIdentification;
    private int typeOfNetworkIdentification;
    private int networkIdentificationPlan;
    // FIXME: ext bit: indicated as to be used as in 3.25 but on specs id
    // different...
    private ByteBuf networkIdentification;
    private ByteBuf networkSpecificaFacilityIndicator;

    public NetworkSpecificFacilityImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public NetworkSpecificFacilityImpl() {
        super();

    }

    public NetworkSpecificFacilityImpl(boolean includeNetworkIdentification, byte typeOfNetworkIdentification,
            byte networkdIdentificationPlan, ByteBuf networkdIdentification, ByteBuf networkSpecificaFacilityIndicator) {
        super();
        this.includeNetworkIdentification = includeNetworkIdentification;
        this.typeOfNetworkIdentification = typeOfNetworkIdentification;
        this.networkIdentificationPlan = networkdIdentificationPlan;
        this.networkIdentification = networkdIdentification;
        this.networkSpecificaFacilityIndicator = networkSpecificaFacilityIndicator;
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() < 1) {
            throw new ParameterException("buffer must nto be null or have length greater than 1");
        }
        // try {
        this.lengthOfNetworkIdentification = b.readByte();

        // FIXME: We ignore ext bit, we dont need it ? ?????
        byte curr=b.readByte();
        this.typeOfNetworkIdentification = (byte) ((curr >> 4) & 0x07);
        this.networkIdentificationPlan = (byte) (curr & 0x0F);
        
        if (this.lengthOfNetworkIdentification > 0) {
        	ByteBuf _networkId=Unpooled.buffer(this.lengthOfNetworkIdentification);
            for (int i = 0; i < this.lengthOfNetworkIdentification-1; i++) {
                _networkId.writeByte((byte) (b.readByte() | 0x80));
            }

            // now lets set it.
            _networkId.writeByte((byte) (b.readByte() & 0x7F));            
            this.setNetworkIdentification(_networkId);
        }

        if (b.readableBytes()==0) {
            throw new ParameterException("There is no facility indicator. This part is mandatory!!!");
        }
        
        ByteBuf _facility=b.slice(b.readerIndex(), b.readableBytes());
        this.setNetworkSpecificaFacilityIndicator(_facility);        
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        buffer.writeByte(this.lengthOfNetworkIdentification);
        // This should always be set to true if there is network ID
        if (this.includeNetworkIdentification) {
            int b1 = 0;
            b1 = ((this.typeOfNetworkIdentification & 0x07) << 4);
            b1 |= (this.networkIdentificationPlan & 0x0F);

            ByteBuf curr=getNetworkIdentification();
            if (curr != null && curr.readableBytes() > 0) {
                b1 |= 0x80;
                buffer.writeByte(b1);
                while(curr.readableBytes()>0) {
                	byte currByte=curr.readByte();
                    if (curr.readableBytes()==0) {
                        buffer.writeByte(currByte & 0x7F);
                    } else {
                    	buffer.writeByte(currByte | (0x01 << 7));
                    }
                }
            } else {
            	buffer.writeByte(b1 & 0x7F);
            }
        }

        if (this.networkSpecificaFacilityIndicator == null) {
            throw new IllegalArgumentException("Network Specific Facility must not be null");
        }
        
        buffer.writeBytes(getNetworkSpecificaFacilityIndicator());
    }

    public boolean isIncludeNetworkIdentification() {
        return includeNetworkIdentification;
    }

    public int getLengthOfNetworkIdentification() {
        return lengthOfNetworkIdentification;
    }

    public int getTypeOfNetworkIdentification() {
        return typeOfNetworkIdentification;
    }

    public void setTypeOfNetworkIdentification(byte typeOfNetworkIdentification) {
        this.typeOfNetworkIdentification = typeOfNetworkIdentification;
    }

    public int getNetworkIdentificationPlan() {
        return networkIdentificationPlan;
    }

    public void setNetworkIdentificationPlan(byte networkdIdentificationPlan) {
        this.networkIdentificationPlan = networkdIdentificationPlan;
    }

    public ByteBuf getNetworkIdentification() {
    	if(networkIdentification==null)
    		return null;
    	
        return Unpooled.wrappedBuffer(networkIdentification);
    }

    public void setNetworkIdentification(ByteBuf networkdIdentification) {

        if (networkdIdentification != null && networkdIdentification.readableBytes() > Byte.MAX_VALUE * 2 - 1) {
            throw new IllegalArgumentException("Length of Network Identification part must not be greater than: "
                    + (Byte.MAX_VALUE * 2 - 1));
        }

        this.networkIdentification = networkdIdentification;
        this.includeNetworkIdentification = true;

    }

    public ByteBuf getNetworkSpecificaFacilityIndicator() {
    	if(networkSpecificaFacilityIndicator==null)
    		return null;
    	
        return Unpooled.wrappedBuffer(networkSpecificaFacilityIndicator);
    }

    public void setNetworkSpecificaFacilityIndicator(ByteBuf networkSpecificaFacilityIndicator) {
        this.networkSpecificaFacilityIndicator = networkSpecificaFacilityIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
