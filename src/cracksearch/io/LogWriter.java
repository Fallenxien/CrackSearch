package cracksearch.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Andy on 31/03/2018.
 */
public class LogWriter {

    public enum LogLevel {
        NONE (0), ROUTE (1), DETAIL (2);

        private final int level;
        LogLevel(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }
    }

    private PrintWriter writer;
    private LogLevel level;

    /**
     * Sets the logging level
     * @param level level to set
     */
    public void setLevel(LogLevel level) {
        this.level = level;
    }

    /**
     * Creates a log writer at the given file location
     * @param f file to write too
     * @throws IOException couldnt create file
     */
    public LogWriter(File f) throws IOException {
        if (!f.exists()) {
            f.createNewFile();
        }
        writer = new PrintWriter(f);
    }

    /**
     * Write a line to the log at the given level
     * @param line Line to write
     * @param level Level to write too
     * @throws IOException Error writing
     */
    public void writeLine(String line, LogLevel level) throws IOException {

        if (level != LogLevel.NONE) {
            if (level.getLevel() <= this.level.getLevel()) {
                writer.write(line + '\n');
            }
        }
    }


}
