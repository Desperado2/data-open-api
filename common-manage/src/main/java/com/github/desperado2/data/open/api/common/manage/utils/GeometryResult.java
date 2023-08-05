package com.github.desperado2.data.open.api.common.manage.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GEOJSON数据格式化
 * @author tu nan
 * @date 2023/4/13
 **/
public class GeometryResult {

    private String type = "FeatureCollection";

    private List<Features> features;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Features> getFeatures() {
        return features;
    }

    public void setFeatures(List<Features> features) {
        this.features = features;
    }

    private static class Features {

        private String type = "Feature";

        private Object properties;

        private Geometry geometry;

        private String geometryType;

        public Features() {
        }

        public Features(Object properties, JSONArray coordinates, String geometryType) {
            this.geometry = new Geometry(coordinates,geometryType);
            this.properties = properties;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getProperties() {
            return properties;
        }

        public void setProperties(Object properties) {
            this.properties = properties;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        public String getGeometryType() {
            return geometryType;
        }

        public void setGeometryType(String geometryType) {
            this.geometryType = geometryType;
        }
    }


    private static class Geometry {

        private JSONArray coordinates;

        private String type = "Polygon";

        public Geometry(JSONArray coordinates,String geometryType) {
            this.coordinates = coordinates;
            this.type = geometryType;
        }

        public JSONArray getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(JSONArray coordinates) {
            this.coordinates = coordinates;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static GeometryResult toPolygonGeoJson(List<Map<String,Object>> coordinates , String coordinatesFiled, String geometryType,
                                             Boolean removeCoordinateFromFeatures ) {
        GeometryResult mapResult = new GeometryResult();
        List<Features> features = new ArrayList<>(coordinates.size());

        for (Map<String,Object> coordinate : coordinates) {
            Object coordinates1 = coordinate.getOrDefault(coordinatesFiled,"");
            if(removeCoordinateFromFeatures){
                coordinate.remove(coordinatesFiled);
            }
            Object properties = JsonUtils.toBean(coordinate, Map.class, new CustomPropertyFilter());
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(JSONObject.parse(coordinates1.toString()));
            features.add(new Features(properties, jsonArray, geometryType));
        }
        mapResult.features = features;
        return mapResult;
    }


    public static GeometryResult toPointGeoJson(List<Map<String,Object>> coordinates , String longitudeField, String latitudeField,
                                             Boolean removeCoordinateFromFeatures ) {
        GeometryResult mapResult = new GeometryResult();
        List<Features> features = new ArrayList<>(coordinates.size());
        for (Map<String,Object> coordinate : coordinates) {
            Object longitude = coordinate.getOrDefault(longitudeField,"");
            Object latitude = coordinate.getOrDefault(latitudeField,"");
            if(removeCoordinateFromFeatures){
                coordinate.remove(longitudeField);
                coordinate.remove(latitudeField);
            }
            Object properties = JsonUtils.toBean(coordinate, Map.class, new CustomPropertyFilter());
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(longitude);
            jsonArray.add(latitude);
            features.add(new Features(properties, jsonArray, "Point"));
        }
        mapResult.features = features;
        return mapResult;
    }



    private static class CustomPropertyFilter implements PropertyFilter {

        @Override
        public boolean apply(Object object, String name, Object value) {
            return true;
        }
    }
}

