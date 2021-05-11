#include <sys/types.h>
#include <pthread.h>
#include <jni.h>
#include <unistd.h>
#include "Logger.h"

bool loggedin = false;

extern "C"
JNIEXPORT void JNICALL
Java_com_example_androidloginjava_MainActivity_Check(JNIEnv *env, jclass clazz) {
    loggedin = true;
}

//Simple security check
void *new_thread(void *) {
    //Loops until logged in
    do {
        sleep(1);
    } while (!loggedin);

    //Successfully logged in and load your stuff here

    LOGD("Logged in!");

    return NULL;
}

__attribute__((constructor))
void lib_main() {
    // Create a new thread so it does not block the main thread, means the app would not freeze
    pthread_t ptid;
    pthread_create(&ptid, NULL, new_thread, NULL);
}

