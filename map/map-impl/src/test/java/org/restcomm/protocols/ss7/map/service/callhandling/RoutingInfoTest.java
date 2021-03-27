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

package org.restcomm.protocols.ss7.map.service.callhandling;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ForwardingDataImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.RoutingInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingOptionsImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingReason;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/*
 *
 * @author cristian veliscu
 *
 */
public class RoutingInfoTest {
    Logger logger = Logger.getLogger(RoutingInfoTest.class);

    byte[] data = new byte[] { 48, 9, 4, 7, -111, -105, 114, 99, 80, 24, -7 };
    byte[] _data = new byte[] { 48, 14, 48, 12, (byte) 133, 7, -111, -105, 114, 99, 80, 24, -7, (byte) 134, 1, 36 };
    
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    @Test(groups = { "functional.decode", "service.callhandling" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(RoutingInfoImpl.class);

        // 4 = 00|0|00100, 7 = length

        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof RoutingInfoImpl);
        RoutingInfoImpl routeInfo = (RoutingInfoImpl)result.getResult();
        
        ISDNAddressStringImpl isdnAdd = routeInfo.getRoamingNumber();

        assertNotNull(isdnAdd);
        assertEquals(isdnAdd.getAddressNature(), AddressNature.international_number);
        assertEquals(isdnAdd.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(isdnAdd.getAddress(), "79273605819");

        // :::::::::::::::::::::::::::::
        result=parser.decode(Unpooled.wrappedBuffer(_data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof RoutingInfoImpl);
        routeInfo = (RoutingInfoImpl)result.getResult();
        
        ForwardingDataImpl _forwardingData = routeInfo.getForwardingData();
        ForwardingOptionsImpl _forwardingOptions = _forwardingData.getForwardingOptions();
        ISDNAddressStringImpl _isdnAdd = _forwardingData.getForwardedToNumber();

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

    @Test(groups = { "functional.encode", "service.callhandling" })
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
