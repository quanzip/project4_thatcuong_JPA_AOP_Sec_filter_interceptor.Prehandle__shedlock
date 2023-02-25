package com.viettel.project.common;

import com.viettel.project.service.dto.ResponseDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NoResultException;

@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(value = NullPointerException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDTO<Void>> nullPointerHandler(Exception ex){
        ex.printStackTrace();
        return ResponseEntity.ok().body(new ResponseDTO<>().status(com.viettel.project.common.HttpStatus.not_found).message(ex.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = NoResultException.class)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity handleNoResultException(Exception ex){
        ex.printStackTrace();
        ResponseDTO responseDTO =  new ResponseDTO<Void>().status(com.viettel.project.common.HttpStatus.not_found).message(ex.getMessage());
        return ResponseEntity.ok().body(responseDTO);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public  ResponseEntity<ResponseDTO<Void>> handleBadInputException(Exception ex){
        ex.printStackTrace();
        return ResponseEntity.ok().body(new ResponseDTO().status(HttpStatus.BAD_REQUEST.value()).message(ex.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    public  ResponseEntity<ResponseDTO<Void>> handlerException(Exception exception){
        exception.printStackTrace();
        return ResponseEntity.ok().body(new ResponseDTO().status(HttpStatus.BAD_REQUEST.value()).message(exception.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ResponseEntity<ResponseDTO<Void>> handleConflictData(Exception ex){
        ex.printStackTrace();
        return ResponseEntity.ok().body(new ResponseDTO().status(HttpStatus.CONFLICT.value()).message(ex.getMessage()));
    }
}
