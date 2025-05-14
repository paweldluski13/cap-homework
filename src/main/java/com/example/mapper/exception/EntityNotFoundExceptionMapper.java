package com.example.mapper.exception;

import com.example.doamin.dto.ErrorDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class EntityNotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {

    private static final Logger log = Logger.getLogger(EntityNotFoundExceptionMapper.class);

    @Override
    public Response toResponse(EntityNotFoundException e) {
        log.warn("Object not found in DB", e);
        ErrorDto error = new ErrorDto(e.getMessage());
        return Response.status(Response.Status.NOT_FOUND).entity(error).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
