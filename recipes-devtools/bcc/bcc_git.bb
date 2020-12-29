SUMMARY = "BPF Compiler Collection (BCC)"
HOMEPAGE = "https://github.com/iovisor/bcc"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=e3fc50a88d0a364313df4b21ef20c29e"

inherit cmake python3native

DEPENDS += "bison-native \
            flex-native \
            elfutils \
            clang-cross-${TARGET_ARCH} \
            luajit \
            iputils \
            flex \
            "
RDEPENDS_${PN} = "python3"

RDEPENDS_${PN} += "bash python3 python3-core python3-setuptools xz"

SRC_URI = "git://github.com/iovisor/bcc \
          file://disable_install_layout_debian.patch \
           "

SRCREV = "297512a31ecc9ceebf79bda169350dace0f36757"

S = "${WORKDIR}/git"

export LLVM_DIR = "${STAGING_DIR_NATIVE}/opt/clang-cross-${TARGET_ARCH}"
    

EXTRA_OECMAKE = " \
              -DENABLE_MAN=OFF \
              -DPYTHON_CMD=${PYTHON} \
"

FILES_${PN} += "${PYTHON_SITEPACKAGES_DIR}"

do_install_append() {
     find ${D}${datadir} -type f -exec sed -i {} -e 's,#!/usr/bin/python,#!/usr/bin/env python3,' \;
}

