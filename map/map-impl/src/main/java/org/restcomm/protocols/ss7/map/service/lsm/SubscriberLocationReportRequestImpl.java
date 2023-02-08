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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMEI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdOrLAIWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.restcomm.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMNList;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;

/**
 * @author <a href="mailto:abhayani@gmail.com"> Amit Bhayani </a>
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 * @author yulianoifa
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SubscriberLocationReportRequestImpl extends LsmMessageImpl implements SubscriberLocationReportRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=10,constructed=false,index=0)
    private ASNLCSEvent lcsEvent;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=1, defaultImplementation = LCSClientIDImpl.class)
    private LCSClientID lcsClientID;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=2, defaultImplementation = LCSLocationInfoImpl.class)
    private LCSLocationInfo lcsLocationInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString msisdn;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1, defaultImplementation = IMSIImpl.class)
    private IMSI imsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = IMEIImpl.class)
    private IMEI imei;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString naEsrd;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString naEsrk;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1, defaultImplementation = ExtGeographicalInformationImpl.class)
    private ExtGeographicalInformation locationEstimate;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private ASNInteger ageOfLocationEstimate;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=true,index=-1, defaultImplementation = SLRArgExtensionContainerImpl.class)
    private SLRArgExtensionContainer slrArgExtensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1, defaultImplementation = AddGeographicalInformationImpl.class)
    private AddGeographicalInformation addLocationEstimate;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=true,index=-1, defaultImplementation = DeferredmtlrDataImpl.class)
    private DeferredmtlrData deferredmtlrData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=false,index=-1)
    private ASNSingleByte lcsReferenceNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=false,index=-1, defaultImplementation = PositioningDataInformationImpl.class)
    private PositioningDataInformation geranPositioningData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=false,index=-1, defaultImplementation = UtranPositioningDataInfoImpl.class)
    private UtranPositioningDataInfo utranPositioningData;

    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=true,index=-1)
    private CellGlobalIdOrServiceAreaIdOrLAIWrapperImpl cellGlobalIdOrServiceAreaIdOrLAIWrapped;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=false,index=-1, defaultImplementation = GSNAddressImpl.class)
    private GSNAddress hgmlcAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=15,constructed=false,index=-1)
    private ASNInteger lcsServiceTypeID;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=17,constructed=false,index=-1)
    private ASNNull saiPresent;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=18,constructed=false,index=-1)
    private ASNNull pseudonymIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=19,constructed=false,index=-1)
    private ASNAccuracyFulfilmentIndicator accuracyFulfilmentIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=20,constructed=false,index=-1, defaultImplementation = VelocityEstimateImpl.class)
    private VelocityEstimate velocityEstimate;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=21,constructed=false,index=-1)
    private ASNInteger sequenceNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=22,constructed=true,index=-1, defaultImplementation = PeriodicLDRInfoImpl.class)
    private PeriodicLDRInfo periodicLDRInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=23,constructed=false,index=-1)
    private ASNNull moLrShortCircuitIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=24,constructed=false,index=-1, defaultImplementation = GeranGANSSpositioningDataImpl.class)
    private GeranGANSSpositioningData geranGANSSpositioningData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=25,constructed=false,index=-1,defaultImplementation = UtranGANSSpositioningDataImpl.class)
    private UtranGANSSpositioningData utranGANSSpositioningData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=26,constructed=true,index=-1)
    private ServingNodeAddressWrapperImpl targetServingNodeForHandover;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=27,constructed=false,index=-1, defaultImplementation = LMSIImpl.class)
    private LMSI lmsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=28,constructed=true,index=-1, defaultImplementation = ReportingPLMNListImpl.class)
    private ReportingPLMNList reportingPLMNList;

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
    public SubscriberLocationReportRequestImpl(LCSEvent lcsEvent, LCSClientID lcsClientID, LCSLocationInfo lcsLocationInfo,
                                               ISDNAddressString msisdn, IMSI imsi, IMEI imei, ISDNAddressString naEsrd, ISDNAddressString naEsrk,
                                               ExtGeographicalInformation locationEstimate, Integer ageOfLocationEstimate,
                                               SLRArgExtensionContainer slrArgExtensionContainer, AddGeographicalInformation addLocationEstimate,
                                               DeferredmtlrData deferredmtlrData, Integer lcsReferenceNumber, PositioningDataInformation geranPositioningData,
                                               UtranPositioningDataInfo utranPositioningData, CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai,
                                               GSNAddress hgmlcAddress, Integer lcsServiceTypeID, boolean saiPresent, boolean pseudonymIndicator,
                                               AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimate velocityEstimate, Integer sequenceNumber,
                                               PeriodicLDRInfo periodicLDRInfo, boolean moLrShortCircuitIndicator,
                                               GeranGANSSpositioningData geranGANSSpositioningData, UtranGANSSpositioningData utranGANSSpositioningData,
                                               ServingNodeAddress targetServingNodeForHandover) {
        super();
        
        if(lcsEvent!=null)
        	this.lcsEvent = new ASNLCSEvent(lcsEvent);
        	
        this.lcsClientID = lcsClientID;
        this.lcsLocationInfo = lcsLocationInfo;
        this.msisdn = msisdn;
        this.imsi = imsi;
        this.imei = imei;
        this.naEsrd = naEsrd;
        this.naEsrk = naEsrk;
        this.locationEstimate = locationEstimate;
        
        if(ageOfLocationEstimate!=null)
        	this.ageOfLocationEstimate = new ASNInteger(ageOfLocationEstimate,"AgeOfLocationEstimate",0,32767,false);
        	
        this.slrArgExtensionContainer = slrArgExtensionContainer;
        this.addLocationEstimate = addLocationEstimate;
        this.deferredmtlrData = deferredmtlrData;
        
        if(lcsReferenceNumber!=null)
        	this.lcsReferenceNumber = new ASNSingleByte(lcsReferenceNumber,"LCSReferenceNumber",0,255,false);
        	
        this.geranPositioningData = geranPositioningData;
        this.utranPositioningData = utranPositioningData;
        
        if(cellIdOrSai!=null)
        	this.cellGlobalIdOrServiceAreaIdOrLAIWrapped = new CellGlobalIdOrServiceAreaIdOrLAIWrapperImpl(cellIdOrSai);
        
        this.hgmlcAddress = hgmlcAddress;
        
        if(lcsServiceTypeID!=null)
        	this.lcsServiceTypeID = new ASNInteger(lcsServiceTypeID,"LCSServiceTypeID",0,127,false);
        	
        if(saiPresent)
        	this.saiPresent = new ASNNull();
        
        if(pseudonymIndicator)
        	this.pseudonymIndicator = new ASNNull();
        
        if(accuracyFulfilmentIndicator!=null)
        	this.accuracyFulfilmentIndicator = new ASNAccuracyFulfilmentIndicator(accuracyFulfilmentIndicator);
        	
        this.velocityEstimate = velocityEstimate;
        
        if(sequenceNumber!=null)
        	this.sequenceNumber = new ASNInteger(sequenceNumber,"SequenceNumber",1,8639999,false);
        	
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
    public SubscriberLocationReportRequestImpl(LCSEvent lcsEvent, LCSClientID lcsClientID, LCSLocationInfo lcsLocationInfo, ISDNAddressString msisdn, IMSI imsi,
    		IMEI imei, ISDNAddressString naEsrd, ISDNAddressString naEsrk, ExtGeographicalInformation locationEstimate,
                                               Integer ageOfLocationEstimate, SLRArgExtensionContainer slrArgExtensionContainer,
                                               AddGeographicalInformation addLocationEstimate, DeferredmtlrData deferredmtlrData, Integer lcsReferenceNumber,
                                               PositioningDataInformation geranPositioningData, UtranPositioningDataInfo utranPositioningData,
                                               CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai, GSNAddress hgmlcAddress, Integer lcsServiceTypeID, boolean saiPresent,
                                               boolean pseudonymIndicator, AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimate velocityEstimate,
                                               Integer sequenceNumber, PeriodicLDRInfo periodicLDRInfo, boolean moLrShortCircuitIndicator, GeranGANSSpositioningData geranGANSSpositioningData,
                                               UtranGANSSpositioningData utranGANSSpositioningData, ServingNodeAddress targetServingNodeForHandover, LMSI lmsi,
                                               ReportingPLMNList reportingPLMNList) {
        super();
        
        if(lcsEvent!=null)
        	this.lcsEvent = new ASNLCSEvent(lcsEvent);
        	
        this.lcsClientID = lcsClientID;
        this.lcsLocationInfo = lcsLocationInfo;
        this.msisdn = msisdn;
        this.imsi = imsi;
        this.imei = imei;
        this.naEsrd = naEsrd;
        this.naEsrk = naEsrk;
        this.locationEstimate = locationEstimate;
        
        if(ageOfLocationEstimate!=null)
        	this.ageOfLocationEstimate = new ASNInteger(ageOfLocationEstimate,"AgeOfLocationEstimate",0,32767,false);
        	
        this.slrArgExtensionContainer = slrArgExtensionContainer;
        this.addLocationEstimate = addLocationEstimate;
        this.deferredmtlrData = deferredmtlrData;
        
        if(lcsReferenceNumber!=null)
        	this.lcsReferenceNumber = new ASNSingleByte(lcsReferenceNumber,"LCSReferenceNumber",0,255,false);
        	
        this.geranPositioningData = geranPositioningData;
        this.utranPositioningData = utranPositioningData;
        
        if(cellIdOrSai!=null)
        	this.cellGlobalIdOrServiceAreaIdOrLAIWrapped = new CellGlobalIdOrServiceAreaIdOrLAIWrapperImpl(cellIdOrSai);
        
        this.hgmlcAddress = hgmlcAddress;
        
        if(lcsServiceTypeID!=null)
        	this.lcsServiceTypeID = new ASNInteger(lcsServiceTypeID,"LCSServiceTypeID",0,127,false);
        	
        if(saiPresent)
        	this.saiPresent = new ASNNull();
        
        if(pseudonymIndicator)
        	this.pseudonymIndicator = new ASNNull();
        
        if(this.accuracyFulfilmentIndicator!=null)
        	this.accuracyFulfilmentIndicator = new ASNAccuracyFulfilmentIndicator(accuracyFulfilmentIndicator);
        	
        this.velocityEstimate = velocityEstimate;

        if(sequenceNumber!=null)
        	this.sequenceNumber = new ASNInteger(sequenceNumber,"SequenceNumber",1,8639999,false);
        	
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
    public LCSClientID getLCSClientID() {
        return this.lcsClientID;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getLCSLocationInfo()
     */
    public LCSLocationInfo getLCSLocationInfo() {
        return this.lcsLocationInfo;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getMSISDN()
     */
    public ISDNAddressString getMSISDN() {
        return this.msisdn;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getIMSI()
     */
    public IMSI getIMSI() {
        return this.imsi;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getIMEI()
     */
    public IMEI getIMEI() {
        return this.imei;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getNaESRD()
     */
    public ISDNAddressString getNaESRD() {
        return this.naEsrd;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getNaESRK()
     */
    public ISDNAddressString getNaESRK() {
        return this.naEsrk;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getLocationEstimate()
     */
    public ExtGeographicalInformation getLocationEstimate() {
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
    	
        return this.ageOfLocationEstimate.getIntValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.
     * SubscriberLocationReportRequestIndication#getSLRArgExtensionContainer()
     */
    public SLRArgExtensionContainer getSLRArgExtensionContainer() {
        return this.slrArgExtensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.
     * SubscriberLocationReportRequestIndication#getAdditionalLocationEstimate()
     */
    public AddGeographicalInformation getAdditionalLocationEstimate() {
        return this.addLocationEstimate;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getDeferredmtlrData()
     */
    public DeferredmtlrData getDeferredmtlrData() {
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
    	
        return this.lcsReferenceNumber.getValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getGeranPositioningData()
     */
    public PositioningDataInformation getGeranPositioningData() {
        return this.geranPositioningData;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication#getUtranPositioningData()
     */
    public UtranPositioningDataInfo getUtranPositioningData() {
        return this.utranPositioningData;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SubscriberLocationReportRequestIndication
     * #getCellGlobalIdOrServiceAreaIdOrLAI()
     */
    public CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI() {
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
    public GSNAddress getHGMLCAddress() {
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
    	
        return this.lcsServiceTypeID.getIntValue();
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

    public VelocityEstimate getVelocityEstimate() {
        return velocityEstimate;
    }

    public Integer getSequenceNumber() {
    	if(this.sequenceNumber==null)
    		return null;
    	
        return sequenceNumber.getIntValue();
    }

    public PeriodicLDRInfo getPeriodicLDRInfo() {
        return periodicLDRInfo;
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
    	if(targetServingNodeForHandover==null)
    		return null;
    	
        return targetServingNodeForHandover.getServingNodeAddress();
    }

    public LMSI getLMSI() {
        return lmsi;
    }

    public void setLmsi(LMSI lmsi) {
        this.lmsi = lmsi;
    }

    public ReportingPLMNList getReportingPLMNList() {
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
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(lcsEvent==null)
			throw new ASNParsingComponentException("lcs event should be set for subscriber location report request", ASNParsingComponentExceptionReason.MistypedRootParameter);

		if(lcsClientID==null)
			throw new ASNParsingComponentException("lcs client ID should be set for subscriber location report request", ASNParsingComponentExceptionReason.MistypedRootParameter);

		if(lcsLocationInfo==null)
			throw new ASNParsingComponentException("lcs location info should be set for subscriber location report request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
