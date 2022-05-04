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

package org.restcomm.protocols.ss7.tcapAnsi.api;

import java.util.List;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformationElement;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCPAbortIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortRequest;

/**
 *
 * * @author baranowb
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public interface DialogPrimitiveFactory {

    TCQueryRequest createQuery(Dialog d, boolean dialogTermitationPermission);

    TCConversationRequest createConversation(Dialog d, boolean dialogTermitationPermission);

    TCResponseRequest createResponse(Dialog d);

    TCUserAbortRequest createUAbort(Dialog d);

    TCQueryIndication createQueryIndication(Dialog d, boolean dialogTermitationPermission);
    
    TCConversationIndication createConversationIndication(Dialog d, boolean dialogTermitationPermission);
    
    TCResponseIndication createResponseIndication(Dialog d);
    
    TCUserAbortIndication createUAbortIndication(Dialog d);

    TCPAbortIndication createPAbortIndication(Dialog d);
    	
    TCUniIndication createUniIndication(Dialog d);
    
    TCUniRequest createUni(Dialog d);

    ApplicationContext createApplicationContext(List<Long> val);

    ApplicationContext createApplicationContext(int val);

    UserInformation createUserInformation();

    UserInformationElement createUserInformationElement();

}