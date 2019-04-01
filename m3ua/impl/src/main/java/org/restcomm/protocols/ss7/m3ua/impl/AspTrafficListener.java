package org.restcomm.protocols.ss7.m3ua.impl;

import io.netty.buffer.ByteBuf;

/**
 * @author OAfanasiev
 */
public interface AspTrafficListener {
    void onAspMessage(String aspName, ByteBuf data);
}
