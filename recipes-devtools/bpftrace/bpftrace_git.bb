SUMMARY = "bpftrace"
HOMEPAGE = "https://github.com/iovisor/bpftrace"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

inherit cmake python3native

DEPENDS += "bison-native \
            flex-native \
            elfutils \
            bcc \
            clang \
            "

OECMAKE_RPATH = "/opt/clang/lib"

RDEPENDS_${PN} = "clang-libclang"

SRC_URI = "git://github.com/iovisor/bpftrace \
           "

SRCREV = "9f7ee99b5443c0c37fd6813c203620ea7a78b5e3"

S = "${WORKDIR}/git"

export LLVM_DIR = "${STAGING_DIR_TARGET}/opt/clang"


EXTRA_OECMAKE = " \
    -DCMAKE_BUILD_TYPE=Release \
    -DENABLE_MAN=OFF \
    -DPYTHON_CMD=${PYTHON} \
    -DCMAKE_EXE_LINKER_FLAGS=-fuse-ld=gold \
"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
