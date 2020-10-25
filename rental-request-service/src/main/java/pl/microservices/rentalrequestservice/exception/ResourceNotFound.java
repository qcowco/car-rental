package pl.microservices.rentalrequestservice.exception;

public class ResourceNotFound extends RuntimeException
{
    public ResourceNotFound() {
        super();
    }

    public ResourceNotFound(String message) {
        super(message);
    }
}
