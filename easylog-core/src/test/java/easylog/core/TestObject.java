package easylog.core;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnusedParameters")
public class TestObject {

    public List<String> messages;

    @Log(detailed = true, position = {LogPosition.BEFORE, LogPosition.AFTER}, level = LogLevel.TRACE)
    public int defaultDetailedBeforeAfter1(String value1, int value2) {
        return value1.length() * value2;
    }

    @Log(detailed = true, position = LogPosition.BEFORE, level = LogLevel.TRACE)
    @Log(detailed = true, position = LogPosition.AFTER, level = LogLevel.INFO)
    public int defaultDetailedBeforeAfter2(String value1, int value2) {
        return value1.length() * value2;
    }

    @Log(detailed = true, position = LogPosition.BEFORE, level = LogLevel.TRACE)
    public int defaultDetailedBefore(String value1, int value2) {
        return value1.length() * value2;
    }

    @Log(detailed = true, position = LogPosition.AFTER)
    public int defaultDetailedAfter(String value1, int value2) {
        return value1.length() * value2;
    }

    @Log()
    public int simpleBefore(String value1, int value2) {
        return value1.length() * value2;
    }

    @Log(position = LogPosition.AFTER)
    public int simpleAfter(String value1, int value2) {
        return value1.length() * value2;
    }

    @Log(position = LogPosition.AFTER_EXCEPTION)
    public int simpleAfterException(String value1, int value2) {
        throw new RuntimeException("ERROR");
    }

    @Log(detailed = true, position = {LogPosition.BEFORE, LogPosition.AFTER})
    public void detailedVoidMethodBeforeAfter(String value1, int value2) {
    }

    @Log(detailed = true, position = LogPosition.AFTER_EXCEPTION)
    public void detailedVoidMethodAfterException(String value1, int value2) {
        throw new RuntimeException("ERROR");
    }

    @Log(detailed = true, position = LogPosition.AFTER_EXCEPTION)
    public int detailedMethodAfterException(String value1, int value2) {
        throw new RuntimeException("ERROR");
    }

    @Log("B_TEST!!!")
    public int customMessageBefore(String value1, int value2) {
        return value1.length() * value2;
    }

    @Log(value = "A_TEST!!!", position = LogPosition.AFTER)
    public int customMessageAfter(String value1, int value2) {
        return value1.length() * value2;
    }

    @Log(value = "AE_TEST!!!", position = LogPosition.AFTER_EXCEPTION)
    public int customMessageAfterException(String value1, int value2) {
        throw new RuntimeException("ERROR");
    }

    @Log(detailed = true, position = LogPosition.AFTER)
    public CompletableFuture<Void> futureWithoutResultDetailed() {
        return CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            messages.add("CALLED");
        });
    }

    @Log(detailed = true, position = LogPosition.AFTER)
    public CompletableFuture<Integer> futureWithResultDetailed() {
        return CompletableFuture.supplyAsync(() -> 123);
    }
}
