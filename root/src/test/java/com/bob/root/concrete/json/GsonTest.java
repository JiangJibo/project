package com.bob.root.concrete.json;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author wb-jjb318191
 * @create 2018-05-25 18:25
 */
public class GsonTest {

    // Gson将时间戳转换为日期
    private static final Gson GSON;

    static {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(java.util.Date.class, new DateSerializer()).setDateFormat(DateFormat.LONG);
        gb.registerTypeAdapter(java.util.Date.class, new DateDeserializer()).setDateFormat(DateFormat.LONG);
        GSON = gb.create();
    }


    static class DateDeserializer implements JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
        }
    }

    static class DateSerializer implements JsonSerializer<Date> {

        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getTime());
        }
    }

}
