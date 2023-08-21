package com.vsafe.admin.server.helpers.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.vsafe.admin.server.business.response.BaseResponse;

import java.util.Date;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class DateDeSerializer extends StdDeserializer<Date> {
    public DateDeSerializer() {
        super(Date.class);
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, RuntimeException {
        String value = p.readValueAs(String.class);
        try {
            if (!isValidFormat("dd/MM/yyyy", value, Locale.ENGLISH)) {
                throw new RuntimeException("Request không hợp lệ");
            }
            return new SimpleDateFormat("dd/MM/yyyy").parse(value);
        } catch (Exception ex) {
            throw new RuntimeException("Request không hợp lệ");
        }
    }

    public  boolean isValidFormat(String format, String value, Locale locale) {
        LocalDateTime ldt = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, locale);

        try {
            ldt = LocalDateTime.parse(value, formatter);
            String result = ldt.format(formatter);
            return result.equals(value);
        } catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(value, formatter);
                String result = ld.format(formatter);
                return result.equals(value);
            } catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(value, formatter);
                    String result = lt.format(formatter);
                    return result.equals(value);
                } catch (DateTimeParseException e2) {
                }
            }
        }
        return false;
    }
}
