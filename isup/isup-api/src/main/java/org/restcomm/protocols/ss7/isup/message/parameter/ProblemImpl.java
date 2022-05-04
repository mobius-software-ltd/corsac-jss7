package org.restcomm.protocols.ss7.isup.message.parameter;

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

import org.restcomm.protocols.ss7.isup.ParameterException;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=0x00,constructed=false,lengthIndefinite=false)
public class ProblemImpl {
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
    public GeneralProblemType getGeneralProblemType() throws ParameterException {
    	if(generalProblemType==null)
    		return null;
    	
        return generalProblemType.getType();
    }

    /**
     * @param generalProblemType the generalProblemType to set
     */
    public void setGeneralProblemType(GeneralProblemType generalProblemType) {
        this.generalProblemType = new ASNGeneralProblemType(generalProblemType);        
    }

    /**
     * @return the invokeProblemType
     * @throws ParseException 
     */
    public InvokeProblemType getInvokeProblemType() throws ParameterException {
    	if(invokeProblemType==null)
    		return null;
    	
        return invokeProblemType.getType();
    }

    /**
     * @param invokeProblemType the invokeProblemType to set
     */
    public void setInvokeProblemType(InvokeProblemType invokeProblemType) {
        this.invokeProblemType = new ASNInvokeProblemType(invokeProblemType);        
    }

    /**
     * @return the returnErrorProblemType
     * @throws ParseException 
     */
    public ReturnErrorProblemType getReturnErrorProblemType() throws ParameterException {
    	if(returnErrorProblemType==null)
    		return null;
    	
        return returnErrorProblemType.getType();
    }

    /**
     * @param returnErrorProblemType the returnErrorProblemType to set
     */
    public void setReturnErrorProblemType(ReturnErrorProblemType returnErrorProblemType) {
        this.returnErrorProblemType = new ASNReturnErrorProblemType(returnErrorProblemType);        
    }

    /**
     * @return the returnResultProblemType
     * @throws ParseException 
     */
    public ReturnResultProblemType getReturnResultProblemType() throws ParameterException {
    	if(returnResultProblemType==null)
    		return null;
    	
        return returnResultProblemType.getType();
    }

    /**
     * @param returnResultProblemType the returnResultProblemType to set
     */
    public void setReturnResultProblemType(ReturnResultProblemType returnResultProblemType) {
        this.returnResultProblemType = new ASNReturnResultProblemType(returnResultProblemType);        
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
				} catch (ParameterException e3) {
				}
	            break;
	        case Invoke:
	            sb.append("invokeProblemType=");
	            try {
					sb.append(this.invokeProblemType.getType());
				} catch (ParameterException e2) {
				}
	            break;
	        case ReturnResult:
	            sb.append("returnResultProblemType=");
	            try {
					sb.append(this.returnResultProblemType.getType());
				} catch (ParameterException e1) {
				}
	            break;
	        case ReturnError:
	            sb.append("returnErrorProblemType=");
	            try {
					sb.append(this.returnErrorProblemType.getType());
				} catch (ParameterException e) {
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
