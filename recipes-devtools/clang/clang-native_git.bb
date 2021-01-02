DEPENDS = "ninja-native"

inherit cmake pkgconfig native
require clang.inc


LLVM_TARGETS ?= "${@get_llvm_host_arch(bb, d)}"
LLVM_PROJECTS ?= "clang"

package_prefix = "${base_prefix}/opt/clang-native"

EXTRA_OECMAKE += "\
                  -DCMAKE_INSTALL_PREFIX=${package_prefix}\
                  -DLLVM_TARGETS_TO_BUILD='${LLVM_TARGETS}' \
                  -DPYTHON_EXECUTABLE=${HOSTTOOLS_DIR}/python3 \
                  -DLLVM_ENABLE_PROJECTS='${LLVM_PROJECTS}' \
                  "

OECMAKE_RPATH_prepend = "${package_prefix}/lib:"

do_install_append() {
    install -Dm 0755 ${B}/bin/clang-tblgen ${D}${package_prefix}/bin/clang-tblgen
    install -Dm 0755 ${B}/bin/llvm-tblgen ${D}${package_prefix}/bin/llvm-tblgen
    for f in `find ${D}${package_prefix}/bin -executable -type f -not -type l`; do
        test -n "`file -b $f|grep -i ELF`" && ${STRIP} $f
        echo "stripped $f"
    done
    
}

SYSROOT_DIRS_NATIVE += "${package_prefix}"

INSANE_SKIP_${PN} += "already-stripped"

