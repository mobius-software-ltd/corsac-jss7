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
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageTaskRefused;
import org.restcomm.protocols.ss7.inap.api.errors.TaskRefusedParameter;

/**
 *
 * @author yulian.oifa
 *
 */
public class INAPErrorMessageTaskRefusedImpl extends EnumeratedINAPErrorMessage1Impl implements INAPErrorMessageTaskRefused {
	protected INAPErrorMessageTaskRefusedImpl(TaskRefusedParameter taskRefusedParameter) {
        super(INAPErrorCode.taskRefused,"TaskRefused",0,2);

        if(taskRefusedParameter!=null)
        	setValue(taskRefusedParameter.getCode());
    }

    public INAPErrorMessageTaskRefusedImpl() {
        super(INAPErrorCode.taskRefused,"TaskRefused",0,2);
    }

    public boolean isEmTaskRefused() {
        return true;
    }

    public INAPErrorMessageTaskRefused getEmTaskRefused() {
        return this;
    }

    public TaskRefusedParameter getTaskRefusedParameter() {
    	Integer value=getValue();
    	if(value==null)
    		return null;
    	
    	return TaskRefusedParameter.getInstance(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("INAPErrorMessageTaskRefused [");
        TaskRefusedParameter taskRefusedParameter=getTaskRefusedParameter();
        if (taskRefusedParameter != null) {
            sb.append("taskRefusedParameter=");
            sb.append(taskRefusedParameter);
            sb.append(",");
        }
        sb.append("]");

        return sb.toString();
    }
}
