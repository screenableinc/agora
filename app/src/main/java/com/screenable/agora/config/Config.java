package com.screenable.agora.config;

public class Config {
    public static String APP_IDENT = "AgoraProject";
    public static String userdata_SP_N = "userdata";
    public static String API_access_SP_N = "apiInfo";
    public static String deviceType = "mobile";
//    links
//    public static String address = "https://www.vendnbuy.com/";
    public static String address ="http://192.168.1.128:4000/";    public static String loginLink = address + "users/login";
    public static String joinLink = address + "users/join";
    public static String topBrands = address + "business/top";
    public static String userExists = address + "userexists";
    public static String search = address + "search";
    public static String barcode_search = address + "barcode_search";
    public static String businessLogo = address + "business/logo";
    public static String businessbanner = address + "business/banner";
    public static String businessItems = address+"business/products";
    public static String trendingProducts = address + "products/latest";
    public static String cartItems = address + "users/cart";
    public static String cart = address + "users/cart/items";
    public static String add_cart = address+"users/cart/add";
    public static String makeorder = address + "orders/order";
    public static String userVerify = address + "verify";
    public static String variationsLink = address+"products/variations";

    public static String discoverProducts = address + "products/discover";
    public static String productImages = address + "products/images";


}
