/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.tcap.asn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.tcap.TCAPTestUtils;
import org.restcomm.protocols.ss7.tcap.asn.comp.ASNInvokeParameterImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.BaseComponent;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCUniMessage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNException;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class TcUnidirectionalTest {
	
	private byte[] getData() {
        return new byte[] { 97, 45, 107, 27, 40, 25, 6, 7, 0, 17, -122, 5, 1, 2, 1, -96, 14, 96, 12, -128, 2, 7, -128, -95, 6,
                6, 4, 40, 2, 3, 4, 108, 14, -95, 12, 2, 1, -128, 2, 2, 2, 79, 4, 3, 1, 2, 3 };
    }
	
	ASNParser parser=new ASNParser();
    
	@BeforeClass
	public void setUp() {
		parser.loadClass(TCUniMessageImpl.class);
    
		parser.clearClassMapping(ASNInvokeParameterImpl.class);	
		
		parser.clearClassMapping(ASNDialogPortionObjectImpl.class);
    	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogRequestAPDUImpl.class);
    	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogResponseAPDUImpl.class);
    	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogAbortAPDUImpl.class);
	}
	
    @Test(groups = { "functional.encode" })
    public void testEncode() throws ASNException {

        byte[] expected = getData();

        TCUniMessageImpl tcUniMessage = new TCUniMessageImpl();

        DialogPortion dp = TcapFactory.createDialogPortion();
        dp.setUnidirectional(true);
        DialogRequestAPDU dapdu = TcapFactory.createDialogAPDURequest();
        ApplicationContextNameImpl acn = new ApplicationContextNameImpl();
        acn.setOid(Arrays.asList(new Long[] { 1L, 0L, 2L, 3L, 4L }));
        dapdu.setApplicationContextName(acn);
        dp.setDialogAPDU(dapdu);
        tcUniMessage.setDialogPortion(dp);

        Invoke invComp = TcapFactory.createComponentInvoke();
        invComp.setInvokeId(-128l);
        invComp.setOperationCode(591L);
        ASNOctetString p=new ASNOctetString(Unpooled.wrappedBuffer(new byte[] { 1, 2, 3 }));
        invComp.setParameter(p);
        
        List<BaseComponent> components=Arrays.asList(new BaseComponent[] { invComp });
        tcUniMessage.setComponents(components);

        ByteBuf buffer=parser.encode(tcUniMessage);
        byte[] data = buffer.array();
        TCAPTestUtils.compareArrays(expected, data);
    }

    @Test(groups = { "functional.decode" })
    public void testDecode() throws ASNException {

    	ByteBuf buffer=Unpooled.wrappedBuffer(this.getData());
        TCUniMessage tcm = (TCUniMessage)parser.decode(buffer).getResult();

        DialogPortion dp = tcm.getDialogPortion();
        List<BaseComponent> comp = tcm.getComponents();

        assertNotNull(dp);
        assertNotNull(dp.getDialogAPDU());
        assertEquals(true, dp.isUnidirectional());
        assertEquals(DialogAPDUType.Request, dp.getDialogAPDU().getType());

        assertNotNull(comp);
        assertEquals(1, comp.size());
    }
}
