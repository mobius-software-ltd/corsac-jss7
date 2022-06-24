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

package org.restcomm.protocols.ss7.m3ua.impl.parameter;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.m3ua.parameter.DestinationPointCode;
import org.restcomm.protocols.ss7.m3ua.parameter.LocalRKIdentifier;
import org.restcomm.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.restcomm.protocols.ss7.m3ua.parameter.OPCList;
import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingKey;
import org.restcomm.protocols.ss7.m3ua.parameter.ServiceIndicators;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class RoutingKeyImpl extends ParameterImpl implements RoutingKey {
    private LocalRKIdentifier localRkId;
    private RoutingContext rc;
    private TrafficModeType trafMdTy;
    private NetworkAppearance netApp;
    private DestinationPointCode[] dpc;
    private ServiceIndicators[] servInds;
    private OPCList[] opcList;

    private ByteBuf value;

    public RoutingKeyImpl() {
        this.tag = Parameter.Routing_Key;
    }

    protected RoutingKeyImpl(ByteBuf value) {
        this.tag = Parameter.Routing_Key;
        this.value = value;

        this.decode(value);
    }

    protected RoutingKeyImpl(LocalRKIdentifier localRkId, RoutingContext rc, TrafficModeType trafMdTy,
            NetworkAppearance netApp, DestinationPointCode[] dpc, ServiceIndicators[] servInds, OPCList[] opcList) {
        this.tag = Parameter.Routing_Key;
        this.localRkId = localRkId;
        this.rc = rc;
        this.trafMdTy = trafMdTy;
        this.netApp = netApp;
        this.dpc = dpc;
        this.servInds = servInds;
        this.opcList = opcList;
    }

    private void decode(ByteBuf data) {
        ArrayList<DestinationPointCode> dpcList = new ArrayList<DestinationPointCode>();
        ArrayList<ServiceIndicators> serIndList = new ArrayList<ServiceIndicators>();
        ArrayList<OPCList> opcListList = new ArrayList<OPCList>();

        while (data.readableBytes()>0) {
            short tag = (short) ((data.readByte() & 0xff) << 8 | (data.readByte() & 0xff));
            short len = (short) ((data.readByte() & 0xff) << 8 | (data.readByte() & 0xff));

            ByteBuf value = data.slice(data.readerIndex(),len-4);
            data.skipBytes(len-4);
            // parameters.put(tag, factory.createParameter(tag, value));
            switch (tag) {
                case ParameterImpl.Local_Routing_Key_Identifier:
                    this.localRkId = new LocalRKIdentifierImpl(value);
                    break;

                case ParameterImpl.Routing_Context:
                    this.rc = new RoutingContextImpl(value);
                    break;

                case ParameterImpl.Traffic_Mode_Type:
                    this.trafMdTy = new TrafficModeTypeImpl(value);
                    break;

                case ParameterImpl.Network_Appearance:
                    this.netApp = new NetworkAppearanceImpl(value);
                    break;

                case ParameterImpl.Destination_Point_Code:
                    dpcList.add(new DestinationPointCodeImpl(value));
                    break;
                case ParameterImpl.Service_Indicators:
                    serIndList.add(new ServiceIndicatorsImpl(value));
                    break;
                case ParameterImpl.Originating_Point_Code_List:
                    opcListList.add(new OPCListImpl(value));
                    break;
            }

            // The Parameter Length does not include any padding octets. We have
            // to consider padding here
            if(len%4!=0)
            	data.skipBytes(len%4);            
        }// end of while

        this.dpc = new DestinationPointCode[dpcList.size()];
        this.dpc = dpcList.toArray(this.dpc);

        if (serIndList.size() > 0) {
            this.servInds = new ServiceIndicators[serIndList.size()];
            this.servInds = serIndList.toArray(this.servInds);
        }

        if (opcListList.size() > 0) {
            this.opcList = new OPCList[opcListList.size()];
            this.opcList = opcListList.toArray(this.opcList);
        }
    }

    private void encode() {
    	this.value=Unpooled.buffer(256);
    	
        if (this.localRkId != null) {
            ((LocalRKIdentifierImpl) this.localRkId).write(value);
        }

        if (this.rc != null) {
            ((RoutingContextImpl) rc).write(value);
        }

        if (this.trafMdTy != null) {
            ((TrafficModeTypeImpl) trafMdTy).write(value);
        }

        if (this.netApp != null) {
            ((NetworkAppearanceImpl) this.netApp).write(value);
        }

        for (int i = 0; i < this.dpc.length; i++) {
            ((DestinationPointCodeImpl) this.dpc[i]).write(value);

            if (this.servInds != null) {
                ((ServiceIndicatorsImpl) this.servInds[i]).write(value);
            }

            if (this.opcList != null) {
                ((OPCListImpl) this.opcList[i]).write(value);
            }
        }
    }

    @Override
    protected ByteBuf getValue() {
    	if(value==null)
    		encode();
    	
        return Unpooled.wrappedBuffer(this.value);
    }

    public DestinationPointCode[] getDestinationPointCodes() {
        return this.dpc;
    }

    public LocalRKIdentifier getLocalRKIdentifier() {
        return this.localRkId;
    }

    public NetworkAppearance getNetworkAppearance() {
        return this.netApp;
    }

    public OPCList[] getOPCLists() {
        return this.opcList;
    }

    public RoutingContext getRoutingContext() {
        return this.rc;
    }

    public ServiceIndicators[] getServiceIndicators() {
        return this.servInds;
    }

    public TrafficModeType getTrafficModeType() {
        return this.trafMdTy;
    }

    @Override
    public String toString() {
        StringBuilder tb = new StringBuilder();
        tb.append("RoutingKey(");
        if (localRkId != null) {
            tb.append(localRkId.toString());
        }

        if (rc != null) {
            tb.append(rc.toString());
        }

        if (trafMdTy != null) {
            tb.append(trafMdTy.toString());
        }

        if (netApp != null) {
            tb.append(netApp.toString());
        }

        if (dpc != null) {
            tb.append(dpc.toString());
        }

        if (servInds != null) {
            tb.append(servInds.toString());
        }

        if (opcList != null) {
            tb.append(opcList.toString());
        }
        tb.append(")");
        return tb.toString();
    }
}
