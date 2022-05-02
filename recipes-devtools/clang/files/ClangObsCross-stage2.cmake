set(OBS_TARGET "" CACHE STRING "")
set(OBS_TARGET_SYS "" CACHE STRING "")
set(${OBS_TARGET_SYS}_CMAKE_SYSROOT "" CACHE STRING "")
set(OBS_HOST "" CACHE STRING "")
set(OBS_HOST_SYS "" CACHE STRING "")
set(${OBS_HOST_SYS}_CMAKE_SYSROOT "" CACHE STRING "")

set(Python3_EXECUTABLE "" CACHE STRING "")

set(CMAKE_BUILD_TYPE Release CACHE STRING "")
set(CMAKE_VERBOSE_MAKEFILE Release CACHE STRING "")

set(CLANG_DEFAULT_LINKER lld CACHE STRING "")
set(CLANG_DEFAULT_OBJCOPY llvm-objcopy CACHE STRING "")
set(CLANG_DEFAULT_RTLIB compiler-rt CACHE STRING "")
set(CLANG_DEFAULT_CXX_STDLIB libc++ CACHE STRING "")

set(LLVM_ENABLE_PROJECTS "clang;clang-tools-extra;lld;llvm" CACHE STRING "")
set(LLVM_ENABLE_RUNTIMES "compiler-rt;libcxx;libcxxabi;libunwind" CACHE STRING "")
set(LLVM_ENABLE_PER_TARGET_RUNTIME_DIR ON CACHE BOOL "")

set(LLVM_ENABLE_LIBCXX ON CACHE BOOL "")
set(LLVM_ENABLE_TERMINFO OFF CACHE BOOL "")
set(LLVM_ENABLE_Z3_SOLVER OFF CACHE BOOL "")
set(LLVM_ENABLE_ZLIB ON CACHE BOOL "")
set(LLVM_INCLUDE_DOCS OFF CACHE BOOL "")
set(LLVM_INCLUDE_EXAMPLES OFF CACHE BOOL "")
set(LLVM_STATIC_LINK_CXX_STDLIB ON CACHE BOOL "")
set(LLVM_USE_RELATIVE_PATHS_IN_FILES ON CACHE BOOL "")
set(LLVM_TARGETS_TO_BUILD "BPF;${OBS_TARGET}" CACHE STRING "")
set(LLVM_ENABLE_LLD ON CACHE BOOL "")

set(LLVM_BUILTIN_TARGETS "${OBS_TARGET_SYS}" CACHE STRING "")
set(LLVM_RUNTIME_TARGETS "${OBS_TARGET_SYS}" CACHE STRING "")

macro(obs_add_target target)
  set(BUILTINS_${target}_CMAKE_SYSTEM_NAME Linux CACHE STRING "")
  set(BUILTINS_${target}_CMAKE_BUILD_TYPE Release CACHE STRING "")
  set(BUILTINS_${target}_CMAKE_SYSROOT ${${target}_CMAKE_SYSROOT} CACHE STRING "")
  set(BUILTINS_${target}_CMAKE_CXX_FLAGS "--target=${target}" CACHE STRING "")
  set(BUILTINS_${target}_CMAKE_C_FLAGS "--target=${target}" CACHE STRING "")
  set(BUILTINS_${target}_CMAKE_ASM_FLAGS "--target=${target}" CACHE STRING "")
  set(BUILTINS_${target}_CMAKE_SHARED_LINKER_FLAGS "" CACHE STRING "")
  set(BUILTINS_${target}_CMAKE_MODULE_LINKER_FLAGS "" CACHE STRING "")
  set(BUILTINS_${target}_CMAKE_EXE_LINKER_FLAGS "" CACHE STRING "")
  
  set(RUNTIMES_${target}_CMAKE_BUILD_TYPE Release CACHE STRING "")
  set(RUNTIMES_${target}_CMAKE_SYSTEM_NAME Linux CACHE STRING "")
  set(RUNTIMES_${target}_CMAKE_SYSROOT ${${target}_CMAKE_SYSROOT} CACHE STRING "")
  set(RUNTIMES_${target}_CMAKE_CXX_FLAGS "--target=${target}" CACHE STRING "")
  set(RUNTIMES_${target}_CMAKE_C_FLAGS "--target=${target}" CACHE STRING "")
  set(RUNTIMES_${target}_CMAKE_ASM_FLAGS "--target=${target}" CACHE STRING "")
  set(RUNTIMES_${target}_CMAKE_SHARED_LINKER_FLAGS "" CACHE STRING "")
  set(RUNTIMES_${target}_CMAKE_MODULE_LINKER_FLAGS "" CACHE STRING "")
  set(RUNTIMES_${target}_CMAKE_EXE_LINKER_FLAGS "" CACHE STRING "")

  set(RUNTIMES_${target}_LLVM_ENABLE_RUNTIMES "compiler-rt;libcxx;libcxxabi;libunwind" CACHE STRING "")
  set(RUNTIMES_${target}_LLVM_ENABLE_RTTI ON CACHE BOOL "")
  set(RUNTIMES_${target}_LLVM_ENABLE_EH ON CACHE BOOL "")
  set(RUNTIMES_${target}_LLVM_INCLUDE_DOCS OFF CACHE BOOL "")
  set(RUNTIMES_${target}_LLVM_INCLUDE_EXAMPLES OFF CACHE BOOL "")
  set(RUNTIMES_${target}_LLVM_ENABLE_ASSERTIONS OFF CACHE BOOL "")

  set(RUNTIMES_${target}_COMPILER_RT_USE_BUILTINS_LIBRARY ON CACHE BOOL "")
  set(RUNTIMES_${target}_COMPILER_RT_INCLUDE_TESTS OFF CACHE BOOL "")
  set(RUNTIMES_${target}_COMPILER_RT_TERMINFO_LIB OFF CACHE BOOL "")
  set(RUNTIMES_${target}_COMPILER_RT_BUILD_SANITIZERS OFF CACHE BOOL "")
  set(RUNTIMES_${target}_COMPILER_RT_BUILD_XRAY OFF CACHE BOOL "")
  set(RUNTIMES_${target}_COMPILER_RT_BUILD_CRT ON CACHE BOOL "")
  set(RUNTIMES_${target}_COMPILER_RT_BUILD_PROFILE OFF CACHE BOOL "")
  set(RUNTIMES_${target}_COMPILER_RT_BUILD_MEMPROF OFF CACHE BOOL "")
  set(RUNTIMES_${target}_COMPILER_RT_BUILD_ORC OFF CACHE BOOL "")
  set(RUNTIMES_${target}_COMPILER_RT_BUILD_GWP_ASAN OFF CACHE BOOL "")
  
