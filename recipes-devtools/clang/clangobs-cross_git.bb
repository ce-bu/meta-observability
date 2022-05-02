# https://llvm.org/docs/HowToCrossCompileLLVM.html

DEPENDS = "libxml2-native ninja-native virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}compilerlibs virtual/${TARGET_PREFIX}binutils gcc-cross-${TARGET_ARCH}"

inherit cmake pkgconfig cross
require clangobs.inc

SRC_URI += "\
        file://ClangObsCross.cmake \
        file://ClangObsCross-stage2.cmake \
        "
        
LLVM_PROJECTS ?= "clang"

package_prefix = "${base_prefix}/opt/obs/${PN}"

EXTRA_OECMAKE += "\
                  -DCMAKE_INSTALL_PREFIX=${package_prefix} \
                  -DOBS_HOST=${@get_llvm_host_arch(bb, d)} \
                  -DOBS_TARGET=${@get_llvm_target_arch(bb, d)} \
                  -DOBS_HOST_SYS=${HOST_SYS} \
                  -DOBS_TARGET_SYS=${TARGET_SYS} \
                  -D${TARGET_SYS}_CMAKE_SYSROOT=${STAGING_DIR_TARGET} \
                  -D${HOST_SYS}_CMAKE_SYSROOT=${STAGING_DIR_HOST} \
                  -DPython3_EXECUTABLE=${HOSTTOOLS_DIR}/python3 \
                  -DCMAKE_VERBOSE_MAKEFILE=ON \
                  -C ${WORKDIR}/ClangCross.cmake \
                  "

do_compile() {
   ninja stage2-distribution
}

do_install() {
   DESTDIR='${D}' cmake --build '${B}'  --target stage2-install-distribution-stripped
}

SYSROOT_DIRS += "${package_prefix}"

OECMAKE_RPATH:prepend = "${package_prefix}/lib:"

INSANE_SKIP_${PN} += "already-stripped"
