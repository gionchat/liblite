package com.suntront.liblite.utils;

import java.util.UUID;

/**
 * Copyright (C), 2015-2019, suntront
 * FileName: UUIDUtils
 * Author: Jeek
 * Date: 2019/12/20 15:54
 * Description: UUID工具类以及UUID常量
 */
public class UUIDUtils {

    public static final String UUID_FINAL = "84F0D958-B058-4483-9204-EE1631EF9A9E";

    public static void main(String[] args) throws Exception {
        System.out.println("UUID:" + UUID.randomUUID().toString().toUpperCase());
    }

}
