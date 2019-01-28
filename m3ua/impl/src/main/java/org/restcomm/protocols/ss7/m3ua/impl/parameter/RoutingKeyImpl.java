/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

    private ByteBuf buf = Unpooled.buffer(256);

    private byte[] value;

    public RoutingKeyImpl() {
        this.tag = Parameter.Routing_Key;
    }

    protected RoutingKeyImpl(byte[] value) {
        this.tag = Parameter.Routing_Key;
        this.value = value;

        this.decode(value);

        this.value = value;
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

        this.encode();
    }

    private void decode(byte[] data) {
        int pos = 0;
        ArrayList<DestinationPointCode> dpcList = new ArrayList<DestinationPointCode>();
        ArrayList<ServiceIndicators> serIndList = new ArrayList<ServiceIndicators>();
        ArrayList<OPCList> opcListList = new ArrayList<OPCList>();

        while (pos < data.length) {
            short tag = (short) ((data[pos] & 0xff) << 8 | (data[pos + 1] & 0xff));
            short len = (short) ((data[pos + 2] & 0xff) << 8 | (data[pos + 3] & 0xff));

            byte[] value = new byte[len - 4];

            System.arraycopy(data, pos + 4, value, 0, value.length);
            pos += len;
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
            pos += (pos % 4);
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
        if (this.localRkId != null) {
            ((LocalRKIdentifierImpl) this.localRkId).write(buf);
        }

        if (this.rc != null) {
            ((RoutingContextImpl) rc).write(buf);
        }

        if (this.trafMdTy != null) {
            ((TrafficModeTypeImpl) trafMdTy).write(buf);
        }

        if (this.netApp != null) {
            ((NetworkAppearanceImpl) this.netApp).write(buf);
        }

        for (int i = 0; i < this.dpc.length; i++) {
            ((DestinationPointCodeImpl) this.dpc[i]).write(buf);

            if (this.servInds != null) {
                ((ServiceIndicatorsImpl) this.servInds[i]).write(buf);
            }

            if (this.opcList != null) {
                ((OPCListImpl) this.opcList[i]).write(buf);
            }
        }

        int length = buf.readableBytes();
        value = new byte[length];
        buf.getBytes(buf.readerIndex(), value);
    }

    @Override
    protected byte[] getValue() {
        return this.value;
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
