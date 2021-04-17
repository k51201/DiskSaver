package ru.vampa.disksaver;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by vampa on 07.02.2016.
 *
 * Logging service.
 */
public class Logger {
    private static final Logger loggerInstance = new Logger();

    private final List<PrintWriter> outs = new ArrayList<>();

    private Logger() {
        outs.add(new PrintWriter(System.out));

        final var fileName =
                String.format("log/%1$te%1$tm%1$tY-%1$tH%1$tM%1$tS.log", Calendar.getInstance().getTime());
        final var logFile = new File(fileName);

        try {
            final var logFileCreated = logFile.createNewFile();
            outs.add(new PrintWriter(logFile));
            if (logFileCreated)
                this.write("Log file created");
        } catch (IOException e) {
            System.out.println("Unable to create log file : " + e.getMessage());
        }
    }

    public void write(String line) {
        outs.forEach(out -> {
            out.format("%1$te.%1$tm.%1$tY %1$tH:%1$tM:%1$tS : %2$s\n", Calendar.getInstance(), line);
            out.flush();
        });
    }

    public void close() {
        outs.forEach(PrintWriter::close);
    }

    public static Logger getInstance() {
        return loggerInstance;
    }
}
