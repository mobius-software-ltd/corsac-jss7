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

package org.restcomm.protocols.ss7.m3ua.impl.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import org.junit.Test;
/**
 * 
 * @author yulianoifa
 *
 */
public class ByteBufTest {
    @Test
    public void testSingleAspInAs() throws Exception {
        try {
            CompositeByteBuf comp = Unpooled.compositeBuffer();
            //ByteBuf top = comp;

            ByteBuf b1 = Unpooled.buffer();
            ByteBuf b2 = Unpooled.buffer();
            ByteBuf b3 = Unpooled.buffer();
            b1.writeByte(1);
            b1.writeByte(2);
            b1.writeByte(3);
            b2.writeByte(11);
            b2.writeByte(12);
            b3.writeByte(21);
            b3.writeByte(22);
            b3.writeByte(23);
            b3.writeByte(24);

            comp.addComponent(b1);
            comp.addComponent(b2);
            comp.writerIndex(comp.capacity());

            byte[] bb = new byte[4];
            comp.readBytes(bb);

            comp.discardReadBytes();

            comp.addComponent(b3);
            comp.writerIndex(comp.capacity());

            byte[] bb2 = new byte[5];
            comp.readBytes(bb2);

            comp.discardReadBytes();
            //int xxx = 0;
            //xxx++;
        } catch (Exception ee) {
            //int iii = 10;
        }
    }
}
