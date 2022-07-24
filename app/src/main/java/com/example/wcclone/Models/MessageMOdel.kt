package com.example.wcclone.Models

class MessageMOdel {
    var uID: String? = null
    var message: String? = null
    var messageID: String? = null
    var timestamp: Long? = null

    constructor(uID: String?, message: String?, timestamp: Long?) {
        this.uID = uID
        this.message = message
        this.timestamp = timestamp
    }

    constructor(uID: String?, message: String?) {
        this.uID = uID
        this.message = message
    }

    constructor() {}

    fun getuID(): String? {
        return uID
    }

    fun setuID(uID: String?) {
        this.uID = uID
    }
}