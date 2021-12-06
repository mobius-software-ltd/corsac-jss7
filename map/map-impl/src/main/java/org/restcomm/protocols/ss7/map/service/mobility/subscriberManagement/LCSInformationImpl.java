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
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSPrivacyClass;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MOLRClass;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class LCSInformationImpl implements LCSInformation {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private ISDNAddressStringWrapperImpl gmlcList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private LCSPrivacyClassListWrapperImpl lcsPrivacyExceptionList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1)
    private MOLRClassListWrapperImpl molrList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private LCSPrivacyClassListWrapperImpl addLcsPrivacyExceptionList;

    public LCSInformationImpl() {
    }

    public LCSInformationImpl(List<ISDNAddressString> gmlcList, List<LCSPrivacyClass> lcsPrivacyExceptionList,
            List<MOLRClass> molrList, List<LCSPrivacyClass> addLcsPrivacyExceptionList) {        
    	if(gmlcList!=null)
    		this.gmlcList = new ISDNAddressStringWrapperImpl(gmlcList);
    	
    	if(lcsPrivacyExceptionList!=null)
    		this.lcsPrivacyExceptionList = new LCSPrivacyClassListWrapperImpl(lcsPrivacyExceptionList);
    	
    	if(molrList!=null)
    		this.molrList = new MOLRClassListWrapperImpl(molrList);
    	
    	if(addLcsPrivacyExceptionList!=null)
    		this.addLcsPrivacyExceptionList = new LCSPrivacyClassListWrapperImpl(addLcsPrivacyExceptionList);
    }

    public List<ISDNAddressString> getGmlcList() {
    	if(this.gmlcList==null)
    		return null;
    	
        return this.gmlcList.getISDNAddressString();
    }

    public List<LCSPrivacyClass> getLcsPrivacyExceptionList() {
    	if(this.lcsPrivacyExceptionList==null)
    		return null;
    	
        return this.lcsPrivacyExceptionList.getLCSPrivacyClass();
    }

    public List<MOLRClass> getMOLRList() {
    	if(this.molrList==null)
    		return null;
    	
        return this.molrList.getMOLRClass();
    }

    public List<LCSPrivacyClass> getAddLcsPrivacyExceptionList() {
    	if(this.addLcsPrivacyExceptionList==null)
    		return null;
    	
        return this.addLcsPrivacyExceptionList.getLCSPrivacyClass();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LCSInformation [");

        if (this.gmlcList != null && this.gmlcList.getISDNAddressString()!=null) {
            sb.append("gmlcList=[");
            boolean firstItem = true;
            for (ISDNAddressString be : this.gmlcList.getISDNAddressString()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.lcsPrivacyExceptionList != null && this.lcsPrivacyExceptionList.getLCSPrivacyClass()!=null) {
            sb.append("lcsPrivacyExceptionList=[");
            boolean firstItem = true;
            for (LCSPrivacyClass be : this.lcsPrivacyExceptionList.getLCSPrivacyClass()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.molrList != null && this.molrList.getMOLRClass()!=null) {
            sb.append("molrList=[");
            boolean firstItem = true;
            for (MOLRClass be : this.molrList.getMOLRClass()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.addLcsPrivacyExceptionList != null && this.addLcsPrivacyExceptionList.getLCSPrivacyClass()!=null) {
            sb.append("addLcsPrivacyExceptionList=[");
            boolean firstItem = true;
            for (LCSPrivacyClass be : this.addLcsPrivacyExceptionList.getLCSPrivacyClass()) {
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
