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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMEI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSCodeword;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSPriority;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationType;
import org.restcomm.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMNList;
import org.restcomm.protocols.ss7.map.api.service.lsm.SupportedGADShapes;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ProvideSubscriberLocationRequestImpl extends LsmMessageImpl implements ProvideSubscriberLocationRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=0, defaultImplementation = LocationTypeImpl.class)
    private LocationType locationType;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString mlcNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1, defaultImplementation = LCSClientIDImpl.class)
    private LCSClientID lcsClientID;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNNull privacyOverride;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = IMSIImpl.class)
    private IMSI imsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString msisdn;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = LMSIImpl.class)
    private LMSI lmsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1, defaultImplementation = IMEIImpl.class)
    private IMEI imei;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private ASNLCSPriority lcsPriority;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=true,index=-1, defaultImplementation = LCSQoSImpl.class)
    private LCSQoS lcsQoS;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1, defaultImplementation = SupportedGADShapesImpl.class)
    private SupportedGADShapes supportedGADShapes;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=false,index=-1)
    private ASNSingleByte lcsReferenceNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=false,index=-1)
    private ASNInteger lcsServiceTypeID;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=true,index=-1, defaultImplementation = LCSCodewordImpl.class)
    private LCSCodeword lcsCodeword;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=true,index=-1, defaultImplementation = LCSPrivacyCheckImpl.class)
    private LCSPrivacyCheck lcsPrivacyCheck;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=true,index=-1, defaultImplementation = AreaEventInfoImpl.class)
    private AreaEventInfo areaEventInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=15,constructed=false,index=-1, defaultImplementation = GSNAddressImpl.class)
    private GSNAddress hgmlcAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=16,constructed=false,index=-1)
    private ASNNull moLrShortCircuitIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=17,constructed=true,index=-1, defaultImplementation = PeriodicLDRInfoImpl.class)
    private PeriodicLDRInfo periodicLDRInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=18,constructed=true,index=-1, defaultImplementation = ReportingPLMNListImpl.class)
    private ReportingPLMNList reportingPLMNList;

    /**
     *
     */
    public ProvideSubscriberLocationRequestImpl() {
        super();
    }

    /**
     * @param locationType
     * @param mlcNumber
     * @param lcsClientID
     * @param privacyOverride
     * @param imsi
     * @param msisdn
     * @param lmsi
     * @param imei
     * @param lcsPriority
     * @param lcsQoS
     * @param extensionContainer
     * @param supportedGADShapes
     * @param lcsReferenceNumber
     * @param lcsServiceTypeID
     * @param lcsCodeword
     * @param lcsPrivacyCheck
     * @param areaEventInfo
     * @param hgmlcAddress
     */
    public ProvideSubscriberLocationRequestImpl(LocationType locationType, ISDNAddressString mlcNumber,
    		LCSClientID lcsClientID, boolean privacyOverride, IMSI imsi, ISDNAddressString msisdn, LMSI lmsi, IMEI imei,
            LCSPriority lcsPriority, LCSQoS lcsQoS, MAPExtensionContainer extensionContainer,
            SupportedGADShapes supportedGADShapes, Integer lcsReferenceNumber, Integer lcsServiceTypeID,
            LCSCodeword lcsCodeword, LCSPrivacyCheck lcsPrivacyCheck, AreaEventInfo areaEventInfo, GSNAddress hgmlcAddress,
            boolean moLrShortCircuitIndicator, PeriodicLDRInfo periodicLDRInfo, ReportingPLMNList reportingPLMNList) {
        super();
        this.locationType = locationType;
        this.mlcNumber = mlcNumber;
        this.lcsClientID = lcsClientID;
        
        if(privacyOverride)
        	this.privacyOverride = new ASNNull();
        
        this.imsi = imsi;
        this.msisdn = msisdn;
        this.lmsi = lmsi;
        this.imei = imei;
        
        if(lcsPriority!=null) {
        	this.lcsPriority = new ASNLCSPriority();
        	this.lcsPriority.setType(lcsPriority);
        }
        
        this.lcsQoS = lcsQoS;
        this.extensionContainer = extensionContainer;
        this.supportedGADShapes = supportedGADShapes;
        
        if(lcsReferenceNumber!=null) {
        	this.lcsReferenceNumber = new ASNSingleByte();
        	this.lcsReferenceNumber.setValue(lcsReferenceNumber);
        }
        
        if(lcsServiceTypeID!=null) {
        	this.lcsServiceTypeID = new ASNInteger();
        	this.lcsServiceTypeID.setValue(lcsServiceTypeID.longValue());
        }
        
        this.lcsCodeword = lcsCodeword;
        this.lcsPrivacyCheck = lcsPrivacyCheck;
        this.areaEventInfo = areaEventInfo;
        this.hgmlcAddress = hgmlcAddress;
        
        if(moLrShortCircuitIndicator)
        	this.moLrShortCircuitIndicator = new ASNNull();
        
        this.periodicLDRInfo = periodicLDRInfo;
        this.reportingPLMNList = reportingPLMNList;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.provideSubscriberLocation_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.provideSubscriberLocation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getLocationType()
     */
    public LocationType getLocationType() {
        return this.locationType;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getMlcNumber()
     */
    public ISDNAddressString getMlcNumber() {
        return this.mlcNumber;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getLCSClientID()
     */
    public LCSClientID getLCSClientID() {
        return this.lcsClientID;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getPrivacyOverride()
     */
    public boolean getPrivacyOverride() {
        return this.privacyOverride!=null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getIMSI()
     */
    public IMSI getIMSI() {
        return this.imsi;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getMSISDN()
     */
    public ISDNAddressString getMSISDN() {
        return this.msisdn;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getLMSI()
     */
    public LMSI getLMSI() {
        return this.lmsi;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getLCSPriority()
     */
    public LCSPriority getLCSPriority() {
    	if(this.lcsPriority==null)
    		return null;
    	
        return this.lcsPriority.getType();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getLCSQoS()
     */
    public LCSQoS getLCSQoS() {
        return this.lcsQoS;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getIMEI()
     */
    public IMEI getIMEI() {
        return this.imei;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getSupportedGADShapes()
     */
    public SupportedGADShapes getSupportedGADShapes() {
        return this.supportedGADShapes;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getLCSReferenceNumber()
     */
    public Integer getLCSReferenceNumber() {
    	if(this.lcsReferenceNumber==null)
    		return null;
    	
        return this.lcsReferenceNumber.getValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getLCSCodeword()
     */
    public LCSCodeword getLCSCodeword() {
        return this.lcsCodeword;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getLCSServiceTypeID()
     */
    public Integer getLCSServiceTypeID() {
    	if(this.lcsServiceTypeID==null)
    		return null;
    	
        return this.lcsServiceTypeID.getValue().intValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getLCSPrivacyCheck()
     */
    public LCSPrivacyCheck getLCSPrivacyCheck() {
        return this.lcsPrivacyCheck;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getAreaEventInfo()
     */
    public AreaEventInfo getAreaEventInfo() {
        return this.areaEventInfo;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getHGMLCAddress()
     */
    public GSNAddress getHGMLCAddress() {
        return this.hgmlcAddress;
    }

    public boolean getMoLrShortCircuitIndicator() {
        return moLrShortCircuitIndicator!=null;
    }

    public PeriodicLDRInfo getPeriodicLDRInfo() {
        return periodicLDRInfo;
    }

    public ReportingPLMNList getReportingPLMNList() {
        return reportingPLMNList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProvideSubscriberLocationRequest [");

        if (this.locationType != null) {
            sb.append("locationType=");
            sb.append(locationType.toString());
        }
        if (this.mlcNumber != null) {
            sb.append(", mlcNumber=");
            sb.append(mlcNumber.toString());
        }
        if (this.lcsClientID != null) {
            sb.append(", lcsClientID=");
            sb.append(lcsClientID.toString());
        }
        if (this.privacyOverride!=null) {
            sb.append(", privacyOverride");
        }
        if (this.imsi != null) {
            sb.append(", imsi=");
            sb.append(imsi.toString());
        }
        if (this.msisdn != null) {
            sb.append(", msisdn=");
            sb.append(msisdn.toString());
        }
        if (this.lmsi != null) {
            sb.append(", lmsi=");
            sb.append(lmsi.toString());
        }
        if (this.imei != null) {
            sb.append(", imei=");
            sb.append(imei.toString());
        }
        if (this.lcsPriority != null) {
            sb.append(", lcsPriority=");
            sb.append(lcsPriority.toString());
        }
        if (this.lcsQoS != null) {
            sb.append(", lcsQoS=");
            sb.append(lcsQoS.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(extensionContainer.toString());
        }
        if (this.supportedGADShapes != null) {
            sb.append(", supportedGADShapes=");
            sb.append(supportedGADShapes.toString());
        }
        if (this.lcsReferenceNumber != null) {
            sb.append(", lcsReferenceNumber=");
            sb.append(lcsReferenceNumber.getValue());
        }
        if (this.lcsServiceTypeID != null) {
            sb.append(", lcsServiceTypeID=");
            sb.append(lcsServiceTypeID.getValue());
        }
        if (this.lcsCodeword != null) {
            sb.append(", lcsCodeword=");
            sb.append(lcsCodeword.toString());
        }
        if (this.lcsPrivacyCheck != null) {
            sb.append(", lcsPrivacyCheck=");
            sb.append(lcsPrivacyCheck.toString());
        }
        if (this.areaEventInfo != null) {
            sb.append(", areaEventInfo=");
            sb.append(areaEventInfo.toString());
        }
        if (this.hgmlcAddress != null) {
            sb.append(", hgmlcAddress=");
            sb.append(hgmlcAddress.toString());
        }
        if (this.moLrShortCircuitIndicator!=null) {
            sb.append(", moLrShortCircuitIndicator");
        }
        if (this.periodicLDRInfo != null) {
            sb.append(", periodicLDRInfo=");
            sb.append(periodicLDRInfo.toString());
        }
        if (this.reportingPLMNList != null) {
            sb.append(", reportingPLMNList=");
            sb.append(reportingPLMNList.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
