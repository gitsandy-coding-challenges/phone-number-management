package com.telecom.phone.number.management.exception;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link GlobalExceptionHandler}.
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    static Stream<TestCaseData> exceptionTestCases() {
        return Stream.of(
            new TestCaseData(
                new NoHandlerFoundException("GET", "/nonexistent-url", HttpHeaders.EMPTY),
                HttpStatus.NOT_FOUND,
                "The requested URL /nonexistent-url was not found on this server."
            ),
            new TestCaseData(
                new ResourceNotFoundException("Resource not found"),
                HttpStatus.NOT_FOUND,
                "Resource not found"
            ),
            new TestCaseData(
                new NumberAlreadyActivatedException("Number already activated"),
                HttpStatus.CONFLICT,
                "Number already activated"
            ),
            new TestCaseData(
                new Exception("Internal error"),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal error"
            )
        );
    }

    @ParameterizedTest
    @MethodSource("exceptionTestCases")
    void testGlobalExceptionHandler(TestCaseData testCaseData) {
        // Act
        ResponseEntity<String> response = handleException(testCaseData.exception());

        // Assert
        assertEquals(testCaseData.expectedStatus(), response.getStatusCode());
        assertEquals(testCaseData.expectedMessage(), response.getBody());
    }

    private ResponseEntity<String> handleException(Exception ex) {
        return switch (ex) {
            case NoHandlerFoundException noHandlerFoundException ->
                globalExceptionHandler.handleNoHandlerFoundException(noHandlerFoundException);
            case ResourceNotFoundException resourceNotFoundException ->
                globalExceptionHandler.handleResourceNotFoundException(resourceNotFoundException);
            case NumberAlreadyActivatedException numberAlreadyActivatedException ->
                globalExceptionHandler.handleNumberAlreadyActivatedException(numberAlreadyActivatedException);
            case null, default -> {
                assert ex != null;
                yield globalExceptionHandler.handleGenericException(ex);
            }
        };
    }

    record TestCaseData(Exception exception, HttpStatus expectedStatus, String expectedMessage) {
    }
}
