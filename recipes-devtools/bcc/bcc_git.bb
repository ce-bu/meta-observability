SUMMARY = "BPF Compiler Collection (BCC)"
HOMEPAGE = "https://github.com/iovisor/bcc"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=e3fc50a88d0a364313df4b21ef20c29e"

inherit cmake python3native

DEPENDS += "bison-native \
            flex-native \
            elfutils \
            clang \
            luajit \
            iputils \
            flex \
            "
RDEPENDS_${PN} += "bash python3 python3-core python3-setuptools xz"

SRC_URI = "git://github.com/iovisor/bcc \
          file://disable_install_layout_debian.patch \
           "

SRCREV = "297512a31ecc9ceebf79bda169350dace0f36757"

S = "${WORKDIR}/git"

export LLVM_DIR = "${STAGING_DIR_TARGET}/opt/clang"
    
OECMAKE_RPATH = "/opt/clang/lib"

EXTRA_OECMAKE = " \
    -DCMAKE_BUILD_TYPE=Release \
    -DENABLE_LLVM_SHARED=ON \
    -DENABLE_MAN=OFF \
    -DPYTHON_CMD=${PYTHON} \
    -DCMAKE_EXE_LINKER_FLAGS=-fuse-ld=gold \
"

FILES_${PN} += "${PYTHON_SITEPACKAGES_DIR}"

do_install_append() {
     find ${D}${datadir} -type f -exec sed -i {} -e 's,#!/usr/bin/python,#!/usr/bin/env python3,' \;
}

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
