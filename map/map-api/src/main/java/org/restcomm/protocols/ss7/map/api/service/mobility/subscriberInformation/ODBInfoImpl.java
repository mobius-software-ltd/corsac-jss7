/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBDataImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 * Created by vsubbotin on 25/05/16.
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ODBInfoImpl {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=0)
	private ODBDataImpl odbData;
	
    private ASNNull notificationToCSE;
    private MAPExtensionContainerImpl extensionContainer;

    public ODBInfoImpl() {
    }

    public ODBInfoImpl(ODBDataImpl odbData, boolean notificationToCSE, MAPExtensionContainerImpl extensionContainer) {
        this.odbData = odbData;
        
        if(notificationToCSE)
        	this.notificationToCSE = new ASNNull();
        
        this.extensionContainer = extensionContainer;
    }

    public ODBDataImpl getOdbData() {
        return this.odbData;
    }

    public boolean getNotificationToCSE() {
        return this.notificationToCSE!=null;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ODBInfo [");

        if (this.odbData != null) {
            sb.append("odbData=");
            sb.append(this.odbData);
        }
        if (this.notificationToCSE!=null) {
            sb.append(", notificationToCSE");
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }

        sb.append("]");
        return sb.toString();
    }
}
