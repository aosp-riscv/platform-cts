# Copyright (C) 2015 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := libctscamera2_jni
LOCAL_LICENSE_KINDS := SPDX-license-identifier-Apache-2.0
LOCAL_LICENSE_CONDITIONS := notice

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := \
	native-camera-jni.cpp \
	dng-validate-jni.cpp \
	dynamic-depth-validate-jni.cpp

# Flags needed by DNG SDK
LOCAL_CFLAGS := -DUNIX_ENV=1 -DqDNGBigEndian=0 -DqDNGThreadSafe=1 -DqDNGUseLibJPEG=1 -DqDNGUseXMP=0 -DqDNGValidate=1 -DqDNGValidateTarget=1 -DqAndroid=1 -fexceptions -Wsign-compare -Wno-reorder -Wframe-larger-than=20000

# Flags to avoid warnings from DNG SDK
LOCAL_CFLAGS += -Wall -Werror -Wno-unused-parameter
LOCAL_CFLAGS += -Wno-unused-value -Wno-unused-variable
# Flags related to dynamic depth
LOCAL_CFLAGS += -Wno-ignored-qualifiers -DSTATIC_LIBXML=1

LOCAL_HEADER_LIBRARIES := jni_headers liblog_headers
LOCAL_STATIC_LIBRARIES := libdng_sdk_validate libjpeg_static_ndk
# Dynamic depth libraries
LOCAL_STATIC_LIBRARIES += libdynamic_depth_ndk libimage_io_ndk libbase_ndk libxml2_ndk
LOCAL_SHARED_LIBRARIES := libandroid \
    libnativehelper_compat_libc++ \
    liblog \
    libcamera2ndk \
    libmediandk \
    libz \

# NDK build, shared C++ runtime
LOCAL_SDK_VERSION := current
LOCAL_NDK_STL_VARIANT := c++_shared

include $(BUILD_SHARED_LIBRARY)
