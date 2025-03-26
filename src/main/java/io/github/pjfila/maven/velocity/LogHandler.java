package io.github.pjfila.maven.velocity;

import org.apache.maven.plugin.logging.Log;
import org.slf4j.Logger;
import org.slf4j.Marker;

import static org.slf4j.helpers.MessageFormatter.arrayFormat;

public class LogHandler implements Logger {

    private final Log log;

    LogHandler(VelocityMojo velocityMojo) {
        log = velocityMojo.getLog();
    }

    @Override
    public String getName() {
        return "velocity";
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public void trace(String s) {
        // no-po
    }

    @Override
    public void trace(String s, Object o) {
        // no-po
    }

    @Override
    public void trace(String s, Object o, Object o1) {
        // no-po
    }

    @Override
    public void trace(String s, Object... objects) {
        // no-po
    }

    @Override
    public void trace(String s, Throwable throwable) {
        // no-po
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return false;
    }

    @Override
    public void trace(Marker marker, String s) {
        // no-po
    }

    @Override
    public void trace(Marker marker, String s, Object o) {
        // no-po
    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {
        // no-po
    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {
        // no-po
    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {
        // no-po
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public void debug(String s) {
        log.debug(s);
    }

    @Override
    public void debug(String s, Object o) {
        log.debug(format(s, o));
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        log.debug(format(s, o, o1));
    }

    @Override
    public void debug(String s, Object... objects) {
        log.debug(format(s, objects));
    }

    @Override
    public void debug(String s, Throwable throwable) {
        log.debug(s, throwable);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return isDebugEnabled();
    }

    @Override
    public void debug(Marker marker, String s) {
        log.debug(s);
    }

    @Override
    public void debug(Marker marker, String s, Object o) {
        log.debug(format(s, o));
    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {
        log.debug(format(s, o, o1));
    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {
        log.debug(format(s, objects));
    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {
        log.debug(s, throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public void info(String s) {
        log.info(s);
    }

    @Override
    public void info(String s, Object o) {
        log.info(format(s, o));
    }

    @Override
    public void info(String s, Object o, Object o1) {
        log.info(format(s, o, o1));
    }

    @Override
    public void info(String s, Object... objects) {
        log.info(format(s, objects));
    }

    @Override
    public void info(String s, Throwable throwable) {
        log.info(s, throwable);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return log.isInfoEnabled();
    }

    @Override
    public void info(Marker marker, String s) {
        log.info(s);
    }

    @Override
    public void info(Marker marker, String s, Object o) {
        log.info(format(s, o));
    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {
        log.info(format(s, o, o1));
    }

    @Override
    public void info(Marker marker, String s, Object... objects) {
        log.info(format(s, objects));
    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {
        log.info(s, throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    @Override
    public void warn(String s) {
        log.warn(s);
    }

    @Override
    public void warn(String s, Object o) {
        log.warn(format(s, o));
    }

    @Override
    public void warn(String s, Object... objects) {
        log.warn(format(s, objects));
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        log.warn(format(s, o, o1));
    }

    @Override
    public void warn(String s, Throwable throwable) {
        log.warn(s, throwable);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return log.isWarnEnabled();
    }

    @Override
    public void warn(Marker marker, String s) {
        log.warn(s);
    }

    @Override
    public void warn(Marker marker, String s, Object o) {
        log.warn(format(s, o));
    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {
        log.warn(format(s, o, o1));
    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {
        log.warn(format(s, objects));
    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {
        log.warn(s, throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public void error(String s) {
        log.error(s);
    }

    @Override
    public void error(String s, Object o) {
        log.error(format(s, o));
    }

    @Override
    public void error(String s, Object o, Object o1) {
        log.error(format(s, o, o1));
    }

    @Override
    public void error(String s, Object... objects) {
        log.error(format(s, objects));
    }

    @Override
    public void error(String s, Throwable throwable) {
        log.error(s, throwable);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return log.isErrorEnabled();
    }

    @Override
    public void error(Marker marker, String s) {
        log.error(s);
    }

    @Override
    public void error(Marker marker, String s, Object o) {
        log.error(format(s, o));
    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {
        log.error(format(s, o, o1));
    }

    @Override
    public void error(Marker marker, String s, Object... objects) {
        log.error(format(s, objects));
    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {
        log.error(s, throwable);
    }

    private String format(String messagePattern, Object... objects) {
        return arrayFormat(messagePattern, objects).getMessage();
    }
}
