package br.com.andredealmei.error;

public class ResourceNotFoundDetails extends ErrorDetails {




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
            resourceNotFoundDetails.setTitle(this.title);
            resourceNotFoundDetails.setDeveloperMessage(this.developerMessage);
            resourceNotFoundDetails.setStatus(this.status);
            resourceNotFoundDetails.setDetails(this.details);
            resourceNotFoundDetails.setTimestamp(this.timestamp);
            return resourceNotFoundDetails;
        }
    }
}
