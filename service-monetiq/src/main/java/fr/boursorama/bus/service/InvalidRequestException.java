package fr.boursorama.bus.service;

/**
 * Created by fpeyron on 02/10/2015.
 */
public class InvalidRequestException extends RuntimeException {

    private String code;

    public InvalidRequestException(String message) {
        super(message);
    }

}
