//
// Created by sxshi on 2018-3-6.
//

#ifndef FASTERIMAGELOADER_GIFLOADER_H
#define FASTERIMAGELOADER_GIFLOADER_H

#include <jni.h>

extern "C"
JNIEXPORT void JNICALL
native_displayGif(JNIEnv *env, jobject instance, jstring jfileName);


extern "C"
JNIEXPORT jint JNICALL
registerNatives(JNIEnv *env, const char *className, const JNINativeMethod *methods,
                jint nMethods);

static const JNINativeMethod displayGifMethod[] = {
        {
                "displayGif", "(Ljava/lang/String)V", (void *) native_displayGif
        }
};

#endif //FASTERIMAGELOADER_GIFLOADER_H
