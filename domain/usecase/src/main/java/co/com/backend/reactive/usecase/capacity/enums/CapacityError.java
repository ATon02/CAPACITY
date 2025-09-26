package co.com.backend.reactive.usecase.capacity.enums;

public enum CapacityError {
    CAPACITY_ID_REQUIRED("Capacity ID is required"),
    CAPACITY_NAME_REQUIRED("Capacity name is required"),
    CAPACITY_DESCRIPTION_REQUIRED("Capacity description is required"),
    TECHNOLOGIES_REQUIRED("Capacity must have at least 3 technologies"),
    MINIMUM_TECHNOLOGIES_REQUIRED("Capacity must have at least 3 technologies"),
    MAXIMUM_TECHNOLOGIES_EXCEEDED("Capacity cannot have more than 20 technologies"),
    INVALID_TECHNOLOGY_COUNT("Capacity must have between 3 and 20 technologies"),
    DUPLICATE_TECHNOLOGIES("Capacity cannot have duplicate technologies"),
    TECHNOLOGY_NOT_FOUND("No found technology with id"),
    TECHNOLOGY_ALREADY_EXISTS("Technology already exists in this capacity"),
    CAPACITY_NOT_FOUND("Capacity not found"),
    CAPACITY_ALREADY_EXISTS("Capacity with this ID already exists"),
    CAPACITY_SAVE_ERROR("Error saving capacity"),
    CAPACITY_UPDATE_ERROR("Error updating capacity"),
    CAPACITY_DELETE_ERROR("Error deleting capacity"),
    TECHNOLOGY_SERVICE_UNAVAILABLE("Technology service is currently unavailable"),
    TECHNOLOGY_SERVICE_ERROR("Error communicating with technology service");

    private final String message;

    CapacityError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}