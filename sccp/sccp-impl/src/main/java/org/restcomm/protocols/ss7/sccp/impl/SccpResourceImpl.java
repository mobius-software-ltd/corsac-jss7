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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.ConcernedSignalingPointCode;
import org.restcomm.protocols.ss7.sccp.RemoteSignalingPointCode;
import org.restcomm.protocols.ss7.sccp.RemoteSubSystem;
import org.restcomm.protocols.ss7.sccp.SccpResource;

/**
 * @author amit bhayani
 * @author yulianoifa
 */
public class SccpResourceImpl implements SccpResource {
    private static final Logger logger = LogManager.getLogger(SccpResourceImpl.class);

    protected RemoteSubSystemMap remoteSsns = new RemoteSubSystemMap();
    protected RemoteSignalingPointCodeMap remoteSpcs = new RemoteSignalingPointCodeMap();
    protected ConcernedSignalingPointCodeMap concernedSpcs = new ConcernedSignalingPointCodeMap();

    private final String name;
    protected final boolean rspProhibitedByDefault;
    
    public SccpResourceImpl(String name) {
        this(name, false);
    }

    public SccpResourceImpl(String name, boolean rspProhibitedByDefault) {
        this.name = name;
        this.rspProhibitedByDefault = rspProhibitedByDefault;
    }

    public void start() {
        logger.info("Started Sccp Resource");
    }

    public void stop() {
    }

    public void addRemoteSsn(int remoteSsnid, int remoteSpc, int remoteSsn, int remoteSsnFlag,
                             boolean markProhibitedWhenSpcResuming) throws Exception {

        if (this.getRemoteSsn(remoteSsnid) != null) {
            throw new Exception(SccpOAMMessage.RSS_ALREADY_EXIST);
        }

        // TODO check if RemoteSignalingPointCode correspond to remoteSpc exist?

        RemoteSubSystemImpl rsscObj = new RemoteSubSystemImpl(remoteSpc, remoteSsn, remoteSsnFlag,
                markProhibitedWhenSpcResuming);

        this.remoteSsns.put(remoteSsnid, rsscObj);        
    }

    public void modifyRemoteSsn(int remoteSsnid, int remoteSpc, int remoteSsn, int remoteSsnFlag,
                                boolean markProhibitedWhenSpcResuming) throws Exception {
        RemoteSubSystemImpl rsscObj = (RemoteSubSystemImpl) this.remoteSsns.get(remoteSsnid);
        if (rsscObj == null) {
            throw new Exception(String.format(SccpOAMMessage.RSS_DOESNT_EXIST, this.name));
        }

        rsscObj.setRemoteSsn(remoteSsn);
        rsscObj.setRemoteSpc(remoteSpc);
        rsscObj.setRemoteSsnFlag(remoteSsnFlag);
        rsscObj.setMarkProhibitedWhenSpcResuming(markProhibitedWhenSpcResuming);        
    }

    public void modifyRemoteSsn(int remoteSsnid, Integer remoteSpc, Integer remoteSsn, Integer remoteSsnFlag,
            Boolean markProhibitedWhenSpcResuming) throws Exception {
        RemoteSubSystemImpl rsscObj = (RemoteSubSystemImpl) this.remoteSsns.get(remoteSsnid);
        if (rsscObj == null) {
            throw new Exception(String.format(SccpOAMMessage.RSS_DOESNT_EXIST, this.name));
        }

        if(remoteSsn!=null)
            rsscObj.setRemoteSsn(remoteSsn);
        if(remoteSpc!=null)
            rsscObj.setRemoteSpc(remoteSpc);
        if(remoteSsnFlag!=null)
            rsscObj.setRemoteSsnFlag(remoteSsnFlag);
        if(markProhibitedWhenSpcResuming != null)
            rsscObj.setMarkProhibitedWhenSpcResuming(markProhibitedWhenSpcResuming);
    }

    public void removeRemoteSsn(int remoteSsnid) throws Exception {

        if (this.getRemoteSsn(remoteSsnid) == null) {
            throw new Exception(String.format(SccpOAMMessage.RSS_DOESNT_EXIST, this.name));
        }

        this.remoteSsns.remove(remoteSsnid);
    }

    public RemoteSubSystem getRemoteSsn(int remoteSsnid) {
        return this.remoteSsns.get(remoteSsnid);
    }

    public RemoteSubSystem getRemoteSsn(int spc, int remoteSsn) {
    	Iterator<RemoteSubSystemImpl> iterator=this.remoteSsns.values().iterator();
        while(iterator.hasNext()) {
            RemoteSubSystem remoteSubSystem = iterator.next();
            if (remoteSubSystem.getRemoteSpc() == spc && remoteSsn == remoteSubSystem.getRemoteSsn()) {
                return remoteSubSystem;
            }

        }
        return null;
    }

    public Map<Integer, RemoteSubSystem> getRemoteSsns() {
        Map<Integer, RemoteSubSystem> remoteSsnsTmp = new HashMap<Integer, RemoteSubSystem>();
        remoteSsnsTmp.putAll(remoteSsns);
        return remoteSsnsTmp;
    }

    public void addRemoteSpc(int remoteSpcId, int remoteSpc, int remoteSpcFlag, int mask) throws Exception {

        if (this.getRemoteSpc(remoteSpcId) != null) {
            throw new Exception(SccpOAMMessage.RSPC_ALREADY_EXIST);
        }

        RemoteSignalingPointCodeImpl rspcObj = new RemoteSignalingPointCodeImpl(remoteSpc, remoteSpcFlag, mask, this.rspProhibitedByDefault);
        this.remoteSpcs.put(remoteSpcId, rspcObj);
    }

