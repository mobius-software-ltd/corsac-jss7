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

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
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
        assertEquals(new Integer(1), rej.getInvokeId(), "Wrong invoke ID");
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
        rej.setInvokeId(1);
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
