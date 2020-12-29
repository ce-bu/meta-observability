# meta-observability

This Yocto layer enables support for performance analysis (Perf, BPF, Hyper-V).

- [Overview](#overview)
- [Setup](#setup)
- [Building the image](#building-the-image)
- [Testing](#testing)

## Overview

Current features:
- compile GCC and system with frame pointers enabled so we can collect proper stack traces.
- **clang-cross** recipe to build the CLang cross compiler needed for BPF programs.
- [BPF Compiler Collection](https://github.com/iovisor/bcc)
- **bpftool** and **libbpf** from Linux kernel.


I used [Yocto](https://www.yoctoproject.org/)  **gatesgarth** branch. 
The build host Ubuntu 20. 
I tested the OS image in a Hyper-V machine.

## Setup

Create a working folder and checkout Yocto **gatesgarth** branch: 
```
sudo mkdir -p /yocto
sudo chmod 0777 /yocto
cd /yocto
git clone -b gatesgarth git://git.yoctoproject.org/poky.git
```

This layer depends on [meta-openembedded layer](https://github.com/openembedded/meta-openembedded.git). To add this layer to the project:
```
cd /yocto
git clone -b gatesgarth https://github.com/openembedded/meta-openembedded.git
```

Clone [meta-observability](https://github.com/ce-bu/meta-observability.git) layer:
```
cd /yocto
git clone https://github.com/ce-bu/meta-observability.git
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
git clone -b v5.8/standard/base git://git.yoctoproject.org/linux-yocto
git clone -b yocto-5.8 git://git.yoctoproject.org/yocto-kernel-cache
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
SRC_URI_pn-linux-yocto = "git:///yocto/linux-yocto;protocol=file;name=machine;branch=v5.8/standard/base; \
                          git:///yocto/yocto-kernel-cache;protocol=file;type=kmeta;name=meta;branch=yocto-5.8;destsuffix=${KMETA}"
SRCREV_meta_genericx86-64 = "${AUTOREV}"
SRCREV_machine_genericx86-64 = "${AUTOREV}"
KERNEL_VERSION_SANITY_SKIP="1"

# Use systemd
DISTRO_FEATURES_append = " systemd"
VIRTUAL-RUNTIME_init_manager = "systemd"
DISTRO_FEATURES_BACKFILL_CONSIDERED = "sysvinit"

# Add BPF tools
IMAGE_INSTALL_append += "bpftool bcc"

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

You can attach **poky.vhdx** to a Hyper-V machine. You need to disable secure boot.

On Ubuntu **qemu-img** utility is part of **qemu-tools** package:
```
sudo apt install qemu-tools
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
