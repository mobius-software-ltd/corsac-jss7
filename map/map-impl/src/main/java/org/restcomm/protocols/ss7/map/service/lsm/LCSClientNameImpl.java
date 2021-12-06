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
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientName;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSFormatIndicator;
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
public class LCSClientNameImpl implements LCSClientName {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=0)
    private ASNCBSDataCodingSchemeImpl dataCodingScheme;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=1, defaultImplementation = USSDStringImpl.class)
    private USSDString nameString;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNLCSFormatIndicator lcsFormatIndicator;

    /**
     *
     */
    public LCSClientNameImpl() {
        super();
    }

    /**
     * @param dataCodingScheme
     * @param nameString
     * @param lcsFormatIndicator
     */
    public LCSClientNameImpl(CBSDataCodingScheme dataCodingScheme, USSDString nameString, LCSFormatIndicator lcsFormatIndicator) {
        super();
        if(dataCodingScheme!=null)
        	this.dataCodingScheme = new ASNCBSDataCodingSchemeImpl(dataCodingScheme);
        
        this.nameString = nameString;
        
        if(lcsFormatIndicator!=null) {
        	this.lcsFormatIndicator = new ASNLCSFormatIndicator();
        	this.lcsFormatIndicator.setType(lcsFormatIndicator);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientName# getDataCodingScheme()
     */
    public CBSDataCodingScheme getDataCodingScheme() throws MAPException {
    	if(this.dataCodingScheme==null)
    		return null;
    	
        return this.dataCodingScheme.getDataCoding();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientName#getNameString ()
     */
    public USSDString getNameString() {
        return this.nameString;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientName# getLCSFormatIndicator()
     */
    public LCSFormatIndicator getLCSFormatIndicator() {
    	if(this.lcsFormatIndicator==null)
    		return null;
    	
        return this.lcsFormatIndicator.getType();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LCSClientName [");

        sb.append("dataCodingScheme=");
        sb.append(this.dataCodingScheme);

        if (this.nameString != null) {
            sb.append(", nameString=");
            sb.append(this.nameString.toString());
        }
        if (this.lcsFormatIndicator != null) {
            sb.append(", lcsFormatIndicator=");
            sb.append(this.lcsFormatIndicator.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
