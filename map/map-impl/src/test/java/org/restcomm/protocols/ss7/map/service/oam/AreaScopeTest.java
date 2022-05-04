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

package org.restcomm.protocols.ss7.map.service.oam;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.LAIFixedLength;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.EUtranCgi;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.RAIdentity;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.TAId;
import org.restcomm.protocols.ss7.commonapp.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.EUtranCgiImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.RAIdentityImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.TAIdImpl;
import org.restcomm.protocols.ss7.map.api.primitives.GlobalCellId;
import org.restcomm.protocols.ss7.map.api.service.oam.AreaScope;
import org.restcomm.protocols.ss7.map.primitives.GlobalCellIdImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class AreaScopeTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 11, (byte) 160, 9, 4, 7, 82, (byte) 240, 112, 69, (byte) 224, 87, (byte) 172 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 91, (byte) 160, 9, 4, 7, 82, (byte) 240, 112, 69, (byte) 224, 87, (byte) 172, (byte) 161, 9, 4, 7, 1, 2, 3, 4, 5, 6, 7,
                (byte) 162, 8, 4, 6, 11, 12, 13, 14, 15, 16, (byte) 163, 7, 4, 5, 81, (byte) 240, 17, 13, 5, (byte) 164, 7, 4, 5, 21, 22, 23, 24, 25,
                (byte) 165, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25,
                26, (byte) 161, 3, 31, 32, 33 };
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
        assertTrue(ByteBufUtil.equals(asc.getEUutranCgiList().get(0).getValue(),Unpooled.wrappedBuffer(getEUtranCgiData())));

        assertEquals(asc.getRoutingAreaIdList().size(), 1);
        assertTrue(ByteBufUtil.equals(asc.getRoutingAreaIdList().get(0).getValue(), Unpooled.wrappedBuffer(getRAIdentity())));

        assertEquals(asc.getLocationAreaIdList().size(), 1);
        assertEquals(asc.getLocationAreaIdList().get(0).getMCC(), 150);
        assertEquals(asc.getLocationAreaIdList().get(0).getMNC(), 11);
        assertEquals(asc.getLocationAreaIdList().get(0).getLac(), 3333);

        assertEquals(asc.getTrackingAreaIdList().size(), 1);
        assertTrue(ByteBufUtil.equals(asc.getTrackingAreaIdList().get(0).getValue(), Unpooled.wrappedBuffer(getTAId())));

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(asc.getExtensionContainer()));

    }

    @Test(groups = { "functional.encode", "service.oam" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AreaScopeImpl.class);
    	
        List<GlobalCellId> cgiList = new ArrayList<GlobalCellId>();
        GlobalCellId gci = new GlobalCellIdImpl(250, 7, 17888, 22444); // int mcc, int mnc, int lac, int cellId
        cgiList.add(gci);
        AreaScope asc = new AreaScopeImpl(cgiList, null, null, null, null, null);

        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        List<EUtranCgi> eUtranCgiList = new ArrayList<EUtranCgi>();
        EUtranCgiImpl eUtranCgi = new EUtranCgiImpl(Unpooled.wrappedBuffer(getEUtranCgiData()));
        eUtranCgiList.add(eUtranCgi);
        List<RAIdentity> routingAreaIdList = new ArrayList<RAIdentity>();
        RAIdentityImpl raIdentity = new RAIdentityImpl(Unpooled.wrappedBuffer(getRAIdentity()));
        routingAreaIdList.add(raIdentity);
        List<LAIFixedLength> locationAreaIdList = new ArrayList<LAIFixedLength>();
        LAIFixedLengthImpl laiFixedLength = new LAIFixedLengthImpl(150, 11, 3333); // int mcc, int mnc, int lac
        locationAreaIdList.add(laiFixedLength);
        List<TAId> trackingAreaIdList = new ArrayList<TAId>();
        TAIdImpl taId = new TAIdImpl(Unpooled.wrappedBuffer(getTAId()));
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
