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

package org.restcomm.protocols.ss7.map.service.oam;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.primitives.GlobalCellIdImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.EUtranCgiImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TAIdImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.AreaScopeImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
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
public class AreaScopeTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 11, (byte) 160, 9, 4, 7, 82, (byte) 240, 112, 69, (byte) 224, 87, (byte) 172 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 97, -96, 9, 4, 7, 82, -16, 112, 69, -32, 87, -84, -95, 9, 4, 7, 1, 2, 3, 4, 5, 6, 7, -94, 8, 4, 6, 11, 12, 13, 14, 15, 16, -93, 7, 4, 5, 81, -16, 17, 13, 5, -92, 7, 4, 5, 21, 22, 23, 24, 25, -91, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33 };
    }

    private byte[] getEUtranCgiData() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7 };
    }

    private byte[] getRAIdentity() {
        return new byte[] { 11, 12, 13, 14, 15, 16 };
    }

    private byte[] getTAId() {
        return new byte[] { 21, 22, 23, 24, 25 };
    }

    @Test(groups = { "functional.decode", "service.oam" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AreaScopeImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AreaScopeImpl);
        AreaScopeImpl asc = (AreaScopeImpl)result.getResult();
        
        assertEquals(asc.getCgiList().size(), 1);
        assertEquals(asc.getCgiList().get(0).getMcc(), 250);
        assertEquals(asc.getCgiList().get(0).getMnc(), 7);
        assertEquals(asc.getCgiList().get(0).getCellId(), 22444);

        assertNull(asc.getEUutranCgiList());
        assertNull(asc.getRoutingAreaIdList());
        assertNull(asc.getLocationAreaIdList());
        assertNull(asc.getTrackingAreaIdList());
        assertNull(asc.getExtensionContainer());

        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AreaScopeImpl);
        asc = (AreaScopeImpl)result.getResult();

        assertEquals(asc.getCgiList().size(), 1);
        assertEquals(asc.getCgiList().get(0).getMcc(), 250);
        assertEquals(asc.getCgiList().get(0).getMnc(), 7);
        assertEquals(asc.getCgiList().get(0).getCellId(), 22444);

        assertEquals(asc.getEUutranCgiList().size(), 1);
        assertEquals(asc.getEUutranCgiList().get(0).getData(), getEUtranCgiData());

        assertEquals(asc.getRoutingAreaIdList().size(), 1);
        assertEquals(asc.getRoutingAreaIdList().get(0).getData(), getRAIdentity());

        assertEquals(asc.getLocationAreaIdList().size(), 1);
        assertEquals(asc.getLocationAreaIdList().get(0).getMCC(), 150);
        assertEquals(asc.getLocationAreaIdList().get(0).getMNC(), 11);
        assertEquals(asc.getLocationAreaIdList().get(0).getLac(), 3333);

        assertEquals(asc.getTrackingAreaIdList().size(), 1);
        assertEquals(asc.getTrackingAreaIdList().get(0).getData(), getTAId());

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(asc.getExtensionContainer()));

    }

    @Test(groups = { "functional.encode", "service.oam" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AreaScopeImpl.class);
    	
        ArrayList<GlobalCellIdImpl> cgiList = new ArrayList<GlobalCellIdImpl>();
        GlobalCellIdImpl gci = new GlobalCellIdImpl(250, 7, 17888, 22444); // int mcc, int mnc, int lac, int cellId
        cgiList.add(gci);
        AreaScopeImpl asc = new AreaScopeImpl(cgiList, null, null, null, null, null);

        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        ArrayList<EUtranCgiImpl> eUtranCgiList = new ArrayList<EUtranCgiImpl>();
        EUtranCgiImpl eUtranCgi = new EUtranCgiImpl(getEUtranCgiData());
        eUtranCgiList.add(eUtranCgi);
        ArrayList<RAIdentityImpl> routingAreaIdList = new ArrayList<RAIdentityImpl>();
        RAIdentityImpl raIdentity = new RAIdentityImpl(getRAIdentity());
        routingAreaIdList.add(raIdentity);
        ArrayList<LAIFixedLengthImpl> locationAreaIdList = new ArrayList<LAIFixedLengthImpl>();
        LAIFixedLengthImpl laiFixedLength = new LAIFixedLengthImpl(150, 11, 3333); // int mcc, int mnc, int lac
        locationAreaIdList.add(laiFixedLength);
        ArrayList<TAIdImpl> trackingAreaIdList = new ArrayList<TAIdImpl>();
        TAIdImpl taId = new TAIdImpl(getTAId());
        trackingAreaIdList.add(taId);
        asc = new AreaScopeImpl(cgiList, eUtranCgiList, routingAreaIdList, locationAreaIdList, trackingAreaIdList,
                MAPExtensionContainerTest.GetTestExtensionContainer());

        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
