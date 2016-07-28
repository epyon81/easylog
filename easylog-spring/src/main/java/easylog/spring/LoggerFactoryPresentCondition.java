package easylog.spring;

import easylog.core.LoggerFactory;

class LoggerFactoryPresentCondition extends TypePresentCondition<LoggerFactory> {

    public LoggerFactoryPresentCondition() {
        super(LoggerFactory.class);
    }
}
