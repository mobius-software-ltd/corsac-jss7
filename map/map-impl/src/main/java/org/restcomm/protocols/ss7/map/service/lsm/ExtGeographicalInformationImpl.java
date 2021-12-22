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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.TypeOfShape;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public class ExtGeographicalInformationImpl extends ASNOctetString implements ExtGeographicalInformation {
	public ExtGeographicalInformationImpl() {
    }

    public ExtGeographicalInformationImpl(byte[] data) {
        setValue(Unpooled.wrappedBuffer(data));
    }

    public ExtGeographicalInformationImpl(TypeOfShape typeOfShape, double latitude, double longitude, double uncertainty,
            double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis, double angleOfMajorAxis, int confidence,
            int altitude, double uncertaintyAltitude, int innerRadius, double uncertaintyRadius, double offsetAngle,
            double includedAngle) throws MAPException {     
        initData(typeOfShape, latitude, longitude, uncertainty, uncertaintySemiMajorAxis, uncertaintySemiMinorAxis,
                angleOfMajorAxis, confidence, altitude, uncertaintyAltitude, innerRadius, uncertaintyRadius, offsetAngle,
                includedAngle);
    }

    protected void initData(TypeOfShape typeOfShape, double latitude, double longitude, double uncertainty,
            double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis, double angleOfMajorAxis, int confidence,
            int altitude, double uncertaintyAltitude, int innerRadius, double uncertaintyRadius, double offsetAngle,
            double includedAngle) throws MAPException {

        if (typeOfShape == null) {
            throw new MAPException("typeOfShape parameter is null");
        }

        byte[] data;
        switch (typeOfShape) {
            case EllipsoidPointWithUncertaintyCircle:
                data=this.initData(8, typeOfShape, latitude, longitude);
                data[7] = (byte) GeographicalInformationImpl.encodeUncertainty(uncertainty);
                break;

            case EllipsoidPointWithUncertaintyEllipse:
            	data=this.initData(11, typeOfShape, latitude, longitude);
                data[7] = (byte) GeographicalInformationImpl.encodeUncertainty(uncertaintySemiMajorAxis);
                data[8] = (byte) GeographicalInformationImpl.encodeUncertainty(uncertaintySemiMinorAxis);
                data[9] = (byte) (angleOfMajorAxis / 2);
                data[10] = (byte) confidence;
                break;

            case EllipsoidPointWithAltitudeAndUncertaintyEllipsoid:
            	data=this.initData(14, typeOfShape, latitude, longitude);

                boolean negativeSign = false;
                if (altitude < 0) {
                    negativeSign = true;
                    altitude = -altitude;
                }
                if (altitude > 0x7FFF)
                    altitude = 0x7FFF;
                if (negativeSign)
                    altitude |= 0x8000;
                data[7] = (byte) ((altitude & 0xFF00) >> 8);
                data[8] = (byte) (altitude & 0xFF);

                data[9] = (byte) GeographicalInformationImpl.encodeUncertainty(uncertaintySemiMajorAxis);
                data[10] = (byte) GeographicalInformationImpl.encodeUncertainty(uncertaintySemiMinorAxis);
                data[11] = (byte) (angleOfMajorAxis / 2);
                data[12] = (byte) GeographicalInformationImpl.encodeUncertainty(uncertaintyAltitude);
                data[13] = (byte) confidence;
                break;

            case EllipsoidArc:
            	data=this.initData(13, typeOfShape, latitude, longitude);

                if (innerRadius > 0x7FFF)
                    innerRadius = 0x7FFF;
                data[7] = (byte) ((innerRadius & 0xFF00) >> 8);
                data[8] = (byte) (innerRadius & 0xFF);
                data[9] = (byte) GeographicalInformationImpl.encodeUncertainty(uncertaintyRadius);
                data[10] = (byte) (offsetAngle / 2);
                data[11] = (byte) (includedAngle / 2);
                data[12] = (byte) confidence;

                break;

            case EllipsoidPoint:
            	data=this.initData(7, typeOfShape, latitude, longitude);
                break;

            default:
                throw new MAPException("typeOfShape parameter has bad value");                
        }
        
        if(data!=null)
        	setValue(Unpooled.wrappedBuffer(data));
    }

    private byte[] initData(int len, TypeOfShape typeOfShape, double latitude, double longitude) {
        byte[] data = new byte[len];
        data[0] = (byte) (typeOfShape.getCode() << 4);
        GeographicalInformationImpl.encodeLatitude(data, 1, latitude);
        GeographicalInformationImpl.encodeLongitude(data, 4, longitude);
        
        return data;
    }

    public byte[] getData() {
    	ByteBuf buffer=getValue();
    	if(getValue()==null)
    		return null;
    	
        byte[] data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
    	return data;
    }

    public TypeOfShape getTypeOfShape() {
    	byte[] data=getData();
        if (data == null || data.length < 1)
            return null;

        return TypeOfShape.getInstance((data[0] & 0xFF) >> 4);
    }

    public double getLatitude() {
    	byte[] data=getData();
        if (data == null || data.length < 7)
            return 0;

        return GeographicalInformationImpl.decodeLatitude(data, 1);
    }

    public double getLongitude() {
    	byte[] data=getData();
        if (data == null || data.length < 7)
            return 0;

        return GeographicalInformationImpl.decodeLongitude(data, 4);
    }

    public double getUncertainty() {
    	byte[] data=getData();
        if (this.getTypeOfShape() != TypeOfShape.EllipsoidPointWithUncertaintyCircle || data == null
                || data.length != 8)
            return 0;

        return GeographicalInformationImpl.decodeUncertainty(data[7]);
    }

    public double getUncertaintySemiMajorAxis() {
    	byte[] data=getData();
        TypeOfShape typeOfShape = this.getTypeOfShape();
        if (typeOfShape != null) {
            switch (typeOfShape) {
                case EllipsoidPointWithUncertaintyEllipse:
                    if (data == null || data.length != 11)
                        return 0;
                    return GeographicalInformationImpl.decodeUncertainty(data[7]);

                case EllipsoidPointWithAltitudeAndUncertaintyEllipsoid:
                    if (data == null || data.length != 14)
                        return 0;
                    return GeographicalInformationImpl.decodeUncertainty(data[9]);
				default:
					break;
            }
        }

        return 0;
    }

    public double getUncertaintySemiMinorAxis() {
    	byte[] data=getData();
        TypeOfShape typeOfShape = this.getTypeOfShape();
        if (typeOfShape != null) {
            switch (typeOfShape) {
                case EllipsoidPointWithUncertaintyEllipse:
                    if (data == null || data.length != 11)
                        return 0;
                    return GeographicalInformationImpl.decodeUncertainty(data[8]);

                case EllipsoidPointWithAltitudeAndUncertaintyEllipsoid:
                    if (data == null || data.length != 14)
                        return 0;
                    return GeographicalInformationImpl.decodeUncertainty(data[10]);
				default:
					break;
            }
        }

        return 0;
    }

    public double getAngleOfMajorAxis() {
    	byte[] data=getData();
        TypeOfShape typeOfShape = this.getTypeOfShape();
        if (typeOfShape != null) {
            switch (typeOfShape) {
                case EllipsoidPointWithUncertaintyEllipse:
                    if (data == null || data.length != 11)
                        return 0;
                    return (data[9] & 0xFF) * 2;

                case EllipsoidPointWithAltitudeAndUncertaintyEllipsoid:
                    if (data == null || data.length != 14)
                        return 0;
                    return (data[11] & 0xFF) * 2;
				default:
					break;
            }
        }

        return 0;
    }

    public int getConfidence() {
    	byte[] data=getData();
        TypeOfShape typeOfShape = this.getTypeOfShape();
        if (typeOfShape != null) {
            switch (typeOfShape) {
                case EllipsoidPointWithUncertaintyEllipse:
                    if (data == null || data.length != 11)
                        return 0;
                    return data[10];

                case EllipsoidPointWithAltitudeAndUncertaintyEllipsoid:
                    if (data == null || data.length != 14)
                        return 0;
                    return data[13];

                case EllipsoidArc:
                    if (data == null || data.length != 13)
                        return 0;
                    return data[12];
				default:
					break;
            }
        }

        return 0;
    }

    public int getAltitude() {
    	byte[] data=getData();
        if (this.getTypeOfShape() != TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid || data == null
                || data.length != 14)
            return 0;

        int i1 = ((data[7] & 0xFF) << 8) + (data[8] & 0xFF);
        int sign = 1;
        if ((i1 & 0x8000) != 0) {
            sign = -1;
            i1 = i1 & 0x7FFF;
        }
        return i1 * sign;
    }

    public double getUncertaintyAltitude() {
    	byte[] data=getData();
        if (this.getTypeOfShape() != TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid || data == null
                || data.length != 14)
            return 0;

        return GeographicalInformationImpl.decodeUncertainty(data[12]);
    }

    public int getInnerRadius() {
    	byte[] data=getData();
        if (this.getTypeOfShape() != TypeOfShape.EllipsoidArc || data == null || data.length != 13)
            return 0;

        int i1 = ((data[7] & 0xFF) << 8) + (data[8] & 0xFF);
        return i1;
    }

    public double getUncertaintyRadius() {
    	byte[] data=getData();
        if (this.getTypeOfShape() != TypeOfShape.EllipsoidArc || data == null || data.length != 13)
            return 0;

        return GeographicalInformationImpl.decodeUncertainty(data[9]);
    }

    public double getOffsetAngle() {
    	byte[] data=getData();
        if (this.getTypeOfShape() != TypeOfShape.EllipsoidArc || data == null || data.length != 13)
            return 0;

        return (data[10] & 0xFF) * 2;
    }

    public double getIncludedAngle() {
    	byte[] data=getData();
        if (this.getTypeOfShape() != TypeOfShape.EllipsoidArc || data == null || data.length != 13)
            return 0;

        return (data[11] & 0xFF) * 2;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtGeographicalInformation [");

        sb.append("TypeOfShape=");
        sb.append(this.getTypeOfShape());

        sb.append(", Latitude=");
        sb.append(this.getLatitude());

        sb.append(", Longitude=");
        sb.append(this.getLongitude());

        if (this.getTypeOfShape() == TypeOfShape.EllipsoidPointWithUncertaintyCircle) {
            sb.append(", Uncertainty=");
            sb.append(this.getUncertainty());
        }

        if (this.getTypeOfShape() == TypeOfShape.EllipsoidPointWithUncertaintyEllipse
                || this.getTypeOfShape() == TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid) {
            sb.append(", UncertaintySemiMajorAxis=");
            sb.append(this.getUncertaintySemiMajorAxis());

            sb.append(", UncertaintySemiMinorAxis=");
            sb.append(this.getUncertaintySemiMinorAxis());

            sb.append(", AngleOfMajorAxis=");
            sb.append(this.getAngleOfMajorAxis());
        }

        if (this.getTypeOfShape() == TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid) {
            sb.append(", Altitude=");
            sb.append(this.getAltitude());

            sb.append(", UncertaintyAltitude=");
            sb.append(this.getUncertaintyAltitude());
        }

        if (this.getTypeOfShape() == TypeOfShape.EllipsoidArc) {
            sb.append(", InnerRadius=");
            sb.append(this.getInnerRadius());

            sb.append(", UncertaintyRadius=");
            sb.append(this.getUncertaintyRadius());

            sb.append(", OffsetAngle=");
            sb.append(this.getOffsetAngle());

            sb.append(", IncludedAngle=");
            sb.append(this.getIncludedAngle());
        }

        if (this.getTypeOfShape() == TypeOfShape.EllipsoidPointWithUncertaintyEllipse
                || this.getTypeOfShape() == TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid
                || this.getTypeOfShape() == TypeOfShape.EllipsoidArc) {
            sb.append(", Confidence=");
            sb.append(this.getConfidence());
        }

        sb.append("]");

        return sb.toString();
    }
}
