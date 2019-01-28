/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.tcapAnsi.api;

import java.util.Collection;
import java.util.List;

/**
 * @author baranowb
 *
 */
public interface TCAPStack {

    /**
     * Returns the name of this stack
     *
     * @return
     */
    String getName();

    /**
     * Set the persist directory to store the xml files
     *
     * @return
     */
    String getPersistDir();

    int getSubSystemNumber();

    /**
     * Returns stack provider.
     *
     * @return
     */
    TCAPProvider getProvider();

    /**
     * Stops this stack and transport layer(SCCP)
     */
    void stop();

    /**
     * Start stack and transport layer(SCCP)
     *
     * @throws IllegalStateException - if stack is already running or not configured
     * @throws StartFailedException
     */
    void start() throws Exception;

    boolean isStarted();

    /**
     * Sets millisecond value for dialog timeout. It specifies how long dialog can be idle - not receive/send any messages.
     *
     * @param l
     */
    void setDialogIdleTimeout(long l) throws Exception;

    long getDialogIdleTimeout();

    void setInvokeTimeout(long v) throws Exception;

    long getInvokeTimeout();

    /**
     * Sets the start of the range of the generated dialog ids.
     */
    void setDialogIdRangeStart(long val) throws Exception;

    /**
     * Sets the start of the range of the generated dialog ids.
     */
    void setDialogIdRangeEnd(long val) throws Exception;

    /**
     *
     * @return starting dialog id within the range
     */
    long getDialogIdRangeStart();

    /**
     *
     * @return ending dialog id within the range
     */
    long getDialogIdRangeEnd();

    /**
     * previewMode is needed for special processing mode When PreviewMode in TCAP level we have: - we only listern incoming
     * messages and sends nothing. send(), close(), sendComponent() and other such methods do nothing. - A TCAP Dialog is
     * temporary. TCAP Dialog is discarded after any icoming message like TC-BEGIN or TC-CONTINUE has been processed - for any
     * incoming messages (including TC-CONTINUE, TC-END, TC-ABORT) a new TCAP Dialog is created (end then deleted). - no timers
     * and timeouts
     *
     * default state: no previewMode
     */
    void setPreviewMode(boolean val) throws Exception;

    /**
     *
     * @return if areviewMode is active
     */
    boolean getPreviewMode();

    void setExtraSsns(List<Integer> extraSsnsNew) throws Exception;

    Collection<Integer> getExtraSsns();

    boolean isExtraSsnPresent(int ssn);

    String getSubSystemNumberList();

    /** Set value for slsRange for this TCAP Stack.
     *
     * @param val
     */
    void setSlsRange(String val) throws Exception;

    /**
     * Returns the SlsRange that this TCAP Stack is registered for
     *
     * @return
     */
    String getSlsRange();

    /**
     * Returns true if there is need to swap bytes for Txid
     *
     * @return
     */
    boolean getSwapTcapIdBytes();

    /**
     * Set is there need to swap bytes for Txid
     *
     * @return
     */
    void setSwapTcapIdBytes(boolean isSwapTcapIdBytes);
}