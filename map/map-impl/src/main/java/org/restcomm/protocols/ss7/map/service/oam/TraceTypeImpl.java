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

package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.map.api.service.oam.BssRecordType;
import org.restcomm.protocols.ss7.map.api.service.oam.HlrRecordType;
import org.restcomm.protocols.ss7.map.api.service.oam.MscRecordType;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceType;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceTypeInvokingEvent;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class TraceTypeImpl extends ASNSingleByte implements TraceType {
	public TraceTypeImpl() {  
		super("TraceType",0,255,false);
    }

    public TraceTypeImpl(int data) {
    	super(data,"TraceType",0,255,false);
    }

    public TraceTypeImpl(BssRecordType bssRecordType, MscRecordType mscRecordType, TraceTypeInvokingEvent traceTypeInvokingEvent, boolean priorityIndication) {
        this(bssRecordType==null?3:bssRecordType.getCode(),mscRecordType==null?3:mscRecordType.getCode(),traceTypeInvokingEvent==null?0:traceTypeInvokingEvent.getCode(),priorityIndication);
    }

    public TraceTypeImpl(HlrRecordType hlrRecordType, TraceTypeInvokingEvent traceTypeInvokingEvent, boolean priorityIndication) {    	
    	this(0,hlrRecordType==null?3:hlrRecordType.getCode(),traceTypeInvokingEvent==null?0:traceTypeInvokingEvent.getCode(),priorityIndication);    	
    }
    
    private TraceTypeImpl(int bssrecordType,int mscRecordType,int traceType,boolean priorityIndication) {
    	super(((bssrecordType << 4) | (mscRecordType << 2) | traceType | (priorityIndication ? 0x80 : 0x00)),"TraceType",0,255,false);
    }


    public int getData() {
        return getValue();
    }

    public boolean isPriorityIndication() {
        if ((this.getData() & 0x80) != 0)
            return true;
        else
            return false;
    }

    public BssRecordType getBssRecordType() {
        int code = (this.getData() >> 4) & 0x03;
        return BssRecordType.getInstance(code);
    }

    public MscRecordType getMscRecordType() {
        int code = (this.getData() >> 2) & 0x03;
        return MscRecordType.getInstance(code);
    }

    public HlrRecordType getHlrRecordType() {
        int code = (this.getData() >> 2) & 0x03;
        return HlrRecordType.getInstance(code);
    }

    public TraceTypeInvokingEvent getTraceTypeInvokingEvent() {
        int code = this.getData() & 0x03;
        return TraceTypeInvokingEvent.getInstance(code);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TraceType");
        sb.append(" [");

        sb.append("bssRecordType=");
        sb.append(this.getBssRecordType());
        sb.append(", ");

        sb.append("mscRecordType=");
        sb.append(this.getMscRecordType());
        sb.append(", ");

        sb.append("hlrRecordType=");
        sb.append(this.getHlrRecordType());
        sb.append(", ");

        sb.append("traceTypeInvokingEvent=");
        sb.append(this.getTraceTypeInvokingEvent());
        sb.append(", ");

        if (isPriorityIndication()) {
            sb.append("priorityIndication, ");
        }

        sb.append("]");

        return sb.toString();
    }

}