#  set(RUNTIMES_${target}_LIBUNWIND_ENABLE_SHARED OFF CACHE BOOL "")
#  set(RUNTIMES_${target}_LIBUNWIND_INSTALL_LIBRARY OFF CACHE BOOL "")
  set(RUNTIMES_${target}_LIBUNWIND_USE_COMPILER_RT ON CACHE BOOL "")
#  set(RUNTIMES_${target}_LIBCXXABI_ENABLE_SHARED OFF CACHE BOOL "")
#  set(RUNTIMES_${target}_LIBCXXABI_ENABLE_STATIC_UNWINDER ON CACHE BOOL "")
#  set(RUNTIMES_${target}_LIBCXXABI_INSTALL_LIBRARY OFF CACHE BOOL "")
  set(RUNTIMES_${target}_LIBCXXABI_USE_COMPILER_RT ON CACHE BOOL "")
  set(RUNTIMES_${target}_LIBCXXABI_USE_LLVM_UNWINDER ON CACHE BOOL "")
#  set(RUNTIMES_${target}_LIBCXX_ENABLE_SHARED OFF CACHE BOOL "")
#  set(RUNTIMES_${target}_LIBCXX_ENABLE_STATIC_ABI_LIBRARY ON CACHE BOOL "")
  set(RUNTIMES_${target}_LIBCXX_USE_COMPILER_RT ON CACHE BOOL "")
  set(RUNTIMES_${target}_LLVM_TOOLS_DIR "${CMAKE_BINARY_DIR}/bin" CACHE BOOL "")

endmacro()

obs_add_target(${OBS_TARGET_SYS})

set(LLVM_TOOLCHAIN_TOOLS
  dsymutil
  llvm-ar
  #llvm-cov
  llvm-cxxfilt
  llvm-dlltool
  llvm-dwarfdump
  llvm-dwp
  llvm-ifs
  llvm-gsymutil
  llvm-lib
  #llvm-libtool-darwin
  #llvm-lipo
  #llvm-mt
  llvm-nm
  llvm-objcopy
  llvm-objdump
  llvm-otool
  #llvm-pdbutil
  #llvm-profdata
  #llvm-rc
  llvm-ranlib
  llvm-readelf
  llvm-readobj
  llvm-size
  llvm-strip
  #llvm-symbolizer
  #llvm-xray
  #sancov
  scan-build-py
  CACHE STRING "")

set(LLVM_DISTRIBUTION_COMPONENTS
  clang
  lld
  clang-apply-replacements
  clang-format
  clang-resource-headers
  clang-include-fixer
  #clang-refactor
  clang-scan-deps
  builtins
  runtimes
  ${LLVM_TOOLCHAIN_TOOLS}

  # LLVM
  cmake-exports
  llvm-headers
  llvm-libraries
  # Clang
  clang-cmake-exports
  clang-headers
  clang-libraries  
  CACHE STRING "")
