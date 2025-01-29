# Telecom Phone Number Management API

## Overview

The **Telecom Phone Number Management API** is designed for managing phone numbers associated with customers. It provides functionality to retrieve, activate, and manage phone numbers for customers. The API uses **HATEOAS** (Hypermedia as the engine of application state), providing navigable links within responses.

## Version: 1.1.0

## Design first approach
The solution includes design first approach. A contract has been designed and added first. The solution is developed based on contract. 
File name: phone-number-api-v1.0.yaml

## Base URL
The base URL for all API endpoints is: http://localhost:8080/api


## API Endpoints

### 1. **Get All Phone Numbers**
Retrieve all phone numbers stored in the system.

- **Endpoint**: `GET /phone-numbers`
- **Description**: Fetches all phone numbers from the system.
- **Responses**:
    - **200 OK**: A list of all phone numbers in the system.
    - **404 Not Found**: If no phone numbers are found.

**Example response**:
```json
{
  "phoneNumbers": [
    {
      "number": "1234567890",
      "isActive": false,
      "_links": {
        "self": {
          "href": "http://localhost:8080/api/phone-numbers/1234567890",
          "httpMethod": "GET",
          "templated": false
        }
      }
    },
    {
      "number": "9876543210",
      "isActive": true,
      "_links": {
        "self": {
          "href": "http://localhost:8080/api/phone-numbers/9876543210",
          "httpMethod": "GET",
          "templated": false
        }
      }
    }
  ]
} 
```

### 2. **Get All Phone Numbers for a Customer**
Retrieve all phone numbers associated with a specific customer by their customer ID.

- **Endpoint**: `GET /customers/{customerId}/phone-numbers`
- **Description**: Fetches all phone numbers for a customer.
- **Parameters**:
    - **customerId**: Id recognising a customer uniquely
- **Responses**:
    - **200 OK**: A list of phone numbers associated with the customer.
    - **404 Not Found**: If the customer is not found.

**Example response**:
```json
{
  "phoneNumbers": [
    {
      "number": "1234567890",
      "isActive": false,
      "_links": {
        "self": {
          "href": "http://localhost:8080/api/customers/1/phone-numbers/1234567890",
          "httpMethod": "GET",
          "templated": false
        }
      }
    }
  ]
}
```

### 2. **Activate a Phone Number**
Activate or update an existing phone number for a customer.

- **Endpoint**: `PATCH /customers/{customerId}/phone-numbers/{phoneNumber}`
- **Description**: Activate a phone number for a customer.
- - **Parameters**:
    - **customerId**: Id recognising a customer uniquely
    - **phoneNumber**: phone number to be activated
- **Responses**:
    - **200 OK**: Phone number activated successfully.
    - **404 Not Found**: If the phone number is not found or already activated.

**Example response**:
```json
{
  "phoneNumbers": [
    {
      "number": "1234567890",
      "isActive": true,
      "_links": {
        "self": {
          "href": "http://localhost:8080/api/customers/1/phone-numbers/1234567890",
          "httpMethod": "GET",
          "templated": false
        }
      }
    }
  ]
}
```


##Error Handling
The API provides detailed error responses for invalid or failed requests.

400 Bad Request: Invalid or malformed request.
404 Not Found: The resource was not found.
500 Internal Server Error: Server-side issue occurred.
```json
{
  "error": "Customer not found with ID: 1"
}
```

## Testing
unit tests and integration tests are added 

## Authentication
Currently, the API does not require authentication and is designed for local development use only.

## Running the Application Locally
1. Clone the repository: git clone https://github.com/your-username/telecom-phone-number-management.git
2. Navigate to the project directory: cd telecom-phone-number-management
3. Build the project: ./gradlew build
4. Run the application: ./gradlew bootRun
5. Access the API at: http://localhost:8080/api/v1.


## Docker
A docker file is written for containerisation of application.

## Commands to test docker
1. Create the Docker image: docker build -t phone-number-management .
2. Run the Docker container: docker run -p 8080:8080 phone-number-management
