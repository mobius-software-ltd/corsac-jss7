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
/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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
package org.restcomm.protocols.ss7.isup.message.parameter;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWildcard;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;


/**
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x04,constructed=true,lengthIndefinite=false)
public class RejectImpl implements RemoteOperation {

    // this can actaully be null in this case.
    private ASNInteger invokeId;
    @SuppressWarnings("unused")
	private ASNNull nullInvokeId=new ASNNull();
    
    private boolean localOriginated = false;

    @ASNWildcard
    private ProblemImpl problem;

    public RejectImpl() {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Reject#getInvokeId()
     */
    public Integer getInvokeId() {
    	if(this.invokeId==null)
    		return null;
    	
        return this.invokeId.getIntValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Reject#getProblem()
     */
    public ProblemImpl getProblem() {

        return this.problem;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Reject#setInvokeId(java.lang .Long)
     */
    public void setInvokeId(Integer i) {
        if (i != null && (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
        }
            
        if(i==null) {
        	this.invokeId=null;
        	this.nullInvokeId=new ASNNull();
        } else {
	        this.invokeId = new ASNInteger(i,"InvokeID",-128,127,false);
	        this.nullInvokeId=null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Reject#setProblem(org.restcomm.protocols.ss7.tcap.asn.comp.Problem)
     */
    public void setProblem(ProblemImpl p) {
        this.problem = p;
    }

    public OperationType getType() {

        return OperationType.Reject;
    }

    public boolean isLocalOriginated() {
        return localOriginated;
    }

    public void setLocalOriginated(boolean p) {
        localOriginated = p;
    }

    public String toString() {
        return "Reject[invokeId=" + invokeId + (this.isLocalOriginated() ? ", localOriginated" : ", remoteOriginated")
                + ", problem=" + problem + "]";
    }
}
