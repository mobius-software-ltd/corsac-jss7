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

package org.restcomm.protocols.ss7.isup.message.parameter;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public interface MessageCompatibilityInstructionIndicator {
    /**
     * See Q.763 3.33 Transit at intermediate exchange indicator : transit interpretation
     */
    boolean _TI_TRANSIT_INTEPRETATION = false;
    /**
     * See Q.763 3.33 Transit at intermediate exchange indicator :
     */
    boolean _TI_ETE_INTEPRETATION = true;

    /**
     * See Q.763 3.33 Release call indicator : do not release
     */
    boolean _RCI_DO_NOT_RELEASE = false;
    /**
     * See Q.763 3.33 Release call indicator : reelase call
     */
    boolean _RCI_RELEASE = true;

    /**
     * See Q.763 3.33 Send notification indicator: do not send notification
     */
    boolean _SNDI_DO_NOT_SEND_NOTIFIACTION = false;
    /**
     * See Q.763 3.33 Send notification indicator: send notification
     */
    boolean _SNDI_SEND_NOTIFIACTION = true;

    /**
     * See Q.763 3.33 Discard message indicator : do not discard message (pass on)
     */
    boolean _DMI_DO_NOT_DISCARD = false;
    /**
     * See Q.763 3.33 Discard message indicator : discard message
     */
    boolean _DMI_DISCARD = true;

    /**
     * See Q.763 3.33 Pass on not possible indicator: release call
     */
    boolean _PNPI_RELEASE_CALL = false;
    /**
     * See Q.763 3.33 Pass on not possible indicator: discard information
     */
    boolean _PNPI_DISCARD_INFORMATION = true;

    /**
     * See Q.763 3.41 Broadband/narrowband interworking indicator : pass on
     */
    int _BII_PASS_ON = 0;

    /**
     * See Q.763 3.41 Broadband/narrowband interworking indicator : discard message
     */
    int _BII_DISCARD_MESSAGE = 1;

    /**
     * See Q.763 3.41 Broadband/narrowband interworking indicator : release call
     */
    int _BII_RELEASE_CALL = 2;

    /**
     * See Q.763 3.41 Broadband/narrowband interworking indicator : reserved
     */
    int _BII_RESERVED = 3;

    boolean isTransitAtIntermediateExchangeIndicator();

    void setTransitAtIntermediateExchangeIndicator(boolean transitAtIntermediateExchangeIndicator);

    boolean isReleaseCallIndicator();

    void setReleaseCallIndicator(boolean releaseCallindicator);

    boolean isSendNotificationIndicator();

    void setSendNotificationIndicator(boolean sendNotificationIndicator);

    boolean isDiscardMessageIndicator();

    void setDiscardMessageIndicator(boolean discardMessageIndicator);

    boolean isPassOnNotPossibleIndicator();

    void setPassOnNotPossibleIndicator(boolean discardMessageIndicator);

    int getBandInterworkingIndicator();

    void setBandInterworkingIndicator(int bandInterworkingIndicator);
}
