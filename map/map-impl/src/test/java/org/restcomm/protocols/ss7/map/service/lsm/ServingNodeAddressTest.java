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

package org.restcomm.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.lsm.ServingNodeAddressImpl;
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
public class ServingNodeAddressTest {

    MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

    public byte[] getDataMsc() {
        return new byte[] { 48, 7, -128, 5, -111, 49, 117, 9, 0 };
    }

    public byte[] getDataSgsn() {
        return new byte[] { 48, 7, -127, 5, -111, 49, 117, 9, 0 };
    }

    public byte[] getDataMme() {
        return new byte[] { 48, 12, -126, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
    }

    public byte[] getDataDiameterIdentity() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ServingNodeAddressImpl.class);
    	
        byte[] data = getDataMsc();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ServingNodeAddressImpl);
        ServingNodeAddressImpl impl = (ServingNodeAddressImpl)result.getResult();
        
        ISDNAddressStringImpl isdnAdd = impl.getMscNumber();
        assertEquals(isdnAdd.getAddressNature(), AddressNature.international_number);
        assertEquals(isdnAdd.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(isdnAdd.getAddress().equals("13579000"));
        assertNull(impl.getSgsnNumber());
        assertNull(impl.getMmeNumber());

        data = getDataSgsn();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ServingNodeAddressImpl);
        impl = (ServingNodeAddressImpl)result.getResult();

        isdnAdd = impl.getSgsnNumber();
        assertEquals(isdnAdd.getAddressNature(), AddressNature.international_number);
        assertEquals(isdnAdd.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(isdnAdd.getAddress().equals("13579000"));
        assertNull(impl.getMscNumber());
        assertNull(impl.getMmeNumber());

        data = getDataMme();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ServingNodeAddressImpl);
        impl = (ServingNodeAddressImpl)result.getResult();

        DiameterIdentityImpl di = impl.getMmeNumber();
        assertTrue(Arrays.equals(di.getData(), getDataDiameterIdentity()));
        assertNull(impl.getMscNumber());
        assertNull(impl.getSgsnNumber());
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ServingNodeAddressImpl.class);
    	
        ISDNAddressStringImpl isdnAdd = MAPParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "13579000");
        ServingNodeAddressImpl impl = new ServingNodeAddressImpl(isdnAdd, true);

        byte[] data = getDataMsc();
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        impl = new ServingNodeAddressImpl(isdnAdd, false);
        data = getDataSgsn();
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        DiameterIdentityImpl di = new DiameterIdentityImpl(getDataDiameterIdentity());
        data = getDataMme();
        impl = new ServingNodeAddressImpl(di);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}