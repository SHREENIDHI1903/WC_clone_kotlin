package com.example.wcclone.Models

class Users {
    var profilePic: String? = null
    var userName: String? = null
    var mail: String? = null
    var password: String? = null
    var userId: String? = null
    var lastMesssage: String? = null
    var status: String? = null

    constructor() {}
    constructor(
        profilePic: String?,
        userName: String?,
        password: String?,
        userId: String?,
        lastMesssage: String?,
        status: String?,
        mail: String?
    ) {
        this.profilePic = profilePic
        this.userName = userName
        this.password = password
        this.userId = userId
        this.lastMesssage = lastMesssage
        this.status = status
        this.mail = mail
    }

    constructor(userName: String?, mail: String?, password: String?) {
        this.userName = userName
        this.mail = mail
        this.password = password
    }
}