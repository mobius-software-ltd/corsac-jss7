package org.restcomm.protocols.ss7.inap.api;
import java.util.Map;

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
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;
/**
 * 
 * @author yulianoifa
 *
 */
public interface INAPStack {
   /**
   * Returns the name of this stack
   *
   * @return
   */
   String getName();
   
   INAPProvider getINAPProvider();

   void stop();

   void start() throws Exception;

   TCAPStack getTCAPStack();

   Map<String,Long> getMessagesSentByType();
   
   Map<String,Long> getMessagesReceivedByType();
   
   Map<String,Long> getErrorsSentByType();
   
   Map<String,Long> getErrorsReceivedByType();
   
   Map<String,Long> getDialogsSentByType();
   
   Map<String,Long> getDialogsReceivedByType();
}