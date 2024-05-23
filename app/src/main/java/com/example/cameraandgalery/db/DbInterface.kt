package com.example.cameraandgalery.db

import com.example.cameraandgalery.model.Gallery

interface DbInterface {
    fun addPhoto(gallery: Gallery)
    fun showPhoto():ArrayList<Gallery>
}