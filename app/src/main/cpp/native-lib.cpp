#include <jni.h>
#include <string>


extern "C"
JNIEXPORT jstring JNICALL
Java_com_app_bigprize_Value_BIG_1Constants_getAppURL(JNIEnv *env, jclass clazz) {
    // TODO: implement getAppURL()
    std::string hello = "https://bigprize.in/App/api101/";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_app_bigprize_Value_BIG_1Constants_getMIV(JNIEnv *env, jclass clazz) {
    // TODO: implement getMIV()
    std::string hello = "df5sgh764dfh8sdf";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_app_bigprize_Value_BIG_1Constants_getMKEY(JNIEnv *env, jclass clazz) {
    // TODO: implement getMKEY()
    std::string hello = "dfhdfd3f5hdf6h4d";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_app_bigprize_Value_BIG_1Constants_getUSERTOKEN(JNIEnv *env, jclass clazz) {
    // TODO: implement getUSERTOKEN()
    std::string hello = "4b36d6a5-e3e9-658t-8584-d8af62d21c92";
    return env->NewStringUTF(hello.c_str());

}