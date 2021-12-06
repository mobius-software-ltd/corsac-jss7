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

package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.map.api.service.oam.BMSCEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.GGSNEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.MGWEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.MMEEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.MSCSEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.PGWEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.SGSNEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.SGWEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceEventList;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class TraceEventListImpl implements TraceEventList {
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = MSCSEventListImpl.class)
    private MSCSEventList mscSList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1, defaultImplementation = MGWEventListImpl.class)
    private MGWEventList mgwList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = SGSNEventListImpl.class)
    private SGSNEventList sgsnList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1, defaultImplementation = GGSNEventListImpl.class)
    private GGSNEventList ggsnList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = BMSCEventListImpl.class)
    private BMSCEventList bmscList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1, defaultImplementation = MMEEventListImpl.class)
    private MMEEventList mmeList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1, defaultImplementation = SGWEventListImpl.class)
    private SGWEventList sgwList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1, defaultImplementation = PGWEventListImpl.class)
    private PGWEventList pgwList;

    public TraceEventListImpl() {
    }

    public TraceEventListImpl(MSCSEventList mscSList, MGWEventList mgwList, SGSNEventList sgsnList, GGSNEventList ggsnList, BMSCEventList bmscList,
            MMEEventList mmeList, SGWEventList sgwList, PGWEventList pgwList) {
        this.mscSList = mscSList;
        this.mgwList = mgwList;
        this.sgsnList = sgsnList;
        this.ggsnList = ggsnList;
        this.bmscList = bmscList;
        this.mmeList = mmeList;
        this.sgwList = sgwList;
        this.pgwList = pgwList;
    }

    public MSCSEventList getMscSList() {
        return mscSList;
    }

    public MGWEventList getMgwList() {
        return mgwList;
    }

    public SGSNEventList getSgsnList() {
        return sgsnList;
    }

    public GGSNEventList getGgsnList() {
        return ggsnList;
    }

    public BMSCEventList getBmscList() {
        return bmscList;
    }

    public MMEEventList getMmeList() {
        return mmeList;
    }

    public SGWEventList getSgwList() {
        return sgwList;
    }

    public PGWEventList getPgwList() {
        return pgwList;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TraceEventList [");

        if (this.mscSList != null) {
            sb.append("mscSList=");
            sb.append(this.mscSList);
            sb.append(", ");
        }
        if (this.mgwList != null) {
            sb.append("mgwList=");
            sb.append(this.mgwList);
            sb.append(", ");
        }
        if (this.sgsnList != null) {
            sb.append("sgsnList=");
            sb.append(this.sgsnList);
            sb.append(", ");
        }
        if (this.ggsnList != null) {
            sb.append("ggsnList=");
            sb.append(this.ggsnList);
            sb.append(", ");
        }
        if (this.bmscList != null) {
            sb.append("bmscList=");
            sb.append(this.bmscList);
            sb.append(", ");
        }
        if (this.mmeList != null) {
            sb.append("mmeList=");
            sb.append(this.mmeList);
            sb.append(", ");
        }
        if (this.sgwList != null) {
            sb.append("sgwList=");
            sb.append(this.sgwList);
            sb.append(", ");
        }
        if (this.pgwList != null) {
            sb.append("pgwList=");
            sb.append(this.pgwList);
            sb.append(", ");
        }

        sb.append("]");
        return sb.toString();
    }

}
