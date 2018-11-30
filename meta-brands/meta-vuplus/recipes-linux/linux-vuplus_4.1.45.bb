SUMMARY = "Linux kernel for ${MACHINE}"
LICENSE = "GPLv2"
SECTION = "kernel"

MODULE = "linux-4.1.45"

MACHINE_KERNEL_PR_append = "oea4.2-r1"

inherit kernel machine_kernel_pr

KSRC_VER = "4.1-1.17"

SRC_URI[md5sum] = "7b5617dc7ec01ff4347b5b50d9d46f0f"
SRC_URI[sha256sum] = "18ecc79522f6b975c96cca7975eb31db93d2e8cfea6755451bba0a1a28feb0bd"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

SRC_URI += "http://archive.vuplus.com/download/kernel/stblinux-${KSRC_VER}.tar.bz2 \
    file://defconfig \
    file://linux_dvb_adapter.patch \
    file://linux_dvb-core.patch \
    file://linux_4_1_45_dvbs2x.patch \
    file://bcmsysport_4_1_45.patch \
    file://linux_usb_hub.patch \
    file://kernel-add-support-for-gcc6.patch \
    file://0001-Support-TBS-USB-drivers-for-4.1-kernel.patch \
    file://0001-TBS-fixes-for-4.1-kernel.patch \
    file://0001-STV-Add-PLS-support.patch \
    file://0001-STV-Add-SNR-Signal-report-parameters.patch \
    file://blindscan2.patch \
    file://0001-stv090x-optimized-TS-sync-control.patch \
    file://kernel-add-support-for-gcc7.patch \
    "

S = "${WORKDIR}/linux"
B = "${WORKDIR}/build"

export OS = "Linux"
KERNEL_IMAGETYPE = "zImage"
KERNEL_OBJECT_SUFFIX = "ko"
KERNEL_IMAGEDEST = "tmp"
KERNEL_OUTPUT = "arch/${ARCH}/boot/${KERNEL_IMAGETYPE}"

FILES_kernel-image = "/${KERNEL_IMAGEDEST}/zImage"

kernel_do_install_append() {
        install -d ${D}/${KERNEL_IMAGEDEST}
        install -m 0755 ${KERNEL_OUTPUT} ${D}/${KERNEL_IMAGEDEST}
}

kernel_do_compile() {
        unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS MACHINE
        oe_runmake ${KERNEL_IMAGETYPE_FOR_MAKE} ${KERNEL_ALT_IMAGETYPE} CC="${KERNEL_CC}" LD="${KERNEL_LD}"
        if test "${KERNEL_IMAGETYPE_FOR_MAKE}.gz" = "${KERNEL_IMAGETYPE}"; then
                gzip -9c < "${KERNEL_IMAGETYPE_FOR_MAKE}" > "${KERNEL_OUTPUT}"
        fi
}

pkg_postinst_kernel-image () {
        if [ -d /proc/stb ] ; then
                dd if=/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE} of=/dev/${MTD_KERNEL}
        fi
        rm -f /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}
        true
}

pkg_postrm_kernel-image () {
}

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-vuplus-${KV}:"

do_rm_work() {
}

# extra tasks
addtask kernel_link_images after do_compile before do_install