    public void modifyRemoteSpc(int remoteSpcId, int remoteSpc, int remoteSpcFlag, int mask) throws Exception {
        RemoteSignalingPointCodeImpl remoteSignalingPointCode = (RemoteSignalingPointCodeImpl) this.getRemoteSpc(remoteSpcId);
        if (remoteSignalingPointCode == null) {
            throw new Exception(String.format(SccpOAMMessage.RSPC_DOESNT_EXIST, this.name));
        }

        remoteSignalingPointCode.setRemoteSpc(remoteSpc);
        remoteSignalingPointCode.setRemoteSpcFlag(remoteSpcFlag);
        remoteSignalingPointCode.setMask(mask);
    }

    public void modifyRemoteSpc(int remoteSpcId, Integer remoteSpc, Integer remoteSpcFlag, Integer mask) throws Exception {
        RemoteSignalingPointCodeImpl remoteSignalingPointCode = (RemoteSignalingPointCodeImpl) this.getRemoteSpc(remoteSpcId);
        if (remoteSignalingPointCode == null) {
            throw new Exception(String.format(SccpOAMMessage.RSPC_DOESNT_EXIST, this.name));
        }

        if(remoteSpc != null)
            remoteSignalingPointCode.setRemoteSpc(remoteSpc);
        if(remoteSpcFlag != null)
            remoteSignalingPointCode.setRemoteSpcFlag(remoteSpcFlag);
        if(mask != null)
            remoteSignalingPointCode.setMask(mask);
    }

    public void removeRemoteSpc(int remoteSpcId) throws Exception {
        if (this.getRemoteSpc(remoteSpcId) == null) {
            throw new Exception(String.format(SccpOAMMessage.RSPC_DOESNT_EXIST, this.name));
        }
        
        this.remoteSpcs.remove(remoteSpcId);
    }

    public RemoteSignalingPointCode getRemoteSpc(int remoteSpcId) {
        return this.remoteSpcs.get(remoteSpcId);
    }

    public RemoteSignalingPointCode getRemoteSpcByPC(int remotePC) {
    	Iterator<RemoteSignalingPointCodeImpl> iterator=this.remoteSpcs.values().iterator();
        while(iterator.hasNext()) {
            RemoteSignalingPointCode remoteSignalingPointCode = iterator.next();
            if (remoteSignalingPointCode.getRemoteSpc() == remotePC) {
                return remoteSignalingPointCode;
            }

        }
        return null;
    }

    public Map<Integer, RemoteSignalingPointCode> getRemoteSpcs() {
        Map<Integer, RemoteSignalingPointCode> remoteSpcsTmp = new HashMap<Integer, RemoteSignalingPointCode>();
        remoteSpcsTmp.putAll(remoteSpcs);
        return remoteSpcsTmp;
    }

    public void addConcernedSpc(int concernedSpcId, int remoteSpc) throws Exception {

        if (this.getConcernedSpc(concernedSpcId) != null) {
            throw new Exception(SccpOAMMessage.CS_ALREADY_EXIST);
        }

        ConcernedSignalingPointCodeImpl concernedSpc = new ConcernedSignalingPointCodeImpl(remoteSpc);
        this.concernedSpcs.put(concernedSpcId, concernedSpc);
    }

    public void removeConcernedSpc(int concernedSpcId) throws Exception {

        if (this.getConcernedSpc(concernedSpcId) == null) {
            throw new Exception(String.format(SccpOAMMessage.CS_DOESNT_EXIST, this.name));
        }

        this.concernedSpcs.remove(concernedSpcId);
    }

    public void modifyConcernedSpc(int concernedSpcId, int remoteSpc) throws Exception {
        ConcernedSignalingPointCodeImpl concernedSignalingPointCode = (ConcernedSignalingPointCodeImpl) this
                .getConcernedSpc(concernedSpcId);

        if (concernedSignalingPointCode == null) {
            throw new Exception(String.format(SccpOAMMessage.CS_DOESNT_EXIST, this.name));
        }

        concernedSignalingPointCode.setRemoteSpc(remoteSpc);

    }

    public ConcernedSignalingPointCode getConcernedSpc(int concernedSpcId) {
        return this.concernedSpcs.get(concernedSpcId);
    }

    public ConcernedSignalingPointCode getConcernedSpcByPC(int remotePC) {
    	Iterator<ConcernedSignalingPointCodeImpl> iterator=this.concernedSpcs.values().iterator();
        while(iterator.hasNext()) {
            ConcernedSignalingPointCode concernedSubSystem = iterator.next();
            if (concernedSubSystem.getRemoteSpc() == remotePC) {
                return concernedSubSystem;
            }

        }
        return null;
    }

    public Map<Integer, ConcernedSignalingPointCode> getConcernedSpcs() {
        Map<Integer, ConcernedSignalingPointCode> concernedSpcsTmp = new HashMap<Integer, ConcernedSignalingPointCode>();
        concernedSpcsTmp.putAll(concernedSpcs);
        return concernedSpcsTmp;
    }

    public void removeAllResourses() {
    	if (this.remoteSsns.size() == 0 && this.remoteSpcs.size() == 0 && this.concernedSpcs.size() == 0)
            // no resources allocated - nothing to do
            return;

    	remoteSsns = new RemoteSubSystemMap();
        remoteSpcs = new RemoteSignalingPointCodeMap();
        concernedSpcs = new ConcernedSignalingPointCodeMap();
    }
}