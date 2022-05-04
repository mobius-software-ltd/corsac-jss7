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

package org.restcomm.protocols.ss7.tcap.asn.tx;

import org.restcomm.protocols.ss7.tcap.asn.ASNResultType;
import org.restcomm.protocols.ss7.tcap.asn.ParseException;
import org.restcomm.protocols.ss7.tcap.asn.Result;
import org.restcomm.protocols.ss7.tcap.asn.ResultType;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x02,constructed=true,lengthIndefinite=false)
public class ResultImpl implements Result {
	private ASNResultType resultType;

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.Result#getResultType()
     */
    public ResultType getResultType() throws ParseException {
    	if(resultType==null)
    		return null;
    	
        return resultType.getType();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.Result#setResultType(org.restcomm .protocols.ss7.tcap.asn.ResultType)
     */
    public void setResultType(ResultType t) {
    	if(t!=null)
    		this.resultType = new ASNResultType(t);
    	else
    		this.resultType = null;
    }

    public String toString() {
    	ResultType realType=null;
    	try {
    		realType=getResultType();
    	}
    	catch(ParseException ex) {
    		
    	}
    	
        return "Result[resultType=" + realType + "]";
    }
}