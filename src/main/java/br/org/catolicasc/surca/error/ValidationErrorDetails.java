package br.org.catolicasc.surca.error;

public class ValidationErrorDetails extends ErrorDetailsWithFieldMessage {

    public static final class Builder {
        private String title;
        private int status;
        private String detail;
        private long timestamp;
        private String developmentMessage;
        private String field;
        private String fieldMessage;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public Builder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder field(String field) {
            this.field = field;
            return this;
        }

        public Builder developmentMessage(String developmentMessage) {
            this.developmentMessage = developmentMessage;
            return this;
        }

        public Builder fieldMessage(String fieldMessage) {
            this.fieldMessage = fieldMessage;
            return this;
        }

        public ValidationErrorDetails build() {
            ValidationErrorDetails validationErrorDetails = new ValidationErrorDetails();
            validationErrorDetails.setStatus(status);
            validationErrorDetails.setDevelopmentMessage(developmentMessage);
            validationErrorDetails.setTitle(title);
            validationErrorDetails.setTimestamp(timestamp);
            validationErrorDetails.setDetail(detail);
            validationErrorDetails.setField(field);
            validationErrorDetails.setFieldMessage(fieldMessage);
            return validationErrorDetails;
        }
    }
}
