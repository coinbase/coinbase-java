package com.coinbase.coinbasesample;

import com.coinbase.v2.models.errors.Errors;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;

import retrofit.Converter;
import retrofit.Retrofit;

/**
 * Created by johnnychan on 4/5/16.
 */
public class Utils {

    public static String getErrorMessage(retrofit.Response<?> response, Retrofit retrofit) {
        Converter<ResponseBody, Errors> converter =
                retrofit.responseConverter(Errors.class, new Annotation[0]);

        String message = null;
        Errors errors;

        try {
            errors = converter.convert(response.errorBody());
            if (errors.getErrors().size() > 0)
                message = errors.getErrors().get(0).getMessage();
        } catch (IOException e) {

        }

        return message;
    }

}
