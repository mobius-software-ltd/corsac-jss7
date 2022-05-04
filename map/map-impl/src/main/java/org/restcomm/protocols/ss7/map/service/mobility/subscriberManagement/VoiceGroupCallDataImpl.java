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
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalSubscriptions;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GroupId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LongGroupId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceGroupCallData;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class VoiceGroupCallDataImpl implements VoiceGroupCallData {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0,defaultImplementation = GroupIdImpl.class)
	private GroupId groupId;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=3,constructed=false,index=-1,defaultImplementation = AdditionalSubscriptionsImpl.class)
	private AdditionalSubscriptions additionalSubscriptions;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1,defaultImplementation = AdditionalInfoImpl.class)
    private AdditionalInfo additionalInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1,defaultImplementation = LongGroupIdImpl.class)
    private LongGroupId longGroupId;

    public VoiceGroupCallDataImpl() {        
    }

    public VoiceGroupCallDataImpl(GroupId groupId, MAPExtensionContainer extensionContainer,
            AdditionalSubscriptions additionalSubscriptions, AdditionalInfo additionalInfo, LongGroupId longGroupId) {
        this.groupId = groupId;
        this.extensionContainer = extensionContainer;
        this.additionalSubscriptions = additionalSubscriptions;
        this.additionalInfo = additionalInfo;
        this.longGroupId = longGroupId;
        if(this.longGroupId!=null)
        	this.groupId=new GroupIdImpl("");
    }

    public GroupId getGroupId() {
        return this.groupId;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public AdditionalSubscriptions getAdditionalSubscriptions() {
        return this.additionalSubscriptions;
    }

    public AdditionalInfo getAdditionalInfo() {
        return this.additionalInfo;
    }

    public LongGroupId getLongGroupId() {
        return this.longGroupId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VoiceGroupCallData [");

        if (this.groupId != null) {
            sb.append("groupId=");
            sb.append(this.groupId.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.additionalSubscriptions != null) {
            sb.append("additionalSubscriptions=");
            sb.append(this.additionalSubscriptions.toString());
            sb.append(", ");
        }

        if (this.additionalInfo != null) {
            sb.append("additionalInfo=");
            sb.append(this.additionalInfo.toString());
            sb.append(", ");
        }

        if (this.longGroupId != null) {
            sb.append("longGroupId=");
            sb.append(this.longGroupId.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(groupId==null)
			throw new ASNParsingComponentException("group ID should be set for voice group call data", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
