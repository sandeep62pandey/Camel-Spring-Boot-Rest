package com.sample.model;

import java.util.List;

public class Failures {

    private List<Failure> failures;

    public List<Failure> getFailures() {
        return failures;
    }

    public void setFailures(List<Failure> failures) {
        this.failures = failures;
    }

    @Override
    public String toString() {
        return "Failures{" +
                "failures=" + failures +
                '}';
    }

    public static class Failure{
        private String code;
        private String reason;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        @Override
        public String toString() {
            return "Failure{" +
                    "code='" + code + '\'' +
                    ", reason='" + reason + '\'' +
                    '}';
        }
    }
}

