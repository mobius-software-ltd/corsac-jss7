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

package org.restcomm.protocols.ss7.tcap.asn.comp;

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
public class RejectImpl implements Reject {

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
    public Problem getProblem() {

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
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Reject#setProblem(GeneralProblemType)
     */
    public void setProblem(GeneralProblemType generalProblemType) {
    	if(generalProblemType==null)
    		this.problem=null;
    	else {
    		this.problem=new ProblemImpl();
    		this.problem.setGeneralProblemType(generalProblemType);
    	}
    }
    
    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Reject#setProblem(InvokeProblemType)
     */
    public void setProblem(InvokeProblemType invokeProblemType) {
    	if(invokeProblemType==null)
    		this.problem=null;
    	else {
    		this.problem=new ProblemImpl();
    		this.problem.setInvokeProblemType(invokeProblemType);
    	}
    }
    
    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Reject#setProblem(ReturnErrorProblemType)
     */
    public void setProblem(ReturnErrorProblemType returnErrorProblemType) {
    	if(returnErrorProblemType==null)
    		this.problem=null;
    	else {
    		this.problem=new ProblemImpl();
    		this.problem.setReturnErrorProblemType(returnErrorProblemType);
    	}
    }
    
    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Reject#setProblem(ReturnResultProblemType)
     */
    public void setProblem(ReturnResultProblemType returnResultProblemType) {
    	if(returnResultProblemType==null)
    		this.problem=null;
    	else {
    		this.problem=new ProblemImpl();
    		this.problem.setReturnResultProblemType(returnResultProblemType);
    	}
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
        return "Reject[invokeId=" + invokeId + (this.isLocalOriginated() ? ", localOriginated" : ", remoteOriginated")
                + ", problem=" + problem + "]";
    }
}
