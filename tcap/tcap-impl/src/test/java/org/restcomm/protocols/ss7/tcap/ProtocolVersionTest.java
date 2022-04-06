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

package org.restcomm.protocols.ss7.tcap;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.RemoteSccpStatus;
import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpListener;
import org.restcomm.protocols.ss7.sccp.SignallingPointStatus;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.restcomm.protocols.ss7.sccp.parameter.Credit;
import org.restcomm.protocols.ss7.sccp.parameter.ErrorCause;
import org.restcomm.protocols.ss7.sccp.parameter.Importance;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.RefusalCause;
import org.restcomm.protocols.ss7.sccp.parameter.ReleaseCause;
import org.restcomm.protocols.ss7.sccp.parameter.ResetCause;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.asn.ASNComponentPortionObjectImpl;
import org.restcomm.protocols.ss7.tcap.asn.ASNDialogPortionObjectImpl;
import org.restcomm.protocols.ss7.tcap.asn.DialogRequestAPDU;
import org.restcomm.protocols.ss7.tcap.asn.ProtocolVersion;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.RejectImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLastImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogAbortAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogRequestAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogResponseAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCBeginMessageImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Test for call flow.
 *
 * @author Nosach Konstantin
 *
 */
public class ProtocolVersionTest extends SccpHarness {
    public static final long WAIT_TIME = 500;
    public static final long[] _ACN_ = new long[] { 0, 4, 0, 0, 1, 0, 19, 2 };
    private TCAPStackImpl tcapStack1;
    private TCAPStackImpl tcapStack2;
    private SccpAddress peer1Address;
    private SccpAddress peer2Address;
    private Client client;
    private TestSccpListener sccpListener;
    private ProtocolVersion pv;

    public ProtocolVersionTest() {

    }

    @BeforeClass
    public void setUpClass() {
        this.sccpStack1Name = "TCAPFunctionalTestSccpStack1";
        this.sccpStack2Name = "TCAPFunctionalTestSccpStack2";
        System.out.println("setUpClass");
    }

    @AfterClass
    public void tearDownClass() throws Exception {
        System.out.println("tearDownClass");
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    @BeforeMethod
    public void setUp() throws Exception {
        System.out.println("setUp");
        super.setUp();

        peer1Address = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
        peer2Address = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

        sccpListener = new TestSccpListener();
        this.sccpProvider2.registerSccpListener(8, sccpListener);
        this.tcapStack1 = new TCAPStackImpl("TCAPFunctionalTest", this.sccpProvider1, 8, 4);
        this.tcapStack2 = new TCAPStackImpl("TCAPFunctionalTest", this.sccpProvider2, 7, 4);

        this.tcapStack1.start();
        this.tcapStack2.start();

        this.tcapStack1.setInvokeTimeout(0);
        this.tcapStack2.setInvokeTimeout(0);
        // create test classes
        this.client = new Client(this.tcapStack1, super.parameterFactory, peer1Address, peer2Address);
        //this.server = new Server(this.tcapStack2, super.parameterFactory, peer2Address, peer1Address);

    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#tearDown()
     */
    @AfterMethod
    public void tearDown() {
        this.tcapStack1.stop();
        this.tcapStack2.stop();
        super.tearDown();

    }

    @Test(groups = { "functional.flow" })
    public void doNotSendProtocolVersionDialogTest() throws Exception {

        client.startClientDialog();
        client.dialog.setDoNotSendProtocolVersion(true);
        EventTestHarness.waitFor(WAIT_TIME);
        
        client.sendBegin();
        EventTestHarness.waitFor(WAIT_TIME);
        assertNull(pv);
    }
    
    @Test(groups = { "functional.flow" })
    public void sendProtocolVersionDialogTest() throws Exception {

        client.startClientDialog();
        client.dialog.setDoNotSendProtocolVersion(false);
        EventTestHarness.waitFor(WAIT_TIME);
        
        client.sendBegin();
        EventTestHarness.waitFor(WAIT_TIME);
        assertNotNull(pv);        
    }
    
    @Test(groups = { "functional.flow" })
    public void doNotSendProtocolVersionStackTest() throws Exception {
        this.tcapStack1.setDoNotSendProtocolVersion(true);
        client.startClientDialog();
        EventTestHarness.waitFor(WAIT_TIME);
        client.sendBegin();
        EventTestHarness.waitFor(WAIT_TIME);        
        assertNull(pv);
    }

    @Test(groups = { "functional.flow" })
    public void sendProtocolVersionStackTest() throws Exception {
        this.tcapStack1.setDoNotSendProtocolVersion(false);
        client.startClientDialog();
        EventTestHarness.waitFor(WAIT_TIME);
        client.sendBegin();
        EventTestHarness.waitFor(WAIT_TIME);
        assertNotNull(pv);
    }

    private class TestSccpListener implements SccpListener {

        private static final long serialVersionUID = 1L;
        private ASNParser parser=new ASNParser(true);
        
        private TestSccpListener() {
        	parser.loadClass(TCBeginMessageImpl.class);
            
        	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogRequestAPDUImpl.class);
        	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogResponseAPDUImpl.class);
        	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogAbortAPDUImpl.class);
        	
        	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, InvokeImpl.class);
        	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnResultImpl.class);
        	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnResultLastImpl.class);
        	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, RejectImpl.class);
        	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnErrorImpl.class); 
        }
        
        @Override
        public void onMessage(SccpDataMessage message) {
        	ByteBuf buffer=Unpooled.wrappedBuffer(message.getData());
        	Object output=null;
        	try {
        		output=parser.decode(buffer).getResult();
        	}
        	catch(ASNException ex) {        		
        	}
        	
            if(output!=null && output instanceof TCBeginMessage) {
            	TCBeginMessage tcb=(TCBeginMessage)output;
            	if(tcb.getDialogPortion().getDialogAPDU() instanceof DialogRequestAPDU) {
                            pv=((DialogRequestAPDU)tcb.getDialogPortion().getDialogAPDU()).getProtocolVersion();
            	} 
                        
            	System.out.println("DIALOG REQUEST:" + tcb.getDialogPortion().toString());
            	System.out.println("PROTOCOL VERSION IS : " + pv);
            }            
        }

        @Override
        public void onNotice(SccpNoticeMessage message) {
        }

        @Override
        public void onCoordResponse(int ssn, int multiplicityIndicator) {
        }

        @Override
        public void onState(int dpc, int ssn, boolean inService,
                int multiplicityIndicator) {
        }

        @Override
        public void onPcState(int dpc, SignallingPointStatus status, Integer restrictedImportanceLevel, RemoteSccpStatus remoteSccpStatus) {
        }

        @Override
        public void onConnectIndication(SccpConnection conn, SccpAddress calledAddress, SccpAddress callingAddress,
                ProtocolClass clazz, Credit credit, ByteBuf data, Importance importance) throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onConnectConfirm(SccpConnection conn, ByteBuf data) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onDisconnectIndication(SccpConnection conn, ReleaseCause reason, ByteBuf data) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onDisconnectIndication(SccpConnection conn, RefusalCause reason, ByteBuf data) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onDisconnectIndication(SccpConnection conn, ErrorCause errorCause) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onResetIndication(SccpConnection conn, ResetCause reason) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onResetConfirm(SccpConnection conn) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onData(SccpConnection conn, ByteBuf data) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onDisconnectConfirm(SccpConnection conn) {
            // TODO Auto-generated method stub
            
        }

    }
    
}
