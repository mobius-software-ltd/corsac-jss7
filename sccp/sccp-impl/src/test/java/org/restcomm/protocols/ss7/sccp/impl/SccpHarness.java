/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
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

package org.restcomm.protocols.ss7.sccp.impl;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.restcomm.protocols.ss7.sccp.Router;
import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.sccp.SccpResource;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;

import com.mobius.software.common.dal.timers.WorkerPool;

/**
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public abstract class SccpHarness {

	protected boolean onlyOneStack;

	protected String sccpStack1Name = null;
	protected String sccpStack2Name = null;

	protected SccpStackImpl sccpStack1;
	protected SccpProvider sccpProvider1;

	protected SccpStackImpl sccpStack2;
	protected SccpProvider sccpProvider2;

	protected Mtp3UserPartImpl mtp3UserPart1;
	protected Mtp3UserPartImpl mtp3UserPart2;

	protected Router router1 = null;
	protected Router router2 = null;

	protected SccpResource resource1 = null;
	protected SccpResource resource2 = null;

	protected ParameterFactory parameterFactory;
	protected WorkerPool workerPool;

	/**
	 *
	 */
	public SccpHarness() {
		workerPool = new WorkerPool(1L);
		workerPool.start(64);

		mtp3UserPart1 = new Mtp3UserPartImpl(this, workerPool.getQueue(), workerPool.getPeriodicQueue());
		mtp3UserPart2 = new Mtp3UserPartImpl(this, workerPool.getQueue(), workerPool.getPeriodicQueue());

		mtp3UserPart1.setOtherPart(mtp3UserPart2);
		mtp3UserPart2.setOtherPart(mtp3UserPart1);
	}

	protected void createStack1() {
		sccpStack1 = createStack(sccpStack1Name);
	}

	protected void createStack2() {
		sccpStack2 = createStack(sccpStack2Name);
	}

	protected SccpStackImpl createStack(final String name) {
		SccpStackImpl stack = new SccpStackImpl(name, workerPool.getPeriodicQueue());
		return stack;
	}

	protected void setUpStack1() throws Exception {
		createStack1();

		sccpStack1.setMtp3UserPart(1, mtp3UserPart1);
		sccpStack1.start();
		sccpStack1.removeAllResourses();
		sccpStack1.getRouter().addMtp3ServiceAccessPoint(1, 1, getStack1PC(), 2, 0, null);
		sccpStack1.getRouter().addMtp3Destination(1, 1, getStack2PC(), getStack2PC(), 0, 255, 255);

		sccpProvider1 = sccpStack1.getSccpProvider();

		router1 = sccpStack1.getRouter();

		resource1 = sccpStack1.getSccpResource();

		resource1.addRemoteSpc(1, getStack2PC(), 0, 0);
		resource1.addRemoteSsn(1, getStack2PC(), getSSN2(), 0, false);
		this.parameterFactory = this.sccpProvider1.getParameterFactory();

	}

	protected void setUpStack2() throws Exception {
		createStack2();

		sccpStack2.setMtp3UserPart(1, mtp3UserPart2);
		sccpStack2.start();
		sccpStack2.removeAllResourses();
		sccpStack2.getRouter().addMtp3ServiceAccessPoint(1, 1, getStack2PC(), 2, 0, null);
		sccpStack2.getRouter().addMtp3Destination(1, 1, getStack1PC(), getStack1PC(), 0, 255, 255);

		sccpProvider2 = sccpStack2.getSccpProvider();

		router2 = sccpStack2.getRouter();

		resource2 = sccpStack2.getSccpResource();

		resource2.addRemoteSpc(02, getStack1PC(), 0, 0);
		resource2.addRemoteSsn(1, getStack1PC(), getSSN(), 0, false);

	}

	private void tearDownStack1() {
		sccpStack1.removeAllResourses();
		sccpStack1.stop();
	}

	private void tearDownStack2() {
		sccpStack2.removeAllResourses();
		sccpStack2.stop();
	}

	protected int getStack1PC() {
		if (sccpStack1.getSccpProtocolVersion() == SccpProtocolVersion.ANSI)
			return 8000001;
		else
			return 1;
	}

	protected int getStack2PC() {
		if (onlyOneStack)
			return getStack1PC();

		if (sccpStack1.getSccpProtocolVersion() == SccpProtocolVersion.ANSI)
			return 8000002;
		else
			return 2;
	}

	protected int getSSN() {
		return 8;
	}

	protected int ssn2 = 8;

	protected int getSSN2() {
		return ssn2;
	}

	public void setUp() throws Exception {
		this.setUpStack1();
		if (!onlyOneStack)
			this.setUpStack2();
	}

	public void tearDown() {
		this.workerPool.stop();

		this.tearDownStack1();
		if (!onlyOneStack)
			this.tearDownStack2();
	}

	protected int tsnNum = (new Random()).nextInt(100000);

	public void assertBothConnectionsExist() {
		if (sccpStack1 != sccpStack2) {
			assertEquals(sccpStack1.getConnectionsNumber(), 1);
			assertEquals(sccpStack2.getConnectionsNumber(), 1);
		} else
			assertEquals(sccpStack1.getConnectionsNumber(), 2);
	}

	public boolean isBothConnectionsExist() {
		if (sccpStack1 != sccpStack2)
			return sccpStack1.getConnectionsNumber() == 1 && sccpStack2.getConnectionsNumber() == 1;
		else
			return sccpStack1.getConnectionsNumber() == 2;
	}

	public SccpConnection getConn2() {
		if (sccpStack1 != sccpStack2)
			return sccpProvider2.getConnections().values().iterator().next();
		else
			return sccpProvider2.getConnections().get(sccpStack2.referenceNumberCounter.get());
	}
}
