# meta-observability

This Yocto layer enables support for performance analysis (Perf, BPF, BCC, bpftrace, Hyper-V).

- [Overview](#overview)
- [Setup](#setup)
- [Building the image](#building-the-image)
- [Testing](#testing)
- [Resources](#resources)
- [ToDo](#todo)

## Overview

Current features:
- [BPF Compiler Collection](https://github.com/iovisor/bcc)
- compile GCC and system with frame pointers enabled so we can collect proper stack traces.
- **clang** recipes to build the CLang cross/native/target compiler needed for BPF.
- [bpftrace](https://github.com/iovisor/bpftrace)
- **bpftool** and **libbpf** from Linux kernel.


I try to track [Yocto](https://www.yoctoproject.org/) and have a branch matching Yocto version. So far I have it working on:
- honister
- hardknott
- gatesgarth

The build host Ubuntu 20.10.  I tested the OS image in a Hyper-V machine (2nd gen).

## Setup

Create a working folder and checkout Yocto **gatesgarth** branch: 
```
sudo mkdir -p /yocto
sudo chmod 0777 /yocto
cd /yocto
git clone -b honister git://git.yoctoproject.org/poky.git
```

This layer depends on [meta-openembedded layer](https://github.com/openembedded/meta-openembedded.git). To add this layer to the project:
```
cd /yocto/poky
git clone -b honister https://github.com/openembedded/meta-openembedded.git
```

Clone [meta-observability](https://github.com/ce-bu/meta-observability.git) layer:
```
cd /yocto
git clone https://github.com/ce-bu/meta-observability.git
cd /yocto/poky
. oe-init-build-env
```

Edit the **/yocto/poky/build/conf/bblayers.conf** and add the required layers:
```
BBLAYERS ?= " \
  /yocto/poky/meta \
  /yocto/poky/meta-poky \
  /yocto/poky/meta-yocto-bsp \
  /yocto/poky/meta-openembedded/meta-oe \
  /yocto/meta-observability \
  "
```

Checkout the Yocto kernel and metadata. Note that this step is optional but it allows you to have custom kernel sources.
```
cd /yocto
git clone -b v5.15/standard/base git://git.yoctoproject.org/linux-yocto
git clone -b yocto-5.15 git://git.yoctoproject.org/yocto-kernel-cache
```


To enable performance tools edit your **local.conf** file:

```
# Set the machine
MACHINE = "genericx86-64"

# Enable extra features
EXTRA_IMAGE_FEATURES = "debug-tweaks dbg-pkgs dev-pkgs tools-sdk tools-debug tools-profile"

# Add kernel moduules
MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += "kernel-modules"

# Use a custom kernel (optional)
SRC_URI_pn-linux-yocto = "git:///yocto/linux-yocto;protocol=file;name=machine;branch=v5.15/standard/base; \
                          git:///yocto/yocto-kernel-cache;protocol=file;type=kmeta;name=meta;branch=yocto-5.15;destsuffix=${KMETA}"
SRCREV_meta_genericx86-64 = "${AUTOREV}"
SRCREV_machine_genericx86-64 = "${AUTOREV}"
KERNEL_VERSION_SANITY_SKIP="1"

# Use systemd
DISTRO_FEATURES_append = " systemd"
VIRTUAL-RUNTIME_init_manager = "systemd"
DISTRO_FEATURES_BACKFILL_CONSIDERED = "sysvinit"

# Add BPF tools
IMAGE_INSTALL_append += "bpftool bcc bpftrace"

# Add Hyper-V support (see meta-observability/recipes-kernel/linux/linux-yocto_%.bbappend )
MACHINE_FEATURES += "hyperv"

```

## Building the image

Open a command prompt and run:
```
cd /yocto/poky
source oe-init-build-env
# Adjust bblayers.conf and local.cond (see below), then run
bitbake core-image-minimal
```

Create an EFI disk image for genericx86:
```
wic create genericx86 -e core-image-minimal
```

Convert the image to **vhdx** format using **qemu-img** utility:
```
qemu-img convert -f raw -O vhdx genericx86.wks-202012290029-sda.direct poky.vhdx
```

You can attach **poky.vhdx** to a Hyper-V machine (Image MUST be 2nd gen). You need to disable secure boot.

On Ubuntu **qemu-img** utility is part of **qemu-tools** package:
```
sudo apt install qemu-utils
```

## Testing

Start some programs in background:
```
find / -exec stat {} \; 1>/dev/null 2>&1 &
while [ 1 ]; do : ; done &

```

Use tools from BCC:
```
/usr/share/bcc/tools/biolatency
```

Use perf to capture stack traces:
```
perf record -a -g -- sleep 4; perf script
```

## Resources

- [Brendan D. Gregg](http://www.brendangregg.com/overview.html) the performance tools grandmaster.
- [BPF Performance Tools](https://amzn.to/2OWoQQX) book by Brendan D. Gregg 
- [Linux Observability with BPF: Advanced Programming for Performance Analysis and Networking](https://www.amazon.ca/Linux-Observability-BPF-Programming-Performance/dp/1492050202) by David Calavera and Lorenzo Fontana is also very good.
- [XDP Tutorial Project](https://github.com/xdp-project/xdp-tutorial.git) is very well structured and teaches you the BPF with focus on XDP. 


## ToDo

- Create LLVM SDK
