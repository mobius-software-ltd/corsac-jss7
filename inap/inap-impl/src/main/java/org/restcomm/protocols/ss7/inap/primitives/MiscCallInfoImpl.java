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

package org.restcomm.protocols.ss7.inap.primitives;

import org.restcomm.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.inap.api.primitives.MiscCallInfoDpAssignment;
import org.restcomm.protocols.ss7.inap.api.primitives.MiscCallInfoMessageType;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x04,constructed=true,lengthIndefinite=false)
public class MiscCallInfoImpl implements MiscCallInfo {
	private ASNMiscCallInfoMessageType messageType;
    private ASNMiscCallInfoDpAssignment dpAssignment;

    public MiscCallInfoImpl() {
    }

    public MiscCallInfoImpl(MiscCallInfoMessageType messageType, MiscCallInfoDpAssignment dpAssignment) {
    	if(messageType!=null) {
	        this.messageType = new ASNMiscCallInfoMessageType();
	        this.messageType.setType(messageType);
    	}
    	
    	if(dpAssignment!=null) {
	        this.dpAssignment = new ASNMiscCallInfoDpAssignment();
	        this.dpAssignment.setType(dpAssignment);
    	}
    }

    @Override
    public MiscCallInfoMessageType getMessageType() {
    	if(this.messageType==null)
    		return null;
    	
        return messageType.getType();
    }

    @Override
    public MiscCallInfoDpAssignment getDpAssignment() {
    	if(dpAssignment==null)
    		return null;
    	
        return dpAssignment.getType();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(" [");
        if (this.messageType != null) {
            sb.append("messageType=");
            sb.append(messageType);
        }
        if (this.dpAssignment != null) {
            sb.append(", dpAssignment=");
            sb.append(dpAssignment);
        }
        sb.append("]");

        return sb.toString();
    }
}
