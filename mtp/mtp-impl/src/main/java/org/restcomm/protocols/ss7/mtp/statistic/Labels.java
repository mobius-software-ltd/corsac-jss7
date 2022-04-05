package org.restcomm.protocols.ss7.mtp.statistic;


import java.util.HashMap;

public class Labels {
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

  // OCF (CAP)
  // CAP Message Types
  public static final String IDP = "idp";
  public static final String ANS = "ans";
  public static final String DIS = "dis";
  public static final String ABA = "aba";
  public static final String OTH = "oth";
  public static final String ACR = "acr";

  // SES
  public static final String SESSION_ACTIVE = "active";

  // Offline
  public static final String FULL_SESSION = "full";
  public static final String PARTIAL_SESSION = "partial";
  public static final String GENERAL_SESSION = "general";
  public static final String REQUEST_ALLOW = "allow";
  public static final String REQUEST_BLOCK = "block";
  public static final String OFF_STATUS_NEW = "new";

  // Voice Transfer Info Status
  public static final String SUCCESS = "success";
  public static final String INVALID = "invalid";
  public static final String IO_ERROR = "io_error";
  public static final String PROXY_FAILED = "proxy_failed";
  public static final String PROXY_TOKEN_GEN_FAILURE = "proxy_token_gen_failure";
  public static final String INSUFFICENT_FUNDS = "insufficient_funds";
  public static final String SUBSCRIBER_NOT_ACTIVE = "subscriber_not_active";
  public static final String INVALID_RECURRENCE_STATE = "invalid_recurrence_state";
  public static final String RATE_NOT_FOUND = "rate_not_found";
  public static final String SERVICE_NOT_AVAILABLE = "service_not_available";
  public static final String NO_RESERVATION_AVAILABLE = "no_reservation_available";
  public static final String INVALID_FLOW_COMMIT_MISSING = "invalid_flow_commit_missing";
  public static final String CHARGING_NOT_ALLOWED = "charging_not_allowed";
  public static final String SUBSCRIBER_ID_BLOCKED = "subscriber_id_blocked";
  public static final String SUBSCRIBER_NOT_EXIST = "subscriber_not_exist";

  // Timeouts
  public static final String DIALOG_TIMEOUT = "dialog_timeout";
  public static final String INVOKE_TIMEOUT = "invoke_timeout";

  // Errors
  public static final String DIALOG_DELIMITER_PROCESS_EXCEPTION = "dialog_delimeter_process_exception";
  public static final String INVALID_TRANSACTION_REQUEST = "invalid_transaction_request";
  public static final String INVALID_CM_RESPONSE = "invalid_cm_response";
  public static final String MNP = "mnp";

  // RTT
  public static final String OCF_PROCESSING = "ocf_processing";
  public static final String CM_PROCESSING = "cm_processing";
  public static final String MNP_QUERY = "mnp_query";

  // Backend Failure Reasons
  public static final String EXCEPTION = "exception";
  public static final String SERVER_ERROR = "server_error";

  // MNP
  public static final String CACHE = "cache";
  public static final String QUERY = "query";
  public static final String EMPTY = "empty";
  public static final String RESPONSE_PARSING = "response_parsing";
  public static final String NO_DIALOG = "no_dialog";

  private static HashMap<Integer,String> MESSAGE_CLASSES = new HashMap<Integer,String>() {
    {
    }
  };

  /**
   * Utility function which will return Message Class for given Message class code
   * @param code Message class code
   * @return Message class string
   */
  public static String getMessageClass(int code) {
    String result = MESSAGE_CLASSES.get(code);
    return result == null ? UNKNOWN : result;
  }

}
