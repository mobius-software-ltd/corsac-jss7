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
 * defined for CS1+ only
 PARAMETER ENUMERATED { noInformationReceived(0), notEnoughInformationReceived (1)
}
 *
 * @author yulian.oifa
 *
 */
public enum ImproperCallerResponseParameter {
	noInformationReceived(1),
	notEnoughInformationReceived(2);

   private int code;

   private ImproperCallerResponseParameter(int code) {
      this.code = code;
   }

   public int getCode() {
      return this.code;
   }

   public static ImproperCallerResponseParameter getInstance(int code) {
      switch(code) {
      case 1:
         return noInformationReceived;
      case 2:
         return notEnoughInformationReceived;
      default:
         return null;
      }
   }
}