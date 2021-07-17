package com.svnlike.utils.common;

import com.svnlike.utils.common.StringUtils;
import com.svnlike.utils.exception.SvnApiException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * command utils
 *
 * @author zhangkx
 */
public class CommandUtils {

    /**
     * log4j.Logger
     */
    private static Logger logger = LoggerFactory.getLogger(CommandUtils.class);

    /**
     * time out, seconds
     */
    private final static long DEFAULT_TIME_OUT_S = 10;

    /**
     * space
     */
    private final static String SPACE = " ";

    /**
     * windows
     */
    private final static String WIN = "win";

    /**
     * linux
     */
    private final static String LINUX = "linux";

    /**
     * OS name. window / linux
     */
    private static String os;

    static {
        os = System.getProperty("os.name").toLowerCase();
    }

    /**
     * Os is windows
     *
     * @return boolean
     */
    public static boolean osIsWindows() {
        return os.startsWith(WIN);
    }

    /**
     * Os is linux
     *
     * @return boolean
     */
    public static boolean osIsLinux() {
        return os.startsWith(LINUX);
    }

    /**
     * execute command
     *
     * @param command command text
     * @return exit value
     */
    public static int execute(String command) {
        return execute(command, 0);
    }

    /**
     * execute command
     *
     * @param command command text
     * @param timeout the timeout for the process in milliseconds.
     * @return exit value
     */
    public static int execute(String command, long timeout) {
        return executeForProcessResult(command, timeout, false).getExitValue();
    }

    /**
     * execute command (Async)
     *
     * @param command command text
     * @return exit value
     */
    public static int executeAsync(String command) {
        return executeAsync(command, 0);
    }

    /**
     * execute command (Async)
     *
     * @param command command text
     * @param timeout the timeout for the process in milliseconds.
     * @return exit value
     */
    public static int executeAsync(String command, long timeout) {
        return executeForProcessResultAsync(command, timeout, false).getExitValue();
    }

    /**
     * execute command for UTF-8 String
     *
     * @param command command text
     * @return output string
     */
    public static String executeForString(String command) {
        return executeForString(command, 0);
    }

    /**
     * execute command for UTF-8 String
     *
     * @param command command text
     * @param timeout the timeout for the process in milliseconds.
     * @return output string
     */
    public static String executeForString(String command, long timeout) {
        return executeForProcessResult(command, timeout, true).outputString();
    }

    /**
     * execute for string (async)
     *
     * @param command command text
     * @return string
     */
    public static String executeForStringAsync(String command) {
        return executeForStringAsync(command, 0);
    }

    /**
     * execute for string (async)
     *
     * @param command command text
     * @param timeout the timeout for the process in milliseconds.
     * @return string
     */
    public static String executeForStringAsync(String command, long timeout) {
        return executeForProcessResultAsync(command, timeout, true).outputString();
    }

    /**
     * execute command for UTF-8 String
     *
     * @param command command text
     * @return output string
     */
    public static String executeForStringUTF8(String command) {
        return executeForStringUTF8(command, 0);
    }

    /**
     * execute command for UTF-8 String
     *
     * @param command command text
     * @param timeout the timeout for the process in milliseconds.
     * @return output string
     */
    public static String executeForStringUTF8(String command, long timeout) {
        return executeForProcessResult(command, timeout, true).outputUTF8();
    }

    /**
     * execute for string (async) UTF8
     *
     * @param command command text
     * @return string
     */
    public static String executeForStringUTF8Async(String command) {
        return executeForStringUTF8Async(command, 0);
    }

    /**
     * execute for string (async) UTF8
     *
     * @param command command text
     * @param timeout the timeout for the process in milliseconds.
     * @return string
     */
    public static String executeForStringUTF8Async(String command, long timeout) {
        return executeForProcessResultAsync(command, timeout, true).outputUTF8();
    }

    /**
     * execute for long
     *
     * @param command command text
     * @return long
     */
    public static long executeForLong(String command) {
        return executeForLong(command, 0);
    }

    /**
     * execute for long
     *
     * @param command command text
     * @param timeout the timeout for the process in milliseconds.
     * @return long
     */
    public static long executeForLong(String command, long timeout) {
        return Long.parseLong(executeForString(command, timeout));
    }

    /**
     * execute for long async
     *
     * @param command command text
     * @return long
     */
    public static long executeForLongAsync(String command) {
        return executeForLong(command, 0);
    }

    /**
     * execute for long async
     *
     * @param command command text
     * @param timeout the timeout for the process in milliseconds.
     * @return long
     */
    public static long executeForLongAsync(String command, long timeout) {
        return Long.parseLong(executeForStringAsync(command, timeout));
    }

    /**
     * @param command command text
     * @return Document
     */
    public static Document executeForXmlDocument(String command) {
        return executeForXmlDocument(command, 0);
    }

    /**
     * execute for xml document
     *
     * @param command command text
     * @param timeout the timeout for the process in milliseconds.
     * @return Document
     */
    public static Document executeForXmlDocument(String command, long timeout) {
        try {
            return DocumentHelper.parseText(executeForString(command, timeout));
        } catch (DocumentException e) {
            throw new SvnApiException(e.getMessage());
        }
    }

