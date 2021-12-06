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

package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ReportSMDeliveryStatusResponseImplV3 extends SmsMessageImpl implements ReportSMDeliveryStatusResponse {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1,defaultImplementation = ISDNAddressStringImpl.class)
	private ISDNAddressString storedMSISDN;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;

    private long mapProtocolVersion;

    public ReportSMDeliveryStatusResponseImplV3() {
    	this.mapProtocolVersion=3;
    }
    
    public ReportSMDeliveryStatusResponseImplV3(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public ReportSMDeliveryStatusResponseImplV3(long mapProtocolVersion, ISDNAddressString storedMSISDN,
            MAPExtensionContainer extensionContainer) {
        this.mapProtocolVersion = mapProtocolVersion;
        this.storedMSISDN = storedMSISDN;
        this.extensionContainer = extensionContainer;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.reportSM_DeliveryStatus_Response;
    }

    public int getOperationCode() {
        return MAPOperationCode.reportSM_DeliveryStatus;
    }

    public ISDNAddressString getStoredMSISDN() {
        return this.storedMSISDN;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public long getMapProtocolVersion() {
        return this.mapProtocolVersion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ReportSMDeliveryStatusResponse [");

        if (this.getMAPDialog() != null) {
            sb.append("DialogId=").append(this.getMAPDialog().getLocalDialogId());
        }

        if (this.storedMSISDN != null) {
            sb.append(", storedMSISDN=");
            sb.append(this.storedMSISDN.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append(", mapProtocolVersion=");
        sb.append(this.mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }

}
