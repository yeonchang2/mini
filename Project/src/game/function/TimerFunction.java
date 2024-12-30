package game.function;

import java.time.Duration;

public class TimerFunction {
    private TimerFunction() {}

    public static String durationToStringFormat(Duration duration) {
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        long milliseconds = duration.toMillis() % 1000;
        return String.format("%02d : %02d : %03d", minutes, seconds, milliseconds);
    }
}
