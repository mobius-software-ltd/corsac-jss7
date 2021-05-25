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
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.ASNAccuracyFulfilmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.ASNLCSEvent;
import org.restcomm.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.AddGeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredmtlrDataImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.ExtGeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningDataImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientIDImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.PeriodicLDRInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.PositioningDataInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMNListImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.ServingNodeAddressImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.ServingNodeAddressWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningDataImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityEstimateImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ASNSingleByte;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 * @author <a href="mailto:abhayani@gmail.com"> Amit Bhayani </a>
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SubscriberLocationReportRequestImpl extends LsmMessageImpl implements SubscriberLocationReportRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=10,constructed=false,index=0)
    private ASNLCSEvent lcsEvent;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=1)
    private LCSClientIDImpl lcsClientID;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=2)
    private LCSLocationInfoImpl lcsLocationInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ISDNAddressStringImpl msisdn;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private IMSIImpl imsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private IMEIImpl imei;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ISDNAddressStringImpl naEsrd;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private ISDNAddressStringImpl naEsrk;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private ExtGeographicalInformationImpl locationEstimate;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private ASNInteger ageOfLocationEstimate;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=true,index=-1)
    private SLRArgExtensionContainerImpl slrArgExtensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private AddGeographicalInformationImpl addLocationEstimate;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=true,index=-1)
    private DeferredmtlrDataImpl deferredmtlrData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=false,index=-1)
    private ASNSingleByte lcsReferenceNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=false,index=-1)
    private PositioningDataInformationImpl geranPositioningData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=false,index=-1)
    private UtranPositioningDataInfoImpl utranPositioningData;

    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=true,index=-1)
    private CellGlobalIdOrServiceAreaIdOrLAIWrapperImpl cellGlobalIdOrServiceAreaIdOrLAIWrapped;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=false,index=-1)
    private GSNAddressImpl hgmlcAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=15,constructed=false,index=-1)
    private ASNInteger lcsServiceTypeID;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=17,constructed=false,index=-1)
    private ASNNull saiPresent;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=18,constructed=false,index=-1)
    private ASNNull pseudonymIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=19,constructed=false,index=-1)
    private ASNAccuracyFulfilmentIndicator accuracyFulfilmentIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=20,constructed=false,index=-1)
    private VelocityEstimateImpl velocityEstimate;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=21,constructed=false,index=-1)
    private ASNInteger sequenceNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=22,constructed=true,index=-1)
    private PeriodicLDRInfoImpl periodicLDRInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=23,constructed=false,index=-1)
    private ASNNull moLrShortCircuitIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=24,constructed=false,index=-1)
    private GeranGANSSpositioningDataImpl geranGANSSpositioningData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=25,constructed=false,index=-1)
    private UtranGANSSpositioningDataImpl utranGANSSpositioningData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=26,constructed=true,index=-1)
    private ServingNodeAddressWrapperImpl targetServingNodeForHandover;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=27,constructed=false,index=-1)
    private LMSIImpl lmsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=28,constructed=true,index=-1)
    private ReportingPLMNListImpl reportingPLMNList;

    /**
     *
     */
    public SubscriberLocationReportRequestImpl() {
        super();
    }

    /**
     * @param lcsEvent
     * @param lcsClientID
     * @param lcsLocationInfo
     * @param msisdn
     * @param imsi
     * @param imei
     * @param naEsrd
     * @param naEsrk
     * @param locationEstimate
     * @param ageOfLocationEstimate
     * @param slrArgExtensionContainer
     * @param addLocationEstimate
     * @param deferredmtlrData
     * @param lcsReferenceNumber
     * @param geranPositioningData
     * @param utranPositioningData
     * @param cellIdOrSai
     * @param hgmlcAddress
     * @param lcsServiceTypeID
     * @param saiPresent
     * @param pseudonymIndicator
     * @param accuracyFulfilmentIndicator
     */
    public SubscriberLocationReportRequestImpl(LCSEvent lcsEvent, LCSClientIDImpl lcsClientID, LCSLocationInfoImpl lcsLocationInfo,
                                               ISDNAddressStringImpl msisdn, IMSIImpl imsi, IMEIImpl imei, ISDNAddressStringImpl naEsrd, ISDNAddressStringImpl naEsrk,
                                               ExtGeographicalInformationImpl locationEstimate, Integer ageOfLocationEstimate,
                                               SLRArgExtensionContainerImpl slrArgExtensionContainer, AddGeographicalInformationImpl addLocationEstimate,
                                               DeferredmtlrDataImpl deferredmtlrData, Integer lcsReferenceNumber, PositioningDataInformationImpl geranPositioningData,
                                               UtranPositioningDataInfoImpl utranPositioningData, CellGlobalIdOrServiceAreaIdOrLAIImpl cellIdOrSai,
                                               GSNAddressImpl hgmlcAddress, Integer lcsServiceTypeID, boolean saiPresent, boolean pseudonymIndicator,
                                               AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimateImpl velocityEstimate, Integer sequenceNumber,
                                               PeriodicLDRInfoImpl periodicLDRInfo, boolean moLrShortCircuitIndicator,
                                               GeranGANSSpositioningDataImpl geranGANSSpositioningData, UtranGANSSpositioningDataImpl utranGANSSpositioningData,
                                               ServingNodeAddressImpl targetServingNodeForHandover) {
        super();
        
        if(lcsEvent!=null) {
        	this.lcsEvent = new ASNLCSEvent();
        	this.lcsEvent.setType(lcsEvent);
        }
        
        this.lcsClientID = lcsClientID;
        this.lcsLocationInfo = lcsLocationInfo;
        this.msisdn = msisdn;
        this.imsi = imsi;
        this.imei = imei;
        this.naEsrd = naEsrd;
        this.naEsrk = naEsrk;
        this.locationEstimate = locationEstimate;
        
        if(ageOfLocationEstimate!=null) {
        	this.ageOfLocationEstimate = new ASNInteger();
        	this.ageOfLocationEstimate.setValue(ageOfLocationEstimate.longValue());
        }
        
        this.slrArgExtensionContainer = slrArgExtensionContainer;
        this.addLocationEstimate = addLocationEstimate;
        this.deferredmtlrData = deferredmtlrData;
        
        if(lcsReferenceNumber!=null) {
        	this.lcsReferenceNumber = new ASNSingleByte();
        	this.lcsReferenceNumber.setValue(lcsReferenceNumber);
        }
        
        this.geranPositioningData = geranPositioningData;
        this.utranPositioningData = utranPositioningData;
        
        if(cellIdOrSai!=null)
        	this.cellGlobalIdOrServiceAreaIdOrLAIWrapped = new CellGlobalIdOrServiceAreaIdOrLAIWrapperImpl(cellIdOrSai);
        
        this.hgmlcAddress = hgmlcAddress;
        
        if(lcsServiceTypeID!=null) {
        	this.lcsServiceTypeID = new ASNInteger();
        	this.lcsServiceTypeID.setValue(lcsServiceTypeID.longValue());
        }
        
        if(saiPresent)
        	this.saiPresent = new ASNNull();
        
        if(pseudonymIndicator)
        	this.pseudonymIndicator = new ASNNull();
        
        if(accuracyFulfilmentIndicator!=null) {
        	this.accuracyFulfilmentIndicator = new ASNAccuracyFulfilmentIndicator();
        	this.accuracyFulfilmentIndicator.setType(accuracyFulfilmentIndicator);
        }
        
        this.velocityEstimate = velocityEstimate;
        
        if(sequenceNumber!=null) {
        	this.sequenceNumber = new ASNInteger();
        	this.sequenceNumber.setValue(sequenceNumber.longValue());
        }
        
        this.periodicLDRInfo = periodicLDRInfo;
        
        if(moLrShortCircuitIndicator)
        	this.moLrShortCircuitIndicator = new ASNNull();
        
        this.geranGANSSpositioningData = geranGANSSpositioningData;
        this.utranGANSSpositioningData = utranGANSSpositioningData;
        
        if(this.targetServingNodeForHandover==null)
        	this.targetServingNodeForHandover = new ServingNodeAddressWrapperImpl(targetServingNodeForHandover);
    }

    /**
     * @param lcsEvent
     * @param lcsClientID
     * @param lcsLocationInfo
     * @param msisdn
     * @param imsi
     * @param imei
     * @param naEsrd
     * @param naEsrk
     * @param locationEstimate
     * @param ageOfLocationEstimate
     * @param slrArgExtensionContainer
     * @param addLocationEstimate
     * @param deferredmtlrData
     * @param lcsReferenceNumber
     * @param geranPositioningData
     * @param utranPositioningData
     * @param cellIdOrSai
     * @param hgmlcAddress
     * @param lcsServiceTypeID
     * @param saiPresent
     * @param pseudonymIndicator
     * @param accuracyFulfilmentIndicator
     * @param lmsi
     * @param reportingPLMNList
     */
    public SubscriberLocationReportRequestImpl(LCSEvent lcsEvent, LCSClientIDImpl lcsClientID, LCSLocationInfoImpl lcsLocationInfo, ISDNAddressStringImpl msisdn, IMSIImpl imsi,
                                               IMEIImpl imei, ISDNAddressStringImpl naEsrd, ISDNAddressStringImpl naEsrk, ExtGeographicalInformationImpl locationEstimate,
                                               Integer ageOfLocationEstimate, SLRArgExtensionContainerImpl slrArgExtensionContainer,
                                               AddGeographicalInformationImpl addLocationEstimate, DeferredmtlrDataImpl deferredmtlrData, Integer lcsReferenceNumber,
                                               PositioningDataInformationImpl geranPositioningData, UtranPositioningDataInfoImpl utranPositioningData,
                                               CellGlobalIdOrServiceAreaIdOrLAIImpl cellIdOrSai, GSNAddressImpl hgmlcAddress, Integer lcsServiceTypeID, boolean saiPresent,
                                               boolean pseudonymIndicator, AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimateImpl velocityEstimate,
                                               Integer sequenceNumber, PeriodicLDRInfoImpl periodicLDRInfo, boolean moLrShortCircuitIndicator, GeranGANSSpositioningDataImpl geranGANSSpositioningData,
                                               UtranGANSSpositioningDataImpl utranGANSSpositioningData, ServingNodeAddressImpl targetServingNodeForHandover, LMSIImpl lmsi,
                                               ReportingPLMNListImpl reportingPLMNList) {
        super();
        
        if(lcsEvent!=null) {
        	this.lcsEvent = new ASNLCSEvent();
        	this.lcsEvent.setType(lcsEvent);
        }
        
        this.lcsClientID = lcsClientID;
        this.lcsLocationInfo = lcsLocationInfo;
        this.msisdn = msisdn;
        this.imsi = imsi;
        this.imei = imei;
        this.naEsrd = naEsrd;
        this.naEsrk = naEsrk;
        this.locationEstimate = locationEstimate;
        
        if(ageOfLocationEstimate!=null) {
        	this.ageOfLocationEstimate = new ASNInteger();
        	this.ageOfLocationEstimate.setValue(ageOfLocationEstimate.longValue());
        }
        
        this.slrArgExtensionContainer = slrArgExtensionContainer;
        this.addLocationEstimate = addLocationEstimate;
        this.deferredmtlrData = deferredmtlrData;
        
        if(lcsReferenceNumber!=null) {
        	this.lcsReferenceNumber = new ASNSingleByte();
        	this.lcsReferenceNumber.setValue(lcsReferenceNumber);
        }
        
        this.geranPositioningData = geranPositioningData;
        this.utranPositioningData = utranPositioningData;
        
        if(cellIdOrSai!=null)
        	this.cellGlobalIdOrServiceAreaIdOrLAIWrapped = new CellGlobalIdOrServiceAreaIdOrLAIWrapperImpl(cellIdOrSai);
        
        this.hgmlcAddress = hgmlcAddress;
        
        if(lcsServiceTypeID!=null) {
        	this.lcsServiceTypeID = new ASNInteger();
        	this.lcsServiceTypeID.setValue(lcsServiceTypeID.longValue());
        }
        
        if(saiPresent)
        	this.saiPresent = new ASNNull();
        
        if(pseudonymIndicator)
        	this.pseudonymIndicator = new ASNNull();
        
        if(this.accuracyFulfilmentIndicator!=null) {
        	this.accuracyFulfilmentIndicator = new ASNAccuracyFulfilmentIndicator();
        	this.accuracyFulfilmentIndicator.setType(accuracyFulfilmentIndicator);
        }
        
        this.velocityEstimate = velocityEstimate;

        if(sequenceNumber!=null) {
        	this.sequenceNumber = new ASNInteger();
        	this.sequenceNumber.setValue(sequenceNumber.longValue());
        }
        
        this.periodicLDRInfo = periodicLDRInfo;
        
        if(moLrShortCircuitIndicator)
        	this.moLrShortCircuitIndicator = new ASNNull();
        
        this.geranGANSSpositioningData = geranGANSSpositioningData;
        this.utranGANSSpositioningData = utranGANSSpositioningData;
        
        if(targetServingNodeForHandover==null)
        	this.targetServingNodeForHandover = new ServingNodeAddressWrapperImpl(targetServingNodeForHandover);

        this.lmsi = lmsi;
        this.reportingPLMNList = reportingPLMNList;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.subscriberLocationReport_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.subscriberLocationReport;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getLCSEvent()
     */
    public LCSEvent getLCSEvent() {
    	if(this.lcsEvent==null)
    		return null;
    	
        return this.lcsEvent.getType();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getLCSClientID()
     */
    public LCSClientIDImpl getLCSClientID() {
        return this.lcsClientID;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getLCSLocationInfo()
     */
    public LCSLocationInfoImpl getLCSLocationInfo() {
        return this.lcsLocationInfo;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getMSISDN()
     */
    public ISDNAddressStringImpl getMSISDN() {
        return this.msisdn;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getIMSI()
     */
    public IMSIImpl getIMSI() {
        return this.imsi;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getIMEI()
     */
    public IMEIImpl getIMEI() {
        return this.imei;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getNaESRD()
     */
    public ISDNAddressStringImpl getNaESRD() {
        return this.naEsrd;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getNaESRK()
     */
    public ISDNAddressStringImpl getNaESRK() {
        return this.naEsrk;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getLocationEstimate()
     */
    public ExtGeographicalInformationImpl getLocationEstimate() {
        return this.locationEstimate;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.
     * SubscriberLocationReportRequestIndication#getAgeOfLocationEstimate()
     */
    public Integer getAgeOfLocationEstimate() {
    	if(this.ageOfLocationEstimate==null)
    		return null;
    	
        return this.ageOfLocationEstimate.getValue().intValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.
     * SubscriberLocationReportRequestIndication#getSLRArgExtensionContainer()
     */
    public SLRArgExtensionContainerImpl getSLRArgExtensionContainer() {
        return this.slrArgExtensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.
     * SubscriberLocationReportRequestIndication#getAdditionalLocationEstimate()
     */
    public AddGeographicalInformationImpl getAdditionalLocationEstimate() {
        return this.addLocationEstimate;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getDeferredmtlrData()
     */
    public DeferredmtlrDataImpl getDeferredmtlrData() {
        return this.deferredmtlrData;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getLCSReferenceNumber()
     */
    public Integer getLCSReferenceNumber() {
    	if(this.lcsReferenceNumber==null)
    		return null;
    	
        return this.lcsReferenceNumber.getValue().intValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getGeranPositioningData()
     */
    public PositioningDataInformationImpl getGeranPositioningData() {
        return this.geranPositioningData;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getUtranPositioningData()
     */
    public UtranPositioningDataInfoImpl getUtranPositioningData() {
        return this.utranPositioningData;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication
     * #getCellGlobalIdOrServiceAreaIdOrLAI()
     */
    public CellGlobalIdOrServiceAreaIdOrLAIImpl getCellGlobalIdOrServiceAreaIdOrLAI() {
    	if(this.cellGlobalIdOrServiceAreaIdOrLAIWrapped!=null)
    		return this.cellGlobalIdOrServiceAreaIdOrLAIWrapped.getCellGlobalIdOrServiceAreaIdOrLAI();
    	
    	return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getSaiPresent()
     */
    public boolean getSaiPresent() {
        return this.saiPresent!=null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getHGMLCAddress()
     */
    public GSNAddressImpl getHGMLCAddress() {
        return this.hgmlcAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getLCSServiceTypeID()
     */
    public Integer getLCSServiceTypeID() {
    	if(this.lcsServiceTypeID==null)
    		return null;
    	
        return this.lcsServiceTypeID.getValue().intValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getPseudonymIndicator()
     */
    public boolean getPseudonymIndicator() {
        return this.pseudonymIndicator!=null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication
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

    public Integer getSequenceNumber() {
    	if(this.sequenceNumber==null)
    		return null;
    	
        return sequenceNumber.getValue().intValue();
    }

    public PeriodicLDRInfoImpl getPeriodicLDRInfo() {
        return periodicLDRInfo;
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
    	if(targetServingNodeForHandover==null)
    		return null;
    	
        return targetServingNodeForHandover.getServingNodeAddress();
    }

    public LMSIImpl getLMSI() {
        return lmsi;
    }

    public void setLmsi(LMSIImpl lmsi) {
        this.lmsi = lmsi;
    }

    public ReportingPLMNListImpl getReportingPLMNList() {
        return reportingPLMNList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SubscriberLocationReportRequest [");

        if (this.lcsEvent != null) {
            sb.append("lcsEvent");
            sb.append(this.lcsEvent.getType());
        }
        if (this.lcsClientID != null) {
            sb.append(", lcsClientID=");
            sb.append(this.lcsClientID);
        }
        if (this.lcsLocationInfo != null) {
            sb.append(", lcsLocationInfo=");
            sb.append(this.lcsLocationInfo);
        }
        if (this.msisdn != null) {
            sb.append(", msisdn=");
            sb.append(this.msisdn);
        }
        if (this.imsi != null) {
            sb.append(", imsi=");
            sb.append(this.imsi);
        }
        if (this.imei != null) {
            sb.append(", imei=");
            sb.append(this.imei);
        }
        if (this.naEsrd != null) {
            sb.append(", naEsrd=");
            sb.append(this.naEsrd);
        }
        if (this.naEsrk != null) {
            sb.append(", naEsrk=");
            sb.append(this.naEsrk);
        }
        if (this.locationEstimate != null) {
            sb.append(", locationEstimate=");
            sb.append(this.locationEstimate);
        }
        if (this.ageOfLocationEstimate != null) {
            sb.append(", ageOfLocationEstimate=");
            sb.append(this.ageOfLocationEstimate.getValue());
        }
        if (this.slrArgExtensionContainer != null) {
            sb.append(", slrArgExtensionContainer=");
            sb.append(this.slrArgExtensionContainer);
        }
        if (this.addLocationEstimate != null) {
            sb.append(", addLocationEstimate=");
            sb.append(this.addLocationEstimate);
        }
        if (this.deferredmtlrData != null) {
            sb.append(", deferredmtlrData=");
            sb.append(this.deferredmtlrData);
        }
        if (this.lcsReferenceNumber != null) {
            sb.append(", lcsReferenceNumber=");
            sb.append(this.lcsReferenceNumber.getValue());
        }
        if (this.geranPositioningData != null) {
            sb.append(", geranPositioningData=");
            sb.append(this.geranPositioningData);
        }
        if (this.utranPositioningData != null) {
            sb.append(", utranPositioningData=");
            sb.append(this.utranPositioningData);
        }
        if (this.cellGlobalIdOrServiceAreaIdOrLAIWrapped != null && this.cellGlobalIdOrServiceAreaIdOrLAIWrapped.getCellGlobalIdOrServiceAreaIdOrLAI()!=null) {
            sb.append(", cellIdOrSai=");
            sb.append(this.cellGlobalIdOrServiceAreaIdOrLAIWrapped.getCellGlobalIdOrServiceAreaIdOrLAI());
        }
        if (this.hgmlcAddress != null) {
            sb.append(", hgmlcAddress=");
            sb.append(this.hgmlcAddress);
        }
        if (this.lcsServiceTypeID != null) {
            sb.append(", lcsServiceTypeID=");
            sb.append(this.lcsServiceTypeID.getValue());
        }
        if (this.saiPresent!=null) {
            sb.append(", saiPresent");
        }
        if (this.pseudonymIndicator!=null) {
            sb.append(", pseudonymIndicator");
        }
        if (this.accuracyFulfilmentIndicator != null) {
            sb.append(", accuracyFulfilmentIndicator=");
            sb.append(this.accuracyFulfilmentIndicator.getType());
        }
        if (this.velocityEstimate != null) {
            sb.append(", velocityEstimate=");
            sb.append(this.velocityEstimate);
        }
        if (this.sequenceNumber != null) {
            sb.append(", sequenceNumber=");
            sb.append(this.sequenceNumber.getValue());
        }
        if (this.periodicLDRInfo != null) {
            sb.append(", periodicLDRInfo=");
            sb.append(this.periodicLDRInfo);
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
        if (this.lmsi != null) {
            sb.append(", lmsi=");
            sb.append(this.lmsi);
        }
        if (this.reportingPLMNList != null) {
            sb.append(", reportingPLMNList=");
            sb.append(this.reportingPLMNList);
        }

        sb.append("]");

        return sb.toString();
    }
}
