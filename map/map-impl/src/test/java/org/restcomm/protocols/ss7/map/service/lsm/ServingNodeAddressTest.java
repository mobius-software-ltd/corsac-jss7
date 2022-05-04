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

package org.restcomm.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
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
        
        ISDNAddressString isdnAdd = impl.getMscNumber();
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

        DiameterIdentity di = impl.getMmeNumber();
        assertTrue(ByteBufUtil.equals(di.getValue(),Unpooled.wrappedBuffer(getDataDiameterIdentity())));
        assertNull(impl.getMscNumber());
        assertNull(impl.getSgsnNumber());
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ServingNodeAddressImpl.class);
    	
        ISDNAddressString isdnAdd = MAPParameterFactory.createISDNAddressString(AddressNature.international_number,
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

        DiameterIdentityImpl di = new DiameterIdentityImpl(Unpooled.wrappedBuffer(getDataDiameterIdentity()));
        data = getDataMme();
        impl = new ServingNodeAddressImpl(di);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}