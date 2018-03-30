package br.com.andredealmei.error;

public class ValidationErrorDetails extends ErrorDetails {

    private String field;

    private String fieldMessage;


    public static final class builder {

        private String title;
        private int status;
        private String details;
        private Long timestamp;
        private String developerMessage;
        private String field;
        private String fieldMessage;


        private builder() {
        }

        public static builder newBuilder() {
            return new builder();
        }

        public builder title(String title) {
            this.title = title;
            return this;
        }

        public builder status(int status) {
            this.status = status;
            return this;
        }

        public builder details(String details) {
            this.details = details;
            return this;
        }

        public builder timestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public builder developerMessage(String developerMessage) {
            this.developerMessage = developerMessage;
            return this;
        }

        public builder field(String field) {
            this.field = field;
            return this;
        }

        public builder fieldMessage(String fieldMessage) {
            this.fieldMessage = fieldMessage;
            return this;
        }

        public ValidationErrorDetails build() {
            ValidationErrorDetails validationErrorDetails = new ValidationErrorDetails();
            validationErrorDetails.setTitle(this.title);
            validationErrorDetails.setDeveloperMessage(this.developerMessage);
            validationErrorDetails.setStatus(this.status);
            validationErrorDetails.setDetails(this.details);
            validationErrorDetails.setTimestamp(this.timestamp);
            validationErrorDetails.field = field;
            validationErrorDetails.fieldMessage = fieldMessage;
            return validationErrorDetails;
        }
    }

    public String getField() {
        return field;
    }

    public String getFieldMessage() {
        return fieldMessage;
    }
}
