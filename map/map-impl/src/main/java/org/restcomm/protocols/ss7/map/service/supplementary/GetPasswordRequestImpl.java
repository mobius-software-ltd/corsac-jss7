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

package org.restcomm.protocols.ss7.map.service.supplementary;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GetPasswordRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GuidanceInfo;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;

/**
*
* @author sergey vetyutnev
*
*/
@ASNWrappedTag
public class GetPasswordRequestImpl extends SupplementaryMessageImpl implements GetPasswordRequest {
	private static final long serialVersionUID = 1L;

	private ASNGuidanceInfoImpl guidanceInfo;

    public GetPasswordRequestImpl() {
    }

    public GetPasswordRequestImpl(GuidanceInfo guidanceInfo) {
    	if(guidanceInfo!=null) {
    		this.guidanceInfo = new ASNGuidanceInfoImpl();
    		this.guidanceInfo.setType(guidanceInfo);
    	}
    }


    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.getPasswordRequest_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.getPassword;
    }

    @Override
    public GuidanceInfo getGuidanceInfo() {
    	if(guidanceInfo==null)
    		return null;
    	
        return guidanceInfo.getType();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GetPasswordRequest [");

        if (this.guidanceInfo != null) {
            sb.append("guidanceInfo=");
            sb.append(this.guidanceInfo);
        }

        sb.append("]");

        return sb.toString();
    }

}
