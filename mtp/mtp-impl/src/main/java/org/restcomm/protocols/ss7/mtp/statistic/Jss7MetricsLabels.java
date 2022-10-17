package org.restcomm.protocols.ss7.mtp.statistic;


public class Jss7MetricsLabels {
  // MTP
  // General
  public static final String UNKNOWN = "unknown";
  public static final String NONE = "";

  // Queued Requests
  public static final String MESSAGE_DELIVERY_EXECUTORS = "message_delivery_executors";

  // Operations
  public static final String READ = "read";
  public static final String WRITE = "write";

  // Errors
  public static final String PAYLOAD_PROCESS = "payload_process";
  public static final String MESSAGE_TYPE = "message_type";
  public static final String MESSAGE_CLASS = "message_class";
  public static final String TCCONTINUE_PARSE = "tccontinue_parse";
  public static final String TCBEGIN_PARSE = "tcbegin_parse";
  public static final String TCEND_PARSE = "tcend_parse";
  public static final String TCABORT_PARSE = "tcabort_parse";
  public static final String TCUNIT_PARSE = "tcunit_parse";
  public static final String SCCP_DECODE = "sccp_decode";
  public static final String PAYLOAD_SEND = "payload_send";

  // Message Classes
  public static final String MANAGEMENT = "management";
  public static final String TRANSFER_MESSAGE = "transfer_message";
  public static final String SIGNALING_NETWORK_MANAGEMENT = "signaling_network_management";
  public static final String ASP_STATE_MAINTENACE = "asp_state_maintenance";
  public static final String ASP_TRAFFIC_MAINTENANCE = "asp_traffic_maintenance";
  public static final String ROUTING_KEY_MANAGEMENT = "routing_key_management";

  // Message Tags
  public static final String TCCONTINUE = "tccontinue";
  public static final String TCBEGIN = "tcbegin";
  public static final String TCEND = "tcend";
  public static final String TCABORT = "tcabort";
  public static final String TCUNI = "tcuni";

}
