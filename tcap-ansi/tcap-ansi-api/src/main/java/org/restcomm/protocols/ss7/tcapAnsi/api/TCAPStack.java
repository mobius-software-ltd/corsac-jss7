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

package org.restcomm.protocols.ss7.tcapAnsi.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public interface TCAPStack {

	public static final long _DIALOG_TIMEOUT = 60000;
    public static final long _INVOKE_TIMEOUT = 30000;
    
    public static final long _EMPTY_INVOKE_TIMEOUT = -1;
    
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
    
    Map<String,Long> getComponentsSentByType();
    
    Map<String,Long> getComponentsReceivedByType();
    
    Map<String,Long> getMessagesSentByType();
    
    Map<String,Long> getMessagesReceivedByType();
    
    Map<String,Long> getRejectsSentByType();
    
    Map<String,Long> getRejectsReceivedByType();
    
    Map<String,Long> getAbortsSentByType();
    
    Map<String,Long> getAbortsReceivedByType();
    
    Long getIncomingDialogsProcessed();
    
    Long getOutgoingDialogsProcessed();
    
    Long getBytesSent();
    
    Long getBytesReceived();
    
    Long getDialogTimeoutProcessed();

	Long getInvokeTimeoutProcessed();
	
	Map<String,Long> getComponentsSentByTypeAndNetwork(Integer networkID);
    
    Map<String,Long> getComponentsReceivedByTypeAndNetwork(Integer networkID);
    
    Map<String,Long> getMessagesSentByTypeAndNetwork(Integer networkID);
    
    Map<String,Long> getMessagesReceivedByTypeAndNetwork(Integer networkID);
    
    Map<String,Long> getRejectsSentByTypeAndNetwork(Integer networkID);
    
    Map<String,Long> getRejectsReceivedByTypeAndNetwork(Integer networkID);
    
    Map<String,Long> getAbortsSentByTypeAndNetwork(Integer networkID);
    
    Map<String,Long> getAbortsReceivedByTypeAndNetwork(Integer networkID);
    
    Long getIncomingDialogsProcessedByNetwork(Integer networkID);
    
    Long getOutgoingDialogsProcessedByNetwork(Integer networkID);
    
    Long getBytesSentByNetwork(Integer networkID);
    
    Long getBytesReceivedByNetwork(Integer networkID);
    
    Long getDialogTimeoutProcessedByNetwork(Integer networkID);

	Long getInvokeTimeoutProcessedByNetwork(Integer networkID);
}