#include <jni.h>

JNIEXPORT void JNICALL

JNIEXPORT jlong JNICALL
Java_com_sxshi_giflib_GifLoader_displayGif(JNIEnv *env, jclass type, jstring fileName_) {
    const char *fileName = (*env)->GetStringUTFChars(env, fileName_, 0);

    // TODO

    (*env)->ReleaseStringUTFChars(env, fileName_, fileName);
}