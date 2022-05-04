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
package org.restcomm.protocols.ss7.inap.api.dialog;

/**
 *
 * @author yulian.oifa
 *
 */
public enum INAPGeneralAbortReason {
	/**
     * Application context name does not supported
     */
    ACNNotSupported(0),
    /**
     * Other part has been refused the Dialog with AARE apdu with abortReason=null or abortReason=no-reason-given
     */
    DialogRefused(1),
    /**
     * Other part has been refused the Dialog with AARE apdu with abortReason=null or abortReason=NoCommonDialogPortion
     */
    NoCommonDialogPortionReceived(2),
    /**
     * User abort, CAPUserAbortReason is present in the message
     */
    UserSpecific(3);

    private int code;

   private INAPGeneralAbortReason(int code) {
      this.code = code;
   }

   public static INAPGeneralAbortReason getInstance(int code) {
      switch(code) {
      case 0:
         return ACNNotSupported;
      case 1:
         return DialogRefused;
      case 2:
         return NoCommonDialogPortionReceived;
      case 3:
         return UserSpecific;
      default:
         return null;
      }
   }

   public int getCode() {
      return this.code;
   }
}
