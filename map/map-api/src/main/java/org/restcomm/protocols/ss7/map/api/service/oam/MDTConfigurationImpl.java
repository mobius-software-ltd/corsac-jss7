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

package org.restcomm.protocols.ss7.map.api.service.oam;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MDTConfigurationImpl {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=10,constructed=false,index=0)
    private ASNJobTypeImpl jobType;
    
	private AreaScopeImpl areaScope;
    
    private ListOfMeasurementsImpl listOfMeasurements;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ReportingTriggerImpl reportingTrigger;
    
    private ASNReportIntervalImpl reportInterval;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNReportAmountImpl reportAmount;
    
    private ASNInteger eventThresholdRSRP;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNInteger eventThresholdRSRQ;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNLoggingIntervalImpl loggingInterval;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private ASNLoggingDurationImpl loggingDuration;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;

    public MDTConfigurationImpl() {        
    }

    public MDTConfigurationImpl(JobType jobType, AreaScopeImpl areaScope, ListOfMeasurementsImpl listOfMeasurements, ReportingTriggerImpl reportingTrigger,
            ReportInterval reportInterval, ReportAmount reportAmount, Integer eventThresholdRSRP, Integer eventThresholdRSRQ, LoggingInterval loggingInterval,
            LoggingDuration loggingDuration, MAPExtensionContainerImpl extensionContainer) {
    	if(jobType!=null) {
        	this.jobType = new ASNJobTypeImpl();
        	this.jobType.setType(jobType);
        }
        
        this.areaScope = areaScope;
        this.listOfMeasurements = listOfMeasurements;
        this.reportingTrigger = reportingTrigger;
        
        if(reportInterval!=null) {
        	this.reportInterval = new ASNReportIntervalImpl();
        	this.reportInterval.setType(reportInterval);
        }
        
        if(reportAmount!=null) {
        	this.reportAmount = new ASNReportAmountImpl();
        	this.reportAmount.setType(reportAmount);
        }
        
        if(eventThresholdRSRP!=null) {
        	this.eventThresholdRSRP = new ASNInteger();
        	this.eventThresholdRSRP.setValue(eventThresholdRSRP.longValue());
        }
        
        if(eventThresholdRSRQ!=null) {
        	this.eventThresholdRSRQ = new ASNInteger();
        	this.eventThresholdRSRQ.setValue(eventThresholdRSRQ.longValue());
        }
        
        if(loggingInterval!=null) {
        	this.loggingInterval = new ASNLoggingIntervalImpl();
        	this.loggingInterval.setType(loggingInterval);
        }
        
        if(loggingDuration!=null) {
        	this.loggingDuration = new ASNLoggingDurationImpl();
        	this.loggingDuration.setType(loggingDuration);
        }
        
        this.extensionContainer = extensionContainer;
    }

    public JobType getJobType() {
    	if(jobType==null)
    		return null;
    	
        return jobType.getType();
    }

    public AreaScopeImpl getAreaScope() {
        return areaScope;
    }

    public ListOfMeasurementsImpl getListOfMeasurements() {
        return listOfMeasurements;
    }

    public ReportingTriggerImpl getReportingTrigger() {
        return reportingTrigger;
    }

    public ReportInterval getReportInterval() {
    	if(reportInterval==null)
    		return null;
    	
        return reportInterval.getType();
    }

    public ReportAmount getReportAmount() {
    	if(reportAmount==null)
    		return null;
    	
        return reportAmount.getType();
    }

    public Integer getEventThresholdRSRP() {
    	if(eventThresholdRSRP==null)
    		return null;
    	
        return eventThresholdRSRP.getValue().intValue();
    }

    public Integer getEventThresholdRSRQ() {
    	if(eventThresholdRSRQ==null)
    		return null;
    	
        return eventThresholdRSRQ.getValue().intValue();
    }

    public LoggingInterval getLoggingInterval() {
    	if(loggingInterval==null)
    		return null;
    	
        return loggingInterval.getType();
    }

    public LoggingDuration getLoggingDuration() {
    	if(loggingDuration==null)
    		return null;
    	
        return loggingDuration.getType();
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MDTConfiguration [");

        if (this.jobType != null) {
            sb.append("jobType=");
            sb.append(this.jobType.getType());
            sb.append(", ");
        }
        if (this.areaScope != null) {
            sb.append("areaScope=");
            sb.append(this.areaScope.toString());
            sb.append(", ");
        }
        if (this.listOfMeasurements != null) {
            sb.append("listOfMeasurements=");
            sb.append(this.listOfMeasurements.toString());
            sb.append(", ");
        }
        if (this.reportingTrigger != null) {
            sb.append("reportingTrigger=");
            sb.append(this.reportingTrigger.toString());
            sb.append(", ");
        }
        if (this.reportInterval != null) {
            sb.append("reportInterval=");
            sb.append(this.reportInterval.getType());
            sb.append(", ");
        }
        if (this.reportAmount != null) {
            sb.append("reportAmount=");
            sb.append(this.reportAmount.getType());
            sb.append(", ");
        }
        if (this.eventThresholdRSRP != null) {
            sb.append("eventThresholdRSRP=");
            sb.append(this.eventThresholdRSRP.getValue());
            sb.append(", ");
        }
        if (this.eventThresholdRSRQ != null) {
            sb.append("eventThresholdRSRQ=");
            sb.append(this.eventThresholdRSRQ.getValue());
            sb.append(", ");
        }
        if (this.loggingInterval != null) {
            sb.append("loggingInterval=");
            sb.append(this.loggingInterval.getValue());
            sb.append(", ");
        }
        if (this.loggingDuration != null) {
            sb.append("loggingDuration=");
            sb.append(this.loggingDuration.getValue());
            sb.append(", ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
