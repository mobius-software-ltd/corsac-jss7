 /*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
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
package org.restcomm.protocols.ss7.cap.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sergey vetyutnev
 * @author yulian.oifa
 *
 */
public enum CAPApplicationContext {
    CapV1_gsmSSF_to_gsmSCF(11),

    CapV2_gsmSSF_to_gsmSCF(21), CapV2_assistGsmSSF_to_gsmSCF(22), CapV2_gsmSRF_to_gsmSCF(24),

    CapV3_gsmSSF_scfGeneric(31), CapV3_gsmSSF_scfAssistHandoff(32), CapV3_gsmSRF_gsmSCF(34), CapV3_gprsSSF_gsmSCF(35), CapV3_gsmSCF_gprsSSF(
            36), CapV3_cap3_sms(37),

    CapV4_gsmSSF_scfGeneric(41), CapV4_gsmSSF_scfAssistHandoff(42), CapV4_scf_gsmSSFGeneric(43), CapV4_gsmSRF_gsmSCF(44), CapV4_cap4_sms(
            48);

    private static long[] oidTemplate = new long[] { 0, 4, 0, 0, 1, 0, 0, 0 };

    // Same as oidTemplate
    private List<Long> res = Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 0L, 0L });

    private int code;
    private CAPApplicationContextVersion applicationContextVersion;

    private static final Map<Integer, CAPApplicationContext> intToTypeMap = new HashMap<Integer, CAPApplicationContext>();
	static
	{
	    for (CAPApplicationContext context : CAPApplicationContext.values()) 
	    {
	        intToTypeMap.put(context.code, context);
	    }
	}

    public static CAPApplicationContext getInstance(Integer code) {
    	return intToTypeMap.get(Integer.valueOf(code));
    }
    
    private CAPApplicationContext(int code) {
        this.code = code;

        if (code == 11) {
            this.applicationContextVersion = CAPApplicationContextVersion.version1;
        } else if (code < 30) {
            this.applicationContextVersion = CAPApplicationContextVersion.version2;
        } else if (code < 40) {
            this.applicationContextVersion = CAPApplicationContextVersion.version3;
        } else {
            this.applicationContextVersion = CAPApplicationContextVersion.version4;
        }
    }

    public static CAPApplicationContext getInstance(List<Long> oid) {

        if (oid == null || oid.size() != oidTemplate.length)
            return null;
        for (int i1 = 0; i1 < oidTemplate.length - 3; i1++) {
            if (oid.get(i1) != oidTemplate[i1])
                return null;
        }

        switch(oid.get(5).intValue()) {
        	case 0:
                switch(oid.get(6).intValue()) {
                	case 50:
                		switch(oid.get(7).intValue()) {
                			case 0:
                				return CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF;
                			case 1:
                				return CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;
                		}
                		break;
                	case 51:
                		switch(oid.get(7).intValue()) {
            				case 1:
            					return CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF;
                		}
                		break;
                	case 52:
                		switch(oid.get(7).intValue()) {
	        				case 1:
	        					return CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF;
	            		}
	            		break;
                }
        		break;
        	case 20:
        		switch(oid.get(6).intValue()) {
        			case 3:
	            		switch(oid.get(7).intValue()) {
	        				case 14:
	        					return CAPApplicationContext.CapV3_gsmSRF_gsmSCF;
	            		}
	            		break;
        		}
        		break;
        	case 21:
        		switch(oid.get(6).intValue()) {
	    			case 3:
	            		switch(oid.get(7).intValue()) {
	        				case 4:
	        					return CAPApplicationContext.CapV3_gsmSSF_scfGeneric;
	        				case 6:
	        					return CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff;
	        				case 50:
	        					return CAPApplicationContext.CapV3_gprsSSF_gsmSCF;
	        				case 51:
	        					return CAPApplicationContext.CapV3_gsmSCF_gprsSSF;
	        				case 61:
	        					return CAPApplicationContext.CapV3_cap3_sms;
	            		}
	            		break;
	    		}
        		break;
        	case 22:
        		switch(oid.get(6).intValue()) {
        			case 3:
	            		switch(oid.get(7).intValue()) {
	        				case 14:
	        					return CAPApplicationContext.CapV4_gsmSRF_gsmSCF;
	            		}
	            		break;
        		}
        		break;
        	case 23:
        		switch(oid.get(6).intValue()) {
	    			case 3:
	            		switch(oid.get(7).intValue()) {
	        				case 4:
	        					return CAPApplicationContext.CapV4_gsmSSF_scfGeneric;
	        				case 6:
	        					return CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff;
	        				case 8:
	        					return CAPApplicationContext.CapV4_scf_gsmSSFGeneric;
	        				case 61:
	        					return CAPApplicationContext.CapV4_cap4_sms;
	            		}
	            		break;
	    		}
        		break;
        }

        return null;
    }

    public int getCode() {
        return this.code;
    }

    public CAPApplicationContextVersion getVersion() {
        return this.applicationContextVersion;
    }

    public List<Long> getOID() {
    	List<Long> result=new ArrayList<Long>(res);
    	
    	switch (this) {
            case CapV1_gsmSSF_to_gsmSCF:
                result.set(5,0L);
                result.set(6,50L);
                result.set(7,0L);
                break;

            case CapV2_gsmSSF_to_gsmSCF:
            	result.set(5,0L);
            	result.set(6,50L);
            	result.set(7,1L);
                break;
            case CapV2_assistGsmSSF_to_gsmSCF:
            	result.set(5,0L);
            	result.set(6,51L);
            	result.set(7,1L);
                break;
            case CapV2_gsmSRF_to_gsmSCF:
            	result.set(5,0L);
            	result.set(6,52L);
            	result.set(7,1L);
                break;

            case CapV3_gsmSSF_scfGeneric:
            	result.set(5,21L);
            	result.set(6,3L);
            	result.set(7,4L);
                break;
            case CapV3_gsmSSF_scfAssistHandoff:
            	result.set(5,21L);
            	result.set(6,3L);
            	result.set(7,6L);
                break;
            case CapV3_gsmSRF_gsmSCF:
            	result.set(5,20L);
                result.set(6,3L);
                result.set(7,14L);
                break;
            case CapV3_gprsSSF_gsmSCF:
                result.set(5,21L);
                result.set(6,3L);
                result.set(7,50L);
                break;
            case CapV3_gsmSCF_gprsSSF:
                result.set(5,21L);
                result.set(6,3L);
                result.set(7,51L);
                break;
            case CapV3_cap3_sms:
                result.set(5,21L);
                result.set(6,3L);
                result.set(7,61L);
                break;

            case CapV4_gsmSSF_scfGeneric:
                result.set(5,23L);
                result.set(6,3L);
                result.set(7,4L);
                break;
            case CapV4_gsmSSF_scfAssistHandoff:
                result.set(5,23L);
                result.set(6,3L);
                result.set(7,6L);
                break;
            case CapV4_scf_gsmSSFGeneric:
                result.set(5,23L);
                result.set(6,3L);
                result.set(7,8L);
                break;
            case CapV4_gsmSRF_gsmSCF:
                result.set(5,22L);
                result.set(6,3L);
                result.set(7,14L);
                break;
            case CapV4_cap4_sms:
                result.set(5,23L);
                result.set(6,3L);
                result.set(7,61L);
                break;
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();

        s.append("CAPApplicationContext [Name=");
        s.append(super.toString());
        s.append(", Version=");
        s.append(this.applicationContextVersion.toString());
        s.append(", Oid=");
        for (long l : this.getOID()) {
            s.append(l).append(", ");
        }
        s.append("]");

        return s.toString();
    }
}
