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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.UserServiceInformationBase;

/**
 * Start time:12:36:18 2009-04-04<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski</a>
 * @author <a href="mailto:oifa.yulian@gmail.com"> Yulian Oifa</a>
 * @author sergey vetyutnev
 */
public abstract class UserServiceInformationBaseImpl extends AbstractISUPParameter implements UserServiceInformationBase {
	private int codingStandart = 0;
    private int informationTransferCapability = 0;
    private int transferMode = 0;
    private int customInformationTransferRate = 0;
    private int informationTransferRate = 0;
    private int l1UserInformation = 0;
    private int l2UserInformation = 0;
    private int l3UserInformation = 0;

    private int syncMode = 0;
    private int negotiation = 0;
    private int userRate = 0;
    private int intermediateRate = 0;
    private int nicOnTx = 0;
    private int nicOnRx = 0;
    private int fcOnTx = 0;
    private int fcOnRx = 0;
    private int hdr = 0;
    private int multiframe = 0;
    private int mode = 0;
    private int lli = 0;
    private int assignor = 0;
    private int inBandNegotiation = 0;
    private int stopBits = 0;
    private int dataBits = 0;
    private int parity = 0;
    private int duplexMode = 0;
    private int modemType = 0;
    private int l3Protocol = 0;

    private boolean byte5aIsPresent;
    private boolean byte5bIsPresent;
    private boolean byte5cIsPresent;
    private boolean byte5dIsPresent;

