# https://llvm.org/docs/HowToCrossCompileLLVM.html

DEPENDS = "libffi libxml2 zlib libedit ninja-native clang-native"

inherit cmake pkgconfig cross
require clang.inc


LLVM_TARGETS ?= "BPF;${@get_llvm_target_arch(bb, d)}"
LLVM_PROJECTS ?= "clang"

PN = "clang-cross-${TARGET_ARCH}"

CLANG_DIR_NATIVE = "${STAGING_DIR_NATIVE}/clang-native-${PV}"
EXTRA_OECMAKE += "\
                  -DCMAKE_INSTALL_PREFIX=${STAGING_DIR_NATIVE}/opt/${PN} \
                  -DLLVM_ENABLE_EXPENSIVE_CHECKS=OFF \
                  -DPYTHON_EXECUTABLE=${HOSTTOOLS_DIR}/python3 \
                  -DLLVM_ENABLE_PROJECTS='${LLVM_PROJECTS}' \
                  -DCMAKE_BUILD_TYPE=Release \
                  -DCMAKE_CROSSCOMPILING:BOOL=ON \
                  -DLLVM_TABLEGEN=${CLANG_DIR_NATIVE}/bin/llvm-tblgen \
                  -DCLANG_TABLEGEN=${CLANG_DIR_NATIVE}/bin/clang-tblgen \
                  -DLLVM_CONFIG_PATH=${CLANG_DIR_NATIVE}/bin/llvm-config \
                  -DLLVM_TARGETS_TO_BUILD='${LLVM_TARGETS}' \
                  -DLLVM_TARGET_ARCH=${@get_llvm_target_arch(bb, d)} \
                  -DLLVM_DEFAULT_TARGET_TRIPLE=${TARGET_SYS} \
                  -G Ninja"
                  
FILES_${PN} += "${D}/${STAGING_DIR_NATIVE}/opt/${PN}"

SYSROOT_DIRS += "${STAGING_DIR_NATIVE}/opt/${PN}"

do_install() {
   install -d ${D}${STAGING_DIR_NATIVE}/opt/${PN}
   DESTDIR='${D}' cmake --build '${B}'  --target install
    for f in `find ${D}/${STAGING_DIR_NATIVE}/opt/${PN}/bin -executable -type f -not -type l`; do
        test -n "`file -b $f|grep -i ELF`" && ${STRIP} $f
        echo "stripped $f"
    done
   
}

INSANE_SKIP_${PN} += "already-stripped"
