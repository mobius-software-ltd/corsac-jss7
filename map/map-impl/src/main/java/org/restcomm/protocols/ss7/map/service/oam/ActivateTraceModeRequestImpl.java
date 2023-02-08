/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.MessageImpl;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeRequest_Mobility;
import org.restcomm.protocols.ss7.map.api.service.oam.ActivateTraceModeRequest_Oam;
import org.restcomm.protocols.ss7.map.api.service.oam.MAPDialogOam;
import org.restcomm.protocols.ss7.map.api.service.oam.MDTConfiguration;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceDepthList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceNETypeList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReference;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReference2;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceType;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ActivateTraceModeRequestImpl extends MessageImpl implements ActivateTraceModeRequest_Oam,ActivateTraceModeRequest_Mobility {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1,defaultImplementation = IMSIImpl.class)
    private IMSI imsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1, defaultImplementation = TraceReferenceImpl.class)
    private TraceReference traceReference;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = TraceTypeImpl.class)
    private TraceType traceType;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1, defaultImplementation = AddressStringImpl.class)
    private AddressString omcId;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1, defaultImplementation = TraceReference2Impl.class)
    private TraceReference2 traceReference2;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=true,index=-1, defaultImplementation = TraceDepthListImpl.class)
    private TraceDepthList traceDepthList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1, defaultImplementation = TraceNETypeListImpl.class)
    private TraceNETypeList traceNeTypeList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=true,index=-1, defaultImplementation = TraceInterfaceListImpl.class)
    private TraceInterfaceList traceInterfaceList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=true,index=-1, defaultImplementation = TraceEventListImpl.class)
    private TraceEventList traceEventList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=false,index=-1, defaultImplementation = GSNAddressImpl.class)
    private GSNAddress traceCollectionEntity;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=true,index=-1,defaultImplementation = MDTConfigurationImpl.class)
    private MDTConfiguration mdtConfiguration;

    public ActivateTraceModeRequestImpl() {
    }

    public ActivateTraceModeRequestImpl(IMSI imsi, TraceReference traceReference, TraceType traceType, AddressString omcId,
    		MAPExtensionContainer extensionContainer, TraceReference2 traceReference2, TraceDepthList traceDepthList, TraceNETypeList traceNeTypeList,
            TraceInterfaceList traceInterfaceList, TraceEventList traceEventList, GSNAddress traceCollectionEntity, MDTConfiguration mdtConfiguration) {

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
    public IMSI getImsi() {
        return imsi;
    }

    @Override
    public TraceReference getTraceReference() {
        return traceReference;
    }

    @Override
    public TraceType getTraceType() {
        return traceType;
    }

    @Override
    public AddressString getOmcId() {
        return omcId;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public TraceReference2 getTraceReference2() {
        return traceReference2;
    }

    @Override
    public TraceDepthList getTraceDepthList() {
        return traceDepthList;
    }

    @Override
    public TraceNETypeList getTraceNeTypeList() {
        return traceNeTypeList;
    }

    @Override
    public TraceInterfaceList getTraceInterfaceList() {
        return traceInterfaceList;
    }

    @Override
    public TraceEventList getTraceEventList() {
        return traceEventList;
    }

    @Override
    public GSNAddress getTraceCollectionEntity() {
        return traceCollectionEntity;
    }

    @Override
    public MDTConfiguration getMdtConfiguration() {
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
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(traceReference==null)
			throw new ASNParsingComponentException("trace reference should be set for activate trace mode request", ASNParsingComponentExceptionReason.MistypedRootParameter);

		if(traceType==null)
			throw new ASNParsingComponentException("trace type should be set for activate trace mode request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
