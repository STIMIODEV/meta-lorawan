DESCRIPTION = "Compact server for private LoRa networks"
HOMEPAGE = "https://gotthardp.github.io/lorawan-server/"
SECTION = "console/utils"
# https://github.com/joaohf/meta-erlang
DEPENDS = "erlang nodejs-native"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit useradd erlang

SRC_URI = "git://github.com/gotthardp/lorawan-server.git;branch=master \
   file://lorawan-server.service"
SRCREV = "b149b6643c27e212df00bc3a4639ce82e2e0672c"

S = "${WORKDIR}/git"

RDEPENDS_${PN} += "bash erlang erlang-compiler erlang-syntax-tools erlang-crypto \
    erlang-inets erlang-asn1 erlang-public-key erlang-ssl erlang-mnesia erlang-os-mon \
    erlang-xmerl"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--home-dir /var/lib/lorawan-server --create-home lorawan"

do_compile() {
    oe_runmake release
}

do_install() {
    mkdir -p ${D}${libdir}
    cp -r ${S}/_build/default/rel/lorawan-server ${D}${libdir}

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/lorawan-server.service ${D}${systemd_system_unitdir}/
}

CONFFILES_${PN} += "${libdir}/lorawan-server/releases/${PV}/sys.config"

inherit systemd

SYSTEMD_SERVICE_${PN} += "lorawan-server.service"
