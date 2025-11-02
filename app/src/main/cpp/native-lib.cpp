#include <jni.h>
#include <string>
#include <jni.h>


extern "C"
JNIEXPORT jstring JNICALL
Java_com_muindi_stephen_mobiledeveloperpractical_utils_Constants_getStringBaseUrlDevelopment(
        JNIEnv *env, jclass clazz) {
    return env->NewStringUTF("https://patientvisitapis.intellisoftkenya.com/api/");
}
