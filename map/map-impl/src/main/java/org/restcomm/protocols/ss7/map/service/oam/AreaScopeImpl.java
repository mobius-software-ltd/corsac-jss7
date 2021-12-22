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

package org.restcomm.protocols.ss7.map.service.oam;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.LAIFixedLength;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.EUtranCgi;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.RAIdentity;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.TAId;
import org.restcomm.protocols.ss7.commonapp.primitives.LAIFixedLengthListWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.EUtranCgiListWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.RAIdentityListWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.TAIdListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.primitives.GlobalCellId;
import org.restcomm.protocols.ss7.map.api.service.oam.AreaScope;
import org.restcomm.protocols.ss7.map.primitives.GlobalCellIdListWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class AreaScopeImpl implements AreaScope {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private GlobalCellIdListWrapperImpl cgiList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private EUtranCgiListWrapperImpl eUtranCgiList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1)
    private RAIdentityListWrapperImpl routingAreaIdList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private LAIFixedLengthListWrapperImpl locationAreaIdList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1)
    private TAIdListWrapperImpl trackingAreaIdList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public AreaScopeImpl() {
    }

    public AreaScopeImpl(List<GlobalCellId> cgiList, List<EUtranCgi> eUtranCgiList, List<RAIdentity> routingAreaIdList,
            List<LAIFixedLength> locationAreaIdList, List<TAId> trackingAreaIdList, MAPExtensionContainer extensionContainer) {
    	
    	if(cgiList!=null)
    		this.cgiList = new GlobalCellIdListWrapperImpl(cgiList);
    	
    	if(eUtranCgiList!=null)
    		this.eUtranCgiList = new EUtranCgiListWrapperImpl(eUtranCgiList);
        
        if(routingAreaIdList!=null)
        	this.routingAreaIdList = new RAIdentityListWrapperImpl(routingAreaIdList);
        
        if(locationAreaIdList!=null)
        	this.locationAreaIdList = new LAIFixedLengthListWrapperImpl(locationAreaIdList);
        
        if(trackingAreaIdList!=null)
        	this.trackingAreaIdList = new TAIdListWrapperImpl(trackingAreaIdList);
        
        this.extensionContainer = extensionContainer;
    }

    public List<GlobalCellId> getCgiList() {
    	if(cgiList==null)
    		return null;
    	
        return cgiList.getGlobalCellIdList();
    }

    public List<EUtranCgi> getEUutranCgiList() {
    	if(eUtranCgiList==null)
    		return null;
    	
        return eUtranCgiList.getEUtranCgiList();
    }

    public List<RAIdentity> getRoutingAreaIdList() {
    	if(routingAreaIdList==null)
    		return null;
    	
        return routingAreaIdList.getRAIdentityList();
    }

    public List<LAIFixedLength> getLocationAreaIdList() {
    	if(locationAreaIdList==null)
    		return null;
    	
        return locationAreaIdList.getLAIFixedLengthList();
    }

    public List<TAId> getTrackingAreaIdList() {
    	if(trackingAreaIdList==null)
    		return null;
    	
        return trackingAreaIdList.getTAIdList();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AreaScope [");

        if (this.cgiList != null && this.cgiList.getGlobalCellIdList()!=null) {
            sb.append("cgiList=[");
            boolean firstItem = true;
            for (GlobalCellId be : this.cgiList.getGlobalCellIdList()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("]");
        }
        if (this.eUtranCgiList != null && this.eUtranCgiList.getEUtranCgiList()!=null) {
            sb.append("eUtranCgiList=[");
            boolean firstItem = true;
            for (EUtranCgi be : this.eUtranCgiList.getEUtranCgiList()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("]");
        }
        if (this.routingAreaIdList != null && this.routingAreaIdList.getRAIdentityList()!=null) {
            sb.append("routingAreaIdList=[");
            boolean firstItem = true;
            for (RAIdentity be : this.routingAreaIdList.getRAIdentityList()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("]");
        }
        if (this.locationAreaIdList != null && this.locationAreaIdList.getLAIFixedLengthList()!=null) {
            sb.append("locationAreaIdList=[");
            boolean firstItem = true;
            for (LAIFixedLength be : this.locationAreaIdList.getLAIFixedLengthList()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("]");
        }
        if (this.trackingAreaIdList != null && this.trackingAreaIdList.getTAIdList()!=null) {
            sb.append("trackingAreaIdList=[");
            boolean firstItem = true;
            for (TAId be : this.trackingAreaIdList.getTAIdList()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("]");
        }

        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
