LOCAL_PATH := $(call my-dir)

cts_bionic_tests_dir := lib32
lib_or_lib64 := lib

ifeq (true,$(TARGET_IS_64_BIT))
  ifeq (,$(cts_bionic_tests_2nd_arch_prefix))
    cts_bionic_tests_dir := lib64
    lib_or_lib64 := lib64
  endif
endif

# TODO(dimitry): Can this list be constructed dynamically?
my_bionic_testlib_files := \
  cfi_test_helper/cfi_test_helper \
  cfi_test_helper2/cfi_test_helper2 \
  dt_runpath_a/libtest_dt_runpath_a.so \
  dt_runpath_b_c_x/libtest_dt_runpath_b.so \
  dt_runpath_b_c_x/libtest_dt_runpath_c.so \
  dt_runpath_b_c_x/libtest_dt_runpath_x.so \
  dt_runpath_y/$(lib_or_lib64)/libtest_dt_runpath_y.so \
  elftls_dlopen_ie_error_helper/elftls_dlopen_ie_error_helper \
  exec_linker_helper/exec_linker_helper \
  exec_linker_helper_lib.so \
  heap_tagging_async_helper/heap_tagging_async_helper \
  heap_tagging_disabled_helper/heap_tagging_disabled_helper \
  heap_tagging_static_sync_helper/heap_tagging_static_sync_helper \
  heap_tagging_static_async_helper/heap_tagging_static_async_helper \
  heap_tagging_static_disabled_helper/heap_tagging_static_disabled_helper \
  heap_tagging_sync_helper/heap_tagging_sync_helper \
  inaccessible_libs/libtestshared.so \
  inaccessible_libs/libtestshared.so \
  ld_config_test_helper/ld_config_test_helper \
  ld_config_test_helper_lib3.so \
  ld_preload_test_helper/ld_preload_test_helper \
  ld_preload_test_helper_lib1.so \
  ld_preload_test_helper_lib2.so \
  libatest_simple_zip/libatest_simple_zip.so \
  libcfi-test-bad.so \
  libcfi-test.so \
  libdl_preempt_test_1.so \
  libdl_preempt_test_2.so \
  libdl_test_df_1_global.so \
  libdlext_test.so \
  libdlext_test_different_soname.so \
  libdlext_test_fd/libdlext_test_fd.so \
  libdlext_test_norelro.so \
  libdlext_test_recursive.so \
  libdlext_test_runpath_zip/libdlext_test_runpath_zip_zipaligned.zip \
  libdlext_test_zip/libdlext_test_zip.so \
  libdlext_test_zip/libdlext_test_zip_zipaligned.zip \
  libgnu-hash-table-library.so \
  libns_hidden_child_global.so \
  libns_hidden_child_internal.so \
  libns_hidden_child_public.so \
  librelocations-fat.so \
  librelocations-ANDROID_RELR.so \
  librelocations-ANDROID_REL.so \
  librelocations-RELR.so \
  libsegment_gap_inner.so \
  libsegment_gap_outer.so \
  libsysv-hash-table-library.so \
  libtest_atexit.so \
  libtest_check_order_dlsym.so \
  libtest_check_order_dlsym_1_left.so \
  libtest_check_order_dlsym_2_right.so \
  libtest_check_order_dlsym_3_c.so \
  libtest_check_order_dlsym_a.so \
  libtest_check_order_dlsym_b.so \
  libtest_check_order_dlsym_d.so \
  libtest_check_order_reloc_root.so \
  libtest_check_order_reloc_root_1.so \
  libtest_check_order_reloc_root_2.so \
  libtest_check_order_reloc_siblings.so \
  libtest_check_order_reloc_siblings_1.so \
  libtest_check_order_reloc_siblings_2.so \
  libtest_check_order_reloc_siblings_3.so \
  libtest_check_order_reloc_siblings_a.so \
  libtest_check_order_reloc_siblings_b.so \
  libtest_check_order_reloc_siblings_c.so \
  libtest_check_order_reloc_siblings_c_1.so \
  libtest_check_order_reloc_siblings_c_2.so \
  libtest_check_order_reloc_siblings_d.so \
  libtest_check_order_reloc_siblings_e.so \
  libtest_check_order_reloc_siblings_f.so \
  libtest_check_rtld_next_from_library.so \
  libtest_dlopen_df_1_global.so \
  libtest_dlopen_from_ctor.so \
  libtest_dlopen_from_ctor_main.so \
  libtest_dlopen_weak_undefined_func.so \
  libtest_dlsym_df_1_global.so \
  libtest_dlsym_from_this.so \
  libtest_dlsym_from_this_child.so \
  libtest_dlsym_from_this_grandchild.so \
  libtest_dlsym_weak_func.so \
  libtest_dt_runpath_d.so \
  libtest_elftls_dynamic.so \
  libtest_elftls_dynamic_filler_1.so \
  libtest_elftls_dynamic_filler_2.so \
  libtest_elftls_dynamic_filler_3.so \
  libtest_elftls_shared_var.so \
  libtest_elftls_shared_var_ie.so \
  libtest_elftls_tprel.so \
  libtest_empty.so \
  libtest_ifunc.so \
  libtest_ifunc_variable.so \
  libtest_ifunc_variable_impl.so \
  libtest_indirect_thread_local_dtor.so \
  libtest_init_fini_order_child.so \
  libtest_init_fini_order_grand_child.so \
  libtest_init_fini_order_root.so \
  libtest_init_fini_order_root2.so \
  libtest_nodelete_1.so \
  libtest_nodelete_2.so \
  libtest_nodelete_dt_flags_1.so \
  libtest_pthread_atfork.so \
  libtest_relo_check_dt_needed_order.so \
  libtest_relo_check_dt_needed_order_1.so \
  libtest_relo_check_dt_needed_order_2.so \
  libtest_simple.so \
  libtest_thread_local_dtor.so \
  libtest_thread_local_dtor2.so \
  libtest_two_parents_child.so \
  libtest_two_parents_parent1.so \
  libtest_two_parents_parent2.so \
  libtest_versioned_lib.so \
  libtest_versioned_libv1.so \
  libtest_versioned_libv2.so \
  libtest_versioned_otherlib.so \
  libtest_versioned_otherlib_empty.so \
  libtest_versioned_uselibv1.so \
  libtest_versioned_uselibv2.so \
  libtest_versioned_uselibv2_other.so \
  libtest_versioned_uselibv3_other.so \
  libtest_with_dependency.so \
  libtest_with_dependency_loop.so \
  libtest_with_dependency_loop_a.so \
  libtest_with_dependency_loop_b.so \
  libtest_with_dependency_loop_b_tmp.so \
  libtest_with_dependency_loop_c.so \
  ns2/ld_config_test_helper_lib1.so \
  ns2/ld_config_test_helper_lib2.so \
  ns_a/libnstest_ns_a_public1.so \
  ns_a/libnstest_ns_a_public1_internal.so \
  ns_b/libnstest_ns_b_public2.so \
  ns_b/libnstest_ns_b_public3.so \
  ns_hidden_child_app/libns_hidden_child_app.so \
  ns_hidden_child_helper/ns_hidden_child_helper \
  prebuilt-elf-files/libtest_invalid-empty_shdr_table.so \
  prebuilt-elf-files/libtest_invalid-rw_load_segment.so \
  prebuilt-elf-files/libtest_invalid-textrels.so \
  prebuilt-elf-files/libtest_invalid-textrels2.so \
  prebuilt-elf-files/libtest_invalid-unaligned_shdr_offset.so \
  prebuilt-elf-files/libtest_invalid-zero_shdr_table_content.so \
  prebuilt-elf-files/libtest_invalid-zero_shdr_table_offset.so \
  prebuilt-elf-files/libtest_invalid-zero_shentsize.so \
  prebuilt-elf-files/libtest_invalid-zero_shstrndx.so \
  preinit_getauxval_test_helper/preinit_getauxval_test_helper \
  preinit_syscall_test_helper/preinit_syscall_test_helper \
  private_namespace_libs/libnstest_dlopened.so \
  private_namespace_libs/libnstest_private.so \
  private_namespace_libs/libnstest_root.so \
  private_namespace_libs/libnstest_root_not_isolated.so \
  private_namespace_libs/libtest_missing_symbol_child_private.so \
  private_namespace_libs/libtest_missing_symbol_root.so \
  private_namespace_libs_external/libnstest_private_external.so \
  public_namespace_libs/libnstest_public.so \
  public_namespace_libs/libnstest_public_internal.so \
  public_namespace_libs/libtest_missing_symbol.so \
  public_namespace_libs/libtest_missing_symbol_child_public.so \

