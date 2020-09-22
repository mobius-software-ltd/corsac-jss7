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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MNPInfoResImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private RouteingNumberImpl routeingNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private IMSIImpl imsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ISDNAddressStringImpl msisdn;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNNumberPortabilityStatusImpl numberPortabilityStatus;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;

    /**
     *
     */
    public MNPInfoResImpl() {
    }

    public MNPInfoResImpl(RouteingNumberImpl routeingNumber, IMSIImpl imsi, ISDNAddressStringImpl msisdn,
            NumberPortabilityStatus numberPortabilityStatus, MAPExtensionContainerImpl extensionContainer) {
        this.routeingNumber = routeingNumber;
        this.imsi = imsi;
        this.msisdn = msisdn;
        
        if(numberPortabilityStatus!=null) {
        	this.numberPortabilityStatus = new ASNNumberPortabilityStatusImpl();
        	this.numberPortabilityStatus.setType(numberPortabilityStatus);
        }
        
        this.extensionContainer = extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.MNPInfoRes #getRouteingNumber()
     */
    public RouteingNumberImpl getRouteingNumber() {
        return this.routeingNumber;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.MNPInfoRes #getIMSI()
     */
    public IMSIImpl getIMSI() {
        return this.imsi;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.MNPInfoRes #getMSISDN()
     */
    public ISDNAddressStringImpl getMSISDN() {
        return this.msisdn;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.MNPInfoRes #getNumberPortabilityStatus()
     */
    public NumberPortabilityStatus getNumberPortabilityStatus() {
    	if(this.numberPortabilityStatus==null)
    		return null;
    	
        return this.numberPortabilityStatus.getType();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.MNPInfoRes #getExtensionContainer()
     */
    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MNPInfoRes [");

        if (this.routeingNumber != null) {
            sb.append("routeingNumber=");
            sb.append(this.routeingNumber);
        }

        if (this.imsi != null) {
            sb.append(", imsi=");
            sb.append(this.imsi);
        }

        if (this.msisdn != null) {
            sb.append(", msisdn=");
            sb.append(this.msisdn);
        }

        if (this.numberPortabilityStatus != null) {
            sb.append(", numberPortabilityStatus=");
            sb.append(this.numberPortabilityStatus.getType());
        }

        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }

        sb.append("]");
        return sb.toString();
    }
}
