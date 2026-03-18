package com.example.swappie_be.config;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Geometry {

    public GeometryFactory geometryFactory() {
        return new GeometryFactory(new PrecisionModel(), 4326);
    }

    public Point createPoint(Double longitude, Double latitude) {
        Point point = geometryFactory().createPoint(new Coordinate(longitude, latitude));
        return point;
    }


}