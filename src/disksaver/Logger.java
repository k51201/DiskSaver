package disksaver;

import java.io.PrintWriter;
import java.util.Calendar;

/**
 * Created by vampa on 07.02.2016.
 *
 * Logging service.
 */
public class Logger {
    private static final Logger loggerInstance = new Logger();

    private PrintWriter out;

    public static Logger getInstance() {
        return loggerInstance;
    }

    private Logger() {
        out = new PrintWriter(System.out);
    }

    void setOut(PrintWriter out) {
        this.out = out;
    }

    public void write(String line) {
        Calendar time = Calendar.getInstance();
        out.format("%1$te.%1$tm.%1$tY %1$tH:%1$tM:%1$tS : %2$s\n", time, line);
        out.flush();
    }

    public void close() {
        out.close();
    }
}
