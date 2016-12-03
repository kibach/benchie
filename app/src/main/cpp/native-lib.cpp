#include <jni.h>
#include <string>

jdouble *z_A;
jdoubleArray _jA;
jdouble *z_B;
jdoubleArray _jB;
jdouble *z_C;
jdoubleArray _jC;
int _n = 0;

jfloat *__A;
jfloatArray  __jA;
jfloat *__B;
jfloatArray  __jB;
jfloat *__C;
jfloatArray  __jC;
int __n = 0;

void mmuli(int n, jdouble * A, jdouble * B, jdouble * C) {
    for (int i = 0; i < n * n; i++) {
        C[i] = 0;
    }

    for (int j = 0; j < n; j++) {
        int jn = j * n;
        for (int k = 0; k < n; k++) {
            int kn = k * n;
            double bkjn = B[k + jn];
            for (int i = 0; i < n; i++) {
                C[i + jn] += A[i + kn] * bkjn;
            }
        }
    }
}

void mmuli(int n, jfloat * A, jfloat * B, jfloat * C) {
    for (int i = 0; i < n * n; i++) {
        C[i] = 0;
    }

    for (int j = 0; j < n; j++) {
        int jn = j * n;
        for (int k = 0; k < n; k++) {
            int kn = k * n;
            float bkjn = B[k + jn];
            for (int i = 0; i < n; i++) {
                C[i + jn] += A[i + kn] * bkjn;
            }
        }
    }
}

extern "C"
void
Java_com_me_kbocharov_bochbench_benchmark_NativeDoubleMultiplicationBenchmark_prepareStage(
        JNIEnv *env,
        jobject _this,
        jint n,
        jdoubleArray A,
        jdoubleArray B,
        jdoubleArray C) {
    jboolean isCopy;

    _n = n;
    _jA = A;
    z_A = env->GetDoubleArrayElements(A, &isCopy);
    _jB = B;
    z_B = env->GetDoubleArrayElements(B, &isCopy);
    _jC = C;
    z_C = env->GetDoubleArrayElements(C, &isCopy);
}

extern "C"
void
        Java_com_me_kbocharov_bochbench_benchmark_NativeDoubleMultiplicationBenchmark_mmuli(
        JNIEnv *env,
        jobject _this) {
    mmuli(_n, z_A, z_B, z_C);
}

extern "C"
void
Java_com_me_kbocharov_bochbench_benchmark_NativeDoubleMultiplicationBenchmark_cleanupStage(
        JNIEnv *env,
        jobject _this) {
    /*env->ReleaseDoubleArrayElements(_jA, z_A, 0);
    env->ReleaseDoubleArrayElements(_jB, z_B, 0);
    env->ReleaseDoubleArrayElements(_jC, z_C, 0);*/
}

extern "C"
void
Java_com_me_kbocharov_bochbench_benchmark_NativeFloatMultiplicationBenchmark_prepareStage(
        JNIEnv *env,
        jobject _this,
        jint n,
        jfloatArray A,
        jfloatArray B,
        jfloatArray C) {
    jboolean isCopy;

    __n = n;
    __jA = A;
    __A = env->GetFloatArrayElements(A, &isCopy);
    __jB = B;
    __B = env->GetFloatArrayElements(B, &isCopy);
    __jC = C;
    __C = env->GetFloatArrayElements(C, &isCopy);
}

extern "C"
void
Java_com_me_kbocharov_bochbench_benchmark_NativeFloatMultiplicationBenchmark_mmuli(
        JNIEnv *env,
        jobject _this) {
    mmuli(__n, __A, __B, __C);
}

extern "C"
void
Java_com_me_kbocharov_bochbench_benchmark_NativeFloatMultiplicationBenchmark_cleanupStage(
        JNIEnv *env,
        jobject _this) {
    /*env->ReleaseFloatArrayElements(__jA, __A, 0);
    env->ReleaseFloatArrayElements(__jB, __B, 0);
    env->ReleaseFloatArrayElements(__jC, __C, 0);*/
}
