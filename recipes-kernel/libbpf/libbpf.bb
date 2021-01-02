SUMMARY = "libbpf"
DESCRIPTION = "libbpf"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
PROVIDES = "virtual/libbpf"



DEPENDS += "elfutils"

RDEPENDS_${PN}   += "elfutils glibc"

inherit kernelsrc kernel-arch manpages

do_populate_lic[depends] += "virtual/kernel:do_shared_workdir"

EXTRA_OEMAKE = '\
             V=1 \
             -C ${S}/tools/lib/bpf O=${B} \
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
    oe_runmake DESTDIR=${D} install_headers

    LIBBPF_VERSION=$(basename $(realpath ${B}/libbpf.so) | sed -e 's/libbpf[.]so[.]//')
    LIBBPF_VERSION_MAJOR=$(echo $LIBBPF_VERSION | cut -d . -f 1)

    install -m 0666 ${B}/bpf_helper_defs.h ${D}/${includedir}/bpf
    install -d ${D}/${libdir}

    install -m 0755 ${B}/libbpf.so.${LIBBPF_VERSION} ${D}/${libdir}
    install  ${B}/libbpf.a ${D}/${libdir}
    install -d ${D}${libdir}/pkgconfig    
    install -m 0644 ${B}/libbpf.pc ${D}/${libdir}/pkgconfig
    sed -i -e "s,^libdir.*,libdir=${libdir}," ${D}${libdir}/pkgconfig/libbpf.pc
    cd ${D}/${libdir}
    ln -sf libbpf.so.${LIBBPF_VERSION} libbpf.so.${LIBBPF_VERSION_MAJOR}
    ln -sf libbpf.so.${LIBBPF_VERSION} libbpf.so

}

do_configure[depends] += "virtual/kernel:do_shared_workdir"

do_compile() {
    oe_runmake
}


python do_package_prepend() {
    d.setVar('PKGV', d.getVar("KERNEL_VERSION", True).split("-")[0])
}


PACKAGE_ARCH = "${MACHINE_ARCH}"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
