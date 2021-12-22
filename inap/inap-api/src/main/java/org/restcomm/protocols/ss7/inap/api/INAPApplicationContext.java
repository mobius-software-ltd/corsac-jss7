/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.restcomm.protocols.ss7.inap.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
   Core_INAP_CS1_SSP_to_SCP_service_management_AC(6);

   private static long[] oidTemplate = new long[]{0L, 4L, 0L, 1L, 1L, 1L, 0L, 0L};

   // Same as oidTemplate
   private List<Long> res = Arrays.asList(new Long[] { 0L, 4L, 0L, 1L, 1L, 1L, 0L, 0L });

   private int code;
   private INAPApplicationContextVersion applicationContextVersion;

   private INAPApplicationContext(int code) {
      this.code = code;
      if (code < 6) {
         this.applicationContextVersion = INAPApplicationContextVersion.cs1;
      } else {
         this.applicationContextVersion = INAPApplicationContextVersion.cs1;
      }

   }

   public static INAPApplicationContext getInstance(List<Long> oid) {
      if (oid != null && oid.size() == oidTemplate.length) {
         for(int i1 = 0; i1 < oidTemplate.length - 3; ++i1) {
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
	   List<Long> result=new ArrayList<Long>(res);
   	switch(this) {
      case Core_INAP_CS1_SSP_to_SCP_AC:
         res.set(6,0L);
         break;
      case Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC:
         res.set(6,1L);
         break;
      case Core_INAP_CS1_IP_to_SCP_AC:
         res.set(6,2L);
         break;
      case Core_INAP_CS1_SCP_to_SSP_AC:
         res.set(6,3L);
         break;
      case Core_INAP_CS1_SCP_to_SSP_traffic_management_AC:
         res.set(6,4L);
         break;
      case Core_INAP_CS1_SCP_to_SSP_service_management_AC:
         res.set(6,5L);
         break;
      case Core_INAP_CS1_SSP_to_SCP_service_management_AC:
         res.set(6,6L);
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
