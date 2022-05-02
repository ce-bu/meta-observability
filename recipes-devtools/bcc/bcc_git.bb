SUMMARY = "BPF Compiler Collection (BCC)"
HOMEPAGE = "https://github.com/iovisor/bcc"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=e3fc50a88d0a364313df4b21ef20c29e"

inherit cmake python3native distutils3-base

DEPENDS += "bison-native \
            flex-native \
            elfutils \
            clangobs \
            luajit \
            iputils \
            flex \
            "
RDEPENDS:${PN} += "bash python3 python3-core python3-setuptools xz"

SRC_URI = "git://github.com/iovisor/bcc \
          file://disable_install_layout_debian.patch \
           "

SRCREV = "7231ddb2bca47655d31996a1e5a8f36bfef3f5a8"

S = "${WORKDIR}/git"

export LLVM_DIR = "${STAGING_DIR_TARGET}/opt/obs/clang"
    
OECMAKE_RPATH = "/opt/obs/clang/lib"

EXTRA_OECMAKE = " \
    -DCMAKE_BUILD_TYPE=Release \
    -DENABLE_LLVM_SHARED=ON \
    -DENABLE_MAN=OFF \
    -DENABLE_EXAMPLES=OFF \
    -DENABLE_TESTS=OFF \
    -DRUN_LUA_TESTS=OFF \
    -DPYTHON_CMD=${PYTHON} \
"


do_install:append() {
     find ${D}${datadir} -type f -exec sed -i {} -e 's,#!/usr/bin/python,#!/usr/bin/env python3,' \;
}

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
