package org.restcomm.protocols.ss7.inap.api;

import org.restcomm.protocols.ss7.tcap.api.TCAPStack;

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
}