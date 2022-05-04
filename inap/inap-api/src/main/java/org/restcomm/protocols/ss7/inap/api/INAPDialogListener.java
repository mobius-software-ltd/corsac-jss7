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

import org.restcomm.protocols.ss7.inap.api.dialog.INAPGeneralAbortReason;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPNoticeProblemDiagnostic;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPUserAbortReason;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
/**
 * @author yulian.oifa
 *
 */
public interface INAPDialogListener {
   /**
   * Called after all components has been processed.
   */
   void onDialogDelimiter(INAPDialog inapDialog);

   /**
   * When TC-BEGIN received. If INAP user rejects this dialog it should call INAPDialog.abort()
   */
   void onDialogRequest(INAPDialog inapDialog);

   /**
   * When TC-CONTINUE or TC-END received with dialogueResponse DialoguePDU (AARE-apdu) (dialog accepted) this is called before
   * ComponentPortion is called
   */
   void onDialogAccept(INAPDialog inapDialog);

   /**
   * When TC-ABORT received with user abort userReason is defined only if generalReason=UserSpecific
   */
   void onDialogUserAbort(INAPDialog inapDialog, INAPGeneralAbortReason generalReason, INAPUserAbortReason userReason);

   /**
   * When TC-ABORT received with provider abort
   *
   */
   void onDialogProviderAbort(INAPDialog inapDialog, PAbortCauseType abortCause);

   /**
   * When TC-END received
   */
   void onDialogClose(INAPDialog inapDialog);

   /**
   * Called when the INAP Dialog has been released
   *
   * @param inapDialog
   */
   void onDialogRelease(INAPDialog inapDialog);

   /**
   * Called when the INAP Dialog is about to aborted because of TimeOut
   *
   * @param inapDialog
   */
   void onDialogTimeout(INAPDialog inapDialog);

   /**
   * Called to notice of abnormal cases
   *
   */
   void onDialogNotice(INAPDialog capDialog, INAPNoticeProblemDiagnostic noticeProblemDiagnostic);
}
