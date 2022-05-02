DESCRIPTION = "The LLVM Compiler Infrastructure"
HOMEPAGE = "http://llvm.org"
LICENSE = "Apache-2.0-with-LLVM-exception"
SECTION = "devel"

MAJOR_VERSION = "14"
MINOR_VERSION = "0"
PATCH_VERSION = "1"

PV = "${MAJOR_VERSION}.${MINOR_VERSION}.${PATCH_VERSION}"

BRANCH = "release/${MAJOR_VERSION}.x"
SRCREV = "c62053979489ccb002efe411c3af059addcb5d7d"
SRC_URI = "git://github.com/llvm/llvm-project.git;branch=${BRANCH};protocol=git \
           "
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=8a15a0759ef07f2682d2ba4b893c9afe"

S = "${WORKDIR}/git/llvm"

LLVM_PARALLEL_LINK_JOBS ??= "1"

EXTRA_OECMAKE += "\
                  -DLLVM_PARALLEL_LINK_JOBS=${LLVM_PARALLEL_LINK_JOBS} \
                  -G Ninja"
                  
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

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
