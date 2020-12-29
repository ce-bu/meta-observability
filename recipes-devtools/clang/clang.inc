DESCRIPTION = "The LLVM Compiler Infrastructure"
HOMEPAGE = "http://llvm.org"
LICENSE = "Apache-2.0-with-LLVM-exception"
SECTION = "devel"

MAJOR_VERSION = "9"
MINOR_VERSION = "0"
PATCH_VERSION = "1"

PV = "${MAJOR_VERSION}.${MINOR_VERSION}.${PATCH_VERSION}"


BRANCH = "release/${MAJOR_VERSION}.x"
SRCREV = "c1a0a213378a458fbea1a5c77b315c7dce08fd05"
SRC_URI = "git://github.com/llvm/llvm-project.git;branch=${BRANCH} \
           file://0006-llvm-TargetLibraryInfo-Undefine-libc-functions-if-th.patch;striplevel=2 \
           file://0007-llvm-allow-env-override-of-exe-path.patch;striplevel=2 \
           file://0001-AsmMatcherEmitter-sort-ClassInfo-lists-by-name-as-we.patch;striplevel=2 \
           "

LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=8a15a0759ef07f2682d2ba4b893c9afe"

S = "${WORKDIR}/git/llvm"

def get_llvm_arch(bb, d, arch_var):
    import re
    a = d.getVar(arch_var)
    if   re.match(r'(i.86|athlon|x86.64)$', a):         return 'X86'
    elif re.match(r'arm$', a):                          return 'ARM'
    elif re.match(r'armeb$', a):                        return 'ARM'
    elif re.match(r'aarch64$', a):                      return 'AArch64'
    elif re.match(r'aarch64_be$', a):                   return 'AArch64'
    elif re.match(r'mips(isa|)(32|64|)(r6|)(el|)$', a): return 'Mips'
    elif re.match(r'riscv(32|64)(eb|)$', a):            return 'RISCV'
    elif re.match(r'p(pc|owerpc)(|64)', a):             return 'PowerPC'
    else:
        raise bb.parse.SkipRecipe("Cannot map '%s' to a supported LLVM architecture" % a)

def get_llvm_host_arch(bb, d):
    return get_llvm_arch(bb, d, 'HOST_ARCH')

def get_llvm_target_arch(bb, d):
    return get_llvm_arch(bb, d, 'TARGET_ARCH')