    /**
     * execute command for ProcessResult
     *
     * @param command    command text
     * @param timeout    the timeout for the process in milliseconds.
     * @param readOutput read output
     * @return ProcessResult
     */
    private static ProcessResult executeForProcessResult(String command, long timeout, boolean readOutput) {
        logger.debug("execute command: " + command);

        try {
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

            ProcessResult processResult = new ProcessExecutor()
                    .command(parseCommand(command))
                    .timeout(timeout > 0 ? timeout : DEFAULT_TIME_OUT_S, TimeUnit.SECONDS)
                    .redirectError(errorStream)
                    .readOutput(readOutput)
                    .execute();

            // throw exception if has error when execute command
            String executeError = errorStream.toString(Charset.defaultCharset());
            if (StringUtils.isNotBlank(executeError)) {
                throw new SvnApiException(executeError);
            }

            return processResult;
        } catch (TimeoutException e) {
            throw new SvnApiException("Time out");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new SvnApiException(e.getMessage());
        }
    }

    /**
     * execute command for ProcessResult (async)
     *
     * @param command command text
     * @param timeout the timeout for the process in milliseconds.
     * @return ProcessResult
     */
    private static ProcessResult executeForProcessResultAsync(String command, long timeout, boolean readOutput) {
        logger.debug("execute command: " + command);

        try {
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

            Future<ProcessResult> future = new ProcessExecutor()
                    .command(parseCommand(command))
                    .redirectError(errorStream)
                    .readOutput(readOutput)
//                    .exitValue(0)
                    .start().getFuture();

            ProcessResult processResult = future.get(timeout > 0 ? timeout : DEFAULT_TIME_OUT_S, TimeUnit.SECONDS);

            // throw exception if has error when execute command
            String executeError = errorStream.toString(Charset.defaultCharset());
            if (StringUtils.isNotBlank(executeError)) {
                throw new SvnApiException(executeError);
            }

            return processResult;

//        } catch (InvalidExitValueException e) {
//            throw new SvnApiException("Unexpected exit value");
        } catch (TimeoutException e) {
            throw new SvnApiException("Time out");
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new SvnApiException(e.getMessage());
        }
    }

    /**
     * kill process by process name
     *
     * @param processName process name
     */
    public static void stopProcess(String processName) {
        if (osIsWindows()) {
            execute("taskkill /F /IM " + processName, 5000);
        } else if (osIsLinux()) {
            execute("pkill " + processName, 5000);
        } else {
            throw new SvnApiException("ECM0001", "Support Windows and Linux only!");
        }
    }

    /**
     * parse command
     *
     * @param command command text
     * @return List<String>
     */
    private static List<String> parseCommand(String command) {
        String[] tmp = translateCommandline(command);
        return new ArrayList<>(Arrays.asList(tmp));
    }

    /**
     * Crack a command line.
     *
     * @param toProcess the command line to process
     * @return the command line broken into strings. An empty or null toProcess parameter results in a zero sized array
     */
    private static String[] translateCommandline(final String toProcess) {
        if (toProcess == null || toProcess.length() == 0) {
            // no command? no string
            return new String[0];
        }

        // parse with a simple finite state machine
        final int normal = 0;
        final int inQuote = 1;
        final int inDoubleQuote = 2;
        int state = normal;
        final StringTokenizer tok = new StringTokenizer(toProcess, "\"\' ", true);
        final ArrayList<String> list = new ArrayList<String>();
        StringBuilder current = new StringBuilder();
        boolean lastTokenHasBeenQuoted = false;

        while (tok.hasMoreTokens()) {
            final String nextTok = tok.nextToken();
            switch (state) {
                case inQuote:
                    if ("\'".equals(nextTok)) {
                        lastTokenHasBeenQuoted = true;
                        state = normal;
                    } else {
                        current.append(nextTok);
                    }
                    break;
                case inDoubleQuote:
                    if ("\"".equals(nextTok)) {
                        lastTokenHasBeenQuoted = true;
                        state = normal;
                    } else {
                        current.append(nextTok);
                    }
                    break;
                default:
                    if ("\'".equals(nextTok)) {
                        state = inQuote;
                    } else if ("\"".equals(nextTok)) {
                        state = inDoubleQuote;
                    } else if (" ".equals(nextTok)) {
                        if (lastTokenHasBeenQuoted || current.length() != 0) {
                            list.add(current.toString());
                            current = new StringBuilder();
                        }
                    } else {
                        current.append(nextTok);
                    }
                    lastTokenHasBeenQuoted = false;
                    break;
            }
        }

        if (lastTokenHasBeenQuoted || current.length() != 0) {
            list.add(current.toString());
        }

        if (state == inQuote || state == inDoubleQuote) {
            throw new IllegalArgumentException("Unbalanced quotes in " + toProcess);
        }

        final String[] args = new String[list.size()];
        return list.toArray(args);
    }
}
