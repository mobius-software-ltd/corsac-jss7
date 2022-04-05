package org.restcomm.protocols.ss7.mtp.statistic;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Summary;

import java.util.concurrent.TimeUnit;

public class Metrics {
  // MTP
  public static final Summary QUEUED_REQUESTS_HISTOGRAM = Summary.build().name("ocs_queued_requests_histogram")
      .help("Queued Requests Histogram").labelNames("type")
      .maxAgeSeconds(TimeUnit.MINUTES.toSeconds(1)).quantile(1, 0.01).register();
  public static final Counter SS7_ERROR_COUNT = Counter.build().name("ocs_ss7_error_count").help("SS7 Errors")
      .labelNames("reason", "operation").register();
  public static final Counter SS7_MESSAGE_CLASS_READ_COUNT = Counter.build().name("ocs_ss7_message_class_read_count").help("SS7 Messages Read")
      .labelNames("class").register();
  public static final Counter SS7_MESSAGE_CLASS_WRITE_COUNT = Counter.build().name("ocs_ss7_message_class_write_count").help("SS7 Messages Write")
      .labelNames("class").register();
  public static final Counter SS7_MESSAGE_TAG_READ_COUNT = Counter.build().name("ocs_ss7_message_tag_read_count").help("Diameter Errors")
      .labelNames("tag").register();


  // Offline
  public static final Counter OFFLINE_REC_PRODUCER_COUNT = Counter.build().name("ocs_offline_record_producer_count")
      .help("Offline Record Producer").labelNames("session_type").register();
  public static final Counter OFFLINE_SES = Counter.build().name("ocs_offline_session_count")
      .help("Offline Sessions").labelNames("status").register();
  public static final Gauge OFFLINE_FOR_ALL_STATUS_GAUGE = Gauge.build().name("ocs_offlineforall_status_gauge")
      .help("Offline For All Status Gauge").register();
  public static final Gauge OFFLINE_FOR_ALL_FAILURE_GAUGE = Gauge.build().name("ocs_offlineforall_failure_gauge")
      .help("Offline For All Failure Histogram").register();

  public static final Counter OFFLINE_REC_CONSUMER_COUNT = Counter.build().name("ocs_offline_record_consumer_count")
      .help("Offline Record Consumer").labelNames("session_type").register();
  public static final Counter OFFLINE_ERROR_PRODUCER_COUNT = Counter.build().name("ocs_offline_error_producer_count")
      .help("Number of offline errors produced").labelNames("session_type").register();
  public static final Counter OFFLINE_ERROR_CONSUMER_COUNT = Counter.build().name("ocs_offline_error_consumer_count")
      .help("Number of offline errors consumed").labelNames("session_type").register();
  public static final Counter OFFLINE_REQUEST_STATUS_COUNT = Counter.build().name("ocs_offline_request_status_count")
      .help("Number of offline requests per status").labelNames("status").register();

  // Voice Transfer Info Status
  public static final Counter VOICE_TRANS_INFO_STATUS_COUNT = Counter.build().name("ocs_cap_vti_count")
      .help("Voice Trans Info Status").labelNames("status").register();

  // Cause Indicators (Release Codes)
  public static final Counter CAP_RELEASE_CODES_COUNT = Counter.build().name("ocs_cap_release_codes_count")
      .help("Release Codes Count").labelNames("code").register();

  // Backend Failure
  public static final Counter BE_FAILURE_COUNT = Counter.build().name("ocs_backend_failure_count")
      .help("BackEnd Failure Count").labelNames("reason", "status_code").help("Http Failure").register();


}
