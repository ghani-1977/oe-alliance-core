DESCRIPTION = "qcacld-2.0 module.bbclass mechanism."
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://Android.mk;md5=235cc8d87e0fb1c956be4af0d07074fb"
CAF_MIRROR = "https://www.codeaurora.org/cgit/external/wlan"

inherit module

COMPATIBLE_MACHINE = "osmio4k|osmio4kplus|xc7439"

SRC_URI = "${CAF_MIRROR}/qcacld-2.0/snapshot/qcacld-2.0-${PV}.tar.gz \
    file://qcacld-2.0-support.patch \
"

SRC_URI_append_xc7439 = "file://qcacld-2.0-support-xc7439.patch"

S = "${WORKDIR}/qcacld-2.0-${PV}"

EXTRA_OEMAKE_append_xc7439 = " CONFIG_CLD_HL_SDIO_CORE=y"

do_install() {
    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra
    install -m 0644 ${S}/wlan.ko ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra
}

python do_package_prepend() {
    d.appendVar('PKGV', '-')
    d.appendVar('PKGV', d.getVar("KERNEL_VERSION", True).split("-")[0])
}

SRC_URI[md5sum] = "36620d06d813895ead1fd123fc5fa056"
SRC_URI[sha256sum] = "2ad7dcaa6f80c67d4affeef39ee17a20e40bb763246d2b6c7b9dfaab99749e4d"
