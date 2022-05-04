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

package org.restcomm.protocols.ss7.sccp.message;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.sccp.parameter.Importance;
import org.restcomm.protocols.ss7.sccp.parameter.ReturnCause;
import org.restcomm.protocols.ss7.sccp.parameter.Segmentation;

/**
 *
 * This interface represents SCCP a connectionless notice message (UDTS, XUDTS and LUDTS)
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface SccpNoticeMessage extends SccpAddressedMessage {

    ReturnCause getReturnCause();

    ByteBuf getData();

    Segmentation getSegmentation();

    Importance getImportance();

    void setReturnCause(ReturnCause rc);

    void setData(ByteBuf data);

    void setImportance(Importance p);

}
