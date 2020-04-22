# This is the xen-tools set of programs from Ubuntu, whose name unfortunately
# conflicts with the xen-tools package in yocto.

SUMMARY = "Xen hypervisor extra tools"
DESCRIPTION = "Helper tools for using the Xen hypervisor"
HOMEPAGE = "https://github.com/xen-tools/xen-tools"
SECTIONS = "console/tools"

SRCREV = "7c8d466a536ccbd45bcf9393f4345e1578b2fc94"
PV = "4.8"

XEN_TOOLS_BRANCH = "master"

SRC_URI = " \
    git://github.com/xen-tools/xen-tools.git;branch=${XEN_TOOLS_BRANCH} \
    "
S = "${WORKDIR}/git"

LICENSE = "Artistic-1.0 | GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=250cdeda9811e1f554094a6f1dd179fc"

# For pod2man
DEPENDS = "perl-native"

RDEPENDS_${PN} = "xen-tools perl \
	debootstrap \
	perl-module-english \
	perl-module-tie-hash-namedcapture \
	perl-module-digest-md5 \
	perl-module-env \
	perl-module-file-temp \
	perl-module-pod-text \
	libfile-slurp-perl \
	libdata-validate-uri-perl \
	libdata-validate-ip-perl \
	libdata-validate-domain-perl \
	libtext-template-perl \
	libconfig-inifiles-perl \
	libfile-which-perl \
	libterm-ui-perl \
	perl-module-deprecate \
	perl-module-params-check \
	libterm-readline-perl \
	libsort-versions-perl \
	"

inherit perl-version
inherit cpan-base

# Due to a race, /etc/xen-tools/roles.d can get created (with a silent failure)
# after the files are copied into it.  So force the install to be serial.
PARALLEL_MAKEINSTALL = ""

do_compile() {
	oe_runmake manpages || die "make failed"
}

do_install() {
	oe_runmake install DESTDIR="${D}"
	mkdir -p ${D}/${libdir}/perl5/${@get_perl_version(d)}
	mv ${D}/usr/share/perl5/Xen ${D}/${libdir}/perl5/${@get_perl_version(d)}
	rmdir ${D}/usr/share/perl5
}

FILES_${PN} += " \
	${datadir}/bash-completion/completions/xen-tools \
	${libdir}/perl5/${@get_perl_version(d)}/Xen \
	${datadir}/xen-tools \
	"
