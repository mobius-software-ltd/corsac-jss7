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

package org.restcomm.protocols.ss7.m3ua.impl.router;

import java.util.concurrent.ConcurrentHashMap;

import org.restcomm.protocols.ss7.m3ua.impl.AsImpl;

/**
 * <p>
 * dpc is mandatory in deciding the correct AS to route the MTP3 traffic.
 * </p>
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class DPCNode {
    int dpc = -1;

    private ConcurrentHashMap<Integer,OPCNode> opcList = new ConcurrentHashMap<Integer,OPCNode>();

    private OPCNode wildCardOpcNode = null;

    public DPCNode(int dpc) {
        this.dpc = dpc;
    }

    protected void addSi(int opc, int si, AsImpl asImpl) throws Exception {
    	OPCNode opcNode=opcList.get(opc);
        if(opcNode==null) {
        	opcNode = new OPCNode(this.dpc, opc);
        	OPCNode oldNode=opcList.putIfAbsent(opc, opcNode);
        	if(oldNode!=null)
        		opcNode=oldNode;
        }
    	
        opcNode.addSi(si, asImpl);
        
        if (opcNode.opc == -1) {
            wildCardOpcNode = opcNode;
        }
    }

    protected AsImpl getAs(int opc, short si) {
    	OPCNode opcNode=opcList.get(opc);
    	if(opcNode!=null) {
    		return opcNode.getAs(si);
    	}
    	
        if (wildCardOpcNode != null) {
            return wildCardOpcNode.getAs(si);
        }
        
        return null;
    }
}
