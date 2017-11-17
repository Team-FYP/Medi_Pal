package lk.ac.mrt.cse.medipal.constant;

/**
 * Created by Lakshan on 2017-05-30.
 */

public class Connection {
    public class Network{
        public static final int NETWORK_CALL_RETRY_COUNT = 3;
    }
    public class Connectivity{
        public static final String CONNECTION_CHECK_URL = "http://clients3.google.com/generate_204";
        public static final String USER_AGENT_KEY = "User-Agent";
        public static final String CONNECTION_KEY = "Connection";
        public static final String USER_AFENT_VALUE = "Android";
        public static final String CONNECTION_VALUE = "close";
        public static final int CHECK_TIME_OUT = 5500;
    }
}
