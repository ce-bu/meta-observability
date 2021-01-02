# https://llvm.org/docs/HowToCrossCompileLLVM.html

DEPENDS = "libffi libxml2 zlib libedit ninja-native clang-native"

inherit cmake pkgconfig
require clang.inc

LLVM_TARGETS ?= "BPF;${@get_llvm_target_arch(bb, d)}"
LLVM_PROJECTS ?= "clang"

PACKAGES =+ "${PN}-libclang ${PN}-libclang-dev"

CLANG_DIR_NATIVE = "${STAGING_DIR_NATIVE}/opt/clang-native"

package_prefix = "/opt/${PN}" 

EXTRA_OECMAKE += "\
                  -DCMAKE_INSTALL_PREFIX=${package_prefix} \
                  -DPYTHON_EXECUTABLE=${HOSTTOOLS_DIR}/python3 \
                  -DLLVM_ENABLE_PROJECTS='${LLVM_PROJECTS}' \
                  -DCMAKE_CROSSCOMPILING:BOOL=ON \
                  -DLLVM_TABLEGEN=${CLANG_DIR_NATIVE}/bin/llvm-tblgen \
                  -DCLANG_TABLEGEN=${CLANG_DIR_NATIVE}/bin/clang-tblgen \
                  -DLLVM_CONFIG_PATH=${CLANG_DIR_NATIVE}/bin/llvm-config \
                  -DLLVM_TARGETS_TO_BUILD='${LLVM_TARGETS}' \
                  -DLLVM_ENABLE_RTTI=ON \
                  -DLLVM_ENABLE_FFI=ON \
                  -DLLVM_BUILD_LLVM_DYLIB=ON \
                  -DLLVM_LINK_LLVM_DYLIB=ON \
                  "                  

do_install() {
    DESTDIR='${D}' cmake --build '${B}'  --target install
    for f in `find ${D}/${package_prefix}/bin -executable -type f -not -type l`; do
        test -n "`file -b $f|grep -i ELF`" && ${STRIP} $f
        echo "stripped $f"
    done
    rm -rf ${D}${package_prefix}/share/man
    rm -rf ${D}${package_prefix}/share/clang
    ${CLANG_DIR_NATIVE}/bin/llvm-strip --strip-debug ${D}${package_prefix}/lib/lib*.a
}

SYSROOT_DIRS += "${package_prefix}"

FILES_${PN} += "\
   ${package_prefix}/bin/* \
   ${package_prefix}/share/scan-* \
   ${package_prefix}/share/opt-viewer/* \
   ${package_prefix}/lib/libLTO.so.* \
   ${package_prefix}/lib/libRemarks.so.* \
   ${package_prefix}/lib/libLLVM-${MAJOR_VERSION}.so \
"

FILES_${PN}-dev += "\
   ${package_prefix}/include/* \
   ${package_prefix}/lib/clang/* \
   ${package_prefix}/lib/cmake/* \
   ${package_prefix}/libexec/* \
   ${package_prefix}/lib/libLTO.so \
   ${package_prefix}/lib/libRemarks.so \
   ${package_prefix}/lib/libLLVM.so \
   ${package_prefix}/lib/libLLVM-${PV}.so \
  "

FILES_${PN}-staticdev += "\
   ${package_prefix}/lib/*.a \
"


FILES_${PN}-libclang-dev += "\
   ${package_prefix}/lib/libclang.so \
   ${package_prefix}/lib/libclang-cpp.so \
    "
    
FILES_${PN}-libclang += "\
   ${package_prefix}/lib/libclang.so.* \
   ${package_prefix}/lib/libclang-cpp.so.* \
    "                 
INSANE_SKIP_${PN} += "already-stripped"

