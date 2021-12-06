/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.map.api.primitives;

import java.util.List;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

import io.netty.buffer.ByteBuf;

/**
 * ExtensionContainer ::= SEQUENCE { privateExtensionList [0]PrivateExtensionList OPTIONAL, pcs-Extensions [1]PCS-Extensions
 * OPTIONAL, ...}
 *
 *
 * @author sergey vetyutnev
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface MAPExtensionContainer {
    /**
     * Get the PrivateExtension list
     *
     * @return
     */
    List<MAPPrivateExtension> getPrivateExtensionList();

    /**
     * Set the PrivateExtension list
     *
     * @param privateExtensionList
     */
    void setPrivateExtensionList(List<MAPPrivateExtension> privateExtensionList);

    /**
     * Get the Pcs-Extensions - ASN.1 encoded byte array
     *
     * @return
     */
    ByteBuf getPcsExtensions();

    /**
     * Set the Pcs-Extensions - ASN.1 encoded byte array
     *
     * @param pcsExtensions
     */
    void setPcsExtensions(ByteBuf pcsExtensions);

}
