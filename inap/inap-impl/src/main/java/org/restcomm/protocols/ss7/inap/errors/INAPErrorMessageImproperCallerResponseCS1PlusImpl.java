/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.inap.errors;

import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorCode;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageImproperCallerResponseCS1Plus;
import org.restcomm.protocols.ss7.inap.api.errors.ImproperCallerResponseParameter;

/**
 *
 * @author yulian.oifa
 *
 */
public class INAPErrorMessageImproperCallerResponseCS1PlusImpl extends EnumeratedINAPErrorMessage1Impl implements INAPErrorMessageImproperCallerResponseCS1Plus {
	protected INAPErrorMessageImproperCallerResponseCS1PlusImpl(ImproperCallerResponseParameter improperCallerResponseParameter) {
        super(INAPErrorCode.systemFailure,"SystemFailure",1,2);

        if(improperCallerResponseParameter!=null)
        	setValue(improperCallerResponseParameter.getCode());        
    }

    public INAPErrorMessageImproperCallerResponseCS1PlusImpl() {
        super(INAPErrorCode.systemFailure,"SystemFailure",1,2);
    }

    public boolean isImproperCallerResponseCs1Plus() {
        return true;
    }

    public INAPErrorMessageImproperCallerResponseCS1Plus getImproperCallerResponseCS1Plus() {
        return this;
    }

    @Override
    public ImproperCallerResponseParameter getImproperCallerResponse() {
    	Integer value=getValue();
    	if(value==null)
    		return null;
    	
    	return ImproperCallerResponseParameter.getInstance(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("INAPErrorMessageSystemFailure [");
        ImproperCallerResponseParameter improperCallerResponseParameter=getImproperCallerResponse();
        if (improperCallerResponseParameter != null) {
            sb.append("improperCallerResponseParameter=");
            sb.append(improperCallerResponseParameter);
            sb.append(",");
        }
        sb.append("]");

        return sb.toString();
    }
}
