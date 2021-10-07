package org.restcomm.protocols.ss7.tcap.asn.comp;

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

import org.restcomm.protocols.ss7.tcap.asn.ParseException;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=0x00,constructed=false,lengthIndefinite=false)
public class ProblemImpl implements Problem {
	private ASNGeneralProblemType generalProblemType;
    private ASNInvokeProblemType invokeProblemType;
    private ASNReturnErrorProblemType returnErrorProblemType;
    private ASNReturnResultProblemType returnResultProblemType;

    /**
     * @return the type
     */
    public ProblemType getType() {
    	ProblemType problemType=null;
    	if(generalProblemType!=null)
    		problemType=ProblemType.General;
    	else if(invokeProblemType!=null)
    		problemType=ProblemType.Invoke;
    	else if(returnErrorProblemType!=null)
    		problemType=ProblemType.ReturnError;
    	else if(returnResultProblemType!=null)
    		problemType=ProblemType.ReturnResult;
    	
    	return problemType;
    }

    /**
     * @return the generalProblemType
     * @throws ParseException 
     */
    public GeneralProblemType getGeneralProblemType() throws ParseException {
    	if(generalProblemType==null)
    		return null;
    	
        return generalProblemType.getType();
    }

    /**
     * @param generalProblemType the generalProblemType to set
     */
    public void setGeneralProblemType(GeneralProblemType generalProblemType) {
        this.generalProblemType = new ASNGeneralProblemType();
        this.generalProblemType.setType(generalProblemType);
    }

    /**
     * @return the invokeProblemType
     * @throws ParseException 
     */
    public InvokeProblemType getInvokeProblemType() throws ParseException {
    	if(invokeProblemType==null)
    		return null;
    	
        return invokeProblemType.getType();
    }

    /**
     * @param invokeProblemType the invokeProblemType to set
     */
    public void setInvokeProblemType(InvokeProblemType invokeProblemType) {
        this.invokeProblemType = new ASNInvokeProblemType();
        this.invokeProblemType.setType(invokeProblemType);
    }

    /**
     * @return the returnErrorProblemType
     * @throws ParseException 
     */
    public ReturnErrorProblemType getReturnErrorProblemType() throws ParseException {
    	if(returnErrorProblemType==null)
    		return null;
    	
        return returnErrorProblemType.getType();
    }

    /**
     * @param returnErrorProblemType the returnErrorProblemType to set
     */
    public void setReturnErrorProblemType(ReturnErrorProblemType returnErrorProblemType) {
        this.returnErrorProblemType = new ASNReturnErrorProblemType();
        this.returnErrorProblemType.setType(returnErrorProblemType);
    }

    /**
     * @return the returnResultProblemType
     * @throws ParseException 
     */
    public ReturnResultProblemType getReturnResultProblemType() throws ParseException {
    	if(returnResultProblemType==null)
    		return null;
    	
        return returnResultProblemType.getType();
    }

    /**
     * @param returnResultProblemType the returnResultProblemType to set
     */
    public void setReturnResultProblemType(ReturnResultProblemType returnResultProblemType) {
        this.returnResultProblemType = new ASNReturnResultProblemType();
        this.returnResultProblemType.setType(returnResultProblemType);
    }

    public String getStringValue() {
        StringBuilder sb = new StringBuilder();
        
        ProblemType problemType=null;
    	if(generalProblemType!=null)
    		problemType=ProblemType.General;
    	else if(invokeProblemType!=null)
    		problemType=ProblemType.Invoke;
    	else if(returnErrorProblemType!=null)
    		problemType=ProblemType.ReturnError;
    	else if(returnResultProblemType!=null)
    		problemType=ProblemType.ReturnResult;
    	
    	if(problemType!=null) {
	        switch (problemType) {
	        case General:
	            sb.append("generalProblemType=");
	            try {
					sb.append(this.generalProblemType.getType());
				} catch (ParseException e3) {
				}
	            break;
	        case Invoke:
	            sb.append("invokeProblemType=");
	            try {
					sb.append(this.invokeProblemType.getType());
				} catch (ParseException e2) {
				}
	            break;
	        case ReturnResult:
	            sb.append("returnResultProblemType=");
	            try {
					sb.append(this.returnResultProblemType.getType());
				} catch (ParseException e1) {
				}
	            break;
	        case ReturnError:
	            sb.append("returnErrorProblemType=");
	            try {
					sb.append(this.returnErrorProblemType.getType());
				} catch (ParseException e) {
				}
	            break;
	        }
    	}
        return sb.toString();
    }

    public String toString() {
    	ProblemType problemType=null;
    	if(generalProblemType!=null)
    		problemType=ProblemType.General;
    	else if(invokeProblemType!=null)
    		problemType=ProblemType.Invoke;
    	else if(returnErrorProblemType!=null)
    		problemType=ProblemType.ReturnError;
    	else if(returnResultProblemType!=null)
    		problemType=ProblemType.ReturnResult;
    	
        StringBuilder sb = new StringBuilder();
        sb.append("Problem[type=");
        sb.append(problemType);
        sb.append(" ");
        if(problemType!=null) {
	        switch (problemType) {
	            case General:
	                sb.append("generalProblemType=");
	                sb.append(this.generalProblemType);
	                break;
	            case Invoke:
	                sb.append("invokeProblemType=");
	                sb.append(this.invokeProblemType);
	                break;
	            case ReturnResult:
	                sb.append("returnResultProblemType=");
	                sb.append(this.returnResultProblemType);
	                break;
	            case ReturnError:
	                sb.append("returnErrorProblemType=");
	                sb.append(this.returnErrorProblemType);
	                break;
	        }
        }
        
        sb.append("]");

        return sb.toString();
    }
}
