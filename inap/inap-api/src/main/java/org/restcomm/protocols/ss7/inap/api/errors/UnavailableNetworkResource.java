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

package org.restcomm.protocols.ss7.inap.api.errors;

/**
 *
 UnavailableNetworkResource ::= ENUMERATED { unavailableResources (0), componentFailure (1), basicCallProcessingException (2),
 * resourceStatusFailure (3), endUserFailure (4) } -- Indicates the network resource that failed.
 *
 *
 * @author yulian.oifa
 *
 */
public enum UnavailableNetworkResource {
   unavailableResources(0),
   componentFailure(1),
   basicCallProcessingException(2),
   resourceStatusFailure(3),
   endUserFailure(4);

   private int code;

   private UnavailableNetworkResource(int code) {
      this.code = code;
   }

   public int getCode() {
      return this.code;
   }

   public static UnavailableNetworkResource getInstance(int code) {
      switch(code) {
      case 0:
         return unavailableResources;
      case 1:
         return componentFailure;
      case 2:
         return basicCallProcessingException;
      case 3:
         return resourceStatusFailure;
      case 4:
         return endUserFailure;
      default:
         return null;
      }
   }
}
