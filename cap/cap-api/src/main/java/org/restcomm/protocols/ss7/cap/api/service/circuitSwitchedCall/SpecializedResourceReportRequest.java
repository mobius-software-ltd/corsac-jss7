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

package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall;

/**
 *
<code>
specializedResourceReport OPERATION ::= {
  ARGUMENT SpecializedResourceReportArg
  RETURN RESULT FALSE
  ALWAYS RESPONDS FALSE
  CODE opcode-specializedResourceReport
}
-- Direction: gsmSRF -> gsmSCF, Timer: Tsrr
-- This operation is used as the response to a PlayAnnouncement operation when the announcement
-- completed report indication is set.

CAP V2 & V3: SpecializedResourceReportArg::=NULL

CAP V4: SpecializedResourceReportArg ::= CHOICE {
  allAnnouncementsComplete [50] NULL,
  firstAnnouncementStarted [51] NULL
}
</code>
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface SpecializedResourceReportRequest extends CircuitSwitchedCallMessage {

    boolean getAllAnnouncementsComplete();

    boolean getFirstAnnouncementStarted();
}
