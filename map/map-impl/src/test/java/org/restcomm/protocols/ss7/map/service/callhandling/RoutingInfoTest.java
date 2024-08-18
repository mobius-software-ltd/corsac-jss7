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

package org.restcomm.protocols.ss7.map.service.callhandling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ForwardingData;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingOptions;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingReason;
import org.restcomm.protocols.ss7.map.service.supplementary.ForwardingOptionsImpl;
import org.junit.AfterClass;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author cristian veliscu
 * @author yulianoifa
 *
 */
public class RoutingInfoTest {
    Logger logger = LogManager.getLogger(RoutingInfoTest.class);

    byte[] data = new byte[] { 48, 9, 4, 7, -111, -105, 114, 99, 80, 24, -7 };
    byte[] _data = new byte[] { 48, 14, 48, 12, (byte) 133, 7, -111, -105, 114, 99, 80, 24, -7, (byte) 134, 1, 36 };
    
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(RoutingInfoImpl.class);

        // 4 = 00|0|00100, 7 = length

        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof RoutingInfoImpl);
        RoutingInfoImpl routeInfo = (RoutingInfoImpl)result.getResult();
        
        ISDNAddressString isdnAdd = routeInfo.getRoamingNumber();

        assertNotNull(isdnAdd);
        assertEquals(isdnAdd.getAddressNature(), AddressNature.international_number);
        assertEquals(isdnAdd.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(isdnAdd.getAddress(), "79273605819");

        // :::::::::::::::::::::::::::::
        result=parser.decode(Unpooled.wrappedBuffer(_data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof RoutingInfoImpl);
        routeInfo = (RoutingInfoImpl)result.getResult();
        
        ForwardingData _forwardingData = routeInfo.getForwardingData();
        ForwardingOptions _forwardingOptions = _forwardingData.getForwardingOptions();
        ISDNAddressString _isdnAdd = _forwardingData.getForwardedToNumber();

        assertNotNull(_isdnAdd);
        assertEquals(_isdnAdd.getAddressNature(), AddressNature.international_number);
        assertEquals(_isdnAdd.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(_isdnAdd.getAddress(), "79273605819");
        assertNotNull(_forwardingOptions);
        assertTrue(!_forwardingOptions.isNotificationToForwardingParty());
        assertTrue(!_forwardingOptions.isRedirectingPresentation());
        assertTrue(_forwardingOptions.isNotificationToCallingParty());
        assertTrue(_forwardingOptions.getForwardingReason() == ForwardingReason.busy);
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(RoutingInfoImpl.class);

        // 4 = 00|0|00100, 7 = length

        ISDNAddressStringImpl isdnAdd = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        RoutingInfoImpl routeInfo = new RoutingInfoImpl(isdnAdd);

        ByteBuf buffer=parser.encode(routeInfo);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        // :::::::::::::::::::::::
        ISDNAddressStringImpl _isdnAdd = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        ForwardingDataImpl _forwardingData = null;
        ForwardingOptionsImpl _forwardingOptions = null;
        _forwardingOptions = new ForwardingOptionsImpl(false, false, true, ForwardingReason.busy);
        _forwardingData = new ForwardingDataImpl(_isdnAdd, null, _forwardingOptions, null, null);

        routeInfo = new RoutingInfoImpl(_forwardingData);

        buffer=parser.encode(routeInfo);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(_data, encodedData));
    }
}
