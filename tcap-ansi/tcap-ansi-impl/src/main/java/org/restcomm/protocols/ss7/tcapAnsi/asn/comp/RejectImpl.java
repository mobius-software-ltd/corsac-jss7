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

package org.restcomm.protocols.ss7.tcapAnsi.asn.comp;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Reject;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=12,constructed=true,lengthIndefinite=false)
public class RejectImpl implements Reject {
	protected ASNCorrelationID correlationId=new ASNCorrelationID();
    private ASNRejectProblemType rejectProblem;
    
    @SuppressWarnings("unused")
	private ASNEmptyParameterImpl parameter=new ASNEmptyParameterImpl();
    
    private boolean localOriginated = false;


    public RejectProblem getProblem() throws ParseException {
    	if(rejectProblem==null)
    		return null;
    	
        return rejectProblem.getType();
    }

    public void setProblem(RejectProblem p) {
        rejectProblem = new ASNRejectProblemType(p);        
    }

    public Long getCorrelationId() {
        Byte value=correlationId.getFirstValue();
        if(value==null)
        	return null;
        
        return value.longValue();
    }

    public void setCorrelationId(Long i) {
        if ((i == null) || (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
        }
        this.correlationId.setFirstValue(i.byteValue());
    }

    public ComponentType getType() {
        return ComponentType.Reject;
    }

    public boolean isLocalOriginated() {
        return localOriginated;
    }

    public void setLocalOriginated(boolean p) {
        localOriginated = p;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reject[");
        sb.append("localOriginated=");
        sb.append(this.localOriginated);
        sb.append(", ");
        if (this.getCorrelationId() != null) {
            sb.append("CorrelationId=");
            sb.append(this.getCorrelationId());
            sb.append(", ");
        }
        if (this.rejectProblem != null) {
            sb.append("Problem=");
            try {            
            	sb.append(this.rejectProblem.getType());
            }
            catch(Exception ex) {
            	
            }
            
            sb.append(", ");
        }
        sb.append("]");

        return sb.toString();
    }
}
