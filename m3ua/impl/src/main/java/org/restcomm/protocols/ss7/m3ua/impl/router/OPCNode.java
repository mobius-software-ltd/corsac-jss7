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
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class OPCNode {

    protected int dpc;
    protected int opc;
    protected ConcurrentHashMap<Integer,SINode> siList = new ConcurrentHashMap<Integer,SINode>();

    // Reference to wild card SINode. If no matching SINode found for passed si
    // and wildcard defined, use wildcard one.
    private SINode wildCardSINode;

    protected OPCNode(int dpc, int opc) {
        this.dpc = dpc;
        this.opc = opc;
    }

    protected void addSi(int si, AsImpl asImpl) throws Exception {
    	SINode siNode=siList.get(si);
    	if(siNode!=null)
    		throw new Exception(String.format("Service indicator %d already exist for OPC %d and DPC %d", si, opc, dpc));
    	        
        siNode = new SINode(si, asImpl);
        SINode oldNode=siList.putIfAbsent(si, siNode);
        if(oldNode!=null)
        	throw new Exception(String.format("Service indicator %d already exist for OPC %d and DPC %d", si, opc, dpc));
	    
        if (si == -1) {
            wildCardSINode = siNode;
        }
    }

    protected AsImpl getAs(short si) {
    	SINode siNode=siList.get(Integer.valueOf(si));
    	if(siNode!=null) {
    		return siNode.asImpl;
    	}
    	
        if (wildCardSINode != null) {
            return wildCardSINode.asImpl;
        }
        return null;
    }
}
