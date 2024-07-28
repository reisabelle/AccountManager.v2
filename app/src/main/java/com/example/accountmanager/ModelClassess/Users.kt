package com.example.accountmanager.ModelClassess

class Users {
    private var email: String = ""
    private var imageUrl: String = ""
    private var phone: String = ""
    private var username: String = ""


    constructor()

    constructor(email: String, imageUrl: String, phone: String, username: String) {
        this.email = email
        this.imageUrl = imageUrl
        this.phone = phone
        this.username = username
    }

    fun getName(): String?{
        return username
    }

    fun setName(username: String){
        this.username = username
    }

    fun getEmail(): String?{
        return email
    }

    fun setEmail(email: String){
        this.email = email
    }

    fun getImgUrl(): String?{
        return imageUrl
    }

    fun setImgUrl(imageUrl: String){
        this.imageUrl = imageUrl
    }

    fun getPhone(): String?{
        return phone
    }

    fun setPhone(imageUrl: String){
        this.phone = phone
    }
}