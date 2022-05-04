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

package org.restcomm.protocols.ss7.mtp;

import java.io.IOException;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface Mtp3UserPart {

    void start() throws Exception;

    void stop() throws Exception;

    /**
     * Add {@link Mtp3UserPartListener}
     *
     * @param listener
     */
    void addMtp3UserPartListener(Mtp3UserPartListener listener);

    /**
     * Remove {@link Mtp3UserPartListener}
     *
     * @param listener
     */
    void removeMtp3UserPartListener(Mtp3UserPartListener listener);

    /**
     * Get the Mtp3TransferPrimitiveFactory
     *
     * @return
     */
    Mtp3TransferPrimitiveFactory getMtp3TransferPrimitiveFactory();

    /**
     * Return the maximum data field length of the MTP-TRANSFER message to the DPC
     *
     * @param dpc
     * @return
     */
    int getMaxUserDataLength(int dpc);

    /**
     * If message delivering failed: MTP-PAUSE or MTR-STATUS indication will be sent
     *
     * @param msg
     *
     */
    void sendMessage(Mtp3TransferPrimitive msg) throws IOException;


    /**
     * return PointCodeFormat
     *
     * @return
     */
    RoutingLabelFormat getRoutingLabelFormat();

    /**
     * Set PointCodeFormat
     *
     * @param length
     */
    void setRoutingLabelFormat(RoutingLabelFormat routingLabelFormat) throws Exception;

    /**
     * Returns true if lowest bit of SLS is used for loadbalancing between Linkset else returns false
     *
     * @return
     */
    boolean isUseLsbForLinksetSelection();

    /**
     * If set to true, lowest bit of SLS is used for loadbalancing between Linkset else highest bit of SLS is used.
     *
     * @param useLsbForLinksetSelection
     */
    void setUseLsbForLinksetSelection(boolean useLsbForLinksetSelection) throws Exception;

    /**
     * @return
     */
    int getDeliveryMessageThreadCount();

    /**
     * @param deliveryMessageThreadCount
     */
    void setDeliveryMessageThreadCount(int deliveryMessageThreadCount) throws Exception;
}
