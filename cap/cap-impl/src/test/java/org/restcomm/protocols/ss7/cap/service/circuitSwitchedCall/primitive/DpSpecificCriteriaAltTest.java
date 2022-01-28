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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ChangeOfLocation;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ChangeOfLocationImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DpSpecificCriteriaAltImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
*
* @author sergey vetyutnev
*
*/
public class DpSpecificCriteriaAltTest {

    public byte[] getData1() {
        return new byte[] { 48, 7, (byte) 160, 2, (byte) 132, 0, (byte) 129, 1, 15 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(DpSpecificCriteriaAltImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DpSpecificCriteriaAltImpl);
        
        DpSpecificCriteriaAltImpl elem = (DpSpecificCriteriaAltImpl)result.getResult();        
        assertEquals(elem.getChangeOfPositionControlInfo().size(), 1);
        assertTrue(elem.getChangeOfPositionControlInfo().get(0).isInterPLMNHandOver());
        assertEquals((int)elem.getNumberOfDigits(), 15);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(DpSpecificCriteriaAltImpl.class);
    	
        ArrayList<ChangeOfLocation> changeOfPositionControlInfo = new ArrayList<ChangeOfLocation>();
        ChangeOfLocationImpl changeOfLocation = new ChangeOfLocationImpl(ChangeOfLocationImpl.Boolean_Option.interPLMNHandOver);
        changeOfPositionControlInfo.add(changeOfLocation);
        DpSpecificCriteriaAltImpl elem = new DpSpecificCriteriaAltImpl(changeOfPositionControlInfo, 15);
        // ArrayList<ChangeOfLocation> changeOfPositionControlInfo, Integer numberOfDigits
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
