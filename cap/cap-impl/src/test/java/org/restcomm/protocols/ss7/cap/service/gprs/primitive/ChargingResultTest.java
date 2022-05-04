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
 * @author yulianoifa
 *
 */
public class ChargingResultTest {

    public byte[] getData() {
        return new byte[] { 48, 5, -95, 3, -128, 1, 24 };
    };

    public byte[] getData1() {
        return new byte[] { 48, 10, -95, 8, -95, 6, -128, 1, 12, -127, 1, 24 };
    };

    public byte[] getData2() {
        return new byte[] { 48, 5, -96, 3, -128, 1, 25 };
    };

    public byte[] getData3() {
        return new byte[] { 48, 10, -96, 8, -95, 6, -128, 1, 12, -127, 1, 24 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ChargingResultWrapperImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ChargingResultWrapperImpl);
        
        ChargingResultWrapperImpl prim = (ChargingResultWrapperImpl)result.getResult();        
        assertEquals(prim.getChargingResult().getElapsedTime().getTimeGPRSIfNoTariffSwitch().intValue(), 24);
        assertNull(prim.getChargingResult().getTransferredVolume());

        // Option 1
        rawData = this.getData1();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ChargingResultWrapperImpl);
        
        prim = (ChargingResultWrapperImpl)result.getResult();  
        assertNull(prim.getChargingResult().getElapsedTime().getTimeGPRSIfNoTariffSwitch());
        assertEquals(prim.getChargingResult().getElapsedTime().getTimeGPRSIfTariffSwitch().getTimeGPRSSinceLastTariffSwitch(), 12);
        assertEquals(prim.getChargingResult().getElapsedTime().getTimeGPRSIfTariffSwitch().getTimeGPRSTariffSwitchInterval().intValue(), 24);
        assertNull(prim.getChargingResult().getTransferredVolume());

        // Option 2
        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ChargingResultWrapperImpl);
        
        prim = (ChargingResultWrapperImpl)result.getResult();  
        assertEquals(prim.getChargingResult().getTransferredVolume().getVolumeIfNoTariffSwitch().longValue(), 25);
        assertNull(prim.getChargingResult().getElapsedTime());

        // Option 3
        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ChargingResultWrapperImpl);
        
        prim = (ChargingResultWrapperImpl)result.getResult();  
        assertNull(prim.getChargingResult().getTransferredVolume().getVolumeIfNoTariffSwitch());
        assertEquals(prim.getChargingResult().getTransferredVolume().getVolumeIfTariffSwitch().getVolumeSinceLastTariffSwitch(), 12);
        assertEquals(prim.getChargingResult().getTransferredVolume().getVolumeIfTariffSwitch().getVolumeTariffSwitchInterval().longValue(), 24);
        assertNull(prim.getChargingResult().getElapsedTime());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ChargingResultWrapperImpl.class);
    	
    	// Option 0
        ElapsedTimeImpl elapsedTime = new ElapsedTimeImpl(new Integer(24));
        ChargingResultImpl prim = new ChargingResultImpl(elapsedTime);
        ChargingResultWrapperImpl wrapper = new ChargingResultWrapperImpl(prim);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // Option 1
        TimeGPRSIfTariffSwitchImpl timeGPRSIfTariffSwitch = new TimeGPRSIfTariffSwitchImpl(12, new Integer(24));
        elapsedTime = new ElapsedTimeImpl(timeGPRSIfTariffSwitch);
        prim = new ChargingResultImpl(elapsedTime);
        wrapper = new ChargingResultWrapperImpl(prim);
        rawData = this.getData1();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // Option 2
        TransferredVolumeImpl extQoSSubscribed = new TransferredVolumeImpl(new Long(25));
        prim = new ChargingResultImpl(extQoSSubscribed);
        wrapper = new ChargingResultWrapperImpl(prim);
        rawData = this.getData2();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // Option 3
        VolumeIfTariffSwitchImpl volumeIfTariffSwitch = new VolumeIfTariffSwitchImpl(12, new Long(24));
        extQoSSubscribed = new TransferredVolumeImpl(volumeIfTariffSwitch);
        prim = new ChargingResultImpl(extQoSSubscribed);
        wrapper = new ChargingResultWrapperImpl(prim);
        rawData = this.getData3();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
