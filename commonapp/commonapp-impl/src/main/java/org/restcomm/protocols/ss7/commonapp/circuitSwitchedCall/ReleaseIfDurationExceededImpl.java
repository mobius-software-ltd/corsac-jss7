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

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBoolean;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 * @author alerant appngin
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ReleaseIfDurationExceededImpl {
	@ASNProperty(asnClass = ASNClass.UNIVERSAL,tag = 1,constructed = false,index = 0)
    private ASNBoolean tone;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = true,index = -1, defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

    public ReleaseIfDurationExceededImpl() {
    }

    public ReleaseIfDurationExceededImpl(Boolean tone, CAPINAPExtensions extensions) {
    	if(tone!=null)
    		this.tone = new ASNBoolean(tone);
    		
        this.extensions = extensions;        
    }

    public Boolean getTone() {
    	if(tone==null)
    		return null;
    	
        return tone.getValue();
    }

    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ReleaseIfDurationExceeded [");

        if (this.tone != null) {
            sb.append(", tone=");
            sb.append(this.tone.getValue());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
