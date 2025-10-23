package org.restcomm.protocols.ss7.cap;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.restcomm.protocols.sctp.SctpManagementImpl;
import org.restcomm.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.restcomm.protocols.ss7.m3ua.message.M3UAMessage;
import org.restcomm.protocols.ss7.m3ua.message.MessageClass;
import org.restcomm.protocols.ss7.m3ua.message.MessageType;
import org.restcomm.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.restcomm.protocols.ss7.m3ua.parameter.ProtocolData;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpSegmentableMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SegmentationImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.tcap.TCAPProviderImpl;
import org.restcomm.protocols.ss7.tcap.TCAPStackImpl;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.DraftParsedMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.BaseComponent;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.Reject;
import org.restcomm.protocols.ss7.tcap.asn.comp.Return;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnError;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCEndMessage;

import com.mobius.software.common.dal.timers.WorkerPool;
import com.mobius.software.telco.protocols.ss7.common.UUIDGenerator;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.sctp.SctpMessage;
import io.pkts.PacketHandler;
import io.pkts.Pcap;
import io.pkts.buffer.Buffer;
import io.pkts.packet.PCapPacket;
import io.pkts.packet.Packet;
import io.pkts.protocol.Protocol;

public class CapParserLoopTest {
	@Test
	public void testCaptureFiles() throws Exception {
		org.restcomm.protocols.ss7.m3ua.message.MessageFactory messageFactory = new org.restcomm.protocols.ss7.m3ua.impl.message.MessageFactoryImpl();
		WorkerPool workerPool = new WorkerPool("CAP");
		workerPool.start(4);

		UUIDGenerator uuidGenerator = new UUIDGenerator(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });
		SctpManagementImpl transportManagement = new SctpManagementImpl(".sctp", 4, 4, 4);
		M3UAManagementImpl m3uaStack = new M3UAManagementImpl(".m3ua", null, uuidGenerator, workerPool);
		m3uaStack.setTransportManagement(transportManagement);
		m3uaStack.start();
		Mtp3TransferPrimitiveFactory mtp3TransferPrimitiveFactory = m3uaStack.getMtp3TransferPrimitiveFactory();
		SccpStackImpl sccpStack = new SccpStackImpl(".sccp", workerPool);
		sccpStack.setMtp3UserPart(1, m3uaStack);
		sccpStack.start();
		TCAPStackImpl tcapStack = new TCAPStackImpl(".tcap", sccpStack.getSccpProvider(), 6, workerPool);
		tcapStack.start();
		TCAPProviderImpl provider = (TCAPProviderImpl) tcapStack.getProvider();
		CAPStackImpl capStack = new CAPStackImpl(".cap", provider);
		capStack.start();

		final AtomicReference<String> lastError = new AtomicReference<String>(null);

		if (this.getClass().getClassLoader().getResource("samples") == null)
			return;

