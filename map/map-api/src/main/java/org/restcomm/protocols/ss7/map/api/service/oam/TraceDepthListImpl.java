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

package org.restcomm.protocols.ss7.map.api.service.oam;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class TraceDepthListImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNTraceDepthImpl mscSTraceDepth;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNTraceDepthImpl mgwTraceDepth;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNTraceDepthImpl sgsnTraceDepth;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNTraceDepthImpl ggsnTraceDepth;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private ASNTraceDepthImpl rncTraceDepth;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private ASNTraceDepthImpl bmscTraceDepth;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private ASNTraceDepthImpl mmeTraceDepth;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private ASNTraceDepthImpl sgwTraceDepth;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private ASNTraceDepthImpl pgwTraceDepth;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1)
    private ASNTraceDepthImpl enbTraceDepth;

    public TraceDepthListImpl() {
    }

    public TraceDepthListImpl(TraceDepth mscSTraceDepth, TraceDepth mgwTraceDepth, TraceDepth sgsnTraceDepth, TraceDepth ggsnTraceDepth,
            TraceDepth rncTraceDepth, TraceDepth bmscTraceDepth, TraceDepth mmeTraceDepth, TraceDepth sgwTraceDepth, TraceDepth pgwTraceDepth,
            TraceDepth enbTraceDepth) {

    	if(mscSTraceDepth!=null) {
    		this.mscSTraceDepth = new ASNTraceDepthImpl();
    		this.mscSTraceDepth.setType(mscSTraceDepth);
    	}
    	
    	if(mgwTraceDepth!=null) {
    		this.mgwTraceDepth = new ASNTraceDepthImpl();
    		this.mgwTraceDepth.setType(mgwTraceDepth);
    	}
    	
    	if(sgsnTraceDepth!=null) {
    		this.sgsnTraceDepth = new ASNTraceDepthImpl();
    		this.sgsnTraceDepth.setType(sgsnTraceDepth);
    	}
    	
    	if(ggsnTraceDepth!=null) {
    		this.ggsnTraceDepth = new ASNTraceDepthImpl();
    		this.ggsnTraceDepth.setType(ggsnTraceDepth);
    	}
    	
    	if(rncTraceDepth!=null) {
    		this.rncTraceDepth = new ASNTraceDepthImpl();
    		this.rncTraceDepth.setType(rncTraceDepth);
    	}
    	
    	if(bmscTraceDepth!=null) {
    		this.bmscTraceDepth = new ASNTraceDepthImpl();
    		this.bmscTraceDepth.setType(bmscTraceDepth);
    	}
    	
    	if(mmeTraceDepth!=null) {
    		this.mmeTraceDepth = new ASNTraceDepthImpl();
    		this.mmeTraceDepth.setType(mmeTraceDepth);
    	}
    	
    	if(sgwTraceDepth!=null) {
    		this.sgwTraceDepth = new ASNTraceDepthImpl();
    		this.sgwTraceDepth.setType(sgwTraceDepth);
    	}
    	
    	if(pgwTraceDepth!=null) {
    		this.pgwTraceDepth = new ASNTraceDepthImpl();
    		this.pgwTraceDepth.setType(pgwTraceDepth);
    	} 
    	
    	if(enbTraceDepth!=null) {
    		this.enbTraceDepth = new ASNTraceDepthImpl();
    		this.enbTraceDepth.setType(enbTraceDepth);
    	} 
    }

    public TraceDepth getMscSTraceDepth() {
    	if(mscSTraceDepth==null)
    		return null;
    	
        return mscSTraceDepth.getType();
    }

    public TraceDepth getMgwTraceDepth() {
    	if(mgwTraceDepth==null)
    		return null;
    	
        return mgwTraceDepth.getType();
    }

    public TraceDepth getSgsnTraceDepth() {
    	if(sgsnTraceDepth==null)
    		return null;
    	
        return sgsnTraceDepth.getType();
    }

    public TraceDepth getGgsnTraceDepth() {
    	if(ggsnTraceDepth==null)
    		return null;
    	
        return ggsnTraceDepth.getType();
    }

    public TraceDepth getRncTraceDepth() {
    	if(rncTraceDepth==null)
    		return null;
    	
        return rncTraceDepth.getType();
    }

    public TraceDepth getBmscTraceDepth() {
    	if(bmscTraceDepth==null)
    		return null;
    	
        return bmscTraceDepth.getType();
    }

    public TraceDepth getMmeTraceDepth() {
    	if(mmeTraceDepth==null)
    		return null;
    	
        return mmeTraceDepth.getType();
    }

    public TraceDepth getSgwTraceDepth() {
    	if(sgwTraceDepth==null)
    		return null;
    	
        return sgwTraceDepth.getType();
    }

    public TraceDepth getPgwTraceDepth() {
    	if(pgwTraceDepth==null)
    		return null;
    	
        return pgwTraceDepth.getType();
    }

    public TraceDepth getEnbTraceDepth() {
    	if(enbTraceDepth==null)
    		return null;
    	
        return enbTraceDepth.getType();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TraceDepthList [");

        if (this.mscSTraceDepth != null) {
            sb.append("mscSTraceDepth=");
            sb.append(this.mscSTraceDepth.getType());
            sb.append(", ");
        }
        if (this.mgwTraceDepth != null) {
            sb.append("mgwTraceDepth=");
            sb.append(this.mgwTraceDepth.getType());
            sb.append(", ");
        }
        if (this.sgsnTraceDepth != null) {
            sb.append("sgsnTraceDepth=");
            sb.append(this.sgsnTraceDepth.getType());
            sb.append(", ");
        }
        if (this.ggsnTraceDepth != null) {
            sb.append("ggsnTraceDepth=");
            sb.append(this.ggsnTraceDepth.getType());
            sb.append(", ");
        }
        if (this.rncTraceDepth != null) {
            sb.append("rncTraceDepth=");
            sb.append(this.rncTraceDepth.getType());
            sb.append(", ");
        }
        if (this.bmscTraceDepth != null) {
            sb.append("bmscTraceDepth=");
            sb.append(this.bmscTraceDepth.getType());
            sb.append(", ");
        }
        if (this.mmeTraceDepth != null) {
            sb.append("mmeTraceDepth=");
            sb.append(this.mmeTraceDepth.getType());
            sb.append(", ");
        }
        if (this.sgwTraceDepth != null) {
            sb.append("sgwTraceDepth=");
            sb.append(this.sgwTraceDepth.getType());
            sb.append(", ");
        }
        if (this.pgwTraceDepth != null) {
            sb.append("pgwTraceDepth=");
            sb.append(this.pgwTraceDepth.getType());
            sb.append(", ");
        }
        if (this.enbTraceDepth != null) {
            sb.append("enbTraceDepth=");
            sb.append(this.enbTraceDepth.getType());
            sb.append(", ");
        }

        sb.append("]");
        return sb.toString();
    }
}