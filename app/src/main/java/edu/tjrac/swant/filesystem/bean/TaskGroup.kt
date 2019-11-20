package edu.tjrac.swant.filesystem.bean

class TaskGroup {
    var name: String? = ""
    var expand=true
    var checked=true

    var tasks: ArrayList<RePushTaskInfo>? = null
    get() {
        if(field==null){
            field= ArrayList()
        }
        return field
    }

    constructor(name: String?) {
        this.name = name
    }
}