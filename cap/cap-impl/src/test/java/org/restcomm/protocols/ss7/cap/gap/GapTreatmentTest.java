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

package org.restcomm.protocols.ss7.cap.gap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.InformationToSendImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ToneImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapTreatmentImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapTreatmentWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CauseIsupImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CauseIndicatorsImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class GapTreatmentTest {

    public byte[] getData() {
        return new byte[] { 48, 10, (byte) 160, 8, (byte) 161, 6, (byte) 128, 1, 10, (byte) 129, 1, 20 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 4, (byte) 129, 2, (byte) 192, (byte) 131 };
    }

    @Test(groups = { "functional.decode", "gap" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(GapTreatmentWrapperImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GapTreatmentWrapperImpl);
        
        GapTreatmentWrapperImpl elem = (GapTreatmentWrapperImpl)result.getResult();        
        assertEquals(elem.getGapTreatment().getInformationToSend().getTone().getToneID(), 10);
        assertEquals((int) (elem.getGapTreatment().getInformationToSend().getTone().getDuration()), 20);


        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GapTreatmentWrapperImpl);
        
        elem = (GapTreatmentWrapperImpl)result.getResult();
        assertEquals(elem.getGapTreatment().getCause().getCauseIndicators().getCauseValue(), 3);
    }

    @Test(groups = { "functional.encode", "gap" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(GapTreatmentWrapperImpl.class);
    	
    	ToneImpl tone = new ToneImpl(10, 20);
        InformationToSendImpl informationToSend = new InformationToSendImpl(tone);
        GapTreatmentImpl elem = new GapTreatmentImpl(informationToSend);
        GapTreatmentWrapperImpl param = new GapTreatmentWrapperImpl(elem);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(param);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));


        // int codingStandard, int location, int recommendation, int causeValue, byte[] diagnostics
        CauseIndicators causeIndicators = new CauseIndicatorsImpl(2, 0, 0, 3, null);
        CauseIsupImpl releaseCause = new CauseIsupImpl(causeIndicators);
        elem = new GapTreatmentImpl(releaseCause);
        param = new GapTreatmentWrapperImpl(elem);
        rawData = this.getData2();
        buffer=parser.encode(param);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
