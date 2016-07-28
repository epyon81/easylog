package easylog.spring;

import easylog.core.LogMessageParser;

class MessageParserPresentCondition extends TypePresentCondition<LogMessageParser> {
    
    public MessageParserPresentCondition() {
        super(LogMessageParser.class);
    }
}
