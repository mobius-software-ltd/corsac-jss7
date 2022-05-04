/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GroupId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LongGroupId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceBroadcastData;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class VoiceBroadcastDataImpl implements VoiceBroadcastData {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0,defaultImplementation = GroupIdImpl.class)
	private GroupId groupId;
	
    private ASNNull broadcastInitEntitlement;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1,defaultImplementation = LongGroupIdImpl.class)
    private LongGroupId longGroupId;

    public VoiceBroadcastDataImpl() {
    }

    public VoiceBroadcastDataImpl(GroupId groupId, boolean broadcastInitEntitlement, MAPExtensionContainer extensionContainer,
            LongGroupId longGroupId) {
        this.groupId = groupId;
        
        if(broadcastInitEntitlement)
        	this.broadcastInitEntitlement = new ASNNull();
        
        this.extensionContainer = extensionContainer;
        this.longGroupId = longGroupId;
        
        if(this.longGroupId!=null)
        	this.groupId=new GroupIdImpl("");
    }

    public GroupId getGroupId() {
        return this.groupId;
    }

    public boolean getBroadcastInitEntitlement() {
        return this.broadcastInitEntitlement!=null;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public LongGroupId getLongGroupId() {
        return this.longGroupId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VoiceBroadcastData [");

        if (this.groupId != null) {
            sb.append("groupId=");
            sb.append(this.groupId.toString());
            sb.append(", ");
        }

        if (this.broadcastInitEntitlement!=null) {
            sb.append("broadcastInitEntitlement, ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.longGroupId != null) {
            sb.append("longGroupId=");
            sb.append(this.longGroupId.toString());
            sb.append(" ");
        }

        sb.append("]");
        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(groupId==null)
			throw new ASNParsingComponentException("group ID should be set for voice broadcast data", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
