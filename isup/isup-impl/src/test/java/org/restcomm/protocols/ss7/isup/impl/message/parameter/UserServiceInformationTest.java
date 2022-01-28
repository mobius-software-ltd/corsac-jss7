/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.message.parameter.UserServiceInformation;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class UserServiceInformationTest {
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

    private ByteBuf getData() {
        return Unpooled.wrappedBuffer(new byte[] { (byte) 184, (byte) 209 });
    }

    private ByteBuf getData2() {
        return Unpooled.wrappedBuffer(new byte[] { (byte) 184, (byte) 216, (byte) 147 });
    }

    private ByteBuf getData3() {
        return Unpooled.wrappedBuffer(new byte[] { -128, -112, -95, -62, -30 });
    }

    private ByteBuf getData4() {
        return Unpooled.wrappedBuffer(new byte[] { -112, -112, 35, 96, 6, 59, -128 });
    }

    private ByteBuf getData5() {
        return Unpooled.wrappedBuffer(new byte[] { -112, -112, 35, 72, 70, 59, -36 });
    }

    @Test(groups = { "functional.decode", "parameter" })
    public void testDecode() throws Exception {

        UserServiceInformationImpl prim = new UserServiceInformationImpl();
        prim.decode(getData());

        assertEquals(prim.getCodingStandart(), UserServiceInformation._CS_INTERNATIONAL);
        assertEquals(prim.getInformationTransferCapability(), UserServiceInformation._ITS_VIDEO);
        assertEquals(prim.getTransferMode(), UserServiceInformation._TM_PACKET);
        assertEquals(prim.getInformationTransferRate(), UserServiceInformation._ITR_64x2);

        assertEquals(prim.getCustomInformationTransferRate(), 0);
        assertEquals(prim.getL1UserInformation(), 0);
        assertEquals(prim.getL2UserInformation(), 0);
        assertEquals(prim.getL3UserInformation(), 0);
        assertEquals(prim.getSyncMode(), 0);
        assertEquals(prim.getNegotiation(), 0);
        assertEquals(prim.getUserRate(), 0);
        assertEquals(prim.getIntermediateRate(), 0);
        assertEquals(prim.getNicOnTx(), 0);
        assertEquals(prim.getNicOnRx(), 0);
        assertEquals(prim.getFlowControlOnTx(), 0);
        assertEquals(prim.getFlowControlOnRx(), 0);
        assertEquals(prim.getHDR(), 0);
        assertEquals(prim.getMultiframe(), 0);
        assertEquals(prim.getMode(), 0);
        assertEquals(prim.getLLINegotiation(), 0);
        assertEquals(prim.getAssignor(), 0);
        assertEquals(prim.getInBandNegotiation(), 0);
        assertEquals(prim.getStopBits(), 0);
        assertEquals(prim.getDataBits(), 0);
        assertEquals(prim.getParity(), 0);
        assertEquals(prim.getDuplexMode(), 0);
        assertEquals(prim.getModemType(), 0);
        assertEquals(prim.getL3Protocol(), 0);

        prim = new UserServiceInformationImpl();
        prim.decode(getData2());

        assertEquals(prim.getCodingStandart(), UserServiceInformation._CS_INTERNATIONAL);
        assertEquals(prim.getInformationTransferCapability(), UserServiceInformation._ITS_VIDEO);
        assertEquals(prim.getTransferMode(), UserServiceInformation._TM_PACKET);
        assertEquals(prim.getInformationTransferRate(), UserServiceInformation._ITR_MULTIRATE);

        assertEquals(prim.getCustomInformationTransferRate(), 19);
        assertEquals(prim.getL1UserInformation(), 0);
        assertEquals(prim.getL2UserInformation(), 0);
        assertEquals(prim.getL3UserInformation(), 0);
        assertEquals(prim.getSyncMode(), 0);
        assertEquals(prim.getNegotiation(), 0);
        assertEquals(prim.getUserRate(), 0);
        assertEquals(prim.getIntermediateRate(), 0);
        assertEquals(prim.getNicOnTx(), 0);
        assertEquals(prim.getNicOnRx(), 0);
        assertEquals(prim.getFlowControlOnTx(), 0);
        assertEquals(prim.getFlowControlOnRx(), 0);
        assertEquals(prim.getHDR(), 0);
        assertEquals(prim.getMultiframe(), 0);
        assertEquals(prim.getMode(), 0);
        assertEquals(prim.getLLINegotiation(), 0);
        assertEquals(prim.getAssignor(), 0);
        assertEquals(prim.getInBandNegotiation(), 0);
        assertEquals(prim.getStopBits(), 0);
        assertEquals(prim.getDataBits(), 0);
        assertEquals(prim.getParity(), 0);
        assertEquals(prim.getDuplexMode(), 0);
        assertEquals(prim.getModemType(), 0);
        assertEquals(prim.getL3Protocol(), 0);

        prim = new UserServiceInformationImpl();
        prim.decode(getData3());

        assertEquals(prim.getCodingStandart(), UserServiceInformation._CS_CCITT);
        assertEquals(prim.getInformationTransferCapability(), UserServiceInformation._ITS_SPEECH);
        assertEquals(prim.getTransferMode(), UserServiceInformation._TM_CIRCUIT);
        assertEquals(prim.getInformationTransferRate(), UserServiceInformation._ITR_64);

        assertEquals(prim.getCustomInformationTransferRate(), 0);
        assertEquals(prim.getL1UserInformation(), UserServiceInformation._L1_ITUT_110);
        assertEquals(prim.getL2UserInformation(), UserServiceInformation._L2_Q921);
        assertEquals(prim.getL3UserInformation(), UserServiceInformation._L3_Q931);
        assertEquals(prim.getSyncMode(), 0);
        assertEquals(prim.getNegotiation(), 0);
        assertEquals(prim.getUserRate(), 0);
        assertEquals(prim.getIntermediateRate(), 0);
        assertEquals(prim.getNicOnTx(), 0);
        assertEquals(prim.getNicOnRx(), 0);
        assertEquals(prim.getFlowControlOnTx(), 0);
        assertEquals(prim.getFlowControlOnRx(), 0);
        assertEquals(prim.getHDR(), 0);
        assertEquals(prim.getMultiframe(), 0);
        assertEquals(prim.getMode(), 0);
        assertEquals(prim.getLLINegotiation(), 0);
        assertEquals(prim.getAssignor(), 0);
        assertEquals(prim.getInBandNegotiation(), 0);
        assertEquals(prim.getStopBits(), 0);
        assertEquals(prim.getDataBits(), 0);
        assertEquals(prim.getParity(), 0);
        assertEquals(prim.getDuplexMode(), 0);
        assertEquals(prim.getModemType(), 0);
        assertEquals(prim.getL3Protocol(), 0);

        // TODO: now tested only for a case l1UserInformation = 0 && l2UserInformation == 0 && l2UserInformation == 0
        // we need to test other case encoding/decoding

        // 10418
        prim = new UserServiceInformationImpl();
        prim.decode(getData4());

        assertEquals(prim.getCodingStandart(), UserServiceInformation._CS_CCITT);
        assertEquals(prim.getInformationTransferCapability(), UserServiceInformation._ITS_3_1_KHZ);
        assertEquals(prim.getTransferMode(), UserServiceInformation._TM_CIRCUIT);
        assertEquals(prim.getInformationTransferRate(), UserServiceInformation._ITR_64);

        assertEquals(prim.getCustomInformationTransferRate(), 0);
        assertEquals(prim.getL1UserInformation(), UserServiceInformation._L1_G711_A);
        assertEquals(prim.getL2UserInformation(), 0);
        assertEquals(prim.getL3UserInformation(), 0);
        assertEquals(prim.getSyncMode(), UserServiceInformation._SA_ASYNC);
        assertEquals(prim.getNegotiation(), UserServiceInformation._NG_INBAND_POSSIBLE);
        assertEquals(prim.getUserRate(), UserServiceInformation._UR_EBITS);
        assertEquals(prim.getIntermediateRate(), UserServiceInformation._IR_NOT_USED);
        assertEquals(prim.getNicOnTx(), UserServiceInformation._NICTX_NOT_REQUIRED);
        assertEquals(prim.getNicOnRx(), UserServiceInformation._NICRX_CANNOT_ACCEPT);

        assertEquals(prim.getFlowControlOnTx(), UserServiceInformation._FCTX_REQUIRED);
        assertEquals(prim.getFlowControlOnRx(), UserServiceInformation._FCRX_CAN_ACCEPT);
        assertEquals(prim.getHDR(), 0);
        assertEquals(prim.getMultiframe(), 0);
        assertEquals(prim.getMode(), 0);
        assertEquals(prim.getLLINegotiation(), 0);
        assertEquals(prim.getAssignor(), 0);
        assertEquals(prim.getInBandNegotiation(), 0);
        assertEquals(prim.getStopBits(), UserServiceInformation._SB_1BIT);
        assertEquals(prim.getDataBits(), UserServiceInformation._DB_8BITS);
        assertEquals(prim.getParity(), UserServiceInformation._PAR_NONE);
        assertEquals(prim.getDuplexMode(), 0);
        assertEquals(prim.getModemType(), 0);
        assertEquals(prim.getL3Protocol(), 0);


        // 213605
        prim = new UserServiceInformationImpl();
        prim.decode(getData5());

        assertEquals(prim.getCodingStandart(), UserServiceInformation._CS_CCITT);
        assertEquals(prim.getInformationTransferCapability(), UserServiceInformation._ITS_3_1_KHZ);
        assertEquals(prim.getTransferMode(), UserServiceInformation._TM_CIRCUIT);
        assertEquals(prim.getInformationTransferRate(), UserServiceInformation._ITR_64);

        assertEquals(prim.getCustomInformationTransferRate(), 0);
        assertEquals(prim.getL1UserInformation(), UserServiceInformation._L1_G711_A);
        assertEquals(prim.getL2UserInformation(), 0);
        assertEquals(prim.getL3UserInformation(), 0);
        assertEquals(prim.getSyncMode(), UserServiceInformation._SA_ASYNC);
        assertEquals(prim.getNegotiation(), UserServiceInformation._NG_INBAND_NOT_POSSIBLE);
        assertEquals(prim.getUserRate(), UserServiceInformation._UR_9_6);

        assertEquals(prim.getIntermediateRate(), UserServiceInformation._IR_16_0);
        assertEquals(prim.getNicOnTx(), UserServiceInformation._NICTX_NOT_REQUIRED);
        assertEquals(prim.getNicOnRx(), UserServiceInformation._NICRX_CANNOT_ACCEPT);
        assertEquals(prim.getFlowControlOnTx(), UserServiceInformation._FCTX_REQUIRED);
        assertEquals(prim.getFlowControlOnRx(), UserServiceInformation._FCRX_CAN_ACCEPT);
        assertEquals(prim.getHDR(), 0);
        assertEquals(prim.getMultiframe(), 0);
        assertEquals(prim.getMode(), 0);
        assertEquals(prim.getLLINegotiation(), 0);
        assertEquals(prim.getAssignor(), 0);
        assertEquals(prim.getInBandNegotiation(), 0);
        assertEquals(prim.getStopBits(), UserServiceInformation._SB_1BIT);
        assertEquals(prim.getDataBits(), UserServiceInformation._DB_8BITS);
        assertEquals(prim.getParity(), UserServiceInformation._PAR_NONE);
        assertEquals(prim.getDuplexMode(), UserServiceInformation._DUP_FULL);
        assertEquals(prim.getModemType(), 28);
        assertEquals(prim.getL3Protocol(), 0);

    }

    @Test(groups = { "functional.encode", "parameter" })
    public void testEncode() throws Exception {

        UserServiceInformationImpl prim = new UserServiceInformationImpl();
        prim.setCodingStandart(UserServiceInformation._CS_INTERNATIONAL);
        prim.setInformationTransferCapability(UserServiceInformation._ITS_VIDEO);
        prim.setTransferMode(UserServiceInformation._TM_PACKET);
        prim.setInformationTransferRate(UserServiceInformation._ITR_64x2);

        ByteBuf data = getData();
        ByteBuf encodedData = Unpooled.buffer();
        prim.encode(encodedData);

        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));

        prim = new UserServiceInformationImpl();
        prim.setCodingStandart(UserServiceInformation._CS_INTERNATIONAL);
        prim.setInformationTransferCapability(UserServiceInformation._ITS_VIDEO);
        prim.setTransferMode(UserServiceInformation._TM_PACKET);
        prim.setInformationTransferRate(UserServiceInformation._ITR_MULTIRATE);
        prim.setCustomInformationTransferRate(19);

        data = getData2();
        encodedData = Unpooled.buffer();
        prim.encode(encodedData);

        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));

        prim = new UserServiceInformationImpl();
        prim.setCodingStandart(UserServiceInformation._CS_CCITT);
        prim.setInformationTransferCapability(UserServiceInformation._ITS_SPEECH);
        prim.setTransferMode(UserServiceInformation._TM_CIRCUIT);
        prim.setInformationTransferRate(UserServiceInformation._ITR_64);
        prim.setL1UserInformation(UserServiceInformation._L1_ITUT_110);
        prim.setL2UserInformation(UserServiceInformation._L2_Q921);
        prim.setL3UserInformation(UserServiceInformation._L3_Q931);

        data = getData3();
        encodedData = Unpooled.buffer();
        prim.encode(encodedData);

        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));

        // TODO: now tested only for a case l1UserInformation = 0 && l2UserInformation == 0 && l2UserInformation == 0
        // we need to test other case encoding/decoding
    }
}
