SUMMARY = "bpftrace"
HOMEPAGE = "https://github.com/iovisor/bpftrace"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

inherit cmake python3native

DEPENDS += "bison-native \
            flex-native \
            elfutils \
            bcc \
            systemtap \
            clangobs \
            binutils \
            libcereal \
            "

OECMAKE_RPATH = "/opt/clang/lib"

RDEPENDS_${PN} = "clangobs-libclang"

SRC_URI = "git://github.com/iovisor/bpftrace \
           "

SRCREV = "e05e7a419c1afb1bf38117e74d6c7970d9d033f7"

S = "${WORKDIR}/git"

export LLVM_DIR = "${STAGING_DIR_TARGET}/opt/obs/clang"

export LLVM_REQUESTED_VERSION = "14"

EXTRA_OECMAKE = " \
    -DCMAKE_BUILD_TYPE=Release \
    -DENABLE_LLVM_SHARED=ON \
    -DENABLE_MAN=OFF \
    -DPYTHON_CMD=${PYTHON} \
    -DBUILD_TESTING=OFF \
    -DCMAKE_PREFIX_PATH=${LLVM_DIR} \
"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