    public void decode(ByteBuf b) throws ParameterException {

        if (b == null || b.readableBytes() < 2 || b.readableBytes() > 13) {
            throw new IllegalArgumentException("buffer must not be null and should be between 2 and 13 bytes in length");
        }

        int v = 0;
        int layerId = 0;
        int extBit = 0;

        // byte 0 bit 1-5 information transfer capability , 6-7 coding standart
        v = b.readByte();
        this.informationTransferCapability = v & 0x1F;
        this.codingStandart = (v >> 5) & 0x03;

        // byte 1 bit 1-5 information transfer rate , 6-7 transfer mode
        v = b.readByte();
        this.informationTransferRate = v & 0x1F;
        this.transferMode = (v >> 5) & 0x03;

        if (this.informationTransferRate == _ITR_MULTIRATE) {
            if (b.readableBytes() == 0)
                throw new IllegalArgumentException("buffer should be at least 3 bytes in length");

            v = b.readByte();
            this.customInformationTransferRate = v & 0x7F;
        }

        while (b.readableBytes()>0) {
            // byte 3-5 l1-l3 user information
            v = b.readByte();
            layerId = (v >> 5) & 0x03;
            switch (layerId) {
            case _LAYER1_IDENTIFIER:
                this.l1UserInformation = v & 0x1F;
                extBit = v & 0x80;
                // check for bytes 5a to 5d depending on l1 information

                // note 2 This octet may be present if octet 3 indicates
                // unrestricted digital information and octet 5
                // indicates either
                // of the ITU-T standardized rate adaptions V.110, I.460 and
                // X.30 or V.120 [9]. It may also be present if
                // octet 3
                // indicates 3.1 kHz audio and octet 5 indicates G.711.

                if (this.informationTransferCapability == _ITS_UNRESTRICTED_DIGITAL) {
                    switch (this.l1UserInformation) {
                    case _L1_ITUT_110:
                    case _L1_NON_ITUT:
                        // should have 5a
                        if (extBit == 0) {
                            byte5aIsPresent = true;
                            v = b.readByte();
                            this.syncMode = (v >> 6) & 0x01;
                            this.negotiation = (v >> 5) & 0x01;
                            this.userRate = v & 0x1F;
                            extBit = v & 0x80;

                            // NOTE 7 Octets 5b-5d may be omitted in the case of
                            // synchronous user rates.
                            if (extBit == 0) {
                                // 5b
                                // NOTE 3 This structure of octet 5b only
                                // applies if octet 5 indicates ITU-T
                                // standardized
                                // rate adaption (see
                                // Recommendations V.110 [7], I.460 [15] and
                                // X.30 [8]).
                                byte5bIsPresent = true;
                                v = b.readByte();
                                this.intermediateRate = (v >> 5) & 0x3;
                                this.nicOnTx = (v >> 4) & 0x1;
                                this.nicOnRx = (v >> 3) & 0x1;
                                this.fcOnTx = (v >> 2) & 0x1;
                                this.fcOnRx = (v >> 1) & 0x1;
                                extBit = v & 0x80;

                                if (extBit == 0) {
                                    // 5c
                                    byte5cIsPresent = true;
                                    v = b.readByte();
                                    this.stopBits = (v >> 5) & 0x03;
                                    this.dataBits = (v >> 3) & 0x03;
                                    this.parity = v & 0x07;
                                    extBit = v & 0x80;

                                    if (extBit == 0) {
                                        // 5d
                                        byte5dIsPresent = true;
                                        v = b.readByte();
                                        this.duplexMode = (v >> 6) & 0x1;
                                        this.modemType = v & 0x1F;
                                        extBit = v & 0x80;
                                    }
                                }
                            }
                        }
                        break;
                    case _L1_ITUT_120:
                        // should have 5a
                        if (extBit == 0) {
                            byte5aIsPresent = true;
                            v = b.readByte();
                            this.syncMode = (v >> 6) & 0x01;
                            this.negotiation = (v >> 5) & 0x01;
                            this.userRate = v & 0x1F;
                            extBit = v & 0x80;

                            if (extBit == 0) {
                                // 5b
                                // NOTE 4 This structure of octet 5b only
                                // applies if
                                // octet 5 indicates ITU-T standardized
                                // rate adaption (see
                                // Recommendation V.120 [9]).
                                byte5bIsPresent = true;
                                v = b.readByte();
                                this.hdr = (v >> 6) & 0x01;
                                this.multiframe = (v >> 5) & 0x01;
                                this.mode = (v >> 4) & 0x01;
                                this.lli = (v >> 3) & 0x01;
                                this.assignor = (v >> 2) & 0x01;
                                this.inBandNegotiation = (v >> 1) & 0x01;
                                extBit = v & 0x80;

                                if (extBit == 0) {
                                    // 5c
                                    byte5cIsPresent = true;
                                    v = b.readByte();
                                    this.stopBits = (v >> 5) & 0x03;
                                    this.dataBits = (v >> 3) & 0x03;
                                    this.parity = v & 0x07;
                                    extBit = v & 0x80;

                                    if (extBit == 0) {
                                        // 5d
                                        byte5dIsPresent = true;
                                        v = b.readByte();
                                        this.duplexMode = (v >> 6) & 0x1;
                                        this.modemType = v & 0x1F;
                                        extBit = v & 0x80;
                                    }
                                }
                            }
                        }
                        break;
                    }
                } else if (this.informationTransferCapability == _ITS_3_1_KHZ) {
                    // && this .transferMode == _TM_PACKET */
                    switch (this.l1UserInformation) {
                    case _L1_G711_MU:
                    case _L1_G711_A:
                        // read 5a
                        if (extBit == 0) {
                            byte5aIsPresent = true;
                            v = b.readByte();
                            this.syncMode = (v >> 6) & 0x01;
                            this.negotiation = (v >> 5) & 0x01;
                            this.userRate = v & 0x1F;
                            extBit = v & 0x80;
                            if (extBit == 0) {
                                // 5b
                                v = b.readByte();
                                this.intermediateRate = (v >> 5) & 0x3;
                                this.nicOnTx = (v >> 4) & 0x1;
                                this.nicOnRx = (v >> 3) & 0x1;
                                this.fcOnTx = (v >> 2) & 0x1;
                                this.fcOnRx = (v >> 1) & 0x1;
                                extBit = v & 0x80;

                                if (extBit == 0) {
                                    // 5c
                                    byte5cIsPresent = true;
                                    v = b.readByte();
                                    this.stopBits = (v >> 5) & 0x03;
                                    this.dataBits = (v >> 3) & 0x03;
                                    this.parity = v & 0x07;
                                    extBit = v & 0x80;

                                    if (extBit == 0) {
                                        // 5d
                                        byte5dIsPresent = true;
                                        v = b.readByte();
                                        this.duplexMode = (v >> 6) & 0x1;
                                        this.modemType = v & 0x1F;
                                        extBit = v & 0x80;
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
                while (b.readableBytes()>0 && extBit == 0) {
                    v = b.readByte();
                    extBit = v & 0x80;
                }
                break;

            case _LAYER2_IDENTIFIER:
                this.l2UserInformation = v & 0x1F;
                break;

            case _LAYER3_IDENTIFIER:
                this.l3UserInformation = v & 0x1F;
                // NOTE 5 This octet may be included if octet 7 indicates
                // ISO/IEC TR 9577 (Protocol Identification in the
                // network
                // layer).
                extBit = v & 0x80;
                if (extBit == 0 && this.l3UserInformation == _L3_ISO_9577) {
                    // check 2 next bytes
                    v = b.readByte();
                    this.l3Protocol = (v & 0x0F) << 4;
                    v = b.readByte();
                    this.l3Protocol |= v & 0x0F;
                }
                while (b.readableBytes()>0 && extBit == 0) {
                    v = b.readByte();
                    extBit = v & 0x80;
                }
                break;
            default:
                throw new IllegalArgumentException("invalid layer identifier");
            }
        }
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        byte b = (byte)0x80;
        b |= (this.codingStandart & 0x3) << 5;
        b |= (informationTransferCapability & 0x1f);
        buffer.writeByte(b);
        
        b = (byte)0x80;
        b |= (this.transferMode & 0x3) << 5;
        b |= (informationTransferRate & 0x1f);
        buffer.writeByte(b);
        
        if (this.informationTransferRate == _ITR_MULTIRATE) {
        	buffer.writeByte(customInformationTransferRate | 0x80);
        }

        if (this.l1UserInformation > 0) {
        	buffer.writeByte((_LAYER1_IDENTIFIER << 5) | (l1UserInformation & 0x1f));
                    	
            switch (this.l1UserInformation) {
                case _L1_ITUT_110:
                case _L1_NON_ITUT:
                    if (this.informationTransferCapability == _ITS_UNRESTRICTED_DIGITAL) {
                        if (byte5aIsPresent) {
                            b = (byte)(this.syncMode << 6);
                            b |= this.negotiation << 5;
                            b |= this.userRate;
                            buffer.writeByte(b);
                        }

                        // 5b
                        if (byte5bIsPresent) {
                            b = (byte)(this.intermediateRate << 5);
                            b |= this.nicOnTx << 4;
                            b |= this.nicOnRx << 3;
                            b |= this.fcOnTx << 2;
                            b |= this.fcOnRx << 1;
                            buffer.writeByte(b);
                        }

                        // 5c
                        if (byte5cIsPresent) {
                        	b = (byte)(this.stopBits << 5);
                        	b |= this.dataBits << 3;
                        	b |= this.parity;
                        	buffer.writeByte(b);
                        }

                        // 5d
                        // b[byteLength] |= 0x80;
                        if (byte5dIsPresent) {
                            b= (byte)(this.duplexMode << 6);
                            b |= this.modemType;
                        	buffer.writeByte(b);
                        }
                    }
                    break;
                case _L1_ITUT_120:
                    if (this.informationTransferCapability == _ITS_UNRESTRICTED_DIGITAL) {
                        if (byte5aIsPresent) {
                            b = (byte)(this.syncMode << 6);
                            b|= this.negotiation << 5;
                            b|= this.userRate;
                        	buffer.writeByte(b);
                        }

                        // 5b
                        if (byte5bIsPresent) {
                            b= (byte)(this.hdr << 6);
                            b |= this.multiframe << 5;
                            b |= this.mode << 4;
                            b |= this.lli << 3;
                            b |= this.assignor << 3;
                            b |= this.inBandNegotiation << 1;
                        	buffer.writeByte(b);
                        }

                        // 5c
                        if (byte5cIsPresent) {
                            b = (byte)(this.stopBits << 5);
                            b |= this.dataBits << 3;
                            b |= this.parity;
                        	buffer.writeByte(b);
                        }

                        // 5d
                        if (byte5dIsPresent) {
                            // b[byteLength] |= 0x80;
                            b = (byte)(this.duplexMode << 6);
                            b |= this.modemType;
                        	buffer.writeByte(b);
                        }
                    }
                    break;
                case _L1_G711_MU:
                case _L1_G711_A:
                    if (this.informationTransferCapability == _ITS_3_1_KHZ) {
                        // read 5a
                        if (byte5aIsPresent) {
                            b = (byte)(this.syncMode << 6);
                            b |= this.negotiation << 5;
                            b |= this.userRate;
                        	buffer.writeByte(b);
                        }

                        // 5b
                        if (byte5bIsPresent) {
                            b = (byte)(this.intermediateRate << 5);
                            b |= this.nicOnTx << 4;
                            b |= this.nicOnRx << 3;
                            b |= this.fcOnTx << 2;
                            b |= this.fcOnRx << 1;
                        	buffer.writeByte(b);
                        }

                        // 5c
                        if (byte5cIsPresent) {
                            b = (byte)(this.stopBits << 5);
                            b |= this.dataBits << 3;
                            b |= this.parity;
                        	buffer.writeByte(b);
                        }

                        // 5d
                        if (byte5dIsPresent) {
                            // b[byteLength] |= 0x80;
                            b = (byte)(this.duplexMode << 6);
                            b |= this.modemType;
                        	buffer.writeByte(b);
                        }
                    }
                    break;
            }
            buffer.setByte(buffer.writerIndex()-1, buffer.getByte(buffer.writerIndex()-1) | 0x80);
        }

        if (this.l2UserInformation > 0) {
            b = (byte)0x80;
            b |= _LAYER2_IDENTIFIER << 5;
            b |= l2UserInformation & 0x1f;
        	buffer.writeByte(b);
        }

        if (this.l3UserInformation > 0) {
            b = (byte)(_LAYER3_IDENTIFIER << 5);
            b |= l3UserInformation & 0x1f;
        	buffer.writeByte(b);

            if (this.l3UserInformation == _L3_ISO_9577) {
            	buffer.writeByte((this.l3Protocol >> 4) & 0x0F);
            	buffer.writeByte(this.l3Protocol & 0x0F);
            }
            
            buffer.setByte(buffer.writerIndex()-1, buffer.getByte(buffer.writerIndex()-1) | 0x80);
        }
    }

    public UserServiceInformationBaseImpl() {
        super();

    }

    public UserServiceInformationBaseImpl(ByteBuf b) throws ParameterException {
        super();
        this.decode(b);
    }

    public int getCodingStandart() {
        return this.codingStandart;
    }

    public void setCodingStandart(int codingStandart) {
        this.codingStandart = codingStandart;
    }

    public int getInformationTransferCapability() {
        return this.informationTransferCapability;
    }

    public void setInformationTransferCapability(int informationTransferCapability) {
        this.informationTransferCapability = informationTransferCapability;
    }

    public int getTransferMode() {
        return this.transferMode;
    }

    public void setTransferMode(int transferMode) {
        this.transferMode = transferMode;
    }

    public int getInformationTransferRate() {
        return this.informationTransferRate;
    }

    public void setInformationTransferRate(int informationTransferRate) {
        this.informationTransferRate = informationTransferRate;
    }

    public int getCustomInformationTransferRate() {
        return this.customInformationTransferRate;
    }

    public void setCustomInformationTransferRate(int customInformationTransferRate) {
        this.customInformationTransferRate = customInformationTransferRate;
    }

    public int getL1UserInformation() {
        return this.l1UserInformation;
    }

    public void setL1UserInformation(int l1UserInformation) {
        this.l1UserInformation = l1UserInformation;
    }

    public int getL2UserInformation() {
        return this.l2UserInformation;
    }

    public void setL2UserInformation(int l2UserInformation) {
        this.l2UserInformation = l2UserInformation;
    }

    public int getL3UserInformation() {
        return this.l3UserInformation;
    }

    public void setL3UserInformation(int l3UserInformation) {
        this.l3UserInformation = l3UserInformation;
    }

    public int getSyncMode() {
        return this.syncMode;
    }

    public void setSyncMode(int syncMode) {
        this.syncMode = syncMode;
    }

    public int getNegotiation() {
        return this.negotiation;
    }

    public void setNegotiation(int negotiation) {
        this.negotiation = negotiation;
    }

    public int getUserRate() {
        return this.userRate;
    }

    public void setUserRate(int userRate) {
        this.userRate = userRate;
    }

    public int getIntermediateRate() {
        return this.intermediateRate;
    }

    public void setIntermediateRate(int intermediateRate) {
        this.intermediateRate = intermediateRate;
    }

    public int getNicOnTx() {
        return this.nicOnTx;
    }

    public void setNicOnTx(int nicOnTx) {
        this.nicOnTx = nicOnTx;
    }

    public int getNicOnRx() {
        return this.nicOnRx;
    }

    public void setNicOnRx(int nicOnRx) {
        this.nicOnRx = nicOnRx;
    }

    public int getFlowControlOnTx() {
        return this.fcOnTx;
    }

    public void setFlowControlOnTx(int fcOnTx) {
        this.fcOnTx = fcOnTx;
    }

    public int getFlowControlOnRx() {
        return this.fcOnRx;
    }

    public void setFlowControlOnRx(int fcOnRx) {
        this.fcOnRx = fcOnRx;
    }

    public int getHDR() {
        return this.hdr;
    }

    public void setHDR(int hdr) {
        this.hdr = hdr;
    }

    public int getMultiframe() {
        return this.multiframe;
    }

    public void setMultiframe(int multiframe) {
        this.multiframe = multiframe;
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getLLINegotiation() {
        return this.lli;
    }

    public void setLLINegotiation(int lli) {
        this.lli = lli;
    }

    public int getAssignor() {
        return this.assignor;
    }

    public void setAssignor(int assignor) {
        this.assignor = assignor;
    }

    public int getInBandNegotiation() {
        return this.inBandNegotiation;
    }

    public void setInBandNegotiation(int inBandNegotiation) {
        this.inBandNegotiation = inBandNegotiation;
    }

    public int getStopBits() {
        return this.stopBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public int getDataBits() {
        return this.dataBits;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public int getParity() {
        return this.parity;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    public int getDuplexMode() {
        return this.duplexMode;
    }

    public void setDuplexMode(int duplexMode) {
        this.duplexMode = duplexMode;
    }

    public int getModemType() {
        return this.modemType;
    }

    public void setModemType(int modemType) {
        this.modemType = modemType;
    }

    public int getL3Protocol() {
        return this.l3Protocol;
    }

    public void setL3Protocol(int l3Protocol) {
        this.l3Protocol = l3Protocol;
    }

    @Override
    public boolean isByte5aIsPresent() {
        return byte5aIsPresent;
    }

    @Override
    public void setByte5aIsPresent(boolean byte5aIsPresent) {
        this.byte5aIsPresent = byte5aIsPresent;
    }

    @Override
    public boolean isByte5bIsPresent() {
        return byte5bIsPresent;
    }

    @Override
    public void setByte5bIsPresent(boolean byte5bIsPresent) {
        this.byte5bIsPresent = byte5bIsPresent;
    }

    @Override
    public boolean isByte5cIsPresent() {
        return byte5cIsPresent;
    }

    @Override
    public void setByte5cIsPresent(boolean byte5cIsPresent) {
        this.byte5cIsPresent = byte5cIsPresent;
    }

    @Override
    public boolean isByte5dIsPresent() {
        return byte5dIsPresent;
    }

    @Override
    public void setByte5dIsPresent(boolean byte5dIsPresent) {
        this.byte5dIsPresent = byte5dIsPresent;
    }

    protected abstract String getPrimitiveName();

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(getPrimitiveName());
        sb.append(" [codingStandart=");
        sb.append(codingStandart);
        sb.append(", informationTransferCapability=");
        sb.append(informationTransferCapability);
        sb.append(", transferMode=");
        sb.append(transferMode);
        sb.append(", informationTransferRate=");
        sb.append(informationTransferRate);
        sb.append(", customInformationTransferRate=");
        sb.append(customInformationTransferRate);

        if (l1UserInformation > 0) {
            sb.append(",\nl1UserInformation=");
            sb.append(l1UserInformation);

            sb.append(", syncMode=");
            sb.append(syncMode);
            sb.append(", negotiation=");
            sb.append(negotiation);
            sb.append(", userRate=");
            sb.append(userRate);
            sb.append(", intermediateRate=");
            sb.append(intermediateRate);
            sb.append(", nicOnTx=");
            sb.append(nicOnTx);
            sb.append(", fcOnTx=");
            sb.append(fcOnTx);
            sb.append(", hdr=");
            sb.append(hdr);
            sb.append(", multiframe=");
            sb.append(multiframe);
            sb.append(", mode=");
            sb.append(mode);
            sb.append(", lli=");
            sb.append(lli);
            sb.append(", assignor=");
            sb.append(assignor);
            sb.append(", inBandNegotiation=");
            sb.append(inBandNegotiation);
            sb.append(", stopBits=");
            sb.append(stopBits);
            sb.append(", dataBits=");
            sb.append(dataBits);
            sb.append(", parity=");
            sb.append(parity);
            sb.append(", duplexMode=");
            sb.append(duplexMode);
            sb.append(", modemType=");
            sb.append(modemType);

            sb.append(", byte5aIsPresent=");
            sb.append(byte5aIsPresent);
            sb.append(", byte5bIsPresent=");
            sb.append(byte5bIsPresent);
            sb.append(", byte5cIsPresent=");
            sb.append(byte5cIsPresent);
            sb.append(", byte5dIsPresent=");
            sb.append(byte5dIsPresent);
        }

        if (l2UserInformation > 0) {
            sb.append("\nl2UserInformation=");
            sb.append(l2UserInformation);
        }

        if (l3UserInformation > 0) {
            sb.append("\nl3UserInformation=");
            sb.append(l3UserInformation);

            sb.append(", l3Protocol=");
            sb.append(l3Protocol);
        }

        sb.append("]");

        return sb.toString();
    }
}