		File folder = new File(this.getClass().getClassLoader().getResource("samples").getFile());
		File[] listOfFiles = folder.listFiles();
		for (File currFile : listOfFiles) {
			System.out.println("FILE:" + currFile.getAbsolutePath());
			Pcap pcap = Pcap.openStream(currFile.getAbsoluteFile());
			pcap.loop(new PacketHandler() {
				@Override
				public boolean nextPacket(Packet packet) throws IOException {
					if (packet.hasProtocol(Protocol.PCAP)) {
						PCapPacket pcapPacket = (PCapPacket) packet.getPacket(Protocol.PCAP);
						Buffer buffer = pcapPacket.getPayload();
						if (buffer != null) {
							byte[] original = buffer.getArray();
							ByteBuf nettyBuffer = Unpooled.wrappedBuffer(original);
							nettyBuffer.skipBytes(62);
							SctpMessage message = new SctpMessage(0, 0, nettyBuffer);
							M3UAMessage m3UAMessage = messageFactory.createMessage(message.content());
							switch (m3UAMessage.getMessageClass()) {
							case MessageClass.TRANSFER_MESSAGES:
								switch (m3UAMessage.getMessageType()) {
								case MessageType.PAYLOAD:
									PayloadData payload = (PayloadData) m3UAMessage;
									ProtocolData protocolData = payload.getData();
									Mtp3TransferPrimitive mtp3TransferPrimitive = mtp3TransferPrimitiveFactory
											.createMtp3TransferPrimitive(protocolData.getSI(), protocolData.getNI(),
													protocolData.getMP(), protocolData.getOpc(), protocolData.getDpc(),
													protocolData.getSLS(), protocolData.getData());

									SccpMessageImpl msg = null;
									ByteBuf data = mtp3TransferPrimitive.getData();
									int mt = data.readUnsignedByte();
									try {
										msg = ((MessageFactoryImpl) sccpStack.getSccpProvider().getMessageFactory())
												.createMessage(mt, mtp3TransferPrimitive.getOpc(),
														mtp3TransferPrimitive.getDpc(), mtp3TransferPrimitive.getSls(),
														data, SccpProtocolVersion.ITU, 0);

										if (msg instanceof SccpSegmentableMessageImpl) {
											SccpSegmentableMessageImpl sgmMsg = (SccpSegmentableMessageImpl) msg;
											SegmentationImpl segm = (SegmentationImpl) sgmMsg.getSegmentation();
											if (segm != null)
												// segmentation info is present - segmentation is possible
												if (segm.isFirstSegIndication() && segm.getRemainingSegments() == 0)
													// the single segment - no reassembly is needed
													// not need to change the ref count here
													sgmMsg.setReceivedSingleSegment();
												else
													// there should not be segmented messages
													return true;
										}

										if (msg instanceof SccpDataMessage) {
											DraftParsedMessage output = ((TCAPProviderImpl) tcapStack.getProvider())
													.parseMessageDraft(((SccpDataMessage) msg).getData());
											if (output.getParsingErrorReason() != null)
												lastError.set(output.getParsingErrorReason());
											else {
												String messageClass = output.getMessage().getClass().getCanonicalName();
												String messageContent = "";
												if (output.getMessage() instanceof TCBeginMessage
														&& ((TCBeginMessage) output.getMessage())
																.getComponents() != null) {
													for (BaseComponent component : ((TCBeginMessage) output
															.getMessage()).getComponents())
														if (component instanceof Invoke)
															messageContent += ((Invoke) component).getParameter();
														else if (component instanceof Return)
															messageContent += ((Return) component).getParameter();
														else if (component instanceof ReturnError)
															messageContent += ((ReturnError) component).getParameter()
																	+ ",CODE " + ((ReturnError) component)
																			.getErrorCode().getLocalErrorCode();
														else if (component instanceof Reject)
															messageContent += ((Reject) component).getProblem();
												} else if (output.getMessage() instanceof TCContinueMessage
														&& ((TCContinueMessage) output.getMessage())
																.getComponents() != null) {
													for (BaseComponent component : ((TCContinueMessage) output
															.getMessage()).getComponents())
														if (component instanceof Invoke)
															messageContent += ((Invoke) component).getParameter();
														else if (component instanceof Return)
															messageContent += ((Return) component).getParameter();
														else if (component instanceof ReturnError)
															messageContent += ((ReturnError) component).getParameter()
																	+ ",CODE " + ((ReturnError) component)
																			.getErrorCode().getLocalErrorCode();
														else if (component instanceof Reject)
															messageContent += ((Reject) component).getProblem();
												} else if (output.getMessage() instanceof TCEndMessage
														&& ((TCEndMessage) output.getMessage()).getComponents() != null)
													for (BaseComponent component : ((TCEndMessage) output.getMessage())
															.getComponents())
														if (component instanceof Invoke)
															messageContent += ((Invoke) component).getParameter();
														else if (component instanceof Return)
															messageContent += ((Return) component).getParameter();
														else if (component instanceof ReturnError)
															messageContent += ((ReturnError) component).getParameter()
																	+ ",CODE " + ((ReturnError) component)
																			.getErrorCode().getLocalErrorCode();
														else if (component instanceof Reject)
															messageContent += ((Reject) component).getProblem();

												if (messageContent.length() == 0) {
													System.out.println("MESSAGE DOES NOT HAVE ANY COMPONENTS/ERRORS "
															+ messageClass);
													System.out.println("Message:" + mtp3TransferPrimitive.toString());
													System.out.println("Buffer:" + mtp3TransferPrimitive.printBuffer());
												}
											}
										}
									} catch (Exception ex) {
										ex.printStackTrace();
										System.out.println("Error parsing the message," + ex.getMessage());
									}
									break;
								}
							}
						}
					}
					return true;
				}
			});
		}

		if (lastError.get() != null)
			throw new IOException(lastError.get());

		workerPool.stop();
	}
}