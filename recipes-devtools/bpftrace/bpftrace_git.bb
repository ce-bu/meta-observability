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

SRCREV = "74efdfac35b2e91bb1579e7b75cd47df7938fe4a"

S = "${WORKDIR}/git"

export LLVM_DIR = "${STAGING_DIR_TARGET}/opt/clang"


EXTRA_OECMAKE = " \
    -DCMAKE_BUILD_TYPE=Release \
    -DENABLE_MAN=OFF \
    -DPYTHON_CMD=${PYTHON} \
    -DCMAKE_EXE_LINKER_FLAGS=-fuse-ld=gold \
    -DBUILD_TESTING=OFF \
"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
