/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.sccp.impl.router;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.restcomm.protocols.ss7.sccp.LongMessageRule;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.Mtp3ServiceAccessPoint;
import org.restcomm.protocols.ss7.sccp.OriginationType;
import org.restcomm.protocols.ss7.sccp.Router;
import org.restcomm.protocols.ss7.sccp.Rule;
import org.restcomm.protocols.ss7.sccp.RuleType;
import org.restcomm.protocols.ss7.sccp.SccpStack;
import org.restcomm.protocols.ss7.sccp.impl.SccpOAMMessage;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * The default implementation for the SCCP router.
 * </p>
 *
 * <p>
 * The SCCP router allows to add/remove/list routing rules and implements persistence for the routing rules.
 * </p>
 * <p>
 * RouterImpl when {@link #start() started} looks for file <tt>sccprouter.xml</tt> containing serialized information of
 * underlying {@link RuleImpl}. Set the directory path by calling {@link #setPersistDir(String)} to direct RouterImpl to look at
 * specified directory for underlying serialized file.
 * </p>
 * <p>
 * If directory path is not set, RouterImpl searches for system property <tt>sccprouter.persist.dir</tt> to get the path for
 * directory
 * </p>
 *
 * <p>
 * Even if <tt>sccprouter.persist.dir</tt> system property is not set, RouterImpl will look at property <tt>user.dir</tt>
 * </p>
 *
 * <p>
 * Implementation of SCCP routing mechanism makes routing decisions based on rules. Each rule consists of three elements:
 * <ul>
 * <li>
 * <p>
 * The <i>pattern</i> determines pattern to which destination address is compared. It has complex structure which looks as
 * follows:
 * <ul>
 * <li>
 * <p>
 * <i>translation type</i> (tt) integer numer which is used in a network to indicate the preferred method of global title
 * analysis
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>numbering plan</i> (np) integer value which inidcates which numbering plan will be used for the global title. Its value
 * aids the routing system in determining the correct network system to route message to.
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>nature of address</i> (noa) integer value which indicates address type., Specifically it indicates the scope of the
 * address value, such as whether it is an international number (i.e. including the country code), a "national" or domestic
 * number (i.e. without country code), and other formats such as "local" format.
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>digits</i> (digits) actual address
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>sub-system number</i> (ssn) identifies application in SCCP routing network.
 * </p>
 * </li>
 * </ul>
 * </p>
 * </li>
 * <li>
 * <p>
 * The <i>translation</i> determines target for messages which destination address matches pattern. It has exactly the same
 * structure as pattern .
 * </p>
 * </li>
 * <li>
 * <p>
 * The <i>mtpinfo</i> determines mtp layer information. If translation does not indicate local address, this information is used
 * to send message through MTP layer. It has following structure:
 * <ul>
 * <li>
 * <p>
 * <i>name</i> (name) identifying one of link sets used by SCCP
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>originating point code</i> (opc) local point code used as originating MTP address
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>adjacent point code</i> (apc) remote point code used as destination MTP address
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>signaling link selection</i> (sls) indentifies link in set
 * </p>
 * </li>
 * </ul>
 * </p>
 * </li>
 * </ul>
 * </p>
 * <p>
 * While the <i>pattern</i> is mandatory, <i>translation</i> and <i>mtpinfo</i> is optional. Following combinations are possible
 * <ul>
 * <li>
 * <p>
 * <i>pattern</i> and <i>translation</i> : specifies local routing
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>pattern</i> and <i>mtpinfo</i> : specifies remote routing using specified mtp routing info and no translation needed
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>pattern</i>, <i>translation</i> and <i>mtpinfo</i> specifies remote routing using specified mtp routing info after
 * applying specified translation
 * </p>
 * </li>
 * </ul>
 * </p>
 *
 * @author amit bhayani
 * @author kulikov
 */
public class RouterImpl implements Router {
    private static final Logger logger = Logger.getLogger(RouterImpl.class);

    // rule list
    private RuleMap rulesMap = new RuleMap();
    private SccpAddressMap routingAddresses = new SccpAddressMap();
    private LongMessageRuleMap longMessageRules = new LongMessageRuleMap();
    private Mtp3ServiceAccessPointMap saps = new Mtp3ServiceAccessPointMap();

    private final String name;
    private final SccpStack sccpStack;

    public RouterImpl(String name, SccpStack sccpStack) {
        this.name = name;
        this.sccpStack = sccpStack;               
    }

    public String getName() {
        return name;
    }

    public void start() {
     logger.info("Started SCCP Router");
    }

    public void stop() {     
    }

    /**
     * Looks up rule for translation.
     *
     * @param calledParty called party address
     * @return the rule with match to the called party address
     */
    public Rule findRule(SccpAddress calledParty, SccpAddress callingParty, boolean isMtpOriginated, int msgNetworkId) {
    	Iterator<Rule> iterator=this.rulesMap.values().iterator();
        while(iterator.hasNext()) {
            Rule rule = iterator.next();
            if (rule.matches(calledParty, callingParty, isMtpOriginated, msgNetworkId)) {
                return rule;
            }
        }
        return null;
    }

    public LongMessageRule findLongMessageRule(int dpc) {
    	Iterator<LongMessageRule> iterator=this.longMessageRules.values().iterator();
        while(iterator.hasNext()) {
            LongMessageRule rule = iterator.next();
            if (rule.matches(dpc)) {
                return rule;
            }
        }
        return null;
    }

    public Mtp3ServiceAccessPoint findMtp3ServiceAccessPoint(int dpc, int sls) {
    	Iterator<Mtp3ServiceAccessPoint> iterator=this.saps.values().iterator();
        while(iterator.hasNext()) {
            Mtp3ServiceAccessPoint sap = iterator.next();
            if (sap.matches(dpc, sls)) {
                return sap;
            }
        }
        return null;
    }

    public Mtp3ServiceAccessPoint findMtp3ServiceAccessPoint(int dpc, int sls, int networkId) {
    	Iterator<Mtp3ServiceAccessPoint> iterator=this.saps.values().iterator();
        while(iterator.hasNext()) {
            Mtp3ServiceAccessPoint sap = iterator.next();
            if (sap.matches(dpc, sls)) {
                if (sap.getNetworkId() == networkId) {
                    return sap;
                }
            }
        }
        return null;
    }

    public Mtp3ServiceAccessPoint findMtp3ServiceAccessPointForIncMes(int localPC, int remotePC, String localGtDigits) {
    	Iterator<Mtp3ServiceAccessPoint> iterator=this.saps.values().iterator();
        while(iterator.hasNext()) {
            Mtp3ServiceAccessPoint sap = iterator.next();
            if (sap.getLocalGtDigits() != null && sap.getLocalGtDigits().length() > 0) {
                if (sap.getOpc() == localPC && sap.matches(remotePC)
                        && (localGtDigits != null && localGtDigits.equals(sap.getLocalGtDigits()))) {
                    return sap;
                }
            }
        }

        // a second step - sap's without LocalGtDigits
        iterator=this.saps.values().iterator();
        while(iterator.hasNext()) {
            Mtp3ServiceAccessPoint sap = iterator.next();
            if (sap.getLocalGtDigits() == null || sap.getLocalGtDigits().length() == 0) {
                if (sap.getOpc() == localPC && sap.matches(remotePC)) {
                    return sap;
                }
            }
        }

        return null;
    }

    public Rule getRule(int id) {
        return this.rulesMap.get(id);
    }

    public SccpAddress getRoutingAddress(int id) {
        return this.routingAddresses.get(id);
    }

    // public SccpAddress getBackupAddress(int id) {
    // return this.backupAddresses.get(id);
    // }

    public LongMessageRule getLongMessageRule(int id) {
        return this.longMessageRules.get(id);
    }

    public Mtp3ServiceAccessPoint getMtp3ServiceAccessPoint(int id) {
        return this.saps.get(id);
    }

    public boolean spcIsLocal(int spc) {
    	Iterator<Mtp3ServiceAccessPoint> iterator=this.saps.values().iterator();
        while(iterator.hasNext()) {
            Mtp3ServiceAccessPoint sap = iterator.next();
            if (sap.getOpc() == spc) {
                return true;
            }
        }
        return false;
    }

    public Map<Integer, Rule> getRules() {
        Map<Integer, Rule> rulesMapTmp = new HashMap<Integer, Rule>();
        rulesMapTmp.putAll(rulesMap);
        return rulesMapTmp;
    }

    public Map<Integer, SccpAddress> getRoutingAddresses() {
        Map<Integer, SccpAddress> routingAddressesTmp = new HashMap<Integer, SccpAddress>();
        routingAddressesTmp.putAll(routingAddresses);
        return routingAddressesTmp;
    }

    // public Map<Integer, SccpAddress> getBackupAddresses() {
    // return backupAddresses.unmodifiable();
    // }

    public Map<Integer, LongMessageRule> getLongMessageRules() {
        Map<Integer, LongMessageRule> longMessageRulesTmp = new HashMap<Integer, LongMessageRule>();
        longMessageRulesTmp.putAll(longMessageRules);
        return longMessageRulesTmp;
    }

    public Map<Integer, Mtp3ServiceAccessPoint> getMtp3ServiceAccessPoints() {
        Map<Integer, Mtp3ServiceAccessPoint> sapsTmp = new HashMap<Integer, Mtp3ServiceAccessPoint>();
        sapsTmp.putAll(saps);
        return sapsTmp;
    }

    public void addRule(int id, RuleType ruleType, LoadSharingAlgorithm algo, OriginationType originationType, SccpAddress pattern, String mask,
            int pAddressId, int sAddressId, Integer newCallingPartyAddressAddressId, int networkId, SccpAddress patternCallingAddress) throws Exception {

        Rule ruleTmp = this.getRule(id);

        if (ruleTmp != null) {
            throw new Exception(SccpOAMMessage.RULE_ALREADY_EXIST);
        }

        int maskumberOfSecs = (mask.split("/").length - 1);
        int patternNumberOfSecs = (pattern.getGlobalTitle().getDigits().split("/").length - 1);

        if (maskumberOfSecs != patternNumberOfSecs) {
            throw new Exception(SccpOAMMessage.SEC_MISMATCH_PATTERN);
        }

        SccpAddress pAddress = this.getRoutingAddress(pAddressId);
        if (pAddress == null) {
            throw new Exception(String.format(SccpOAMMessage.NO_PRIMARY_ADDRESS, pAddressId));
        }

        int primAddNumberOfSecs = (pAddress.getGlobalTitle().getDigits().split("/").length - 1);
        if (maskumberOfSecs != primAddNumberOfSecs) {
            throw new Exception(SccpOAMMessage.SEC_MISMATCH_PRIMADDRESS);
        }

        if (sAddressId != -1) {
            SccpAddress sAddress = this.getRoutingAddress(sAddressId);
            if (sAddress == null) {
                throw new Exception(String.format(SccpOAMMessage.NO_BACKUP_ADDRESS, sAddressId));
            }

            int secAddNumberOfSecs = (sAddress.getGlobalTitle().getDigits().split("/").length - 1);
            if (maskumberOfSecs != secAddNumberOfSecs) {
                throw new Exception(SccpOAMMessage.SEC_MISMATCH_SECADDRESS);
            }
        }

        if (sAddressId == -1 && ruleType != RuleType.SOLITARY) {
            throw new Exception(SccpOAMMessage.RULETYPE_NOT_SOLI_SEC_ADD_MANDATORY);
        }

        RuleImpl rule = new RuleImpl(ruleType, algo, originationType, pattern, mask, networkId, patternCallingAddress);
        rule.setPrimaryAddressId(pAddressId);
        rule.setSecondaryAddressId(sAddressId);
        rule.setNewCallingPartyAddressId(newCallingPartyAddressAddressId);

        rule.setRuleId(id);
        this.rulesMap.put(id, rule);        
    }

    public void modifyRule(int id, RuleType ruleType, LoadSharingAlgorithm algo, OriginationType originationType, SccpAddress pattern, String mask,
            int pAddressId, int sAddressId, Integer newCallingPartyAddressAddressId, int networkId, SccpAddress patternCallingAddress) throws Exception {
    	Rule ruleTmp = this.getRule(id);

        if (ruleTmp == null) {
            throw new Exception(String.format(SccpOAMMessage.RULE_DOESNT_EXIST, name));
        }

        int maskumberOfSecs = (mask.split("/").length - 1);
        int patternNumberOfSecs = (pattern.getGlobalTitle().getDigits().split("/").length - 1);

        if (maskumberOfSecs != patternNumberOfSecs) {
            throw new Exception(SccpOAMMessage.SEC_MISMATCH_PATTERN);
        }

        SccpAddress pAddress = this.getRoutingAddress(pAddressId);

        if (pAddress == null) {
            throw new Exception(String.format(SccpOAMMessage.NO_PRIMARY_ADDRESS, pAddressId));
        }
        int primAddNumberOfSecs = (pattern.getGlobalTitle().getDigits().split("/").length - 1);
        if (maskumberOfSecs != primAddNumberOfSecs) {
            throw new Exception(SccpOAMMessage.SEC_MISMATCH_PRIMADDRESS);
        }

        if (sAddressId != -1) {
            SccpAddress sAddress = this.getRoutingAddress(sAddressId);
            if (sAddress == null) {
                throw new Exception(String.format(SccpOAMMessage.NO_BACKUP_ADDRESS, sAddressId));
            }
            int secAddNumberOfSecs = (pattern.getGlobalTitle().getDigits().split("/").length - 1);
            if (maskumberOfSecs != secAddNumberOfSecs) {
                throw new Exception(SccpOAMMessage.SEC_MISMATCH_SECADDRESS);
            }
        }

        if (sAddressId == -1 && ruleType != RuleType.SOLITARY) {
            throw new Exception(SccpOAMMessage.RULETYPE_NOT_SOLI_SEC_ADD_MANDATORY);
        }

        this.removeRule( id );
    }

    public void removeRule(int id) throws Exception {

        if (this.getRule(id) == null) {
            throw new Exception(String.format(SccpOAMMessage.RULE_DOESNT_EXIST, name));
        }

        this.rulesMap.remove(id);
    }

    public void addRoutingAddress(int primAddressId, SccpAddress primaryAddress) throws Exception {

        if (this.getRoutingAddress(primAddressId) != null) {
            throw new Exception(SccpOAMMessage.ADDRESS_ALREADY_EXIST);
        }

        this.routingAddresses.put(primAddressId, (SccpAddressImpl) primaryAddress);
    }

    public void modifyRoutingAddress(int primAddressId, SccpAddress primaryAddress) throws Exception {
        if (this.getRoutingAddress(primAddressId) == null) {
            throw new Exception(String.format(SccpOAMMessage.ADDRESS_DOESNT_EXIST, name));
        }

        this.routingAddresses.put(primAddressId, (SccpAddressImpl) primaryAddress);
    }

    public void removeRoutingAddress(int id) throws Exception {
        if (this.getRoutingAddress(id) == null) {
            throw new Exception(String.format(SccpOAMMessage.ADDRESS_DOESNT_EXIST, name));
        }

        this.routingAddresses.remove(id);
    }

    public void addLongMessageRule(int id, int firstSpc, int lastSpc, LongMessageRuleType ruleType) throws Exception {
        if (this.getLongMessageRule(id) != null) {
            throw new Exception(SccpOAMMessage.LMR_ALREADY_EXIST);
        }

        LongMessageRuleImpl longMessageRule = new LongMessageRuleImpl(firstSpc, lastSpc, ruleType);
        this.longMessageRules.put(id, longMessageRule);
    }

    public void modifyLongMessageRule(int id, int firstSpc, int lastSpc, LongMessageRuleType ruleType) throws Exception {
        if (this.getLongMessageRule(id) == null) {
            throw new Exception(String.format(SccpOAMMessage.LMR_DOESNT_EXIST, name));
        }

        LongMessageRuleImpl longMessageRule = new LongMessageRuleImpl(firstSpc, lastSpc, ruleType);
        this.longMessageRules.put(id, longMessageRule);
    }

    public void modifyLongMessageRule(int id, Integer firstSpc, Integer lastSpc, LongMessageRuleType ruleType) throws Exception {

        LongMessageRule oldLmr = this.getLongMessageRule(id);
        if (oldLmr == null) {
            throw new Exception(String.format(SccpOAMMessage.LMR_DOESNT_EXIST, name));
        }

        if(firstSpc == null)
            firstSpc = oldLmr.getFirstSpc();
        if(lastSpc == null)
            lastSpc = oldLmr.getLastSpc();
        if(ruleType == null)
            ruleType = oldLmr.getLongMessageRuleType();

        LongMessageRuleImpl longMessageRule = new LongMessageRuleImpl(firstSpc, lastSpc, ruleType);
        this.longMessageRules.put(id, longMessageRule);
    }
    
    public void removeLongMessageRule(int id) throws Exception {

        if (this.getLongMessageRule(id) == null) {
            throw new Exception(String.format(SccpOAMMessage.LMR_DOESNT_EXIST, name));
        }
        
        this.longMessageRules.remove(id);
    }

    public void addMtp3Destination(int sapId, int destId, int firstDpc, int lastDpc, int firstSls, int lastSls, int slsMask)
            throws Exception {
        Mtp3ServiceAccessPoint sap = this.getMtp3ServiceAccessPoint(sapId);
        if (sap == null) {
            throw new Exception(String.format(SccpOAMMessage.SAP_DOESNT_EXIST, name));
        }
        // TODO Synchronize??
        sap.addMtp3Destination(destId, firstDpc, lastDpc, firstSls, lastSls, slsMask);        
    }

    public void modifyMtp3Destination(int sapId, int destId, int firstDpc, int lastDpc, int firstSls, int lastSls, int slsMask)
            throws Exception {
        Mtp3ServiceAccessPoint sap = this.getMtp3ServiceAccessPoint(sapId);

        if (sap == null) {
            throw new Exception(String.format(SccpOAMMessage.SAP_DOESNT_EXIST, name));
        }
        // TODO Synchronize??
        sap.modifyMtp3Destination(destId, firstDpc, lastDpc, firstSls, lastSls, slsMask);        
    }

    public void modifyMtp3Destination(int sapId, int destId, Integer firstDpc, Integer lastDpc, Integer firstSls, Integer lastSls, Integer slsMask)
            throws Exception {
        Mtp3ServiceAccessPoint sap = this.getMtp3ServiceAccessPoint(sapId);
        if (sap == null)
            throw new Exception(String.format(SccpOAMMessage.SAP_DOESNT_EXIST, name));

        Mtp3DestinationImpl dest = (Mtp3DestinationImpl) sap.getMtp3Destination(destId);

        if(dest == null)
            throw new Exception(String.format(SccpOAMMessage.DEST_DOESNT_EXIST, name));

        if(firstDpc == null)
            firstDpc = dest.getFirstDpc();
        if(lastDpc == null)
            lastDpc = dest.getLastDpc();
        if(firstSls == null)
            firstSls = dest.getFirstSls();
        if(lastSls == null)
            lastSls = dest.getLastSls();
        if(slsMask == null)
            slsMask = dest.getSlsMask();

        sap.modifyMtp3Destination(destId, firstDpc, lastDpc, firstSls, lastSls, slsMask);
    }
    
    public void removeMtp3Destination(int sapId, int destId) throws Exception {
        Mtp3ServiceAccessPoint sap = this.getMtp3ServiceAccessPoint(sapId);

        if (sap == null) {
            throw new Exception(String.format(SccpOAMMessage.SAP_DOESNT_EXIST, name));
        }

        sap.removeMtp3Destination(destId);
    }

    public void addMtp3ServiceAccessPoint(int id, int mtp3Id, int opc, int ni, int networkId, String localGtDigits) throws Exception {

        if (this.getMtp3ServiceAccessPoint(id) != null) {
            throw new Exception(SccpOAMMessage.SAP_ALREADY_EXIST);
        }

        if (this.sccpStack.getMtp3UserPart(mtp3Id) == null) {
            throw new Exception(SccpOAMMessage.MUP_DOESNT_EXIST);
        }

        if (localGtDigits != null && (localGtDigits.equals("null") || localGtDigits.equals("")))
            localGtDigits = null;


        Mtp3ServiceAccessPointImpl sap = new Mtp3ServiceAccessPointImpl(mtp3Id, opc, ni, this.name, networkId, localGtDigits);
        this.saps.put(id, sap);
    }

    public void modifyMtp3ServiceAccessPoint(int id, int mtp3Id, int opc, int ni, int networkId, String localGtDigits) throws Exception {
        if (this.getMtp3ServiceAccessPoint(id) == null) {
            throw new Exception(String.format(SccpOAMMessage.SAP_DOESNT_EXIST, name));
        }

        if (this.sccpStack.getMtp3UserPart(mtp3Id) == null) {
            throw new Exception(SccpOAMMessage.MUP_DOESNT_EXIST);
        }

        if (localGtDigits != null && (localGtDigits.equals("null") || localGtDigits.equals("")))
            localGtDigits = null;

        Mtp3ServiceAccessPointImpl sap = new Mtp3ServiceAccessPointImpl(mtp3Id, opc, ni, this.name, networkId, localGtDigits);
        this.saps.put(id, sap);
    }

    public void modifyMtp3ServiceAccessPoint(int id, Integer mtp3Id, Integer opc, Integer ni, Integer networkId, String localGtDigits) throws Exception {
        Mtp3ServiceAccessPointImpl sap = (Mtp3ServiceAccessPointImpl) this.getMtp3ServiceAccessPoint(id);
        if (sap == null) {
            throw new Exception(String.format(SccpOAMMessage.SAP_DOESNT_EXIST, name));
        }

        if (mtp3Id != null && this.sccpStack.getMtp3UserPart(mtp3Id) == null) {
            throw new Exception(SccpOAMMessage.MUP_DOESNT_EXIST);
        }

        if (localGtDigits != null && (localGtDigits.equals("null") || localGtDigits.equals("")))
            localGtDigits = null;

        if(mtp3Id == null)
            mtp3Id = sap.getMtp3Id();
        if(opc == null)
            opc = sap.getOpc();
        if(ni == null)
            ni = sap.getNi();
        if(networkId == null)
            networkId = sap.getNetworkId();
        if(localGtDigits == null)
            localGtDigits = sap.getLocalGtDigits();

        Mtp3ServiceAccessPointImpl newSap = new Mtp3ServiceAccessPointImpl(mtp3Id, opc, ni, this.name, networkId, localGtDigits);
        this.saps.put(id, newSap);
    }
    
    public void removeMtp3ServiceAccessPoint(int id) throws Exception {

        if (this.getMtp3ServiceAccessPoint(id) == null) {
            throw new Exception(String.format(SccpOAMMessage.SAP_DOESNT_EXIST, name));
        }

        this.saps.remove(id);
    }

    public void removeAllResourses() {
    	// if (this.rulesMap.size() == 0 && this.routingAddresses.size() == 0 && this.backupAddresses.size() == 0
        // && this.longMessageRules.size() == 0 && this.saps.size() == 0)
        if (this.rulesMap.size() == 0 && this.routingAddresses.size() == 0 && this.longMessageRules.size() == 0
                && this.saps.size() == 0)
            // no resources allocated - nothing to do
            return;

        rulesMap = new RuleMap();
        routingAddresses = new SccpAddressMap();
        // backupAddresses = new SccpAddressMap<Integer, SccpAddress>();
        longMessageRules = new LongMessageRuleMap();
        saps = new Mtp3ServiceAccessPointMap();
    }
}