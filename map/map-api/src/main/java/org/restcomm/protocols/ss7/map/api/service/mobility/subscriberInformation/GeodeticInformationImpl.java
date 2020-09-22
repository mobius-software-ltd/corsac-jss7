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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.map.api.MAPException;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class GeodeticInformationImpl extends ASNOctetString {
	
	public GeodeticInformationImpl() {
    }

    public GeodeticInformationImpl(byte[] data) {
    	setValue(Unpooled.wrappedBuffer(data));        
    }

    public GeodeticInformationImpl(int screeningAndPresentationIndicators, TypeOfShape typeOfShape, double latitude,
            double longitude, double uncertainty, int confidence) throws MAPException {
        this.setData(screeningAndPresentationIndicators, typeOfShape, latitude, longitude, uncertainty, confidence);
    }

    public void setData(int screeningAndPresentationIndicators, TypeOfShape typeOfShape, double latitude, double longitude,
            double uncertainty, int confidence) throws MAPException {

        if (typeOfShape != TypeOfShape.EllipsoidPointWithUncertaintyCircle) {
            throw new MAPException(
                    "typeOfShape parameter for GeographicalInformation can be only \" ellipsoid point with uncertainty circle\"");
        }

        byte[] data = new byte[10];

        data[0] = (byte) screeningAndPresentationIndicators;
        data[1] = (byte) (typeOfShape.getCode() << 4);

        GeographicalInformationImpl.encodeLatitude(data, 2, latitude);
        GeographicalInformationImpl.encodeLongitude(data, 5, longitude);
        data[8] = (byte) GeographicalInformationImpl.encodeUncertainty(uncertainty);
        data[9] = (byte) confidence;
        setValue(Unpooled.wrappedBuffer(data));
    }

    public byte[] getData() {
    	ByteBuf value=getValue();
    	if(value==null)
    		return null;
    	
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public int getScreeningAndPresentationIndicators() {
    	byte[] data=getData();
        if (data == null || data.length != 10)
            return 0;

        return data[0];
    }

    public TypeOfShape getTypeOfShape() {
    	byte[] data=getData();
        if (data == null || data.length != 10)
            return null;

        return TypeOfShape.getInstance((data[1] & 0xFF) >> 4);
    }

    public double getLatitude() {
    	byte[] data=getData();
        if (data == null || data.length != 10)
            return 0;

        return GeographicalInformationImpl.decodeLatitude(data, 2);
    }

    public double getLongitude() {
    	byte[] data=getData();
        if (data == null || data.length != 10)
            return 0;

        return GeographicalInformationImpl.decodeLongitude(data, 5);
    }

    public double getUncertainty() {
    	byte[] data=getData();
        if (data == null || data.length != 10)
            return 0;

        return GeographicalInformationImpl.decodeUncertainty(data[8]);
    }

    public int getConfidence() {
    	byte[] data=getData();
        if (data == null || data.length != 10)
            return 0;

        return data[9];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GeodeticInformation [");

        sb.append("ScreeningAndPresentationIndicators=");
        sb.append(this.getScreeningAndPresentationIndicators());

        sb.append(", TypeOfShape=");
        sb.append(this.getTypeOfShape());

        sb.append(", Latitude=");
        sb.append(this.getLatitude());

        sb.append(", Longitude=");
        sb.append(this.getLongitude());

        sb.append(", Uncertainty=");
        sb.append(this.getUncertainty());

        sb.append(", Confidence=");
        sb.append(this.getConfidence());

        sb.append("]");

        return sb.toString();
    }
}
