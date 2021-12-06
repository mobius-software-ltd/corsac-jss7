/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.primitives.IMEI;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClass;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MNPInfoRes;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberState;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.restcomm.protocols.ss7.map.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SubscriberInfoImpl implements SubscriberInfo {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1, defaultImplementation = LocationInformationImpl.class)
    private LocationInformation locationInformation = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private SubscriberStateWrapperImpl subscriberState = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1, defaultImplementation = LocationInformationGPRSImpl.class)
    private LocationInformationGPRS locationInformationGPRS = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1)
    private PSSubscriberStateWrapperImpl psSubscriberState = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1, defaultImplementation = IMEIImpl.class)
    private IMEI imei = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1, defaultImplementation = MSClassmark2Impl.class)
    private MSClassmark2 msClassmark2 = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=true,index=-1, defaultImplementation = GPRSMSClassImpl.class)
    private GPRSMSClass gprsMSClass = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=true,index=-1, defaultImplementation = MNPInfoResImpl.class)
    private MNPInfoRes mnpInfoRes = null;

    public SubscriberInfoImpl() {
    }

    public SubscriberInfoImpl(LocationInformation locationInformation, SubscriberState subscriberState,
            MAPExtensionContainer extensionContainer, LocationInformationGPRS locationInformationGPRS, PSSubscriberState psSubscriberState,
            IMEI imei, MSClassmark2 msClassmark2, GPRSMSClass gprsMSClass, MNPInfoRes mnpInfoRes) {
        this.locationInformation = locationInformation;
        
        if(subscriberState!=null)
        	this.subscriberState = new SubscriberStateWrapperImpl(subscriberState);
        
        this.extensionContainer = extensionContainer;
        this.locationInformationGPRS = locationInformationGPRS;
        
        if(psSubscriberState!=null)
        	this.psSubscriberState = new PSSubscriberStateWrapperImpl(psSubscriberState);
        
        this.imei = imei;
        this.msClassmark2 = msClassmark2;
        this.gprsMSClass = gprsMSClass;
        this.mnpInfoRes = mnpInfoRes;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * SubscriberInfo#getLocationInformation()
     */
    public LocationInformation getLocationInformation() {
        return this.locationInformation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * SubscriberInfo#getSubscriberState()
     */
    public SubscriberState getSubscriberState() {
    	if(this.subscriberState==null)
    		return null;
    	
        return this.subscriberState.getSubscriberState();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * SubscriberInfo#getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * SubscriberInfo#getLocationInformationGPRS()
     */
    public LocationInformationGPRS getLocationInformationGPRS() {
        return this.locationInformationGPRS;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * SubscriberInfo#getPSSubscriberState()
     */
    public PSSubscriberState getPSSubscriberState() {
    	if(this.psSubscriberState==null)
    		return null;
    	
        return this.psSubscriberState.getPSSubscriberState();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * SubscriberInfo#getIMEI()
     */
    public IMEI getIMEI() {
        return this.imei;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * SubscriberInfo#getMSClassmark2()
     */
    public MSClassmark2 getMSClassmark2() {
        return this.msClassmark2;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * SubscriberInfo#getGPRSMSClass()
     */
    public GPRSMSClass getGPRSMSClass() {
        return this.gprsMSClass;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * SubscriberInfo#getMNPInfoRes()
     */
    public MNPInfoRes getMNPInfoRes() {
        return this.mnpInfoRes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SubscriberInfo [");

        if (this.locationInformation != null) {
            sb.append(", locationInformation=");
            sb.append(this.locationInformation);
        }
        if (this.subscriberState != null) {
            sb.append(", subscriberState=");
            sb.append(this.subscriberState);
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (this.locationInformationGPRS != null) {
            sb.append(", locationInformationGPRS=");
            sb.append(this.locationInformationGPRS);
        }
        if (this.psSubscriberState != null) {
            sb.append(", psSubscriberState=");
            sb.append(this.psSubscriberState);
        }
        if (this.imei != null) {
            sb.append(", imei=");
            sb.append(this.imei);
        }
        if (this.msClassmark2 != null) {
            sb.append(", msClassmark2=");
            sb.append(this.msClassmark2);
        }
        if (this.gprsMSClass != null) {
            sb.append(", gprsMSClass=");
            sb.append(this.gprsMSClass);
        }
        if (this.mnpInfoRes != null) {
            sb.append(", mnpInfoRes=");
            sb.append(this.mnpInfoRes);
        }

        sb.append("]");
        return sb.toString();
    }
}
