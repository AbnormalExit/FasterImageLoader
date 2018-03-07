//
// Created by sxshi on 2018-3-6.
//
#include "gifloader.h"

//绘制一张图片
extern "C"
JNIEXPORT void JNICALL
drawFrame(GifFileType *gifFileType, GifBean *gifBean, AndroidBitmapInfo info, void *pixels) {
    //播放底层代码
//        拿到当前帧
    SavedImage savedImage = gifFileType->SavedImages[gifBean->current_frame];

    GifImageDesc frameInfo = savedImage.ImageDesc;
    //整幅图片的首地址
    int *px = (int *) pixels;
//    每一行的首地址
    int *line;

//   其中一个像素的位置  不是指针  在颜色表中的索引
    int pointPixel;
    GifByteType gifByteType;
    GifColorType gifColorType;
    ColorMapObject *colorMapObject = frameInfo.ColorMap;
    px = (int *) ((char *) px + info.stride * frameInfo.Top);
    for (int y = frameInfo.Top; y < frameInfo.Top + frameInfo.Height; ++y) {
        line = px;
        for (int x = frameInfo.Left; x < frameInfo.Left + frameInfo.Width; ++x) {
            pointPixel = (y - frameInfo.Top) * frameInfo.Width + (x - frameInfo.Left);
            gifByteType = savedImage.RasterBits[pointPixel];
            gifColorType = colorMapObject->Colors[gifByteType];
            line[x] = argb(255, gifColorType.Red, gifColorType.Green, gifColorType.Blue);
        }
        px = (int *) ((char *) px + info.stride);
    }
}

extern "C"
JNIEXPORT jlong JNICALL
native_displayGif(JNIEnv *env, jclass clzz, jstring jfileName) {

    LOGI("displayGif begin");
    const char *fileName = env->GetStringUTFChars(jfileName, 0);
    int err;
    //用系统函数打开一个gif文件   返回一个结构体，这个结构体为句柄
    GifFileType *gifFileType = DGifOpenFileName(fileName, &err);
    if (err == D_GIF_ERR_OPEN_FAILED) {
        LOGI("加载图片错误%d", err);
        return GIF_ERROR;
    }
    //将图片读写入内存
    if (DGifSlurp(gifFileType) == GIF_ERROR) {
        LOGI("读取图片到内存失败 错误码%d", err);
        return GIF_ERROR;
    }
    GifBean *gifBean = (GifBean *) malloc(sizeof(GifBean));


//    清空内存地址
    memset(gifBean, 0, sizeof(GifBean));
    gifFileType->UserData = gifBean;

    gifBean->dealys = (int *) malloc(sizeof(int) * gifFileType->ImageCount);
    memset(gifBean->dealys, 0, sizeof(int) * gifFileType->ImageCount);
    gifBean->total_frame = gifFileType->ImageCount;
    ExtensionBlock *ext;
    for (int i = 0; i < gifFileType->ImageCount; ++i) {
        SavedImage frame = gifFileType->SavedImages[i];
        for (int j = 0; j < frame.ExtensionBlockCount; ++j) {
            if (frame.ExtensionBlocks[j].Function == GRAPHICS_EXT_FUNC_CODE) {
                ext = &frame.ExtensionBlocks[j];
                break;
            }
        }
        if (ext) {
            int frame_delay = 10 * (ext->Bytes[2] << 8 | ext->Bytes[1]);
            LOGI("时间  %d   ", frame_delay);
            gifBean->dealys[i] = frame_delay;

        }
    }
    LOGI("gif  长度大小    %d  ", gifFileType->ImageCount);
    env->ReleaseStringUTFChars(jfileName, fileName);
    return (jlong) gifFileType;
}


extern "C"
JNIEXPORT jint JNICALL
native_updateFrame(JNIEnv *env, jclass clzz, jlong ndkGif,
                   jobject bitmap) {

    //强转代表gif图片的结构体
    GifFileType *gifFileType = (GifFileType *) ndkGif;
    GifBean *gifBean = (GifBean *) gifFileType->UserData;
    AndroidBitmapInfo info;
    //代表一幅图片的像素数组
    void *pixels;
    AndroidBitmap_getInfo(env, bitmap, &info);
    //锁定bitmap  一幅图片--》二维 数组   ===一个二维数组
    AndroidBitmap_lockPixels(env, bitmap, &pixels);


    drawFrame(gifFileType, gifBean, info, pixels);

    //播放完成之后   循环到下一帧
    gifBean->current_frame += 1;
    LOGI("当前帧  %d  ", gifBean->current_frame);
    LOGI("总共帧数  %d  ", gifBean->total_frame);
    if (gifBean->current_frame >= gifBean->total_frame - 1) {
        gifBean->current_frame = 0;
        LOGI("重新过来  %d  ", gifBean->current_frame);
    }
    AndroidBitmap_unlockPixels(env, bitmap);
    return gifBean->dealys[gifBean->current_frame];
}




extern "C"
JNIEXPORT jint JNICALL
native_getWidth(JNIEnv *env, jclass clzz, jlong ndkGif) {
    LOGI("native_getWidth begin");
    GifFileType *gifFileType = (GifFileType *) ndkGif;
    return gifFileType->SWidth;
}

extern "C"
JNIEXPORT jint JNICALL
native_getHeight(JNIEnv *env, jclass clzz, jlong ndkGif) {
    LOGI("native_getHeight begin");
    GifFileType *gifFileType = (GifFileType *) ndkGif;
    return gifFileType->SHeight;
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
