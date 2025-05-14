package com.example.mapper.exception;

import com.example.doamin.dto.ErrorDto;
import com.example.exception.WrongStatusException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class WrongStatusExceptionMapper implements ExceptionMapper<WrongStatusException> {

    private static final Logger log = Logger.getLogger(EntityNotFoundExceptionMapper.class);

    @Override
    public Response toResponse(WrongStatusException e) {
        log.warn("Invalid status value", e);
        ErrorDto error = new ErrorDto(e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(error).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
