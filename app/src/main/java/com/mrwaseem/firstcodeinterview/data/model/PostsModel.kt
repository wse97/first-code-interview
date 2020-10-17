package com.mr.waseem.firstcode.Data.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts_table")
open class PostsModel {
    @PrimaryKey
    var id : Int= 0
    var title :String ?= null
    var image :String ?= null;
}