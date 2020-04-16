//
// Created by wangpengcheng on 2020-04-16.
//
#include <android/log.h>
#ifndef BIUBIU_IM_HERO_LOG_H
#define BIUBIU_IM_HERO_LOG_H

#define LOG_TAG "NDK.LOG"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)

#endif //BIUBIU_IM_HERO_LOG_H
