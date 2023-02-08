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
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public class ExtGeographicalInformationImpl extends ASNOctetString implements ExtGeographicalInformation {
	public ExtGeographicalInformationImpl() {
		super("ExtGeographicalInformation",1,20,false);
    }

	public ExtGeographicalInformationImpl(TypeOfShape typeOfShape, double latitude, double longitude, double uncertainty,
            double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis, double angleOfMajorAxis, int confidence,
            int altitude, double uncertaintyAltitude, int innerRadius, double uncertaintyRadius, double offsetAngle,
            double includedAngle) throws MAPException {     
	    super(initData(typeOfShape, latitude, longitude, uncertainty, uncertaintySemiMajorAxis, uncertaintySemiMinorAxis,
                angleOfMajorAxis, confidence, altitude, uncertaintyAltitude, innerRadius, uncertaintyRadius, offsetAngle,
                includedAngle),"ExtGeographicalInformation",1,20,false);
    }

    protected static ByteBuf initData(TypeOfShape typeOfShape, double latitude, double longitude, double uncertainty,
            double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis, double angleOfMajorAxis, int confidence,
            int altitude, double uncertaintyAltitude, int innerRadius, double uncertaintyRadius, double offsetAngle,
            double includedAngle) throws MAPException {

        if (typeOfShape == null) {
            throw new MAPException("typeOfShape parameter is null");
        }

        ByteBuf data;
        switch (typeOfShape) {
            case EllipsoidPointWithUncertaintyCircle:
                data=initData(8, typeOfShape, latitude, longitude);
                data.writeByte((byte) GeographicalInformationImpl.encodeUncertainty(uncertainty));
                break;

            case EllipsoidPointWithUncertaintyEllipse:
            	data=initData(11, typeOfShape, latitude, longitude);
            	data.writeByte((byte) GeographicalInformationImpl.encodeUncertainty(uncertaintySemiMajorAxis));
            	data.writeByte((byte) GeographicalInformationImpl.encodeUncertainty(uncertaintySemiMinorAxis));
            	data.writeByte((byte) (angleOfMajorAxis / 2));
            	data.writeByte((byte) confidence);
                break;

            case EllipsoidPointWithAltitudeAndUncertaintyEllipsoid:
            	data=initData(14, typeOfShape, latitude, longitude);

                boolean negativeSign = false;
                if (altitude < 0) {
                    negativeSign = true;
                    altitude = -altitude;
                }
                if (altitude > 0x7FFF)
                    altitude = 0x7FFF;
                if (negativeSign)
                    altitude |= 0x8000;
                data.writeByte((byte) ((altitude & 0xFF00) >> 8));
                data.writeByte((byte) (altitude & 0xFF));

                data.writeByte((byte) GeographicalInformationImpl.encodeUncertainty(uncertaintySemiMajorAxis));
                data.writeByte((byte) GeographicalInformationImpl.encodeUncertainty(uncertaintySemiMinorAxis));
                data.writeByte((byte) (angleOfMajorAxis / 2));
                data.writeByte((byte) GeographicalInformationImpl.encodeUncertainty(uncertaintyAltitude));
                data.writeByte((byte) confidence);
                break;

            case EllipsoidArc:
            	data=initData(13, typeOfShape, latitude, longitude);

                if (innerRadius > 0x7FFF)
                    innerRadius = 0x7FFF;
                
                data.writeByte((byte) ((innerRadius & 0xFF00) >> 8));
                data.writeByte((byte) (innerRadius & 0xFF));
                data.writeByte((byte) GeographicalInformationImpl.encodeUncertainty(uncertaintyRadius));
                data.writeByte((byte) (offsetAngle / 2));
                data.writeByte((byte) (includedAngle / 2));
                data.writeByte((byte) confidence);

                break;

            case EllipsoidPoint:
            	data=initData(7, typeOfShape, latitude, longitude);
                break;

            default:
                throw new MAPException("typeOfShape parameter has bad value");                
        }
        
        return data;
    }

    private static ByteBuf initData(int len, TypeOfShape typeOfShape, double latitude, double longitude) {
        ByteBuf buf = Unpooled.buffer(len);
        buf.writeByte((byte) (typeOfShape.getCode() << 4));
        GeographicalInformationImpl.encodeLatitude(buf, latitude);
        GeographicalInformationImpl.encodeLongitude(buf, longitude);        
        return buf;
    }

    public TypeOfShape getTypeOfShape() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() < 1)
            return null;

        return TypeOfShape.getInstance((value.readByte() & 0xFF) >> 4);
    }

    public double getLatitude() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() < 7)
            return 0;

        value.skipBytes(1);
        return GeographicalInformationImpl.decodeLatitude(value);
    }

    public double getLongitude() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() < 7)
            return 0;

        value.skipBytes(4);
        return GeographicalInformationImpl.decodeLongitude(value);
    }

    public double getUncertainty() {
    	ByteBuf value=getValue();
        if (this.getTypeOfShape() != TypeOfShape.EllipsoidPointWithUncertaintyCircle || value == null
                || value.readableBytes() != 8)
            return 0;

        value.skipBytes(7);
        return GeographicalInformationImpl.decodeUncertainty(value.readByte());
    }

    public double getUncertaintySemiMajorAxis() {
    	ByteBuf value=getValue();
        TypeOfShape typeOfShape = this.getTypeOfShape();
        if (typeOfShape != null) {
            switch (typeOfShape) {
                case EllipsoidPointWithUncertaintyEllipse:
                    if (value == null || value.readableBytes() != 11)
                        return 0;
                    
                    value.skipBytes(7);
                    return GeographicalInformationImpl.decodeUncertainty(value.readByte());
                case EllipsoidPointWithAltitudeAndUncertaintyEllipsoid:
                    if (value == null || value.readableBytes() != 14)
                        return 0;
                    
                    value.skipBytes(9);
                    return GeographicalInformationImpl.decodeUncertainty(value.readByte());
				default:
					break;
            }
        }

        return 0;
    }

    public double getUncertaintySemiMinorAxis() {
    	ByteBuf value=getValue();
        TypeOfShape typeOfShape = this.getTypeOfShape();
        if (typeOfShape != null) {
            switch (typeOfShape) {
                case EllipsoidPointWithUncertaintyEllipse:
                    if (value == null || value.readableBytes() != 11)
                        return 0;
                    
                    value.skipBytes(8);
                    return GeographicalInformationImpl.decodeUncertainty(value.readByte());
                case EllipsoidPointWithAltitudeAndUncertaintyEllipsoid:
                    if (value == null || value.readableBytes() != 14)
                        return 0;
                    
                    value.skipBytes(10);
                    return GeographicalInformationImpl.decodeUncertainty(value.readByte());
				default:
					break;
            }
        }

        return 0;
    }

    public double getAngleOfMajorAxis() {
    	ByteBuf value=getValue();
        TypeOfShape typeOfShape = this.getTypeOfShape();
        if (typeOfShape != null) {
            switch (typeOfShape) {
                case EllipsoidPointWithUncertaintyEllipse:
                    if (value == null || value.readableBytes() != 11)
                        return 0;
                    
                    value.skipBytes(9);
                    return (value.readByte() & 0xFF) * 2;
                case EllipsoidPointWithAltitudeAndUncertaintyEllipsoid:
                    if (value == null || value.readableBytes() != 14)
                        return 0;
                    
                    value.skipBytes(11);
                    return (value.readByte() & 0xFF) * 2;
				default:
					break;
            }
        }

        return 0;
    }

    public int getConfidence() {
    	ByteBuf value=getValue();
        TypeOfShape typeOfShape = this.getTypeOfShape();
        if (typeOfShape != null) {
            switch (typeOfShape) {
                case EllipsoidPointWithUncertaintyEllipse:
                    if (value == null || value.readableBytes() != 11)
                        return 0;
                    value.skipBytes(10);
                    return value.readByte();
                case EllipsoidPointWithAltitudeAndUncertaintyEllipsoid:
                    if (value == null || value.readableBytes() != 14)
                        return 0;
                    value.skipBytes(13);
                    return value.readByte();
                case EllipsoidArc:
                    if (value == null || value.readableBytes() != 13)
                        return 0;
                    
                    value.skipBytes(12);
                    return value.readByte();
				default:
					break;
            }
        }

        return 0;
    }

    public int getAltitude() {
    	ByteBuf value=getValue();
        if (this.getTypeOfShape() != TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid || value == null
                || value.readableBytes() != 14)
            return 0;

        value.skipBytes(7);
        int i1 = ((value.readByte() & 0xFF) << 8) + (value.readByte() & 0xFF);
        int sign = 1;
        if ((i1 & 0x8000) != 0) {
            sign = -1;
            i1 = i1 & 0x7FFF;
        }
        return i1 * sign;
    }

    public double getUncertaintyAltitude() {
    	ByteBuf value=getValue();
        if (this.getTypeOfShape() != TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid || value == null
                || value.readableBytes() != 14)
            return 0;

        value.skipBytes(12);
        return GeographicalInformationImpl.decodeUncertainty(value.readByte());
    }

    public int getInnerRadius() {
    	ByteBuf value=getValue();
        if (this.getTypeOfShape() != TypeOfShape.EllipsoidArc || value == null || value.readableBytes() != 13)
            return 0;

        value.skipBytes(7);
        int i1 = ((value.readByte() & 0xFF) << 8) + (value.readByte() & 0xFF);
        return i1;
    }

    public double getUncertaintyRadius() {
    	ByteBuf value=getValue();
        if (this.getTypeOfShape() != TypeOfShape.EllipsoidArc || value == null || value.readableBytes() != 13)
            return 0;

        value.skipBytes(9);
        return GeographicalInformationImpl.decodeUncertainty(value.readByte());
    }

    public double getOffsetAngle() {
    	ByteBuf value=getValue();
        if (this.getTypeOfShape() != TypeOfShape.EllipsoidArc || value == null || value.readableBytes() != 13)
            return 0;

        value.skipBytes(10);
        return (value.readByte() & 0xFF) * 2;
    }

    public double getIncludedAngle() {
    	ByteBuf value=getValue();
        if (this.getTypeOfShape() != TypeOfShape.EllipsoidArc || value == null || value.readableBytes() != 13)
            return 0;

        value.skipBytes(11);
        return (value.readByte() & 0xFF) * 2;
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
