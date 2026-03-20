package com.example.swappie_be.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.locationtech.jts.geom.Point;
import org.springframework.boot.jackson.JacksonComponent;

import java.io.IOException;

@JacksonComponent
public class PointSerializer extends JsonSerializer<Point> {

    @Override
    public void serialize(Point point, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("lng", point.getX());
        gen.writeNumberField("lat", point.getY());
        gen.writeEndObject();
    }
}
