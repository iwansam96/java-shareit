package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RestControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler({ UserNotFoundException.class })
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ BookingNotFoundException.class })
    public ResponseEntity<Object> handleBookingNotFoundException(BookingNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ BookingApproveAfterApproveException.class })
    public ResponseEntity<Object> handleBookingApproveAfterApproveException(BookingApproveAfterApproveException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ BookingApprovePermissionsException.class })
    public ResponseEntity<Object> handleBookingApprovePermissionsException(BookingApprovePermissionsException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ BookingItemByOwnerException.class })
    public ResponseEntity<Object> handleBookingItemByOwnerException(BookingItemByOwnerException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ ItemNotFoundException.class })
    public ResponseEntity<Object> handleItemNotFoundException(ItemNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ ItemIsNotAvailableException.class })
    public ResponseEntity<Object> handleItemIsNotAvailableException(ItemIsNotAvailableException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ CommentBeforeBookingException.class })
    public ResponseEntity<Object> handleCommentBeforeBookingException(CommentBeforeBookingException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ EntityNotFoundException.class })
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ ItemEditingByNonOwnerException.class })
    public ResponseEntity<Object> handleItemEditingByNonOwnerException(ItemEditingByNonOwnerException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ ItemRequestNotFoundException.class })
    public ResponseEntity<Object> handleItemRequestNotFoundException(ItemRequestNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
