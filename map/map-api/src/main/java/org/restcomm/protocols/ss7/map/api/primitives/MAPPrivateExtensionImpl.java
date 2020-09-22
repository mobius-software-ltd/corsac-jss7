/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.map.api.primitives;

import java.util.List;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNGenericMapping;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWildcard;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNObjectIdentifier;

/**
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=0,constructed=false,lengthIndefinite=false)
public class MAPPrivateExtensionImpl {
	private ASNObjectIdentifier oId;
	
	@ASNWildcard
    private ASNPrivateExtentionImpl data;

    public MAPPrivateExtensionImpl() {
    }

    public MAPPrivateExtensionImpl(List<Long> oId, Object data) {
        this.oId = new ASNObjectIdentifier();
        this.oId.setValue(oId);
        this.data = new ASNPrivateExtentionImpl();
        this.data.setValue(data);
    }

    @ASNGenericMapping
    public Class<?> getMapping(Object parent,ASNParser parser) {
    	if(oId!=null && oId.getValue()!=null)
    	{
    		Class<?> result=parser.getLocalMapping(this.getClass(), oId.getValue());
    		if(result==null)
    			result=parser.getDefaultLocalMapping(this.getClass());
    		
    		return result;
    	}
    	
    	return null;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.dialog.MAPPrivateExtension#getOId()
     */
    public List<Long> getOId() {
    	if(this.oId==null)
    		return null;
    	
        return this.oId.getValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.dialog.MAPPrivateExtension#setOId (long[])
     */
    public void setOId(List<Long> oId) {
        this.oId = new ASNObjectIdentifier();
        this.oId.setValue(oId);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.dialog.MAPPrivateExtension#getData()
     */
    public Object getData() {
        return this.data.getValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.dialog.MAPPrivateExtension#setData (byte[])
     */
    public void setData(Object data) {
        this.data = new ASNPrivateExtentionImpl();
        this.data.setValue(data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PrivateExtension [");

        if (this.oId != null || this.oId.getValue()!=null && this.oId.getValue().size() > 0) {
            sb.append("Oid=");
            sb.append(this.ListToString(this.oId.getValue()));
        }

        if (this.data != null) {
            sb.append(", data=");
            sb.append(this.data.toString());
        }

        sb.append("]");

        return sb.toString();
    }

    private String ListToString(List<Long> array) {
        StringBuilder sb = new StringBuilder();
        int i1 = 0;
        for (Long b : array) {
            if (i1 == 0)
                i1 = 1;
            else
                sb.append(", ");
            sb.append(b);
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data==null || data.getValue()==null)?0:data.getValue().hashCode());
        if(oId!=null && oId.getValue()!=null)
        result = prime * result + oId.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MAPPrivateExtensionImpl other = (MAPPrivateExtensionImpl) obj;
        if(data==null || data.getValue()==null) {
        	if(other.data!=null && other.data.getValue()!=null)
        		return false;
        }
        else if (data.getValue().equals(other.data.getValue()))
            return false;
        
        if(oId==null || oId.getValue()==null) {
        	if(other.oId!=null && other.oId.getValue()!=null)
        		return false;
        }
        else if (oId.getValue().equals(other.oId.getValue()))
            return false;
        
        return true;
    }
}