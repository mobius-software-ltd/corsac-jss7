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

import org.restcomm.protocols.ss7.map.api.service.oam.BMSCInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.ENBInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.GGSNInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.MGWInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.MMEInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.MSCSInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.PGWInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.RNCInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.SGSNInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.SGWInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceInterfaceList;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class TraceInterfaceListImpl implements TraceInterfaceList {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = MSCSInterfaceListImpl.class)
    private MSCSInterfaceList mscSList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1, defaultImplementation = MGWInterfaceListImpl.class)
    private MGWInterfaceList mgwList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = SGSNInterfaceListImpl.class)
    private SGSNInterfaceList sgsnList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1, defaultImplementation = GGSNInterfaceListImpl.class)
    private GGSNInterfaceList ggsnList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = RNCInterfaceListImpl.class)
    private RNCInterfaceList rncList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1, defaultImplementation = BMSCInterfaceListImpl.class)
    private BMSCInterfaceList bmscList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1, defaultImplementation = MMEInterfaceListImpl.class)
    private MMEInterfaceList mmeList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1 , defaultImplementation = SGWInterfaceListImpl.class)
    private SGWInterfaceList sgwList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1, defaultImplementation = PGWInterfaceListImpl.class)
    private PGWInterfaceList pgwList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1, defaultImplementation = ENBInterfaceListImpl.class)
    private ENBInterfaceList enbList;

    public TraceInterfaceListImpl() {
    }

    public TraceInterfaceListImpl(MSCSInterfaceList mscSList, MGWInterfaceList mgwList, SGSNInterfaceList sgsnList, GGSNInterfaceList ggsnList,
            RNCInterfaceList rncList, BMSCInterfaceList bmscList, MMEInterfaceList mmeList, SGWInterfaceList sgwList, PGWInterfaceList pgwList,
            ENBInterfaceList enbList) {
        this.mscSList = mscSList;
        this.mgwList = mgwList;
        this.sgsnList = sgsnList;
        this.ggsnList = ggsnList;
        this.rncList = rncList;
        this.bmscList = bmscList;
        this.mmeList = mmeList;
        this.sgwList = sgwList;
        this.pgwList = pgwList;
        this.enbList = enbList;
    }

    public MSCSInterfaceList getMscSList() {
        return this.mscSList;
    }

    public MGWInterfaceList getMgwList() {
        return mgwList;
    }

    public SGSNInterfaceList getSgsnList() {
        return sgsnList;
    }

    public GGSNInterfaceList getGgsnList() {
        return ggsnList;
    }

    public RNCInterfaceList getRncList() {
        return rncList;
    }

    public BMSCInterfaceList getBmscList() {
        return bmscList;
    }

    public MMEInterfaceList getMmeList() {
        return mmeList;
    }

    public SGWInterfaceList getSgwList() {
        return sgwList;
    }

    public PGWInterfaceList getPgwList() {
        return pgwList;
    }

    public ENBInterfaceList getEnbList() {
        return enbList;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TraceInterfaceList [");

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
        if (this.rncList != null) {
            sb.append("rncList=");
            sb.append(this.rncList);
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
        if (this.enbList != null) {
            sb.append("enbList=");
            sb.append(this.enbList);
            sb.append(", ");
        }

        sb.append("]");
        return sb.toString();
    }

}
