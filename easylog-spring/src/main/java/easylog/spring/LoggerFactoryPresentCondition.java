package easylog.spring;

class LoggerFactoryPresentCondition extends TypePresentCondition {

    static final String TYPE_NAME = "easylog.slf4j.Slf4jLoggerFactory";

    public LoggerFactoryPresentCondition() {
        super(TYPE_NAME);
    }
}
