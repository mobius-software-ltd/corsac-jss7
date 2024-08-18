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

package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
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

    @Test
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

    @Test
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