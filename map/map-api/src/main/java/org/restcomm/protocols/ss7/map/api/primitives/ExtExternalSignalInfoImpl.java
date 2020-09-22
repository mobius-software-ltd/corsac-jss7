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

package org.restcomm.protocols.ss7.map.api.primitives;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/*
 *
 * @author cristian veliscu
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ExtExternalSignalInfoImpl {
	private SignalInfoImpl signalInfo = null;
    private ASNExtProtocolIDImpl extProtocolId = null;
    private MAPExtensionContainerImpl extensionContainer = null;

    private static final String _PrimitiveName = "ExtExternalSignalInfo";

    public ExtExternalSignalInfoImpl() {
    }

    public ExtExternalSignalInfoImpl(SignalInfoImpl signalInfo, ExtProtocolId extProtocolId,
            MAPExtensionContainerImpl extensionContainer) {
        this.signalInfo = signalInfo;
        if(extProtocolId!=null) {
        	this.extProtocolId = new ASNExtProtocolIDImpl();
        	this.extProtocolId.setType(extProtocolId);
        }        
        this.extensionContainer = extensionContainer;
    }

    public SignalInfoImpl getSignalInfo() {
        return this.signalInfo;
    }

    public ExtProtocolId getExtProtocolId() {
    	if(this.extProtocolId==null)
    		return null;
    	
        return this.extProtocolId.getType();
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.signalInfo != null) {
            sb.append("signalInfo=[");
            sb.append(this.signalInfo);
            sb.append("], ");
        }

        if (this.extProtocolId != null) {
            sb.append("extProtocolId=[");
            sb.append(this.extProtocolId);
            sb.append("], ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=[");
            sb.append(this.extensionContainer);
            sb.append("]");
        }

        sb.append("]");
        return sb.toString();
    }
}