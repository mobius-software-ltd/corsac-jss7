package org.restcomm.protocols.ss7.commonapp.api;

import java.util.Map;

import org.restcomm.protocols.ss7.tcap.api.TCAPStack;

public interface CommonAppStack<T>
{
	/**
     * Returns the name of this stack
     *
     * @return
     */
    String getName();

    String getProtocol();
	
	T getProvider();
    
    void stop();
	
    void start() throws Exception;

    TCAPStack getTCAPStack();

    Map<String,Long> getMessagesSentByType();
    
    Map<String,Long> getMessagesReceivedByType();
    
    Map<String,Long> getErrorsSentByType();
    
    Map<String,Long> getErrorsReceivedByType();
    
    Map<String,Long> getDialogsSentByType();
    
    Map<String,Long> getDialogsReceivedByType();  
    
    Map<String,Long> getMessagesSentByTypeAndNetwork(int networkID);
    
    Map<String,Long> getMessagesReceivedByTypeAndNetwork(int networkID);
    
    Map<String,Long> getErrorsSentByTypeAndNetwork(int networkID);
    
    Map<String,Long> getErrorsReceivedByTypeAndNetwork(int networkID);
    
    Map<String,Long> getDialogsSentByTypeAndNetwork(int networkID);
    
    Map<String,Long> getDialogsReceivedByTypeAndNetwork(int networkID);
}
