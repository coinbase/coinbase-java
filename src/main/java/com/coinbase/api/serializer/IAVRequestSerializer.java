package com.coinbase.api.serializer;

import com.coinbase.api.entity.IAVRequest;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.HashMap;

public class IAVRequestSerializer extends JsonSerializer<IAVRequest> {

    @Override
    public void serialize(IAVRequest request, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();

        HashMap<String, String> fields = request.getIavFields();
        if(fields != null)
            for(String key : fields.keySet())
                if(fields.get(key) != null)
                    jgen.writeObjectField(key, fields.get(key));

        jgen.writeObjectField("verification_method", request.getVerificationMethod().toString());

        jgen.writeEndObject();
    }
}
