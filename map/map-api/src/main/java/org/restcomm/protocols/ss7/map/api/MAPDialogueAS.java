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

package org.restcomm.protocols.ss7.map.api;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public enum MAPDialogueAS {

    /**
     * Look at http://www.oid-info.com/get/0.4.0.0.1.1.1.1
     */
    MAP_DialogueAS(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 1L, 1L, 1L }), 1);

    private List<Long> oid;
    private int dialogAS;

    private MAPDialogueAS(List<Long> oid, int dialogAS) {
        this.oid = oid;
        this.dialogAS = dialogAS;
    }

    public List<Long> getOID() {
        return this.oid;
    }

    public int getDialogAS() {
        return this.dialogAS;
    }

    public static MAPDialogueAS getInstance(int dialogAS) {
        switch (dialogAS) {
            case 1:
                return MAP_DialogueAS;
            default:
                return null;
        }
    }

    public static MAPDialogueAS getInstance(List<Long> oid) {
        List<Long> temp = MAP_DialogueAS.getOID();
        if (temp.equals(oid)) {
            return MAP_DialogueAS;
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        for (long l : this.oid) {
            s.append(l).append(", ");
        }
        return s.toString();
    }
}
