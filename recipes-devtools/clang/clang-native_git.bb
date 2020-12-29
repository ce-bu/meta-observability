

DEPENDS = "libffi libxml2 zlib libedit ninja-native"

inherit cmake pkgconfig native
require clang.inc


LLVM_TARGETS ?= "${@get_llvm_host_arch(bb, d)}"
LLVM_PROJECTS ?= "clang"


EXTRA_OECMAKE += "\
                  -DCMAKE_INSTALL_PREFIX=${STAGING_DIR_NATIVE}/clang-native-${PV} \
                  -DLLVM_ENABLE_EXPENSIVE_CHECKS=OFF \
                  -DLLVM_ENABLE_ASSERTIONS=OFF \
                  -DLLVM_TARGETS_TO_BUILD='${LLVM_TARGETS}' \
                  -DPYTHON_EXECUTABLE=${HOSTTOOLS_DIR}/python3 \
                  -DLLVM_ENABLE_PROJECTS='${LLVM_PROJECTS}' \
                  -DCMAKE_BUILD_TYPE=Release \
                  -G Ninja"


FILES_${PN} += "${D}/${STAGING_DIR_NATIVE}/clang-native-${PV}"

do_install_append() {
    install -Dm 0755 ${B}/bin/clang-tblgen ${D}/${STAGING_DIR_NATIVE}/clang-native-${PV}/bin/clang-tblgen
    install -Dm 0755 ${B}/bin/llvm-tblgen ${D}/${STAGING_DIR_NATIVE}/clang-native-${PV}/bin/llvm-tblgen
    for f in `find ${D}/${STAGING_DIR_NATIVE}/clang-native-${PV}/bin -executable -type f -not -type l`; do
        test -n "`file -b $f|grep -i ELF`" && ${STRIP} $f
        echo "stripped $f"
    done
    
}

SYSROOT_DIRS += "${STAGING_DIR_NATIVE}/clang-native-${PV}"

INSANE_SKIP_${PN} += "already-stripped"
