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
package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.util.ArrayList;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.ASNLCSClientInternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.ASNLCSClientInternalIDListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class LCSPrivacyClassImpl {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0)
    private SSCodeImpl ssCode;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1)
    private ExtSSStatusImpl ssStatus;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNNotificationToMSUser notificationToMSUser;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private ExternalClientIDListWrapperImpl externalClientList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1)
    private ASNLCSClientInternalIDListWrapperImpl plmnClientList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1)
    private ExternalClientIDListWrapperImpl extExternalClientList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=true,index=-1)
    private ServiceTypeListWrapperImpl serviceTypeList;

    public LCSPrivacyClassImpl() {
    }

    public LCSPrivacyClassImpl(SSCodeImpl ssCode, ExtSSStatusImpl ssStatus, NotificationToMSUser notificationToMSUser,
            ArrayList<ExternalClientImpl> externalClientList, ArrayList<LCSClientInternalID> plmnClientList,
            MAPExtensionContainerImpl extensionContainer, ArrayList<ExternalClientImpl> extExternalClientList,
            ArrayList<ServiceTypeImpl> serviceTypeList) {
        this.ssCode = ssCode;
        this.ssStatus = ssStatus;
        
        if(notificationToMSUser!=null) {
        	this.notificationToMSUser = new ASNNotificationToMSUser();
        	this.notificationToMSUser.setType(notificationToMSUser);
        }
        
        if(externalClientList!=null)
        	this.externalClientList = new ExternalClientIDListWrapperImpl(externalClientList);
        
        if(plmnClientList!=null) {
        	ArrayList<ASNLCSClientInternalID> realData=new ArrayList<ASNLCSClientInternalID>();
        	for(LCSClientInternalID curr:plmnClientList) {
        		ASNLCSClientInternalID currData=new ASNLCSClientInternalID();
        		currData.setType(curr);
        		realData.add(currData);
        	}
        	
        	this.plmnClientList = new ASNLCSClientInternalIDListWrapperImpl(realData);
        }
        
        this.extensionContainer = extensionContainer;
        
        if(extExternalClientList!=null)
        	this.extExternalClientList = new ExternalClientIDListWrapperImpl(extExternalClientList);
        
        if(this.serviceTypeList!=null)
        	this.serviceTypeList = new ServiceTypeListWrapperImpl(serviceTypeList);
    }

    public SSCodeImpl getSsCode() {
        return this.ssCode;
    }

    public ExtSSStatusImpl getSsStatus() {
        return this.ssStatus;
    }

    public NotificationToMSUser getNotificationToMSUser() {
    	if(this.notificationToMSUser==null)
    		return null;
    	
        return this.notificationToMSUser.getType();
    }

    public ArrayList<ExternalClientImpl> getExternalClientList() {
    	if(this.externalClientList==null)
    		return null;
    	
        return this.externalClientList.getExternalClient();
    }

    public ArrayList<LCSClientInternalID> getPLMNClientList() {
    	if(this.plmnClientList==null || this.plmnClientList.getLCSClientInternalID()==null)
    		return null;
    	
    	ArrayList<LCSClientInternalID> result=new ArrayList<LCSClientInternalID>();
    	for(ASNLCSClientInternalID curr:plmnClientList.getLCSClientInternalID()) {
    		result.add(curr.getType());
    	}
        return result;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    public ArrayList<ExternalClientImpl> getExtExternalClientList() {
    	if(this.extExternalClientList==null)
    		return null;
    	
        return this.extExternalClientList.getExternalClient();
    }

    public ArrayList<ServiceTypeImpl> getServiceTypeList() {
    	if(this.serviceTypeList==null)
    		return null;
    	
        return this.serviceTypeList.getServiceType();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LCSPrivacyClass [");

        if (this.ssCode != null) {
            sb.append("ssCode=");
            sb.append(this.ssCode.toString());
            sb.append(", ");
        }

        if (this.ssStatus != null) {
            sb.append("ssStatus=");
            sb.append(this.ssStatus.toString());
            sb.append(", ");
        }

        if (this.notificationToMSUser != null) {
            sb.append("notificationToMSUser=");
            sb.append(this.notificationToMSUser.toString());
            sb.append(", ");
        }

        if (this.externalClientList != null && this.externalClientList.getExternalClient()!=null) {
            sb.append("externalClientList=[");
            boolean firstItem = true;
            for (ExternalClientImpl be : this.externalClientList.getExternalClient()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.plmnClientList != null && this.plmnClientList.getLCSClientInternalID()!=null) {
            sb.append("plmnClientList=[");
            boolean firstItem = true;
            for (ASNLCSClientInternalID be : this.plmnClientList.getLCSClientInternalID()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.getType());
            }
            sb.append("], ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.extExternalClientList != null && this.extExternalClientList.getExternalClient()!=null) {
            sb.append("extExternalClientList=[");
            boolean firstItem = true;
            for (ExternalClientImpl be : this.extExternalClientList.getExternalClient()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.serviceTypeList != null && this.serviceTypeList.getServiceType()!=null) {
            sb.append("serviceTypeList=[");
            boolean firstItem = true;
            for (ServiceTypeImpl be : this.serviceTypeList.getServiceType()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("] ");
        }

        sb.append("]");

        return sb.toString();
    }
}