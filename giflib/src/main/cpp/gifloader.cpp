//
// Created by sxshi on 2018-3-6.
//
#include <android/log.h>
#include <assert.h>
#include "gifloader.h"

#define TAG "SSX_JNI"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define NELEM(x) ((int) (sizeof(x) / sizeof((x)[0])))

JavaVM *g_jvm = NULL;
jobject g_obj = NULL;

extern "C"
JNIEXPORT void JNICALL
native_displayGif(JNIEnv *env, jobject instance, jstring jfileName) {
    LOGI("displayGif begin");
}


extern "C"
JNIEXPORT jint JNICALL
registerNatives(JNIEnv *env, const char *className, const JNINativeMethod *methods,
                jint nMethods) {
    LOGI("registerNatives begin");
    //首先找到含有本地方法的java类
    jclass clzz = env->FindClass(className);
    if (clzz == NULL) {
        LOGI("clzz is NULL");
        return JNI_FALSE;
    }
    if ((env->RegisterNatives(clzz, methods, nMethods)) < 0) {
        LOGI("registerNatives fail");
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {

    LOGI("jni_OnLoad begin");
    JNIEnv *env = NULL;

    if ((vm->GetEnv((void **) &env, JNI_VERSION_1_4)) != JNI_OK) {
        LOGI("jni_OnLoad fail");
        return -1;
    }
    assert(env != NULL);
    const char *className = "com/sxshi/giflib/GifLoader";
    registerNatives(env, className, displayGifMethod, NELEM(displayGifMethod));
    return JNI_VERSION_1_4;
}
