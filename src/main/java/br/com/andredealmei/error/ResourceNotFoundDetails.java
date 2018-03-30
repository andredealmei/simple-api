package br.com.andredealmei.error;

public class ResourceNotFoundDetails {

    private String title;

    private int status;

    private String details;

    private Long timestamp;

    private String developerMessage;

    private ResourceNotFoundDetails() {
    }



    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public static final class builder {
        private String title;
        private int status;
        private String details;
        private Long timestamp;
        private String developerMessage;

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

        public ResourceNotFoundDetails build() {
            ResourceNotFoundDetails resourceNotFoundDetails = new ResourceNotFoundDetails();
            resourceNotFoundDetails.title = this.title;
            resourceNotFoundDetails.developerMessage = this.developerMessage;
            resourceNotFoundDetails.status = this.status;
            resourceNotFoundDetails.details = this.details;
            resourceNotFoundDetails.timestamp = this.timestamp;
            return resourceNotFoundDetails;
        }
    }
}
