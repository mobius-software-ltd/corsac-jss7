/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.api.EsiBcsm;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class DpSpecificInfoAltImpl {
	public static final int _ID_oServiceChangeSpecificInfo = 0;
    public static final int _ID_tServiceChangeSpecificInfo = 1;
    public static final int _ID_collectedInfoSpecificInfo = 2;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 0, constructed = true,index = -1)
    private OServiceChangeSpecificInfoImpl oServiceChangeSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 1, constructed = true,index = -1)
    private CollectedInfoSpecificInfoImpl collectedInfoSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 2, constructed = true,index = -1)
    private TServiceChangeSpecificInfoImpl tServiceChangeSpecificInfo;

    public DpSpecificInfoAltImpl() {
    }

    public DpSpecificInfoAltImpl(OServiceChangeSpecificInfoImpl oServiceChangeSpecificInfo, CollectedInfoSpecificInfoImpl collectedInfoSpecificInfo,
            TServiceChangeSpecificInfoImpl tServiceChangeSpecificInfo) {
        this.oServiceChangeSpecificInfo = oServiceChangeSpecificInfo;
        this.collectedInfoSpecificInfo = collectedInfoSpecificInfo;
        this.tServiceChangeSpecificInfo = tServiceChangeSpecificInfo;
    }

    public OServiceChangeSpecificInfoImpl getOServiceChangeSpecificInfo() {
        return oServiceChangeSpecificInfo;
    }

    public CollectedInfoSpecificInfoImpl getCollectedInfoSpecificInfo() {
        return collectedInfoSpecificInfo;
    }

    public TServiceChangeSpecificInfoImpl getTServiceChangeSpecificInfo() {
        return tServiceChangeSpecificInfo;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("DpSpecificInfoAlt [");

        if (this.oServiceChangeSpecificInfo != null) {
            sb.append("oServiceChangeSpecificInfo=");
            sb.append(oServiceChangeSpecificInfo);
            sb.append(", ");
        }
        if (this.tServiceChangeSpecificInfo != null) {
            sb.append("tServiceChangeSpecificInfo=");
            sb.append(tServiceChangeSpecificInfo);
            sb.append(", ");
        }
        if (this.collectedInfoSpecificInfo != null) {
            sb.append("collectedInfoSpecificInfo=");
            sb.append(collectedInfoSpecificInfo);
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }
}
