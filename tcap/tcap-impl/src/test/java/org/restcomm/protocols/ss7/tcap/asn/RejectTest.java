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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcap.asn.comp.GeneralProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;
import org.restcomm.protocols.ss7.tcap.asn.comp.ProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Reject;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNException;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class RejectTest {

    private byte[] getData() {
        return new byte[] { 108, 8, (byte) 164, 6, 2, 1, 1, (byte) 129, 1, 2 };
    }

    private byte[] getDataNullInvokeId() {
        return new byte[] { 108, 7, -92, 5, 5, 0, -128, 1, 0 };
    }

    @Test(groups = { "functional.decode" })
    public void testDecode() throws ParseException, ASNException {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(ComponentImpl.class);
    	
        byte[] b = getData();
        Object output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        assertTrue(output instanceof ComponentImpl);
        ComponentImpl comp = (ComponentImpl)output;

        assertEquals(ComponentType.Reject, comp.getType(), "Wrong component Type");
        Reject rej = comp.getReject();
        assertEquals(new Long(1), rej.getInvokeId(), "Wrong invoke ID");
        Problem prb = rej.getProblem();
        assertEquals(ProblemType.Invoke, prb.getType());
        assertEquals(InvokeProblemType.MistypedParameter, prb.getInvokeProblemType());

        b = getDataNullInvokeId();
        output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        assertTrue(output instanceof ComponentImpl);
        comp = (ComponentImpl)output;

        assertEquals(ComponentType.Reject, comp.getType(), "Wrong component Type");
        rej = comp.getReject();
        assertNull(rej.getInvokeId());
        prb = rej.getProblem();
        assertEquals(ProblemType.General, prb.getType());
        assertEquals(GeneralProblemType.UnrecognizedComponent, prb.getGeneralProblemType());
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws EncodeException, ASNException {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(ComponentImpl.class);
    	
        byte[] expected = this.getData();
        Reject rej = TcapFactory.createComponentReject();
        rej.setInvokeId(1L);
        rej.setProblem(InvokeProblemType.MistypedParameter);
        ComponentImpl comp=new ComponentImpl();
        comp.setReject(rej);
        
        ByteBuf buffer=parser.encode(comp);
        byte[] encodedData = buffer.array();
        assertTrue(Arrays.equals(expected, encodedData));

        expected = this.getDataNullInvokeId();
        rej = TcapFactory.createComponentReject();
        rej.setProblem(GeneralProblemType.UnrecognizedComponent);
        comp=new ComponentImpl();
        comp.setReject(rej);
        
        buffer=parser.encode(comp);
        encodedData = buffer.array();
        assertTrue(Arrays.equals(expected, encodedData));
    }
}