my_bionic_testlibs_src_dir := \
  $($(cts_bionic_tests_2nd_arch_prefix)TARGET_OUT_DATA_NATIVE_TESTS)/bionic-loader-test-libs
my_bionic_testlibs_out_dir := $(cts_bionic_tests_dir)/bionic-loader-test-libs

LOCAL_COMPATIBILITY_SUPPORT_FILES += \
  $(foreach lib, $(my_bionic_testlib_files), \
    $(my_bionic_testlibs_src_dir)/$(lib):$(my_bionic_testlibs_out_dir)/$(lib))

# Special casing for libtest_dt_runpath_y.so. Since we use the standard ARM CTS
# to test ARM-on-x86 devices where ${LIB} is expanded to lib/arm, the lib
# is installed to ./lib/arm as well as ./lib to make sure that the lib can be
# found on any device.
archname := $(TARGET_ARCH)
ifneq (,$(cts_bionic_tests_2nd_arch_prefix))
  archname := $(TARGET_2ND_ARCH)
endif

src := $(my_bionic_testlibs_src_dir)/dt_runpath_y/$(lib_or_lib64)/libtest_dt_runpath_y.so
dst := $(my_bionic_testlibs_out_dir)/dt_runpath_y/$(lib_or_lib64)/$(archname)/libtest_dt_runpath_y.so

LOCAL_COMPATIBILITY_SUPPORT_FILES += $(src):$(dst)

my_bionic_testlib_files :=
my_bionic_testlibs_src_dir :=
my_bionic_testlibs_out_dir :=
cts_bionic_tests_dir :=
cts_bionic_tests_2nd_arch_prefix :=
lib_or_lib64 :=
archname :=
src :=
dst :=

