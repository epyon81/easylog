package easylog.slf4j;

import easylog.core.Logger;
import easylog.core.LoggerFactory;

/**
 * Easylog logger factory using slf4j.
 */
public class Slf4jLoggerFactory implements LoggerFactory {
    @Override
    public Logger getLogger(Class<?> clazz) {
        return new Slf4jLogger(org.slf4j.LoggerFactory.getLogger(clazz));
    }
}
