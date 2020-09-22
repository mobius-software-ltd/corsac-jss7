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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAIWrapperImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.ASNAccuracyFulfilmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.AddGeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.ExtGeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningDataImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.PositioningDataInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.ServingNodeAddressImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.ServingNodeAddressWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningDataImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityEstimateImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 *
 * @author amit bhayani
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ProvideSubscriberLocationResponseImpl extends LsmMessageImpl implements ProvideSubscriberLocationResponse {
	private static final long serialVersionUID = 1L;
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0)
    private ExtGeographicalInformationImpl locationEstimate;    
    
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNInteger ageOfLocationEstimate;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private AddGeographicalInformationImpl additionalLocationEstimate;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNNull deferredMTLRResponseIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private PositioningDataInformationImpl geranPositioningData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private UtranPositioningDataInfoImpl utranPositioningData;

    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=true,index=-1)
    private CellGlobalIdOrServiceAreaIdOrLAIWrapperImpl cellGlobalIdOrServiceAreaIdOrLAIWrapped;

    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private ASNNull saiPresent;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private ASNAccuracyFulfilmentIndicator accuracyFulfilmentIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1)
    private VelocityEstimateImpl velocityEstimate;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=false,index=-1)
    private ASNNull moLrShortCircuitIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=false,index=-1)
    private GeranGANSSpositioningDataImpl geranGANSSpositioningData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=false,index=-1)
    private UtranGANSSpositioningDataImpl utranGANSSpositioningData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=true,index=-1)
    private ServingNodeAddressWrapperImpl targetServingNodeForHandover;


     /**
     *
     */
    public ProvideSubscriberLocationResponseImpl() {
        super();
    }

    public ProvideSubscriberLocationResponseImpl(ExtGeographicalInformationImpl locationEstimate,
            PositioningDataInformationImpl geranPositioningData, UtranPositioningDataInfoImpl utranPositioningData,
            Integer ageOfLocationEstimate, AddGeographicalInformationImpl additionalLocationEstimate,
            MAPExtensionContainerImpl extensionContainer, boolean deferredMTLRResponseIndicator,
            CellGlobalIdOrServiceAreaIdOrLAIImpl cellGlobalIdOrServiceAreaIdOrLAI, boolean saiPresent,
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimateImpl velocityEstimate,
            boolean moLrShortCircuitIndicator, GeranGANSSpositioningDataImpl geranGANSSpositioningData,
            UtranGANSSpositioningDataImpl utranGANSSpositioningData, ServingNodeAddressImpl targetServingNodeForHandover) {
        super();

        this.locationEstimate = locationEstimate;
        this.geranPositioningData = geranPositioningData;
        this.utranPositioningData = utranPositioningData;
        
        if(ageOfLocationEstimate!=null) {
        	this.ageOfLocationEstimate = new ASNInteger();
        	this.ageOfLocationEstimate.setValue(ageOfLocationEstimate.longValue());
        }
        
        this.additionalLocationEstimate = additionalLocationEstimate;
        this.extensionContainer = extensionContainer;
        
        if(deferredMTLRResponseIndicator)
        	this.deferredMTLRResponseIndicator = new ASNNull();
        
        if(cellGlobalIdOrServiceAreaIdOrLAI!=null)
        	this.cellGlobalIdOrServiceAreaIdOrLAIWrapped = new CellGlobalIdOrServiceAreaIdOrLAIWrapperImpl(cellGlobalIdOrServiceAreaIdOrLAI);
        
        if(saiPresent)
        	this.saiPresent = new ASNNull();
        
        if(accuracyFulfilmentIndicator!=null) {
        	this.accuracyFulfilmentIndicator = new ASNAccuracyFulfilmentIndicator();
        	this.accuracyFulfilmentIndicator.setType(accuracyFulfilmentIndicator);
        }
        
        this.velocityEstimate = velocityEstimate;
        
        if(moLrShortCircuitIndicator)
        	this.moLrShortCircuitIndicator = new ASNNull();
        
        this.geranGANSSpositioningData = geranGANSSpositioningData;
        this.utranGANSSpositioningData = utranGANSSpositioningData;
        
        if(targetServingNodeForHandover!=null)
        	this.targetServingNodeForHandover = new ServingNodeAddressWrapperImpl(targetServingNodeForHandover);
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.provideSubscriberLocation_Response;
    }

    public int getOperationCode() {
        return MAPOperationCode.provideSubscriberLocation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationResponseIndication#getLocationEstimate()
     */
    public ExtGeographicalInformationImpl getLocationEstimate() {
        return this.locationEstimate;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.
     * ProvideSubscriberLocationResponseIndication#getGeranPositioningData()
     */
    public PositioningDataInformationImpl getGeranPositioningData() {
        return this.geranPositioningData;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.
     * ProvideSubscriberLocationResponseIndication#getUtranPositioningData()
     */
    public UtranPositioningDataInfoImpl getUtranPositioningData() {
        return this.utranPositioningData;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.
     * ProvideSubscriberLocationResponseIndication#getAgeOfLocationEstimate()
     */
    public Integer getAgeOfLocationEstimate() {
    	if(this.ageOfLocationEstimate==null)
    		return null;
    	
        return this.ageOfLocationEstimate.getValue().intValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationResponseIndication
     * #getAdditionalLocationEstimate()
     */
    public AddGeographicalInformationImpl getAdditionalLocationEstimate() {
        return this.additionalLocationEstimate;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationResponseIndication#getExtensionContainer()
     */
    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationResponseIndication
     * #getDeferredMTLRResponseIndicator()
     */
    public boolean getDeferredMTLRResponseIndicator() {
        return this.deferredMTLRResponseIndicator!=null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationResponseIndication
     * #getCellGlobalIdOrServiceAreaIdOrLAI()
     */
    public CellGlobalIdOrServiceAreaIdOrLAIImpl getCellIdOrSai() {
    	if(this.cellGlobalIdOrServiceAreaIdOrLAIWrapped!=null)
    		return this.cellGlobalIdOrServiceAreaIdOrLAIWrapped.getCellGlobalIdOrServiceAreaIdOrLAI();
    	
    	return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationResponseIndication#getSaiPresent()
     */
    public boolean getSaiPresent() {
        return this.saiPresent!=null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationResponseIndication
     * #getAccuracyFulfilmentIndicator()
     */
    public AccuracyFulfilmentIndicator getAccuracyFulfilmentIndicator() {
    	if(this.accuracyFulfilmentIndicator==null)
    		return null;
    	
        return this.accuracyFulfilmentIndicator.getType();
    }

    public VelocityEstimateImpl getVelocityEstimate() {
        return velocityEstimate;
    }

    public boolean getMoLrShortCircuitIndicator() {
        return moLrShortCircuitIndicator!=null;
    }

    public GeranGANSSpositioningDataImpl getGeranGANSSpositioningData() {
        return geranGANSSpositioningData;
    }

    public UtranGANSSpositioningDataImpl getUtranGANSSpositioningData() {
        return utranGANSSpositioningData;
    }

    public ServingNodeAddressImpl getTargetServingNodeForHandover() {
    	if(this.targetServingNodeForHandover==null)
    		return null;
    	
        return targetServingNodeForHandover.getServingNodeAddress();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProvideSubscriberLocationResponse [");

        if (this.locationEstimate != null) {
            sb.append("locationEstimate");
            sb.append(this.locationEstimate);
        }
        if (this.geranPositioningData != null) {
            sb.append(", geranPositioningData=");
            sb.append(this.geranPositioningData);
        }
        if (this.utranPositioningData != null) {
            sb.append(", utranPositioningData=");
            sb.append(this.utranPositioningData);
        }
        if (this.ageOfLocationEstimate != null) {
            sb.append(", ageOfLocationEstimate=");
            sb.append(this.ageOfLocationEstimate.getValue());
        }
        if (this.additionalLocationEstimate != null) {
            sb.append(", additionalLocationEstimate=");
            sb.append(this.additionalLocationEstimate);
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (this.deferredMTLRResponseIndicator!=null) {
            sb.append(", deferredMTLRResponseIndicator");
        }
        if (this.cellGlobalIdOrServiceAreaIdOrLAIWrapped != null && this.cellGlobalIdOrServiceAreaIdOrLAIWrapped.getCellGlobalIdOrServiceAreaIdOrLAI()!=null) {
            sb.append(", cellGlobalIdOrServiceAreaIdOrLAI=");
            sb.append(this.cellGlobalIdOrServiceAreaIdOrLAIWrapped.getCellGlobalIdOrServiceAreaIdOrLAI());
        }
        if (this.saiPresent!=null) {
            sb.append(", saiPresent");
        }
        if (this.accuracyFulfilmentIndicator != null) {
            sb.append(", accuracyFulfilmentIndicator=");
            sb.append(this.accuracyFulfilmentIndicator.getType());
        }
        if (this.velocityEstimate != null) {
            sb.append(", velocityEstimate=");
            sb.append(this.velocityEstimate);
        }
        if (this.moLrShortCircuitIndicator!=null) {
            sb.append(", moLrShortCircuitIndicator");
        }
        if (this.geranGANSSpositioningData != null) {
            sb.append(", geranGANSSpositioningData=");
            sb.append(this.geranGANSSpositioningData);
        }
        if (this.utranGANSSpositioningData != null) {
            sb.append(", utranGANSSpositioningData=");
            sb.append(this.utranGANSSpositioningData);
        }
        if (this.targetServingNodeForHandover != null && this.targetServingNodeForHandover.getServingNodeAddress()!=null) {
            sb.append(", targetServingNodeForHandover=");
            sb.append(this.targetServingNodeForHandover.getServingNodeAddress());
        }

        sb.append("]");

        return sb.toString();
    }
}
