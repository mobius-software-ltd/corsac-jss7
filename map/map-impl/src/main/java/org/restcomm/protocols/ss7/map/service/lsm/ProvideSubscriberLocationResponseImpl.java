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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdOrLAIWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityEstimate;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ProvideSubscriberLocationResponseImpl extends LsmMessageImpl implements ProvideSubscriberLocationResponse {
	private static final long serialVersionUID = 1L;
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0, defaultImplementation = ExtGeographicalInformationImpl.class)
    private ExtGeographicalInformation locationEstimate;    
    
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNInteger ageOfLocationEstimate;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = AddGeographicalInformationImpl.class)
    private AddGeographicalInformation additionalLocationEstimate;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNNull deferredMTLRResponseIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = PositioningDataInformationImpl.class)
    private PositioningDataInformation geranPositioningData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1, defaultImplementation = UtranPositioningDataInfoImpl.class)
    private UtranPositioningDataInfo utranPositioningData;

    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=true,index=-1)
    private CellGlobalIdOrServiceAreaIdOrLAIWrapperImpl cellGlobalIdOrServiceAreaIdOrLAIWrapped;

    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private ASNNull saiPresent;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private ASNAccuracyFulfilmentIndicator accuracyFulfilmentIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1, defaultImplementation = VelocityEstimateImpl.class)
    private VelocityEstimate velocityEstimate;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=false,index=-1)
    private ASNNull moLrShortCircuitIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=false,index=-1, defaultImplementation = GeranGANSSpositioningDataImpl.class)
    private GeranGANSSpositioningData geranGANSSpositioningData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=false,index=-1, defaultImplementation = UtranGANSSpositioningDataImpl.class)
    private UtranGANSSpositioningData utranGANSSpositioningData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=true,index=-1)
    private ServingNodeAddressWrapperImpl targetServingNodeForHandover;


     /**
     *
     */
    public ProvideSubscriberLocationResponseImpl() {
        super();
    }

    public ProvideSubscriberLocationResponseImpl(ExtGeographicalInformation locationEstimate,
    		PositioningDataInformation geranPositioningData, UtranPositioningDataInfo utranPositioningData,
            Integer ageOfLocationEstimate, AddGeographicalInformation additionalLocationEstimate,
            MAPExtensionContainer extensionContainer, boolean deferredMTLRResponseIndicator,
            CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI, boolean saiPresent,
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimate velocityEstimate,
            boolean moLrShortCircuitIndicator, GeranGANSSpositioningData geranGANSSpositioningData,
            UtranGANSSpositioningData utranGANSSpositioningData, ServingNodeAddress targetServingNodeForHandover) {
        super();

        this.locationEstimate = locationEstimate;
        this.geranPositioningData = geranPositioningData;
        this.utranPositioningData = utranPositioningData;
        
        if(ageOfLocationEstimate!=null)
        	this.ageOfLocationEstimate = new ASNInteger(ageOfLocationEstimate,"AgeOfLocationEstimate",0,32767,false);        	
        
        this.additionalLocationEstimate = additionalLocationEstimate;
        this.extensionContainer = extensionContainer;
        
        if(deferredMTLRResponseIndicator)
        	this.deferredMTLRResponseIndicator = new ASNNull();
        
        if(cellGlobalIdOrServiceAreaIdOrLAI!=null)
        	this.cellGlobalIdOrServiceAreaIdOrLAIWrapped = new CellGlobalIdOrServiceAreaIdOrLAIWrapperImpl(cellGlobalIdOrServiceAreaIdOrLAI);
        
        if(saiPresent)
        	this.saiPresent = new ASNNull();
        
        if(accuracyFulfilmentIndicator!=null)
        	this.accuracyFulfilmentIndicator = new ASNAccuracyFulfilmentIndicator(accuracyFulfilmentIndicator);
        	
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
    public ExtGeographicalInformation getLocationEstimate() {
        return this.locationEstimate;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.
     * ProvideSubscriberLocationResponseIndication#getGeranPositioningData()
     */
    public PositioningDataInformation getGeranPositioningData() {
        return this.geranPositioningData;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.
     * ProvideSubscriberLocationResponseIndication#getUtranPositioningData()
     */
    public UtranPositioningDataInfo getUtranPositioningData() {
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
    	
        return this.ageOfLocationEstimate.getIntValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationResponseIndication
     * #getAdditionalLocationEstimate()
     */
    public AddGeographicalInformation getAdditionalLocationEstimate() {
        return this.additionalLocationEstimate;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationResponseIndication#getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
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
    public CellGlobalIdOrServiceAreaIdOrLAI getCellIdOrSai() {
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

    public VelocityEstimate getVelocityEstimate() {
        return velocityEstimate;
    }

    public boolean getMoLrShortCircuitIndicator() {
        return moLrShortCircuitIndicator!=null;
    }

    public GeranGANSSpositioningData getGeranGANSSpositioningData() {
        return geranGANSSpositioningData;
    }

    public UtranGANSSpositioningData getUtranGANSSpositioningData() {
        return utranGANSSpositioningData;
    }

    public ServingNodeAddress getTargetServingNodeForHandover() {
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
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(locationEstimate==null)
			throw new ASNParsingComponentException("location estimate should be set for provide subscriber location response", ASNParsingComponentExceptionReason.MistypedRootParameter);			
	}
}
