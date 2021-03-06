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
# Builds a compatibility test suite.
#

# Generate suite info property file
suite_info_prop := $(call intermediates-dir-for,JAVA_LIBRARIES,$(LOCAL_MODULE),true,COMMON)/test-suite-info.properties
$(suite_info_prop): PRIVATE_SUITE_BUILD_NUMBER := $(LOCAL_SUITE_BUILD_NUMBER)
$(suite_info_prop): PRIVATE_SUITE_TARGET_ARCH := $(LOCAL_SUITE_TARGET_ARCH)
$(suite_info_prop): PRIVATE_SUITE_NAME := $(LOCAL_SUITE_NAME)
$(suite_info_prop): PRIVATE_SUITE_FULLNAME := $(LOCAL_SUITE_FULLNAME)
$(suite_info_prop): PRIVATE_SUITE_VERSION := $(LOCAL_SUITE_VERSION)
$(suite_info_prop): cts/build/compatibility_test_suite.mk $(LOCAL_MODULE_MAKEFILE)
	@echo Generating: $@
	$(hide) echo "# This file is auto generated by Android.mk. Do not modify." > $@
	$(hide) echo "build_number = $(PRIVATE_SUITE_BUILD_NUMBER)" >> $@
	$(hide) echo "target_arch = $(PRIVATE_SUITE_TARGET_ARCH)" >> $@
	$(hide) echo "name = $(PRIVATE_SUITE_NAME)" >> $@
	$(hide) echo "fullname = $(PRIVATE_SUITE_FULLNAME)" >> $@
	$(hide) echo "version = $(PRIVATE_SUITE_VERSION)" >> $@

# Reset variables
LOCAL_SUITE_BUILD_NUMBER :=
LOCAL_SUITE_NAME :=
LOCAL_SUITE_FULLNAME :=
LOCAL_SUITE_VERSION :=

# Include the test suite properties file
LOCAL_JAVA_RESOURCE_FILES += $(suite_info_prop)

# Add the base libraries
LOCAL_JAVA_LIBRARIES += tradefed loganalysis compatibility-host-util

LOCAL_MODULE_TAGS := optional

# If DynamicConfig.xml exists copy it inside the jar
ifneq (,$(wildcard $(LOCAL_PATH)/DynamicConfig.xml))
  dynamic_config_local := $(call intermediates-dir-for,JAVA_LIBRARIES,$(LOCAL_MODULE),true,COMMON)/$(LOCAL_MODULE).dynamic
  $(eval $(call copy-one-file,$(LOCAL_PATH)/DynamicConfig.xml,$(dynamic_config_local)))
  LOCAL_JAVA_RESOURCE_FILES += $(dynamic_config_local)
endif

include $(BUILD_HOST_JAVA_LIBRARY)

dynamic_config_local :=
suite_info_prop :=
