# https://llvm.org/docs/HowToCrossCompileLLVM.html

DEPENDS = "libxml2-native ninja-native clangobs-native"

inherit cmake pkgconfig
require clangobs.inc

SRC_URI += "\
        file://ClangObs.cmake \
        file://01-setoevendor.patch \
        "

PACKAGES =+ "${PN}-libclang ${PN}-libclang-dev"

RDEPENDS:${PN} += "libxml2 zlib ncurses"

CLANG_DIR_NATIVE = "${STAGING_DIR_NATIVE}/opt/obs/clang-native"
CLANG_BINDIR_NATIVE = "${CLANG_DIR_NATIVE}/bin"

package_prefix = "/opt/obs/clang"

export TUNE_CCARGS = ""

EXTRA_OECMAKE += "\
                  -DCMAKE_INSTALL_PREFIX=${package_prefix} \
                  -DPython3_EXECUTABLE=${HOSTTOOLS_DIR}/python3 \
                  -DLLVM_TABLEGEN=${CLANG_DIR_NATIVE}/bin/llvm-tblgen \
                  -DCLANG_TABLEGEN=${CLANG_DIR_NATIVE}/bin/clang-tblgen \
                  -DLLVM_CONFIG_PATH=${CLANG_DIR_NATIVE}/bin/llvm-config \
                  -DLLVM_USE_LINKER=gold \
                  -DCMAKE_RANLIB=${CLANG_BINDIR_NATIVE}/llvm-ranlib \
                  -DCMAKE_AR=${CLANG_BINDIR_NATIVE}/llvm-ar \
                  -DCMAKE_NM=${CLANG_BINDIR_NATIVE}/llvm-nm \
                  -DCMAKE_STRIP=${CLANG_BINDIR_NATIVE}/llvm-strip \
                  -DCMAKE_SYSROOT=${STAGING_DIR_TARGET} \
                  -DLLVM_TARGET_ARCH="${@get_llvm_target_arch(bb, d)}" \
                  -DCOMPILER_RT_DEFAULT_TARGET_ARCH="${@d.getVar('TARGET_ARCH')}" \
                  -C ${WORKDIR}/ClangObs.cmake \
                  "


do_compile() {
   ninja distribution
}

do_install() {
   DESTDIR='${D}' cmake --build '${B}'  --target install-distribution-stripped
   ${CLANG_DIR_NATIVE}/bin/llvm-strip --strip-debug ${D}${package_prefix}/lib/lib*.a
}

SYSROOT_DIRS += "${package_prefix}"

FILES:${PN} += "\
   ${package_prefix}/bin/* \   
   ${package_prefix}/include/* \
   ${package_prefix}/lib/clang/* \
   ${package_prefix}/lib/cmake/* \
   ${package_prefix}/lib/libear/* \
   ${package_prefix}/lib/libscanbuild/* \
   ${package_prefix}/lib/*.so \
   ${package_prefix}/lib/*.so.* \
   ${package_prefix}/libexec/* \
   ${package_prefix}/share/* \
  "

FILES:${PN}-staticdev += "\
   ${package_prefix}/lib/*.a \
   ${package_prefix}/lib/linux/*.a \
   ${package_prefix}/lib/clang/14.0.1/lib/linux/*.a \
"

FILES:${PN}-libclang += "\
   ${package_prefix}/lib/libclang.so.* \
   ${package_prefix}/lib/libclang-cpp.so.* \
    "      

FILES:${PN}-libclang-dev += "\
   ${package_prefix}/lib/libclang.so.* \
   ${package_prefix}/lib/libclang-cpp.so.* \
    "      

INSANE_SKIP:${PN} += "already-stripped dev-so file-rdeps"
