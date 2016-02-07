import java.io.PrintWriter;

/**
 * Created by vampa on 07.02.2016.
 */
public class Logger {
    private static Logger loggerInstance = new Logger();

    private PrintWriter out;

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public static Logger getInstance() {
        return loggerInstance;
    }

    private Logger() {
        out = new PrintWriter(System.out);
    }

    public void write(String line) {
        out.println(line);
    }
}
