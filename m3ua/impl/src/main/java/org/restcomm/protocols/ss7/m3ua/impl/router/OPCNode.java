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
