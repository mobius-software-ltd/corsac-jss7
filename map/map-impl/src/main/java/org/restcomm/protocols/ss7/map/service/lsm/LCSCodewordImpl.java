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

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSCodeword;
import org.restcomm.protocols.ss7.map.datacoding.ASNCBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.primitives.USSDStringImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author amit bhayani
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class LCSCodewordImpl implements LCSCodeword {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=0)
    private ASNCBSDataCodingSchemeImpl dataCodingScheme;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=1,defaultImplementation = USSDStringImpl.class)
    private USSDString lcsCodewordString;

    /**
     *
     */
    public LCSCodewordImpl() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param dataCodingScheme
     * @param lcsCodewordString
     */
    public LCSCodewordImpl(CBSDataCodingScheme dataCodingScheme, USSDString lcsCodewordString) {
        super();
        
        if(dataCodingScheme!=null)
        	this.dataCodingScheme = new ASNCBSDataCodingSchemeImpl(dataCodingScheme);
        
        this.lcsCodewordString = lcsCodewordString;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSCodeword# getDataCodingScheme()
     */
    public CBSDataCodingScheme getDataCodingScheme() throws MAPException {
    	if(this.dataCodingScheme==null)
    		return null;
    	
        return this.dataCodingScheme.getDataCoding();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSCodeword# getLCSCodewordString()
     */
    public USSDString getLCSCodewordString() {
        return this.lcsCodewordString;
    }    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LCSCodeword [");

        if(dataCodingScheme!=null) {
	        sb.append("dataCodingScheme=");
	        sb.append(this.dataCodingScheme.getDataCoding());	        
        }
        
        if (this.lcsCodewordString != null) {
            sb.append(", lcsCodewordString=");
            sb.append(this.lcsCodewordString.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
