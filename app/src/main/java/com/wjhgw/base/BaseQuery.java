package com.wjhgw.base;

import android.content.Context;

import com.wjhgw.config.AppConstants;


public class BaseQuery {

    private Context MContext;

    public BaseQuery(Context context) {
        MContext = context;
    }

    public static final int ENVIRONMENT_PRODUCTION = 1; //公网
    public static final int ENVIROMENT_DEVELOPMENT = 2;  //dev服务器

    public static int environment() {
        return ENVIRONMENT_PRODUCTION;
    }

    public static String serviceUrl() {

        if (ENVIRONMENT_PRODUCTION == BaseQuery.environment()) {
            return AppConstants.SERVER_PRODUCTION;
        } else {
            return AppConstants.SERVER_DEVELOPMENT;
        }
    }
}