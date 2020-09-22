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

import org.restcomm.protocols.ss7.map.MessageImpl;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeRequest_Mobility;
import org.restcomm.protocols.ss7.map.api.service.oam.ActivateTraceModeRequest_Oam;
import org.restcomm.protocols.ss7.map.api.service.oam.MAPDialogOam;
import org.restcomm.protocols.ss7.map.api.service.oam.MDTConfigurationImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceDepthListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceInterfaceListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceNETypeListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReference2Impl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReferenceImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceTypeImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ActivateTraceModeRequestImpl extends MessageImpl implements ActivateTraceModeRequest_Oam,ActivateTraceModeRequest_Mobility {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private IMSIImpl imsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private TraceReferenceImpl traceReference;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private TraceTypeImpl traceType;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private AddressStringImpl omcId;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private TraceReference2Impl traceReference2;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=true,index=-1)
    private TraceDepthListImpl traceDepthList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private TraceNETypeListImpl traceNeTypeList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=true,index=-1)
    private TraceInterfaceListImpl traceInterfaceList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=true,index=-1)
    private TraceEventListImpl traceEventList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=false,index=-1)
    private GSNAddressImpl traceCollectionEntity;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=true,index=-1)
    private MDTConfigurationImpl mdtConfiguration;

    public ActivateTraceModeRequestImpl() {
    }

    public ActivateTraceModeRequestImpl(IMSIImpl imsi, TraceReferenceImpl traceReference, TraceTypeImpl traceType, AddressStringImpl omcId,
            MAPExtensionContainerImpl extensionContainer, TraceReference2Impl traceReference2, TraceDepthListImpl traceDepthList, TraceNETypeListImpl traceNeTypeList,
            TraceInterfaceListImpl traceInterfaceList, TraceEventListImpl traceEventList, GSNAddressImpl traceCollectionEntity, MDTConfigurationImpl mdtConfiguration) {

        this.imsi = imsi;
        this.traceReference = traceReference;
        this.traceType = traceType;
        this.omcId = omcId;
        this.extensionContainer = extensionContainer;
        this.traceReference2 = traceReference2;
        this.traceDepthList = traceDepthList;
        this.traceNeTypeList = traceNeTypeList;
        this.traceInterfaceList = traceInterfaceList;
        this.traceEventList = traceEventList;
        this.traceCollectionEntity = traceCollectionEntity;
        this.mdtConfiguration = mdtConfiguration;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.activateTraceMode_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.activateTraceMode;
    }

    public MAPDialogMobility getMAPDialog() {
        MAPDialog mapDialog = super.getMAPDialog();
        return (MAPDialogMobility) mapDialog;
    }

    public MAPDialogOam getOamMAPDialog() {
        MAPDialog mapDialog = super.getMAPDialog();
        return (MAPDialogOam) mapDialog;
    }

    @Override
    public IMSIImpl getImsi() {
        return imsi;
    }

    @Override
    public TraceReferenceImpl getTraceReference() {
        return traceReference;
    }

    @Override
    public TraceTypeImpl getTraceType() {
        return traceType;
    }

    @Override
    public AddressStringImpl getOmcId() {
        return omcId;
    }

    @Override
    public MAPExtensionContainerImpl getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public TraceReference2Impl getTraceReference2() {
        return traceReference2;
    }

    @Override
    public TraceDepthListImpl getTraceDepthList() {
        return traceDepthList;
    }

    @Override
    public TraceNETypeListImpl getTraceNeTypeList() {
        return traceNeTypeList;
    }

    @Override
    public TraceInterfaceListImpl getTraceInterfaceList() {
        return traceInterfaceList;
    }

    @Override
    public TraceEventListImpl getTraceEventList() {
        return traceEventList;
    }

    @Override
    public GSNAddressImpl getTraceCollectionEntity() {
        return traceCollectionEntity;
    }

    @Override
    public MDTConfigurationImpl getMdtConfiguration() {
        return mdtConfiguration;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ActivateTraceModeRequest [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(imsi);
            sb.append(", ");
        }
        if (this.traceReference != null) {
            sb.append("traceReference=");
            sb.append(traceReference);
            sb.append(", ");
        }
        if (this.traceType != null) {
            sb.append("traceType=");
            sb.append(traceType);
            sb.append(", ");
        }
        if (this.omcId != null) {
            sb.append("omcId=");
            sb.append(omcId);
            sb.append(", ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer);
            sb.append(", ");
        }
        if (this.traceReference2 != null) {
            sb.append("traceReference2=");
            sb.append(traceReference2);
            sb.append(", ");
        }
        if (this.traceDepthList != null) {
            sb.append("traceDepthList=");
            sb.append(traceDepthList);
            sb.append(", ");
        }
        if (this.traceNeTypeList != null) {
            sb.append("traceNeTypeList=");
            sb.append(traceNeTypeList);
            sb.append(", ");
        }
        if (this.traceInterfaceList != null) {
            sb.append("traceInterfaceList=");
            sb.append(traceInterfaceList);
            sb.append(", ");
        }
        if (this.traceEventList != null) {
            sb.append("traceEventList=");
            sb.append(traceEventList);
            sb.append(", ");
        }
        if (this.traceCollectionEntity != null) {
            sb.append("traceCollectionEntity=");
            sb.append(traceCollectionEntity);
            sb.append(", ");
        }
        if (this.mdtConfiguration != null) {
            sb.append("mdtConfiguration=");
            sb.append(mdtConfiguration);
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
