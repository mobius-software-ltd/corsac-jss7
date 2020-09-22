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

import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface MAPDialogOam extends MAPDialog {

    Long addActivateTraceModeRequest(IMSIImpl imsi, TraceReferenceImpl traceReference, TraceTypeImpl traceType, AddressStringImpl omcId,
    		MAPExtensionContainerImpl extensionContainer, TraceReference2Impl traceReference2, TraceDepthListImpl traceDepthList, TraceNETypeListImpl traceNeTypeList,
            TraceInterfaceListImpl traceInterfaceList, TraceEventListImpl traceEventList, GSNAddressImpl traceCollectionEntity, MDTConfigurationImpl mdtConfiguration)
            throws MAPException;

    Long addActivateTraceModeRequest(int customInvokeTimeout, IMSIImpl imsi, TraceReferenceImpl traceReference, TraceTypeImpl traceType, AddressStringImpl omcId,
    		MAPExtensionContainerImpl extensionContainer, TraceReference2Impl traceReference2, TraceDepthListImpl traceDepthList, TraceNETypeListImpl traceNeTypeList,
            TraceInterfaceListImpl traceInterfaceList, TraceEventListImpl traceEventList, GSNAddressImpl traceCollectionEntity, MDTConfigurationImpl mdtConfiguration)
            throws MAPException;

    void addActivateTraceModeResponse(long invokeId, MAPExtensionContainerImpl extensionContainer, boolean traceSupportIndicator) throws MAPException;

    Long addSendImsiRequest(ISDNAddressStringImpl msisdn) throws MAPException;

    Long addSendImsiRequest(int customInvokeTimeout, ISDNAddressStringImpl msisdn) throws MAPException;

    void addSendImsiResponse(long invokeId, IMSIImpl imsi) throws MAPException;

}
