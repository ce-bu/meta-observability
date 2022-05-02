SUMMARY = "libbpf"
DESCRIPTION = "libbpf"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"
PROVIDES = "virtual/libbpf"

LIBBPF_VERSION_MAJOR = "0"
LIBBPF_VERSION_MINOR = "0"
LIBBPF_VERSION_PATCH = "5"
LIBBPF_VERSION = "${LIBBPF_VERSION_MAJOR}.${LIBBPF_VERSION_MINOR}.${LIBBPF_VERSION_PATCH}"

DEPENDS += "elfutils"

inherit kernelsrc kernel-arch

do_populate_lic[depends] += "virtual/kernel:do_shared_workdir"

EXTRA_OEMAKE = '\
             V=1 \
             -C ${S}/tools/bpf/bpftool O=${B} \
             CROSS=${TARGET_PREFIX} \
             CC="${CC}" \
             LD="${LD}" \
             AR="${AR}" \
             ARCH=${ARCH} \
             EXTRA_CFLAGS="-O2 -Wl,--hash-style=gnu" \
             '


TARGET_CC_ARCH += "${LDFLAGS}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

B = "${WORKDIR}/${BPN}-${PV}"

do_install() {
    oe_runmake DESTDIR=${D} install
}

do_configure[depends] += "virtual/kernel:do_shared_workdir"

do_compile() {
    oe_runmake
}

FILES:${PN} += "/usr/share*"

python do_package:prepend() {
    d.setVar('PKGV', d.getVar("KERNEL_VERSION", True).split("-")[0])
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
