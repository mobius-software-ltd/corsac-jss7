/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
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

package org.restcomm.protocols.ss7.isup.message.parameter;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public interface RedirectReason {

    byte REASON_UNKNOWN = 0x0;
    byte REASON_SERVICE_PROVIDER_PORTABILITY = 0x01;
    byte REASON_RESERVED_FOR_LOCATION_PORTABILITY = 0x02;
    byte REASON_RESERVED_FOR_SERVICE_PORTABILITY = 0x03;
    byte PERFORMING_EXCANGE_NO_INDICATION = 0x0;
    byte PERFORMING_EXCANGE_REDIRECT_POSSIBLE_BEFORE_ACM = 0x1;
    byte PERFORMING_EXCANGE_REDIRECT_POSSIBLE_BEFORE_ANM = 0x2;
    byte PERFORMING_EXCANGE_REDIRECT_POSSIBLE_ANY_TIME = 0x3;

    byte getRedirectReason();

    void setRedirectReason(byte b);

    byte getRedirectPossibleAtPerformingExchange();

    void setRedirectPossibleAtPerformingExchange(byte b);
}
