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
package org.restcomm.protocols.ss7.inap.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author yulian.oifa
 *
 */
public enum INAPApplicationContext {
   Core_INAP_CS1_SSP_to_SCP_AC(0),
   Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC(1),
   Core_INAP_CS1_IP_to_SCP_AC(2),
   Core_INAP_CS1_SCP_to_SSP_AC(3),
   Core_INAP_CS1_SCP_to_SSP_traffic_management_AC(4),
   Core_INAP_CS1_SCP_to_SSP_service_management_AC(5),
   Core_INAP_CS1_SSP_to_SCP_service_management_AC(6),
   //Q1218
   Q1218_generic_SSF_to_SCF_AC(100),
   Q1218_DP_specific_SSF_to_SCF_AC(101),
   Q1218_assist_handoff_SSF_to_SCF_AC(102),
   Q1218_SRF_to_SCF_AC(103),
   Q1218_generic_SCF_to_SSF_AC(104),
   Q1218_DP_specific_SCF_to_SSF_AC(105),
   Q1218_SCF_to_SSF_traffic_management_AC(106),
   Q1218_SCF_to_SSF_service_management_AC(107),
   Q1218_SSF_to_SCF_service_management_AC(108),
   Q1218_SCF_to_SSF_status_reporting_AC(109),
   //CS1+
   Ericcson_cs1plus_SSP_TO_SCP_AC(200),
   Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC(201),
   Ericcson_cs1plus_IP_to_SCP_AC(202),
   Ericcson_cs1plus_SCP_to_SSP_AC(203),
   Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC(204),
   Ericcson_cs1plus_SCP_to_SSP_service_management_AC(205),
   Ericcson_cs1plus_SSP_to_SCP_service_management_AC(206),
   Ericcson_cs1plus_data_management_AC(207),
   Ericcson_cs1plus_SCP_to_SSP_traffic_limitation_AC(208),
   //CS1+ REV B
   Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B(210),
   Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B(211),
   Ericcson_cs1plus_IP_to_SCP_AC_REV_B(212),
   Ericcson_cs1plus_SCP_to_SSP_AC_REV_B(213),
   Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC_REV_B(214),
   Ericcson_cs1plus_SCP_to_SSP_service_management_AC_REV_B(215),
   Ericcson_cs1plus_SSP_to_SCP_service_management_AC_REV_B(216),
   Ericcson_cs1plus_data_management_AC_REV_B(217),
   Ericcson_cs1plus_SCP_to_SSP_traffic_limitation_AC_REV_B(218);

   private static long[] oidTemplate = new long[]{0L, 4L, 0L, 1L, 1L, 1L, 0L, 0L};
   private static long[] q1218OidTemplate = new long[]{0L, 0L, 17L, 1218L, 1L, 0L, 0L};
   private static long[] ericssonOidTemplate = new long[]{1L, 2L, 826L, 0L, 1249L, 51L, 1L, 1L, 1L, 0L, 0L};

   // Same as oidTemplate
   private List<Long> res = Arrays.asList(new Long[] { 0L, 4L, 0L, 1L, 1L, 1L, 0L, 0L });
   private List<Long> q1218Res = Arrays.asList(new Long[] { 0L, 0L, 17L, 1218L, 1L, 0L, 0L });
   private List<Long> ericssonRes = Arrays.asList(new Long[] { 1L, 2L, 826L, 0L, 1249L, 51L, 1L, 1L, 1L, 0L, 0L });

   private int code;
   private INAPApplicationContextVersion applicationContextVersion;

   private static final Map<Integer, INAPApplicationContext> intToTypeMap = new HashMap<Integer, INAPApplicationContext>();
	static
	{
	    for (INAPApplicationContext context : INAPApplicationContext.values()) 
	    {
	        intToTypeMap.put(context.code, context);
	    }
	}

    public static INAPApplicationContext getInstance(Integer code) {
    	return intToTypeMap.get(Integer.valueOf(code));
    }
	
   private INAPApplicationContext(int code) {
      this.code = code;
      if (code < 100) {
         this.applicationContextVersion = INAPApplicationContextVersion.cs1;
      } else if (code < 200) {
         this.applicationContextVersion = INAPApplicationContextVersion.q1218;
      } else
    	  this.applicationContextVersion = INAPApplicationContextVersion.cs1plus;
   }

