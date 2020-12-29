FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

LICENSE = "MIT"

SRC_URI_append = "\
     ${@bb.utils.contains('MACHINE_FEATURES', 'hyperv', 'file://hyperv.cfg', '', d)} \
    "
