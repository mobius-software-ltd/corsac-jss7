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
package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ROVolumeIfTariffSwitchImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.TransferVolumeRollOverWrapperImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.TransferredVolumeRollOverImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class TransferredVolumeRollOverTest {

    public byte[] getData() {
        return new byte[] { 48, 3, -128, 1, 25 };
    };

    public byte[] getData2() {
        return new byte[] { 48, 8, -95, 6, -128, 1, 12, -127, 1, 24 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TransferVolumeRollOverWrapperImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TransferVolumeRollOverWrapperImpl);
        
        TransferVolumeRollOverWrapperImpl prim = (TransferVolumeRollOverWrapperImpl)result.getResult();        
        assertNull(prim.getTransferredVolumeRollOver().getROVolumeIfTariffSwitch());
        assertEquals(prim.getTransferredVolumeRollOver().getROVolumeIfNoTariffSwitch().intValue(), 25);

        // Option 2
        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TransferVolumeRollOverWrapperImpl);
        
        prim = (TransferVolumeRollOverWrapperImpl)result.getResult();  
        assertNull(prim.getTransferredVolumeRollOver().getROVolumeIfNoTariffSwitch());
        assertEquals(prim.getTransferredVolumeRollOver().getROVolumeIfTariffSwitch().getROVolumeSinceLastTariffSwitch().intValue(), 12);
        assertEquals(prim.getTransferredVolumeRollOver().getROVolumeIfTariffSwitch().getROVolumeTariffSwitchInterval().intValue(), 24);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TransferVolumeRollOverWrapperImpl.class);
    	
    	// Option 1
        TransferredVolumeRollOverImpl prim = new TransferredVolumeRollOverImpl(new Integer(25));
        TransferVolumeRollOverWrapperImpl wrapper = new TransferVolumeRollOverWrapperImpl(prim);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // Option 2
        ROVolumeIfTariffSwitchImpl volumeIfTariffSwitch = new ROVolumeIfTariffSwitchImpl(new Integer(12), new Integer(24));
        prim = new TransferredVolumeRollOverImpl(volumeIfTariffSwitch);
        wrapper = new TransferVolumeRollOverWrapperImpl(prim);
        rawData = this.getData2();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}