   public static INAPApplicationContext getInstance(List<Long> oid) {
	  if(oid!=null) {
		  switch(oid.size()) {
		  		case 8:
		  			 //REGULAR
			  		 for(int i1 = 0; i1 < oidTemplate.length - 2; ++i1) {
			            if (oid.get(i1) != oidTemplate[i1]) {
			               return null;
			            }
			         }
	
			         if(oid.get(7)!=0L)
			        	 return null;
			         
			         switch(oid.get(6).intValue()) {
			         	case 0:
			         		return Core_INAP_CS1_SSP_to_SCP_AC;
			         	case 1:
			         		return Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC;
			         	case 2:
			         		return Core_INAP_CS1_IP_to_SCP_AC;
			         	case 3:
			         		return Core_INAP_CS1_SCP_to_SSP_AC;
			         	case 4:
			         		return Core_INAP_CS1_SCP_to_SSP_traffic_management_AC;
			         	case 5:
			         		return Core_INAP_CS1_SCP_to_SSP_service_management_AC;
			         	case 6:
			         		return Core_INAP_CS1_SSP_to_SCP_service_management_AC;
			         }      
			  		 break;
		  		case 7:
		  			//Q1218
		  			 for(int i1 = 0; i1 < q1218OidTemplate.length - 2; ++i1) {
			            if (oid.get(i1) != q1218OidTemplate[i1]) {
			               return null;
			            }
			         }
	
			         if(oid.get(6)!=0L)
			        	 return null;
			         
			         int realValue=100+oid.get(5).intValue();
			         switch(realValue) {
			         	case 100:
			         		return Q1218_generic_SSF_to_SCF_AC;
			         	case 101:
			         		return Q1218_DP_specific_SSF_to_SCF_AC;
			         	case 102:
			         		return Q1218_assist_handoff_SSF_to_SCF_AC;
			         	case 103:
			         		return Q1218_SRF_to_SCF_AC;
			         	case 104:
			         		return Q1218_generic_SCF_to_SSF_AC;
			         	case 105:
			         		return Q1218_DP_specific_SCF_to_SSF_AC;
			         	case 106:
			         		return Q1218_SCF_to_SSF_traffic_management_AC;
			         	case 107:
			         		return Q1218_SCF_to_SSF_service_management_AC;
			         	case 108:
			         		return Q1218_SSF_to_SCF_service_management_AC;
			         	case 109:
			         		return Q1218_SCF_to_SSF_status_reporting_AC;
			         }      
			  		 break;	
		  		case 11:
		  			 //ericsson
		  			 for(int i1 = 0; i1 < ericssonOidTemplate.length - 2; ++i1) {
			            if (oid.get(i1) != ericssonOidTemplate[i1]) {
			               return null;
			            }
			         }
	
			         if(oid.get(10)!=0L && oid.get(10)!=1L)
			        	 return null;
			         
			         realValue=200+oid.get(9).intValue() + (int)(oid.get(10)*10);
			         switch(realValue) {
			         	case 200:
			         		return Ericcson_cs1plus_SSP_TO_SCP_AC;
			         	case 201:
			         		return Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC;
			         	case 202:
			         		return Ericcson_cs1plus_IP_to_SCP_AC;
			         	case 203:
			         		return Ericcson_cs1plus_SCP_to_SSP_AC;
			         	case 204:
			         		return Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC;
			         	case 205:
			         		return Ericcson_cs1plus_SCP_to_SSP_service_management_AC;
			         	case 206:
			         		return Ericcson_cs1plus_SSP_to_SCP_service_management_AC;
			         	case 207:
			         		return Ericcson_cs1plus_data_management_AC;
			         	case 208:
			         		return Ericcson_cs1plus_SCP_to_SSP_traffic_limitation_AC;	
			         	case 210:
			         		return Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B;
			         	case 211:
			         		return Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B;
			         	case 212:
			         		return Ericcson_cs1plus_IP_to_SCP_AC_REV_B;
			         	case 213:
			         		return Ericcson_cs1plus_SCP_to_SSP_AC_REV_B;
			         	case 214:
			         		return Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC_REV_B;
			         	case 215:
			         		return Ericcson_cs1plus_SCP_to_SSP_service_management_AC_REV_B;
			         	case 216:
			         		return Ericcson_cs1plus_SSP_to_SCP_service_management_AC_REV_B;
			         	case 217:
			         		return Ericcson_cs1plus_data_management_AC_REV_B;
			         	case 218:
			         		return Ericcson_cs1plus_SCP_to_SSP_traffic_limitation_AC_REV_B;	
			         }      
			  		 break;		
		  }
	  }      
      return null;      
   }

   public int getCode() {
      return this.code;
   }

   public INAPApplicationContextVersion getVersion() {
      return this.applicationContextVersion;
   }

