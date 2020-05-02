#include <jni.h>
#include <string>

bool featuresEnabled = false;

extern "C" JNIEXPORT jboolean JNICALL
Java_com_example_androidloginjava_LoginForm_Check(JNIEnv *env, jclass clazz, jstring user,
                                                  jstring pass) {
    const char *userStr = env->GetStringUTFChars(user, 0);
    const char *passStr = env->GetStringUTFChars(pass, 0);

    // Here you can implement server checks

    // This is just an example
    // You should not check username and password locally as it can be easly crackable
    // This implementation is for peoples who have programming skills in Java, C++ and php and can protect/encrypt the lib
    if (strcmp(userStr, "myusername") == 0 && strcmp(passStr, "mypassword") == 0) {
        featuresEnabled = true;
        return true;
    }
    return false;
}