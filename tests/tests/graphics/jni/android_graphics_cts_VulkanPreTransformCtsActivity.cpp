/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

#define LOG_TAG "VulkanPreTransformCtsActivity"

#include <android/log.h>
#include <jni.h>
#include <array>

#include "NativeTestHelpers.h"
#include "VulkanPreTransformTestHelpers.h"

#define ALOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

namespace {

jboolean validatePixelValues(JNIEnv* env, jint width, jint height, jboolean setPreTransform,
                             jint preTransformHint) {
    jclass clazz = env->FindClass("android/graphics/cts/VulkanPreTransformTest");
    jmethodID mid = env->GetStaticMethodID(clazz, "validatePixelValuesAfterRotation", "(IIZI)Z");
    if (mid == 0) {
        ALOGE("Failed to find method ID");
        return false;
    }
    return env->CallStaticBooleanMethod(clazz, mid, width, height, setPreTransform,
                                        preTransformHint);
}

void createNativeTest(JNIEnv* env, jclass /*clazz*/, jobject jAssetManager, jobject jSurface,
                      jboolean setPreTransform) {
    ALOGD("jboolean setPreTransform = %d", setPreTransform);
    ASSERT(jAssetManager, "jAssetManager is NULL");
    ASSERT(jSurface, "jSurface is NULL");

    DeviceInfo deviceInfo;
    int preTransformHint;
    VkTestResult ret = deviceInfo.init(env, jSurface);
    if (ret == VK_TEST_PHYSICAL_DEVICE_NOT_EXISTED) {
        ALOGD("Hardware not supported for this test");
        return;
    }
    ASSERT(ret == VK_TEST_SUCCESS, "Failed to initialize Vulkan device");

    SwapchainInfo swapchainInfo(&deviceInfo);
    ASSERT(swapchainInfo.init(setPreTransform, &preTransformHint) == VK_TEST_SUCCESS,
           "Failed to initialize Vulkan swapchain");

    Renderer renderer(&deviceInfo, &swapchainInfo);
    ASSERT(renderer.init(env, jAssetManager) == VK_TEST_SUCCESS,
           "Failed to initialize Vulkan renderer");

    for (uint32_t i = 0; i < 120; ++i) {
        ret = renderer.drawFrame();
        if (setPreTransform || preTransformHint == 0x1 /*VK_SURFACE_TRANSFORM_IDENTITY_BIT_KHR*/) {
            ASSERT(ret == VK_TEST_SUCCESS, "Failed to draw frame(%u) ret(%d)", i, (int)ret);
        } else {
            ASSERT(ret == VK_TEST_SUCCESS_SUBOPTIMAL, "Failed to draw suboptimal frame(%u) ret(%d)",
                   i, (int)ret);
        }
    }

    const VkExtent2D surfaceSize = swapchainInfo.surfaceSize();
    ASSERT(validatePixelValues(env, surfaceSize.width, surfaceSize.height, setPreTransform,
                               preTransformHint),
           "Not properly rotated");
}

const std::array<JNINativeMethod, 1> JNI_METHODS = {{
        {"nCreateNativeTest", "(Landroid/content/res/AssetManager;Landroid/view/Surface;Z)V",
         (void*)createNativeTest},
}};

} // anonymous namespace

int register_android_graphics_cts_VulkanPreTransformCtsActivity(JNIEnv* env) {
    jclass clazz = env->FindClass("android/graphics/cts/VulkanPreTransformCtsActivity");
    return env->RegisterNatives(clazz, JNI_METHODS.data(), JNI_METHODS.size());
}
