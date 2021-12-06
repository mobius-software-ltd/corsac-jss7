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
public class ChargingRollOverTest {

    public byte[] getData() {
        return new byte[] { 48, 5, -128, 3, -128, 1, 25 };
    };

    public byte[] getData1() {
        return new byte[] { 48, 10, -96, 8, -95, 6, -128, 1, 12, -127, 1, 24 };
    };

    public byte[] getData2() {
        return new byte[] { 48, 5, -127, 3, -128, 1, 24 };
    };

    public byte[] getData3() {
        return new byte[] { 48, 10, -95, 8, -95, 6, -128, 1, 12, -127, 1, 24 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ChargingRollOverWrapperImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ChargingRollOverWrapperImpl);
        
        ChargingRollOverWrapperImpl prim = (ChargingRollOverWrapperImpl)result.getResult();        
        assertEquals(prim.getChargingRollOver().getTransferredVolumeRollOver().getROVolumeIfNoTariffSwitch().longValue(), 25);
        assertNull(prim.getChargingRollOver().getElapsedTimeRollOver());

        // Option 1
        rawData = this.getData1();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ChargingRollOverWrapperImpl);
        
        prim = (ChargingRollOverWrapperImpl)result.getResult();    
        assertNull(prim.getChargingRollOver().getTransferredVolumeRollOver().getROVolumeIfNoTariffSwitch());
        assertEquals(prim.getChargingRollOver().getTransferredVolumeRollOver().getROVolumeIfTariffSwitch().getROVolumeSinceLastTariffSwitch()
                .intValue(), 12);
        assertEquals(prim.getChargingRollOver().getTransferredVolumeRollOver().getROVolumeIfTariffSwitch().getROVolumeTariffSwitchInterval()
                .intValue(), 24);
        assertNull(prim.getChargingRollOver().getElapsedTimeRollOver());

        // Option 2
        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ChargingRollOverWrapperImpl);
        
        prim = (ChargingRollOverWrapperImpl)result.getResult();   
        assertEquals(prim.getChargingRollOver().getElapsedTimeRollOver().getROTimeGPRSIfNoTariffSwitch().intValue(), 24);
        assertNull(prim.getChargingRollOver().getTransferredVolumeRollOver());

        // Option 3
        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ChargingRollOverWrapperImpl);
        
        prim = (ChargingRollOverWrapperImpl)result.getResult();   
        assertNull(prim.getChargingRollOver().getElapsedTimeRollOver().getROTimeGPRSIfNoTariffSwitch());
        assertEquals(prim.getChargingRollOver().getElapsedTimeRollOver().getROTimeGPRSIfTariffSwitch().getROTimeGPRSSinceLastTariffSwitch()
                .intValue(), 12);
        assertEquals(
                prim.getChargingRollOver().getElapsedTimeRollOver().getROTimeGPRSIfTariffSwitch().getROTimeGPRSTariffSwitchInterval().intValue(), 24);
        assertNull(prim.getChargingRollOver().getTransferredVolumeRollOver());

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ChargingRollOverWrapperImpl.class);
    	
    	// Option 0
        TransferredVolumeRollOverImpl transferredVolumeRollOver = new TransferredVolumeRollOverImpl(new Integer(25));
        ChargingRollOverImpl prim = new ChargingRollOverImpl(transferredVolumeRollOver);
        ChargingRollOverWrapperImpl wrapper = new  ChargingRollOverWrapperImpl(prim);        		
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // Option 1
        ROVolumeIfTariffSwitchImpl roVolumeIfTariffSwitch = new ROVolumeIfTariffSwitchImpl(new Integer(12), new Integer(24));
        transferredVolumeRollOver = new TransferredVolumeRollOverImpl(roVolumeIfTariffSwitch);
        prim = new ChargingRollOverImpl(transferredVolumeRollOver);
        wrapper = new  ChargingRollOverWrapperImpl(prim);
        rawData = this.getData1();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // Option 2
        ElapsedTimeRollOverImpl elapsedTimeRollOver = new ElapsedTimeRollOverImpl(new Integer(24));
        prim = new ChargingRollOverImpl(elapsedTimeRollOver);
        wrapper = new  ChargingRollOverWrapperImpl(prim);
        rawData = this.getData2();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // Option 3
        ROTimeGPRSIfTariffSwitchImpl roTimeGPRSIfTariffSwitch = new ROTimeGPRSIfTariffSwitchImpl(new Integer(12), new Integer(
                24));
        elapsedTimeRollOver = new ElapsedTimeRollOverImpl(roTimeGPRSIfTariffSwitch);
        prim = new ChargingRollOverImpl(elapsedTimeRollOver);
        wrapper = new  ChargingRollOverWrapperImpl(prim);
        rawData = this.getData3();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}