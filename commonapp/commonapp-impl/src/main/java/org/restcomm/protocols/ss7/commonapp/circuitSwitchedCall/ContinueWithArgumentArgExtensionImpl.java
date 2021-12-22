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

package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ContinueWithArgumentArgExtension;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.LegOrCallSegment;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author Povilas Jurna
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ContinueWithArgumentArgExtensionImpl implements ContinueWithArgumentArgExtension {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNNull suppressDCSI;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNNull suppressNCSI;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private ASNNull suppressOutgoingCallBarring;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1)
    private LegOrCallSegmentWrapperImpl legOrCallSegment;

    public ContinueWithArgumentArgExtensionImpl() {
    }

    public ContinueWithArgumentArgExtensionImpl(boolean suppressDCSI, boolean suppressNCSI,
            boolean suppressOutgoingCallBarring, LegOrCallSegment legOrCallSegment) {
        if(suppressDCSI)
        	this.suppressDCSI = new ASNNull();
        
        if(suppressNCSI)
        	this.suppressNCSI = new ASNNull();
        
        if(suppressOutgoingCallBarring)
        	this.suppressOutgoingCallBarring = new ASNNull();
        
        if(legOrCallSegment!=null) {
        	this.legOrCallSegment = new LegOrCallSegmentWrapperImpl(legOrCallSegment);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ContinueWithArgumentArgExtension [");

        if (suppressDCSI!=null) {
            sb.append("suppressDCSI, ");
        }
        if (suppressNCSI!=null) {
            sb.append("suppressNCSI, ");
        }
        if (suppressOutgoingCallBarring!=null) {
            sb.append("suppressOutgoingCallBarring, ");
        }
        if (this.legOrCallSegment != null && this.legOrCallSegment.getLegOrCallSegment()!=null) {
            sb.append("legOrCallSegment=");
            sb.append(this.legOrCallSegment.getLegOrCallSegment());
            sb.append(",");
        }

        sb.append("]");

        return sb.toString();
    }

    public boolean getSuppressDCsi() {
        return suppressDCSI!=null;
    }

    public boolean getSuppressNCsi() {
        return suppressNCSI!=null;
    }

    public boolean getSuppressOutgoingCallBarring() {
        return suppressOutgoingCallBarring!=null;
    }

    public LegOrCallSegment getLegOrCallSegment() {
    	if(legOrCallSegment==null)
    		return null;
    	
        return legOrCallSegment.getLegOrCallSegment();
    }

}
