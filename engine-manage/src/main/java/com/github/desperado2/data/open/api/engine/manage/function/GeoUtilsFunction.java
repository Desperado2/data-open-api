package com.github.desperado2.data.open.api.engine.manage.function;


import com.github.desperado2.data.open.api.common.manage.utils.GeometryResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 工具类
 */
@Component
public class GeoUtilsFunction implements IFunction{


    @Override
    public String getVarName() {
        return "GeoUtils";
    }


    public static GeometryResult toPolygonGeoJson(List<Map<String,Object>> coordinates, String geometryType) {
        return toPolygonGeoJson(coordinates, "coordinates", geometryType );
    }


    public static GeometryResult toPolygonGeoJson(List<Map<String,Object>> coordinates , String coordinatesFiled, String geometryType) {
        return toPolygonGeoJson(coordinates, coordinatesFiled, geometryType, false);
    }


    public static GeometryResult toPolygonGeoJson(List<Map<String,Object>> coordinates , String coordinatesFiled, String geometryType,
                                                  Boolean removeCoordinateFromFeatures ) {
        return GeometryResult.toPolygonGeoJson(coordinates, coordinatesFiled, geometryType, removeCoordinateFromFeatures);
    }


    public static GeometryResult toPointGeoJson(List<Map<String,Object>> coordinates , String longitudeField, String latitudeField,
                                                Boolean removeCoordinateFromFeatures ) {
       return GeometryResult.toPointGeoJson(coordinates, longitudeField, latitudeField, removeCoordinateFromFeatures);
    }


    public static GeometryResult toPointGeoJson(List<Map<String,Object>> coordinates ,
                                                Boolean removeCoordinateFromFeatures ) {
        return toPointGeoJson(coordinates, "longitude", "latitude", removeCoordinateFromFeatures);
    }

    public static GeometryResult toPointGeoJson(List<Map<String,Object>> coordinates) {
        return toPointGeoJson(coordinates, false);
    }

}
