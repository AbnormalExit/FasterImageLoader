//
// Created by sxshi on 2018-3-6.
//

#ifndef FASTERIMAGELOADER_GIFLOADER_H
#define FASTERIMAGELOADER_GIFLOADER_H

#include <jni.h>
#include "gif_lib.h"
#include <android/log.h>
#include <assert.h>
#include <malloc.h>
#include <string.h>


#define TAG "SSX_JNI"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define NELEM(x) ((int) (sizeof(x) / sizeof((x)[0])))
#define  argb(a, r, g, b) ( ((a) & 0xff) << 24 ) | ( ((b) & 0xff) << 16 ) | ( ((g) & 0xff) << 8 ) | ((r) & 0xff)

typedef struct GifBean {
//    有多少帧
    int total_frame;
    //当前帧
    int current_frame;
//   时间集合
    int *dealys;
};


extern "C"
JNIEXPORT jlong JNICALL
native_getGifFileType(JNIEnv *env, jobject instance, jstring jfileName);


extern "C"
JNIEXPORT jint JNICALL
native_getWidth(JNIEnv *env, jobject instance, jlong ndkGif);

extern "C"
JNIEXPORT jint JNICALL
native_getHeight(JNIEnv *env, jobject instance, jlong ndkGif);

extern "C"
JNIEXPORT jint JNICALL
native_updateFrame(JNIEnv *env, jobject instance, jlong ndkGif,
                   jobject bitmap);

extern "C"
JNIEXPORT void JNICALL
drawFrame(GifFileType *gifFileType, GifBean *gifBean, AndroidBitmapInfo info, void *pixels);

extern "C"
JNIEXPORT jint JNICALL
registerNatives(JNIEnv *env, const char *className, const JNINativeMethod *methods,
                jint nMethods);


static const JNINativeMethod displayGifMethod[] = {
        {
                "getGifFileType",  "(Ljava/lang/String;)J",        (void *) native_getGifFileType
        },
        {
                "getWidth",    "(J)I",                         (void *) native_getWidth
        },
        {
                "getHeight",   "(J)I",                         (void *) native_getHeight
        },
        {
                "updateFrame", "(JLandroid/graphics/Bitmap;)I", (void *) native_updateFrame
        }
};

#endif //FASTERIMAGELOADER_GIFLOADER_H
