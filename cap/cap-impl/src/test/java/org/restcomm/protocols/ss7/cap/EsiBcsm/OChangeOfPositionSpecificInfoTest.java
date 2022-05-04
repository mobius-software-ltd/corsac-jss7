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

package org.restcomm.protocols.ss7.cap.EsiBcsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.cap.api.EsiBcsm.MetDPCriterion;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OChangeOfPositionSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.LocationInformationImpl;
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
public class OChangeOfPositionSpecificInfoTest {

    public byte[] getData1() {
        return new byte[] { 48, 7, (byte) 191, 50, 4, 2, 2, 0, (byte) 200 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 19, (byte) 191, 50, 4, 2, 2, 0, (byte) 200, (byte) 191, 51, 9, (byte) 133, 5, 82, (byte) 240, 16, (byte) 128, (byte) 232,
                (byte) 135, 0 };
    }

    @Test(groups = { "functional.decode", "EsiBcsm" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(OChangeOfPositionSpecificInfoImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof OChangeOfPositionSpecificInfoImpl);
        
        OChangeOfPositionSpecificInfoImpl elem = (OChangeOfPositionSpecificInfoImpl)result.getResult();        
        assertEquals((int) elem.getLocationInformation().getAgeOfLocationInformation(), 200);
        assertNull(elem.getMetDPCriteriaList());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof OChangeOfPositionSpecificInfoImpl);
        
        elem = (OChangeOfPositionSpecificInfoImpl)result.getResult();  
        assertEquals((int) elem.getLocationInformation().getAgeOfLocationInformation(), 200);

        assertEquals(elem.getMetDPCriteriaList().size(), 2);
        assertEquals(elem.getMetDPCriteriaList().get(0).getLeavingLocationAreaId().getLac(), 33000);
        assertTrue(elem.getMetDPCriteriaList().get(1).getInterSystemHandOverToGSM());
    }

    @Test(groups = { "functional.encode", "EsiBcsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(OChangeOfPositionSpecificInfoImpl.class);
    	
        LocationInformationImpl locationInformation = new LocationInformationImpl(200, null, null, null, null, null, null, null, null, false, false, null, null);
        OChangeOfPositionSpecificInfo elem = new OChangeOfPositionSpecificInfoImpl(locationInformation, null);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));


        List<MetDPCriterion> metDPCriteriaList = new ArrayList<MetDPCriterion>();
        LAIFixedLengthImpl value = new LAIFixedLengthImpl(250, 1, 33000);
        MetDPCriterionImpl met1 = new MetDPCriterionImpl(value, MetDPCriterionImpl.LAIFixedLength_Option.leavingLocationAreaId);
        metDPCriteriaList.add(met1);
        MetDPCriterionImpl met2 = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interSystemHandOverToGSM);
        metDPCriteriaList.add(met2);
        elem = new OChangeOfPositionSpecificInfoImpl(locationInformation, metDPCriteriaList);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