   public List<Long> getOID() {
	   List<Long> result;
	   switch (this) {
			case Core_INAP_CS1_SSP_to_SCP_AC:
				result = new ArrayList<Long>(res);
				result.set(6, 0L);
				break;
			case Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC:
				result = new ArrayList<Long>(res);
				result.set(6, 1L);
				break;
			case Core_INAP_CS1_IP_to_SCP_AC:
				result = new ArrayList<Long>(res);
				result.set(6, 2L);
				break;
			case Core_INAP_CS1_SCP_to_SSP_AC:
				result = new ArrayList<Long>(res);
				result.set(6, 3L);
				break;
			case Core_INAP_CS1_SCP_to_SSP_traffic_management_AC:
				result = new ArrayList<Long>(res);
				result.set(6, 4L);
				break;
			case Core_INAP_CS1_SCP_to_SSP_service_management_AC:
				result = new ArrayList<Long>(res);
				result.set(6, 5L);
				break;
			case Core_INAP_CS1_SSP_to_SCP_service_management_AC:
				result = new ArrayList<Long>(res);
				result.set(6, 6L);
			case Ericcson_cs1plus_SSP_TO_SCP_AC:
				result = new ArrayList<Long>(ericssonRes);
				result.set(9, 0L);
				break;
			case Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC:
				result = new ArrayList<Long>(ericssonRes);
				result.set(9, 1L);
				break;
			case Ericcson_cs1plus_IP_to_SCP_AC:
				result = new ArrayList<Long>(ericssonRes);
				result.set(9, 2L);
				break;
			case Ericcson_cs1plus_SCP_to_SSP_AC:
				result = new ArrayList<Long>(ericssonRes);
				result.set(9, 3L);
				break;
			case Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC:
				result = new ArrayList<Long>(ericssonRes);
				result.set(9, 4L);
				break;
			case Ericcson_cs1plus_SCP_to_SSP_service_management_AC:
				result = new ArrayList<Long>(ericssonRes);
				result.set(9, 5L);
				break;
			case Ericcson_cs1plus_SSP_to_SCP_service_management_AC:
				result = new ArrayList<Long>(ericssonRes);
				result.set(9, 6L);
				break;
			case Ericcson_cs1plus_data_management_AC:
				result = new ArrayList<Long>(ericssonRes);
				result.set(9, 7L);
				break;
			case Ericcson_cs1plus_SCP_to_SSP_traffic_limitation_AC:
				result = new ArrayList<Long>(ericssonRes);
				result.set(9, 8L);
				break;
			case Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B:
				result = new ArrayList<Long>(ericssonRes);
				result.set(9, 0L);
				result.set(10, 1L);
				break;
			case Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B:
				result = new ArrayList<Long>(ericssonRes);
				result.set(9, 1L);
				result.set(10, 1L);
				break;
			case Ericcson_cs1plus_IP_to_SCP_AC_REV_B:
				result = new ArrayList<Long>(ericssonRes);
				result.set(9, 2L);
				result.set(10, 1L);
				break;
			case Ericcson_cs1plus_SCP_to_SSP_AC_REV_B:
				result = new ArrayList<Long>(ericssonRes);
				result.set(9, 3L);
				result.set(10, 1L);
				break;
			case Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC_REV_B:
				result = new ArrayList<Long>(ericssonRes);
				result.set(9, 4L);
				result.set(10, 1L);
				break;
			case Ericcson_cs1plus_SCP_to_SSP_service_management_AC_REV_B:
				result = new ArrayList<Long>(ericssonRes);
				result.set(9, 5L);
				result.set(10, 1L);
				break;
			case Ericcson_cs1plus_SSP_to_SCP_service_management_AC_REV_B:
				result = new ArrayList<Long>(ericssonRes);
				result.set(9, 6L);
				result.set(10, 1L);
				break;
			case Ericcson_cs1plus_data_management_AC_REV_B:
				result = new ArrayList<Long>(ericssonRes);
				result.set(9, 7L);
				result.set(10, 1L);
				break;
			case Ericcson_cs1plus_SCP_to_SSP_traffic_limitation_AC_REV_B:
				result = new ArrayList<Long>(ericssonRes);
				result.set(9, 8L);
				result.set(10, 1L);
				break;
			case Q1218_generic_SSF_to_SCF_AC:
				result = new ArrayList<Long>(q1218Res);
				result.set(5, 0L);
				break;
			case Q1218_DP_specific_SSF_to_SCF_AC:
				result = new ArrayList<Long>(q1218Res);
				result.set(5, 1L);
				break;
			case Q1218_assist_handoff_SSF_to_SCF_AC:
				result = new ArrayList<Long>(q1218Res);
				result.set(5, 2L);
				break;
			case Q1218_SRF_to_SCF_AC:
				result = new ArrayList<Long>(q1218Res);
				result.set(5, 3L);
				break;
			case Q1218_generic_SCF_to_SSF_AC:
				result = new ArrayList<Long>(q1218Res);
				result.set(5, 4L);
				break;
			case Q1218_DP_specific_SCF_to_SSF_AC:
				result = new ArrayList<Long>(q1218Res);
				result.set(5, 5L);
				break;
			case Q1218_SCF_to_SSF_traffic_management_AC:
				result = new ArrayList<Long>(q1218Res);
				result.set(5, 6L);
				break;
			case Q1218_SCF_to_SSF_service_management_AC:
				result = new ArrayList<Long>(q1218Res);
				result.set(5, 7L);
				break;
			case Q1218_SSF_to_SCF_service_management_AC:
				result = new ArrayList<Long>(q1218Res);
				result.set(5, 8L);
				break;
			case Q1218_SCF_to_SSF_status_reporting_AC:
				result = new ArrayList<Long>(q1218Res);
				result.set(5, 9L);
				break;
			default:
				result = new ArrayList<Long>(res);
				break;
	   }

	   return result;
   }

   public String toString() {
      StringBuffer s = new StringBuffer();
      s.append("INAPApplicationContext [Name=");
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
