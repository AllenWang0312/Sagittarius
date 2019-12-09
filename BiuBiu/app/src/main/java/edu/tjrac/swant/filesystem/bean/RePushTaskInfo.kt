package edu.tjrac.swant.filesystem.bean

class RePushTaskInfo (){
    var fromPath: String? = ""
    var toPath: String? = ""
    var checked = true

    constructor(fromPath: String?, toPath: String?) : this() {
        this.fromPath = fromPath
        this.toPath = toPath
        checked=true
    }
}