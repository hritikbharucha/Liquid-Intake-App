//package edu.sdccd.cisc191;
//import com.google.gson.Gson;
//public class HydrationRequest {
//    private int year;
//    private String make;
//    private String model;
//
//    private static final Gson gson = new Gson();
//    public VehicleRequest(int year, String make, String model) {
//        this.year = year;
//        this.make = make;
//        this.model = model;
//    }
//
//    @Override
//    public String toString() {
//        return "year="+year+", make="+make+", model="+model;
//    }
//
//    public int getYear() {
//        return year;
//    }
//
//    public String getMake() {
//        return make;
//    }
//
//    public String getModel() {
//        return model;
//    }
//
//    public void setYear(int year) {
//        this.year = year;
//    }
//
//    public void setMake(String make) {
//        this.make = make;
//    }
//
//    public void setModel(String model) {
//        this.model = model;
//    }
//
//    public static String toJSON(VehicleRequest customer)  {
//        Gson gson = new Gson();
//        return gson.toJson(customer);
//    }
//    public static VehicleRequest toRequest(String requestStr) {
//        return gson.fromJson(requestStr, VehicleRequest.class);
//    }
//
//}
