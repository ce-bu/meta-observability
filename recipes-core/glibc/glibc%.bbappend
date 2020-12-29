python fixoeconf() {
    tmp = d.getVar('EXTRA_OECONF')
    tmp = tmp.replace('--disable-debug','--enable-debug')    
    tmp = tmp.replace('--enable-stack-protector=strong', '--enable-stack-protector=no')
    tmp = tmp.replace('--enable-stackguard-randomization', '')
    tmp = tmp.replace('--enable-bind-now', '')    
    
    d.setVar('EXTRA_OECONF', tmp)
}

do_configure_append() {
         tmp=${@bb.build.exec_func("fixoeconf", d)}
}


