package com.coinbase.coinbasesample;

import com.coinbase.v2.models.errors.Errors;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;

import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by johnnychan on 4/5/16.
 */
public class Utils {

    public static String getErrorMessage(retrofit2.Response<?> response, Retrofit retrofit) {
        Converter<ResponseBody, Errors> converter =
                retrofit.responseBodyConverter(Errors.class, new Annotation[0]);

        String message = null;
        Errors errors;

        try {
            errors = converter.convert(response.errorBody());
            if (errors.getErrors() != null && errors.getErrors().size() > 0)
                message = errors.getErrors().get(0).getMessage();
        } catch (IOException e) {

        }

        return message;
    }

}
