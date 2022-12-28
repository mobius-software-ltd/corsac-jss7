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

package org.restcomm.protocols.ss7.map.api;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class MAPApplicationContext implements Serializable {
	private static final long serialVersionUID = 1L;

	private static long[] oidTemplate = new long[] { 0, 4, 0, 0, 1, 0, 0, 0 };

    private static ConcurrentHashMap<MAPApplicationContextName, ConcurrentHashMap<MAPApplicationContextVersion, MAPApplicationContext>> appContextCache = new ConcurrentHashMap<MAPApplicationContextName, ConcurrentHashMap<MAPApplicationContextVersion, MAPApplicationContext>>();

    private MAPApplicationContextName contextName;
    private MAPApplicationContextVersion contextVersion;

    // Same as oidTemplate
    private List<Long> res = Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 0L, 0L });

    private MAPApplicationContext(MAPApplicationContextName contextName, MAPApplicationContextVersion contextVersion) {
        this.contextName = contextName;
        this.contextVersion = contextVersion;

        this.res.set(6,(long)this.contextName.getApplicationContextCode());
        this.res.set(7,(long)this.contextVersion.getVersion());
    }

    public List<Long> getOID() {
        return res;
    }

    public static MAPApplicationContext fromValues(MAPApplicationContextName contextName, MAPApplicationContextVersion contextVersion) {
    	return new MAPApplicationContext(contextName, contextVersion);
    }
    
    public MAPApplicationContextName getApplicationContextName() {
        return this.contextName;
    }

    public MAPApplicationContextVersion getApplicationContextVersion() {
        return this.contextVersion;
    }

    private static MAPApplicationContext getMAPApplicationContext(MAPApplicationContextName contextName,
            MAPApplicationContextVersion contextVersion) {
        ConcurrentHashMap<MAPApplicationContextVersion, MAPApplicationContext> verCache = appContextCache.get(contextName);

        if (verCache == null) {
            verCache = new ConcurrentHashMap<MAPApplicationContextVersion, MAPApplicationContext>();
            appContextCache.put(contextName, verCache);
        }

        MAPApplicationContext mapApplicationContext = verCache.get(contextVersion);

        if (mapApplicationContext == null) {
            mapApplicationContext = new MAPApplicationContext(contextName, contextVersion);
            verCache.put(contextVersion, mapApplicationContext);
        }

        return mapApplicationContext;
    }

    public static MAPApplicationContext getInstance(MAPApplicationContextName contextName,
            MAPApplicationContextVersion contextVersion) {
        if (MAPApplicationContext.availableApplicationContextVersion(contextName, contextVersion.getVersion()))
            return getMAPApplicationContext(contextName, contextVersion);
        else
            return null;
    }

    public static MAPApplicationContext getInstance(List<Long> oid) {

        if (oid == null || oid.size() != oidTemplate.length)
            return null;
        for (int i1 = 0; i1 < oidTemplate.length - 2; i1++) {
            if (oid.get(i1) != oidTemplate[i1])
                return null;
        }

        MAPApplicationContextName contextName = MAPApplicationContextName.getInstance(oid.get(6).intValue());
        MAPApplicationContextVersion contextVersion = MAPApplicationContextVersion.getInstance(oid.get(7));

        if (contextName == null || contextVersion == null)
            return null;
        if (!MAPApplicationContext.availableApplicationContextVersion(contextName, oid.get(7).intValue()))
            return null;

        return getMAPApplicationContext(contextName, contextVersion);
    }

    /**
     * Return if the contextVersion is available for the contextName
     *
     * @param contextName
     * @param version
     * @return
     */
    public static boolean availableApplicationContextVersion(MAPApplicationContextName contextName, int contextVersion) {
        switch (contextName) {

        // -- Mobility Services
        // --- Location management services
            case networkLocUpContext:
            case locationCancellationContext:
            case gprsLocationUpdateContext:
                if (contextVersion >= 1 && contextVersion <= 3)
                    return true;
                else
                    return false;
            case interVlrInfoRetrievalContext:
            case msPurgingContext:
                if (contextVersion >= 2 && contextVersion <= 3)
                    return true;
                else
                    return false;
            case mmEventReportingContext:
                if (contextVersion == 3)
                    return true;
                else
                    return false;

                // --- Handover services
            case handoverControlContext:
                if (contextVersion >= 1 && contextVersion <= 3)
                    return true;
                else
                    return false;

                // --- Authentication management services
            case infoRetrievalContext:
                if (contextVersion >= 1 && contextVersion <= 3)
                    return true;
                else
                    return false;
            case authenticationFailureReportContext:
                if (contextVersion == 3)
                    return true;
                else
                    return false;

                // --- IMEI management services
            case equipmentMngtContext:
                if (contextVersion >= 1 && contextVersion <= 3)
                    return true;
                else
                    return false;

                // --- Subscriber management services
            case subscriberDataMngtContext:
                if (contextVersion >= 1 && contextVersion <= 3)
                    return true;
                else
                    return false;

                // --- Fault recovery services
            case resetContext:
                if (contextVersion >= 1 && contextVersion <= 2)
                    return true;
                else
                    return false;

                // --- Subscriber Information services
            case anyTimeEnquiryContext:
            case subscriberInfoEnquiryContext:
            case anyTimeInfoHandlingContext:
            case subscriberDataModificationNotificationContext:
                if (contextVersion == 3)
                    return true;
                else
                    return false;

                // -- oam
            case tracingContext:
                if (contextVersion >= 1 && contextVersion <= 3)
                    return true;
                else
                    return false;
            case imsiRetrievalContext:
                if (contextVersion == 2)
                    return true;
                else
                    return false;

                // -- Call Handling Services
            case locationInfoRetrievalContext:
            case roamingNumberEnquiryContext:
                if (contextVersion >= 1 && contextVersion <= 3)
                    return true;
                else
                    return false;
            case callControlTransferContext:
                if (contextVersion >= 3 && contextVersion <= 4)
                    return true;
                else
                    return false;
            case groupCallControlContext:
            case groupCallInfoRetrievalContext:
            case reportingContext:
            case istAlertingContext:
            case ServiceTerminationContext:
            case resourceManagementContext:
                if (contextVersion == 3)
                    return true;
                else
                    return false;

                // -- Supplementary services
            case networkFunctionalSsContext:
                if (contextVersion >= 1 && contextVersion <= 2)
                    return true;
                else
                    return false;
            case networkUnstructuredSsContext:
                if (contextVersion == 2)
                    return true;
                else
                    return false;
            case ssInvocationNotificationContext:
            case callCompletionContext:
                if (contextVersion == 3)
                    return true;
                else
                    return false;

                // -- short message service
            case shortMsgGatewayContext:
            case shortMsgMORelayContext:
            case shortMsgMTRelayContext:
            case mwdMngtContext:
                if (contextVersion >= 1 && contextVersion <= 3)
                    return true;
                else
                    return false;
            case shortMsgMTVgcsRelayContext:
                if (contextVersion == 3)
                    return true;
                else
                    return false;
            case shortMsgAlertContext:
                if (contextVersion >= 1 && contextVersion <= 2)
                    return true;
                else
                    return false;

                // -- Network-Requested PDP Context Activation services
            case gprsLocationInfoRetrievalContext:
                if (contextVersion >= 3 && contextVersion <= 4)
                    return true;
                else
                    return false;
            case failureReportContext:
            case gprsNotifyContext:
                if (contextVersion == 3)
                    return true;
                else
                    return false;

                // -- Location Service (lms)
            case locationSvcEnquiryContext:
            case locationSvcGatewayContext:
                if (contextVersion == 3)
                    return true;
                else
                    return false;
        }

        return false;
    }

    /**
     * Get ApplicationContext version. If oid is bad 0 will be received
     *
     * @param oid
     * @return
     */
    public static int getProtocolVersion(List<Long> oid) {
        if (oid == null || oid.size() != 8)
            return 0;
        else
            return oid.get(7).intValue();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contextName == null) ? 0 : contextName.hashCode());
        result = prime * result + ((contextVersion == null) ? 0 : contextVersion.hashCode());
        for(Long curr:res)
        	result = prime * result + curr.intValue();
        
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MAPApplicationContext other = (MAPApplicationContext) obj;
        if (contextName != other.contextName)
            return false;
        
        if (contextVersion != other.contextVersion)
        	return false;
       
        if(res==null) {
        	if(other.res!=null) {
        		return false;
        	}
        }
        else if (!res.equals(other.res))
        	return false;
        
        return true;
    }

    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();

        s.append("MAPApplicationContext [Name=");
        s.append(this.contextName.toString());
        s.append(", Version=");
        s.append(this.contextVersion.toString());
        s.append(", Oid=");
        for (long l : this.getOID()) {
            s.append(l).append(", ");
        }
        s.append("]");

        return s.toString();
    }

}