package com.jfcc.castaneda_javier.utils;

import com.jfcc.castaneda_javier.exceptions.ApiException;
import com.jfcc.castaneda_javier.exceptions.ExceptionMaker;
import org.apache.commons.validator.EmailValidator;

public class VerificationUtils {

    public static void checkEmail(String email) throws ApiException {
        EmailValidator validator = EmailValidator.getInstance();
        if (!validator.isValid(email)) {
            throw ExceptionMaker.getException("MAIL1");
        }
    }

}
