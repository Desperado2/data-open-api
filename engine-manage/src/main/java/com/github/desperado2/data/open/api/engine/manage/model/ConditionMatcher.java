package com.github.desperado2.data.open.api.engine.manage.model;

public class ConditionMatcher {
    private String condition;
    private int start;
    private int end;

    public ConditionMatcher() {
    }

    public ConditionMatcher(String condition, int start, int end) {
        this.condition = condition;
        this.start = start;
        this.end = end;
    }

    private ConditionMatcher(Builder builder) {
        setCondition(builder.condition);
        setStart(builder.start);
        setEnd(builder.end);
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public static final class Builder {
        private String condition;
        private int start;
        private int end;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder condition(String val) {
            condition = val;
            return this;
        }

        public Builder start(int val) {
            start = val;
            return this;
        }

        public Builder end(int val) {
            end = val;
            return this;
        }

        public ConditionMatcher build() {
            return new ConditionMatcher(this);
        }
    }
}
