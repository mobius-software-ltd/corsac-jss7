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

import org.restcomm.protocols.ss7.sccp.LongMessageRule;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class LongMessageRuleImpl implements LongMessageRule {
    private int firstSpc;
    private int lastSpc;
    private LongMessageRuleType ruleType;

    public LongMessageRuleImpl() {
    }

    public LongMessageRuleImpl(int firstSpc, int lastSpc, LongMessageRuleType ruleType) {
        this.firstSpc = firstSpc;
        this.lastSpc = lastSpc;
        this.ruleType = ruleType;
    }

    public LongMessageRuleType getLongMessageRuleType() {
        return ruleType;
    }

    public int getFirstSpc() {
        return firstSpc;
    }

    public int getLastSpc() {
        return lastSpc;
    }

    public boolean matches(int dpc) {
        if (dpc >= this.firstSpc && dpc <= this.lastSpc)
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("firstSpc=").append(this.firstSpc).append(", lastSpc=").append(this.lastSpc).append(", ruleType=")
                .append(this.ruleType);
        return sb.toString();
    }
}
